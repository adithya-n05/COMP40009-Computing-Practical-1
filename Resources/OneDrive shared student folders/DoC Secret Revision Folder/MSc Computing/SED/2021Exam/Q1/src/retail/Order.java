package retail;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;

public abstract class Order {
    protected final List<Product> items;
    protected final CreditCardDetails creditCardDetails;
    protected final Address billingAddress;
    protected final Address shippingAddress;
    protected final Courier courier;

    public Order(List<Product> items, CreditCardDetails creditCardDetails, Address billingAddress, Address shippingAddress, Courier courier) {
        this.items = Collections.unmodifiableList(items);
        this.creditCardDetails = creditCardDetails;
        this.billingAddress = billingAddress;
        this.shippingAddress = shippingAddress;
        this.courier = courier;
    }

    protected BigDecimal round(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.CEILING);
    }

    public void process(){
        process(CreditCardProcessor.getInstance());
    }
    public void process(CardProcessor paymentProcessor) {

      BigDecimal total = new BigDecimal(0);

      for (Product item : items) {
        total = total.add(item.unitPrice());
      }

      total = calculateCost(total);

      paymentProcessor.charge(round(total), creditCardDetails, billingAddress);

      courier.send(packageOrder(), shippingAddress);
    }

    protected abstract Parcel packageOrder();

    protected abstract BigDecimal calculateCost(BigDecimal total);
}
