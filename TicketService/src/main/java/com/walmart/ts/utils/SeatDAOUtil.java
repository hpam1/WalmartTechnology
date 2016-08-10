package com.walmart.ts.utils;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import com.walmart.ts.beans.Level;
import com.walmart.ts.beans.Seat;
import com.walmart.ts.beans.SeatHold;
import com.walmart.ts.beans.SeatHoldRepository;
import com.walmart.ts.beans.SeatStatus;
import com.walmart.ts.beans.Venue;

/**
 * 
 * @author Haarthi Padmanabhan
 * 
 *         A utility class for querying about seat and seat holds
 *
 */
public class SeatDAOUtil {

	/*
	 * count the number of available seats in the venue
	 * 
	 */
	public static int countAvailSeatInVenue(Venue venue) {
		if (venue == null)
			return 0;
		List<Level> levelList = venue.getVenueLevels();
		return getAvailSeat(levelList);
	}

	/*
	 * count the number of available seats in a particular venue level
	 * 
	 */
	public static int countAvailSeatInLevel(Venue venue, int levelId) {
		if (venue == null)
			return 0;
		List<Level> levelList = venue.getVenueLevels().stream().filter(lvl -> lvl.getId() == levelId)
				.collect(Collectors.toList());
		return getAvailSeat(levelList);
	}

	/*
	 * count the number of available seats
	 *
	 */
	private static int getAvailSeat(List<Level> levelList) {
		if (levelList == null || levelList.size() == 0)
			return 0;
		List<Seat> seatList = levelList.stream().flatMap(lvl -> lvl.getSeatList().stream())
				.collect(Collectors.toList());
		if (seatList == null || seatList.size() == 0)
			return 0;
		return (int) seatList.stream().filter(seat -> seat.getStatus().equals(SeatStatus.AVAILABLE)).count();
	}

	/*
	 * count the seats available, optionally limited by levels If minLevel is
	 * present, then search for available seats from this level If maxLevel is
	 * present, then search for available seats within this level otherwise,
	 * search the entire venue
	 * 
	 */
	public static int countAvailSeatsWithinLevels(Venue venue, Optional<Integer> minLevel, Optional<Integer> maxLevel) {
		if (venue == null)
			return 0;
		List<Level> levelList = venue.getVenueLevels();
		if (levelList == null || levelList.size() == 0)
			return 0;
		if (minLevel.isPresent() && maxLevel.isPresent()) {
			if (minLevel.get() > maxLevel.get())
				return 0;
			levelList = levelList.stream().filter(lvl -> lvl.getId() >= minLevel.get() && lvl.getId() <= maxLevel.get())
					.collect(Collectors.toList());
		} else if (minLevel.isPresent()) {
			levelList = levelList.stream().filter(lvl -> lvl.getId() >= minLevel.get()).collect(Collectors.toList());
		} else if (maxLevel.isPresent()) {
			levelList = levelList.stream().filter(lvl -> lvl.getId() <= maxLevel.get()).collect(Collectors.toList());
		}
		return getAvailSeat(levelList);
	}

	/*
	 * get the available seat list, optionally limited by levels If minLevel is
	 * present, then search for available seats from this level If maxLevel is
	 * present, then search for available seats within this level otherwise,
	 * search the entire venue
	 */
	private static List<Seat> getAvailSeatsInLevels(Venue venue, Optional<Integer> minLevel,
			Optional<Integer> maxLevel) {
		if (venue == null)
			return null;
		List<Level> levelList = venue.getVenueLevels();
		if (levelList == null || levelList.size() == 0)
			return null;
		if (minLevel.isPresent() && maxLevel.isPresent()) {
			if (minLevel.get() > maxLevel.get())
				return null;
			levelList = levelList.stream().filter(lvl -> lvl.getId() >= minLevel.get() && lvl.getId() <= maxLevel.get())
					.collect(Collectors.toList());
		} else if (minLevel.isPresent()) {
			levelList = levelList.stream().filter(lvl -> lvl.getId() >= minLevel.get()).collect(Collectors.toList());
		} else if (maxLevel.isPresent()) {
			levelList = levelList.stream().filter(lvl -> lvl.getId() <= maxLevel.get()).collect(Collectors.toList());
		}
		if (levelList == null || levelList.size() == 0)
			return null;
		List<Seat> seatList = levelList.stream().flatMap(lvl -> lvl.getSeatList().stream())
				.collect(Collectors.toList());
		if (seatList != null && seatList.size() > 0) {
			seatList = seatList.stream().filter(seat -> seat.getStatus().equals(SeatStatus.AVAILABLE))
					.collect(Collectors.toList());
		}
		return seatList;
	}

	/*
	 * randomly choose an available seat in the venue, optionally limited by
	 * levels, and hold it for a user.
	 * 
	 */
	public static synchronized Seat holdARandomSeat(Venue venue, Optional<Integer> minLevel,
			Optional<Integer> maxLevel) {
		if (venue == null)
			return null;
		List<Seat> seatList = getAvailSeatsInLevels(venue, minLevel, maxLevel);
		if (seatList == null || seatList.size() == 0)
			return null;
		Random random = new Random();
		Seat seat = seatList.get(random.nextInt(seatList.size()));
		seat.setStatus(SeatStatus.HELD);
		return seat;
	}

	/*
	 * get the SeatHold object given a hold id and an email id Hold that matches
	 * both the hold id and the email id is returned
	 */
	public static SeatHold getSeatHoldListFor(int seatHoldId, String customerEmail) {
		List<SeatHold> seatHoldList = SeatHoldRepository.getSeatHoldList().stream()
				.filter(sh -> sh.getId() == seatHoldId && sh.getCustomerEmail().equals(customerEmail))
				.collect(Collectors.toList());
		if (seatHoldList != null && seatHoldList.size() == 1) {
			return seatHoldList.get(0);
		} else
			return null;
	}
}
