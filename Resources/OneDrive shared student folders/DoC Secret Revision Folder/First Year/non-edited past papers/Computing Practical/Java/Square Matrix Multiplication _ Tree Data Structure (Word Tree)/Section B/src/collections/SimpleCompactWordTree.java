package collections;

import collections.exceptions.InvalidWordException;
import java.util.ArrayList;
import java.util.List;

public class SimpleCompactWordTree implements CompactWordsSet {

  private int size;
  private CharNode root;

  public SimpleCompactWordTree() {
    size = 0;
    root = new CharNode(' ');
  }

  @Override

  public boolean add(String word) throws InvalidWordException {
    CompactWordsSet.checkIfWordIsValid(word);
    int pos = 0;
    CharNode parent = root;
    CharNode curr;
    parent.lock();

    try {
      while (pos < word.length()) {
        char letter = word.charAt(pos);
        curr = parent.getChild(letter);
        if (curr == null) {
          curr = new CharNode(letter);
          parent.addChild(curr);
        }
        parent.unlock();
        curr.lock();
        parent = curr;
        pos++;
      }
      if (parent.isValid()) {
        return false;
      } else {
        incrementSize();
        parent.setValid();
        return true;
      }
    } finally {
      parent.unlock();
    }
  }

  @Override
  public boolean remove(String word) throws InvalidWordException {
    CompactWordsSet.checkIfWordIsValid(word);

    int pos = 0;
    CharNode parent = root;
    parent.lock();
    CharNode curr;

    try {
      while (pos < word.length()) {
        char letter = word.charAt(pos);
        curr = parent.getChild(letter);

        if (curr == null) {
          return false;
        }

        parent.unlock();
        curr.lock();
        parent = curr;
        pos++;
      }

      if (parent.isValid()) {
        decrementSize();
        parent.setInvalid();
        return true;
      }

      return false;
    } finally {
      parent.unlock();
    }
  }

  @Override
  public boolean contains(String word) throws InvalidWordException {
    CompactWordsSet.checkIfWordIsValid(word);

    int pos = 0;
    CharNode parent = root;
    parent.lock();
    CharNode curr;
    try {
      while (pos < word.length()) {
        char letter = word.charAt(pos);
        curr = parent.getChild(letter);

        if (curr == null) {
          return false;
        }

        parent.unlock();
        curr.lock();
        parent = curr;
        pos++;
      }

      return parent.isValid();
    } finally {
      parent.unlock();
    }
  }

  @Override
  public int size() {
    return size;
  }

  public synchronized void incrementSize() {
    size++;
  }

  public synchronized void decrementSize() {
    assert size >= 1;
    size--;
  }

  @Override
  public List<String> uniqueWordsInAlphabeticOrder() {

    List<String> words = new ArrayList<>();

    for (CharNode child : root.getChildren()) {
      if (child != null) {
        parseTree(child, "", words);
      }
    }

    return words;
  }

  public void parseTree(CharNode node, String string, List<String> words) {

    string = string + node.getContent();

    if (node.isValid()) {
      words.add(string);
    }

    for (CharNode child : node.getChildren()) {
      if (child != null) {
        parseTree(child, string, words);
      }
    }
  }
}
