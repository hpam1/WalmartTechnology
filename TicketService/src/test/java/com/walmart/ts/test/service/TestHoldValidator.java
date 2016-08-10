package com.walmart.ts.test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.time.LocalDateTime;
import java.util.LinkedList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.walmart.ts.beans.Seat;
import com.walmart.ts.beans.SeatHold;
import com.walmart.ts.beans.SeatHoldRepository;
import com.walmart.ts.beans.SeatStatus;
import com.walmart.ts.service.HoldValidator;

/**
 * 
 * @author Haarthi Padmanabhan
 * 
 *         test cases to test hold validator (for clearing expired holds)
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(SeatHoldRepository.class)
@PowerMockIgnore("javax.management.*")
public class TestHoldValidator {
	private LinkedList<SeatHold> seatHoldList;

	@Before
	public void setup() {
		PowerMockito.mockStatic(SeatHoldRepository.class);
		seatHoldList = new LinkedList<SeatHold>();
	}

	@After
	public void after() {
		PowerMockito.verifyStatic();
		SeatHoldRepository.getSeatHoldList();
	}

	@Test
	public void testHVForZeroHolds() throws Exception {
		Mockito.when(SeatHoldRepository.getSeatHoldList()).thenReturn(seatHoldList);

		HoldValidator.purgeExpiredHolds();
		assertEquals(seatHoldList.size(), 0);
	}

	@Test
	public void testHVAllExpiredHolds1v1() throws Exception {
		SeatHold seatHold = Mockito.mock(SeatHold.class);
		seatHoldList.add(seatHold);

		Mockito.when(SeatHoldRepository.getSeatHoldList()).thenReturn(seatHoldList);
		Mockito.when(seatHold.getHoldStartTime()).thenReturn(LocalDateTime.now().minusHours(1));

		HoldValidator.purgeExpiredHolds();
		assertEquals(seatHoldList.size(), 0);
	}

	@Test
	public void testHVAllExpiredHolds1v2() throws Exception {
		Seat seat1 = new Seat();
		Seat seat2 = new Seat();
		seat1.setStatus(SeatStatus.HELD);
		seat2.setStatus(SeatStatus.HELD);
		SeatHold seatHold = Mockito.mock(SeatHold.class);
		seatHoldList.add(seatHold);
		LinkedList<Seat> seatList = new LinkedList<Seat>();
		seatList.add(seat1);
		seatList.add(seat2);

		Mockito.when(SeatHoldRepository.getSeatHoldList()).thenReturn(seatHoldList);
		Mockito.when(seatHold.getHoldStartTime()).thenReturn(LocalDateTime.now().minusHours(1));
		Mockito.when(seatHold.getHeldSeatList()).thenReturn(seatList);

		HoldValidator.purgeExpiredHolds();
		assertEquals(seat1.getStatus(), SeatStatus.AVAILABLE);
		assertEquals(seat2.getStatus(), SeatStatus.AVAILABLE);
	}

	@Test
	public void testHVAllExpiredHolds2() throws Exception {
		SeatHold seatHold = Mockito.mock(SeatHold.class);
		seatHoldList.add(seatHold);

		Mockito.when(SeatHoldRepository.getSeatHoldList()).thenReturn(seatHoldList);
		Mockito.when(seatHold.getHoldStartTime()).thenReturn(LocalDateTime.now().minusSeconds(180));

		HoldValidator.purgeExpiredHolds();
		assertEquals(seatHoldList.size(), 0);
	}

	@Test
	public void testHVAllExpiredHolds3v1() throws Exception {
		SeatHold seatHold1 = Mockito.mock(SeatHold.class);
		SeatHold seatHold2 = Mockito.mock(SeatHold.class);
		SeatHold seatHold3 = Mockito.mock(SeatHold.class);
		SeatHold seatHold4 = Mockito.mock(SeatHold.class);
		SeatHold seatHold5 = Mockito.mock(SeatHold.class);

		seatHoldList.add(seatHold1);
		seatHoldList.add(seatHold2);
		seatHoldList.add(seatHold3);
		seatHoldList.add(seatHold4);
		seatHoldList.add(seatHold5);

		Mockito.when(SeatHoldRepository.getSeatHoldList()).thenReturn(seatHoldList);
		Mockito.when(seatHold1.getHoldStartTime()).thenReturn(LocalDateTime.now().minusMinutes(10));
		Mockito.when(seatHold2.getHoldStartTime()).thenReturn(LocalDateTime.now().minusMinutes(8));
		Mockito.when(seatHold3.getHoldStartTime()).thenReturn(LocalDateTime.now().minusSeconds(400));
		Mockito.when(seatHold4.getHoldStartTime()).thenReturn(LocalDateTime.now().minusSeconds(354));
		Mockito.when(seatHold5.getHoldStartTime()).thenReturn(LocalDateTime.now().minusSeconds(261));

		HoldValidator.purgeExpiredHolds();
		assertEquals(seatHoldList.size(), 0);

	}

	@Test
	public void testHVAllExpiredHolds3v2() throws Exception {
		SeatHold seatHold1 = new SeatHold();
		SeatHold seatHold2 = new SeatHold();
		SeatHold seatHold3 = new SeatHold();
		SeatHold seatHold4 = new SeatHold();
		SeatHold seatHold5 = new SeatHold();
		Seat seat1 = new Seat();
		Seat seat2 = new Seat();
		Seat seat3 = new Seat();
		Seat seat4 = new Seat();
		LinkedList<Seat> seatList1 = new LinkedList<Seat>();
		LinkedList<Seat> seatList2 = new LinkedList<Seat>();
		LinkedList<Seat> seatList3 = new LinkedList<Seat>();
		seatList1.add(seat1);
		seatList1.add(seat2);
		seatList2.add(seat3);
		seatList3.add(seat4);
		seatHold1.setHeldSeatList(seatList1);
		seatHold2.setHeldSeatList(null);
		seatHold3.setHeldSeatList(new LinkedList<Seat>());
		seatHold4.setHeldSeatList(seatList2);
		seatHold5.setHeldSeatList(seatList3);
		seat1.setStatus(SeatStatus.HELD);
		seat2.setStatus(SeatStatus.HELD);
		seat3.setStatus(SeatStatus.HELD);
		seat4.setStatus(SeatStatus.HELD);
		seatHold1.setHoldStartTime(LocalDateTime.now().minusMinutes(10));
		seatHold2.setHoldStartTime(LocalDateTime.now().minusMinutes(8));
		seatHold3.setHoldStartTime(LocalDateTime.now().minusSeconds(400));
		seatHold4.setHoldStartTime(LocalDateTime.now().minusSeconds(354));
		seatHold5.setHoldStartTime(LocalDateTime.now().minusSeconds(261));
		seatHoldList.add(seatHold1);
		seatHoldList.add(seatHold2);
		seatHoldList.add(seatHold3);
		seatHoldList.add(seatHold4);
		seatHoldList.add(seatHold5);

		Mockito.when(SeatHoldRepository.getSeatHoldList()).thenReturn(seatHoldList);

		HoldValidator.purgeExpiredHolds();
		assertEquals(seat1.getStatus(), SeatStatus.AVAILABLE);
		assertEquals(seat2.getStatus(), SeatStatus.AVAILABLE);
		assertEquals(seat3.getStatus(), SeatStatus.AVAILABLE);
		assertEquals(seat4.getStatus(), SeatStatus.AVAILABLE);
	}

	@Test
	public void testHVAllValidHolds1v1() throws Exception {
		SeatHold seatHold = Mockito.mock(SeatHold.class);
		seatHoldList.add(seatHold);

		Mockito.when(SeatHoldRepository.getSeatHoldList()).thenReturn(seatHoldList);
		Mockito.when(seatHoldList.get(0).getHoldStartTime()).thenReturn(LocalDateTime.now());

		HoldValidator.purgeExpiredHolds();
		assertEquals(seatHoldList.size(), 1);
	}

	@Test
	public void testHVAllValidHolds1v2() throws Exception {
		SeatHold seatHold = Mockito.mock(SeatHold.class);
		seatHoldList.add(seatHold);
		Seat seat1 = Mockito.mock(Seat.class);
		LinkedList<Seat> seatList = new LinkedList<Seat>();
		seatList.add(seat1);

		Mockito.when(SeatHoldRepository.getSeatHoldList()).thenReturn(seatHoldList);
		Mockito.when(seatHold.getHoldStartTime()).thenReturn(LocalDateTime.now());
		Mockito.when(seatHold.getHeldSeatList()).thenReturn(seatList);

		HoldValidator.purgeExpiredHolds();
		assertNull(seat1.getStatus());
	}

	@Test
	public void testHVAllValidHolds2() throws Exception {
		SeatHold seatHold = Mockito.mock(SeatHold.class);
		seatHoldList.add(seatHold);

		Mockito.when(SeatHoldRepository.getSeatHoldList()).thenReturn(seatHoldList);
		Mockito.when(seatHoldList.get(0).getHoldStartTime()).thenReturn(LocalDateTime.now().minusSeconds(178));

		HoldValidator.purgeExpiredHolds();
		assertEquals(seatHoldList.size(), 1);
	}

	@Test
	public void testHVAllValidHolds3() throws Exception {
		SeatHold seatHold = Mockito.mock(SeatHold.class);
		seatHoldList.add(seatHold);

		Mockito.when(SeatHoldRepository.getSeatHoldList()).thenReturn(seatHoldList);
		Mockito.when(seatHoldList.get(0).getHoldStartTime()).thenReturn(LocalDateTime.now().minusSeconds(1));

		HoldValidator.purgeExpiredHolds();
		assertEquals(seatHoldList.size(), 1);
	}

	@Test
	public void testHVAllValidHolds4() throws Exception {
		SeatHold seatHold = Mockito.mock(SeatHold.class);
		seatHoldList.add(seatHold);
		seatHoldList.add(seatHold);
		seatHoldList.add(seatHold);
		seatHoldList.add(seatHold);
		seatHoldList.add(seatHold);

		Mockito.when(SeatHoldRepository.getSeatHoldList()).thenReturn(seatHoldList);
		Mockito.when(seatHoldList.get(4).getHoldStartTime()).thenReturn(LocalDateTime.now());
		Mockito.when(seatHoldList.get(3).getHoldStartTime()).thenReturn(LocalDateTime.now().minusSeconds(34));
		Mockito.when(seatHoldList.get(2).getHoldStartTime()).thenReturn(LocalDateTime.now().minusSeconds(150));
		Mockito.when(seatHoldList.get(1).getHoldStartTime()).thenReturn(LocalDateTime.now().minusSeconds(174));
		Mockito.when(seatHoldList.get(0).getHoldStartTime()).thenReturn(LocalDateTime.now().minusSeconds(178));

		HoldValidator.purgeExpiredHolds();
		assertEquals(seatHoldList.size(), 5);
	}

	@Test
	public void testHVPartialValid1() throws Exception {
		SeatHold seatHold1 = Mockito.mock(SeatHold.class);
		SeatHold seatHold2 = Mockito.mock(SeatHold.class);
		seatHoldList.add(seatHold1);
		seatHoldList.add(seatHold2);

		Mockito.when(SeatHoldRepository.getSeatHoldList()).thenReturn(seatHoldList);
		Mockito.when(seatHold1.getHoldStartTime()).thenReturn(LocalDateTime.now().minusSeconds(300));
		Mockito.when(seatHold2.getHoldStartTime()).thenReturn(LocalDateTime.now().minusSeconds(54));

		HoldValidator.purgeExpiredHolds();
		assertEquals(seatHoldList.size(), 1);
	}

	@Test
	public void testHVPartialValid2v1() throws Exception {
		SeatHold seatHold1 = Mockito.mock(SeatHold.class);
		SeatHold seatHold2 = Mockito.mock(SeatHold.class);
		SeatHold seatHold3 = Mockito.mock(SeatHold.class);
		SeatHold seatHold4 = Mockito.mock(SeatHold.class);
		SeatHold seatHold5 = Mockito.mock(SeatHold.class);

		seatHoldList.add(seatHold1);
		seatHoldList.add(seatHold2);
		seatHoldList.add(seatHold3);
		seatHoldList.add(seatHold4);
		seatHoldList.add(seatHold5);

		Mockito.when(SeatHoldRepository.getSeatHoldList()).thenReturn(seatHoldList);
		Mockito.when(seatHold1.getHoldStartTime()).thenReturn(LocalDateTime.now().minusSeconds(357));
		Mockito.when(seatHold2.getHoldStartTime()).thenReturn(LocalDateTime.now().minusSeconds(321));
		Mockito.when(seatHold3.getHoldStartTime()).thenReturn(LocalDateTime.now().minusSeconds(189));
		Mockito.when(seatHold4.getHoldStartTime()).thenReturn(LocalDateTime.now().minusSeconds(154));
		Mockito.when(seatHold5.getHoldStartTime()).thenReturn(LocalDateTime.now().minusSeconds(19));

		HoldValidator.purgeExpiredHolds();
		assertEquals(seatHoldList.size(), 2);
	}

	@Test
	public void testHVPartialValid2v2() throws Exception {
		SeatHold seatHold1 = new SeatHold();
		SeatHold seatHold2 = new SeatHold();
		SeatHold seatHold3 = new SeatHold();
		SeatHold seatHold4 = new SeatHold();
		SeatHold seatHold5 = new SeatHold();
		seatHold1.setHoldStartTime(LocalDateTime.now().minusSeconds(357));
		seatHold2.setHoldStartTime(LocalDateTime.now().minusSeconds(321));
		seatHold3.setHoldStartTime(LocalDateTime.now().minusSeconds(189));
		seatHold4.setHoldStartTime(LocalDateTime.now().minusSeconds(154));
		seatHold5.setHoldStartTime(LocalDateTime.now().minusSeconds(19));

		seatHoldList.add(seatHold1);
		seatHoldList.add(seatHold2);
		seatHoldList.add(seatHold3);
		seatHoldList.add(seatHold4);
		seatHoldList.add(seatHold5);

		Mockito.when(SeatHoldRepository.getSeatHoldList()).thenReturn(seatHoldList);

		HoldValidator.purgeExpiredHolds();
		assertEquals(seatHoldList.size(), 2);
		assertEquals(seatHoldList.get(0), seatHold4);
		assertEquals(seatHoldList.get(1), seatHold5);
	}

	@Test
	public void testHVPartialValid2v3() throws Exception {
		SeatHold seatHold1 = new SeatHold();
		SeatHold seatHold2 = new SeatHold();
		SeatHold seatHold3 = new SeatHold();
		SeatHold seatHold4 = new SeatHold();
		SeatHold seatHold5 = new SeatHold();
		seatHold1.setHoldStartTime(LocalDateTime.now().minusSeconds(357));
		seatHold2.setHoldStartTime(LocalDateTime.now().minusSeconds(321));
		seatHold3.setHoldStartTime(LocalDateTime.now().minusSeconds(189));
		seatHold4.setHoldStartTime(LocalDateTime.now().minusSeconds(154));
		seatHold5.setHoldStartTime(LocalDateTime.now().minusSeconds(19));
		Seat seat1 = new Seat();
		Seat seat2 = new Seat();
		Seat seat3 = new Seat();
		Seat seat4 = new Seat();
		LinkedList<Seat> seatList1 = new LinkedList<Seat>();
		LinkedList<Seat> seatList2 = new LinkedList<Seat>();
		LinkedList<Seat> seatList3 = new LinkedList<Seat>();
		seatList1.add(seat1);
		seatList2.add(seat2);
		seatList2.add(seat3);
		seatList3.add(seat4);
		seatHold1.setHeldSeatList(seatList1);
		seatHold3.setHeldSeatList(seatList2);
		seatHold4.setHeldSeatList(seatList3);
		seat1.setStatus(SeatStatus.HELD);
		seat2.setStatus(SeatStatus.HELD);
		seat3.setStatus(SeatStatus.HELD);
		seat4.setStatus(SeatStatus.HELD);

		seatHoldList.add(seatHold1);
		seatHoldList.add(seatHold2);
		seatHoldList.add(seatHold3);
		seatHoldList.add(seatHold4);
		seatHoldList.add(seatHold5);

		Mockito.when(SeatHoldRepository.getSeatHoldList()).thenReturn(seatHoldList);

		HoldValidator.purgeExpiredHolds();
		assertEquals(seat1.getStatus(), SeatStatus.AVAILABLE);
		assertEquals(seat2.getStatus(), SeatStatus.AVAILABLE);
		assertEquals(seat3.getStatus(), SeatStatus.AVAILABLE);
		assertEquals(seat4.getStatus(), SeatStatus.HELD);
	}
}
