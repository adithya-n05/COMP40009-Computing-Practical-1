package aeroplane;

public abstract class NonCrewMember extends Passenger {

  private int age;

  public NonCrewMember(String firstName, String surname, int age)
      throws MalformedDataException {
    super(firstName, surname);
    if (age <= 0) {
      throw new MalformedDataException();
    }
    this.age = age;
  }

  @Override
  public boolean isAdult() {
    return age >= 18;
  }

  @Override
  public String toString() {
    return super.toString() + " Age " + age;
  }

}