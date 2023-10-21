package com.worldpay;

// DO NOT CHANGE THE CODE IN THIS FILE

public class CreditCardTransaction {

  private final CardNumber cardNumber;
  private final int amount;
  private final int pence;

  public CreditCardTransaction(CardNumber cardNumber, int pounds, int pence) {
    this.cardNumber = cardNumber;
    this.amount = pounds;
    this.pence = pence;
  }
}
