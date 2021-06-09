package edu.brown.cs.stonefall.motion;

import edu.brown.cs.stonefall.custom_exceptions.UnreachableVertexException;
import edu.brown.cs.stonefall.game.Constants;
import edu.brown.cs.stonefall.interfaces.Motion;
import edu.brown.cs.stonefall.interfaces.Movable;
import edu.brown.cs.stonefall.map.GridBlock;
import edu.brown.cs.stonefall.map.GridEdge;
import edu.brown.cs.stonefall.pathing.AStar;
import edu.brown.cs.stonefall.pathing.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * An implementation of the Motion interface representing a Movable object's
 * motion between two initially empty GridBlocks.
 */
public class MovableMotion implements Motion {
  private Movable parent;
  private List<GridEdge> path;
  private Direction currentDirection;
  private GridEdge currentEdge;
  private Double currentRatio;
  private int pathLen;
  private int currentIndex;
  private boolean inMotion;

  /**
   * Constructs a new MovableMotion object for a stopped Movable parent.
   * @param newParent The Movable object that is using this MovableMotion
   *                  object.
   */
  public MovableMotion(Movable newParent) {
    parent = newParent;
    stopMotion(parent.getBlock());
  }

  /**
   * Constructs a new MovableMotion object for a moving Movable parent.
   * @param newParent The Movable object that is using this MovableMotion
   *                  object.
   * @param newPath The List of GridEdges representing the path
   *                of this Motion object.
   */
  public MovableMotion(Movable newParent, List<GridEdge> newPath) {
    parent = newParent;
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
    return !currentEdge.getDest().isFull();
  }

  private void recalculatePath() {
    AStar<GridBlock, GridEdge> aStar = new AStar<>();
    GridBlock newStart = currentEdge.getSrc();
    GridBlock newDest = path.get(pathLen - 1).getDest();
    List<GridEdge> newPath = new ArrayList<>();

    // may help smoothness
    stopMotion(newStart);

    for (int i = 0; i < Constants.PATH_RECALCULATION_LIMIT; i++) {
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
    path = new ArrayList<>();
    pathLen = 0;
    currentIndex = 0;
    currentEdge = null;
    currentRatio = 0.0;
    currentDirection = Direction.STOP;
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
          stopMotion(currentEdge.getDest());
        }
        currentRatio = 0.0;
      }
    } else {
      recalculatePath();
    }
  }
}

