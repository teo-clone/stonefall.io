package edu.brown.cs.stonefall.map;

import edu.brown.cs.stonefall.pathing.Edge;

import java.util.Objects;

/**
 * A class representing a connection between two GridBlocks.
 *
 * @author doyeka
 */
public class GridEdge implements Edge<GridBlock, GridEdge> {
  private GridBlock src;
  private GridBlock dest;
  private double weight;

  /**
   * Constructs a GridEdge object.
   * @param src The source GridBlock of this GridEdge.
   * @param dest The destination GridBlock of this GridEdge.
   * @param weight The weight of the GridEdge.
   */
  public GridEdge(GridBlock src, GridBlock dest, double weight) {
    this.src = src;
    this.dest = dest;
    this.weight = weight;
  }

  @Override
  public double getWeight() {
    return weight;
  }

  @Override
  public void setWeight(double weight) {
    throw new UnsupportedOperationException();
  }

  @Override
  public GridBlock getSrc() {
    return src;
  }

  @Override
  public void setSrc(GridBlock src) {
    this.src = src;
  }

  @Override
  public GridBlock getDest() {
    return dest;
  }

  @Override
  public void setDest(GridBlock dest) {
    this.dest = dest;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }

    if (!(o instanceof GridEdge)) {
      return false;
    }

    GridEdge s = (GridEdge) o;

    return getSrc().equals(s.getSrc()) && getDest().equals(s.getDest())
        && Double.compare(getWeight(), s.getWeight()) == 0;
  }

  @Override
  public String toString() {
    return getSrc().toString() + " --" + getWeight()
        + "--> " + getDest().toString();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getSrc(), getDest());
  }
}
