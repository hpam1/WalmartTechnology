package com.walmart.ts.client;

import java.util.Optional;
import java.util.Scanner;

import com.walmart.ts.beans.SeatHold;
import com.walmart.ts.service.TicketService;
import com.walmart.ts.service.TicketServiceImpl;

/**
 * 
 * @author Haarthi Padmanabhan
 * 
 * A simple client to run the ticket service
 *
 */
public class TicketServiceClient {
	
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		TicketService ts = new TicketServiceImpl();
		System.out.println("Welcome to Ticket Reservation Service");
		String message = "Please choose from the following options \n(i) To enquire about seat availablility -> 1 <<level>>\n"
				+ "(ii) To place hold on seats -> 2 numOfSeats <<minLevel>> <<maxLevel>> emailId\n"
				+ "(iii) To reserve seats -> 3 holdId emailId\n" + "(iv) To exit -> 4" 
				+ "The parameters specified withing <<>> are optional. "
				+ "You can specify them or leave them with a single space.\n";
		System.out.println(message);
		while(true) {
			try {
				String[] splitCommand = in.nextLine().split(" ");
				int command = Integer.parseInt(splitCommand[0]);
				switch(command) {
				case 1:
					Integer level = null;
					if(splitCommand.length == 2 && splitCommand[1] != null && splitCommand[1].trim().length() > 0)
						level = Integer.parseInt(splitCommand[1]);
					int seatsAvail = ts.numSeatsAvailable(Optional.ofNullable(level));
					System.out.println("The number of seats available: " + seatsAvail);
					break;
				case 2:
					Integer minLevel = null;
					Integer maxLevel = null;
					if(splitCommand.length != 5) {
						System.out.println("Invalid input format. Type 2 numOfSeats <<minLevel>> <<maxLevel>> emailId");
						break;
					}
					int noOfSeats = Integer.parseInt(splitCommand[1]);
					if(splitCommand[2] != null && splitCommand[2].trim().length() > 0)
						minLevel = Integer.parseInt(splitCommand[2]);
					if(splitCommand[3] != null && splitCommand[3].trim().length() > 0)
						maxLevel = Integer.parseInt(splitCommand[3]);
					SeatHold sh = ts.findAndHoldSeats(noOfSeats, Optional.ofNullable(minLevel), Optional.ofNullable(maxLevel), splitCommand[4]);
					System.out.println(sh.toString());
					break;
				case 3:
					if(splitCommand.length != 3) {
						System.out.println("Invalid input format. Type 3 holdId emailId");
					}
					int holdId = Integer.parseInt(splitCommand[1]);
					String result = ts.reserveSeats(holdId, splitCommand[2]);
					System.out.println(result);
					break;
				case 4:
					return;
				}
			} catch(Exception e) {
				System.out.println("Invalid input format");
			}
			System.out.println(message);
		}
	}

}
