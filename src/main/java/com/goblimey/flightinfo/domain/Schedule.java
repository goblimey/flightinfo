package com.goblimey.flightinfo.domain;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.goblimey.flightinfo.FlightinfoApplication;

/**
 * The Schedule holds the schedule for a flightNumber.
 * @author simon
 *
 */

public class Schedule {
	private LocalTime departureTimeUTC;
	private String destination;
	private String destIATA;
	private String flightNumber;
	private boolean fliesOnDay[];			// One for every day of the week starting on Sunday.
	
	private static final int expectedNumberOfFields = 11;
	
	private static final Logger log = LoggerFactory.getLogger(Schedule.class);
	
	public Schedule(LocalTime departureTimeUTC, String destination, String destIATA, String flightNumber,
			boolean[] fliesOnDay) {
		super();
		this.departureTimeUTC = departureTimeUTC;
		this.destination = destination;
		this.destIATA = destIATA;
		this.flightNumber = flightNumber;
		this.fliesOnDay = fliesOnDay;
		
	}
	
	/**
	 * Factory method to create a Schedule from an array of text fields.
	 * The fields are assumed to be read from a CSV and there should be 11 of them,
	 * for example: "09:00","Antigua","ANU","VS033","x","","x","","","",""
	 * represents a flight that departs at 9am UTC on a Sunday and a Tuesday.
	 * @param fields the array of text fields
	 * @return the Schedule
	 * @throws IOException if there are not exactly 11 fields or there is a syntax error.
	 */
	public static Schedule createScheduleFromFields(String[] fields) throws IOException {
		if (fields.length != expectedNumberOfFields) {
			throw new IOException(
					"each line must have " + expectedNumberOfFields + " fields found " + fields.length);
		}
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime departureTime = null;
		try {
			departureTime = LocalTime.parse(fields[0], formatter);
		} catch (DateTimeParseException e) {
			throw new IOException("illegal time spec " + fields[0], e);
		}
		
		// Create fliesOnDay booleans from fields 4-10.
		boolean[] fliesOnDay = new boolean[7];
		for (int i = 0; i < 7; i++) {
			if ("x".contentEquals(fields[i+4])) {
				fliesOnDay[i] = true;
			}
		}
		
		return new Schedule(departureTime, fields[1], fields[2], fields[3], fliesOnDay);
	}
	
	/**
	 * Factory method to read a list of String field arrays and create an array
	 * of schedules.  It's assumed that the fields are created by reading a CSV
	 * file and the first line is a header:
	 *     Departure Time,Destination, .....
	 * Any exceptions from creating a schedule are ignored.
	 * @param fieldList
	 * @return the resulting schedules.
	 */
	public static Schedule[] createSchedulesFromFields(List<String[]> fieldList) {
		List<Schedule> scheduleList = new ArrayList<Schedule>();
		
		for (String[] fields: fieldList) {
			try {
				if ("Departure Time".contentEquals(fields[0])) {
					// Ignore the header line.
					continue;
				}
				Schedule schedule = Schedule.createScheduleFromFields(fields);
				scheduleList.add(schedule);
				
			} catch (IOException e) {
				log.error("error in input file - {}", e.getMessage());
			}
		}
		// Convert the result to an array of Schedule objects sorted on departure time.
		Schedule[] schedules = new Schedule[1];
		schedules = scheduleList.toArray(schedules);
		Arrays.sort(schedules, (first, second) -> first.getDepartureTimeUTC().compareTo(second.getDepartureTimeUTC()));
		return schedules;
	}

	public LocalTime getDepartureTimeUTC() {
		return departureTimeUTC;
	}

	public String getDestination() {
		return destination;
	}

	public String getDestIATA() {
		return destIATA;
	}

	public String getFlightNumber() {
		return flightNumber;
	}

	public boolean[] getFliesOnDay() {
		return fliesOnDay;
	}

	@Override
	public String toString() {
		return "Schedule [departureTimeUTC=" + departureTimeUTC + ", destination=" + destination + ", destIATA="
				+ destIATA + ", flightNumber=" + flightNumber + ", fliesOnDay=" + Arrays.toString(fliesOnDay) + "]";
	}
}
