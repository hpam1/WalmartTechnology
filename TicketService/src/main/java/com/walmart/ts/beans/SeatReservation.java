package com.walmart.ts.beans;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * 
 * @author Haarthi Padmanabhan
 * 
 * A POJO for representing a SeatReservation
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
	
	public JSONObject getSeatAsJSON() {
		JSONObject obj = new JSONObject();
		if(reservedSeatList != null && reservedSeatList.size() > 0) {
			JSONArray list = new JSONArray();
			for(Seat seat: reservedSeatList) {
				JSONObject seatObj = seat.getSeatAsJSON();
				list.add(seatObj);
			}
			obj.put("Seats", list);
		}
		return obj;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Reservation Id: " + id);
		sb.append("\nCustomer Email: " + customerEmail);
		sb.append("\nStatus: " + message);
		if(reservationTime != null)
			sb.append("\nReservation Time: " + reservationTime);
		if(estimatedCost != null)
			sb.append("\nEstimated Cost: $" + estimatedCost);
		if(reservedSeatList != null && reservedSeatList.size() > 0) {
			sb.append("\n" + getSeatAsJSON().toString());
		}
		
		return sb.toString();
	}
}
