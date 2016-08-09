package com.walmart.ts.utils;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import com.walmart.ts.beans.Level;
import com.walmart.ts.beans.Seat;
import com.walmart.ts.beans.SeatStatus;
import com.walmart.ts.beans.Venue;

/**
 * 
 * @author Haarthi Padmanabhan
 *
 */
public class SeatDAOUtil {
	public static int countAvailSeatInVenue(Venue venue) {
		List<Level> levelList = venue.getVenueLevels();
		return getAvailSeat(levelList);
	}
	
	public static int countAvailSeatInLevel(Venue venue, int levelId) {
		List<Level> levelList = venue.getVenueLevels().stream().filter(lvl -> lvl.getId() == levelId).collect(Collectors.toList());
		return getAvailSeat(levelList);
	}
	
	private static int getAvailSeat(List<Level> levelList) {
		List<Seat> seatList = levelList.stream().flatMap(lvl -> lvl.getSeatList().stream()).collect(Collectors.toList());
		return (int) seatList.stream().filter(seat -> seat.getStatus().equals(SeatStatus.AVAILABLE)).count();
	}
	
	public static int countAvailSeatsWithinLevels(Venue venue, Optional<Integer> minLevel, Optional<Integer> maxLevel) {
		List<Level> levelList = venue.getVenueLevels();
		if(minLevel.isPresent() && maxLevel.isPresent()) {
			levelList = levelList.stream().filter(lvl -> lvl.getId() >= minLevel.get() && lvl.getId() <= maxLevel.get()).collect(Collectors.toList());
		} else if(minLevel.isPresent()) {
			levelList = levelList.stream().filter(lvl -> lvl.getId() >= minLevel.get()).collect(Collectors.toList());
		} else if(maxLevel.isPresent()) {
			levelList = levelList.stream().filter(lvl -> lvl.getId() <= maxLevel.get()).collect(Collectors.toList());
		}
		return getAvailSeat(levelList);
	}
	
	private static List<Seat> getAvailSeatsInLevels(Venue venue, Optional<Integer> minLevel, Optional<Integer> maxLevel) {
		List<Level> levelList = venue.getVenueLevels();
		if(minLevel.isPresent() && maxLevel.isPresent()) {
			levelList = levelList.stream().filter(lvl -> lvl.getId() >= minLevel.get() && lvl.getId() <= maxLevel.get()).collect(Collectors.toList());
		} else if(minLevel.isPresent()) {
			levelList = levelList.stream().filter(lvl -> lvl.getId() >= minLevel.get()).collect(Collectors.toList());
		} else if(maxLevel.isPresent()) {
			levelList = levelList.stream().filter(lvl -> lvl.getId() <= maxLevel.get()).collect(Collectors.toList());
		}
		List<Seat> seatList = levelList.stream().flatMap(lvl -> lvl.getSeatList().stream()).collect(Collectors.toList());
		return seatList;
	}
	
	public static Seat holdARandomSeat(Venue venue, Optional<Integer> minLevel, Optional<Integer> maxLevel) {
		List<Seat> seatList = getAvailSeatsInLevels(venue, minLevel, maxLevel);
		Random random = new Random();
		return seatList.get(random.nextInt(seatList.size()));
	}
}
