package com.walmart.ts.test.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.LinkedList;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.walmart.ts.beans.Level;
import com.walmart.ts.beans.Seat;
import com.walmart.ts.beans.SeatStatus;
import com.walmart.ts.beans.Venue;
import com.walmart.ts.utils.SeatDAOUtil;

public class TestSeatDAOUtil {
	private Venue venue;
	private LinkedList<Level> levelList;
	
	@Before
	public void setup() {
		venue = Mockito.mock(Venue.class);
		levelList = new LinkedList<Level>();
	}
	
	@Test
	public void testSeatAvailInVenueNull() {
		int seatsAvail = SeatDAOUtil.countAvailSeatInVenue(null);
		assertEquals(seatsAvail, 0);
	}
	
	@Test
	public void testSeatAvailInVenue1v1() {	
		Mockito.when(venue.getVenueLevels()).thenReturn(null);
		
		int seatsAvail = SeatDAOUtil.countAvailSeatInVenue(venue);
		assertEquals(seatsAvail, 0);
	}
	
	@Test
	public void testSeatAvailInVenue1v2() {	
		Mockito.when(venue.getVenueLevels()).thenReturn(levelList);
		
		int seatsAvail = SeatDAOUtil.countAvailSeatInVenue(venue);
		assertEquals(seatsAvail, 0);
	}
	
	
	@Test
	public void testSeatAvailInVenue2v1() {
		LinkedList<Seat> seatList = new LinkedList<Seat>();
		Level level = Mockito.mock(Level.class);
		Seat seat = Mockito.mock(Seat.class);
		levelList.add(level);
		seatList.add(seat);
		
		Mockito.when(venue.getVenueLevels()).thenReturn(levelList);
		Mockito.when(level.getSeatList()).thenReturn(seatList);
		Mockito.when(seat.getStatus()).thenReturn(SeatStatus.HELD);
		
		int seatsAvail = SeatDAOUtil.countAvailSeatInVenue(venue);
		assertEquals(seatsAvail, 0);
	}
	
	@Test
	public void testSeatAvailInVenue2v2() {
		LinkedList<Seat> seatList = new LinkedList<Seat>();
		Level level = Mockito.mock(Level.class);
		Seat seat = Mockito.mock(Seat.class);
		levelList.add(level);
		seatList.add(seat);
		
		Mockito.when(venue.getVenueLevels()).thenReturn(levelList);
		Mockito.when(level.getSeatList()).thenReturn(seatList);
		Mockito.when(seat.getStatus()).thenReturn(SeatStatus.RESERVED);
		
		int seatsAvail = SeatDAOUtil.countAvailSeatInVenue(venue);
		assertEquals(seatsAvail, 0);
	}
	
	@Test
	public void testSeatAvailInVenue2v3() {
		LinkedList<Seat> seatList = new LinkedList<Seat>();
		Level level = Mockito.mock(Level.class);
		Seat seat = Mockito.mock(Seat.class);
		levelList.add(level);
		seatList.add(seat);
		
		Mockito.when(venue.getVenueLevels()).thenReturn(levelList);
		Mockito.when(level.getSeatList()).thenReturn(seatList);
		Mockito.when(seat.getStatus()).thenReturn(SeatStatus.AVAILABLE);
		
		int seatsAvail = SeatDAOUtil.countAvailSeatInVenue(venue);
		assertEquals(seatsAvail, 1);
	}
	
	@Test
	public void testSeatAvailInVenue3() {
		LinkedList<Seat> seatList = new LinkedList<Seat>();
		Level level = Mockito.mock(Level.class);
		Seat seat1 = Mockito.mock(Seat.class);
		Seat seat2 = Mockito.mock(Seat.class);
		Seat seat3 = Mockito.mock(Seat.class);
		levelList.add(level);
		seatList.add(seat1);
		seatList.add(seat2);
		seatList.add(seat3);
		
		Mockito.when(venue.getVenueLevels()).thenReturn(levelList);
		Mockito.when(level.getSeatList()).thenReturn(seatList);
		Mockito.when(seat1.getStatus()).thenReturn(SeatStatus.HELD);
		Mockito.when(seat2.getStatus()).thenReturn(SeatStatus.AVAILABLE);
		Mockito.when(seat3.getStatus()).thenReturn(SeatStatus.AVAILABLE);
		
		int seatsAvail = SeatDAOUtil.countAvailSeatInVenue(venue);
		assertEquals(seatsAvail, 2);
	}
	
