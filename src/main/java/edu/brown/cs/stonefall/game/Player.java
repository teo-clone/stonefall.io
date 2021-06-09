package edu.brown.cs.stonefall.game;

import java.awt.Color;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import edu.brown.cs.stonefall.entity.MeleeAttacker;
import edu.brown.cs.stonefall.entity.RangedAttacker;
import edu.brown.cs.stonefall.interfaces.Attacker;
import edu.brown.cs.stonefall.interfaces.Killable;
import edu.brown.cs.stonefall.map.Grid;
import edu.brown.cs.stonefall.structure.Base;
import edu.brown.cs.stonefall.structure.Mine;
import edu.brown.cs.stonefall.structure.Scaffold;
import edu.brown.cs.stonefall.structure.Turret;
import edu.brown.cs.stonefall.structure.Wall;

/**
 * Backend player class. Responsible for maintaining relationship between player
 * and all of their owned objects.
 *
 * @author Theodoros
 */
public class Player {

  private Base base;

  private Map<String, Wall> walls;
  private Map<String, Turret> turrets;
  private Map<String, Mine> mines;
  private Map<String, Scaffold> scaffolds;

  private Map<String, Attacker> attackers;

  private String name;
  private String id;
  private Color color;

  private int resourceCount;
  private int mineIdNum;
  private int attackerIdNum;
  private int structureIdNum;
  private int scaffoldIdNum;
  private int score;
  private int topScore;
  //these represent the viewing window of the user on the frontend
  private int topLX;
  private int topLY;
  private int bottomRX;
  private int bottomRY;

  private Random random;

  /**
   * Constructor for a player.
   *
   * Fields:
   *
   * name: player name
   *
   * id: player id
   *
   * color: player color
   *
   * mines: hashmap of all mines owned by player
   *
   * attackers: hashmap of all attackers owned by player
   *
   * turrets: hashmap of all turrets owned by player
   *
   * walls: hashmap of all walls owned by player
   *
   * scaffolds: hashmap of all scaffolds owned by player
   *
   * base: player base
   *
   * score: player score
   *
   * resourceCount: player resource count
   *
   * @param name
   *          name of player
   * @param id
   *          id of player
   */
  public Player(String name, String id) {
    random = new Random();

    this.name = name;
    this.id = id;
    color = new Color(random.nextInt(Constants.MAX_COLOR_VALUE));

    mines = new ConcurrentHashMap<>();
    attackers = new ConcurrentHashMap<>();
    turrets = new ConcurrentHashMap<>();
    walls = new ConcurrentHashMap<>();
    scaffolds = new ConcurrentHashMap<>();

    int x = random.nextInt(Constants.BOARD_WIDTH);
    int y = random.nextInt(Constants.BOARD_HEIGHT);

    while (!Grid.validateCoordinates(x, y)) {
      x = random.nextInt(Constants.BOARD_WIDTH);
      y = random.nextInt(Constants.BOARD_HEIGHT);
    }

    base = new Base(Grid.getGridBlock(x, y).get());
    topLX = x - 10;
    topLY = x - 10;
    bottomRX = x + 10;
    bottomRY = x + 10;

    score = 0;
    topScore = 0;

    if (name.equals("breezy") || name.equals("jj") || name.equals("papper")
        || name.equals("dave") || name.equals("mac")) {
      resourceCount = 200000;
    } else {
      resourceCount = 2000;
    }

    mineIdNum = 0;
    attackerIdNum = 0;
    structureIdNum = 0;
    scaffoldIdNum = 0;
  }

  /**
   * Returns color of player in string format.
   *
   * @return string formatted color
   */
  public String getColorHex() {
    return String.format("#%06x", color.getRGB() & Constants.MAX_COLOR_VALUE);
  }

  /**
   * Validates the spawning of a new object. Checks its coordinates and if it's
   * within 3 units of other owned structures
   *
   * @param x
   *          x coordinate
   * @param y
   *          y coordinate
   * @return true if spawn is valid, false otherwise
   */
  public synchronized boolean validateSpawn(int x, int y) {
    if (Grid.isWithinNBlocks(3, base.getBlock(),
        Grid.getGridBlock(x, y).get())) {
      return true;
    }

    for (Wall wall : walls.values()) {
      if (Grid.isWithinNBlocks(3, wall.getBlock(),
          Grid.getGridBlock(x, y).get())) {
        return true;
      }
    }
    for (Mine mine : mines.values()) {
      if (Grid.isWithinNBlocks(3, mine.getBlock(),
          Grid.getGridBlock(x, y).get())) {
        return true;
      }
    }
    for (Turret turret : turrets.values()) {
      if (Grid.isWithinNBlocks(3, turret.getBlock(),
          Grid.getGridBlock(x, y).get())) {
        return true;
      }
    }

    return false;
  }

