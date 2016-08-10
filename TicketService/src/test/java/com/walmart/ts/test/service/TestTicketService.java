/**
 * 
 */
package com.walmart.ts.test.service;

import static org.junit.Assert.assertEquals;

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
import com.walmart.ts.beans.SeatStatus;
import com.walmart.ts.beans.Venue;
import com.walmart.ts.service.TicketServiceImpl;
import com.walmart.ts.utils.SeatDAOUtil;
import com.walmart.ts.utils.VenueConstructor;

/**
 * @author Haarthi Padmanabhan
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({VenueConstructor.class,SeatDAOUtil.class})
@PowerMockIgnore("javax.management.*")
public class TestTicketService {
	private Venue venue;
	private LinkedList<Level> levelList;
	
	@Before
	public void setupVenue() {
		PowerMockito.mockStatic(VenueConstructor.class);
		PowerMockito.mockStatic(SeatDAOUtil.class);
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
	
}
