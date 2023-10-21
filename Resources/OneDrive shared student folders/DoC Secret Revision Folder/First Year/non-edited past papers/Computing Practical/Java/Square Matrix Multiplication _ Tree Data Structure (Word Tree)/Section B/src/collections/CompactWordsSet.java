package collections;

import collections.exceptions.InvalidWordException;
import java.util.List;

public interface CompactWordsSet {

  static void checkIfWordIsValid(String word) throws InvalidWordException {
    if (word == null || word == "") {
      throw new InvalidWordException("expected a non empty word");
    }

    if (word.chars().filter(v -> v < 97 || v > 122).count() > 0) {
      throw new InvalidWordException("there are invalid letters, use a-z only");
    }
  }

  boolean add(String word) throws InvalidWordException;

  boolean remove(String word) throws InvalidWordException;

  boolean contains(String word) throws InvalidWordException;

  int size();

  List<String> uniqueWordsInAlphabeticOrder();

}