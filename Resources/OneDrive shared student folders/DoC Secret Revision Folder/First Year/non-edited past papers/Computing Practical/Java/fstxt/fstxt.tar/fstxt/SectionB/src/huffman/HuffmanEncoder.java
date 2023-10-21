package huffman;

import java.util.*;

public class HuffmanEncoder {

  final HuffmanNode root;
  final Map<String, String> word2bitsequence;

  private HuffmanEncoder(HuffmanNode root,
      Map<String, String> word2bitSequence) {
    this.root = root;
    this.word2bitsequence = word2bitSequence;
  }

  public static HuffmanEncoder buildEncoder(Map<String, Integer> wordCounts) {
    //TODO: complete the implementation of this method (Q1)

    if (wordCounts == null) {
      throw new HuffmanEncoderException("wordCounts cannot be null");
    }
    if (wordCounts.size() < 2) {
      throw new HuffmanEncoderException("This encoder requires at least two different words");
    }

    // fixing the order in which words will be processed: this determinize the execution and makes
    // tests reproducible.
    TreeMap<String, Integer> sortedWords = new TreeMap<>(wordCounts);
    PriorityQueue<HuffmanNode> queue = new PriorityQueue<>(sortedWords.size());

    //YOUR IMPLEMENTATION HERE...

    Map<String, String> word2bitSequence = new HashMap<>();

    wordCounts
        .keySet()
        .forEach(x -> queue.offer(new HuffmanLeaf(wordCounts.get(x), x)));

    while (queue.size() > 1) {
      HuffmanNode left = queue.poll();
      HuffmanNode right = queue.poll();
      queue.offer(new HuffmanInternalNode(left, right));
    }

    HuffmanNode root = queue.poll();
    word2bitSequence = encodeWords(root, word2bitSequence, "");

    return new HuffmanEncoder(root, word2bitSequence);
  }

  public static Map<String, String> encodeWords(HuffmanNode node, Map<String, String> map, String seq) {
    if (node instanceof HuffmanLeaf leaf && !leaf.word.isEmpty()) {
      map.put(leaf.word, seq);
    } else if (node instanceof HuffmanInternalNode treeNode) {
      map = encodeWords(treeNode.left, map, seq.concat("0"));
      map = encodeWords(treeNode.right, map, seq.concat("1"));
    }
    return map;
  }


  public String compress(List<String> text) {
    assert text != null && text.size() > 0;
    return text
        .stream()
        .map(x -> {
          if (word2bitsequence.containsKey(x)) {
            return word2bitsequence.get(x);
          }
          throw new HuffmanEncoderException();
        })
        .reduce(String::concat)
        .get();

  }


  public List<String> decompress(String compressedText) {
    assert compressedText != null && compressedText.length() > 0;

    HuffmanNode currentNode = root;
    int charPointer = 0;
    List<String> stringList = new ArrayList<>();

    while (charPointer < compressedText.length()) {
      if (currentNode instanceof HuffmanInternalNode internalNode) {
        char nextChar = compressedText.charAt(charPointer);
        charPointer++;
        if (nextChar == '0') {
          currentNode = internalNode.left;
        } else if (nextChar == '1') {
          currentNode = internalNode.right;
        } else {
          throw new HuffmanEncoderException();
        }
      }

      if (currentNode instanceof HuffmanLeaf leaf) {
        stringList.add(leaf.word);
        currentNode = root;
      }
    }

    if (!currentNode.equals(root)) {
      throw new HuffmanEncoderException();
    }

    return stringList;
  }

  // Below the classes representing the tree's nodes. There should be no need to modify them, but
  // feel free to do it if you see it fit

  private static abstract class HuffmanNode implements Comparable<HuffmanNode> {

    private final int count;

    public HuffmanNode(int count) {
      this.count = count;
    }

    @Override
    public int compareTo(HuffmanNode otherNode) {
      return count - otherNode.count;
    }
  }


  private static class HuffmanLeaf extends HuffmanNode {

    private final String word;

    public HuffmanLeaf(int frequency, String word) {
      super(frequency);
      this.word = word;
    }
  }


  private static class HuffmanInternalNode extends HuffmanNode {

    private final HuffmanNode left;
    private final HuffmanNode right;

    public HuffmanInternalNode(HuffmanNode left, HuffmanNode right) {
      super(left.count + right.count);
      this.left = left;
      this.right = right;
    }
  }
}
