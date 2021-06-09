package edu.brown.cs.stonefall.map;

import edu.brown.cs.stonefall.game.Constants;
import edu.brown.cs.stonefall.interfaces.Killable;
import edu.brown.cs.stonefall.network.BoundingBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The class representing the game's grid system.
 */
public final class Grid {
  private static List<List<GridBlock>> gridBlocks;
  private static BoundingBox mapBounds;

  private Grid() { }

  /**
   * Optionally retrieves the GridBlock at the specified coordinate pair.
   *
   * @param x
   *          The x-coordinate to retrieve the GridBlock from.
   * @param y
   *          The y-coordinate to retrieve the GridBlock from.
   * @return An Optional representing if a GridBlock could be retrieved from the
   *         specified location.
   */
  public static Optional<GridBlock> getGridBlock(int x, int y) {
    if (x >= 0 && x < Constants.BOARD_WIDTH
        && y >= 0 && y < Constants.BOARD_HEIGHT) {
      return Optional.of(gridBlocks.get(x).get(y));
    }
    return Optional.empty();
  }

  /**
   * Constructs a grid with size and GridBlocks specified by elements in the
   * Constants class. Indexing starts from (0,0) and increases as one moves
   * right and down, consistent with HTML canvas elements.
   */
  public static void buildGrid() {
    List<List<GridBlock>> gridBlockList = new ArrayList<>();
    mapBounds = new BoundingBox(0, 0,
        Constants.BOARD_WIDTH - 1, Constants.BOARD_HEIGHT - 1);

    // Creating the GridBlocks.
    for (int i = 0; i < Constants.BOARD_WIDTH; i++) {
      List<GridBlock> gridBlockRow = new ArrayList<>();
      for (int j = 0; j < Constants.BOARD_HEIGHT; j++) {
        gridBlockRow.add(new GridBlock(i, j));
      }
      gridBlockList.add(gridBlockRow);
    }

    gridBlocks = gridBlockList;

    // Connecting the GridBlocks.
    createEdges(gridBlockList);
  }

  private static void createEdges(List<List<GridBlock>> gridBlockList) {
    for (int i = 0; i < Constants.BOARD_WIDTH; i++) {
      for (int j = 0; j < Constants.BOARD_HEIGHT; j++) {
        List<GridEdge> prospectiveEdges = new ArrayList<>();
        GridBlock curr = gridBlockList.get(i).get(j);

        // Iterating through valid possible edges.
        for (int m = i - 1; m <= i + 1; m++) {
          for (int n = j - 1; n <= j + 1; n++) {
            if (m >= 0 && m < Constants.BOARD_WIDTH
                && n >= 0 && n < Constants.BOARD_HEIGHT) {
              if (m == i && n == j) {
                continue;
              }
              if ((m == i - 1 || m == i + 1) && (n == j - 1 || n == j + 1)) {
                prospectiveEdges
                    .add(new GridEdge(
                        curr, gridBlockList.get(m).get(n), Math.sqrt(2.0)));
              } else {
                prospectiveEdges.
                    add(new GridEdge(
                        curr, gridBlockList.get(m).get(n), 1.0));
              }
            }
          }
        }
        curr.setEdges(prospectiveEdges);
      }
    }
  }

  /**
   * Validates if certain coordinates are present and empty in the Grid.
   * @param x The x coordinate to validate.
   * @param y The y coordiante to validate.
   * @return A boolean indicating if the given coordinates are present
   * and empty in the Grid.
   */
  public static boolean validateCoordinates(int x, int y) {
    return mapBounds.contains(x, y) && !Grid.getGridBlock(x, y).get().isFull();
  }

  /**
   * Validates if the target GridBlock is within n GridBlocks of the source
   * GridBlock.
   * @param n The number of Blocks specifying the range to check.
   * @param source The GridBlock to calculate the range from.
   * @param target The GridBlock to determine if it is within the range
   *               specified by n.
   * @return A boolean indicating if target is within n GridBlocks from source.
   */
  public static boolean isWithinNBlocks(
      int n, GridBlock source, GridBlock target) {
    return Math.abs(source.getX() - target.getX()) <= n
        && Math.abs(source.getY() - target.getY()) <= n;
  }

  private static Optional<Killable> getAttackableInRange(
      GridBlock block, int range) {
    int offset = 1;
    while (offset <= range) {
      for (int i = -offset; i < 1 + offset; i++) {
        for (int j = -offset; j < 1 + offset; j++) {
          Optional<GridBlock> target =
              Grid.getGridBlock(block.getX() + i, block.getY() + j);
          if (target.isPresent() && target.get().isChargeable(range)) {
            return Optional.of((Killable) target.get().getEntity());
          }
        }
      }
      offset++;
    }
    return Optional.empty();
  }

  private static Optional<Killable> getKillableInRange(
      GridBlock block, int range) {
    // System.out.println("block" + block.getEntity());
    int offset = 1;
    while (offset <= range) {
      for (int i = -offset; i < 1 + offset; i++) {
        for (int j = -offset; j < 1 + offset; j++) {
          Optional<GridBlock> target =
              Grid.getGridBlock(block.getX() + i, block.getY() + j);
          if (target.isPresent() && target.get().isChargeable(range)) {
            return Optional.of((Killable) target.get().getEntity());
          }
        }
      }
      offset++;
    }
    return Optional.empty();
  }

  private static GridBlock getEntityWithin(GridBlock block) {
    int offset = 1;
    while (offset < 3) {
      for (int i = -offset; i < 1 + offset; i++) {
        for (int j = -offset; j < 1 + offset; j++) {
          Optional<GridBlock> target = Grid.getGridBlock(block.getX() + i, block.getY() + j);
          if (target.isPresent() && target.get().isFull()) {
            return target.get();
          }
        }
      }
      offset++;
    }
    return null;
  }
}
