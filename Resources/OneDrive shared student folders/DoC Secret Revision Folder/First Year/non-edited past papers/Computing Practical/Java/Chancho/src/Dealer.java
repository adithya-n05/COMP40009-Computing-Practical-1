package src;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * The class Dealer encapsulates the actions of a Chancho game-dealer. The game
 * dealer is responsible for dealing cards from the provided game-deck to each
 * player, and scheduling rounds of the game until a player has won the game.
 * All players who have declared themselves as a winner should be congratulated.
 *
 * Developers should provide the constructor,
 *
 * public Dealer(Set<Player> players, Deck gameDeck);
 */
public final class Dealer {

  private final List<Player> players;

  public Dealer(Set<Player> players, Deck gameDeck) {
    this.players = new ArrayList<>(players);
    for (Player player : players) {
      for (int card = 0; card < 4; card++) {
        player.addToHand(gameDeck.removeFromTop());
      }
    }
  }

  public void playGame() {
    while (players.stream().noneMatch(Player::hasWon)) {
      for (Player player : players) {
        player.discard();
        if (player != players.get(0)) {
          player.pickup();
        }
      }
      players.get(0).pickup();
    }
    congratulateWinners();
  }

  private void congratulateWinners() {
    System.out.println("The game has been won! Congratulations to:");
    for (Object player : players.stream().filter(Player::hasWon).toArray()) {
      System.out.println(player);
    }
  }

}