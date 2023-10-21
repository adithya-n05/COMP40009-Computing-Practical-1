import java.util.Set;

public class MaxEmergencyDialler extends EmergencyDialler {

  public MaxEmergencyDialler(Location location, Dialler dialler) {
    super(location, dialler);
  }

  @Override
  public void addToEmergencyContactList(Contact contact) {
    queue.add(
        contact.getPeople().stream().map(Person::getAddress)
        .mapToDouble(l -> l.distanceTo(location))
        .max().orElse(Integer.MIN_VALUE), contact
    );
  }

}