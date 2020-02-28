package com.goblimey.flightinfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.goblimey.flightinfo.domain.Flight;
import com.goblimey.flightinfo.domain.Schedule;

@SpringBootApplication
public class FlightinfoApplication {
	
	private static final Logger log = LoggerFactory.getLogger(FlightinfoApplication.class);
	
	public static void main(String[] args) {
		
		// Read the CSV file and create the flight schedule.  The CSV must be
		// in the current directory and named "flights.csv".  In a real
		// application we would specify the full pathname of the CSV and there
		// could also be an HTTP request for submitting it.

		Path csv = Paths.get("flights.csv");
		Schedule[] schedules = null;
		
		try {
			List<String[]> fieldList = new ArrayList<String[]>();
			List<String> lines = Files.readAllLines(csv);
			for (String line: lines) {
				String[] fields = line.split(",");
				fieldList.add(fields);
			}
			schedules = Schedule.createSchedulesFromFields(fieldList);
		} catch (IOException e) {
			log.error(e.getMessage());
			return;
		}
		
		// If there is an argument, get the date and then the flights for
		// that date and print them.
		
		if (args.length >= 1) {
			Flight[] flights = Schedule.getFlightsOnDate(schedules, args[0]);
			
			log.info("{} flights", flights.length);
			for (Flight flight: flights) {
				log.info("flight {}", flight);
			}
		}
		
		SpringApplication.run(FlightinfoApplication.class, args);
	}

}
