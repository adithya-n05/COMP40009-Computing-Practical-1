public interface QuadTreeInterface {

  /**
   * Adds a 2D-object with Cartesian coordinates to the tree.
   *
   * @param elem the 2D-object to add to the tree.
   */
  void add(Object2D elem);

  /**
   * Given an axis-aligned bounding box region, it returns all the 2D-objects
   * in the quadtree that are within the region.
   *
   * @return a list of 2D-objects
   */
  ListInterface<Object2D> queryRegion(AABB region);

  /**
   * Checks if the quad-tree contains the given object.
   *
   * @return true if and only if the given elem is in the quadtree
   */
  boolean contains(Object2D elem);

}