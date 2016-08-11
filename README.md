Walmart Technology Homework - Ticket Service

This is an implementation of a simple ticket service that facilitates the discovery, temporary hold, and final reservation of seats within a performance venue.

Project Dependency

Java 1.8

Maven 3.5.1

JSON-Simple, version 1.1

Mockito, version 1.10.19

PowerMockito, version 1.6.2

JUnit 4.11 

Assumptions

The level id is an integer value and it is assumed that it can take any integer value. There are no restrictions that the id levels must be consecutive and positive. However, it is assumed that lower levels have lower id values than higher levels. It is also assumed that id of the levels are all unique.

A user can place any number of holds and/or reservations and there does not exist limit on the number of hold a user can place. A hold request will be processed successfully if and only if sufficient number of free seats are available in the user requested levels. Whenever there is a hold request, the service will randomly allocate seats from within the user specified levels.

Reservation request will be processed successfully only if there exists a valid hold.

Configuration

Venue input:

The input about the performance venue is stored in a comma separated file, input.txt, located under main/resources folder of the project. Any change in the venue input must be updated in this file. This file is of the form:
	Level Id, Level Name, Price, Rows in the level, Seats per row
e.g	1,Orchestra,100.00,25,50

Each line in this file represents the details of a level. 

Hold expiration time:

Each hold request is short-lived. The request has to be reserved or else it will expire within a set number of seconds. The default threshold time is 60 seconds. This threshold time information can also be configured in the file main/resources/TSProperties.properties, by the setting value for “HoldExpirationThresholdInSeconds” property. This property specifies the threshold time in seconds.

Execution

To build the project:
	mvn clean package

This will produce two jar files in the target folder. The jar, TicketService-jar-with-dependencies.jar, is an executable jar file with all the dependent jars included with it. It can be executed in a command line (please make sure JAVA_HOME environment variable is set), with the command:
	
	java -jar TicketService-jar-with-dependencies.jar 

In case of any change in the input configuration, the project has to be built again and executed in order for the changes to be reflected.
 
