import java.util.ArrayList;
import java.util.Set;
import java.util.Scanner;
import java.io.IOException;
import java.io.FileInputStream;

final public class AirlineTest {

  private AirlineInterface airline;
  private AirlineGraph g;
  private Scanner scan;
  private MenuProgram program;

  /**
   * Test client.
   */
  public static void main(String[] args) throws IOException {
    new AirlineTest();
  }

  private class LoadMenuItem implements CallableMenuItem {

    @Override
    public String getDisplayString() {
      return "Load graph from a file";
    }

    @Override
    public void handle() {
      readGraph();
    }

  }

  private class PrintMenuItem implements CallableMenuItem {

    @Override
    public String getDisplayString() {
      return "Display all routes";
    }

    @Override
    public void handle() {
      try {
        printGraph();
      } catch (AirportNotFoundException e) {
        e.printStackTrace();
      }
    }
  }

  private class ExitMenuItem implements CallableMenuItem {

    @Override
    public String getDisplayString() {
      return "Exit";
    }

    @Override
    public void handle() {
      System.out.println("Good Bye!");
      System.exit(0);
    }

  }

  private class FewestHopsMenuItem implements CallableMenuItem {

    @Override
    public String getDisplayString() {
      return "Fewest hops";
    }

    @Override
    public void handle() {
      try {
        shortestHops();
      } catch (AirportNotFoundException e) {
        e.printStackTrace();
      }
    }

  }

  private class ConnectedComponentsMenuItem implements CallableMenuItem {

    @Override
    public String getDisplayString() {
      return "Connected components";
    }

    @Override
    public void handle() {
      components();
    }

  }

  private class FewestHopsTransitMenuItem implements CallableMenuItem {

    @Override
    public String getDisplayString() {
      return "Fewest hops with a transit";
    }

    @Override
    public void handle() {
      try {
        shortestHopsWithTransit();
      } catch (AirportNotFoundException e) {
        e.printStackTrace();
      }
    }

  }

  private class TripsUnderairportMenuItem implements CallableMenuItem {

    @Override
    public String getDisplayString() {
      return "Find all paths between two airports, within a given budget, and with a maximum number of stops";
    }

    @Override
    public void handle() {
      tripsUnderFrom();
    }

  }

  private class RoundTripsUnderairportMenuItem implements CallableMenuItem {

    @Override
    public String getDisplayString() {
      return "Find all round-trip paths from an airport, within a given budget, and with a maximum number of stops";
    }

    @Override
    public void handle() {
      roundTripsUnderFrom();
    }

  }


  public AirlineTest() {
    ArrayList<CallableMenuItem> menuItems = new ArrayList<>();
    airline = new AirlineSystem();
    scan = new Scanner(System.in);

    menuItems.add(new LoadMenuItem());
    menuItems.add(new PrintMenuItem());
    menuItems.add(new FewestHopsMenuItem());
    menuItems.add(new FewestHopsTransitMenuItem());
    menuItems.add(new ConnectedComponentsMenuItem());
    menuItems.add(new TripsUnderairportMenuItem());
    menuItems.add(new RoundTripsUnderairportMenuItem());
    menuItems.add(new ExitMenuItem());
    program = new MenuProgram(menuItems);
    program.run();
  }

  private void readGraph() {
    System.out.println("Please enter file name:");
    String filename = scan.nextLine();
    g = loadRoutes(filename);
    System.out.println("Data imported successfully.");
  }

  /**
   * Load graph from a file
   * File format:
   * <number of airports>
   * <airport 1> <price to airport 1> <price to airport 2> ...
   * <airport 2> <price to airport 1> <price to airport 2> ...
   * ..
   * <airport N> <price to airport 1> <price to airport 2> ...
   * ...
   * Negative price means no route
   * 
   * @param filename the full path to the file
   * @return true if loading from file was successful and false otherwise (e.g.,
   *         when file is not found or file formatting not as expected.)
   */
  public AirlineGraph loadRoutes(String filename) {
    AirlineGraph result = null;
    Scanner fileScan = null;
    try {
      fileScan = new Scanner(new FileInputStream(filename));
      int v = Integer.parseInt(fileScan.nextLine());
      String[] airportcodes = new String[v];
      for (int i = 0; i < v; i++) {
        String[] line = fileScan.nextLine().split("\\s+");
        airportcodes[i] = line[0];
      }
      fileScan.close();
      fileScan = new Scanner(new FileInputStream(filename));
      fileScan.nextLine();
      result = new AirlineGraph(airportcodes);
      int from = 0;
      while (fileScan.hasNext()) {
        fileScan.next();
        for (int to = 0; to < v; to++) {
          double price = fileScan.nextDouble();
          if (price > 0.0) {
            Route r = new Route(airportcodes[from], airportcodes[to], price);
            result.addRoute(r);
          }
        }
        from++;
        if (fileScan.hasNext())
          fileScan.nextLine();
      }
    } catch (Exception e) {
      System.out.println("Error loading airports from file " + filename);
      result = null;
    }
    return result;
  }

