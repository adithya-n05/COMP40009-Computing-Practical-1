package aeroplane;

public class BusinessClass extends EconomyClass {

  private Luxury luxury;

  public BusinessClass(String name, String surname, int age, Luxury luxury) {
    super(name, surname, age);
    this.luxury = luxury;
  }

  @Override
  public String toString() {
    return "Business Class Passenger: " + name + " " + surname + " is " + age + " years old" + " has luxury: "
        + luxury;
  }

}
