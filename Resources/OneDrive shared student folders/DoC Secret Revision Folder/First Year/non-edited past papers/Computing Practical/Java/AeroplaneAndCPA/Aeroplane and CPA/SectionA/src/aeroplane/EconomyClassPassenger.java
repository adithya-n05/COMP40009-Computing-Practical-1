package aeroplane;

public class EconomyClassPassenger extends NonCrewMember {

  public EconomyClassPassenger(String firstName, String surname, int age)
      throws MalformedDataException {
    super(firstName, surname, age);
  }

  @Override
  public String toString() {
    return "Economy Class Passenger " + super.toString();
  }

}