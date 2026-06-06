import java.util.Objects;

final public class Route {
  public String source;
  public String destination;
  public double price;

  public Route(String source, String destination, double price){
    this.source = source;
    this.destination = destination;
    this.price = price;
  }

  @Override
  public boolean equals(Object other){
    if (other instanceof Route){
      Route otherRoute = (Route) other;
      return equalEndPoints(otherRoute)
             && price == otherRoute.price;
    }
    return false;
  }

  private boolean equalEndPoints(Route otherRoute){
    return (source.equals(otherRoute.source)
            && destination.equals(otherRoute.destination))
          ||(source.equals(otherRoute.destination)
            && destination.equals(otherRoute.source));
  }

  @Override
  public int hashCode(){
    return Objects.hash(source.hashCode() + destination.hashCode(), price);
  }

  @Override
  public String toString(){
    return source + " ($" + price + ") "
      + destination;
  }
}
