package edu.brown.cs.stonefall.interfaces;

import edu.brown.cs.stonefall.map.GridBlock;
import edu.brown.cs.stonefall.pathing.Direction;

/**
 * An interface for movable Entities.
 */
public interface Movable extends Entity {

  /**
   * Returns if this Movable object is in motion.
   * @return A boolean indicating if this Movable object is in motion.
   */
  boolean inMotion();

  /**
   * Returns the ratio of this Movable object between two GridBlocks.
   * @return A double indicating the ratio of this Movable object between
   * two GridBlocks.
   */
  double getMotionRatio();

  /**
   * Returns the direction of this Movable object.
   * @return A Direction indicating the direction of this Movable object.
   */
  Direction getDirection();

  /**
   * Starts the motion of this Movable object to an empty GridBlock.
   * @param toReach The GridBlock for this Movable object to move to.
   */
  void startMotion(GridBlock toReach);

  /**
   * Updates the motion of this Movable object.
   */
  void updateMotion();

  /**
   * Sets the direction of this Movable object.
   * @param d The Direction to set this Movable object to.
   */
  void setDirection(Direction d);

  /**
   * Starts a chasing motion for this Movable object to chase the specified
   * target.
   * @param target A Killable target for this Movable object to chase.
   */
  void startChase(Attacker target);

  /**
   * Stops the chasing Motion for this Movable object, and verifies if this
   * Movable object should chase again.
   * @param dest The current GridBlock of this Movable object.
   * @param targetBlock The current GridBlock of this Movable object's target.
   */
  void stopChase(GridBlock dest, GridBlock targetBlock);

  void startCharge(Killable target);
}
