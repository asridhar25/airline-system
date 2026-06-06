import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * The <tt>AirlineGraph</tt> class represents an directed graph with
 * vertices representing airports and edges representing direct routes
 * between the airports.
 */
public class AirlineGraph {
    private int v;
    private ArrayList<HashSet<Route>> adj; // adjacency lists
    private HashMap<String, Integer> airportHT; // a hash table from airport code to vertex label

    /**
     * Create a graph with the given airport codes
     */
    public AirlineGraph(String[] airports) {
        v = airports.length;
        // populate the hash table
        airportHT = new HashMap<>();
        for (int i = 0; i < v; i++) {
            airportHT.put(airports[i], i);
        }
        // initialize the adjacency lists
        adj = new ArrayList<>();
        for (int i = 0; i < v; i++)
            adj.add(new HashSet<Route>());
    }

     /**
     * Create a graph with the given airport codes
     */
    public AirlineGraph(Set<String> airports) {
        v = airports.size();
        // populate the hash table
        airportHT = new HashMap<>();
        int i = 0;
        for (String airport: airports) {
            airportHT.put(airport, i++);
        }
        // initialize the adjacency lists
        adj = new ArrayList<>();
        for (i = 0; i < v; i++)
            adj.add(new HashSet<Route>());
    }


    /**
     * Add/Update an edge to/in the graph
     */
    public void addRoute(Route edge) {
        int from = airportHT.get(edge.source);
        if (adj.get(from).contains(edge)) {
            adj.get(from).remove(edge);
            adj.get(from).add(edge);
        } else {
            adj.get(from).add(edge);
        }
    }

    /**
     * Retrieve the Route between source to destination if one exists
     * in the graph
     * @param source the String source airport 
     * @param destination the String destination airport
     * @return Route from source to destination if route exists and
     *         null otherwise
     */
    public Route getRoute(String source, String destination) {
        Route result = null;
        for(Route r: adj(source)){
            if(r.destination.equals(destination)){
                result = r;
            }
        }
        return result;
    }


    /**
     * Return the routes from source airport as a Set.
     * To iterate over the routes, use foreach notation:
     * <tt>for (Route e : graph.adj(v))</tt>.
     */
    public Set<Route> adj(String source) {
        return adj.get(airportHT.get(source));
    }

    /**
     * 
     * @return the airport codes in the graph
     */
    public Set<String> getAirports() {

        return airportHT.keySet();
    }

    /**
     * Return the integer corresponding to the airport code
     * 
     * @param airport the String airport code
     * @return the corresponding integer to the airport code or -1 if airport not
     *         found
     */
    public int getAirportNo(String airport) {
        Integer result = airportHT.get(airport);
        if (result == null)
            return -1;
        else
            return result;
    }    
}