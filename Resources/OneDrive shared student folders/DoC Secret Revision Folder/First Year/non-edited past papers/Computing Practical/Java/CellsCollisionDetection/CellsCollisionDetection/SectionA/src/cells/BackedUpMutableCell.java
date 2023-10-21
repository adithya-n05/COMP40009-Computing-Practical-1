package cells;


import java.util.ArrayDeque;
import java.util.Deque;

public class BackedUpMutableCell<T> extends MutableCell<T> implements BackedUpCell<T> {

  private final Deque<T> prevValues;
  protected final int limit;

  public BackedUpMutableCell() {
    limit = -1;
    // Its fine for limit to be -1 since limit is only bound by equals.
    value = null;
    prevValues = new ArrayDeque<>();
  }

  public BackedUpMutableCell(int limit) {
    if (limit < 0) {
      throw new IllegalArgumentException("Cannot have a negative limit");
    }
    this.limit = limit;
    prevValues = new ArrayDeque<>(limit);
  }

  @Override
  public boolean hasBackup() {
    return !prevValues.isEmpty();
  }

  @Override
  public void revertToPrevious() {
    if (!hasBackup()) {
      throw new UnsupportedOperationException("No backups available.");
    }
    value = prevValues.pollFirst();
  }

  @Override
  public void set(T value) {
    if (this.value != null && value != null && limit != 0) {
      if (prevValues.size() == limit) {
        prevValues.pollFirst();
      }
      prevValues.push(this.value);
    }
    super.set(value);
  }
}
