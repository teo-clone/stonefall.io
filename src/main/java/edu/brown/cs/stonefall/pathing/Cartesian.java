package edu.brown.cs.stonefall.pathing;

/**
 * An interface allowing objects access to cartesian points and relations.
 */
public interface Cartesian {
  /**
   * Retrieves the x-coordinate of the object implementing this
   * interface.
   *
   * @return The value of this object's x-coordinate.
   */
  int getX();

  /**
   * Retrieves the y-coordinate of the object implementing this
   * interface.
   *
   * @return The value of this object's y-coordinate.
   */
  int getY();

  /**
   * A method to calculate the 2 dimensional distance between two cartesian
   * points.
   * @param c The cartesian point to find the distance to, from the Cartesian
   *           object that calls this method.
   * @return The distance between the calling Cartesian object and c.
   */
  double getDistance(Cartesian c);
}
