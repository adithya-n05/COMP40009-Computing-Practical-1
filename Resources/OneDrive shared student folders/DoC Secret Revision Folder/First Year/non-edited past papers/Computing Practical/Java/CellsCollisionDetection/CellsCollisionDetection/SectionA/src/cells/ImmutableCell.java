package cells;

import java.util.Objects;

public class ImmutableCell<T> implements Cell<T> {

  private final T value;

  public ImmutableCell(T value) {
    if (value == null) {
      throw new IllegalArgumentException("Cannot have null set for immutable cell");
    }
    this.value = value;
  }

  @Override
  public void set(T value) {
    throw new UnsupportedOperationException("Cannot reset an Immutable cell.");
  }

  @Override
  public T get() {
    return value;
  }

  @Override
  public boolean isSet() {
    return value != null;
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof ImmutableCell<?> immutableCell) {
      return value.equals(immutableCell.value);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
