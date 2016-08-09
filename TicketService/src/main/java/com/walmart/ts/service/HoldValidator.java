/**
 * 
 */
package com.walmart.ts.service;

import java.time.LocalDateTime;
import java.util.LinkedList;

import com.walmart.ts.beans.SeatHold;
import com.walmart.ts.beans.SeatHoldRepository;
import com.walmart.ts.beans.SeatStatus;

/**
 * @author Haarthi Padmanabhan
 *
 */
public class HoldValidator {
	
	public static void purgeExpiredHolds() {
		LinkedList<SeatHold> seatHoldList = SeatHoldRepository.getSeatHoldList();
		while(seatHoldList != null && seatHoldList.size() > 0) {
			SeatHold sh = seatHoldList.getFirst();
			LocalDateTime holdTime = sh.getHoldStartTime();
			LocalDateTime currentTime = LocalDateTime.now();
			long diffInSeconds = java.time.Duration.between(holdTime, currentTime).getSeconds();
		    if(diffInSeconds >= 60) {
		    	sh.getHeldSeatList().forEach(seat -> seat.setStatus(SeatStatus.AVAILABLE));
		    	seatHoldList.removeFirst();
		    } else {
		    	break;
		    }
		}
	}
}
