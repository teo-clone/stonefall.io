package edu.brown.cs.stonefall.structure;

import edu.brown.cs.stonefall.game.Constants;
import edu.brown.cs.stonefall.interfaces.Killable;
import edu.brown.cs.stonefall.map.Grid;
import edu.brown.cs.stonefall.map.GridBlock;
import edu.brown.cs.stonefall.pathing.Cartesian;

/**
 * Object which represents backend Base object.
 *
 * @author Theodoros
 */
public class Base implements Killable {

  private double hp;
  private GridBlock block;

  private boolean alreadyRewarded;

  /**
   * Constructor for a base, instantiates it with its default characteristics in
   * and logically places it in the grid.
   *
   * FIELDS:
   *
   * hp - health of the base
   *
   * block - block that the base is located on
   *
   * alreadyRewarded = flag that makes sure that players are only rewarded once
   * for killing the base
   *
   * @param startingBlock
   *          block that the base is placed on in the grid
   */
  public Base(GridBlock startingBlock) {
    this.hp = Constants.BASE_HP;

    Grid.getGridBlock(startingBlock.getX(), startingBlock.getY()).get()
        .populate(this);
    this.block = startingBlock;

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
    // making sure that we only reward once for a base kill
    if (!alreadyRewarded) {
      alreadyRewarded = true;
      return Constants.BASE_REWARD;
    }
    return 0;
  }

}
