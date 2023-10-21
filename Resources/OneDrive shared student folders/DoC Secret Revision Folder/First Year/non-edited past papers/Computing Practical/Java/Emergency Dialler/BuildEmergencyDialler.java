import java.util.HashSet;
import java.util.Set;

public abstract class BuildEmergencyDialler {

  public static void main(String[] args) {
    Contact contact1 = new Person("Jensen", 4, 2, -9);
    Group contact2 = new Group("Contact 2");
    Contact jamil = new Person("Jamil", 3, 0, 32);
    contact2.add(jamil);
    Group jiJaneGroup = new Group("Ji Jane Group");
    Contact ji = new Person("Ji", 5, -4, -9);
    Contact jane = new Person("Ji", 2, -4, 1);
    jiJaneGroup.add(ji);
    jiJaneGroup.add(jane);
    contact2.add(jiJaneGroup);
    Group contact3 = new Group("Contact 3");
    Contact joe = new Person("Joe", 1, 2, 3);
    contact3.add(joe);
    EmergencyDialler dialler
        = new AvgEmergencyDialler(new Location(0, 0), new Dialler());
    dialler.addToEmergencyContactList(contact1);
    dialler.addToEmergencyContactList(contact2);
    dialler.addToEmergencyContactList(contact3);
    dialler.emergency();
  }

}