  /**
   * Spawns a mine at the x, y coordinate.
   *
   * @param x
   *          x coordinate
   * @param y
   *          y coordinate
   */
  public synchronized void spawnMine(int x, int y) {
    // if placement coordinates are valid and player has at least 30 resources
    if (Grid.validateCoordinates(x, y) && validateSpawn(x, y)) {
      Mine mine = new Mine(Grid.getGridBlock(x, y).get());

      String mineId = "/w/" + mineIdNum;
      mines.put(mineId, mine);
      mineIdNum++;

      score += Constants.MINE_COST;
      if (score > topScore) {
        topScore = score;
      }
    }
  }

  /**
   * Spawns a melee attacker at the x, y coordinate.
   *
   * @param x
   *          x coordinate
   * @param y
   *          y coordinate
   */
  public synchronized void spawnMeleeAttacker(int x, int y) {
    if (Grid.validateCoordinates(x, y)
        && resourceCount >= multiplyByScoreLogistically(Constants.ATTACKER_COST)
        && validateSpawn(x, y)) {
      Attacker attacker = new MeleeAttacker(Grid.getGridBlock(x, y).get());

      String attackerId = "/a/" + attackerIdNum;
      attackers.put(attackerId, attacker);
      attackerIdNum++;

      resourceCount -= multiplyByScoreLogistically(Constants.ATTACKER_COST);
      score += Constants.ATTACKER_COST;
      if (score > topScore) {
        topScore = score;
      }
    }
  }

  /**
   * Spawns a ranged attacker at the x, y coordinate.
   *
   * @param x
   *          x coordinate
   * @param y
   *          y coordinate
   */
  public synchronized void spawnRangedAttacker(int x, int y) {
    if (Grid.validateCoordinates(x, y)
        && resourceCount >= multiplyByScoreLogistically(Constants.ATTACKER_COST)
        && validateSpawn(x, y)) {
      Attacker attacker = new RangedAttacker(Grid.getGridBlock(x, y).get());

      String attackerId = "/a/" + attackerIdNum;
      attackers.put(attackerId, attacker);
      attackerIdNum++;

      resourceCount -= multiplyByScoreLogistically(Constants.ATTACKER_COST);
      score += Constants.ATTACKER_COST;
      if (score > topScore) {
        topScore = score;
      }
    }
  }

  /**
   * Spawns a turret at the x, y coordinate.
   *
   * @param x
   *          x coordinate
   * @param y
   *          y coordinate
   */
  public synchronized void spawnTurret(int x, int y) {
    if (Grid.validateCoordinates(x, y) && validateSpawn(x, y)) {
      Turret turret = new Turret(Grid.getGridBlock(x, y).get());

      String turretId = "/s/" + structureIdNum;
      turrets.put(turretId, turret);
      structureIdNum++;

      score += Constants.TURRET_COST;
      if (score > topScore) {
        topScore = score;
      }
    }
  }

  /**
   * Spawns a wall at the x, y coordinate.
   *
   * @param x
   *          x coordinate
   * @param y
   *          y coordinate
   */
  public synchronized void spawnWall(int x, int y) {
    if (Grid.validateCoordinates(x, y) && validateSpawn(x, y)) {

      Wall wall = new Wall(Grid.getGridBlock(x, y).get());

      String wallId = "/s/" + structureIdNum;
      walls.put(wallId, wall);
      structureIdNum++;

      score += Constants.WALL_COST;
      if (score > topScore) {
        topScore = score;
      }
    }
  }