	@Test
	public void testSeatAvailInVenue4() {
		LinkedList<Seat> seatList = new LinkedList<Seat>();
		Level level1 = Mockito.mock(Level.class);
		Level level2 = Mockito.mock(Level.class);
		Seat seat1 = Mockito.mock(Seat.class);
		Seat seat2 = Mockito.mock(Seat.class);
		Seat seat3 = Mockito.mock(Seat.class);
		
		levelList.add(level1);
		levelList.add(level2);
		seatList.add(seat1);
		seatList.add(seat2);
		seatList.add(seat3);
		
		Mockito.when(venue.getVenueLevels()).thenReturn(levelList);
		Mockito.when(level1.getSeatList()).thenReturn(seatList);
		Mockito.when(level2.getSeatList()).thenReturn(seatList);
		Mockito.when(seat1.getStatus()).thenReturn(SeatStatus.HELD);
		Mockito.when(seat2.getStatus()).thenReturn(SeatStatus.AVAILABLE);
		Mockito.when(seat3.getStatus()).thenReturn(SeatStatus.AVAILABLE);
		
		int seatsAvail = SeatDAOUtil.countAvailSeatInVenue(venue);
		assertEquals(seatsAvail, 4);
	}
	
	@Test
	public void testSeatAvailInLevel1() {
		int seatsAvail = SeatDAOUtil.countAvailSeatInLevel(null, 1);
		assertEquals(seatsAvail, 0);
	}
	
	@Test
	public void testSeatAvailInLevel2() {
		Level level = Mockito.mock(Level.class);
		levelList.add(level);
		
		Mockito.when(venue.getVenueLevels()).thenReturn(levelList);
		Mockito.when(level.getId()).thenReturn(2);
		
		int seatsAvail = SeatDAOUtil.countAvailSeatInLevel(venue, 1);
		assertEquals(seatsAvail, 0);
	}
	
	@Test
	public void testSeatAvailInLevel3() {
		LinkedList<Seat> seatList1 = new LinkedList<Seat>();
		LinkedList<Seat> seatList2 = new LinkedList<Seat>();
		Level level1 = Mockito.mock(Level.class);
		Level level2 = Mockito.mock(Level.class);
		Seat seat1 = Mockito.mock(Seat.class);
		Seat seat2 = Mockito.mock(Seat.class);
		Seat seat3 = Mockito.mock(Seat.class);
		levelList.add(level1);
		levelList.add(level2);
		seatList1.add(seat1);
		seatList1.add(seat2);
		seatList2.add(seat3);
		
		Mockito.when(venue.getVenueLevels()).thenReturn(levelList);
		Mockito.when(level1.getId()).thenReturn(1);
		Mockito.when(level2.getId()).thenReturn(2);
		Mockito.when(level1.getSeatList()).thenReturn(seatList1);
		Mockito.when(level2.getSeatList()).thenReturn(seatList2);
		Mockito.when(seat1.getStatus()).thenReturn(SeatStatus.HELD);
		Mockito.when(seat2.getStatus()).thenReturn(SeatStatus.RESERVED);
		Mockito.when(seat3.getStatus()).thenReturn(SeatStatus.AVAILABLE);
		
		int seatsAvail = SeatDAOUtil.countAvailSeatInLevel(venue, 1);
		assertEquals(seatsAvail, 0);
	}
	
