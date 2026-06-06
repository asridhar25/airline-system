# CS 1501 – Algorithm Implementation – Assignment #4

_(Assignment originally adapted from Dr. John Ramirez’s CS 1501 class.)_

Due: Monday, April 8th @ 11:59pm on Gradescope

Late submission deadline: Wednesday, April 10th @11:59pm with a 10% penalty per late day

## Overview

Purpose: The purpose of this assignment is to practice implementing some Graph
algorithms and to see how they can be used practically.

**Feel free to use as much code as you need from Lab 5 and the Assignment 4 Support recitation.**

## Details

You are to implement the backend of a simple information system for a fictional airline. The airline airport and route data are stored in an `AirlineGraph` object. Please familiarize yourself with the public methods in `AirlineGraph.java`.

Your program should be able to handle the following queries as specified in `AirlineInterface.java`:

1. Return a set of all airports served by the Airline. 

  ```java
  /**
   * returns the set of airport codes in an Airline Graph
   * @param ag the AirlineGraph graph representing the airline system
   * @return a (possibly empty) Set<String> of airport codes
   */
  public Set<String> retrieveAirports(AirlineGraph ag);
  ```

2. Return a set of all non-stop `Route`s out of a given airport. Please check `Route.java` for the specification of the `Route` objects.

```java
  /**
   * returns the set of direct routes out of a given airport
   * @param airport the String airport name
   * @return a (possibly empty) Set<Route> of Route objects representing the direct routes out
   * of airport
   * @throws airportNotFoundException if the airport is not found in the Airline
   * system
   */
  public Set<Route> retrieveDirectRoutesFrom(String airport) throws airportNotFoundException;
```

3.	Allow for each of the "shortest path" searches below. If multiple paths "tie" for the shortest, you should return any of them.

    a.	Shortest path based on number of hops (individual segments) from the source to the destination. This option could be useful to passengers who prefer fewer segments for one reason or another (e.g., traveling with small children).
  	
  ```java
  /**
   * finds fewest-stops path(s) between two airports
   * @param ag the AirlineGraph graph representing the airline system
   * @param source the String source airport code
   * @param destination the String destination airport code
   * @return a (possibly empty) Set<ArrayList<String>> of fewest-stops paths.
   * Each path is an ArrayList<String> of airport codes that includes the source
   * and destination airport codes.
   * @throws AirportNotFoundException if any of the two airports are not found in the
   * Airline system
   */
  public Set<ArrayList<String>> fewestStopsItinerary(AirlineGraph ag, String source,
                                                      String destination) throws AirportNotFoundException;
```

  b. Shortest path based on number of hops (individual segments) from the source to the destination through a third (transit) airport. In other words, "What is the shortest path from A to B, given that I want to stop at C for a while?"
  
```java
      /**
       * finds fewest-stops path(s) between two airports through a transit airport
      * @param ag the AirlineGraph graph representing the airline system
      * @param source the String source airport code
      * @param destination the String destination airport code
      * @param destination the String transit airport code
      * @return a (possibly empty) Set<ArrayList<String>> of fewest-stops paths.
      * Each path is an ArrayList<String> of airport codes that includes the source, transit,
      * and destination airport codes.
      * @throws AirportNotFoundException if any of the three airports are not found in the
      * Airline system
      */
      public Set<ArrayList<String>> fewestStopsItinerary(AirlineGraph ag, String source,
                                                        String destination, String transit) throws AirportNotFoundException;
```

4. Find the smallest set of connected components of the Graph.

```java
  /**
   * finds the fewest connected components of a graph
   * @param ag the AirlineGraph graph representing the airline system
   * @return a (possibly empty) Set<Set<String>> of connected components.
   * Each connected component is a Set<String> of airport codes in the component.
   */
  public Set<Set<String>> connectedComponents(AirlineGraph ag);
```

5. Given a dollar amount and a maximum number of stops entered by the user, return all trips between two different airports with, at most, the given maximum number of stops and whose cost is less than or equal to the given budget. Each trip should not repeat any airports – i.e., it cannot contain a cycle. Be careful to implement this option as efficiently as possible since it can have an exponential run-time (especially for long paths). Consider a backtracking-with-pruning approach.

  ```java
   /**
   * finds all itineraries within a given price and within a given number of stops from source to destination airports
   * @param ag the AirlineGraph graph representing the airline system
   * @param source the String source airport code
   * @param destination the String destination airport code
   * @param  budget the double maximum price in dollars
   * @param  stops the int maximum number of stops
   * @return a (possibly empty) Set<ArrayList<Route>> of paths with a total cost
   * less than or equal to the budget and, at most, the given number of stops. Each path is an ArrayList<Route> of 
   * at most 2+stops entries starting with source and ending with destination.
   */
  public Set<ArrayList<Route>> allTrips(AirlineGraph ag, String source, String destination, double budget, int stops);
  ```
6. Given a dollar amount and a maximum number of stops entered by the user, return all trips from an airport and back to the same airport with, at most, the given maximum number of stops whose cost is less than or equal to the given budget. Each trip should not repeat any airports except the start airport – i.e., each trip is a simple cycle. Be careful to implement this option as efficiently as possible since it can have an exponential run-time (especially for long paths). Consider a backtracking-with-pruning approach.

```java
/**
   * finds all non-trivial itineraries within a given price and within a given number of stops from an airport back to itself
   * @param ag the AirlineGraph graph representing the airline system
   * @param source the String source airport code
   * @param  budget the double maximum price in dollars
   * @param  stops the int (> 0) maximum number of stops
   * @return a (possibly empty) Set<ArrayList<Route>> of paths with a total cost
   * less than or equal to the budget and, at most, the given number of stops. Each path is an ArrayList<Route> of 
   * at least 2 and at most 2+stops entries starting and ending with source.
   */
  public Set<ArrayList<Route>> allRoundTrips(AirlineGraph ag, String source, double budget, int stops);
  ```

. You must encapsulate your airline system's functionality in a single, cohesive class named `AirlineSystem.java`, which implements  `AirlineInterface`.

. You must use the algorithms and implementations discussed in class for your queries. For example, you must use **Breadth-First Search** for the shortest-hops methods.

. The test program `AirlineTest.java` has a menu-driven loop that asks the user for many choices. You may use this program to test your code.

Start by testing your program on small Graphs. Check the Graph file formatting as specified in `AirlineTest.java` and exemplified in `a4data1.txt`, `a4data2.txt`, `a4data3.xt`, and `a4data4.txt` and create graph files for smaller graphs. Draw the graph and manually find the shortest paths, connected components, etc. This will help you debug your code.

## Extra Credit (10 points)

For each of the "shortest path" searches listed above, if multiple paths "tie" for the shortest, you should return **all** of them.

## Submission Requirements

You must submit to Gradescope the following file:
1.	`AirlineSystem.java`

The autograder should compile and run your programs from the command line WITHOUT any additional files or changes, so be sure to test it thoroughly before submitting it. If the autograder cannot compile or run your submitted code, it will be graded as if the program does not work.

Note: If you use an IDE such as NetBeans, Eclipse, or IntelliJ to develop your programs, make sure they will compile and run on the command line before submitting – this may require some modifications to your program (such as removing some package information).

## Rubrics

__*Please note that if an autograder is available, its score will be used as guidance for the TA, not as an official final score*__.

Please also note that the autograder rubrics are the definitive rubrics for the assignment. **There is no manually assigned partial credit for this assignment.**
