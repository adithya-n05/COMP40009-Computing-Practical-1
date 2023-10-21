package retail;

import java.math.BigDecimal;
import java.util.List;

public class SmallOrder extends Order {

  private static final BigDecimal GIFT_WRAP_CHARGE = new BigDecimal(3);

  private final boolean giftWrap;

  public SmallOrder(
      List<Product> items,
      CreditCardDetails creditCardDetails,
      Address billingAddress,
      Address shippingAddress,
      Courier courier,
      boolean giftWrap) {
    super(items, creditCardDetails, billingAddress, shippingAddress, courier);
    this.giftWrap = giftWrap;
  }

  @Override
  protected Parcel packageOrder() {
    if (giftWrap) {
      return new GiftBox(items);
    } else {
      return new Parcel(items);
    }
  }

  @Override
  protected BigDecimal calculateCost(BigDecimal total) {
    total = total.add(courier.deliveryCharge());

    if (giftWrap) {
      total = total.add(GIFT_WRAP_CHARGE);
    }
    return total;
  }
}
