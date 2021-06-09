package edu.brown.cs.stonefall.pathing;

/**
 * An interface for an Edge.
 * @param <V> V represents Vertex objects that can be
 *           accessed from this Edge.
 * @param <E> E represents Edge objects that can be accessed from this Edge.
 */
public interface Edge<V extends Vertex<V, E>, E extends Edge<V, E>> {

  /**
   * Returns the weight of this Edge as a double.
   *
   * @return The weight of this Edge.
   */
  double getWeight();

  /**
   * Sets the weight of this Edge.
   *
   * @param weight A double to be set as the weight of this Edge.
   */
  void setWeight(double weight);

  /**
   * Returns the source Vertex of this Edge.
   *
   * @return The source Vertex of this Edge.
   */
  V getSrc();

  /**
   * Sets the source Vertex of this Edge.
   *
   * @param src A Vertex to be set as the source of this Edge.
   */
  void setSrc(V src);

  /**
   * Returns the destination Vertex of this Edge.
   *
   * @return The destination Vertex of this Edge.
   */
  V getDest();

  /**
   * Sets the destination Vertex of this Edge.
   *
   * @param dest A Vertex to be set as the destination of this Edge.
   */
  void setDest(V dest);
}