	@Test
	public void testSeatAvailInLevel4() {
		LinkedList<Seat> seatList1 = new LinkedList<Seat>();
		LinkedList<Seat> seatList2 = new LinkedList<Seat>();
		Level level1 = Mockito.mock(Level.class);
		Level level2 = Mockito.mock(Level.class);
		Seat seat1 = Mockito.mock(Seat.class);
		Seat seat2 = Mockito.mock(Seat.class);
		Seat seat3 = Mockito.mock(Seat.class);
		levelList.add(level1);
		levelList.add(level2);
		seatList1.add(seat1);
		seatList1.add(seat2);
		seatList2.add(seat3);
		
		Mockito.when(venue.getVenueLevels()).thenReturn(levelList);
		Mockito.when(level1.getId()).thenReturn(1);
		Mockito.when(level2.getId()).thenReturn(2);
		Mockito.when(level1.getSeatList()).thenReturn(seatList1);
		Mockito.when(level2.getSeatList()).thenReturn(seatList2);
		Mockito.when(seat1.getStatus()).thenReturn(SeatStatus.AVAILABLE);
		Mockito.when(seat2.getStatus()).thenReturn(SeatStatus.RESERVED);
		Mockito.when(seat3.getStatus()).thenReturn(SeatStatus.AVAILABLE);
		
		int seatsAvail = SeatDAOUtil.countAvailSeatInLevel(venue, 1);
		assertEquals(seatsAvail, 1);
	}
	
	@Test
	public void testSeatsAvailWithLevels1() {
		int seatsAvail = SeatDAOUtil.countAvailSeatsWithinLevels(null, Optional.of(1), Optional.ofNullable(null));
		assertEquals(seatsAvail, 0);
	}
	
	@Test
	public void testSeatsAvailWithLevels2() {
		Mockito.when(venue.getVenueLevels()).thenReturn(null);
		
		int seatsAvail = SeatDAOUtil.countAvailSeatsWithinLevels(venue, Optional.ofNullable(null), Optional.ofNullable(null));
		assertEquals(seatsAvail, 0);
	}
	
	@Test
	public void testSeatsAvailWithLevels3() {
		LinkedList<Seat> seatList1 = new LinkedList<Seat>();
		LinkedList<Seat> seatList2 = new LinkedList<Seat>();
		LinkedList<Seat> seatList3 = new LinkedList<Seat>();
		
		Level level1 = Mockito.mock(Level.class);
		Level level2 = Mockito.mock(Level.class);
		Level level3 = Mockito.mock(Level.class);
		levelList.add(level1);
		levelList.add(level2);
		levelList.add(level3);
		Seat seat1 = Mockito.mock(Seat.class);
		Seat seat2 = Mockito.mock(Seat.class);
		Seat seat3 = Mockito.mock(Seat.class);
		Seat seat4 = Mockito.mock(Seat.class);
		seatList1.add(seat1);
		seatList1.add(seat2);
		seatList1.add(seat3);
		seatList2.add(seat2);
		seatList3.add(seat2);
		seatList3.add(seat3);
		seatList3.add(seat4);
		
		Mockito.when(venue.getVenueLevels()).thenReturn(levelList);
		Mockito.when(level1.getSeatList()).thenReturn(seatList1);
		Mockito.when(level2.getSeatList()).thenReturn(seatList2);
		Mockito.when(level3.getSeatList()).thenReturn(seatList3);
		Mockito.when(seat1.getStatus()).thenReturn(SeatStatus.HELD);
		Mockito.when(seat2.getStatus()).thenReturn(SeatStatus.AVAILABLE);
		Mockito.when(seat3.getStatus()).thenReturn(SeatStatus.RESERVED);
		Mockito.when(seat4.getStatus()).thenReturn(SeatStatus.AVAILABLE);
		
		int seatsAvail = SeatDAOUtil.countAvailSeatsWithinLevels(venue, Optional.ofNullable(null), Optional.ofNullable(null));
		assertEquals(seatsAvail, 4);
	}
	
