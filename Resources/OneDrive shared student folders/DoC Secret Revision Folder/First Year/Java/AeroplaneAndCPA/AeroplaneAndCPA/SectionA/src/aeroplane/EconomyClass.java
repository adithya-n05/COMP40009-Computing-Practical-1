package aeroplane;

public class EconomyClass extends Passenger {

  protected int age;

  public EconomyClass(String name, String surname, int age) {
    super(name, surname);
    assert (age >= 0): "Age >= 0";
    this.age = age;
  }

  public boolean isAdult() {
    return age >= 18;
  }

  @Override
  public String toString() {
    return "Economy Class Passenger: " + super.toString() + " is " + age + " years old";
  }

}
