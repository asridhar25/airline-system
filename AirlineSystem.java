import java.util.*;
import java.io.*;

public class AirlineSystem<DisjointSet> implements AirlineInterface {
    private class Digraph {
        private int v;
        private int e;
        private LinkedList<Route>[] adj;
        private boolean[] marked;
        private int[] edgeTo;
        private double[] priceTo;

        public Digraph(int v) {
            if(v < 0) {
                throw new RuntimeException("Number of vertices must be nonnegative");
            }
            this.v = v;
            this.e = 0;
            @SuppressWarnings("unchecked")
            LinkedList<Route>[] temp = (LinkedList<Route>[]) new LinkedList[v];
            adj = temp;
            for(int i = 0; i < v; i++) {
                adj[i] = new LinkedList<Route>();
            }
        }

        public void addEdge(Route edge) {
            int s = 0;
            for(int i = 0; i < Graph.v; i++) {
                if(cityNames.get(i).equals(edge.source)) {
                    s = i;
                    break;
                }
            }
            adj[s].add(edge);
            e++;
        }

        public Iterable<Route> adj(int v) {
            if(adj[v] == null) {
                return null;
            }
            return adj[v];
        }

        public void dijkstras(int source, int destination) {
            marked = new boolean[this.v];
            priceTo = new double[this.v];
            edgeTo = new int[this.v];
            for(int i = 0; i < v; i++) {
                priceTo[i] = INFINITY;
                marked[i] = false;
            }
            priceTo[source] = 0;
            marked[source] = true;
            component[source] = cpt;
            int nMarked = 1;
            int current = source;
            while (nMarked < this.v) {
                for(Route e : adj(current)) {
                    if(priceTo[current] + e.price < priceTo[cityNames.indexOf(e.destination)]) {
                        edgeTo[cityNames.indexOf(e.destination)] = current;
                        priceTo[cityNames.indexOf(e.destination)] = priceTo[current] + e.price;
                    }
                }
            double min = INFINITY;
            current = -1;
            for(int i = 0; i < priceTo.length; i++) {
                if(marked[i]) {
                    continue;
                }
                if(priceTo[i] < min) {
                    min = priceTo[i];
                    current = i;
                }
            }
            if(current >= 0) {
                marked[current] = true;
                if(component[current] == -1) {
                    component[current] = cpt;
                    nMarked++;
                }
            } else {
                break;
            }
            }
        }
    }   


    private ArrayList<String> cityNames = new ArrayList<String>();
    private Digraph Graph = null;
    private static final int INFINITY = Integer.MAX_VALUE;
    private boolean[] allVisited = null;
    public int[] component = null;
    public int IdeS = -1;
    public int ideD = -1;
    public int cpt = 1;





