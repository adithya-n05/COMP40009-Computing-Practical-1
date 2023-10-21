package cells;

import java.util.ArrayList;
import java.util.List;

public class BackedUpMutableCell<T> extends MutableCell<T> implements
    BackedUpCell<T> {

  private final List<T> values;
  private final int limit;
  private int size;

  public BackedUpMutableCell() {
    values = new ArrayList<>();
    limit = Integer.MAX_VALUE;
    size = 0;
  }

  public BackedUpMutableCell(int limit) {
    if (limit < 0) {
      throw new IllegalArgumentException();
    }
    values = new ArrayList<>(limit);
    this.limit = limit;
    size = 0;
  }

  @Override
  public void set(T value) {
    if (isSet() && limit > 0) {
      if (size >= limit) {
        values.remove(0);
        size--;
      }
      values.add(get());
      size++;
    }
    super.set(value);
  }

  @Override
  public boolean hasBackup() {
    return !values.isEmpty();
  }

  @Override
  public void revertToPrevious() {
    if (!hasBackup()) {
      throw new UnsupportedOperationException();
    }
    size--;
    super.set(values.get(size));
    values.remove(size);
  }

}