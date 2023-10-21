package cells;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;

public class BackedUpCellComparator<U> implements Comparator<BackedUpCell<U>> {

  private Comparator<U> valueComparator;

  public BackedUpCellComparator(Comparator<U> valueComparator) {
    this.valueComparator = valueComparator;
  }

  @Override
  public int compare(BackedUpCell<U> first, BackedUpCell<U> second) {
    if (!first.isSet() || !second.isSet()) {
      return first.isSet() ? 1 : second.isSet() ? -1 : 0;
    }
    Deque<U> values = new ArrayDeque<>();
    int difference = valueComparator.compare(first.get(), second.get());
    while (difference == 0) {
      if (!first.hasBackup() || !second.hasBackup()) {
        difference = first.hasBackup() ? 1 : second.hasBackup() ? -1 : 0;
        break;
      }
      values.push(first.get());
      values.push(second.get());
      first.revertToPrevious();
      second.revertToPrevious();
      difference = valueComparator.compare(first.get(), second.get());
    }
    while (!values.isEmpty()) {
      second.set(values.pop());
      first.set(values.pop());
    }
    return difference;
  }

}