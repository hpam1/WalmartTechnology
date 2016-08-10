package com.walmart.ts.beans;

/**
 * 
 * @author Haarthi Padmanabhan
 *
 * An enum that represents the various statuses a seat can be in. A seat can be
 * 1. Available -> when it is free and open for reservation
 * 2. Held -> the seat is temporarily held by another customer and will be released to the free pool
 * 			  if it is not reserved within a specific threshold time limit
 * 3. Reserved -> the seat is reserved to a customer
 */
public enum SeatStatus {
	AVAILABLE, HELD, RESERVED;
}
