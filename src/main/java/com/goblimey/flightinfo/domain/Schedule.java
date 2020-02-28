package com.goblimey.flightinfo.domain;

import java.io.IOException;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Schedule holds the schedule for a flight.
 * 
 * @author Simon Ritchie
 *
 */

public class Schedule {
	private LocalTime departureTimeUTC;
	private String destination;
	private String destIATA;
	private String flightNumber;
	// One entry for every day of the week starting on Sunday, true if flying that day.
	private boolean fliesOnDay[]; 

	private static final int minimumNumberOfFields = 5;

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
	 * Factory method to create a Schedule from an array of text fields. The fields
	 * are assumed to be read from a CSV and there should be at least of them, for
	 * example: "09:00","Antigua","ANU","VS033","x","","x","","","","" represents a
	 * flight that departs at 9am UTC on a Sunday and a Tuesday.  That could also be:
	 * "09:00","Antigua","ANU","VS033","x","","x"
	 * 
	 * @param fields the array of text fields
	 * @return the Schedule
	 * @throws IOException if there are not at least five fields or there is a syntax
	 *                     error.
	 */
	public static Schedule createScheduleFromFields(String[] fields) throws IOException {
		if (fields.length < minimumNumberOfFields) {
			throw new IOException("each line must have at least " + minimumNumberOfFields + " fields - found " + fields.length);
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime departureTime = null;
		try {
			departureTime = LocalTime.parse(fields[0].trim(), formatter);
		} catch (DateTimeParseException e) {
			throw new IOException("illegal time spec " + fields[0], e);
		}
		
		String destination = fields[1].trim();
		String destIATA = fields[2].trim();
		String flightNumber = fields[3].trim();

		// Create fliesOnDay booleans from fields 4-10.  Trailing
		// fields may be missing from the fields array.
		boolean[] fliesOnDay = new boolean[7];
		for (int i = 4, j = 0; i < 11; i++, j++) {
			if (i < fields.length && "x".contentEquals(fields[i].trim())) {
				fliesOnDay[j] = true;
			}
		}

		return new Schedule(departureTime, destination, destIATA, flightNumber, fliesOnDay);
	}

	/**
	 * Factory method to read a list of String field arrays and create an array of
	 * schedules. It's assumed that the fields are created by reading a CSV file and
	 * the first line is a header: "Departure Time,Destination, ....." and such lines
	 * are ignored. Any exceptions from createScheduleFromFields are caught and
	 * ignored, so that line of the CSV will be lost.
	 * 
	 * @param fieldList
	 * @return the resulting schedules.
	 */
	public static Schedule[] createSchedulesFromFields(List<String[]> fieldList) {
		List<Schedule> scheduleList = new ArrayList<Schedule>();

		for (int i = 0; i < fieldList.size(); i++) {
			String[] fields = fieldList.get(i);
			try {
				if ("Departure Time".contentEquals(fields[0])) {
					// Ignore the header line.
					continue;
				}
				Schedule schedule = Schedule.createScheduleFromFields(fields);
				scheduleList.add(schedule);

			} catch (IOException e) {
				log.error("error in input at row {}:{} - {}", i, fields, e.getMessage());
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

	/**
	 * Returns an array 0-7, true if flying that day.
	 * Sunday is day 0, Monday is day 1, and so on.
	 * @return the array showing which days are included in the schedule.
	 */
	public boolean[] getFliesOnDay() {
		return fliesOnDay;
	}

	/**
	 * Return a list of flights departing on the given date, which is assumed to be
	 * in the UTC timezone.
	 * 
	 * @param schedules a list of Schedule objects.
	 * @param dateStr   "yyyy-mm-dd", UTC timezone assumed
	 * @return an array containing the Flight objects.
	 */
	public static Flight[] getFlightsOnDate(Schedule[] schedules, String dateStr) throws DateTimeParseException {
		// Get the date, UTC assumed.
		dateStr += " 00:00:00 UTC";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
		ZonedDateTime date = ZonedDateTime.parse(dateStr, formatter);
		// Get the index from the day of the day - Sunday is index 0, Monday 1 and so
		// on.
		int dayOfWeek = date.getDayOfWeek().getValue();
		// If the dayOfWeek is Sunday its value is 7. Index must be 0. Otherwise it's correct.
		final int index = dayOfWeek == 7 ? 0 : dayOfWeek;
		// Get the schedules which apply on the given date.
		Schedule[] s = Stream.of(schedules).filter(schedule -> schedule.getFliesOnDay()[index]).toArray(Schedule[]::new);
		
		// Get the flights for those schedules.
		Flight[] flights = Stream.of(s).map(schedule -> new Flight(schedule.getDepartureTimeUTC(), schedule.getDestination(),
				schedule.getDestIATA(), schedule.getFlightNumber())).toArray(Flight[]::new);
		
		return flights;
	}

	@Override
	public String toString() {
		return "Schedule [departureTimeUTC=" + departureTimeUTC + ", destination=" + destination + ", destIATA="
				+ destIATA + ", flightNumber=" + flightNumber + ", fliesOnDay=" + Arrays.toString(fliesOnDay) + "]";
	}
}
