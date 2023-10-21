package cells;

public class ImmutableCell<T> implements Cell<T> {

  private final T value;

  public ImmutableCell(T value) {
    if (value == null) {
      throw new IllegalArgumentException();
    }
    this.value = value;
  }

  @Override
  public void set(T value) {
    throw new UnsupportedOperationException();
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
  public boolean equals(Object object) {
    return object instanceof Cell && value.equals(((Cell) object).get());
  }

  @Override
  public int hashCode() {
    return value.hashCode();
  }

}