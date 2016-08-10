/**
 * 
 */
package com.walmart.ts.beans;

import java.util.LinkedList;

/**
 * @author Haarthi Padmanabhan
 *
 */
public class SeatReservationRepository {
	private static final LinkedList<SeatReservation> reservedSeatList = new LinkedList<SeatReservation>();

	public static LinkedList<SeatReservation> getReservedSeatList() {
		return reservedSeatList;
	}
	
}
