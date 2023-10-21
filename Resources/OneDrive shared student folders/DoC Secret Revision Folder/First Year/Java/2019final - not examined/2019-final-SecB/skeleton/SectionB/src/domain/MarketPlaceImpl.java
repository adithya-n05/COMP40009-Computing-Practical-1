package domain;

import domain.goods.PlasticGood;
import domain.goods.RawPlastic;
import java.util.Optional;


public class MarketPlaceImpl implements MarketPlace {

  private final boolean DEBUG_MESSAGES = true;

  public void sellRawPlastic(RawPlastic plasticItem) {
    if (DEBUG_MESSAGES) {
      System.out
          .println("Thread: " + Thread.currentThread().getId() + " - Sell plastic: " + plasticItem);
    }

    //TODO for Question 2
  }

  public Optional<RawPlastic> buyRawPlastic() {
    //TODO for Question 2
    return Optional.empty();
  }

  public void sellPlasticGood(PlasticGood good) {
    if (DEBUG_MESSAGES) {
      System.out.println("Thread: " + Thread.currentThread().getId() + " - Sell good: " + good);
    }
    //TODO for Question 2
  }

  public Optional<PlasticGood> buyPlasticGood() {
    //TODO for Question 2
    return Optional.empty();
  }

  public void disposePlasticGood(PlasticGood good) {
    if (DEBUG_MESSAGES) {
      System.out.println("Thread: " + Thread.currentThread().getId() + " - Dispose good: " + good);
    }
    //TODO for Question 2
  }

  public Optional<PlasticGood> collectDisposedGood() {
    //TODO for Question 2
    return Optional.empty();
  }
}
