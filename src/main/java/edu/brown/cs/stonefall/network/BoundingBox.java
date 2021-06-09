package edu.brown.cs.stonefall.network;

import edu.brown.cs.stonefall.interfaces.Entity;

/**
 * Bounding box object.
 *
 * @author Theodoros
 */
public class BoundingBox {
  private int x1;
  private int x2;
  private int y1;
  private int y2;

  /**
   * Constructor for bounding box.
   *
   * @param x1
   *          top left x coordinate
   * @param y1
   *          top left y coordinate
   * @param x2
   *          bottom right x coordinate
   * @param y2
   *          bottom right y coordinate
   */
  public BoundingBox(int x1, int y1, int x2, int y2) {
    this.x1 = x1;
    this.y1 = y1;
    this.x2 = x2;
    this.y2 = y2;
  }

  /**
   * Return top left x coordinate.
   *
   * @return x1
   */
  public int getX1() {
    return x1;
  }

  /**
   * Return top left y coordinate.
   *
   * @return y1
   */
  public int getY1() {
    return y1;
  }

  /**
   * Return bottom right x coordinate.
   *
   * @return x2
   */
  public int getX2() {
    return x2;
  }

  /**
   * Return bottom right y coordinate.
   *
   * @return y2
   */
  public int getY2() {
    return y2;
  }

  /**
   * Returns whether the bounding box contains the passed in entity.
   *
   * @param ent
   *          entity to check whether it is in box
   * @return true if ent position is in box, false otherwise
   */
  public boolean contains(Entity ent) {
    int entX = ent.getX();
    int entY = ent.getY();
    return x1 >= entX && x2 <= entX && y1 >= entY && y2 <= entY;
  }

  /**
   * Returns whether the bounding box contains the passed in coordinates.
   *
   * @param coordX
   *          x coordinate
   * @param coordY
   *          y coordinate
   * @return true if coordinate is in box, false otherwise
   */
  public boolean contains(int coordX, int coordY) {
    return x1 <= coordX && x2 >= coordX && y1 <= coordY && y2 >= coordY;
  }
}
