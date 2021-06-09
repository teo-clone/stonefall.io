package edu.brown.cs.stonefall.gamebean;

import edu.brown.cs.stonefall.game.Constants;
import edu.brown.cs.stonefall.game.Player;
import edu.brown.cs.stonefall.pathing.Direction;
import edu.brown.cs.stonefall.interfaces.Killable;
import edu.brown.cs.stonefall.interfaces.Attacker;
import java.util.Optional;


/**
 * Turret Bean object which is used to store and send information to the
 * frontend.
 *
 * @author Theodoros
 */
public class TurretBean {
  private String id;
  private int x;
  private int y;
  private double hp;
  private String playerId;
  private String colorhex;
  private boolean attackStatus;
  private int targetX;
  private int targetY;
  private Direction targetDirection;
  private double targetRatio;

  /**
   * Turret bean constructor.
   *
   * @param turretId
   *          turret's Id
   * @param player
   *          player that the turret belongs to
   */
  public TurretBean(String turretId, Player player) {
    playerId = player.getId();

    id = turretId;
    colorhex = player.getColorHex();

    x = player.getTurrets().get(turretId).getX();
    y = player.getTurrets().get(turretId).getY();

    hp = player.getTurrets().get(turretId).getHealth();

    attackStatus = player.getTurrets().get(turretId).getAttackStatus();

    if (attackStatus) {
      targetX = player.getTurrets().get(turretId).getTarget().get().getX();
      targetY = player.getTurrets().get(turretId).getTarget().get().getY();
      Killable target = player.getTurrets().get(turretId).getTarget().get();
      if(target instanceof Attacker){
        targetDirection = ((Attacker) target).getDirection();
        targetRatio = ((Attacker) target).getMotionRatio();
      } else {
        targetDirection = Direction.N;
        targetRatio = 0;
      }

    }
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
   * Gets turret's id.
   *
   * @return id
   */
  public String getId() {
    return id;
  }

  /**
   * Gets turret's color.
   *
   * @return color
   */
  public String getColorHex() {
    return colorhex;
  }

  /**
   * Gets turret's x coordinate.
   *
   * @return x coordinate
   */
  public int getX() {
    return x;
  }

  /**
   * Gets turret's y coordinate.
   *
   * @return y coordinate
   */
  public int getY() {
    return y;
  }

  /**
   * Gets turret's health.
   *
   * @return health
   */
  public double getHp() {
    return hp;
  }

  /**
   * Gets turret's maximum hp.
   *
   * @return health
   */
  public double getMaxHp() {
    return Constants.TURRET_HP;
  }

  /**
   * Gets turret's attack status.
   *
   * @return attack status
   */
  public boolean getAttackStatus() {
    return attackStatus;
  }

  /**
   * Gets turret's target's x coordinate.
   *
   * @return target x
   */
  public int getTargetX() {
    return targetX;
  }

  /**
   * Gets turret's target's y coordinate.
   *
   * @return target y
   */
  public int getTargetY() {
    return targetY;
  }

  /**
   * Gets turret's target direction.
   *
   * @return target direction
   */
  public Direction getTargetDirection() {
    return targetDirection;
  }

  /**
   * Gets turret's target ratio.
   *
   * @return target ratio
   */
  public double getTargetRatio() {
    return targetRatio;
  }

}