  /**
   * Spawns a scaffold of type scaffoldType at the x, y coordinate.
   *
   * @param x
   *          x coordinate
   * @param y
   *          y coordinate
   * @param scaffoldType
   *          scaffold type: could either be a mine or a turret or a wall
   */
  public synchronized void spawnScaffold(int x, int y, int scaffoldType) {
    if (Grid.validateCoordinates(x, y)) {
      String scaffoldId = "/f/" + scaffoldIdNum;
      Scaffold scaffold = new Scaffold(Grid.getGridBlock(x, y).get(), this,
          scaffoldType, scaffoldId);

      if (scaffoldType == Constants.OBJECT_TYPE.MINE.ordinal()
          && resourceCount >= multiplyByScoreLogistically(
              Constants.MINE_COST)) {
        resourceCount -= multiplyByScoreLogistically(Constants.MINE_COST);
      } else if (scaffoldType == Constants.OBJECT_TYPE.TURRET.ordinal()
          && resourceCount >= multiplyByScoreLogistically(
              Constants.TURRET_COST)) {
        resourceCount -= multiplyByScoreLogistically(Constants.TURRET_COST);
      } else if (scaffoldType == Constants.OBJECT_TYPE.WALL.ordinal()
          && resourceCount >= multiplyByScoreLogistically(
              Constants.WALL_COST)) {
        resourceCount -= multiplyByScoreLogistically(Constants.WALL_COST);
      }

      scaffolds.put(scaffoldId, scaffold);
      scaffoldIdNum++;
    }
  }

  /**
   * Upgrades the passed in scaffold to its scaffoldType.
   *
   * @param scaffold
   *          scaffold to upgrade
   */
  public synchronized void upgradeScaffold(Scaffold scaffold) {
    // Removing the scaffold.
    Grid.getGridBlock(scaffold.getX(), scaffold.getY()).get().depopulate();
    scaffolds.remove(scaffold.getId());

    // spawning the structure it represents in its place
    if (scaffold.getScaffoldType() == Constants.OBJECT_TYPE.MINE.ordinal()) {
      spawnMine(scaffold.getX(), scaffold.getY());
    } else if (scaffold.getScaffoldType() == Constants.OBJECT_TYPE.TURRET
        .ordinal()) {
      spawnTurret(scaffold.getX(), scaffold.getY());
    } else if (scaffold.getScaffoldType() == Constants.OBJECT_TYPE.WALL
        .ordinal()) {
      spawnWall(scaffold.getX(), scaffold.getY());
    }
  }

  /**
   * Checks whether a player is dead by checking the base health.
   *
   * @return true if player is dead, false otherwise
   */
  public boolean isDead() {
    return base.isDead();
  }

    /**
   * Sets the player's viewing window.
   *
   * @param x1,y1,x1,y2
   *          new resource count
   */
  public void setViewingWindow(int x1, int y1, int x2, int y2) {
    topLX = x1;
    topLY = y1;
    bottomRX = x2;
    bottomRY = y2;
  }

  /**
   * Sets the player's viewing window.
   *
   * @param x1,y1,x1,y2
   *          new resource count
   */
  public boolean inViewingWindow(int x, int y) {
    // return (x >= topLX-1) && (x<= bottomRX+1) && (y >= topLY-1) && (y <= bottomRY+1);
    return (x >= topLX) && (x<= bottomRX) && (y >= topLY) && (y <= bottomRY);
  }

  /**
   * Sets the player's resource count.
   *
   * @param newCount
   *          new resource count
   */
  public void setResourceCount(int newCount) {
    resourceCount = newCount;
  }

  /**
   * Gets the resource count.
   *
   * @return resource count
   */
  public int getResourceCount() {
    return resourceCount;
  }

  /**
   * Gets the player's name.
   *
   * @return name
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the player's id.
   *
   * @return id
   */
  public String getId() {
    return id;
  }

  /**
   * Gets the player's attackers.
   *
   * @return map from attacker id to attacker object
   */
  public Map<String, Attacker> getAttackers() {
    return attackers;
  }

  /**
   * Gets the player's turrets.
   *
   * @return map from turret id to turret object
   */
  public Map<String, Turret> getTurrets() {
    return turrets;
  }

  /**
   * Gets the player's walls.
   *
   * @return map from wall id to wall object
   */
  public Map<String, Wall> getWalls() {
    return walls;
  }

  /**
   * Gets the player's mines.
   *
   * @return map from mine id to mine object
   */
  public Map<String, Mine> getMines() {
    return mines;
  }

  /**
   * Gets the player's scaffolds.
   *
   * @return map from scaffold id to scaffold object
   */
  public Map<String, Scaffold> getScaffolds() {
    return scaffolds;
  }

  /**
   * Gets the player's base.
   *
   * @return base
   */
  public Base getBase() {
    return base;
  }

