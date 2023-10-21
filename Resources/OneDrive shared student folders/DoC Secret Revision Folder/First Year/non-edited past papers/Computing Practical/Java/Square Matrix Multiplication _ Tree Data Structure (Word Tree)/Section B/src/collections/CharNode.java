package collections;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CharNode {

  final int A_MAX_VALUE = 97;
  private final char content;
  private boolean valid;
  private Lock lock = new ReentrantLock();
  private CharNode[] children;

  public CharNode(char content) {
    this.content = content;
    valid = false;
    children = new CharNode[26];
  }

  public char getContent() {
    return content;
  }

  public boolean isValid() {
    return valid;
  }

  public void setValid() {
    this.valid = true;
  }

  public void setInvalid() {
    this.valid = false;
  }

  public void lock() {
    lock.lock();
  }

  public void unlock() {
    lock.unlock();
  }

  public synchronized void addChild(CharNode child) {
    children[child.getContent() - A_MAX_VALUE] = child;
  }

  public synchronized CharNode getChild(char c) {
    return children[c - A_MAX_VALUE];
  }

  public CharNode[] getChildren() {
    return children;
  }

}
