package com.walmart.ts.test.utils;

import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Test;
import org.mockito.Mockito;

import com.walmart.ts.beans.SeatHold;
import com.walmart.ts.beans.SeatReservation;
import com.walmart.ts.utils.MessageConstants;
import com.walmart.ts.utils.TicketServiceUtils;


/**
 * 
 * @author Haarthi Padmanabhan
 *
 */
public class TestTicketServiceUtils {
	
	@Test
	public void testSeatHoldValidateInvalidNumOfSeats1() {
		SeatHold sh = Mockito.mock(SeatHold.class);
		
		boolean result = TicketServiceUtils.validateSeatHoldInput(sh, -9, Optional.ofNullable(null), Optional.ofNullable(null), "test");
		assertFalse(result);
	}
	
	@Test
	public void testSeatHoldValidateInvalidNumOfSeats2() {
		SeatHold sh = new SeatHold();
		
		TicketServiceUtils.validateSeatHoldInput(sh, -9, Optional.ofNullable(null), Optional.ofNullable(null), "test");
		assertEquals(sh.getId(), -1);
		assertEquals(sh.getMessage(), MessageConstants.REQ_NO_SEATS_INVALID);
	}
	
	@Test
	public void testSeatHoldValidateInvalidNumOfSeats3() {
		SeatHold sh = Mockito.mock(SeatHold.class);
		
		boolean result = TicketServiceUtils.validateSeatHoldInput(sh, 0, Optional.ofNullable(null), Optional.ofNullable(null), "test");
		assertFalse(result);
	}
	
	@Test
	public void testSeatHoldValidateNoEmail1() {
		SeatHold sh = Mockito.mock(SeatHold.class);
			
		boolean result = TicketServiceUtils.validateSeatHoldInput(sh, 1, Optional.ofNullable(null), Optional.ofNullable(null), null);
		assertFalse(result);
	}
	
	@Test
	public void testSeatHoldValidateNoEmail2() {
		SeatHold sh = Mockito.mock(SeatHold.class);
			
		boolean result = TicketServiceUtils.validateSeatHoldInput(sh, 1, Optional.ofNullable(null), Optional.ofNullable(null), "");
		assertFalse(result);
	}
	
	@Test
	public void testSeatHoldValidateNoEmail3() {
		SeatHold sh = new SeatHold();
			
		TicketServiceUtils.validateSeatHoldInput(sh, 1, Optional.ofNullable(null), Optional.ofNullable(null), null);
		assertEquals(sh.getId(), -1);
		assertEquals(sh.getMessage(), MessageConstants.EMAIL_NULL);
	}
	
	@Test
	public void testSeatHoldValidateInvalidLevel1() {
		SeatHold sh = Mockito.mock(SeatHold.class);
			
		boolean result = TicketServiceUtils.validateSeatHoldInput(sh, 1, Optional.ofNullable(-1), Optional.ofNullable(-5), "test");
		assertFalse(result);
	}
	
	@Test
	public void testSeatHoldValidateInvalidLevel2() {
		SeatHold sh = new SeatHold();
			
		TicketServiceUtils.validateSeatHoldInput(sh, 1, Optional.ofNullable(-1), Optional.ofNullable(-5), "test");
		assertEquals(sh.getId(), -1);
		assertEquals(sh.getMessage(), MessageConstants.INVALID_LEVELS);
	}
	
	@Test
	public void testSeatHoldValidateValidIp() {
		SeatHold sh = Mockito.mock(SeatHold.class);
		
		boolean result = TicketServiceUtils.validateSeatHoldInput(sh, 1, Optional.ofNullable(-1), Optional.ofNullable(5), "test");
		assertTrue(result);
	}
	
	@Test
	public void testSRValidateInvalidHoldId1() {
		SeatReservation sr = Mockito.mock(SeatReservation.class);
		
		boolean result = TicketServiceUtils.validateReserveSeatInput(sr, 0, "");
		assertFalse(result);
	}
	
	@Test
	public void testSRValidateInvalidHoldId2() {
		SeatReservation sr = new SeatReservation();
		
		TicketServiceUtils.validateReserveSeatInput(sr, 0, "");
		assertEquals(sr.getId(), -1);
		assertEquals(sr.getMessage(), MessageConstants.INVALID_HOLD_ID);
	}
	
	@Test
	public void testSRValidateNullEmail1() {
		SeatReservation sr = Mockito.mock(SeatReservation.class);
		
		boolean result = TicketServiceUtils.validateReserveSeatInput(sr, 4, "");
		assertFalse(result);
	}
	
	@Test
	public void testSRValidateNullEmail2() {
		SeatReservation sr = new SeatReservation();
		
		TicketServiceUtils.validateReserveSeatInput(sr, 4, "  ");
		assertEquals(sr.getId(), -1);
		assertEquals(sr.getMessage(), MessageConstants.EMAIL_NULL);
	}
	
	@Test
	public void testSRValidateNullEmail3() {
		SeatReservation sr = Mockito.mock(SeatReservation.class);
		
		boolean result = TicketServiceUtils.validateReserveSeatInput(sr, 4, null);
		assertFalse(result);
	}
	
	@Test
	public void testSRValidateValidIp() {
		SeatReservation sr = Mockito.mock(SeatReservation.class);
		
		boolean result = TicketServiceUtils.validateReserveSeatInput(sr, 4, "test");
		assertTrue(result);
	}
	
}
