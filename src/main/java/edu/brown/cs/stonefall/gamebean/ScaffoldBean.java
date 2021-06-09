package edu.brown.cs.stonefall.gamebean;

import edu.brown.cs.stonefall.game.Player;

/**
 * Scaffold Bean object which is used to store and send information to the
 * frontend.
 *
 * @author Theodoros
 */
public class ScaffoldBean {
  private String id;
  private int x;
  private int y;
  private double maxHp;
  private double hp;
  private String playerId;
  private String colorhex;

  /**
   * Scaffold bean constructor.
   *
   * @param scaffoldId
   *          scaffold's Id
   * @param player
   *          player that the scaffold belongs to
   */
  public ScaffoldBean(String scaffoldId, Player player) {
    playerId = player.getId();

    id = scaffoldId;
    colorhex = player.getColorHex();

    x = player.getScaffolds().get(scaffoldId).getX();
    y = player.getScaffolds().get(scaffoldId).getY();

    hp = player.getScaffolds().get(scaffoldId).getHealth();
    maxHp = player.getScaffolds().get(scaffoldId).getMaxHealth();
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
   * Gets scaffold's id.
   *
   * @return id
   */
  public String getId() {
    return id;
  }

  /**
   * Gets scaffold's color.
   *
   * @return color
   */
  public String getColorHex() {
    return colorhex;
  }

  /**
   * Gets scaffold's x coordinate.
   *
   * @return x coordinate
   */
  public int getX() {
    return x;
  }

  /**
   * Gets scaffold's y coordinate.
   *
   * @return y coordinate
   */
  public int getY() {
    return y;
  }

  /**
   * Gets scaffold's hp.
   *
   * @return hp
   */
  public double getHp() {
    return hp;
  }

  /**
   * Gets scaffold's maximum hp.
   *
   * @return maxHp
   */
  public double getMaxHp() {
    return maxHp;
  }
}
