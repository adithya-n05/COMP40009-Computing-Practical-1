package ic.doc;

import com.worldpay.CardNumber;
import com.worldpay.CreditCardTransaction;
import com.worldpay.TransactionProcessor;

public class WorldpayPaymentProcessor implements PaymentProcessor {

    @Override
    public void pay(String cardNumber, int pounds, int pence) {
        CreditCardTransaction transaction = new CreditCardTransaction(new CardNumber(cardNumber), pounds, pence);
        new TransactionProcessor().process(transaction);
    }
}
