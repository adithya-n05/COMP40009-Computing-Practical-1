import java.util.HashSet;
import java.util.Set;

public class Group implements Contact {

  private String name;
  private Set<Contact> contacts;

  public Group(String _name) {
    name = _name;
    contacts = new HashSet<>();
  }

  public boolean add(Contact contact) {
    if (contacts == null) {
      return false;
    }
    contacts.add(contact);
    return true;
  }

  public boolean remove(Contact contact) {
    if (contacts == null || !contacts.contains(contact)) {
      return false;
    }
    contacts.remove(contact);
    return true;
  }

  public Set<Person> getPeople() {
    Set<Person> set = new HashSet<>();
    for (Contact contact : contacts) {
      set.addAll(contact.getPeople());
    }
    return set;
  }

  public String getName() {
    return name;
  }

  public int size() {
    return contacts.stream().mapToInt(Contact::size).sum();
  }

}