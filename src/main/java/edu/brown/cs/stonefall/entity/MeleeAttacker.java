package edu.brown.cs.stonefall.entity;

import edu.brown.cs.stonefall.custom_exceptions.UnreachableVertexException;
import edu.brown.cs.stonefall.game.Constants;
import edu.brown.cs.stonefall.interfaces.Attacker;
import edu.brown.cs.stonefall.interfaces.Killable;
import edu.brown.cs.stonefall.interfaces.Motion;
import edu.brown.cs.stonefall.interfaces.Movable;
import edu.brown.cs.stonefall.map.Grid;
import edu.brown.cs.stonefall.map.GridBlock;
import edu.brown.cs.stonefall.map.GridEdge;
import edu.brown.cs.stonefall.motion.MeleeChargeMotion;
import edu.brown.cs.stonefall.motion.MeleeChaseMotion;
import edu.brown.cs.stonefall.motion.MovableMotion;
import edu.brown.cs.stonefall.pathing.AStar;
import edu.brown.cs.stonefall.pathing.Cartesian;
import edu.brown.cs.stonefall.pathing.Direction;
import edu.brown.cs.stonefall.structure.Scaffold;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * An implementation of the Attacker interface with close range attacks.
 */
public class MeleeAttacker implements Attacker, Movable {
  private GridBlock block;
  private double hp;
  private Motion eMotion;
  private List<GridEdge> path;
  private Optional<Killable> target;
  private boolean attackStatus;
  private boolean alreadyRewarded;

  /**
   * Constructs a MeleeAttacker on the given GridBlock. Fields: block, the
   * location of this MeleeAttacker. hp, the current hit points (health) of this
   * MeleeAttacker. eMotion, the Motion object for this MeleeAttacker. path, the
   * List of GridEdges this MeleeAttacker trying to take. target, the Killable
   * this MeleeAttacker is trying to attack. attackStatus, a boolean
   * representing if this Attacker is currently attacking. alreadyRewarded, a
   * boolean representing if the reward for killing this MeleeAttacker has
   * already been claimed.
   *
   * @param startingBlock
   *          The block for the RangedAttacker to be constructed on.
   */
  public MeleeAttacker(GridBlock startingBlock) {
    block = startingBlock;
    Grid.getGridBlock(startingBlock.getX(), startingBlock.getY()).get()
        .populate(this);
    hp = Constants.MELEE_ATTACKER_HP;
    eMotion = new MovableMotion(this);
    target = Optional.empty();
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
  public Optional<Killable> getTarget() {
    return target;
  }

  @Override
  public void setTarget(Optional<Killable> newTarget) {
    target = newTarget;
  }

  @Override
  public void stopChase(GridBlock dest, GridBlock targetBlock) {
    /*
     * We'll attempt to attack the Entity on the target block if it is in range
     * of this Attacker.
     */
    if (Grid.isWithinNBlocks(Constants.MELEE_ATTACKER_RANGE, dest,
        targetBlock)) {
      setDirection(Direction.findDirection(block, targetBlock));
    } else {
      if (target.isPresent() && !target.get().isDead()) {
        startChase((Attacker) target.get());
      }
    }
  }

  public void stopCharge(GridBlock dest, GridBlock targetBlock) {
    /*
     * We'll attempt to attack the Entity on the target block if it is in range
     * of this Attacker.
     */
    if (Grid.isWithinNBlocks(
        Constants.MELEE_ATTACKER_RANGE, dest, targetBlock)) {
      setDirection(Direction.findDirection(block, targetBlock));
    }
  }

  @Override
  public void attack() {
    if (target.isPresent()) {
      if (target.get() instanceof Scaffold) {
        target.get().setHealth(target.get().getHealth()
            - Constants.MELEE_ATTACKER_SCAFFOLD_DAMAGE);
      } else {
        target.get().setHealth(
            target.get().getHealth() - Constants.MELEE_ATTACKER_DAMAGE);
      }
    }
  }

  @Override
  public void startMotion(GridBlock toReach) {
    // System.out.println("REACHED STAGE 1.1");
    path = new ArrayList<>();
    target = Optional.empty();

    AStar<GridBlock, GridEdge> aStar = new AStar<>();
    GridBlock newDest = toReach;
    for (int i = 0; i < Constants.PATH_RECALCULATION_LIMIT; i++) {
      // System.out.println("REACHED STAGE 1.2, i: " + i);
      try {
        // System.out.println("REACHED STAGE 1.3");
        path = aStar.shortestPath(block, newDest);
        break;
      } catch (UnreachableVertexException e) {
        // System.out.println("REACHED STAGE 1.4");
        if (e.getClosest().isPresent()) {
          newDest = (GridBlock) e.getClosest().get();
          // System.out.println("STAGE 1.5, NEW DEST IS: " + newDest);
        } else {
          break;
        }
      }
    }

    if (path != null && !path.isEmpty()) {
      // System.out.println("REACHED STAGE 1.6");
      eMotion = new MovableMotion(this, path);
    } else {
      // System.out.println("REACHED STAGE 1.7");
      eMotion = new MovableMotion(this);
    }
  }

  @Override
  public void startChase(Attacker newTarget) {
    path = new ArrayList<>();
    target = Optional.empty();

    AStar<GridBlock, GridEdge> aStar = new AStar<>();
    GridBlock newDest = newTarget.getBlock();
    for (int i = 0; i < Constants.PATH_RECALCULATION_LIMIT; i++) {
      try {
        // Checking to see if the newDest is within the range of the target.
        if (!Grid.isWithinNBlocks(Constants.MELEE_ATTACKER_RANGE, newDest,
            newTarget.getBlock())) {
          break;
        }
        path = aStar.shortestPath(block, newDest);
        break;
      } catch (UnreachableVertexException e) {
        if (e.getClosest().isPresent()) {
          newDest = (GridBlock) e.getClosest().get();
        } else {
          break;
        }
      }
    }

    if (path != null && !path.isEmpty()) {
      setTarget(Optional.of(newTarget));
      eMotion = new MeleeChaseMotion(this, path, newTarget);
    } else {
      eMotion = new MovableMotion(this);
      target = Optional.empty();  
    }
  }

  @Override
  public void startCharge(Killable newTarget) {
    path = new ArrayList<>();
    target = Optional.empty();

    AStar<GridBlock, GridEdge> aStar = new AStar<>();
    GridBlock newDest = newTarget.getBlock();
    for (int i = 0; i < Constants.PATH_RECALCULATION_LIMIT; i++) {
      try {
        if (!Grid.isWithinNBlocks(Constants.MELEE_ATTACKER_RANGE, newDest,
            newTarget.getBlock())) {
          break;
        }
        path = aStar.shortestPath(block, newDest);
        break;
      } catch (UnreachableVertexException e) {
        if (e.getClosest().isPresent()) {
          newDest = (GridBlock) e.getClosest().get();
        } else {
          break;
        }
      }
    }

    if (path != null && !path.isEmpty()) {
      setTarget(Optional.of(newTarget));
      eMotion = new MeleeChargeMotion(this, path, newTarget);
    } else {
      eMotion = new MovableMotion(this);
      target = Optional.empty();
    }
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
  public double getMotionRatio() {
    return eMotion.getCurrentRatio();
  }

  @Override
  public Direction getDirection() {
    return eMotion.getCurrentDirection();
  }

  @Override
  public int getReward() {
    if (!alreadyRewarded) {
      alreadyRewarded = true;
      return Constants.MELEE_ATTACKER_REWARD;
    }
    return 0;
  }

  @Override
  public void setDirection(Direction d) {
    eMotion.setCurrentDirection(d);
  }
}
