package pathing;

import edu.brown.cs.stonefall.game.Constants;
import edu.brown.cs.stonefall.map.Grid;
import edu.brown.cs.stonefall.map.GridBlock;
import edu.brown.cs.stonefall.map.GridEdge;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class GridConsistencyTest {

  @Test
  public void testConsistentEdges() {
    Grid.buildGrid();
    GridBlock gb1 = Grid.getGridBlock(5, 21).get();
    List<GridEdge> correctEdges = new ArrayList<>();
    correctEdges.addAll(Arrays.asList(new GridEdge(gb1, new GridBlock(4, 20), Math.sqrt(2.0)),
        new GridEdge(gb1, new GridBlock(4, 21), 1.0),
        new GridEdge(gb1, new GridBlock(4, 22), Math.sqrt(2.0)),
        new GridEdge(gb1, new GridBlock(5, 20), 1.0),
        new GridEdge(gb1, new GridBlock(5, 22), 1.0),
        new GridEdge(gb1, new GridBlock(6, 20), Math.sqrt(2.0)),
        new GridEdge(gb1, new GridBlock(6, 21), 1.0),
        new GridEdge(gb1, new GridBlock(6, 22), Math.sqrt(2.0))));
    assertEquals(correctEdges, gb1.getEdges());
  }

  @Test
  public void testBoundedEdges() {
    Grid.buildGrid();
    int maxX = Constants.BOARD_WIDTH - 1;
    int maxY = Constants.BOARD_HEIGHT - 1;
    GridBlock gb1 = Grid
        .getGridBlock(Constants.BOARD_WIDTH - 1, Constants.BOARD_HEIGHT - 1).get();
    List<GridEdge> correctEdges = Arrays.asList(
        new GridEdge(gb1, new GridBlock(maxX - 1, maxY - 1), Math.sqrt(2.0)),
        new GridEdge(gb1, new GridBlock(maxX - 1, maxY), 1.0),
        new GridEdge(gb1, new GridBlock(maxX, maxY - 1), 1.0));
    assertEquals(correctEdges, gb1.getEdges());
  }

  @Test
  public void testConsistentEquality() {
    Grid.buildGrid();
    GridBlock gb1 = new GridBlock(20, 40);
    GridBlock gb2 = new GridBlock(20, 40);
    assertEquals(gb1, gb2);

    GridBlock gb3 = new GridBlock(23, 41);
    GridBlock gb4 = new GridBlock(15, 41);
    assertNotEquals(gb3, gb4);
  }
}
