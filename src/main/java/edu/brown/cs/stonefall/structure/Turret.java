package edu.brown.cs.stonefall.structure;

import java.util.Optional;

import edu.brown.cs.stonefall.game.Constants;
import edu.brown.cs.stonefall.interfaces.Attacker;
import edu.brown.cs.stonefall.interfaces.Killable;
import edu.brown.cs.stonefall.map.Grid;
import edu.brown.cs.stonefall.map.GridBlock;
import edu.brown.cs.stonefall.pathing.Cartesian;

/**
 * Backend Turret object.
 *
 * @author Theodoros
 */
public class Turret implements Killable {

  private double hp;
  private GridBlock block;
  private boolean alreadyRewarded;
  private boolean attackStatus;
  private Optional<Killable> target;

  /**
   * Constructor for a turret, instantiates it with its default characteristics
   * in and logically places it in the grid.
   *
   * FIELDS:
   *
   * hp - health of the turret
   *
   * block - block that the turret is located on
   *
   * attackStatus - keeps track of whether the turret is currently attacking or
   * not
   *
   * target - keeps track of the turret's target when it is attacking
   *
   * alreadyRewarded = flag that makes sure that players are only rewarded once
   * for killing the turret
   *
   * @param startingBlock
   *          block that the turret is placed on in the grid
   */
  public Turret(GridBlock startingBlock) {
    hp = Constants.TURRET_HP;
    Grid.getGridBlock(startingBlock.getX(), startingBlock.getY()).get()
        .populate(this);
    block = startingBlock;

    alreadyRewarded = false;

    attackStatus = false;

    target = Optional.empty();
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

  /**
   * Attack method for turrets. Decreases target's health by the turret damage.
   *
   * @param killable
   *          killable that the turret targets
   */
  public void attack(Killable killable) {
    killable.setHealth(killable.getHealth() - Constants.TURRET_DAMAGE);
  }

  /**
   * Gets the target of the turret.
   *
   * @return target
   */
  public Optional<Killable> getTarget() {
    return target;
  }

  /**
   * Sets the target of the turret.
   *
   * @param newTarget
   *          new target of turret
   */
  public void setTarget(Optional<Killable> newTarget) {
    target = newTarget;
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
      return Constants.TURRET_REWARD;
    }
    return 0;
  }

  /**
   * Sets the attack status of the turret.
   *
   * @param newStatus
   *          new status of attacking mode of turret
   */
  public void setAttackStatus(boolean newStatus) {
    attackStatus = newStatus;
  }

  /**
   * Gets the attack status of the turret.
   *
   * @return true if it is attacking, false otherwise
   */
  public boolean getAttackStatus() {
    return attackStatus;
  }

}
