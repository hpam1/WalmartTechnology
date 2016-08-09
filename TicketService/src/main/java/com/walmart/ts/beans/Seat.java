/**
 * 
 */
package com.walmart.ts.beans;

import java.math.BigDecimal;

import org.json.simple.JSONObject;

/**
 * @author hpam1
 *
 */
public class Seat {
	private long id;
	private int levelId;
	private int rowId;
	private BigDecimal price;
	private SeatStatus status;
	private static long seatId = 0l;
	
	public Seat() {
		seatId++;
		this.id = seatId;
		this.status = SeatStatus.AVAILABLE;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getLevelId() {
		return levelId;
	}
	public void setLevelId(int levelId) {
		this.levelId = levelId;
	}
	public int getRowId() {
		return rowId;
	}
	public void setRowId(int rowId) {
		this.rowId = rowId;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public SeatStatus getStatus() {
		return status;
	}
	public void setStatus(SeatStatus status) {
		this.status = status;
	}
	
	public JSONObject getSeatAsJSON() {
		JSONObject obj = new JSONObject();
		obj.put("Seat Id", id);
		obj.put("Level id", levelId);
		obj.put("Row Id", rowId);
		obj.put("price", price);
		obj.put("status", status);
		return obj;
	}

	@Override
	public String toString() {
		return getSeatAsJSON().toString();
	}
	
}
