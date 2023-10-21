import java.util.Iterator;

/**
 * You must implement the <code>remove</code> and <code>PQRebuild</code>
 * methods.
 */

public class PriorityQueue<T extends Comparable<T>> implements
    PriorityQueueInterface<T> {

  private static final int max_size = 512;
  private T[] items;             // a minHeap of elements T
  private int size;              // number of elements in the minHeap.

  @SuppressWarnings("unchecked")
  public PriorityQueue() {
    items = (T[]) new Comparable[max_size];
    size = 0;
  }

  /**
   * Returns true if the priority queue is empty. False otherwise.
   */
  public synchronized boolean isEmpty() {
    return size == 0;
  }

  /**
   * Returns the size of the priority queue.
   */
  public synchronized int getSize() {
    return size;
  }

  /**
   * Returns the element with highest priority, or returns null if the priority.
   * queue is empty. The priority queue is left unchanged
   */
  public synchronized T peek() {
    T root = null;
    if (!isEmpty()) {
      root = items[0];
    }
    return root;
  }

  /**
   * Adds a new entry to the priority queue according to the priority value.
   *
   * @param newEntry the new element to add to the priority queue
   * @throws PQException an exception if the priority queue is full
   */
  public synchronized void add(T newEntry) throws PQException {
    if (size < max_size) {
      items[size] = newEntry;
      int place = size;
      int parent = (place - 1) / 2;
      while ((parent >= 0) && (items[place].compareTo(items[parent]) < 0)) {
        T temp = items[place];
        items[place] = items[parent];
        items[parent] = temp;
        place = parent;
        parent = (place - 1) / 2;
      }
      size++;
    } else {
      throw new PQException("The PriorityQueue is full");
    }
  }

  /**
   * <p> Implement this method for Question 1 </p>
   *
   * Removes the element with highest priority.
   */
  public synchronized void remove() {
    size--;
    items[0] = items[size];
    items[size] = null;
    PQRebuild(0);
  }

  /**
   * <p> Implement this method for Question 1 </p>
   */
  private void PQRebuild(int root) {
    int child = 2 * root + 1;
    if (child < size) {
      if (child + 1 < size) {
        child += items[child].compareTo(items[child + 1]) <= 0 ? 0 : 1;
      }
      if (items[root].compareTo(items[child]) > 0) {
        T temp = items[root];
        items[root] = items[child];
        items[child] = temp;
        PQRebuild(child);
      }
    }
  }

  public synchronized Iterator<Object> iterator() {
    return new PQIterator<>();
  }

  /**
   * Returns a priority queue that is a clone of the current priority queue.
   */
  @SuppressWarnings("unchecked")
  public synchronized PriorityQueue<T> clone() {
    PriorityQueue<T> clone = new PriorityQueue<>();
    clone.size = this.size;
    clone.items = (T[]) new Comparable[max_size];
    System.arraycopy(this.items, 0, clone.items, 0, size);
    return clone;
  }

  private class PQIterator<T> implements Iterator<Object> {

    private int position = 0;

    public boolean hasNext() {
      return position < size;
    }

    public Object next() {
      Object temp = items[position];
      position++;
      return temp;
    }

    public void remove() {
      throw new IllegalStateException();
    }

  }

}