package edu.brown.cs.stonefall.structure;

import java.util.Optional;

import edu.brown.cs.stonefall.game.Constants;
import edu.brown.cs.stonefall.game.Player;
import edu.brown.cs.stonefall.interfaces.Killable;
import edu.brown.cs.stonefall.map.Grid;
import edu.brown.cs.stonefall.map.GridBlock;
import edu.brown.cs.stonefall.pathing.Cartesian;

/**
 * Scaffold object. One common object which transforms into a wall, turret, or
 * mine.
 *
 * @author Theodoros
 */
public class Scaffold implements Killable {
  private Player parent;
  private double hp;
  private double maxHp;
  private String id;
  private int scaffoldType;
  private GridBlock block;

  /**
   * Constructor for a mine, instantiates it with its default characteristics in
   * and logically places it in the grid.
   *
   * @param startingBlock
   *          block that the scaffold is placed on
   * @param player
   *          player that the scaffold belongs to
   * @param scaffoldVal
   *          stores what the scaffold will transform to when it is ready
   *          (turret, wall, or mine)
   * @param id
   *          id of the scaffold
   */
  public Scaffold(GridBlock startingBlock, Player player, int scaffoldVal,
      String id) {
    this.parent = player;
    this.id = id;

    this.scaffoldType = scaffoldVal;

    Grid.getGridBlock(startingBlock.getX(), startingBlock.getY()).get()
        .populate(this);
    this.block = startingBlock;

    this.hp = 0;
    if (scaffoldType == Constants.OBJECT_TYPE.MINE.ordinal()) {
      maxHp = 2 * Constants.MINE_HP;
    } else if (scaffoldType == Constants.OBJECT_TYPE.TURRET.ordinal()) {
      maxHp = 2 * Constants.TURRET_HP;
    } else if (scaffoldType == Constants.OBJECT_TYPE.WALL.ordinal()) {
      maxHp = 2 * Constants.WALL_HP;
    }
  }

  /**
   * Checks whether the health of the scaffold has filled up already, and if it
   * has then it calls on the player to upgrade it to a structure.
   */
  public void update() {
    if (hp >= 0) {
      hp += Constants.SCAFFOLD_REGENERATION_RATE;
      if (hp >= maxHp) {
        parent.upgradeScaffold(this);
      }
    }
  }

  @Override
  public double getHealth() {
    return hp;
  }

  /**
   * Returns the maximum health of the scaffold.
   *
   * @return maxHp
   */
  public double getMaxHealth() {
    return maxHp;
  }

  @Override
  public void setHealth(double newHp) {
    hp = newHp;
  }

  @Override
  public boolean isDead() {
    return hp < 0;
  }

  @Override
  public int getReward() {
    return 0;
  }

  @Override
  public GridBlock getBlock() {
    return block;
  }

  @Override
  public void setBlock(GridBlock toSet) {
    Optional<GridBlock> b = Grid.getGridBlock(toSet.getX(), toSet.getY());
    if (b.isPresent()) {
      Grid.getGridBlock(block.getX(), block.getY()).get().depopulate();
      b.get().populate(this);

      block = toSet;
    }
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

  /**
   * Returns the scaffold type, 0 for WALL, 1 for TURRET, 3 for MINE.
   *
   * @return scaffoldType
   */
  public int getScaffoldType() {
    return scaffoldType;
  }

  /**
   * Returns the id of the scaffold. Will be of the form /f/...
   *
   * @return id
   */
  public String getId() {
    return id;
  }
}