  /**
   * Validates a target.
   *
   * @param target
   *          target to validate
   * @param attacker
   *          attacker that wants to attack target
   * @return true if target is valid, false otherwise
   */
  public boolean validateTarget(Optional<Killable> target, Attacker attacker) {
    if (!target.isPresent() || attackers.containsValue(target.get())
        || walls.containsValue(target.get())
        || base.getBlock().equals(target.get().getBlock())
        || mines.containsValue(target.get())
        || turrets.containsValue(target.get())
        || scaffolds.containsValue(target.get())
        || !attacker.getTarget().isPresent()
        || !target.get().getBlock().isFull()
        || !Grid.isWithinNBlocks(Constants.MELEE_ATTACKER_RANGE,
            attacker.getBlock(), attacker.getTarget().get().getBlock())) {
      return false;
    }
    return true;
  }

  @Override
  public boolean equals(Object other) {
    return (other instanceof Player)
        && ((Player) other).getId().equals(getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }

  /**
   * Removes an object, and updates map, score, and player hashmaps accordingly.
   *
   * @param objectType
   *          object type we wish to remove
   * @param objectId
   *          id of object we wish to remove
   */
  public synchronized void remove(int objectType, String objectId) {
    try{
      if (objectType == Constants.OBJECT_TYPE.MINE.ordinal()) {
        Grid.getGridBlock(mines.get(objectId).getX(), mines.get(objectId).getY())
            .get().depopulate();
        mines.remove(objectId);
        score -= Constants.MINE_COST;
      } else if (objectType == Constants.OBJECT_TYPE.TURRET.ordinal()) {
        Grid.getGridBlock(turrets.get(objectId).getX(),
            turrets.get(objectId).getY()).get().depopulate();
        turrets.remove(objectId);
        score -= Constants.TURRET_COST;
      } else if (objectType == Constants.OBJECT_TYPE.WALL.ordinal()) {
        Grid.getGridBlock(walls.get(objectId).getX(), walls.get(objectId).getY())
            .get().depopulate();
        walls.remove(objectId);
        score -= Constants.WALL_COST;
      } else if (objectType == Constants.OBJECT_TYPE.ATTACKER.ordinal()) {
        Grid.getGridBlock(attackers.get(objectId).getX(),
            attackers.get(objectId).getY()).get().depopulate();
        attackers.remove(objectId);
        score -= Constants.ATTACKER_COST;
      } else if (objectType == Constants.OBJECT_TYPE.SCAFFOLD.ordinal()) {
        Grid.getGridBlock(scaffolds.get(objectId).getX(),
            scaffolds.get(objectId).getY()).get().depopulate();
        scaffolds.remove(objectId);
      }
    } catch (NullPointerException n){
      System.out.println("Attempted to remove null object");
    }

  }

  /**
   * Sells an object. Increments the resource count of the player and then
   * removes it.
   *
   * @param objectType
   *          type of object we want to sell
   * @param objectId
   *          id of object we want to sell
   */
  public synchronized void sell(int objectType, String objectId) {
    if (objectType == Constants.OBJECT_TYPE.MINE.ordinal()) {
      resourceCount += Constants.MINE_REWARD;
    } else if (objectType == Constants.OBJECT_TYPE.TURRET.ordinal()) {
      resourceCount += Constants.TURRET_REWARD;
    } else if (objectType == Constants.OBJECT_TYPE.WALL.ordinal()) {
      resourceCount += Constants.WALL_REWARD;
    } else if (objectType == Constants.OBJECT_TYPE.ATTACKER.ordinal()) {
      resourceCount += Constants.MELEE_ATTACKER_REWARD;
    }
    remove(objectType, objectId);

  }

  /**
   * Gets the player score.
   *
   * @return score
   */
  public int getScore() {
    return score;
  }

  /**
   * Gets the players top score.
   *
   * @return top score
   */
  public int getTopScore() {
    return topScore;
  }

  /**
   * Multiplies cost by a logistic growth model with a carrying capacity of a
   * ten multiplier.
   *
   * @param cost
   *          cost we wish to multiply logistically
   * @return multiplied cost
   */
  public int multiplyByScoreLogistically(int cost) {
    double e = Math.E;
    double exponent = Math.pow(e, (0.0001 * getScore()));
    double newMultiplier = (((10 * 1.001) * exponent)
        / (10 + 1.001 * (exponent) - 1));
    double newCost = newMultiplier * cost;
    int intCost = (int) Math.round(newCost);
    int roundedToHundreds = Math.round((intCost + 99) / 100) * 100;
    return roundedToHundreds;
  }
}
