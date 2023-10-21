package ic.doc;

import java.util.HashMap;
import java.util.Map;

public class ShoppingBasket {

  private PaymentProcessor paymentProcessor;

  public ShoppingBasket(PaymentProcessor paymentProcessor){

    this.paymentProcessor = paymentProcessor;
  }

  private final Map<Item, Integer> items = new HashMap<>();
  private String cardNumber;

  public void addItem(Item item) {
    if (items.containsKey(item)) {
      items.put(item, items.get(item) + 1);
    } else {
      items.put(item, 1);
    }
  }

  public void enterCardDetails(String cardNumber) {
    this.cardNumber = cardNumber;
  }

  public void checkout() {
    int totalPounds = 0;
    int totalItems = 0;
    for (Item item : items.keySet()) {
      Integer quantity = items.get(item);
      totalItems = totalItems + quantity;
      totalPounds = totalPounds + quantity * item.priceInPounds();
    }

    if (totalItems > 3) {
      totalPounds = Math.min(totalPounds, totalPounds - 5);
    }

    paymentProcessor.pay(cardNumber, totalPounds, 0);

  }
}

// a) i) Adapter pattern
// iii) Hexagonal Architectures