	@Test
	public void testSeatsAvailWithLevels4() {
		LinkedList<Seat> seatList1 = new LinkedList<Seat>();
		LinkedList<Seat> seatList2 = new LinkedList<Seat>();
		LinkedList<Seat> seatList3 = new LinkedList<Seat>();
		
		Level level1 = Mockito.mock(Level.class);
		Level level2 = Mockito.mock(Level.class);
		Level level3 = Mockito.mock(Level.class);
		levelList.add(level1);
		levelList.add(level2);
		levelList.add(level3);
		Seat seat1 = Mockito.mock(Seat.class);
		Seat seat2 = Mockito.mock(Seat.class);
		Seat seat3 = Mockito.mock(Seat.class);
		Seat seat4 = Mockito.mock(Seat.class);
		seatList1.add(seat1);
		seatList1.add(seat2);
		seatList1.add(seat3);
		seatList2.add(seat2);
		seatList3.add(seat2);
		seatList3.add(seat3);
		seatList3.add(seat4);
		
		Mockito.when(venue.getVenueLevels()).thenReturn(levelList);
		Mockito.when(level1.getSeatList()).thenReturn(seatList1);
		Mockito.when(level2.getSeatList()).thenReturn(seatList2);
		Mockito.when(level3.getSeatList()).thenReturn(seatList3);
		Mockito.when(seat1.getStatus()).thenReturn(SeatStatus.HELD);
		Mockito.when(seat2.getStatus()).thenReturn(SeatStatus.AVAILABLE);
		Mockito.when(seat3.getStatus()).thenReturn(SeatStatus.RESERVED);
		Mockito.when(seat4.getStatus()).thenReturn(SeatStatus.AVAILABLE);
		Mockito.when(level1.getId()).thenReturn(1);
		Mockito.when(level2.getId()).thenReturn(2);
		Mockito.when(level3.getId()).thenReturn(3);
		
		int seatsAvail = SeatDAOUtil.countAvailSeatsWithinLevels(venue, Optional.ofNullable(2), Optional.ofNullable(null));
		assertEquals(seatsAvail, 3);
	}
	
	@Test
	public void testSeatsAvailWithLevels5() {
		LinkedList<Seat> seatList1 = new LinkedList<Seat>();
		LinkedList<Seat> seatList2 = new LinkedList<Seat>();
		LinkedList<Seat> seatList3 = new LinkedList<Seat>();
		
		Level level1 = Mockito.mock(Level.class);
		Level level2 = Mockito.mock(Level.class);
		Level level3 = Mockito.mock(Level.class);
		levelList.add(level1);
		levelList.add(level2);
		levelList.add(level3);
		Seat seat1 = Mockito.mock(Seat.class);
		Seat seat2 = Mockito.mock(Seat.class);
		Seat seat3 = Mockito.mock(Seat.class);
		Seat seat4 = Mockito.mock(Seat.class);
		seatList1.add(seat1);
		seatList1.add(seat2);
		seatList1.add(seat3);
		seatList2.add(seat2);
		seatList3.add(seat2);
		seatList3.add(seat3);
		seatList3.add(seat4);
		
		Mockito.when(venue.getVenueLevels()).thenReturn(levelList);
		Mockito.when(level1.getSeatList()).thenReturn(seatList1);
		Mockito.when(level2.getSeatList()).thenReturn(seatList2);
		Mockito.when(level3.getSeatList()).thenReturn(seatList3);
		Mockito.when(seat1.getStatus()).thenReturn(SeatStatus.HELD);
		Mockito.when(seat2.getStatus()).thenReturn(SeatStatus.AVAILABLE);
		Mockito.when(seat3.getStatus()).thenReturn(SeatStatus.RESERVED);
		Mockito.when(seat4.getStatus()).thenReturn(SeatStatus.AVAILABLE);
		Mockito.when(level1.getId()).thenReturn(1);
		Mockito.when(level2.getId()).thenReturn(2);
		Mockito.when(level3.getId()).thenReturn(3);
		
		int seatsAvail = SeatDAOUtil.countAvailSeatsWithinLevels(venue, Optional.ofNullable(0), Optional.ofNullable(null));
		assertEquals(seatsAvail, 4);
	}
	
