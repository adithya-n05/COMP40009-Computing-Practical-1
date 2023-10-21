public interface PriorityQueueInterface<T extends Comparable<T>> extends
    Iterable<Object> {

  /**
   * Adds a new entry to the priority queue according to the priority value.
   *
   * @param newEntry the new element to add to the priority queue
   * @throws an exception if the priority queue is full
   */
  void add(T newEntry) throws PQException;

  /**
   * Returns the element with highest priority, or returns null if the priority.
   * queue is empty. The priority queue is left unchanged
   */
  T peek();

  /**
   * Removes the element with highest priority.
   */
  void remove();

  /**
   * Returns true if the priority queue is empty. False otherwise.
   */
  boolean isEmpty();

  /**
   * Returns the size of the priority queue.
   */
  int getSize();

  /**
   * Returns a priority queue that is a clone of the current priority queue.
   */
  PriorityQueue<T> clone();

}