# Flight Information Web App

## Building and Running

In this version the CSV must be called "flights.csv" and it must be in the directory from which the app is run.
To use different data,
overwrite the file.

In a real app,
there would be an HTTP request to load a CSV.

    gradle test
    
builds the app and runs the unit tests.

     gradle run
     
builds the app and runs it as a web server.

For a quick runtime test:

     gradle run --args 2020/02/29

runs the app and supplies a date.
The app lists the flights running on that day
before starting the web server.
The given date is a Saturday
and it yields 6 flights.

For some reason, the app restarts itself, so the results appear twice:

```
$ gradle run --args 2020/02/29

> Task :run
17:28:00.499 [main] INFO com.goblimey.flightinfo.FlightinfoApplication - 6 flights
17:28:00.503 [main] INFO com.goblimey.flightinfo.FlightinfoApplication - flight Flight [departureTimeUTC=10:00, destination=Antigua, destIATA=ANU, flightNumber=VS033]
17:28:00.518 [main] INFO com.goblimey.flightinfo.FlightinfoApplication - flight Flight [departureTimeUTC=10:15, destination=Las Vegas, destIATA=LAS, flightNumber=VS043]
17:28:00.518 [main] INFO com.goblimey.flightinfo.FlightinfoApplication - flight Flight [departureTimeUTC=11:05, destination=Barbados, destIATA=BGI, flightNumber=VS029]
17:28:00.518 [main] INFO com.goblimey.flightinfo.FlightinfoApplication - flight Flight [departureTimeUTC=11:20, destination=Orlando, destIATA=MCO, flightNumber=VS027]
17:28:00.518 [main] INFO com.goblimey.flightinfo.FlightinfoApplication - flight Flight [departureTimeUTC=13:00, destination=Orlando, destIATA=MCO, flightNumber=VS015]
17:28:00.518 [main] INFO com.goblimey.flightinfo.FlightinfoApplication - flight Flight [departureTimeUTC=15:35, destination=Las Vegas, destIATA=LAS, flightNumber=VS044]
17:28:00.753 [restartedMain] INFO com.goblimey.flightinfo.FlightinfoApplication - 6 flights
17:28:00.753 [restartedMain] INFO com.goblimey.flightinfo.FlightinfoApplication - flight Flight [departureTimeUTC=10:00, destination=Antigua, destIATA=ANU, flightNumber=VS033]
17:28:00.754 [restartedMain] INFO com.goblimey.flightinfo.FlightinfoApplication - flight Flight [departureTimeUTC=10:15, destination=Las Vegas, destIATA=LAS, flightNumber=VS043]
17:28:00.754 [restartedMain] INFO com.goblimey.flightinfo.FlightinfoApplication - flight Flight [departureTimeUTC=11:05, destination=Barbados, destIATA=BGI, flightNumber=VS029]
17:28:00.754 [restartedMain] INFO com.goblimey.flightinfo.FlightinfoApplication - flight Flight [departureTimeUTC=11:20, destination=Orlando, destIATA=MCO, flightNumber=VS027]
17:28:00.754 [restartedMain] INFO com.goblimey.flightinfo.FlightinfoApplication - flight Flight [departureTimeUTC=13:00, destination=Orlando, destIATA=MCO, flightNumber=VS015]
17:28:00.754 [restartedMain] INFO com.goblimey.flightinfo.FlightinfoApplication - flight Flight [departureTimeUTC=15:35, destination=Las Vegas, destIATA=LAS, flightNumber=VS044]
```
