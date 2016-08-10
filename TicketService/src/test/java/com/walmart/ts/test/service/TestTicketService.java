/**
 * 
 */
package com.walmart.ts.test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.walmart.ts.beans.Level;
import com.walmart.ts.beans.Seat;
import com.walmart.ts.beans.SeatHold;
import com.walmart.ts.beans.SeatHoldRepository;
import com.walmart.ts.beans.SeatStatus;
import com.walmart.ts.beans.Venue;
import com.walmart.ts.service.HoldValidator;
import com.walmart.ts.service.TicketServiceImpl;
import com.walmart.ts.utils.MessageConstants;
import com.walmart.ts.utils.SeatDAOUtil;
import com.walmart.ts.utils.TicketServiceUtils;
import com.walmart.ts.utils.VenueConstructor;

/**
 * @author Haarthi Padmanabhan
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({VenueConstructor.class,SeatDAOUtil.class, HoldValidator.class, SeatHoldRepository.class})
@PowerMockIgnore("javax.management.*")
public class TestTicketService {
	private Venue venue;
	private LinkedList<Level> levelList;
	
	@Before
	public void setupVenue() {
		PowerMockito.mockStatic(VenueConstructor.class);
		PowerMockito.mockStatic(SeatDAOUtil.class);
		PowerMockito.mockStatic(HoldValidator.class);
		PowerMockito.mockStatic(SeatHoldRepository.class);
		venue = Mockito.mock(Venue.class);
		levelList = new LinkedList<Level>();
	}
	
	@After
	public void verify() {
		PowerMockito.verifyStatic();
		VenueConstructor.getVenue();
	}
	
	@Test
	public void testCountAvailTicket1() throws Exception {
		Mockito.when(VenueConstructor.getVenue()).thenReturn(venue);
		Mockito.when(venue.getVenueLevels()).thenReturn(null);
		Mockito.when(SeatDAOUtil.countAvailSeatInVenue(venue)).thenReturn(5);
		
		TicketServiceImpl ts = new TicketServiceImpl();
		int seatsAvail = ts.numSeatsAvailable(Optional.ofNullable(null));
		assertEquals(seatsAvail, 5);
		
		PowerMockito.verifyStatic();
		SeatDAOUtil.countAvailSeatInVenue(venue);
	}
	
	@Test
	public void testCountAvailTicket2() throws Exception {
		Mockito.when(VenueConstructor.getVenue()).thenReturn(venue);
		Mockito.when(venue.getVenueLevels()).thenReturn(null);
		Mockito.when(SeatDAOUtil.countAvailSeatInLevel(venue, 2)).thenReturn(0);
		
		TicketServiceImpl ts = new TicketServiceImpl();
		int seatsAvail = ts.numSeatsAvailable(Optional.ofNullable(2));
		assertEquals(seatsAvail, 0);
		
		PowerMockito.verifyStatic();
		SeatDAOUtil.countAvailSeatInLevel(venue, 2);
	}
	
	@Test
	public void testCountAvailTicket3() throws Exception {
		Mockito.when(VenueConstructor.getVenue()).thenReturn(venue);
		Mockito.when(venue.getVenueLevels()).thenReturn(null);
		Mockito.when(SeatDAOUtil.countAvailSeatInLevel(venue, -3)).thenReturn(50);
		
		TicketServiceImpl ts = new TicketServiceImpl();
		int seatsAvail = ts.numSeatsAvailable(Optional.ofNullable(-3));
		assertEquals(seatsAvail, 50);
		
		PowerMockito.verifyStatic();
		SeatDAOUtil.countAvailSeatInLevel(venue, -3);
	}
	
	@Test
	public void testHoldService1() throws Exception {
		Mockito.when(VenueConstructor.getVenue()).thenReturn(venue);
		Mockito.when(venue.getVenueLevels()).thenReturn(null);
		
		TicketServiceImpl ts = new TicketServiceImpl();
		SeatHold sh = ts.findAndHoldSeats(4, Optional.ofNullable(null), Optional.ofNullable(null), "test@test.com");
		assertEquals(sh.getMessage(), MessageConstants.INSUFFICIENT_SEATS);
		assertEquals(sh.getId(), -1);
	}
	
	@Test
	public void testHoldService2() throws Exception {
		Mockito.when(VenueConstructor.getVenue()).thenReturn(venue);
		Mockito.when(venue.getVenueLevels()).thenReturn(levelList);
		
		TicketServiceImpl ts = new TicketServiceImpl();
		SeatHold sh = ts.findAndHoldSeats(4, Optional.ofNullable(null), Optional.ofNullable(null), "test@test.com");
		assertEquals(sh.getMessage(), MessageConstants.INSUFFICIENT_SEATS);
		assertEquals(sh.getId(), -1);
	}
	
	@Test
	public void testHoldService3() throws Exception {
		Seat seat = Mockito.mock(Seat.class);
		Mockito.when(VenueConstructor.getVenue()).thenReturn(venue);
		Mockito.when(SeatDAOUtil.countAvailSeatsWithinLevels(venue, Optional.ofNullable(null), Optional.ofNullable(null))).thenReturn(50);
		Mockito.when(SeatDAOUtil.holdARandomSeat(venue, Optional.ofNullable(null), Optional.ofNullable(null))).thenReturn(seat);
		
		TicketServiceImpl ts = new TicketServiceImpl();
		SeatHold sh = ts.findAndHoldSeats(1, Optional.ofNullable(null), Optional.ofNullable(null), "test@test.com");
		assertEquals(sh.getMessage(), MessageConstants.SUCCESS_PROCESSING_REQUEST);
		assertNotNull(sh.getHeldSeatList());
		assertEquals(sh.getHeldSeatList().size(), 1);
	}
	
	@Test
	public void testHoldService4() throws Exception {
		Seat seat = Mockito.mock(Seat.class);
		Mockito.when(VenueConstructor.getVenue()).thenReturn(venue);
		Mockito.when(SeatDAOUtil.countAvailSeatsWithinLevels(venue, Optional.ofNullable(null), Optional.ofNullable(5))).thenReturn(0);
		Mockito.when(SeatDAOUtil.holdARandomSeat(venue, Optional.ofNullable(null), Optional.ofNullable(5))).thenReturn(seat);
		
		TicketServiceImpl ts = new TicketServiceImpl();
		SeatHold sh = ts.findAndHoldSeats(2, Optional.ofNullable(null), Optional.ofNullable(5), "test@test.com");
		assertEquals(sh.getMessage(), MessageConstants.INSUFFICIENT_SEATS);
		assertEquals(sh.getHeldSeatList().size(), 0);
	}
	
	@Test
	public void testHoldService5() throws Exception {
		Seat seat = Mockito.mock(Seat.class);
		Mockito.when(VenueConstructor.getVenue()).thenReturn(venue);
		Mockito.when(SeatDAOUtil.countAvailSeatsWithinLevels(venue, Optional.ofNullable(null), Optional.ofNullable(5))).thenReturn(2);
		Mockito.when(SeatDAOUtil.holdARandomSeat(venue, Optional.ofNullable(null), Optional.ofNullable(5))).thenReturn(seat);
		
		TicketServiceImpl ts = new TicketServiceImpl();
		SeatHold sh = ts.findAndHoldSeats(2, Optional.ofNullable(null), Optional.ofNullable(5), "test@test.com");
		assertEquals(sh.getMessage(), MessageConstants.SUCCESS_PROCESSING_REQUEST);
		assertEquals(sh.getHeldSeatList().size(), 2);
	}
	
	@Test
	public void testHoldService6() throws Exception {
		Seat seat = Mockito.mock(Seat.class);
		Mockito.when(VenueConstructor.getVenue()).thenReturn(venue);
		Mockito.when(SeatDAOUtil.countAvailSeatsWithinLevels(venue, Optional.ofNullable(2), Optional.ofNullable(5))).thenReturn(4);
		Mockito.when(SeatDAOUtil.holdARandomSeat(venue, Optional.ofNullable(2), Optional.ofNullable(5))).thenReturn(seat);
		
		TicketServiceImpl ts = new TicketServiceImpl();
		SeatHold sh = ts.findAndHoldSeats(3, Optional.ofNullable(2), Optional.ofNullable(5), "test@test.com");
		assertEquals(sh.getMessage(), MessageConstants.SUCCESS_PROCESSING_REQUEST);
		assertEquals(sh.getHeldSeatList().size(), 3);
	}
	
	@Test
	public void testReserveService1() throws Exception {
		SeatHold seatHold = Mockito.mock(SeatHold.class);
		Mockito.when(VenueConstructor.getVenue()).thenReturn(venue);
		Mockito.when(SeatDAOUtil.getSeatHoldListFor(10, "test@test.com")).thenReturn(seatHold);
		
		TicketServiceImpl ts = new TicketServiceImpl();
		String result = ts.reserveSeats(10, "test@test.com");
		if(result.contains(MessageConstants.SUCCESS_PROCESSING_REQUEST))
			assertTrue(true);
		else
			assertTrue(false);
	}
	
	@Test
	public void testReserveService2() throws Exception {
		Mockito.when(VenueConstructor.getVenue()).thenReturn(venue);
		Mockito.when(SeatDAOUtil.getSeatHoldListFor(10, "test@test.com")).thenReturn(null);
		
		TicketServiceImpl ts = new TicketServiceImpl();
		String result = ts.reserveSeats(10, "test@test.com");
		if(result.contains(MessageConstants.INVALID_SEAT_HOLD))
			assertTrue(true);
		else
			assertTrue(false);
	}
}


