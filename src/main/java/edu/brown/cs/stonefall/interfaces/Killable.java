package edu.brown.cs.stonefall.interfaces;

/**
 * An interface for any Killable object in Game.
 */
public interface Killable extends Entity {

  /**
   * Returns the health of this Killable object.
   * @return A double indicating the health of this Killable object.
   */
  double getHealth();

  /**
   * Sets the health of this Killable object.
   * @param hp The health to set this Killable object to.
   */
  void setHealth(double hp);

  /**
   * Returns if this Killable is dead.
   * @return A boolean indicating if this Killable is dead.
   */
  boolean isDead();

  /**
   * Returns the reward for killing this Killable object.
   * @return An int reward for killing this Killable object.
   */
  int getReward();
}
