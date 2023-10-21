public class Queue<T> implements QueueInterface<T> {

  private Node<T> first;
  private Node<T> last;

  public boolean isEmpty() {
    return last == null;
  }

  //post: Adds the given item to the queue
  public void enqueue(T item) {
    if (first == null) {
      first = new Node<>(item);
      last = first;
      return;
    }
    last.setNext(new Node<>(item));
    last = last.getNext();
  }

  //post: Removes and returns the head of the queue. It throws an
  //      exception if the queue is empty.
  public T dequeue() throws QueueException {
    if (first == null) {
      throw new QueueException("Queue Exception");
    }
    if (first == last) {
      last = null;
    }
    Node<T> temp = first;
    first = first.getNext();
    temp.setNext(null);
    return temp.getItem();
  }

}