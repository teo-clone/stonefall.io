package pathing;

import edu.brown.cs.stonefall.custom_exceptions.UnreachableVertexException;
import edu.brown.cs.stonefall.pathing.AStar;
import edu.brown.cs.stonefall.pathing.Edge;
import edu.brown.cs.stonefall.pathing.SimpleEdge;
import edu.brown.cs.stonefall.pathing.SimpleVertex;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SimpleAStarTest {

  @Test
  public void testAStarSimple() throws SQLException {
    SimpleVertex v1 = new SimpleVertex("1", 0, 0);
    SimpleVertex v2 = new SimpleVertex("2", 1, 0);
    SimpleVertex v3 = new SimpleVertex("3", 4, 0);
    SimpleVertex v4 = new SimpleVertex("4", 5, 1);
    SimpleVertex v5 = new SimpleVertex("5", 4, 4);
    SimpleEdge e1 = new SimpleEdge(v1, v2, 1);
    SimpleEdge e2 = new SimpleEdge(v2, v3, 3);
    SimpleEdge e3 = new SimpleEdge(v2, v5, 5);
    SimpleEdge e4 = new SimpleEdge(v3, v4, Math.sqrt(2));
    new SimpleEdge(v4, v5, Math.sqrt(10));
    new SimpleEdge(v5, v4, Math.sqrt(10));
    AStar<SimpleVertex, SimpleEdge> aStar = new AStar<>();
    List<Edge<SimpleVertex, SimpleEdge>> expected = new ArrayList<>();
    expected.add(e2);
    expected.add(e4);
    try {
      assertEquals(expected, aStar.shortestPath(v2, v4));
    } catch (UnreachableVertexException e) {
      e.printStackTrace();
    }

    AStar<SimpleVertex, SimpleEdge> aStar1 = new AStar<>();
    List<Edge<SimpleVertex, SimpleEdge>> expected2 = new ArrayList<>();
    expected2.add(e1);
    expected2.add(e2);
    expected2.add(e4);
    try {
      assertEquals(expected2, aStar1.shortestPath(v1, v4));
    } catch (UnreachableVertexException e) {
      e.printStackTrace();
    }

    AStar<SimpleVertex, SimpleEdge> aStar2 = new AStar<>();
    List<Edge<SimpleVertex, SimpleEdge>> expected3 = new ArrayList<>();
    expected3.add(e1);
    expected3.add(e3);
    try {
      assertEquals(expected3, aStar2.shortestPath(v1, v5));
    } catch (UnreachableVertexException e) {
      e.printStackTrace();
    }
  }
}
