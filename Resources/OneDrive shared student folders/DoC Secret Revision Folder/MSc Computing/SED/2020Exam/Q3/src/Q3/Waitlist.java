package Q3;

public interface Waitlist {
    void add(Customer customer, Show show, int count);

    void anyoneWaiting(Show show, int count);
}