  private void printGraph() throws AirportNotFoundException {
    if (g == null) {
      System.out.println("Please load a graph first.");
    } else {

      for (String airport : airline.retrieveAirports(g)) {
        System.out.print(airport + ": ");
        for (Route r : airline.retrieveDirectRoutesFrom(g, airport)) {
          System.out.print(r.destination + "($"
              + r.price + ") ");
        }
        System.out.println();
      }
    }
  }

  private void components() {
    if (g == null) {
      System.out.println("Please load a graph first.");
    } else {
      Set<Set<String>> components = airline.connectedComponents(g);
      System.out.println("Found " + components.size() + " component(s):");
      for (Set<String> component : components) {
        for (String r : component) {
          System.out.print(r + " ");
        }
        System.out.println();
      }
    }
  }

  private void shortestHops() throws AirportNotFoundException {
    if (g == null) {
      System.out.println("Please load a graph first.");
    } else {

      for (String airport : airline.retrieveAirports(g)) {
        System.out.println(airport);
      }
      System.out.print("Please enter source airport: ");
      String source = scan.nextLine();
      System.out.print("Please enter destination airport: ");
      String destination = scan.nextLine();

      Set<ArrayList<String>> shortestSet = airline.fewestStopsItinerary(g, source, destination);
      System.out.println("Found " + shortestSet.size() + " path(s):");
      for (ArrayList<String> shortest : shortestSet) {
        System.out.print("The shortest path from " + source +
            " to " + destination + " has " +
            (shortest.size() - 2) + " stop(s): ");
        for (String r : shortest) {
          System.out.print(r + " ");
        }
        System.out.println();
      }
    }
  }

  private void shortestHopsWithTransit() throws AirportNotFoundException {
    if (g == null) {
      System.out.println("Please load a graph first.");
    } else {

      for (String airport : airline.retrieveAirports(g)) {
        System.out.println(airport);
      }
      System.out.print("Please enter source airport: ");
      String source = scan.nextLine();
      System.out.print("Please enter destination airport: ");
      String destination = scan.nextLine();
      System.out.print("Please enter transit airport: ");
      String transit = scan.nextLine();

      Set<ArrayList<String>> shortestSet = airline.fewestStopsItinerary(g, source, destination, transit);
      System.out.println("Found " + shortestSet.size() + " path(s):");
      for (ArrayList<String> shortest : shortestSet) {
        System.out.print("The shortest path from " + source +
            " to " + destination + " has " +
            (shortest.size() - 2) + " stop(s): ");
        for (String r : shortest) {
          System.out.print(r + " ");
        }
        System.out.println();
      }
    }
  }

  private void tripsUnderFrom() {
    if (g == null) {
      System.out.println("Please load a graph first.");
    } else {

      System.out.print("Please enter source airport: ");
      String source = scan.nextLine();
      System.out.print("Please enter destination airport: ");
      String destination = scan.nextLine();
      double budget = 0.0;
      while (true) {
        try {
          System.out.print("Please enter your target budget in dollars: ");
          budget = Double.parseDouble(scan.nextLine());
          break;
        } catch (Exception e) {
          System.out.println("Invalid input: " + e.getMessage());
        }
      }
      int stops = 0;
      while (true) {
        try {
          System.out.print("Please enter maximum number of stops: ");
          stops = Integer.parseInt(scan.nextLine());
          break;
        } catch (Exception e) {
          System.out.println("Invalid input: " + e.getMessage());
        }
      }
      Set<ArrayList<Route>> result = airline.allTrips(g, source, destination, budget, stops);
      System.out.println("Found " + result.size() + " path(s):");

      for (ArrayList<Route> trip : result) {
        double totalPrice = 0;
        for (Route r : trip) {
          totalPrice += r.price;
        }
        System.out.print("Cost: " + String.format("%.2f", totalPrice) + " dollars: ");
        System.out.print(trip.get(0).source);
        for (Route r : trip) {
          System.out.print(" " + r.price + " " + r.destination);
        }
        System.out.println();
      }
    }
  } 
  
  private void roundTripsUnderFrom() {
    if (g == null) {
      System.out.println("Please load a graph first.");
    } else {

      System.out.print("Please enter source airport: ");
      String source = scan.nextLine();
      double budget = 0.0;
      while (true) {
        try {
          System.out.print("Please enter your target budget in dollars: ");
          budget = Double.parseDouble(scan.nextLine());
          break;
        } catch (Exception e) {
          System.out.println("Invalid input: " + e.getMessage());
        }
      }
      int stops = 0;
      while (true) {
        try {
          System.out.print("Please enter maximum number of stops: ");
          stops = Integer.parseInt(scan.nextLine());
          break;
        } catch (Exception e) {
          System.out.println("Invalid input: " + e.getMessage());
        }
      }
      Set<ArrayList<Route>> result = airline.allRoundTrips(g, source, budget, stops);
      System.out.println("Found " + result.size() + " path(s):");

      for (ArrayList<Route> trip : result) {
        double totalPrice = 0;
        for (Route r : trip) {
          totalPrice += r.price;
        }
        System.out.print("Cost: " + String.format("%.2f", totalPrice) + " dollars: ");
        System.out.print(trip.get(0).source);
        for (Route r : trip) {
          System.out.print(" " + r.price + " " + r.destination);
        }
        System.out.println();
      }
    }
  }  
}
