package edu.brown.cs.stonefall.map;

import edu.brown.cs.stonefall.interfaces.Attacker;
import edu.brown.cs.stonefall.interfaces.Entity;
import edu.brown.cs.stonefall.interfaces.Killable;
import edu.brown.cs.stonefall.pathing.Cartesian;
import edu.brown.cs.stonefall.pathing.Vertex;
import edu.brown.cs.stonefall.structure.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A class representing a coordinate in the game's grid system.
 *
 * @author doyeka
 */
public class GridBlock implements Vertex<GridBlock, GridEdge>, Cartesian {
  private int x;
  private int y;
  private List<GridEdge> edges;
  private Entity entity;

  /**
   * Constructs a GridBlock object.
   *
   * @param x
   *          The x-coordinate of this GridBlock.
   * @param y
   *          The y-coordinate of this GridBlock.
   */
  public GridBlock(int x, int y) {
    this.x = x;
    this.y = y;
    entity = null;
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

  @Override
  public List<GridEdge> getEdges() {
    List<GridEdge> validEdges = new ArrayList<>();
    for (GridEdge e : edges) {
      if (!e.getDest().isFull()) {
        validEdges.add(e);
      }
    }
    return validEdges;
  }

  /**
   * Sets the edges of this GridBlock.
   *
   * @param edges
   *          The edges to set of this GridBlock.
   */
  void setEdges(List<GridEdge> edges) {
    this.edges = edges;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }

    if (!(o instanceof GridBlock)) {
      return false;
    }

    GridBlock s = (GridBlock) o;

    return getX() == s.getX() && getY() == s.getY();
  }

  @Override
  public String toString() {
    return "(" + getX() + ", " + getY() + ")";
  }

  @Override
  public int hashCode() {
    return Objects.hash(getX(), getY());
  }

  /**
   * Populates this GridBlock with the given Entity.
   * @param ent The Entity to populate this GridBlock with.
   */
  public void populate(Entity ent) {
    entity = ent;
  }

  /**
   * Depopulates this GridBlock.
   */
  public void depopulate() {
    entity = null;
  }

  /**
   * Returns the Entity on this GridBlock.
   * @return The Entity on this GridBlock.
   */
  public Entity getEntity() {
    return entity;
  }

  /**
   * Returns a boolean indicating if this GridBlock is full.
   * @return A boolean indicating if this GridBlock is full.
   */
  public boolean isFull() {
    return entity != null;
  }

  /**
   * Returns a boolean indicating if this GridBlock is attackable.
   * @return A boolean indicating if this GridBlock is attackable.
   */
  public boolean isChargeable(int range) {
    return isFull()
        && entity instanceof Killable
        && !(entity instanceof Attacker)
        && !((Killable) entity).isDead()
        && isSomeFreeBlockInRadius(range)
        && !(entity instanceof Resource);
  }

  public boolean isSomeFreeBlockInRadius(int n) {
    if (n == 1) {
      return !getEdges().isEmpty();
    }
    // TODO: Expand for Attackers with ranges greater than 1.
    return false;
  }

  public boolean isReachable(int range) {
    return isFull()
        && entity instanceof Attacker
        && !((Attacker) entity).isDead()
        && isSomeFreeBlockInRadius(range);
  }
}
