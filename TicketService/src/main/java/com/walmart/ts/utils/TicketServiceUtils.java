package com.walmart.ts.utils;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.walmart.ts.beans.SeatHold;
import com.walmart.ts.beans.SeatReservation;

/**
 * 
 * @author Haarthi Padmanabhan
 *
 * Utility class to help validate ticket service api inputs
 * 
 */
public class TicketServiceUtils {
	static Logger log = LogManager.getLogger(TicketServiceUtils.class);
	
	/*
	 * Validate the input of the findAndHoldSeat service
	 */
	public static boolean validateSeatHoldInput(SeatHold seatHold, int numSeats, Optional<Integer> minLevel, Optional<Integer> maxLevel, String customerEmail) {
		log.debug("In TicketServiceUtils:: validateSeatHoldInput");
		if(numSeats <= 0) {
			seatHold.setId(-1);
			seatHold.setMessage(MessageConstants.REQ_NO_SEATS_INVALID);
			log.debug("TicketServiceUtils:: validateSeatHoldInput" + MessageConstants.REQ_NO_SEATS_INVALID);
			return false;
		} else if(customerEmail == null || customerEmail.trim().length() == 0) {
			seatHold.setId(-1);
			seatHold.setMessage(MessageConstants.EMAIL_NULL);
			log.debug("TicketServiceUtils:: validateSeatHoldInput " + MessageConstants.EMAIL_NULL);
			return false;
		} else if(minLevel.isPresent() && maxLevel.isPresent() && minLevel.get() > maxLevel.get()) {
			seatHold.setId(-1);
			seatHold.setMessage(MessageConstants.INVALID_LEVELS);
			log.debug("TicketServiceUtils:: validateSeatHoldInput" + MessageConstants.INVALID_LEVELS);
			return false;
		}
		log.debug("TicketServiceUtils:: validateSeatHoldInput valid input");
		return true;
	}
	
	/*
	 * Validate the input of the reserveSeat service
	 */
	public static boolean validateReserveSeatInput(SeatReservation sr, int seatHoldId, String customerEmail) {
		log.debug("In TicketServiceUtils:: validateReserveSeatInput");
		if(seatHoldId <= 0) {
			log.debug("TicketServiceUtils:: validateReserveSeatInput " + MessageConstants.INVALID_HOLD_ID);
			sr.setMessage(MessageConstants.INVALID_HOLD_ID);
			sr.setId(-1);
			return false;
		} else if(customerEmail == null || customerEmail.trim().length() == 0) {
			log.debug("TicketServiceUtils:: validateReserveSeatInput " + MessageConstants.EMAIL_NULL);
			sr.setMessage(MessageConstants.EMAIL_NULL);
			sr.setId(-1);
			return false;
		}
		log.debug("TicketServiceUtils:: validateReserveSeatInput valid input");
		return true;
	}
}
