package com.walmart.ts.beans;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * 
 * @author Haarthi Padmanabhan
 * 
 */
public class SeatReservation {
	private long id;
	private String customerEmail;
	private LocalDateTime reservationTime;
	private String message;
	private BigDecimal estimatedCost;
	private List<Seat> reservedSeatList;
	private static long reservationId = 0l;
	
	public SeatReservation() {
		reservationId++;
		this.id = reservationId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String email) {
		this.customerEmail = email;
	}

	public LocalDateTime getReservationTime() {
		return reservationTime;
	}

	public void setReservationTime(LocalDateTime reservationTime) {
		this.reservationTime = reservationTime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public BigDecimal getEstimatedCost() {
		return estimatedCost;
	}

	public void setEstimatedCost(BigDecimal estimatedCost) {
		this.estimatedCost = estimatedCost;
	}

	public List<Seat> getReservedSeatList() {
		return reservedSeatList;
	}

	public void setReservedSeatList(List<Seat> reservedSeatList) {
		this.reservedSeatList = reservedSeatList;
	}
	
	public JSONObject getHoldAsJSON() {
		JSONObject obj = new JSONObject();
		obj.put("Id", id);
		obj.put("Customer Email", customerEmail);
		if(reservationTime != null)
			obj.put("Reserved at", reservationTime);
		if(reservedSeatList != null && reservedSeatList.size() > 0) {
			JSONArray list = new JSONArray();
			for(Seat seat: reservedSeatList) {
				JSONObject seatObj = seat.getSeatAsJSON();
				list.add(seatObj);
			}
			obj.put("Seats", list);
		}
		if(estimatedCost != null)
			obj.put("Estimated Cost", estimatedCost);
		obj.put("message", message);
		return obj;
	}

	@Override
	public String toString() {
		return getHoldAsJSON().toString();
	}
}
