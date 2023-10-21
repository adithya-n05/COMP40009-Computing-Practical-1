/**
 * You must implement the <code>add</code> and <code>queryRegion</code> methods
 * in the region-based QuadTree class given below.
 */

/**
 * A region-based quadtree implementation.
 */
public class QuadTree implements QuadTreeInterface {

  private QuadTreeNode root;
  private int nodeCapacity;

  /**
   * Default constructor.
   *
   * @param region The axis-aligned bounding region representing the given area
   * that the current quadtree covers
   * @param capacity The maximum number of objects each quadtree node can store.
   * If a quadtree node's number of stored objects exceeds its capacity, the
   * node should be subdivided.
   */
  public QuadTree(AABB region, int capacity) {
    root = new QuadTreeNode(region);
    nodeCapacity = capacity;
  }

  /**
   * <p> Implement this method for Question 2 </p>
   *
   * Adds a 2D-object with Cartesian coordinates to the tree.
   *
   * @param elem the 2D-object to add to the tree.
   */
  public synchronized void add(Object2D elem) {
    addHelper(root, elem);
  }

  /**
   * <p> Implement this method for Question 2 </p>
   *
   * @param elem the 2D-object to add to the tree.
   * @param node the root of the current subtree to visit
   */
  private QuadTreeNode addHelper(QuadTreeNode node, Object2D elem) {
    if (!node.isLeaf()) {
      Point2D centre = elem.getCenter();
      if (node.NE.region.covers(centre)) {
        node = node.NE;
      } else if (node.NW.region.covers(centre)) {
        node = node.NW;
      } else if (node.SE.region.covers(centre)) {
        node = node.SE;
      } else if (node.SW.region.covers(centre)) {
        node = node.SW;
      }
      addHelper(node, elem);
    } else {
      int size = node.values.size();
      if (size < nodeCapacity) {
        node.values.add(1, elem);
      } else {
        node.subdivide();
        addHelper(node, elem);
        while (node.values.isEmpty()) {
          addHelper(node, node.values.get(size));
          node.values.remove(size);
          size--;
        }
      }
    }
    return null;
  }

  /**
   * <p> Implement this method for Question 3 </p>
   *
   * Given an axis-aligned bounding box region, it returns all the 2D-objects
   * in the quadtree that are within the region.
   *
   * @param region axies-aligned bounding box region
   * @return a list of 2D-objects
   */
  public synchronized ListInterface<Object2D> queryRegion(AABB region) {
    ListInterface<Object2D> bucket = new ListArrayBased<>();
    queryRegionHelper(root, region, bucket);
    return bucket;
  }

  /**
   * <p> Implement this method for Question 3 </p>
   *
   * Auxiliary method that recursively goes down from the root of the tree
   * through all the children whose regions overlap with the given region.
   * When a leaf node is reached, then only the 2D-objects stored at this leaf
   * node that are covered by the given region are collected.
   *
   * @param region axies-aligned bounding box region
   * @param node the root of the current subtree to visit
   */
  private void queryRegionHelper(QuadTreeNode node, AABB region,
      ListInterface<Object2D> bucket) {
    if (node.isLeaf()) {
      for (int pos = 1; pos <= node.values.size(); pos++) {
        Object2D object = node.values.get(pos);
        if (region.covers(object.getCenter())) {
          bucket.add(1, object);
        }
      }
    } else {
      if (node.region.covers(region.topRight())
          || node.region.covers(region.topLeft())
          || node.region.covers(region.bottomLeft())
          || node.region.covers(region.bottomRight())) {
        queryRegionHelper(node.NE, region, bucket);
        queryRegionHelper(node.NW, region, bucket);
        queryRegionHelper(node.SE, region, bucket);
        queryRegionHelper(node.SW, region, bucket);
      }
    }
  }

  /**
   * Returns true if a 2D-object is in the tree.
   *
   * @param elem the 2D-object to search for in the tree.
   */
  public synchronized boolean contains(Object2D elem) {
    return containsHelper(root, elem);
  }

  /**
   * @param elem the 2D-object to search for in the tree.
   */
  private boolean containsHelper(QuadTreeNode node, Object2D elem) {
    if (node.isLeaf()) {
      return node.values.contains(elem);
    } else {
      if (node.NE.region.covers(elem.getCenter())) {
        return containsHelper(node.NE, elem);
      } else if (node.NW.region.covers(elem.getCenter())) {
        return containsHelper(node.NW, elem);
      } else if (node.SE.region.covers(elem.getCenter())) {
        return containsHelper(node.SE, elem);
      } else {
        return containsHelper(node.SW, elem);
      }
    }
  }

}