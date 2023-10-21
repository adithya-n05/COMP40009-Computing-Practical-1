package com.worldpay;

// DO NOT CHANGE THE CODE IN THIS FILE

public class CardNumber {

  public CardNumber(String cardnumber) {
    if (cardnumber.length() != 16) {
      throw new IllegalArgumentException("card number must be 16 digits");
    }

    // imagine more code here - not implemented for the exam

  }
}
