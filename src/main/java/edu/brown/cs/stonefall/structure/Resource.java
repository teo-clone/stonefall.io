package edu.brown.cs.stonefall.structure;

import edu.brown.cs.stonefall.game.Constants;
import edu.brown.cs.stonefall.interfaces.Killable;
import edu.brown.cs.stonefall.map.Grid;
import edu.brown.cs.stonefall.map.GridBlock;
import edu.brown.cs.stonefall.pathing.Cartesian;

/**
 * Object which represents backend Resource object.
 *
 * @author Theodoross
 */
public class Resource implements Killable {

  private double hp;
  private GridBlock block;

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
   * @param startingBlock
   *          block that the mine is placed on on construction
   */
  public Resource(GridBlock startingBlock) {
    this.hp = Constants.RESOURCE_HP;

    Grid.getGridBlock(startingBlock.getX(), startingBlock.getY()).get()
        .populate(this);
    this.block = startingBlock;
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
    return 0;
  }

}
