package com.walmart.ts.utils;

import java.util.Optional;

import com.walmart.ts.beans.SeatHold;
import com.walmart.ts.beans.SeatReservation;

/**
 * 
 * @author Haarthi Padmanabhan
 *
 *         Utility class to help validate ticket service api inputs
 * 
 */
public class TicketServiceUtils {

	/*
	 * Validate the input of the findAndHoldSeat service
	 */
	public static boolean validateSeatHoldInput(SeatHold seatHold, int numSeats, Optional<Integer> minLevel,
			Optional<Integer> maxLevel, String customerEmail) {
		if (numSeats <= 0) {
			seatHold.setId(-1);
			seatHold.setMessage(MessageConstants.REQ_NO_SEATS_INVALID);
			return false;
		} else if (customerEmail == null || customerEmail.trim().length() == 0) {
			seatHold.setId(-1);
			seatHold.setMessage(MessageConstants.EMAIL_NULL);
			return false;
		} else if (minLevel.isPresent() && maxLevel.isPresent() && minLevel.get() > maxLevel.get()) {
			seatHold.setId(-1);
			seatHold.setMessage(MessageConstants.INVALID_LEVELS);
			return false;
		}
		return true;
	}

	/*
	 * Validate the input of the reserveSeat service
	 */
	public static boolean validateReserveSeatInput(SeatReservation sr, int seatHoldId, String customerEmail) {
		if (seatHoldId <= 0) {
			sr.setMessage(MessageConstants.INVALID_HOLD_ID);
			sr.setId(-1);
			return false;
		} else if (customerEmail == null || customerEmail.trim().length() == 0) {
			sr.setMessage(MessageConstants.EMAIL_NULL);
			sr.setId(-1);
			return false;
		}
		return true;
	}
}
