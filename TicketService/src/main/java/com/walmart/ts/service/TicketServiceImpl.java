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
import com.walmart.ts.utils.SeatDAOUtil;
import com.walmart.ts.utils.VenueConstructor;

/**
 * @author Haarthi Padmanabhan
 * TODO - input validatio
 * 		  thread synchronization
 * 		  messages to constant files
 * 		  estimated cost calculation
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
		if(venueLevel.isPresent()) {
			return SeatDAOUtil.countAvailSeatInLevel(venue, venueLevel.get());
		}
		return SeatDAOUtil.countAvailSeatInVenue(venue);
	}

	
	/* (non-Javadoc)
	 * @see com.walmart.ts.service.TicketService#findAndHoldSeats(int, java.util.Optional, java.util.Optional, java.lang.String)
	 */
	@Override
	public SeatHold findAndHoldSeats(int numSeats, Optional<Integer> minLevel, Optional<Integer> maxLevel,
			String customerEmail) {
		HoldValidator.purgeExpiredHolds();
		int numAvailSeats = SeatDAOUtil.countAvailSeatsWithinLevels(venue, minLevel, maxLevel);
		SeatHold seatHold = new SeatHold();
		seatHold.setCustomerEmail(customerEmail);
		if(numAvailSeats >= numSeats) {
			List<Seat> heldSeatList = new LinkedList<Seat>();
			while(numSeats >= 0) {
				Seat seat = SeatDAOUtil.holdARandomSeat(venue, minLevel, maxLevel);
				if(seat != null) {
					heldSeatList.add(seat);
					numSeats--;
				} else {
					//TODO release previously held seats
					seatHold.setId(-1);
					seatHold.setMessage("Sorry We are unable process your request currently. Insufficient seats");
					return seatHold;
				}
			}
			seatHold.setHeldSeatList(heldSeatList);
			seatHold.setHoldStartTime(LocalDateTime.now());
			seatHold.setMessage("Success processing your hold request");
		} else {
			seatHold.setId(-1);
			seatHold.setMessage("Sorry We are unable process your request currently. Insufficient seats");
		}
		return seatHold;
	}

	/* (non-Javadoc)
	 * @see com.walmart.ts.service.TicketService#reserveSeats(int, java.lang.String)
	 */
	@Override
	public String reserveSeats(int seatHoldId, String customerEmail) {
		HoldValidator.purgeExpiredHolds();
		List<SeatHold> seatHoldList = SeatHoldRepository.getSeatHoldList().stream().filter(sh -> sh.getId() == seatHoldId 
				&& sh.getCustomerEmail().equals(customerEmail)).collect(Collectors.toList());
		SeatReservation sr = new SeatReservation();
		if(seatHoldList != null && seatHoldList.size() == 1) {
			SeatHold hold = seatHoldList.get(0);
			sr.setEstimatedCost(hold.getTotalEstimatedCost());
			sr.setReservationTime(LocalDateTime.now());
			sr.setReservedSeatList(hold.getHeldSeatList());
			sr.getReservedSeatList().forEach(seat -> seat.setStatus(SeatStatus.RESERVED));
			sr.setMessage("Success processing reservation request");
			SeatHoldRepository.getSeatHoldList().remove(hold);
			SeatReservationRepository.getReservedSeatList().add(sr);
		} else {
			sr.setId(-1);
			sr.setCustomerEmail(customerEmail);
			sr.setMessage("Sorry reservation request cannot be processed. Invalid input");
		}
		return sr.toString();
	}

}
