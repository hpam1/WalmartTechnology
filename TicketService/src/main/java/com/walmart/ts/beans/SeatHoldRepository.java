/**
 * 
 */
package com.walmart.ts.beans;

import java.util.LinkedList;

/**
 * @author Haarthi Padmanabhan
 *
 */
public class SeatHoldRepository {
	private static LinkedList<SeatHold> seatHoldList = new LinkedList<SeatHold>();

	public static LinkedList<SeatHold> getSeatHoldList() {
		return seatHoldList;
	}
	
}
