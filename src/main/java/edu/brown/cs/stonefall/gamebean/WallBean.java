package edu.brown.cs.stonefall.gamebean;

import edu.brown.cs.stonefall.game.Constants;
import edu.brown.cs.stonefall.game.Player;

/**
 * Wall Bean object which is used to store and send information to the frontend.
 *
 * @author Theodoros
 */
public class WallBean {

  private String id;
  private int x;
  private int y;
  private double hp;
  private String playerId;
  private String colorhex;

  /**
   * Wall bean constructor.
   *
   * @param wallId
   *          wall's Id
   * @param player
   *          player that the mine belongs to
   */
  public WallBean(String wallId, Player player) {
    playerId = player.getId();

    id = wallId;
    colorhex = player.getColorHex();

    x = player.getWalls().get(wallId).getX();
    y = player.getWalls().get(wallId).getY();

    hp = player.getWalls().get(wallId).getHealth();
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
   * Gets wall id.
   *
   * @return id
   */
  public String getId() {
    return id;
  }

  /**
   * Gets wall's color.
   *
   * @return color
   */
  public String getColorHex() {
    return colorhex;
  }

  /**
   * Gets wall's x coordinate.
   *
   * @return x coordinate
   */
  public int getX() {
    return x;
  }

  /**
   * Gets wall's y coordinate.
   *
   * @return y coordinate
   */
  public int getY() {
    return y;
  }

  /**
   * Gets wall's x health.
   *
   * @return hp
   */
  public double getHp() {
    return hp;
  }

  /**
   * Gets wall's maximum health.
   *
   * @return maxHp
   */
  public double getMaxHp() {
    return Constants.WALL_HP;
  }
}