	@Test
	public void testSeatsAvailWithLevels6() {
		LinkedList<Seat> seatList1 = new LinkedList<Seat>();
		LinkedList<Seat> seatList2 = new LinkedList<Seat>();
		LinkedList<Seat> seatList3 = new LinkedList<Seat>();
		
		Level level1 = Mockito.mock(Level.class);
		Level level2 = Mockito.mock(Level.class);
		Level level3 = Mockito.mock(Level.class);
		levelList.add(level1);
		levelList.add(level2);
		levelList.add(level3);
		Seat seat1 = Mockito.mock(Seat.class);
		Seat seat2 = Mockito.mock(Seat.class);
		Seat seat3 = Mockito.mock(Seat.class);
		Seat seat4 = Mockito.mock(Seat.class);
		seatList1.add(seat1);
		seatList1.add(seat2);
		seatList1.add(seat3);
		seatList2.add(seat2);
		seatList3.add(seat2);
		seatList3.add(seat3);
		seatList3.add(seat4);
		
		Mockito.when(venue.getVenueLevels()).thenReturn(levelList);
		Mockito.when(level1.getSeatList()).thenReturn(seatList1);
		Mockito.when(level2.getSeatList()).thenReturn(seatList2);
		Mockito.when(level3.getSeatList()).thenReturn(seatList3);
		Mockito.when(seat1.getStatus()).thenReturn(SeatStatus.HELD);
		Mockito.when(seat2.getStatus()).thenReturn(SeatStatus.AVAILABLE);
		Mockito.when(seat3.getStatus()).thenReturn(SeatStatus.RESERVED);
		Mockito.when(seat4.getStatus()).thenReturn(SeatStatus.AVAILABLE);
		Mockito.when(level1.getId()).thenReturn(1);
		Mockito.when(level2.getId()).thenReturn(2);
		Mockito.when(level3.getId()).thenReturn(3);
		
		int seatsAvail = SeatDAOUtil.countAvailSeatsWithinLevels(venue, Optional.ofNullable(null), Optional.ofNullable(5));
		assertEquals(seatsAvail, 4);
	}
	
	@Test
	public void testSeatsAvailWithLevels7() {
		LinkedList<Seat> seatList1 = new LinkedList<Seat>();
		LinkedList<Seat> seatList2 = new LinkedList<Seat>();
		LinkedList<Seat> seatList3 = new LinkedList<Seat>();
		
		Level level1 = Mockito.mock(Level.class);
		Level level2 = Mockito.mock(Level.class);
		Level level3 = Mockito.mock(Level.class);
		levelList.add(level1);
		levelList.add(level2);
		levelList.add(level3);
		Seat seat1 = Mockito.mock(Seat.class);
		Seat seat2 = Mockito.mock(Seat.class);
		Seat seat3 = Mockito.mock(Seat.class);
		Seat seat4 = Mockito.mock(Seat.class);
		seatList1.add(seat1);
		seatList1.add(seat2);
		seatList1.add(seat3);
		seatList2.add(seat2);
		seatList3.add(seat2);
		seatList3.add(seat3);
		seatList3.add(seat4);
		
		Mockito.when(venue.getVenueLevels()).thenReturn(levelList);
		Mockito.when(level1.getSeatList()).thenReturn(seatList1);
		Mockito.when(level2.getSeatList()).thenReturn(seatList2);
		Mockito.when(level3.getSeatList()).thenReturn(seatList3);
		Mockito.when(seat1.getStatus()).thenReturn(SeatStatus.HELD);
		Mockito.when(seat2.getStatus()).thenReturn(SeatStatus.AVAILABLE);
		Mockito.when(seat3.getStatus()).thenReturn(SeatStatus.RESERVED);
		Mockito.when(seat4.getStatus()).thenReturn(SeatStatus.AVAILABLE);
		Mockito.when(level1.getId()).thenReturn(1);
		Mockito.when(level2.getId()).thenReturn(2);
		Mockito.when(level3.getId()).thenReturn(3);
		
		int seatsAvail = SeatDAOUtil.countAvailSeatsWithinLevels(venue, Optional.ofNullable(null), Optional.ofNullable(0));
		assertEquals(seatsAvail, 0);
	}
	
