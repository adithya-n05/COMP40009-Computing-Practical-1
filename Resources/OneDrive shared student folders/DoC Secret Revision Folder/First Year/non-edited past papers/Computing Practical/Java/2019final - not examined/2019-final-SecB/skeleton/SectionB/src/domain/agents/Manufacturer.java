package domain.agents;

import domain.MarketPlace;

public class Manufacturer extends Agent {

  private final int neededRawPlasticBatches;

  public Manufacturer(int neededRawPlasticBatches, int thinkingTimeInMillis,
      MarketPlace marketPlace) {
    super(thinkingTimeInMillis, marketPlace);
    this.neededRawPlasticBatches = neededRawPlasticBatches;
    //TODO for Question 3
  }

  @Override
  protected void doAction() {
    //TODO for Question 3
  }
}
