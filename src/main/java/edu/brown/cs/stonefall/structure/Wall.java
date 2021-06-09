package edu.brown.cs.stonefall.structure;

import edu.brown.cs.stonefall.game.Constants;
import edu.brown.cs.stonefall.interfaces.Killable;
import edu.brown.cs.stonefall.map.Grid;
import edu.brown.cs.stonefall.map.GridBlock;
import edu.brown.cs.stonefall.pathing.Cartesian;

/**
 * Backend object for wall.
 *
 * @author Theodoros
 */
public class Wall implements Killable {

  private double hp;
  private GridBlock block;
  private boolean alreadyRewarded;

  /**
   * Constructor for a wall, instantiates it with its default characteristics in
   * and logically places it in the grid.
   *
   * FIELDS:
   *
   * hp - health of the wall
   *
   * block - block that the wall is located on
   *
   * alreadyRewarded = flag that makes sure that players are only rewarded once
   * for killing the wall
   *
   * @param startingBlock
   *          block that the wall is placed on in the grid
   */
  public Wall(GridBlock startingBlock) {
    hp = Constants.WALL_HP;
    Grid.getGridBlock(startingBlock.getX(), startingBlock.getY()).get()
        .populate(this);
    block = startingBlock;

    alreadyRewarded = false;
  }

  @Override
  public double getHealth() {
    return hp;
  }

  @Override
  public void setHealth(double newHp) {
    hp = newHp;
  }

  @Override
  public boolean isDead() {
    return hp <= 0;
  }

  @Override
  public GridBlock getBlock() {
    return block;
  }

  @Override
  public void setBlock(GridBlock toSet) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getX() {
    return block.getX();
  }

  @Override
  public int getY() {
    return block.getY();
  }

  @Override
  public double getDistance(Cartesian c) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getReward() {
    if (!alreadyRewarded) {
      alreadyRewarded = true;
      return Constants.WALL_REWARD;
    }
    return 0;
  }

}
