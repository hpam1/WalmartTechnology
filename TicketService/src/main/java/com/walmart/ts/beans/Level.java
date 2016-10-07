/**
 * 
 */
package com.walmart.ts.beans;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * @author Haarthi Padmanabhan
 * 
 *  A POJO to represent the levels in the venue
 *
 */
public class Level {
	private int id;
	private String name;
	private int noOfRows;
	private List<Seat> seatList;

	// getters and setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNoOfRows() {
		return noOfRows;
	}

	public void setNoOfRows(int noOfRows) {
		this.noOfRows = noOfRows;
	}

	public List<Seat> getSeatList() {
		return seatList;
	}

	public void setSeatList(List<Seat> seatList) {
		this.seatList = seatList;
	}

	public JSONObject getLevelAsJSON() {
		JSONObject obj = new JSONObject();
		obj.put("Id", id);
		obj.put("Name", name);
		obj.put("No of Rows", noOfRows);
		JSONArray list = new JSONArray();
		for (Seat seat : seatList) {
			JSONObject seatObj = seat.getSeatAsJSON();
			list.add(seatObj);
		}
		obj.put("Seats", list);
		System.out.println("hello");
		return obj;
	}

	@Override
	public String toString() {
		return getLevelAsJSON().toString();
	}

}
