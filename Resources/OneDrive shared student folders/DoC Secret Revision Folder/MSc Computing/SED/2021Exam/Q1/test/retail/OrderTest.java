package retail;


import org.jmock.Expectations;
import org.junit.Rule;
import org.junit.Test;
import org.jmock.integration.junit4.JUnitRuleMockery;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;

public class OrderTest {

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    CardProcessor testProcessor = context.mock(CardProcessor.class);
    Courier testCourier = context.mock(Courier.class);

    @Test
    public void smallOrderIsChargedCorrectly(){

        CreditCardDetails details = new CreditCardDetails("1234123412341234", 111);
        Address address = new Address("180 Queens Gate, London, SW7 2AZ");
        List order = List.of(
                new Product("One Book", new BigDecimal("10.00"))
        );

        context.checking(
                new Expectations() {
                    {
                        oneOf(testCourier).deliveryCharge();
                        will(returnValue(new BigDecimal("3.00")));
                        oneOf(testProcessor).charge(new BigDecimal("13.00"),details, address);
                        oneOf(testCourier).send(with(any(Parcel.class)),with(equalTo(address)));
                    }
                });


  /*      SmallOrder smallOrder =
                new SmallOrder(
                        order,
                        details,
                        address,
                        address,
                        testCourier,
                        false);*/

        Order smallOrder2 = new OrderBuilder().setItems(order)
                .setBillingAddress(address)
                .setCourier(testCourier)
                .setCreditCardDetails(details)
                .setGiftWrap(false)
                .createOrder();

        smallOrder2.process(testProcessor);

    }

    @Test
    public void bigOrderIsChargedCorrectly(){

        CreditCardDetails details = new CreditCardDetails("1234123412341234", 111);
        Address address = new Address("180 Queens Gate, London, SW7 2AZ");
        List order = List.of(
                new Product("One Book", new BigDecimal("10.00")),
                new Product("One Book", new BigDecimal("10.00")),
                new Product("One Book", new BigDecimal("10.00")),
                new Product("One Book", new BigDecimal("10.00")),
                new Product("One Book", new BigDecimal("10.00")),
                new Product("One Book", new BigDecimal("10.00")));

        context.checking(
                new Expectations() {
                    {
                        oneOf(testProcessor).charge(new BigDecimal("54.00"),details, address);
                        oneOf(testCourier).send(with(any(Parcel.class)),with(equalTo(address)));
                    }
                });

        Order bigOrder = new OrderBuilder().setItems(order)
                .setBillingAddress(address)
                .setCourier(testCourier)
                .setCreditCardDetails(details)
                .setGiftWrap(false)
                .createOrder();

        bigOrder.process(testProcessor);

    }

}