import java.util.Iterator;

public class LinkedListPriorityQueue<T> implements PriorityQueue<T> {

  private Node<T> high;

  @Override
  public void add(double priority, T object) {
    Node<T> newNode = new Node<>(priority, object);
    if (high == null || priority < high.getPriority()) {
      newNode.setNext(high);
      high = newNode;
      return;
    }
    Node<T> node = high;
    while (priority >= node.getPriority()) {
      if (node.getNext() == null || priority < node.getNext().getPriority()) {
        break;
      }
      node = node.getNext();
    }
    Node<T> next = node.getNext();
    node.setNext(newNode);
    newNode.setNext(next);
  }

  @Override
  public void clear() {
    high = null;
  }

  @Override
  public boolean isEmpty() {
    return high == null;
  }

  @Override
  public T peek() {
    return high == null ? null : high.getValue();
  }

  @Override
  public T dequeue() {
    if (high == null) {
      return null;
    }
    Node<T> high = this.high;
    this.high = this.high.getNext();
    high.setNext(null);
    return high.getValue();
  }

  @Override
  public Iterator iterator() {
    return new Iterator<T>() {

      private Node<T> node = high;

      @Override
      public boolean hasNext() {
        return node.getNext() != null;
      }

      @Override
      public T next() {
        node = node.getNext();
        return node.getValue();
      }

    };
  }

}