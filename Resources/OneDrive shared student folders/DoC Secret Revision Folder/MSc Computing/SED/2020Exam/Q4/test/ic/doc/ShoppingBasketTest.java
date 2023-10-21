package ic.doc;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;


public class ShoppingBasketTest {

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();
    PaymentProcessor paymentProcessor = context.mock(PaymentProcessor.class);

    ShoppingBasket basket = new ShoppingBasket(paymentProcessor);

    @Test
    public void canAddItem(){
        basket.addItem(new Item("Laptop", 500));
    }

    @Test
    public void canAddCardDetails(){
        basket.enterCardDetails("1234123412341234");
    }

    @Test
    public void canCheckoutShopping(){
        String cardNumber = "1234123412341234";
        int price = 500;

        context.checking(new Expectations() {{
            oneOf(paymentProcessor).pay(cardNumber, price, 0);
        }});

        basket.addItem(new Item("Laptop", price));
        basket.enterCardDetails(cardNumber);
        basket.checkout();
    }

}