package edu.brown.cs.stonefall.interfaces;

import edu.brown.cs.stonefall.map.GridBlock;
import edu.brown.cs.stonefall.pathing.Cartesian;

/**
 * An interface for any Entity in Game.
 */
public interface Entity extends Cartesian {
  /**
   * Returns the GridBlock this Entity has populated.
   * @return The GridBlock this Entity has populated
   */
  GridBlock getBlock();

  /**
   * Sets the GridBlock for this Entity to populate.
   * @param toSet The GridBlock for this Entity to populate.
   */
  void setBlock(GridBlock toSet);
}
