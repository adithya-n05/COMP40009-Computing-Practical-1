package src;

/**
 * The Player interface models a Chancho game player.
 *
 * Developers must ensure the player is not in possession of more than HANDSIZE
 * cards on any occasion.
 */
public interface Player {

  // the maximum number of cards a Player may hold.
  int HANDSIZE = 4;

  // select a card to discard from the player's hand and
  // place it on the CardPile to the player's left.
  void discard();

  // pick up a card from the CardPile on the player's right and
  // add it to the player's hand.
  void pickup();

  // returns true if this Player has won the game. i.e. if the player holds
  // all the suit values for a particular card rank.
  boolean hasWon();

  // add the provided card to this player's hand.
  boolean addToHand(Card card);

}