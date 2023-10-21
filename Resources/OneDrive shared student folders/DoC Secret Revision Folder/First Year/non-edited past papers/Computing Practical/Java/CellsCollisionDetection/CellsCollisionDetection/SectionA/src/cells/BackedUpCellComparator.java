package cells;

import java.util.Comparator;

public class BackedUpCellComparator<U> implements Comparator<BackedUpCell<U>> {

  Comparator<U> valueComparator;

  public BackedUpCellComparator(Comparator<U> comparator) {
    this.valueComparator = comparator;
  }


  @Override
  public int compare(BackedUpCell<U> obj1, BackedUpCell<U> obj2) {
    if (!obj1.isSet() || !obj2.isSet()) {
      return
          !obj1.isSet() && !obj2.isSet()
              ? 0 : !obj1.isSet()
              ? -1 : 1;
    }

    int comparisonValue = valueComparator.compare(obj1.get(), obj2.get());
    if (comparisonValue == 0) {

      if (obj1.hasBackup() && obj2.hasBackup()) {
        obj1.revertToPrevious();
        obj2.revertToPrevious();
        comparisonValue = compare(obj1, obj2);
      } else {
        comparisonValue = !obj1.hasBackup() && !obj2.hasBackup()
            ? 0 : !obj1.hasBackup()
            ? -1 : 1;
      } // doesnt pass last test.
    }



    return comparisonValue;
  }
}
