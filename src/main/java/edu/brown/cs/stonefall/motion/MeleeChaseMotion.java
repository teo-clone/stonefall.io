package edu.brown.cs.stonefall.motion;

import edu.brown.cs.stonefall.custom_exceptions.UnreachableVertexException;
import edu.brown.cs.stonefall.entity.MeleeAttacker;
import edu.brown.cs.stonefall.game.Constants;
import edu.brown.cs.stonefall.interfaces.Attacker;
import edu.brown.cs.stonefall.interfaces.Motion;
import edu.brown.cs.stonefall.map.Grid;
import edu.brown.cs.stonefall.map.GridBlock;
import edu.brown.cs.stonefall.map.GridEdge;
import edu.brown.cs.stonefall.pathing.AStar;
import edu.brown.cs.stonefall.pathing.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * An implementation of the Motion interface representing a MeleeAttacker's
 * chasing motion.
 */
public class MeleeChaseMotion implements Motion {
  private MeleeAttacker parent;
  private Attacker target;
  private List<GridEdge> path;
  private Direction currentDirection;
  private GridEdge currentEdge;
  private Double currentRatio;
  private int pathLen;
  private int currentIndex;
  private boolean inMotion;

  /**
   * Constructs a new MeleeChaseMotion object.
   * @param newParent The MeleeAttacker that is using this Motion object.
   * @param newPath The List of GridEdges representing the path
   *                of this Motion object.
   * @param newTarget The Killable that is target of the newParent's chase.
   */
  public MeleeChaseMotion(
      MeleeAttacker newParent, List<GridEdge> newPath, Attacker newTarget) {
    parent = newParent;
    target = newTarget;
    startMotion(newPath);
  }

  private Optional<GridEdge> nextEdge() {
    if (currentIndex + 1 >= pathLen) {
      return Optional.empty();
    } else {
      return Optional.of(path.get(currentIndex + 1));
    }
  }

  private boolean pathStillValid() {
    if (target.inMotion()) {
      stopMotion(currentEdge.getSrc());
      return false;
    } else {
      return !currentEdge.getDest().isFull()
          && target.getBlock().isReachable(Constants.MELEE_ATTACKER_RANGE);
    }
  }

  private void recalculatePath() {
    AStar<GridBlock, GridEdge> aStar = new AStar<>();
    GridBlock newStart = currentEdge.getSrc();
    GridBlock newDest = target.getBlock();
    List<GridEdge> newPath = new ArrayList<>();
    stopMotion(newStart);

    /*
    If the target is no longer chaseable, this Attacker will get as close
    as it can to the target.
    */
    if (!target.getBlock().isReachable(Constants.MELEE_ATTACKER_RANGE)) {
      parent.startMotion(target.getBlock());
      return;
    }

    /*
    If the target is chaseable, this Attacker will start to recalculate a path.
     */
    for (int i = 0; i < Constants.PATH_RECALCULATION_LIMIT; i++) {
      if (!Grid.isWithinNBlocks(Constants.MELEE_ATTACKER_RANGE, newDest,
          target.getBlock())) {
        break;
      }
      try {
        newPath = aStar.shortestPath(newStart, newDest);
        break;
      } catch (UnreachableVertexException e) {
        if (e.getClosest().isPresent()) {
          newDest = (GridBlock) e.getClosest().get();
        } else {
          break;
        }
      }
    }

    startMotion(newPath);
  }

  private void startMotion(List<GridEdge> newPath) {
    if (newPath.size() == 0) {
      stopMotion(parent.getBlock());
      return;
    }
    path = newPath;
    pathLen = path.size();
    currentIndex = 0;
    currentEdge = path.get(currentIndex);
    currentRatio = 0.0;
    setCurrentDirection(Direction.findDirection(
        currentEdge.getSrc(), currentEdge.getDest()));
    inMotion = true;
  }

  private void stopMotion(GridBlock toStop) {
    pathLen = 0;
    currentIndex = 0;
    currentEdge = null;
    currentRatio = 0.0;
    inMotion = false;
    parent.setBlock(toStop);
  }

  public double getCurrentRatio() {
    return currentRatio;
  }

  public Direction getCurrentDirection() {
    return currentDirection;
  }

  public void setCurrentDirection(Direction d) {
    currentDirection = d;
  }

  public boolean inMotion() {
    return inMotion;
  }

  public void updateMotion() {
    if (pathStillValid()) {
      if (currentDirection.encode() % 2 == 0) {
        currentRatio += Constants.STRAIGHT_MOTION_RATIO;
      } else {
        currentRatio += Constants.DIAGONAL_MOTION_RATIO;
      }
      if (currentRatio >= 1) {
        Optional<GridEdge> nextEdge = nextEdge();
        if (nextEdge.isPresent()) {
          currentIndex++;
          currentEdge = nextEdge.get();
          setCurrentDirection(Direction.findDirection(
              nextEdge.get().getSrc(), nextEdge.get().getDest()));
          parent.setBlock(currentEdge.getSrc());
        } else {
          GridBlock dest = currentEdge.getDest();
          stopMotion(dest);
          parent.stopChase(dest, target.getBlock());
        }
        currentRatio = 0.0;
      }
    } else {
      if (inMotion()) {
        recalculatePath();
      }
    }
  }
}