    public boolean loadRoutes(String fileName) {
        try {
            Scanner sc = new Scanner(new FileInputStream(fileName));
            int vertex = Integer.parseInt(sc.nextLine());
            Graph = new Digraph(vertex);
            for(int i = 0; i < vertex; i++) {
                cityNames.add(sc.nextLine());
            }
            while(sc.hasNext()) {
                String source = cityNames.get(sc.nextInt() - 1);
                String dest = cityNames.get(sc.nextInt() - 1);
                Double price = sc.nextDouble();
                Graph.addEdge(new Route(source, dest, price));
                Graph.addEdge(new Route(dest, source, price));
                if(sc.hasNextLine()) {
                    sc.nextLine();
                }
            }
            sc.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean saveRoutes(String filename) {
        try {
            PrintWriter writer = new PrintWriter(filename, "UTF-8");
            writer.println(cityNames.size());
            for(String city : cityNames) {
                writer.println(city);
            }
            for(int i = 0; i < Graph.v; i++) {
                for(Route route : Graph.adj(i)) {
                    writer.println(cityNames.indexOf(route.source) + 1 + " " + cityNames.indexOf(route.destination) + 1 + " " + route.price);
                }
            }
            writer.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public Set<String> retrieveCityNames() {
        Set<String> result = new HashSet<String>();
        if(cityNames != null) {
            for(String s : cityNames) {
                result.add(s);
            }
            return result;
        }
        return result;
    }

    private boolean checkCity(String city) {
        IdeS = -1;
        boolean there = false;
        int s = 0;
        while(s < Graph.v && !there) {
            if(cityNames.get(s).equals(city)) {
                there = true;
                IdeS = s;
            }
            s++;
        }
        return there;
    }

    private String checkCity(String city, String city2) {
        IdeS = -1;
        ideD = -1;
        String there = "true";
        int s = 0;
        while (s < Graph.v && IdeS == -1) {
            if(cityNames.get(s).equals(city)) {
                IdeS = s;
            }
            s++;
        }
        int d = 0;
        while (d < Graph.v && ideD == -1) {
            if(cityNames.get(d).equals(city2)) {
                ideD = d;
            }
            d++;
        }
        if(IdeS == -1) {
            there = city;
        } else if (ideD == -1) {
            there = city2;
        }
        return there;
    }

    public Set<Route> retrieveDirectRoutesFrom(String city) throws IndexOutOfBoundsException {
        Set<Route> result = new HashSet<>();
        // Check if the city is in the AL system
        if(!checkCity(city)) {
            throw new IndexOutOfBoundsException(city + " is not a city in the Airline System!");
        }
        // Find roots that start from inputted city
        for(Route e : Graph.adj(IdeS)) {
            result.add(e);
        }
        return result;
    }

    public Set<ArrayList<String>> fewestStopsItinerary(String source, String destination) throws IndexOutOfBoundsException {
      Set<ArrayList<String>> fewestStopsPaths = new HashSet<>();
      // Check if source and destination cities are in the airline system
      String result = checkCity(source, destination);
      if (!result.equals("true")) {
        throw new IndexOutOfBoundsException(result + " is not a city in the airline system");
      }
      // Initialize variables for BFS
      int s = cityNames.indexOf(source);
      int d = cityNames.indexOf(destination);
      boolean[] visited = new boolean[Graph.v];
      visited[s] = true;
      Queue<Integer> queue = new LinkedList<>();
      queue.offer(s);
      Map<Integer, Integer> prev = new HashMap<>();
      // Perform BFS
      while (!queue.isEmpty()) {
        int curr = queue.poll();
        for (Route route : Graph.adj(curr)) {
          int v = cityNames.indexOf(route.destination);
          if (!visited[v]) {
            visited[v] = true;
            prev.put(v, curr);
            queue.offer(v);
          }
        }
      }
      // Construct list of cities for each fewest-stops path
      if (visited[d]) {
        ArrayList<String> path = new ArrayList<>();
        int curr = d;
        while (curr != s) {
          path.add(0, cityNames.get(curr));
          curr = prev.get(curr);
        }
        path.add(0, source);
        fewestStopsPaths.add(path);
      }
      return fewestStopsPaths;
    }
  
  
    public Set<ArrayList<Route>> shortestDistanceItinerary(String source, String destination) throws IndexOutOfBoundsException {
      // Check if source and destination cities are in the airline system
      int sourceIndex = cityNames.indexOf(source);
      if (sourceIndex == -1) {
        throw new IndexOutOfBoundsException("Source city not found in airline system: " + source);
      }
      int destinationIndex = cityNames.indexOf(destination);
      if (destinationIndex == -1) {
        throw new IndexOutOfBoundsException("Destination city not found in airline system: " + destination);
      }
  
      // Run Dijkstra's algorithm on the graph to find the shortest path from source to destination
      Graph.dijkstras(sourceIndex, destinationIndex);
  
      // Create a set to store the shortest-distance paths
      Set<ArrayList<Route>> paths = new HashSet<>();
  
      // If the destination is not reachable from the source, return an empty set
      if (Graph.priceTo[destinationIndex] == INFINITY) {
        return paths;
      }
  
      // Create a stack to store the path from the source to the destination
      Stack<Route> stack = new Stack<>();
      int current = destinationIndex;
      while (current != sourceIndex) {
        stack.push(new Route(cityNames.get(Graph.edgeTo[x]), cityNames.get(x), 0));
        current = Graph.edgeTo[current];
      }
  
      // Create an ArrayList of Route objects from the stack and add it to the set of paths
      ArrayList<Route> path = new ArrayList<>();
      while (!stack.isEmpty()) {
        path.add(stack.pop());
      }
      paths.add(path);
  
      // Return the set of shortest-distance paths
      return paths;
    }
    /**
    * finds cheapest path(s) between two cities
    * @param source the String source city name
    * @param destination the String destination city name
    * @return a (possibly empty) Set<ArrayList<Route>> of cheapest
    * paths. Each path is an ArrayList<Route> of Route objects that includes a
    * Route out of the source and a Route into the destination.
    * @throws CityNotFoundException if any of the two cities are not found in the
    * Airline system
    */
  
  
  
  
    public Set<ArrayList<Route>> cheapestItinerary(String source, String destination) throws IndexOutOfBoundsException {
      // Check if source and destination cities are in the airline system
      int sourceIndex = cityNames.indexOf(source);
      if (sourceIndex == -1) {
        throw new IndexOutOfBoundsException("Source city not found in airline system: " + source);
      }
      int destinationIndex = cityNames.indexOf(destination);
      if (destinationIndex == -1) {
        throw new IndexOutOfBoundsException("Destination city not found in airline system: " + destination);
      }
  
      // Run Dijkstra's algorithm on the graph to find the shortest path based on price from source to destination
      Graph.dijkstras(sourceIndex, destinationIndex);
  
      // Create a set to store the cheapest paths
      Set<ArrayList<Route>> paths = new HashSet<>();
  
      // If the destination is not reachable from the source, return an empty set
      if (Graph.priceTo[destinationIndex] == INFINITY) {
        return paths;
      }
  
      // Create a stack to store the path from the source to the destination
      Stack<Route> stack = new Stack<>();
      int current = destinationIndex;
      while (current != sourceIndex) {
        stack.push(new Route(cityNames.get(Graph.edgeTo[x]), cityNames.get(x), 0));
        current = Graph.edgeTo[current];
      }
  
      // Create an ArrayList of Route objects from the stack and add it to the set of paths
      ArrayList<Route> path = new ArrayList<>();
      while (!stack.isEmpty()) {
        path.add(stack.pop());
      }
      paths.add(path);
  
      // Return the set of cheapest paths
      return paths;
    }

    public Set<ArrayList<Route>> cheapestItinerary(String source, String transit, String destination) throws IndexOutOfBoundsException {
      Set<ArrayList<Route>> cheapestPaths = new HashSet<>();
  
      int sourceIndex = cityNames.indexOf(source);
      int transitIndex = cityNames.indexOf(transit);
      int destinationIndex = cityNames.indexOf(destination);
  
      if (sourceIndex == -1 || transitIndex == -1 || destinationIndex == -1) {
        throw new IndexOutOfBoundsException("Impossible");
      }
  
      Graph.dijkstras(sourceIndex, transitIndex);
      ArrayList<Route> path1 = getPath(transitIndex);
      double cost1 = Graph.priceTo[transitIndex];
  
      Graph.dijkstras(transitIndex, destinationIndex);
      ArrayList<Route> path2 = getPath(destinationIndex);
      double cost2 = Graph.priceTo[destinationIndex];
  
      ArrayList<Route> combinedPath = new ArrayList<>(path1);
      combinedPath.addAll(path2);
      double combinedCost = cost1 + cost2;
  
      cheapestPaths.add(combinedPath);
  
      return cheapestPaths;
    }
  
    private ArrayList<Route> getPath(int destination) {
      ArrayList<Route> path = new ArrayList<>();
      for (int x = destination; x != Graph.edgeTo[x]; x = Graph.edgeTo[x]) {
        path.add(new Route(cityNames.get(Graph.edgeTo[x]), cityNames.get(x), 0));
      }
      Collections.reverse(path);
      return path;
    }
  
  
    /**
     * finds one Minimum Spanning Tree (MST) for each connected component of
     * the graph
     *
     * @return a (possibly empty) Set<Set<Route>> of MSTs. Each MST is a Set<Route>
     * of Route objects representing the MST edges.
     */
    public Set<Set<Route>> getMSTs() {
      return null;
    }
  
  
    /**
     * finds all itineraries starting out of a source city and within a given
     * price
     * @param city the String city name
     * @param budget the double budget amount in dollars
     * @return a (possibly empty) Set<ArrayList<Route>> of paths with a total cost
     * less than or equal to the budget. Each path is an ArrayList<Route> of Route
     * objects starting with a Route object out of the source city.
     */
    public Set<ArrayList<Route>> tripsWithin(String city, double budget) throws IndexOutOfBoundsException {
      // check if the given city is valid
      if (!cityNames.contains(city)) {
        throw new IndexOutOfBoundsException("null");
      }
  
      // find the index of the given city
      int source = cityNames.indexOf(city);
  
      // create a Set to store the paths
      Set<ArrayList<Route>> paths = new HashSet<>();
  
      // create an empty path
      ArrayList<Route> path = new ArrayList<>();
  
      // call the helper function with the source city, the budget, and the empty path
      tripsHelper(source, budget, path, paths);
  
      return paths;
    }
    private void tripsHelper(int current, double budget, ArrayList<Route> path, Set<ArrayList<Route>> paths) {
      // if the budget is less than or equal to the given budget, add the path to the Set
      if (budget <= 0) {
        paths.add(path);
        return;
      }
  
      // iterate through the adjacency list of the current city
      for (Route r : Graph.adj(current)) {
        // create a copy of the path so far
        ArrayList<Route> pathCopy = new ArrayList<>(path);
        // add the current Route to the copy
        pathCopy.add(r);
        // call the helper function recursively with the neighbor city, the updated budget, and the updated path
        tripsHelper(cityNames.indexOf(r.destination), budget - r.price, pathCopy, paths);
      }
    }
  
  
    /**
     * finds all itineraries within a given price regardless of the
     * starting city
     * @param  budget the double budget amount in dollars
     * @return a (possibly empty) Set<ArrayList<Route>> of paths with a total cost
     * less than or equal to the budget. Each path is an ArrayList<Route> of Route
     * objects.
     */
  
  
    public Set<ArrayList<Route>> tripsWithin(double budget) {
      // create a Set to store the paths
      Set<ArrayList<Route>> paths = new HashSet<>();
  
      // iterate through all the cities
      for (int i = 0; i < cityNames.size(); i++) {
        // create an empty path
        ArrayList<Route> path = new ArrayList<>();
        // call the helper function with the current city, the budget, and the empty path
        tripsHelper(i, budget, path, paths);
      }
  
      return paths;
}

    @Override
    public Set<String> retrieveAirports(AirlineGraph ag) {
        Set <String> allAirports = ag.getAirports();
        return allAirports;
    }

    @Override
    public Set<Route> retrieveDirectRoutesFrom(AirlineGraph ag, String airport) throws AirportNotFoundException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'retrieveDirectRoutesFrom'");
    }

    @Override
    public Set<ArrayList<String>> fewestStopsItinerary(AirlineGraph ag, String source, String destination)
            throws AirportNotFoundException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fewestStopsItinerary'");
    }

    @Override
    public Set<ArrayList<String>> fewestStopsItinerary(AirlineGraph ag, String source, String destination,
            String transit) throws AirportNotFoundException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fewestStopsItinerary'");
    }

    @Override
    public Set<Set<String>> connectedComponents(AirlineGraph ag) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'connectedComponents'");
    }

    @Override
    public Set<ArrayList<Route>> allTrips(AirlineGraph ag, String source, String destination, double budget,
            int stops) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'allTrips'");
    }

    @Override
    public Set<ArrayList<Route>> allRoundTrips(AirlineGraph ag, String source, double budget, int stops) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'allRoundTrips'");
    }
}