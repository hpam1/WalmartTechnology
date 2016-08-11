/**
 * 
 */
package com.walmart.ts.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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
 *         TicketService API implementation
 * 
 * 
 * 
 */
public class TicketServiceImpl implements TicketService {
	private Venue venue;
	
	public TicketServiceImpl() {
		this.venue = VenueConstructor.getVenue();
	}

	/*
	 * Count the number of seats available in the venue, optionally by a
	 * specific venue level
	 */
	@Override
	public int numSeatsAvailable(Optional<Integer> venueLevel) {
		HoldValidator.purgeExpiredHolds();

		if (venueLevel.isPresent()) {
			return SeatDAOUtil.countAvailSeatInLevel(venue, venueLevel.get());
		}
		return SeatDAOUtil.countAvailSeatInVenue(venue);
	}

	/*
	 * randomly find specific number of avaibale seats in the venue, optionally
	 * limited by level and place a hold on them
	 */
	@Override
	public SeatHold findAndHoldSeats(int numSeats, Optional<Integer> minLevel, Optional<Integer> maxLevel,
			String customerEmail) {
		HoldValidator.purgeExpiredHolds();

		SeatHold seatHold = new SeatHold();
		seatHold.setCustomerEmail(customerEmail); 
		try {
			// validate input
			boolean validInput = TicketServiceUtils.validateSeatHoldInput(seatHold, numSeats, minLevel, maxLevel,
				customerEmail);
			if (!validInput) {
				return seatHold;
			}

			// get the available seats in the venue
			int numAvailSeats = SeatDAOUtil.countAvailSeatsWithinLevels(venue, minLevel, maxLevel);
			if (numAvailSeats >= numSeats) { // sufficient no of seats available
				List<Seat> heldSeatList = new LinkedList<Seat>();
				BigDecimal cost = new BigDecimal("0");
				while (numSeats > 0) {
					// get a random available seat within the specified levels
					Seat seat = SeatDAOUtil.holdARandomSeat(venue, minLevel, maxLevel);
					if (seat != null) {
						heldSeatList.add(seat);
						cost = cost.add(seat.getPrice());
						numSeats--;
					} else { // no seat available; might be assigned to other users
						// release previously held seats
						heldSeatList.forEach(st -> st.setStatus(SeatStatus.AVAILABLE));
						seatHold.setId(-1);
						seatHold.setMessage(MessageConstants.INSUFFICIENT_SEATS);
						return seatHold;
					}
				}
				seatHold.setHeldSeatList(heldSeatList);
				seatHold.setHoldStartTime(LocalDateTime.now());
				seatHold.setTotalEstimatedCost(cost);
				seatHold.setMessage(MessageConstants.SUCCESS_PROCESSING_REQUEST);
				SeatHoldRepository.getSeatHoldList().add(seatHold);
			} else { // insufficient number of seats available
				seatHold.setId(-1);
				seatHold.setMessage(MessageConstants.INSUFFICIENT_SEATS);
			}
		} catch(Exception e) {
			e.printStackTrace();
			seatHold.setId(-1);
			seatHold.setMessage(MessageConstants.ERROR_PROCESSING_REQUEST);
		}
		return seatHold;
	}

	/*
	 * Given a seat hold id, reserve the seats held by the customer
	 * 
	 */
	@Override
	public String reserveSeats(int seatHoldId, String customerEmail) {
		HoldValidator.purgeExpiredHolds();

		// validate input
		SeatReservation sr = new SeatReservation();
		sr.setCustomerEmail(customerEmail);
		try {
			boolean validInput = TicketServiceUtils.validateReserveSeatInput(sr, seatHoldId, customerEmail);
			if (!validInput) {
				return sr.toString();
			}

			// get the seathold matching the input
			SeatHold hold = SeatDAOUtil.getSeatHoldListFor(seatHoldId, customerEmail);
			if (hold != null) {
				sr.setEstimatedCost(hold.getTotalEstimatedCost());
				sr.setReservationTime(LocalDateTime.now());
				sr.setReservedSeatList(hold.getHeldSeatList());
				sr.getReservedSeatList().forEach(seat -> seat.setStatus(SeatStatus.RESERVED));
				sr.setMessage(MessageConstants.SUCCESS_PROCESSING_REQUEST);
				SeatHoldRepository.getSeatHoldList().remove(hold);
				SeatReservationRepository.getReservedSeatList().add(sr);
			} else { // invalid seat hold
				sr.setId(-1);
				sr.setMessage(MessageConstants.INVALID_SEAT_HOLD);
			}
		} catch(Exception e) {
			e.printStackTrace();
			sr.setId(-1);
			sr.setMessage(MessageConstants.ERROR_PROCESSING_REQUEST);
		}
		return sr.toString();
	}

}
