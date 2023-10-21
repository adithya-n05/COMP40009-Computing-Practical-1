public interface QueueInterface<T> {

  void enqueue(T newEntry);

  T dequeue();

  boolean isEmpty();

}