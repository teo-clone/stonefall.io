package edu.brown.cs.stonefall.pathing;

import java.util.List;

/**
 * An interface for an Edge.
 * @param <V> V represents Vertex objects that can be
 *           accessed from this Edge.
 * @param <E> E represents Edge objects that can be accessed from this Edge.
 */
public interface Vertex<V extends Vertex<V, E>, E extends Edge<V, E>> {
  /**
   * Returns a list of Edges accessible from this Vertex.
   *
   * @return A list of Edges accessible from this Vertex.
   */
  List<E> getEdges();
}
