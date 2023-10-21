package src;

/**
 * This class implements a min-heap abstract data type (as described by the
 * generic interface IMinHeap<T extends Comparable<T>>) using a fixed array of
 * size MinHeap.MAXIMUM_HEAP_SIZE.
 */
public class MinHeap<T extends Comparable<T>> implements IMinHeap<T> {

  private static final int MAXIMUM_HEAP_SIZE = 52;
  private final T[] heap;
  private int size;

  @SuppressWarnings("unchecked")
  public MinHeap() {
    heap = (T[]) new Comparable[MAXIMUM_HEAP_SIZE];
    size = 0;
  }

  @Override
  public void add(T element) throws HeapException {
    if (size >= MAXIMUM_HEAP_SIZE) {
      throw new HeapException("Heap Exception");
    }
    heap[size] = element;
    siftUp();
    size++;
  }

  @Override
  public T removeMin() {
    if (size <= 0) {
      throw new HeapException("Heap Exception");
    }
    size--;
    swap(0, size);
    T min = heap[size];
    heap[size] = null;
    siftDown();
    return min;
  }

  @Override
  public T getMin() {
    if (size <= 0) {
      throw new HeapException("Heap Exception");
    }
    return heap[0];
  }

  @Override
  public boolean isEmpty() {
    return size <= 0;
  }

  @Override
  public int size() {
    return size + 1;
  }

  private void siftUp() {
    int child = size;
    int parent = (child - 1) / 2;
    while (parent >= 0) {
      if (heap[child].compareTo(heap[parent]) >= 0) {
        break;
      }
      swap(child, parent);
      child = parent;
      parent--;
      parent /= 2;
    }
  }

  private void siftDown() {
    int parent = 0;
    int child = 2 * parent + 1;
    while (child < size) {
      if (child + 1 < size && heap[child + 1] != null) {
        child += heap[child].compareTo(heap[child + 1]) <= 0 ? 0 : 1;
      }
      if (heap[parent].compareTo(heap[child]) <= 0) {
        break;
      }
      swap(parent, child);
      parent = child;
      child *= 2;
      child++;
    }
  }

  private void swap(int fst, int snd) {
    T temp = heap[fst];
    heap[fst] = heap[snd];
    heap[snd] = temp;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append('[');
    for (int size = 0; size < this.size; size++) {
      if (size > 0) {
        sb.append(',');
      }
      sb.append(heap[size]);
    }
    sb.append(']');
    return sb.toString();
  }

}