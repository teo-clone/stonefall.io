package edu.brown.cs.stonefall.structure;

import edu.brown.cs.stonefall.game.Constants;
import edu.brown.cs.stonefall.interfaces.Killable;
import edu.brown.cs.stonefall.map.Grid;
import edu.brown.cs.stonefall.map.GridBlock;
import edu.brown.cs.stonefall.pathing.Cartesian;

/**
 * Object which represents backend Mine object.
 *
 * @author Theodoross
 */
public class Mine implements Killable {

  private double hp;
  private GridBlock block;

  private boolean alreadyRewarded;

  /**
   * Constructor for a mine, instantiates it with its default characteristics in
   * and logically places it in the grid.
   *
   * FIELDS:
   *
   * hp - health of the mine
   *
   * block - block that the mine is located on
   *
   * alreadyRewarded = flag that makes sure that players are only rewarded once
   * for killing the mine
   *
   * @param startingBlock
   *          block that the mine is placed on on construction
   */
  public Mine(GridBlock startingBlock) {
    this.hp = Constants.MINE_HP;

    Grid.getGridBlock(startingBlock.getX(), startingBlock.getY()).get()
        .populate(this);
    this.block = startingBlock;

    alreadyRewarded = false;
  }

  @Override
  public double getHealth() {
    return this.hp;
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

  /**
   * Method for collecting resource, decreases resource health by
   * MINE_COLLECT_RATIO.
   *
   * @param res
   *          resource to collect
   */
  public void collect(Resource res) {
    res.setHealth(res.getHealth() - Constants.MINE_COLLECT_RATIO);
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
      return Constants.MINE_REWARD;
    }
    return 0;
  }

}
