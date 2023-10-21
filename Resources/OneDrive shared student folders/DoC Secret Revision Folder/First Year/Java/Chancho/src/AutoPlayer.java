package src;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AutoPlayer extends AbstractPlayer {

  AutoPlayer(CardPile in, CardPile out, String name) {
    super(out, in, name);
  }

  @Override
  protected int selectCard() {
    Random rnd = new Random();
    int next = rnd.nextInt(4);
    for (int i = 0; i < HANDSIZE; i++) {
      List<Integer> different = new ArrayList<>();
      for (int j = 0; j < HANDSIZE; j++) {
        if (cards[i].getRank() != cards[j].getRank()) {
          different.add(j);
        }
      }
      switch (different.size()) {
        case 0:
          return next;
        case 1:
          return different.get(0);
        case 2:
          int zero = different.get(0);
          int one = different.get(1);
          return cards[zero].getRank() == cards[one].getRank()
              ? next : rnd.nextBoolean() ? zero : one;
        default:
          break;
      }
    }
    return next;
  }

}