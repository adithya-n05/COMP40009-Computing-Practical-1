public interface PriorityQueueInterface<E extends Comparable<E>> {

  void add(E newEntry) throws PriorityQueueException;
  //  post: Adds a new entry to the priority queue according to
  // the priority value (i.e.its frequency value).

  E getMin();
  // post: Returns the object E with the highest priority (i.e. lowest
  // frequency value) or returns null if the priority queue is empty

  E removeMin();
  // post: Removes and returns the object E with the highest
  // priority (i.e. lowest frequency value) or returns null
  // if the priority queue is empty

  boolean isEmpty();
  // post: Returns true if the queue is empty. False otherwise.

  int getSize();
  // post: returns the size of the priority queue.

}