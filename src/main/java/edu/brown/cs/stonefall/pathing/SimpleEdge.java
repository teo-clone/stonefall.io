package edu.brown.cs.stonefall.pathing;

/**
 * A SimpleEdge class for use in testing.
 */
public class SimpleEdge implements Edge<SimpleVertex, SimpleEdge> {

  private SimpleVertex src;
  private SimpleVertex dest;
  private double weight;

  /**
   * Constructs a SimpleEdge for testing.
   * @param src The source SimpleVertex of this SimpleEdge.
   * @param dest The destination SimpleVertex of this SimpleEdge.
   * @param weight The weight of this SimpleEdge as a double.
   */
  public SimpleEdge(SimpleVertex src, SimpleVertex dest, double weight) {
    this.src = src;
    this.dest = dest;
    this.weight = weight;
    this.src.addEdge(this);
  }

  @Override
  public double getWeight() {
    return this.weight;
  }

  @Override
  public void setWeight(double weight) {
    this.weight = weight;
  }

  @Override
  public SimpleVertex getSrc() {
    return src;
  }

  @Override
  public void setSrc(SimpleVertex src) {
    this.src = src;
  }

  @Override
  public SimpleVertex getDest() {
    return dest;
  }

  @Override
  public void setDest(SimpleVertex dest) {
    this.dest = dest;
  }

  @Override
  public String toString() {
    return src.toString() + "->" + dest.toString();
  }
}
