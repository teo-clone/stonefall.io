package edu.brown.cs.stonefall.entity;

import edu.brown.cs.stonefall.game.Constants;
import edu.brown.cs.stonefall.interfaces.Attacker;
import edu.brown.cs.stonefall.interfaces.Killable;
import edu.brown.cs.stonefall.interfaces.Motion;
import edu.brown.cs.stonefall.interfaces.Movable;
import edu.brown.cs.stonefall.map.Grid;
import edu.brown.cs.stonefall.map.GridBlock;
import edu.brown.cs.stonefall.map.GridEdge;
import edu.brown.cs.stonefall.motion.MovableMotion;
import edu.brown.cs.stonefall.pathing.Cartesian;
import edu.brown.cs.stonefall.pathing.Direction;

import java.util.List;
import java.util.Optional;

/**
 * An implementation of the Attacker interface with longer ranged attacks.
 */
public class RangedAttacker implements Attacker, Movable {
  private GridBlock block;
  private double hp;
  private Motion eMotion;
  private List<GridEdge> path;
  private Optional<Killable> target;
  private boolean attackStatus;
  private boolean alreadyRewarded;

  /**
   * Constructs a RangedAttacker on the given GridBlock.
   * Fields:
   * block, the location of this RangedAttacker.
   * hp, the current hit points (health) of this RangedAttacker.
   * eMotion, the Motion object for this RangedAttacker.
   * path, the List of GridEdges this RangedAttacker trying to take.
   * target, the Killable this RangedAttacker is trying to attack.
   * attackStatus, a boolean representing if this Attacker is currently
   * attacking.
   * alreadyRewarded, a boolean representing if the reward for killing this
   * RangedAttacker has already been claimed.
   *
   * @param startingBlock The block for the RangedAttacker to be constructed on.
   */
  public RangedAttacker(GridBlock startingBlock) {
    // set location to base location.
    block = startingBlock;
    Grid.getGridBlock(startingBlock.getX(),
        startingBlock.getY()).get().populate(this);
    hp = Constants.RANGED_ATTACKER_HP;
    eMotion = new MovableMotion(this);
    attackStatus = false;
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
  public void setTarget(Optional<Killable> newTarget) {
    target = newTarget;
  }

  @Override
  public void attack() {
    target.get()
        .setHealth(target.get().getHealth() - Constants.RANGED_ATTACKER_DAMAGE);
  }

  @Override
  public Optional<Killable> getTarget() {
    return target;
  }

  @Override
  public void startMotion(GridBlock toReach) {
    // TODO: Implement later.
  }

  @Override
  public void stopChase(GridBlock dest, GridBlock targetBlock) { }

  @Override
  public void startCharge(Killable newTarget) {
    // TODO: Implement later.
  }

  @Override
  public void setAttackStatus(boolean status) {
    attackStatus = status;
  }

  @Override
  public boolean getAttackStatus() {
    return attackStatus;
  }

  @Override
  public boolean inMotion() {
    return eMotion.inMotion();
  }

  @Override
  public void updateMotion() {
    eMotion.updateMotion();
  }

  @Override
  public double getMotionRatio() {
    return eMotion.getCurrentRatio();
  }

  @Override
  public Direction getDirection() {
    return eMotion.getCurrentDirection();
  }

  @Override
  public void setDirection(Direction d) {
    eMotion.setCurrentDirection(d);
  }

  @Override
  public void startChase(Attacker newTarget) {
    // TODO: Implement later.
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

  @Override
  public int getReward() {
    if (!alreadyRewarded) {
      alreadyRewarded = true;
      return Constants.RANGED_ATTACKER_REWARD;
    }
    return 0;
  }
}
