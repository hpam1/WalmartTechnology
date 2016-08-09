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
	private static LinkedList<SeatReservation> reservedSeatList = new LinkedList<SeatReservation>();

	public static LinkedList<SeatReservation> getReservedSeatList() {
		return reservedSeatList;
	}
	
}
