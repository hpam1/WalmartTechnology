/**
 * 
 */
package com.walmart.ts.beans;

import java.util.LinkedList;

/**
 * @author Haarthi Padmanabhan
 *  
 * A repository for holding all the seat holds
 */
public class SeatHoldRepository {
	private static final LinkedList<SeatHold> seatHoldList = new LinkedList<SeatHold>();

	public static LinkedList<SeatHold> getSeatHoldList() {
		return seatHoldList;
	}
	
}
