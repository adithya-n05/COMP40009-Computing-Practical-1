package aeroplane;

public class Crew extends Passenger {

  public Crew(String name, String surname) {
    super(name, surname);
  }

  public boolean isAdult() {
    return true;
  }

  @Override
  public String toString() {
    return "Crew: " + super.toString();
  }

}
