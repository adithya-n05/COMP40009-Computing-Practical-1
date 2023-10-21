package Q3;

public class BoxOffice {
    private Theatre theatre;
    private Payments payments;
    private Waitlist waitlist;

    public BoxOffice(Theatre theatre, Payments payments, Waitlist waitlist) {
        this.theatre = theatre;
        this.payments = payments;
        this.waitlist = waitlist;
    }

    public void bookTickets(Show show, int count, Customer customer) {
        if(theatre.checkAvailable(show, count)){
            payments.pay(show.price()*count, customer);
            theatre.confirm(show, count);
            return;
        }
        waitlist.add(customer, show, count);
    }

    public void returnTickets(Show show, int count) {
        waitlist.anyoneWaiting(show, count);
    }

    public void bookTickets(Customer customer, Show show, int count) {
        payments.pay(show.price()*count, customer);
    }
}
