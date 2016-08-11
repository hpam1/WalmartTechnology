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
 * POJO to represent a SeatHold
 * A seatHold is comprised of:
 * 	1. id -> seat hold id for uniquely identify the hold
 * 	2. customerEmail -> the email id of the customer placing the hold
 * 	3. holdStartTime -> the time of placing the hold
 *  4. message -> a string indicating the status of the hold request
 *  5. a list of seats held with this hold
 *  6. the total cost of the seats with this hold
 *  
 */
public class SeatHold {
	private int id;
	private String customerEmail;
	private LocalDateTime holdStartTime;
	private String message;
	private List<Seat> heldSeatList;
	private BigDecimal totalEstimatedCost;
	private static int holdId = 0;
	
	public SeatHold() {
		holdId++;
		this.id = holdId;
		this.heldSeatList = new LinkedList<Seat>();
	}

	// getters and setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
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

	public JSONObject getSeatAsJSON() {
		JSONObject obj = new JSONObject();
		if(heldSeatList != null && heldSeatList.size() > 0) {
			JSONArray list = new JSONArray();
			for(Seat seat: heldSeatList) {
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
		sb.append("Seat Hold Id: " + id);
		sb.append("\nCustomer Email: " + customerEmail);
		sb.append("\nStatus: " + message);
		if(holdStartTime != null)
			sb.append("\nHold Start Time: " + holdStartTime);
		if(totalEstimatedCost != null)
			sb.append("\nEstimated Cost: $" + totalEstimatedCost);
		if(heldSeatList != null && heldSeatList.size() > 0) {
			sb.append("\n" + getSeatAsJSON().toString());
		}
		
		return sb.toString();
	}
	
}
