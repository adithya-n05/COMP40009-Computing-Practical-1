package src;

/**
 * This class implements a Deck of playing cards using a min-heap to store the
 * cards. Complete implementations of the methods: size(), isEmpty(),
 * addToBottom(Card) and removeFromTop() have been provided and should not be
 * modified.
 *
 * The developer should implement the two Deck shuffling operations: cut() and
 * riffleShuffle(Deck).
 */
public final class MinHeapDeck implements Deck {

  private final IMinHeap<HeapEntry<Card>> heap;

  public MinHeapDeck() {
    heap = new MinHeap<>();
  }

  public int size() {
    return heap.size();
  }

  public boolean isEmpty() {
    return heap.isEmpty();
  }

  public void addToBottom(Card card) {
    assert card != null : "Attempting to add 'null' to a MinHeapDeck";
    heap.add(new HeapEntry<>(card, size()));
  }

  public Card removeFromTop() {
    assert !isEmpty() : "Attempting to remove a card from an empty MinHeapDeck.";
    return heap.removeMin().getItem();
  }

  public Deck cut() {
    Deck top = new MinHeapDeck();
    while (top.size() < size()) {
      top.addToBottom(removeFromTop());
    }
    return top;
  }

  public Deck riffleShuffle(Deck deck) {
    Deck riffle = new MinHeapDeck();
    while (!deck.isEmpty()) {
      riffle.addToBottom(removeFromTop());
      riffle.addToBottom(deck.removeFromTop());
    }
    return riffle;
  }

  @Override
  public String toString() {
    return heap.toString();
  }

}