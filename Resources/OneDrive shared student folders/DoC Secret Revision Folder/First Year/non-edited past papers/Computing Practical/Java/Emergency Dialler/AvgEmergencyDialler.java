import java.util.Set;

public class AvgEmergencyDialler extends EmergencyDialler {

  public AvgEmergencyDialler(Location location, Dialler dialler) {
    super(location, dialler);
  }

  @Override
  public void addToEmergencyContactList(Contact contact) {
    Set<Person> people = contact.getPeople();
    queue.add(
        people.stream().map(Person::getAddress)
        .mapToDouble(l -> l.distanceTo(location)).sum()
        / people.stream().count(), contact
    );
  }

}