	@Test
	public void testSeatsAvailWithLevels8() {
		LinkedList<Seat> seatList1 = new LinkedList<Seat>();
		LinkedList<Seat> seatList2 = new LinkedList<Seat>();
		LinkedList<Seat> seatList3 = new LinkedList<Seat>();
		
		Level level1 = Mockito.mock(Level.class);
		Level level2 = Mockito.mock(Level.class);
		Level level3 = Mockito.mock(Level.class);
		levelList.add(level1);
		levelList.add(level2);
		levelList.add(level3);
		Seat seat1 = Mockito.mock(Seat.class);
		Seat seat2 = Mockito.mock(Seat.class);
		Seat seat3 = Mockito.mock(Seat.class);
		Seat seat4 = Mockito.mock(Seat.class);
		seatList1.add(seat1);
		seatList1.add(seat2);
		seatList1.add(seat3);
		seatList2.add(seat2);
		seatList3.add(seat2);
		seatList3.add(seat3);
		seatList3.add(seat4);
		
		Mockito.when(venue.getVenueLevels()).thenReturn(levelList);
		Mockito.when(level1.getSeatList()).thenReturn(seatList1);
		Mockito.when(level2.getSeatList()).thenReturn(seatList2);
		Mockito.when(level3.getSeatList()).thenReturn(seatList3);
		Mockito.when(seat1.getStatus()).thenReturn(SeatStatus.HELD);
		Mockito.when(seat2.getStatus()).thenReturn(SeatStatus.AVAILABLE);
		Mockito.when(seat3.getStatus()).thenReturn(SeatStatus.RESERVED);
		Mockito.when(seat4.getStatus()).thenReturn(SeatStatus.AVAILABLE);
		Mockito.when(level1.getId()).thenReturn(1);
		Mockito.when(level2.getId()).thenReturn(2);
		Mockito.when(level3.getId()).thenReturn(3);
		
		int seatsAvail = SeatDAOUtil.countAvailSeatsWithinLevels(venue, Optional.ofNullable(2), Optional.ofNullable(3));
		assertEquals(seatsAvail, 3);
	}
	
	@Test
	public void testSeatsAvailWithLevels9() {
		LinkedList<Seat> seatList1 = new LinkedList<Seat>();
		LinkedList<Seat> seatList2 = new LinkedList<Seat>();
		LinkedList<Seat> seatList3 = new LinkedList<Seat>();
		
		Level level1 = Mockito.mock(Level.class);
		Level level2 = Mockito.mock(Level.class);
		Level level3 = Mockito.mock(Level.class);
		levelList.add(level1);
		levelList.add(level2);
		levelList.add(level3);
		Seat seat1 = Mockito.mock(Seat.class);
		Seat seat2 = Mockito.mock(Seat.class);
		Seat seat3 = Mockito.mock(Seat.class);
		Seat seat4 = Mockito.mock(Seat.class);
		seatList1.add(seat1);
		seatList1.add(seat2);
		seatList1.add(seat3);
		seatList2.add(seat2);
		seatList3.add(seat2);
		seatList3.add(seat3);
		seatList3.add(seat4);
		
		Mockito.when(venue.getVenueLevels()).thenReturn(levelList);
		Mockito.when(level1.getSeatList()).thenReturn(seatList1);
		Mockito.when(level2.getSeatList()).thenReturn(seatList2);
		Mockito.when(level3.getSeatList()).thenReturn(seatList3);
		Mockito.when(seat1.getStatus()).thenReturn(SeatStatus.HELD);
		Mockito.when(seat2.getStatus()).thenReturn(SeatStatus.AVAILABLE);
		Mockito.when(seat3.getStatus()).thenReturn(SeatStatus.RESERVED);
		Mockito.when(seat4.getStatus()).thenReturn(SeatStatus.AVAILABLE);
		Mockito.when(level1.getId()).thenReturn(1);
		Mockito.when(level2.getId()).thenReturn(2);
		Mockito.when(level3.getId()).thenReturn(3);
		
		int seatsAvail = SeatDAOUtil.countAvailSeatsWithinLevels(venue, Optional.ofNullable(2), Optional.ofNullable(2));
		assertEquals(seatsAvail, 1);
	}
	
	@Test
	public void testHoldSeat1v1() {
		Mockito.when(venue.getVenueLevels()).thenReturn(null);
		
		Seat seat = SeatDAOUtil.holdARandomSeat(venue, Optional.ofNullable(null), Optional.ofNullable(null));
		assertNull(seat);
	}
	
	@Test
	public void testHoldSeat1v2() {
		Mockito.when(venue.getVenueLevels()).thenReturn(levelList);
		
		Seat seat = SeatDAOUtil.holdARandomSeat(venue, Optional.ofNullable(null), Optional.ofNullable(null));
		assertNull(seat);
	}
	
	
	@Test
	public void testHoldSeat1v3() {
		Level level = Mockito.mock(Level.class);
		levelList.add(level);
		
		Mockito.when(venue.getVenueLevels()).thenReturn(levelList);
		Mockito.when(level.getSeatList()).thenReturn(new LinkedList<Seat>());
		
		Seat seat = SeatDAOUtil.holdARandomSeat(venue, Optional.ofNullable(null), Optional.ofNullable(null));
		assertNull(seat);
	}
	
