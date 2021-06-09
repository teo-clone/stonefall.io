package edu.brown.cs.stonefall.pathing;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A SimpleVertex for use in testing.
 */
public class SimpleVertex implements Vertex<SimpleVertex, SimpleEdge>, Cartesian {
  private List<SimpleEdge> edges = new ArrayList<>();
  private String id;
  private int x;
  private int y;

  /**
   * Constructs a SimpleVertex for use in testing.
   *
   * @param id
   *          A unique id for this SimpleVertex.
   * @param x
   *          An int representing the x-coordinate of this SimpleVertex.
   * @param y
   *          An int representing the y-coordinate of this SimpleVertex.
   */
  public SimpleVertex(String id, int x, int y) {
    this.id = id;
    this.x = x;
    this.y = y;
  }

  /**
   * Connects a SimpleEdge to this SimpleVertex.
   *
   * @param edge
   *          A SimpleEdge to connect to this SimpleVertex.
   */
  void addEdge(SimpleEdge edge) {
    edges.add(edge);
  }

  @Override
  public List<SimpleEdge> getEdges() {
    return this.edges;
  }

  /**
   * Returns the id associated with this SimpleVertex.
   *
   * @return A string representing the ID of this SimpleVertex.
   */
  public String getId() {
    return this.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  @Override
  public String toString() {
    return this.id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof SimpleVertex)) {
      return false;
    }
    SimpleVertex other = (SimpleVertex) o;
    return other.getId().equals(this.getId());
  }

  @Override
  public int getX() {
    return x;
  }

  @Override
  public int getY() {
    return y;
  }

  @Override
  public double getDistance(Cartesian c) {
    return Math.sqrt(Math.pow((c.getX() - getX()), 2)
        + Math.pow((c.getY() - getY()), 2));
  }
}
