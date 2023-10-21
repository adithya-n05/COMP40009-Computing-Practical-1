public interface GenericListInterface<T> extends Iterable<T> {

  int size();

  boolean isEmpty();

  T get(int index) throws ListIndexOutOfBoundsException;

  void add(int index, T newItem) throws ListIndexOutOfBoundsException;

  void remove(int index) throws ListIndexOutOfBoundsException;

}