package retail;

import java.math.BigDecimal;

public class CreditCardProcessor implements CardProcessor {

  private static final CreditCardProcessor INSTANCE = new CreditCardProcessor();

  private CreditCardProcessor() {}

  static CreditCardProcessor getInstance() {
    return CreditCardProcessor.INSTANCE;
  }

  @Override
  public void charge(BigDecimal amount, CreditCardDetails account, Address billingAddress) {

    System.out.println("Credit card charged: " + account + " amount: " + amount);

    // further implementation omitted for exam question
  }
}

// a) This is a singleton
// b) A singleton is difficult to test.
// Only one instance of it exists, so it is more challenging to ensure a consistent state