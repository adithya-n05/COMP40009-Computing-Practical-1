package ic.doc;

public interface PaymentProcessor {
    void pay(String cardNumber, int pounds, int pence);
}
