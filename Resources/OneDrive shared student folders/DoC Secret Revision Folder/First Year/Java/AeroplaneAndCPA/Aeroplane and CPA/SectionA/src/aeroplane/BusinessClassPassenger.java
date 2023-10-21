package aeroplane;

public class BusinessClassPassenger extends NonCrewMember {

  private Luxury luxury;

  public BusinessClassPassenger(String firstName, String surname, int age,
      Luxury luxury) throws MalformedDataException {
    super(firstName, surname, age);
    this.luxury = luxury;
  }

  @Override
  public String toString() {
    return "Business Class Passenger " + super.toString() + " Luxury " + luxury;
  }

}