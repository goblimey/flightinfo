package com.goblimey.flightinfo.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class ScheduleTests {

	@Test
	void testScheduleInput() {
		List<String[]> fieldList = new ArrayList<String[]>();
		String[] fields0 = {"Departure Time","Destination","Destination Airport IATA",
				"Flight No","Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
		
		fieldList.add(fields0);
		String[] fields1 = {"10:05","Antigua","ANU","VS033","","","","","x","","x"};
		fieldList.add(fields1);
		String[] fields2 = {"09:00","St Lucia","UVF","VS097","","","x","","","",""};
		fieldList.add(fields2);
		
		Schedule[] schedules = Schedule.createSchedulesFromFields(fieldList);
		
		// The result is sorted so the 9am flight comes first.
		assertEquals(9, schedules[0].getDepartureTimeUTC().getHour());
		assertEquals(0, schedules[0].getDepartureTimeUTC().getMinute());
		assertEquals("St Lucia", schedules[0].getDestination());
		assertEquals("UVF", schedules[0].getDestIATA());
		assertEquals(7, schedules[0].getFliesOnDay().length);
		assertFalse(schedules[0].getFliesOnDay()[0]);
		assertFalse(schedules[0].getFliesOnDay()[1]);
		assert(schedules[0].getFliesOnDay()[2]);
		assertFalse(schedules[0].getFliesOnDay()[3]);
		assertFalse(schedules[0].getFliesOnDay()[4]);
		assertFalse(schedules[0].getFliesOnDay()[5]);
		assertFalse(schedules[0].getFliesOnDay()[6]);
		
		
		assertEquals(10, schedules[1].getDepartureTimeUTC().getHour());
		assertEquals(5, schedules[1].getDepartureTimeUTC().getMinute());
	}
}
