package edu.brown.cs.stonefall.gamebean;

import edu.brown.cs.stonefall.game.Player;

/**
 * Player Stats Bean class. Used for storing and sending information to the
 * frontend.
 *
 * @author Theodoros
 *
 */
public class PlayerStatsBean {

  private double baseHealth;
  private double resourceCount;
  private int mineCount;
  private int attackerCount;
  private String name;
  private String id;

  private Player player;

  /**
   * Constructor for player stats bean object.
   *
   * @param player
   *          player whose stats we want to bean
   */
  public PlayerStatsBean(Player player) {
    this.player = player;

    baseHealth = player.getBase().getHealth();
    resourceCount = player.getResourceCount();
    mineCount = player.getMines().size();
    attackerCount = player.getAttackers().size();
    name = player.getName();
    id = player.getId();
  }

  /**
   * Updates player stats.
   */
  public void update() {
    baseHealth = player.getBase().getHealth();
    resourceCount = player.getResourceCount();
    mineCount = player.getMines().size();
    attackerCount = player.getAttackers().size();
    name = player.getName();
    id = player.getId();
  }

  /**
   * Gets player base health.
   *
   * @return base health
   */
  public double getBaseHealth() {
    return baseHealth;
  }

  /**
   * Gets player resource count.
   *
   * @return resource count
   */
  public double getResourceCount() {
    return resourceCount;
  }

  /**
   * Gets player mine count.
   *
   * @return mine count
   */
  public int getMineCount() {
    return mineCount;
  }

  /**
   * Gets attacker count.
   *
   * @return attacker count
   */
  public int getAttackerCount() {
    return attackerCount;
  }

  /**
   * Gets player name.
   *
   * @return name
   */
  public String getName() {
    return name;
  }

  /**
   * Gets player id.
   *
   * @return id
   */
  public String getId() {
    return id;
  }

}
