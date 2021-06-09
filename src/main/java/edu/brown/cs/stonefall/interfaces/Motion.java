package edu.brown.cs.stonefall.interfaces;

import edu.brown.cs.stonefall.pathing.Direction;

/**
 * An interface for motion objects, which represent the Motion of their parents.
 */
public interface Motion {
  /**
   * Returns the current ratio of this Motion object between two GridBlocks.
   * @return A double indicating the current ratio of this Motion object
   * between two GridBlocks.
   */
  double getCurrentRatio();

  /**
   * Returns the current direction of this Motion object.
   * @return A Direction indicating the current direction of this Motion object.
   */
  Direction getCurrentDirection();

  /**
   * Sets the current direction of this Motion object.
   * @param d The Direction to set this Motion object to.
   */
  void setCurrentDirection(Direction d);

  /**
   * Returns if this Motion object is in motion.
   * @return A boolean indicating if this Motion object is in motion.
   */
  boolean inMotion();

  /**
   * Updates the motion of this Motion object.
   */
  void updateMotion();
}
