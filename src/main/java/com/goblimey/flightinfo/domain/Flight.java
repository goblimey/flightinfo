package com.goblimey.flightinfo.domain;

import java.time.LocalTime;

/**
 * A Flight contains the data describing a flight - departure time, departure airport and so on.
 * @author simon
 *
 */
public class Flight {
	private LocalTime departureTimeUTC;
	private String destination;
	private String destIATA;
	private String flightNumber;
	
	public Flight(LocalTime departureTimeUTC, String destination, String destIATA, String flightNumber) {
		super();
		this.departureTimeUTC = departureTimeUTC;
		this.destination = destination;
		this.destIATA = destIATA;
		this.flightNumber = flightNumber;
	}

	public LocalTime getDepartureTimeUTC() {
		return departureTimeUTC;
	}

	public void setDepartureTimeUTC(LocalTime departureTimeUTC) {
		this.departureTimeUTC = departureTimeUTC;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getDestIATA() {
		return destIATA;
	}

	public void setDestIATA(String destIATA) {
		this.destIATA = destIATA;
	}

	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

	@Override
	public String toString() {
		return "Flight [departureTimeUTC=" + departureTimeUTC + ", destination=" + destination + ", destIATA="
				+ destIATA + ", flightNumber=" + flightNumber + "]";
	}
}
