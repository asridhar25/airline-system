import java.util.Set;
import java.util.ArrayList;

public interface AirlineInterface {


  /**
   * returns the set of airport codes in an Airline Graph
   * @param ag the AirlineGraph graph representing the airline system
   * @return a (possibly empty) Set<String> of airport codes
   */
  public Set<String> retrieveAirports(AirlineGraph ag);

  /**
   * returns the set of direct routes out of a given airport
   * @param ag the AirlineGraph graph representing the airline system
   * @param airport the String airport code
   * @return a (possibly empty) Set<Route> of Route objects representing the
   * direct routes out of airport
   * @throws AirportNotFoundException if the airport is not found in the Airline
   * system
   */
  public Set<Route> retrieveDirectRoutesFrom(AirlineGraph ag, String airport)
    throws AirportNotFoundException;

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


 /**
   * finds fewest-stops path(s) between two airports through a transit airport
   * @param ag the AirlineGraph graph representing the airline system
   * @param source the String source airport code
   * @param destination the String destination airport code
   * @param transit the String transit airport code
   * @return a (possibly empty) Set<ArrayList<String>> of fewest-stops paths.
   * Each path is an ArrayList<String> of airport codes that includes the source, transit,
   * and destination airport codes.
   * @throws AirportNotFoundException if any of the three airports are not found in the
   * Airline system
   */
  public Set<ArrayList<String>> fewestStopsItinerary(AirlineGraph ag, String source,
  String destination, String transit) throws AirportNotFoundException;

  /**
   * finds the fewest connected components of a graph
   * @param ag the AirlineGraph graph representing the airline system
   * @return a (possibly empty) Set<Set<String>> of connected components.
   * Each connected component is a Set<String> of airport codes in the component.
   */
  public Set<Set<String>> connectedComponents(AirlineGraph ag);


  /**
   * finds all itineraries within a given price and within a given number of stops from source to destination airports
   * @param ag the AirlineGraph graph representing the airline system
   * @param source the String source airport code
   * @param destination the String destination airport code
   * @param  budget the double maximum price in dollars
   * @param  stops the int (>=0) maximum number of stops
   * @return a (possibly empty) Set<ArrayList<Route>> of paths with a total cost
   * less than or equal to the budget and at most the given number of stops. Each path is an ArrayList<Route> of 
   * at most 2+stops entries starting with source and ending with destination.
   */
  public Set<ArrayList<Route>> allTrips(AirlineGraph ag, String source, String destination, double budget, int stops);

   /**
   * finds all non-trivial itineraries within a given price and within a given number of stops from an airport back to itself
   * @param ag the AirlineGraph graph representing the airline system
   * @param source the String source airport code
   * @param  budget the double maximum price in dollars
   * @param  stops the int (> 0) maximum number of stops
   * @return a (possibly empty) Set<ArrayList<Route>> of paths with a total cost
   * less than or equal to the budget and at most the given number of stops. Each path is an ArrayList<Route> of 
   * at least 2 and at most 2+stops entries starting and ending with source.
   */
  public Set<ArrayList<Route>> allRoundTrips(AirlineGraph ag, String source, double budget, int stops);
}
