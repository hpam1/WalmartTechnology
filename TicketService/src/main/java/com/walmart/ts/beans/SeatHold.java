/**
 * 
 */
package com.walmart.ts.beans;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * @author Haarthi Padmanabhan
 *
 */
public class SeatHold {
	private long id;
	private String customerEmail;
	private LocalDateTime holdStartTime;
	private String message;
	private List<Seat> heldSeatList;
	private BigDecimal totalEstimatedCost;
	private static long holdId = 0l;
	
	public SeatHold() {
		holdId++;
		this.id = holdId;
		this.heldSeatList = new LinkedList<Seat>();
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

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public LocalDateTime getHoldStartTime() {
		return holdStartTime;
	}

	public void setHoldStartTime(LocalDateTime holdStartTime) {
		this.holdStartTime = holdStartTime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<Seat> getHeldSeatList() {
		return heldSeatList;
	}

	public void setHeldSeatList(List<Seat> heldSeatList) {
		this.heldSeatList = heldSeatList;
	}
	
	public BigDecimal getTotalEstimatedCost() {
		return totalEstimatedCost;
	}

	public void setTotalEstimatedCost(BigDecimal totalEstimatedCost) {
		this.totalEstimatedCost = totalEstimatedCost;
	}

	public JSONObject getHoldAsJSON() {
		JSONObject obj = new JSONObject();
		obj.put("Id", id);
		obj.put("Customer Email", customerEmail);
		if(holdStartTime != null)
			obj.put("Hold start time", holdStartTime);
		if(heldSeatList != null && heldSeatList.size() > 0) {
			JSONArray list = new JSONArray();
			for(Seat seat: heldSeatList) {
				JSONObject seatObj = seat.getSeatAsJSON();
				list.add(seatObj);
			}
			obj.put("Seats", list);
		}
		if(totalEstimatedCost != null)
			obj.put("Estimated Cost", totalEstimatedCost);
		obj.put("Message", message);
		return obj;
	}

	@Override
	public String toString() {
		return getHoldAsJSON().toString();
	}
	
}
