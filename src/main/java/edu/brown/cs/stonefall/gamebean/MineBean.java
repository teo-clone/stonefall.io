package edu.brown.cs.stonefall.gamebean;

import edu.brown.cs.stonefall.game.Constants;
import edu.brown.cs.stonefall.game.Player;

/**
 * Mine Bean object which is used to store and send information to the frontend.
 *
 * @author Theodoros
 */
public class MineBean {
  private String id;
  private int x;
  private int y;
  private double hp;
  private String playerId;
  private String colorhex;

  /**
   * Mine bean constructor.
   *
   * @param mineId
   *          mine's Id
   * @param player
   *          player that the mine belongs to
   */
  public MineBean(String mineId, Player player) {
    playerId = player.getId();

    id = mineId;
    colorhex = player.getColorHex();

    x = player.getMines().get(mineId).getX();
    y = player.getMines().get(mineId).getY();

    hp = player.getMines().get(mineId).getHealth();
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
   * Gets mine's id.
   *
   * @return id
   */
  public String getId() {
    return id;
  }

  /**
   * Gets mine's color.
   *
   * @return color
   */
  public String getColorHex() {
    return colorhex;
  }

  /**
   * Gets mine's x coordinate.
   *
   * @return x
   */
  public int getX() {
    return x;
  }

  /**
   * Gets mine's y coordinate.
   *
   * @return y
   */
  public int getY() {
    return y;
  }

  /**
   * Gets mine's health.
   *
   * @return hp
   */
  public double getHp() {
    return hp;
  }

  /**
   * Gets mine's maximum hp.
   *
   * @return maximum mine hp
   */
  public double getMaxHp() {
    return Constants.MINE_HP;
  }

}
