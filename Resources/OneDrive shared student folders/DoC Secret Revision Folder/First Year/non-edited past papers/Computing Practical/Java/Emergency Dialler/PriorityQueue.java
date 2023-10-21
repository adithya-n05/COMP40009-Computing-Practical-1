import java.util.Iterator;

public interface PriorityQueue<T> extends Iterable<T> {

  void add(double priority, T object);

  void clear();

  boolean isEmpty();

  T peek();

  T dequeue();

  Iterator iterator();

}