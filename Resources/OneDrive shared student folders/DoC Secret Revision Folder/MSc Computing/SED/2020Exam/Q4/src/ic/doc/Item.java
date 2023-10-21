package ic.doc;

import java.util.Objects;

public class Item {

  private final String description;
  private final Integer priceInPounds;

  public Item(String description, Integer priceInPounds) {
    this.description = description;
    this.priceInPounds = priceInPounds;
  }

  public Integer priceInPounds() {
    return priceInPounds;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Item item = (Item) o;
    return description.equals(item.description) &&
        priceInPounds.equals(item.priceInPounds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(description, priceInPounds);
  }
}