	@Test
	public void testHoldSeat2v1() {
		Level level = Mockito.mock(Level.class);
		Seat seat1 = Mockito.mock(Seat.class);
		Seat seat2 = Mockito.mock(Seat.class);
		LinkedList<Seat> seatList = new LinkedList<Seat>();
		levelList.add(level);
		seatList.add(seat1);
		seatList.add(seat2);
		
		Mockito.when(venue.getVenueLevels()).thenReturn(levelList);
		Mockito.when(level.getSeatList()).thenReturn(seatList);
		Mockito.when(seat1.getStatus()).thenReturn(SeatStatus.RESERVED);
		Mockito.when(seat2.getStatus()).thenReturn(SeatStatus.AVAILABLE);
		
		Seat seat = SeatDAOUtil.holdARandomSeat(venue, Optional.ofNullable(null), Optional.ofNullable(null));
		assertEquals(seat, seat2);
	}
	
	@Test
	public void testHoldSeat2v2() {
		Level level = Mockito.mock(Level.class);
		Seat seat1 = new Seat();
		Seat seat2 = new Seat();
		LinkedList<Seat> seatList = new LinkedList<Seat>();
		levelList.add(level);
		seatList.add(seat1);
		seatList.add(seat2);
		seat1.setStatus(SeatStatus.RESERVED);
		seat2.setStatus(SeatStatus.AVAILABLE);
		
		Mockito.when(venue.getVenueLevels()).thenReturn(levelList);
		Mockito.when(level.getSeatList()).thenReturn(seatList);
		
		Seat seat = SeatDAOUtil.holdARandomSeat(venue, Optional.ofNullable(null), Optional.ofNullable(null));
		assertEquals(seat.getStatus(), SeatStatus.HELD);
	}
	
	@Test
	public void testHoldSeat3v1() {
		Level level1 = Mockito.mock(Level.class);
		Level level2 = Mockito.mock(Level.class);
		Seat seat1 = Mockito.mock(Seat.class);
		Seat seat2 = Mockito.mock(Seat.class);
		LinkedList<Seat> seatList1 = new LinkedList<Seat>();
		LinkedList<Seat> seatList2 = new LinkedList<Seat>();
		
		levelList.add(level1);
		levelList.add(level2);
		seatList1.add(seat1);
		seatList2.add(seat2);
		
		Mockito.when(venue.getVenueLevels()).thenReturn(levelList);
		Mockito.when(level1.getSeatList()).thenReturn(seatList1);
		Mockito.when(level2.getSeatList()).thenReturn(seatList2);
		Mockito.when(seat1.getStatus()).thenReturn(SeatStatus.RESERVED);
		Mockito.when(seat2.getStatus()).thenReturn(SeatStatus.HELD);
		Mockito.when(level1.getId()).thenReturn(-1);
		Mockito.when(level2.getId()).thenReturn(2);
		
		Seat seat = SeatDAOUtil.holdARandomSeat(venue, Optional.ofNullable(-3), Optional.ofNullable(3));
		assertNull(seat);
	}
	
	@Test
	public void testHoldSeat3v2() {
		Level level1 = Mockito.mock(Level.class);
		Level level2 = Mockito.mock(Level.class);
		Seat seat1 = Mockito.mock(Seat.class);
		Seat seat2 = Mockito.mock(Seat.class);
		LinkedList<Seat> seatList1 = new LinkedList<Seat>();
		LinkedList<Seat> seatList2 = new LinkedList<Seat>();
		
		levelList.add(level1);
		levelList.add(level2);
		seatList1.add(seat1);
		seatList2.add(seat2);
		
		Mockito.when(venue.getVenueLevels()).thenReturn(levelList);
		Mockito.when(level1.getSeatList()).thenReturn(seatList1);
		Mockito.when(level2.getSeatList()).thenReturn(seatList2);
		Mockito.when(seat1.getStatus()).thenReturn(SeatStatus.AVAILABLE);
		Mockito.when(seat2.getStatus()).thenReturn(SeatStatus.AVAILABLE);
		Mockito.when(level1.getId()).thenReturn(-1);
		Mockito.when(level2.getId()).thenReturn(2);
		
		Seat seat = SeatDAOUtil.holdARandomSeat(venue, Optional.ofNullable(-3), Optional.ofNullable(null));
		assertNotNull(seat);
	}
	
