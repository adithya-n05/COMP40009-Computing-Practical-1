package aeroplane;

public abstract class Passenger {

	protected String name;
	protected String surname;

  public Passenger(String name, String surname) {
    this.name = name;
    this.surname = surname;
  }

  public abstract boolean isAdult();

  public String toString() {
    return name + " " + surname;
  }

}
