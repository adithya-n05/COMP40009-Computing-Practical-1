public class Queue<T> implements QueueInterface<T>{
	
	private Node<T> first;
	private Node<T> last;
	
	public boolean isEmpty() {
		return last == null;
	}
	
	//post: Adds the given item to the queue
	public void enqueue(T item) {

	  Node<T> newNode = new Node<T>(item);

	  if (isEmpty()) {
	    first = newNode;
    } else {
      last.setNext(newNode);
    }

    last = newNode;

	}
	
	//post: Removes and returns the head of the queue. It throws an 
	//      exception if the queue is empty.
	public T dequeue() throws QueueException {

	  if (isEmpty()) {
	    throw new QueueException("Error! Queue is empty!");
    } else {
      T oldHead = first.getItem();
	    if (first == last) {
	      first = null;
	      last = null;
      } else {
        first = first.getNext();
      }
      return oldHead;
	  }

	}
	
}
