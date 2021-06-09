package edu.brown.cs.stonefall.gamebean;

import edu.brown.cs.stonefall.game.Constants;
import edu.brown.cs.stonefall.game.Player;

/**
 * Base Bean object which is used to store and send information to the frontend.
 *
 * @author Theodoros
 */
public class BaseBean {
  private int x;
  private int y;
  private double hp;
  private String name;
  private String playerId;
  private String colorhex;

  /**
   * Base bean constructor.
   *
   * @param player
   *          player that the base belongs to
   */
  public BaseBean(Player player) {
    playerId = player.getId();
    colorhex = player.getColorHex();

    x = player.getBase().getX();
    y = player.getBase().getY();

    hp = player.getBase().getHealth();
    name = player.getName();
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
   * Gets base's color.
   *
   * @return color
   */
  public String getColorHex() {
    return colorhex;
  }

  /**
   * Gets base's x coordinate.
   *
   * @return x
   */
  public int getX() {
    return x;
  }

  /**
   * Gets base's y coordinate.
   *
   * @return y
   */
  public int getY() {
    return y;
  }

  /**
   * Gets base's health.
   *
   * @return hp
   */
  public double getHp() {
    return hp;
  }

  /**
   * Gets owner player's name.
   *
   * @return player name
   */
  public String getName() {
    return name;
  }

  /**
   * Gets base's maximum hp.
   *
   * @return maximum base hp
   */
  public double getMaxHp() {
    return Constants.BASE_HP;
  }

  // Overriding equals() to compare two Complex objects 
  @Override
  public boolean equals(Object o) { 
    BaseBean b = (BaseBean) o;
    return (x == b.getX() && y==b.getY());
  }

}
