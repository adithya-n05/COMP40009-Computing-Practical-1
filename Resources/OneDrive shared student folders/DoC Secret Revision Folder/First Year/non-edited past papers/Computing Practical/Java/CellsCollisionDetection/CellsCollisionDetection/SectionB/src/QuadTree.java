/**
 * You must implement the <code>add</code> and <code>queryRegion</code> methods in the
 * region-based QuadTree class given below.
 */


import java.util.List;

/**
 * A region-based quadtree implementation.
 */
public class QuadTree implements QuadTreeInterface {

  private QuadTreeNode root;
  private int nodeCapacity;

  /**
   * Default constructor.
   *
   * @param region The axis-aligned bounding region representing the given area that the current
   * quadtree covers
   * @param capacity The maximum number of objects each quadtree node can store. If a quadtree
   * node's number of stored objects exceeds its capacity, the node should be subdivided.
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
  public void add(Object2D elem) {
    // TODO: Implement this method for Question 2
    if (elem == null) {
      return;
    }

    if (root.isLeaf()) {
      if (root.values.size() < nodeCapacity) {
        root.values.add(root.values.size() + 1, elem);
      } else {
        root.subdivide();
        for (int i = 0; i < root.values.size(); i++) {
          Object2D element = root.values.get(i);
          add(element);
          root.values.remove(i);
        }
      }
    } else { // Ranking: NE, NW, SE, SW
      if (root.NE.region.covers(elem.getCenter())) {
        root.NE = addHelper(root.NE, elem);
      } else if (root.NW.region.covers(elem.getCenter())) {
        root.NW = addHelper(root.NW, elem);
      } else if (root.SE.region.covers(elem.getCenter())) {
        root.SE = addHelper(root.SE, elem);
      } else if (root.SW.region.covers(elem.getCenter())) {
        root.SW = addHelper(root.SW, elem);
      }
    }
  }

  /**
   * <p> Implement this method for Question 2 </p>
   *
   * @param elem the 2D-object to add to the tree.
   * @param node the root of the current subtree to visit
   */
  private QuadTreeNode addHelper(QuadTreeNode node, Object2D elem) {
    // TODO: Implement this method for Question 2
    QuadTree tempTree = new QuadTree(node.region, nodeCapacity);
    tempTree.add(elem);
    return tempTree.root;
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
  public ListInterface<Object2D> queryRegion(AABB region) {
    if (root.region.covers(region.topLeft()) || root.region.covers(region.bottomRight())) {
      if (root.isLeaf()) {
        ListInterface<Object2D> bucket = root.values;
        for (int i = 0; i < bucket.size(); i++) {
          if (!region.covers(bucket.get(i).getCenter())) {
            bucket.remove(i);
          }
        }
        return bucket;
      } else {
        queryRegionHelper(root.NE, region, root.values);
        queryRegionHelper(root.NW, region, root.values);
        queryRegionHelper(root.SE, region, root.values);
        queryRegionHelper(root.SW, region, root.values);
      }
    }

    ListInterface<Object2D> returnList = root.values;

    root.values = new ListArrayBased<>(); // Clears the list
    return returnList;
  }

  /**
   * <p> Implement this method for Question 3 </p>
   *
   * Auxiliary method that recursively goes down from the root of the tree through all * the
   * children whose regions overlap with the given region. When a leaf node is reached, then only
   * the 2D-objects stored at this leaf node that are covered by the given region are collected.
   *
   * @param region axies-aligned bounding box region
   * @param node the root of the current subtree to visit
   */
    private void queryRegionHelper(QuadTreeNode node, AABB region,
      ListInterface<Object2D> bucket) {
      QuadTree nodeTree = new QuadTree(node.region, nodeCapacity);
      ListInterface<Object2D> objectList = nodeTree.queryRegion(region);
      for (int i = 0; i <= objectList.size(); i++) {
        if (objectList.get(i) != null) {
          bucket.add(bucket.size(), objectList.get(i));
        }
      }
  }

  /**
   * Returns true if a 2D-object is in the tree.
   *
   * @param elem the 2D-object to search for in the tree.
   */
  public boolean contains(Object2D elem) {
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
