package edu.brown.cs.stonefall.gamebean;

import edu.brown.cs.stonefall.game.Constants;
import edu.brown.cs.stonefall.game.Player;

/**
 * Attacker Bean object which is used to store and send information to the
 * frontend.
 *
 * @author Theodoros
 */
public class AttackerBean {
  private String playerId;

  private String id;
  private String colorhex;

  private boolean attackStatus;
  private int targetX;
  private int targetY;

  private int x;
  private int y;
  private MotionBean motionBean;

  private double hp;

  /**
   * Attacker bean constructor.
   *
   * @param attackerId
   *          id of attacker
   * @param player
   *          player that the attacker belongs to
   */
  public AttackerBean(String attackerId, Player player) {
    playerId = player.getId();

    id = attackerId;
    colorhex = player.getColorHex();

    attackStatus = player.getAttackers().get(attackerId).getAttackStatus();
    if (player.getAttackers().get(attackerId).getTarget().isPresent()) {
      targetX = player.getAttackers().get(attackerId).getTarget().get().getX();
      targetY = player.getAttackers().get(attackerId).getTarget().get().getY();
    }

    x = player.getAttackers().get(attackerId).getX();
    y = player.getAttackers().get(attackerId).getY();
    if (player.getAttackers().get(attackerId).inMotion()) {
      motionBean = new MotionBean(
          player.getAttackers().get(attackerId).getMotionRatio(),
          player.getAttackers().get(attackerId).getDirection(), true);
    } else {
      motionBean = new MotionBean(0.0,
          player.getAttackers().get(attackerId).getDirection(), false);
    }

    // attackStatus = player.getAttackers().get(attackerId).getTarget().isPresent()
    //     && player.validateTarget(
    //         player.getAttackers().get(attackerId).getTarget(),
    //         player.getAttackers().get(attackerId));

    hp = player.getAttackers().get(attackerId).getHealth();

  }

  /**
   * Gets player's id.
   *
   * @return playerId
   */
  public String getPlayerId() {
    return playerId;
  }

  /**
   * Gets attacker's id.
   *
   * @return id
   */
  public String getId() {
    return id;
  }

  /**
   * Gets attacker's color.
   *
   * @return color
   */
  public String getColorHex() {
    return colorhex;
  }

  /**
   * Gets attacker's x coordinate.
   *
   * @return x
   */
  public int getX() {
    return x;
  }

  /**
   * Gets attacker's y coordinate.
   *
   * @return x
   */
  public int getY() {
    return y;
  }

  /**
   * Gets attacker's direction.
   *
   * @return direction
   */
  public int getDirection() {
    return motionBean.getCurrentDirection().encode();
  }

  /**
   * Gets attacker target's x coordinate.
   *
   * @return target x
   */
  public int getTargetX() {
    return targetX;
  }

  /**
   * Gets attacker target's y coordinate.
   *
   * @return target y
   */
  public int getTargetY() {
    return targetY;
  }

  /**
   * Gets attacker motion ratio.
   *
   * @return ratio
   */
  public double getRatio() {
    return motionBean.getCurrentRatio();
  }

  /**
   * Gets whether the attacker is in miton.
   *
   * @return true if in motion, false otherwise
   */
  public boolean inMotion() {
    return motionBean.isInMotion();
  }

  /**
   * Gets attacker's attack status.
   *
   * @return attack status
   */
  public boolean attackStatus() {
    return attackStatus;
  }

  /**
   * Gets attacker's health.
   *
   * @return hp
   */
  public double getHp() {
    return hp;
  }

  /**
   * Gets attacker's maximum hp.
   *
   * @return maximum melee attacker hp
   */
  public double getMaxHp() {
    return Constants.MELEE_ATTACKER_HP;
  }
}
