package huffman;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TreeMap;

public class HuffmanEncoder {

  final HuffmanNode root;
  final Map<String, String> word2bitsequence;

  private HuffmanEncoder(HuffmanNode root, Map<String, String> word2bitSequence) {
    this.root = root;
    this.word2bitsequence = word2bitSequence;
  }

  public static HuffmanEncoder buildEncoder(Map<String, Integer> wordCounts) {
    if (wordCounts == null) {
      throw new HuffmanEncoderException("wordCounts cannot be null");
    }
    if (wordCounts.size() < 2) {
      throw new HuffmanEncoderException("This encoder requires at least two different words");
    }

    // fixing the order in which words will be processed: this determinize the execution and makes
    // tests reproducible.
    TreeMap<String, Integer> sortedWords = new TreeMap<String, Integer>(wordCounts);
    PriorityQueue<HuffmanNode> queue = new PriorityQueue<>(sortedWords.size());

    // YOUR IMPLEMENTATION HERE...

    for (Map.Entry<String, Integer> entry : sortedWords.entrySet()) {
      queue.offer(new HuffmanLeaf(entry.getValue(), entry.getKey()));
    }

    while (queue.size() >= 2) {
      HuffmanNode first = queue.poll();
      HuffmanNode second = queue.poll();
      HuffmanNode parent = new HuffmanInternalNode(first, second);
      queue.offer(parent);
    }

    HuffmanNode root = queue.poll();
    Map<String, String> word2bitSequence = new HashMap<>();
    traverse(root, word2bitSequence, "");

    return new HuffmanEncoder(root, word2bitSequence);
  }

  private static void traverse(
      HuffmanNode curr, Map<String, String> word2bitsequence, String bits) {
    if (curr instanceof HuffmanLeaf) {
      word2bitsequence.put(((HuffmanLeaf) curr).word, bits);
    } else {
      traverse(((HuffmanInternalNode) curr).left, word2bitsequence, bits + '0');
      traverse(((HuffmanInternalNode) curr).right, word2bitsequence, bits + '1');
    }
  }

  public String compress(List<String> text) {
    assert text != null && text.size() > 0;
    StringBuilder result = new StringBuilder();

    for (String word : text) {
      if (!word2bitsequence.containsKey(word)) {
        throw new HuffmanEncoderException("Invalid word");
      }
      result.append(word2bitsequence.get(word));
    }

    return result.toString();
  }

  public List<String> decompress(String compressedText) {
    assert compressedText != null && compressedText.length() > 0;

    List<String> result = new ArrayList<>();
    Queue<Character> characterQueue = new LinkedList<>();
    for (Character character : compressedText.toCharArray()) {
      characterQueue.add(character);
    }

    while (!characterQueue.isEmpty()) {
      result.add(findWord(characterQueue, root));
    }
    return result;
  }

  private String findWord(Queue<Character> characterQueue, HuffmanNode curr) {
    if (curr instanceof HuffmanLeaf) {
      return ((HuffmanLeaf) curr).word;
    }
    if (characterQueue.isEmpty()) {
      throw new HuffmanEncoderException("Invalid Compressed Text");
    }

    Character bit = characterQueue.poll();
    if (bit == '0') {
      return findWord(characterQueue, ((HuffmanInternalNode) curr).left);
    } else {
      return findWord(characterQueue, ((HuffmanInternalNode) curr).right);
    }
  }

  // Below the classes representing the tree's nodes. There should be no need to modify them, but
  // feel free to do it if you see it fit

  private abstract static class HuffmanNode implements Comparable<HuffmanNode> {

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
