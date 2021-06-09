package edu.brown.cs.stonefall.gamebean;

import edu.brown.cs.stonefall.pathing.Direction;

/**
 * Motion Bean object which is used to store and send information to the
 * frontend.
 *
 * @author Theodoros
 */
public class MotionBean {
  private double currentRatio;
  private Direction currentDirection;
  private boolean inMotion;

  /**
   * Motion Bean object constructor.
   *
   * @param ratio
   *          ratio of movement
   * @param direction
   *          direction of movement
   * @param inMotion
   *          whether unit is in motion
   */
  public MotionBean(double ratio, Direction direction, boolean inMotion) {
    currentRatio = ratio;
    currentDirection = direction;
    this.inMotion = inMotion;
  }

  /**
   * Gets motion ratio.
   *
   * @return ratio
   */
  public double getCurrentRatio() {
    return currentRatio;
  }

  /**
   * Gets motion direction.
   *
   * @return direction
   */
  public Direction getCurrentDirection() {
    return currentDirection;
  }

  /**
   * Gets whether unit is in motion.
   *
   * @return true if in motion, false otherwise
   */
  public boolean isInMotion() {
    return inMotion;
  }
}
