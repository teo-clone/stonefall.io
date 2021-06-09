package edu.brown.cs.stonefall.interfaces;

import java.util.Optional;

/**
 * An interface for object that can Attack other objects.
 */
public interface Attacker extends Killable, Movable {

  /**
   * Attacks this Attacker's target.
   */
  void attack();

  /**
   * Returns the target of this Attacker.
   * @return An Optional containing a Killable representing this Attacker's
   * target.
   */
  Optional<Killable> getTarget();

  /**
   * Sets the target of this Attacker.
   * @param newTarget An Optional containing a Killable representing this
   *                  Attacker's target.
   */
  void setTarget(Optional<Killable> newTarget);


  /**
   * Sets the status of this Attacker.
   * @param status A boolean representing if this Attacker is attacking an
   *               Entity or not.
   */
  void setAttackStatus(boolean status);

  /**
   * Returns the status of this Attacker.
   * @return A boolean representing if this Attacker is attacking an
   * Entity or not.
   */
  boolean getAttackStatus();
}