	@Test
	public void testHoldSeat3v3() {
		Level level1 = Mockito.mock(Level.class);
		Level level2 = Mockito.mock(Level.class);
		Seat seat1 = Mockito.mock(Seat.class);
		Seat seat2 = Mockito.mock(Seat.class);
		LinkedList<Seat> seatList1 = new LinkedList<Seat>();
		LinkedList<Seat> seatList2 = new LinkedList<Seat>();
		
		levelList.add(level1);
		levelList.add(level2);
		seatList1.add(seat1);
		seatList2.add(seat2);
		
		Mockito.when(venue.getVenueLevels()).thenReturn(levelList);
		Mockito.when(level1.getSeatList()).thenReturn(seatList1);
		Mockito.when(level2.getSeatList()).thenReturn(seatList2);
		Mockito.when(seat1.getStatus()).thenReturn(SeatStatus.AVAILABLE);
		Mockito.when(seat2.getStatus()).thenReturn(SeatStatus.AVAILABLE);
		Mockito.when(level1.getId()).thenReturn(-1);
		Mockito.when(level2.getId()).thenReturn(2);
		
		Seat seat = SeatDAOUtil.holdARandomSeat(venue, Optional.ofNullable(null), Optional.ofNullable(-3));
		assertNull(seat);
	}
	
	@Test
	public void testHoldSeat3v4() {
		Level level1 = Mockito.mock(Level.class);
		Level level2 = Mockito.mock(Level.class);
		Seat seat1 = Mockito.mock(Seat.class);
		Seat seat2 = Mockito.mock(Seat.class);
		LinkedList<Seat> seatList1 = new LinkedList<Seat>();
		LinkedList<Seat> seatList2 = new LinkedList<Seat>();
		
		levelList.add(level1);
		levelList.add(level2);
		seatList1.add(seat1);
		seatList2.add(seat2);
		
		Mockito.when(venue.getVenueLevels()).thenReturn(levelList);
		Mockito.when(level1.getSeatList()).thenReturn(seatList1);
		Mockito.when(level2.getSeatList()).thenReturn(seatList2);
		Mockito.when(seat1.getStatus()).thenReturn(SeatStatus.AVAILABLE);
		Mockito.when(seat2.getStatus()).thenReturn(SeatStatus.AVAILABLE);
		Mockito.when(level1.getId()).thenReturn(-1);
		Mockito.when(level2.getId()).thenReturn(2);
		
		Seat seat = SeatDAOUtil.holdARandomSeat(venue, Optional.ofNullable(1), Optional.ofNullable(-2));
		assertNull(seat);
	}
	
	@Test
	public void testHoldSeat3v5() {
		Level level1 = Mockito.mock(Level.class);
		Level level2 = Mockito.mock(Level.class);
		Seat seat1 = Mockito.mock(Seat.class);
		Seat seat2 = Mockito.mock(Seat.class);
		LinkedList<Seat> seatList1 = new LinkedList<Seat>();
		LinkedList<Seat> seatList2 = new LinkedList<Seat>();
		
		levelList.add(level1);
		levelList.add(level2);
		seatList1.add(seat1);
		seatList2.add(seat2);
		
		Mockito.when(venue.getVenueLevels()).thenReturn(levelList);
		Mockito.when(level1.getSeatList()).thenReturn(seatList1);
		Mockito.when(level2.getSeatList()).thenReturn(seatList2);
		Mockito.when(seat1.getStatus()).thenReturn(SeatStatus.AVAILABLE);
		Mockito.when(seat2.getStatus()).thenReturn(SeatStatus.AVAILABLE);
		Mockito.when(level1.getId()).thenReturn(-1);
		Mockito.when(level2.getId()).thenReturn(2);
		
		Seat seat = SeatDAOUtil.holdARandomSeat(venue, Optional.ofNullable(1), Optional.ofNullable(2));
		assertNotNull(seat);
	}
	
}
