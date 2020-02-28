package com.goblimey.flightinfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.goblimey.flightinfo.controller.FlightsController;
import com.goblimey.flightinfo.domain.Flight;
import com.goblimey.flightinfo.domain.Schedule;

@SpringBootApplication
public class FlightinfoApplication {
	
	private static final Logger log = LoggerFactory.getLogger(FlightinfoApplication.class);
	
	public static void main(String[] args) {
		
		// If there is an argument, get the date and then the flights for
		// that date, and print them.
		
		if (args.length >= 1) {
			FlightsController controller = new FlightsController();
			Schedule[] schedules = controller.getSchedules();
			Flight[] flights = Schedule.getFlightsOnDate(schedules, args[0]);
			
			log.info("{} flights on {}", flights.length, args[0]);
			for (Flight flight: flights) {
				log.info("flight {}", flight);
			}
		}
		
		SpringApplication.run(FlightinfoApplication.class, args);
	}

}
