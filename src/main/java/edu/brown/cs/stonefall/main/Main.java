package edu.brown.cs.stonefall.main;

import edu.brown.cs.stonefall.game.Game;
import edu.brown.cs.stonefall.network.SparkHandler;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

/**
 * The Main class of our project. This is where execution begins.
 *
 * @author Theodoros
 */
public final class Main {

  private static final int DEFAULT_PORT = 4567;
  private static final int GRID_SIZE_X = 100;
  private static final int GRID_SIZE_Y = 100;

  /**
   * The initial method called when execution begins.
   *
   * @param args
   *          An array of command line arguments
   */
  public static void main(String[] args) {
    new Main(args).run();
  }

  private String[] args;

  private Main(String[] args) {
    this.args = args;
  }

  private void run() {
    // Parse command line arguments
    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
        .defaultsTo(DEFAULT_PORT);
    OptionSet options = parser.parse(args);
    Game game = new Game();
    game.startTimers();
    new SparkHandler(options, game);
  }
}
