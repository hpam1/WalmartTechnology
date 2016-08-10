/**
 * 
 */
package com.walmart.ts.service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.walmart.ts.beans.Seat;
import com.walmart.ts.beans.SeatHold;
import com.walmart.ts.beans.SeatHoldRepository;
import com.walmart.ts.beans.SeatReservation;
import com.walmart.ts.beans.SeatReservationRepository;
import com.walmart.ts.beans.SeatStatus;
import com.walmart.ts.beans.Venue;
import com.walmart.ts.utils.MessageConstants;
import com.walmart.ts.utils.SeatDAOUtil;
import com.walmart.ts.utils.TicketServiceUtils;
import com.walmart.ts.utils.VenueConstructor;

/**
 * @author Haarthi Padmanabhan
 * 
 * TicketService API implementation
 * 
 * 
 * TODO - estimated cost calculation
 */
public class TicketServiceImpl implements TicketService {
	
	private Venue venue;
	static Logger log = LogManager.getLogger(TicketServiceImpl.class);
	
	public TicketServiceImpl() {
		this.venue = VenueConstructor.getVenue();
	}

	/* 
	 * Count the number of seats available in the venue, optionally by a specific venue level
	 * 
	 * @param venueLevel - an optional parameter that specifies a venue level to look for seats
	 * @return the count of the available seats
	 */
	@Override
	public int numSeatsAvailable(Optional<Integer> venueLevel) {
		log.debug("In TicketServiceImpl numAvailableSeats service");
		HoldValidator.purgeExpiredHolds();
		if(venueLevel.isPresent()) {
			return SeatDAOUtil.countAvailSeatInLevel(venue, venueLevel.get());
		}
		return SeatDAOUtil.countAvailSeatInVenue(venue);
	}

	
	/*
	 * randomly find specific number of avaibale seats in the venue, optionally limited by level
	 * and place a hold on them 
	 * 
	 * @param numSeats - the number of seats to be held
	 * @param minLevel - an optional parameter specifying the minimum level to hold a seat
	 * @param maxLevel - an optional parameter specifying the maximum level to hold a seat
	 * @param customerEmail - the email id of the customer
	 * @return seatHold - with information about this seat hold request
	 */
	@Override
	public SeatHold findAndHoldSeats(int numSeats, Optional<Integer> minLevel, Optional<Integer> maxLevel,
			String customerEmail) {
		log.debug("In TicketServiceImpl findAndHoldSeats " + numSeats);
		// clear expired holds
		HoldValidator.purgeExpiredHolds();
	
		SeatHold seatHold = new SeatHold();
		seatHold.setCustomerEmail(customerEmail);
		
		//validate input
		log.debug("findAndHoldSeats:: Validate input");
		boolean validInput = TicketServiceUtils.validateSeatHoldInput(seatHold, numSeats, minLevel, maxLevel, customerEmail);
		if(!validInput) {
			return seatHold;
		}
		// get the available seats in the venue
		log.debug("findAndHoldSeats:: enquiring about the seats available in the venue");
		int numAvailSeats = SeatDAOUtil.countAvailSeatsWithinLevels(venue, minLevel, maxLevel);
		if(numAvailSeats >= numSeats) { //sufficient no of seats available
			log.debug("findAndHoldSeats:: sufficient number of seats available");
			List<Seat> heldSeatList = new LinkedList<Seat>();
			while(numSeats > 0) {
				// get a random available seat within the specified levels
				Seat seat = SeatDAOUtil.holdARandomSeat(venue, minLevel, maxLevel);
				if(seat != null) { 
					// add this seat to the hold list
					heldSeatList.add(seat);
					numSeats--;
				} else { // no seat available; might be assigned to other users
					log.debug("findAndHoldSeats:: " + MessageConstants.INSUFFICIENT_SEATS);
					//release previously held seats
					heldSeatList.forEach(st -> st.setStatus(SeatStatus.AVAILABLE));
					seatHold.setId(-1);
					seatHold.setMessage(MessageConstants.INSUFFICIENT_SEATS);
				}
			}
			seatHold.setHeldSeatList(heldSeatList);
			seatHold.setHoldStartTime(LocalDateTime.now());
			seatHold.setMessage(MessageConstants.SUCCESS_PROCESSING_REQUEST);
			log.debug("findAndHoldSeats:: " + MessageConstants.SUCCESS_PROCESSING_REQUEST);
		} else { // insufficient number of seats available
			log.debug("findAndHoldSeats:: " + MessageConstants.INSUFFICIENT_SEATS);
			seatHold.setId(-1);
			seatHold.setMessage(MessageConstants.INSUFFICIENT_SEATS);
		}
		return seatHold;
	}

	/* 
	 * Given a seat hold id, reserve the seats held by the customer
	 * 
	 * @param seatHoldId - the seat hold id
	 * @param customerEmail - the email id of the customer having the hold
	 * @return seat reservation information
	 */
	@Override
	public String reserveSeats(int seatHoldId, String customerEmail) {
		log.debug("In TicketServiceImpl reserveSeats " + seatHoldId + " " + customerEmail);
		//clear expired holds
		HoldValidator.purgeExpiredHolds();
		//validate input
		SeatReservation sr = new SeatReservation();
		sr.setCustomerEmail(customerEmail);
		boolean validInput = TicketServiceUtils.validateReserveSeatInput(sr, seatHoldId, customerEmail);
		if(!validInput) {
			return sr.toString();
		}
		
		// get the seathold matching the input
		log.debug("reserveSeats:: valid seat hold");
		SeatHold hold = SeatDAOUtil.getSeatHoldListFor(seatHoldId, customerEmail);
		if(hold != null) {
			sr.setEstimatedCost(hold.getTotalEstimatedCost());
			sr.setReservationTime(LocalDateTime.now());
			sr.setReservedSeatList(hold.getHeldSeatList());
			sr.getReservedSeatList().forEach(seat -> seat.setStatus(SeatStatus.RESERVED));
			sr.setMessage(MessageConstants.SUCCESS_PROCESSING_REQUEST);
			SeatHoldRepository.getSeatHoldList().remove(hold);
			SeatReservationRepository.getReservedSeatList().add(sr);
			log.debug("reserveSeats:: " + MessageConstants.SUCCESS_PROCESSING_REQUEST);
		} else { // invalid seat hold
			sr.setId(-1);
			sr.setMessage(MessageConstants.INVALID_SEAT_HOLD);
			log.debug("reserveSeats:: " + MessageConstants.INVALID_SEAT_HOLD);
		}
		return sr.toString();
	}

}
