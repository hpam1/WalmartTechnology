/**
 * 
 */
package com.walmart.ts.beans;

import java.util.LinkedList;

/**
 * @author Haarthi Padmanabhan
 * 
 * A repository of seat reservations
 * 
 */
public class SeatReservationRepository {
	// immutable so that the repository is not altered
	private static final LinkedList<SeatReservation> reservedSeatList = new LinkedList<SeatReservation>();

	public static LinkedList<SeatReservation> getReservedSeatList() {
		return reservedSeatList;
	}
	
}
