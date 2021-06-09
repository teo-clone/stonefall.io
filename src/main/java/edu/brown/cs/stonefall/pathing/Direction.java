package edu.brown.cs.stonefall.pathing;

import edu.brown.cs.stonefall.map.GridBlock;

/**
 * A class representing Directions.
 */
public enum Direction {
  N, NE, E, SE, S, SW, W, NW, STOP;

  /**
   * Encodes this Direction as an int for Javascript communication.
   * @return A unique int corresponding to this Direction.
   */
  public int encode() {
    if (this == Direction.N) {
      return 0;
    } else if (this == Direction.NE) {
      return 1;
    } else if (this == Direction.E) {
      return 2;
    } else if (this == Direction.SE) {
      return 3;
    } else if (this == Direction.S) {
      return 4;
    } else if (this == Direction.SW) {
      return 5;
    } else if (this == Direction.W) {
      return 6;
    } else if (this == Direction.NW) {
      return 7;
    } else {
      return 8;
    }
  }

  /**
   * Returns the direction between two GridBlocks.
   * @param source The source GridBlock.
   * @param dest The destination GridBlock.
   * @return Returns the direction between the two GridBlocks.
   */
  public static Direction findDirection(GridBlock source, GridBlock dest) {
    Direction currentDirection = Direction.STOP;
    double srcX = source.getX();
    double srcY = source.getY();
    double destX = dest.getX();
    double destY = dest.getY();

    if (destX > srcX) {
      // Moving East.
      if (destY > srcY) {
        currentDirection = Direction.SE;
      } else if (destY < srcY) {
        currentDirection = Direction.NE;
      } else {
        currentDirection = Direction.E;
      }
    } else if (destX < srcX) {
      // Moving West.
      if (destY > srcY) {
        currentDirection = Direction.SW;
      } else if (destY < srcY) {
        currentDirection = Direction.NW;
      } else {
        currentDirection = Direction.W;
      }
    } else if (Double.compare(destX, srcX) == 0) {
      // Moving only North or South.
      if (destY > srcY) {
        // Moving South.
        currentDirection = Direction.S;
      } else if (destY < srcY) {
        // Moving North.
        currentDirection = Direction.N;
      }
    }

    return currentDirection;
  }

  @Override
  public String toString() {
    if (this == Direction.N) {
      return "N";
    } else if (this == Direction.NE) {
      return "NE";
    } else if (this == Direction.E) {
      return "E";
    } else if (this == Direction.SE) {
      return "SE";
    } else if (this == Direction.S) {
      return "S";
    } else if (this == Direction.SW) {
      return "SW";
    } else if (this == Direction.W) {
      return "W";
    } else if (this == Direction.NW) {
      return "NW";
    } else {
      return "STOP";
    }
  }
}
