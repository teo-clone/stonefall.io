package edu.brown.cs.stonefall.game;

import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import edu.brown.cs.stonefall.custom_exceptions.UnreachableVertexException;
import edu.brown.cs.stonefall.entity.MeleeAttacker;
import edu.brown.cs.stonefall.interfaces.Attacker;
import edu.brown.cs.stonefall.interfaces.Killable;
import edu.brown.cs.stonefall.map.Grid;
import edu.brown.cs.stonefall.map.GridBlock;
import edu.brown.cs.stonefall.network.WebSockets;
import edu.brown.cs.stonefall.pathing.Direction;
import edu.brown.cs.stonefall.structure.Mine;
import edu.brown.cs.stonefall.structure.Resource;
import edu.brown.cs.stonefall.structure.Scaffold;
import edu.brown.cs.stonefall.structure.Turret;
import edu.brown.cs.stonefall.structure.Wall;

/**
 * Class representing the game of Stonefall.
 */
public class Game {

  private Map<String, Player> players;
  private Map<String, GameState> gameStates;
  private Game game;
  private GameState theOneTrueState;
  private Random random;

  private Map<String, Resource> resources;
  private int resourceIdNum;

  private int resSpawnCounter;
  private int resCollectCounter;

  /**
   * Constructs a new instance of a Stonefall game.
   */
  public Game() {
    game = this;
    theOneTrueState = null;
    players = new ConcurrentHashMap<>();
    gameStates = new ConcurrentHashMap<>();
    resources = new ConcurrentHashMap<>();

    resourceIdNum = 0;
    resSpawnCounter = 0;
    resCollectCounter = 0;

    Grid.buildGrid();
    random = new Random();

    for (int i = 0; i < Constants.INITIAL_RESOURCE_COUNT; i++) {
      spawnResource();
    }
  }

  // TimeHandler class responsible for updating GameState every 0.1 seconds.

  /**
   * TimeHandler class responsible for updating Resources, GameStates and the
   * information received over WebSockets per tick period.
   */
  private class Tick extends TimerTask {
    @Override
    public void run() {
      if (System.currentTimeMillis()
          - scheduledExecutionTime() >= Constants.MAX_TARDINESS) {
        return; // Too late; skip this execution.
      }
      // Perform the task
      updateResources();
      updateGameStates();

      WebSockets.update();
    }
  }

  /**
   * Starts the Timer that drives updating, checking and validating game
   * actions.
   */
  public void startTimers() {
    Timer timer = new Timer();
    timer.schedule(new Tick(), 0, Constants.TICK_PERIOD);
  }

  private void updateGameStates() {
    for (Map.Entry<String, Player> player : players.entrySet()) {
      if(theOneTrueState == null){
        theOneTrueState = new GameState(game, player.getValue());
      }
      theOneTrueState.update();
      return;

      // // if player's gamestate already exists, update it.
      // if (gameStates.containsKey(player.getKey())) {
      //   GameState gameStates.get(player.getKey()).update();
      //   // if player's gamestate doesn't exist yet, create it
      // } else {
      //   GameState gs = new GameState(game, player.getValue());
      //   gameStates.put(player.getKey(), gs);
      // }
      // //only keep one game state
      // return;
    }
  }

  private synchronized void updateResources() {
    resCollectCounter++;
    if (resCollectCounter == Constants.RESOURCE_COLLECT_TIME) {

      for (Player player : players.values()) {
        player.setResourceCount(player.getResourceCount() + 2);
        for (Map.Entry<String, Resource> resource : resources.entrySet()) {

          for (Mine mine : player.getMines().values()) {
            if (Grid.isWithinNBlocks(1, mine.getBlock(),
                resource.getValue().getBlock())) {
              mine.collect(resource.getValue());
              player.setResourceCount(
                  player.getResourceCount() + Constants.MINE_COLLECT_RATIO);

              if (resource.getValue().getHealth() <= 0) {
                Grid.getGridBlock(resource.getValue().getX(),
                    resource.getValue().getY()).get().depopulate();
                resources.remove(resource.getKey());
              }
            }
          }
        }
      }
      resCollectCounter = 0;
    }

    resSpawnCounter++;
    if (resSpawnCounter == Constants.RESOURCE_SPAWN_TIME) {
      if (resources.size() <= Constants.MAX_RESOURCES) {
        spawnResource();
      }
      resSpawnCounter = 0;
    }
  }

  /**
   * Adds a new Player to the Game.
   * 
   * @param player
   *          The new Player to add.
   */
  public synchronized void addPlayer(Player player) {
    players.put(player.getId(), player);
    // initialize gamestate
    if(theOneTrueState == null){
      theOneTrueState = new GameState(game, player);
    }
    //tell the gamestate that a new base needs to be added
    // theOneTrueState.addBase(player);
    //when a new player is added the game states have to be updated
    this.updateGameStates();
  }

