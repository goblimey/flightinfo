# Flight Information Web App

## Building and Running

In this version the CSV must be called "flights.csv" and it must be in the directory from which the app is run.
To use different data,
overwrite the file.

In a real app,
there would be an HTTP request to load a CSV.

All dates and times are assumed to be in the UTC time zone.

    gradle test
    
builds the app and runs the unit tests.

     gradle run
     
builds the app and runs it as a command line program.

For a quick runtime test:

     gradle run --args 2020-02-29

runs the app and supplies a date (UTC assumed).
The app lists the flights running on that day
before starting the web server.
The given date is a Saturday
and it yields 6 flights.

For some reason, the app restarts itself, so the results appear twice:

```
$ gradle run --args 2020-02-29

> Task :run
17:28:00.499 [main] INFO com.goblimey.flightinfo.FlightinfoApplication - 6 flights on 2020-02-29
17:28:00.503 [main] INFO com.goblimey.flightinfo.FlightinfoApplication - flight Flight [departureTimeUTC=10:00, destination=Antigua, destIATA=ANU, flightNumber=VS033]
17:28:00.518 [main] INFO com.goblimey.flightinfo.FlightinfoApplication - flight Flight [departureTimeUTC=10:15, destination=Las Vegas, destIATA=LAS, flightNumber=VS043]
17:28:00.518 [main] INFO com.goblimey.flightinfo.FlightinfoApplication - flight Flight [departureTimeUTC=11:05, destination=Barbados, destIATA=BGI, flightNumber=VS029]
17:28:00.518 [main] INFO com.goblimey.flightinfo.FlightinfoApplication - flight Flight [departureTimeUTC=11:20, destination=Orlando, destIATA=MCO, flightNumber=VS027]
17:28:00.518 [main] INFO com.goblimey.flightinfo.FlightinfoApplication - flight Flight [departureTimeUTC=13:00, destination=Orlando, destIATA=MCO, flightNumber=VS015]
17:28:00.518 [main] INFO com.goblimey.flightinfo.FlightinfoApplication - flight Flight [departureTimeUTC=15:35, destination=Las Vegas, destIATA=LAS, flightNumber=VS044]
17:28:00.753 [restartedMain] INFO com.goblimey.flightinfo.FlightinfoApplication - 6 flights on 2020-02-29
17:28:00.753 [restartedMain] INFO com.goblimey.flightinfo.FlightinfoApplication - flight Flight [departureTimeUTC=10:00, destination=Antigua, destIATA=ANU, flightNumber=VS033]
17:28:00.754 [restartedMain] INFO com.goblimey.flightinfo.FlightinfoApplication - flight Flight [departureTimeUTC=10:15, destination=Las Vegas, destIATA=LAS, flightNumber=VS043]
17:28:00.754 [restartedMain] INFO com.goblimey.flightinfo.FlightinfoApplication - flight Flight [departureTimeUTC=11:05, destination=Barbados, destIATA=BGI, flightNumber=VS029]
17:28:00.754 [restartedMain] INFO com.goblimey.flightinfo.FlightinfoApplication - flight Flight [departureTimeUTC=11:20, destination=Orlando, destIATA=MCO, flightNumber=VS027]
17:28:00.754 [restartedMain] INFO com.goblimey.flightinfo.FlightinfoApplication - flight Flight [departureTimeUTC=13:00, destination=Orlando, destIATA=MCO, flightNumber=VS015]
17:28:00.754 [restartedMain] INFO com.goblimey.flightinfo.FlightinfoApplication - flight Flight [departureTimeUTC=15:35, destination=Las Vegas, destIATA=LAS, flightNumber=VS044]
```


To run the web server:

    ./gradlew bootRun
    
and issue a URL like this:

    http://localhost:8080/flights/2020-02-29
    
The result looks something like this:

```
List of Flights
10:00	Antigua	  ANU VS033
10:15	Las Vegas LAS	VS043
11:05	Barbados  BGI	VS029
11:20	Orlando   MCO	VS027
13:00	Orlando   MCO	VS015
15:35	Las Vegas LAS	VS044
```
    

## General Approach

The solution assumes that all dates and times are in the UTC time zone.
If the given date and the times in the CSV are in the local time zones,
the problem becomes more complicated.

The supplied date could be any old junk,
which would cause an error during parsing.
This should be handled much better
and the code properly tested.

I structure my source code so that as much of it as possible is available for simple unit tests.
For example,
the FlightsController constructor reads the CSV file and breaks it into fields,
but Schedule.createSchedulesFromFields is responsible for taking
those fields and creating
the list of schedules.
This allows that method to be tested thoroughly
without faffing about with files.

I avoid using streams and lambdas for the sake of it.
For example,
Schedule.createSchedulesFromFields
uses simple for loops to scan the fields.
On the other hand,
Schedule.getFlightsOnDate
has to scan and filter the schedules.
Streams and lamdas seemed appropriate for that.

I created two classes Schedule and Flight.
An array of Schedule objects represent the data in the CSV
and an array of Flights represent the results.
Flight contains some of the same fields as Schedule,
so strictly I could use Schedule for both purposes.
However, it seemed cleaner to have separate classes.

I haven't done any front-end stuff in java for a while.
The last time I did any I used JSPs.
I read that Spring Boot and JSPs don't work so well together,
so this time I used Thymeleaf,
which I've not used before.

## Unit Testing

I did some unit testing,
but there are gaps.
There could be a lot more testing of error cases,
for example illegal data in the CSV,
the user entering junk data,
and so on.
There needs to be a lot more