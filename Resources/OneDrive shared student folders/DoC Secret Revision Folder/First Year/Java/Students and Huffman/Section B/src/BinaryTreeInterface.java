public interface BinaryTreeInterface<K extends Comparable<K>> extends
    Comparable<BinaryTreeInterface<K>> {

  boolean isEmpty();
  // return true if BinaryTree is empty otherwise return false

  K getRootData();
  // return item stored in root

  BinaryTreeInterface<K> getLeftSubtree();
  // return left subtree

  BinaryTreeInterface<K> getRightSubtree();
  // return right subtree

}