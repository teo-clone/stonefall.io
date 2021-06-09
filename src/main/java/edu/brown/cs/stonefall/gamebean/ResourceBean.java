package edu.brown.cs.stonefall.gamebean;

import edu.brown.cs.stonefall.game.Constants;
import edu.brown.cs.stonefall.structure.Resource;

/**
 * Resource Bean object which is used to store and send information to the
 * frontend.
 *
 * @author Theodoros
 */
public class ResourceBean {
  private int x;
  private int y;
  private double hp;

  /**
   * Resource bean constructor.
   *
   * @param resource
   *          resource which we make bean for
   */
  public ResourceBean(Resource resource) {
    this.x = resource.getX();
    this.y = resource.getY();
    this.hp = resource.getHealth();
  }

  /**
   * Gets resource's x coordinate.
   *
   * @return x
   */
  public int getX() {
    return x;
  }

  /**
   * Gets resource's y coordinate.
   *
   * @return y
   */
  public int getY() {
    return y;
  }

  /**
   * Gets resource's health.
   *
   * @return hp
   */
  public double getHp() {
    return hp;
  }

  /**
   * Gets resource's maximum hp.
   *
   * @return maxhp
   */
  public double getMaxHp() {
    return Constants.RESOURCE_HP;
  }
}
