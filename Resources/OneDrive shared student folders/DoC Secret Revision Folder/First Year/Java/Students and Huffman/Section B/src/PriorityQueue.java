public class PriorityQueue<E extends Comparable<E>> implements
    PriorityQueueInterface<E> {

  private static final int max_size = 256;
  private E[] items;    //a heap of HuffmanTrees
  private int size;    //number of HuffManTrees in the heap.

  @SuppressWarnings("unchecked")
  public PriorityQueue() {
    // constructor which creates an empty heap
    items = (E[]) new Comparable[max_size];
    size = 0;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public int getSize() {
    return size;
  }

  public E getMin() {
    E root = null;
    if (!isEmpty()) {
      root = items[0];
    }
    return root;
  }

  public void add(E newEntry) throws PriorityQueueException {
    // post: Adds a new entry to the priority queue according to
    // the priority value.
    if (size >= max_size) {
      throw new PriorityQueueException("Priority Queue Exception");
    }
    items[size] = newEntry;
    size++;
    heapRebuild(size);
  }

  public E removeMin() {
    // post: Removes the minimum valued item from the PriorityQueue
    E root = null;
    if (!isEmpty()) {
      root = items[0];
      items[0] = items[size - 1];
      size--;
      heapRebuild(0);
    }
    return root;
  }

  private void heapRebuild(int root) {
    // Rebuild heap to keep it ordered
    if (root == 0) {
      siftDown();
    } else {
      siftUp();
    }
  }

  private void siftUp() {
    int child = size - 1;
    int parent = (child - 1) / 2;
    while (parent >= 0) {
      if (items[child].compareTo(items[parent]) >= 0) {
        break;
      }
      swap(child, parent);
      child = parent;
      parent = (child - 1) / 2;
    }
  }

  private void siftDown() {
    int parent = 0;
    int child = 2 * parent + 1;
    while (child < size) {
      if (child + 1 < size) {
        child += items[child].compareTo(items[child + 1]) <= 0 ? 0 : 1;
      }
      if (items[parent].compareTo(items[child]) <= 0) {
        break;
      }
      swap(parent, child);
      parent = child;
      child = 2 * parent + 1;
    }
  }

  private void swap(int fst, int snd) {
    E temp = items[fst];
    items[fst] = items[snd];
    items[snd] = temp;
  }

}