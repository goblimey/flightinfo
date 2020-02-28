package com.goblimey.flightinfo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.goblimey.flightinfo.domain.Flight;
import com.goblimey.flightinfo.domain.Schedule;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FlightsController provides a web interface that, given a date in yyyy-mm-dd format,
 * provides a list of flights:
 * 
 *   GET /flights/yyyy-mm-dd produces HTML showing the flights for that day.
 *   
 * @Author Simon Ritchie
 */

@Controller
@RequestMapping("/flights")
public class FlightsController {

	private Schedule[] schedules;
	private Logger log = LoggerFactory.getLogger(FlightsController.class);
	
	public FlightsController() {
		
		// Get the CSV.
		List<String[]> fieldList = getCSV("flights.csv");
		
		if (fieldList == null) {
			log.error("failed to get flight schedules");
			return;
		}
		
		// Create the schedules from those data.
		schedules = Schedule.createSchedulesFromFields(fieldList);
	}
	
	@GetMapping("/{date}")
	public ModelAndView getFlightList(@PathVariable("date") String date) throws Exception {
		
		Flight[] flights = Schedule.getFlightsOnDate(schedules, date);
		return new ModelAndView("flights", "flights", flights);
	}
	
	
	/**
	 * Gets the schedules.  (Required by the startup code in the main program.)
	 * @return the schedules.
	 */
	public Schedule[] getSchedules() {
		return schedules;
	}
	
	// Reads the CSV and returns the contents as a list, one per line, of
	// String arrays, one entry per CSV field.
	private List<String[]> getCSV(String fileName) {
		// The CSV must be in the current directory and named "flights.csv".
		// In a real web application we would specify the full pathname of
		// the CSV and there could also be an HTTP request for submitting it.

		Path csv = Paths.get("flights.csv");
		
		try {
			List<String[]> fieldList = new ArrayList<String[]>();
			List<String> lines = Files.readAllLines(csv);
			for (String line: lines) {
				String[] fields = line.split(",");
				fieldList.add(fields);
			}
			return fieldList;
		} catch (IOException e) {
			log.error("failed to read CSV - {}", e.getMessage());
			return null;
		}
	}
}
