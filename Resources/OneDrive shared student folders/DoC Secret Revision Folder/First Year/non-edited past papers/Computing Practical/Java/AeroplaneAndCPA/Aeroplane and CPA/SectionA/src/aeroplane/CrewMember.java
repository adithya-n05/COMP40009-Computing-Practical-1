package aeroplane;

public class CrewMember extends Passenger {

  public CrewMember(String firstName, String surname) {
    super(firstName, surname);
  }

  @Override
  public boolean isAdult() {
    return true;
  }

  @Override
  public String toString() {
    return "Crew Member " + super.toString();
  }

}