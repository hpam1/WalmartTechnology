/**
 * 
 */
package com.walmart.ts.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Properties;

import com.walmart.ts.beans.SeatHold;
import com.walmart.ts.beans.SeatHoldRepository;
import com.walmart.ts.beans.SeatStatus;

/**
 * @author Haarthi Padmanabhan
 * 
 *         HoldValidator clears SeatHolds that are older than a threshold value
 *         and releases the held seats to other users
 * 
 *         SeatHoldList is a linkedlist whose elements are sorted by the hold
 *         creation time with the element at the head of the list representing
 *         the oldest hold and that at the tail represents the newest hold
 *         Iterate through the seatholdlist from the head till the element whose
 *         hold creation time is within the threshold (in seconds)
 * 
 *
 */
public class HoldValidator {
	// default threshold time is 60 seconds
	static long holdThreshold = 60l;

	/*
	 * read the hold expiration threshold value from a properties file
	 * TSProperties.properties located in resources folder
	 * 
	 */
	static {
		ClassLoader classLoader = HoldValidator.class.getClassLoader();
		File file = new File(classLoader.getResource("TSProperties.properties").getFile());
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(file));
			holdThreshold = Long.parseLong((String) properties.get("HoldExpirationThresholdInSeconds"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * clear the holds that are more than threshold seconds from the current
	 * time
	 */
	public static void purgeExpiredHolds() {
		LinkedList<SeatHold> seatHoldList = SeatHoldRepository.getSeatHoldList();
		// iterate through the hold list
		while (seatHoldList != null && seatHoldList.size() > 0) {
			SeatHold sh = seatHoldList.getFirst();
			LocalDateTime holdTime = sh.getHoldStartTime();
			LocalDateTime currentTime = LocalDateTime.now();
			long diffInSeconds = java.time.Duration.between(holdTime, currentTime).getSeconds();
			if (diffInSeconds >= holdThreshold) { // hold expired
				if (sh.getHeldSeatList() != null && sh.getHeldSeatList().size() > 0)
					sh.getHeldSeatList().forEach(seat -> seat.setStatus(SeatStatus.AVAILABLE));
				seatHoldList.removeFirst();
			} else { // the current element is within the threshold and so are
						// the next elements; break the processing
				break;
			}
		}
	}
}
