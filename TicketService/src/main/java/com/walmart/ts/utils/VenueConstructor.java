package com.walmart.ts.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import com.walmart.ts.beans.Level;
import com.walmart.ts.beans.Seat;
import com.walmart.ts.beans.Venue;

/**
 * 
 * @author Haarthi Padmanabhan
 *
 * A Utility class that reads in the input file located in resources/input.txt and constructs 
 * the venue object.
 * 
 * The input file is a csv file of the form:
 * 	Levelid, Name, price, rows in the level, seats per row
 * 
 * The venue once constructed is valid for the entire session and any change in the input
 * will require a restart of the session
 * 
 */
public class VenueConstructor {
	private static Venue venue;
	
	// get the venue object
	public static Venue getVenue() {
		if(venue == null)
			generateVenue();
		return venue;
	}
	
	// construct the venue object
	private static void generateVenue() {
		String fileName = "input.txt";
		ClassLoader classLoader = VenueConstructor.class.getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());
		List<Level> levelList = new LinkedList<Level>();
		
		try(BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] splitInput = line.split(",");
				int levelId = Integer.parseInt(splitInput[0]);
				String levelName = splitInput[1];
				BigDecimal price = new BigDecimal(splitInput[2]);
				int noOfRows = Integer.parseInt(splitInput[3]);
				int seatsPerRow = Integer.parseInt(splitInput[4]);
				List<Seat> seatList = new LinkedList<Seat>();
				for(int i = 1; i <= noOfRows; i++) {
					for(int j = 1; j <= seatsPerRow; j++) {
						Seat seat = new Seat();
						seat.setLevelId(levelId);
						seat.setPrice(price);
						seat.setRowId(i);
						seatList.add(seat);
					}
				}
				Level level = new Level();
				level.setId(levelId);
				level.setName(levelName);
				level.setNoOfRows(noOfRows);
				level.setSeatList(seatList);
				levelList.add(level);
			}
			venue = new Venue();
			venue.setVenueLevels(levelList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