  /**
   * Removes an existing Player from the Game.
   * 
   * @param player
   *          The existing Player to remove from the Game.
   */
  public synchronized void removePlayer(Player player) {

    players.remove(player.getId());

    player.getBase().getBlock().depopulate();

    for (String mineId : player.getMines().keySet()) {
      Mine mine = player.getMines().get(mineId);
      Grid.getGridBlock(mine.getX(), mine.getY()).get().depopulate();
    }

    for (String turretId : player.getTurrets().keySet()) {
      Turret turret = player.getTurrets().get(turretId);
      Grid.getGridBlock(turret.getX(), turret.getY()).get().depopulate();
    }

    for (String wallId : player.getWalls().keySet()) {
      Wall wall = player.getWalls().get(wallId);
      Grid.getGridBlock(wall.getX(), wall.getY()).get().depopulate();
    }

    for (String attackerId : player.getAttackers().keySet()) {
      Attacker attacker = player.getAttackers().get(attackerId);
      Grid.getGridBlock(attacker.getX(), attacker.getY()).get().depopulate();
    }

    for (String scaffoldId : player.getScaffolds().keySet()) {
      Scaffold scaffold = player.getScaffolds().get(scaffoldId);
      Grid.getGridBlock(scaffold.getX(), scaffold.getY()).get().depopulate();
    }
  }

  private void spawnResource() {
    int x = random.nextInt(Constants.BOARD_WIDTH);
    int y = random.nextInt(Constants.BOARD_HEIGHT);

    while (!Grid.validateCoordinates(x, y)) {
      x = random.nextInt(Constants.BOARD_WIDTH);
      y = random.nextInt(Constants.BOARD_HEIGHT);
    }

    Resource resource = new Resource(Grid.getGridBlock(x, y).get());
    Grid.getGridBlock(x, y).get().populate(resource);

    String resourceId = "/r/" + resourceIdNum;
    resources.put(resourceId, resource);

    resourceIdNum++;
  }

  /**
   * Starts a chasing motion, or a moving motion for the Attacker associated
   * with the given attackerId.
   * 
   * @param attackingPlayer
   *          The Player initiating the Attack.
   * @param attackerId
   *          The attackerId to start a motion or chase for.
   * @param x
   *          The x coordinate of the GridBlock to start the chase or motion to.
   * @param y
   *          The x coordinate of the GridBlock to start the chase or motion to.
   * @throws UnreachableVertexException
   *           If the GridBlock at the specified coordinates is not a valid
   *           GridBlock.
   */
  public void startAction(Player attackingPlayer, String attackerId, int x,
      int y) throws UnreachableVertexException {
    Map<String, Attacker> attackers = players.get(attackingPlayer.getId())
        .getAttackers();
    if (!Grid.getGridBlock(x, y).isPresent()) {
      throw new UnreachableVertexException("non-existent location");
    }

    GridBlock target = Grid.getGridBlock(x, y).get();
    Attacker attacker = attackers.get(attackerId);
    int range;
    if (attacker instanceof MeleeAttacker) {
      range = Constants.MELEE_ATTACKER_RANGE;
    } else {
      range = Constants.RANGED_ATTACKER_RANGE;
    }

    if (attacker != null) {
      // If the target is already where the Attacker wants to go...
      if (Grid.isWithinNBlocks(Constants.MELEE_ATTACKER_RANGE,
          attacker.getBlock(), target) && target.isFull()
          && target.getEntity() instanceof Killable
          && !(target.getEntity() instanceof Resource)) {
        attacker.setDirection(Direction.findDirection(attacker.getBlock(), target));
        attacker.setTarget(Optional.of((Killable) target.getEntity()));
        return;
      }

      if (target.isReachable(range)) {
        // System.out.println("REACHABLE --> START CHASE");
        attacker.startChase((Attacker) target.getEntity());
      } else if (target.isChargeable(range)) {
        // System.out.println("CHARGEABLE --> START CHARGE");
        attacker.startCharge((Killable) target.getEntity());
      } else {
        // System.out.println("NOT REACH/CHARGEABLE --> START MOTION");
        attacker.startMotion(target);
      }
    }
  }

  /**
   * Returns the Map of playerIds to Players.
   * 
   * @return The Map of playerIds to Players.
   */
  public Map<String, Player> getPlayers() {
    return players;
  }

  /**
   * Returns the Map of resourceIds to Resources.
   * 
   * @return The Map of resourceIds to Resources.
   */
  public Map<String, Resource> getResources() {
    return resources;
  }

  /**
   * Returns the Player associated with the given id.
   * 
   * @param id
   *          The id associated with a player.
   * @return The Player associated with id, or null if mapping is empty.
   */
  public Player getPlayer(String id) {
    return players.get(id);
  }

  /**
   * Returns a boolean indicating if a Player exists with the given id.
   * 
   * @param id
   *          The id to validate.
   * @return A boolean indicating if a Player exists with the given id.
   */
  public boolean playerExists(String id) {
    return !(players.get(id) == null);
  }

  /**
   * Returns the f associated with a given Player.
   * 
   * @param player
   *          The Player to retrieve the GameState of.
   * @return The GameState associated with a given Player.
   */
  public GameState getPayload() {
    for (Map.Entry<String, Player> player : players.entrySet()) {
      if(theOneTrueState == null){
        theOneTrueState = new GameState(game, player.getValue());
      }
      return theOneTrueState;
    }
    return theOneTrueState;
    // for (Map.Entry<String, Player> player : players.entrySet()) {

    //   // if player's gamestate already exists, update it.
    //   if (gameStates.containsKey(player.getKey())) {
    //     gameStates.get(player.getKey()).update();
    //     // if player's gamestate doesn't exist yet, create it
    //   } else {
    //     GameState gs = new GameState(game, player.getValue());
    //     gameStates.put(player.getKey(), gs);
    //   }
    //   //only keep one game state
    //   return;
    // }

    // if (!gameStates.containsKey(player.getId())) {
    //   GameState gs = new GameState(game, player);
    //   gameStates.put(player.getId(), gs);
    // }
    // return gameStates.get(player.getId());
  }
}
