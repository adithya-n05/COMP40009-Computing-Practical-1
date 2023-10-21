public abstract class EmergencyDialler extends Dialler {

  protected PriorityQueue<Contact> queue;
  protected Location location;
  private Dialler dialler;

  public EmergencyDialler(Location location, Dialler dialler) {
    queue = new LinkedListPriorityQueue<>();
    this.location = location;
    this.dialler = dialler;
  }

  public Contact emergency() {
    Contact contact = queue.dequeue();
    for (Person person : contact.getPeople()) {
      dialler.call(person.getTelephoneNumber(), "Emergency");
    }
    return contact;
  }

  public abstract void addToEmergencyContactList(Contact contact);

}