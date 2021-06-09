package edu.brown.cs.stonefall.game;

/**
 * A class containing constantly accessed runtime constants.
 */
public final class Constants {
  public static final int PATH_RECALCULATION_LIMIT = 2;

  public static final int MAX_TARDINESS = 25;

  public static final int BOARD_WIDTH = 200;
  public static final int BOARD_HEIGHT = 100;
  static final int TICK_PERIOD = 20;

  static final int INITIAL_RESOURCE_COUNT = 200;

  public static final int RESOURCE_HP = 5000;

  public static final int MELEE_ATTACKER_HP = 3000;
  public static final int MELEE_ATTACKER_DAMAGE = 3;
  public static final int MELEE_ATTACKER_SCAFFOLD_DAMAGE = 10
      * MELEE_ATTACKER_DAMAGE;
  public static final int MELEE_ATTACKER_RANGE = 1;
  public static final int RANGED_ATTACKER_HP = 80;
  public static final int RANGED_ATTACKER_DAMAGE = 2;
  public static final int RANGED_ATTACKER_RANGE = 3;

  public static final int MINE_COLLECT_RATIO = 5;
  static final int RESOURCE_SPAWN_TIME = 100;
  static final int RESOURCE_COLLECT_TIME = 15;
  static final int MAX_RESOURCES = 280;

  public static final int TURRET_DAMAGE = 2;
  static final int TURRET_RANGE = 3;

  public static final double STRAIGHT_MOTION_RATIO = .002525 * TICK_PERIOD;
  public static final double DIAGONAL_MOTION_RATIO = .0017854445 * TICK_PERIOD;

  public static final int BASE_HP = 20000;
  public static final int MINE_HP = 2000;
  public static final int TURRET_HP = 500;
  public static final int WALL_HP = 4000;

  public static final int WALL_COST = 90;
  public static final int MINE_COST = 950;
  public static final int TURRET_COST = 450;
  public static final int ATTACKER_COST = 250;

  public static final int MAX_COLOR_VALUE = 0xFFFFFF;

  public static final int MELEE_ATTACKER_REWARD = (int) Math
      .round(0.25 * ATTACKER_COST);
  public static final int RANGED_ATTACKER_REWARD = (int) Math
      .round(0.25 * ATTACKER_COST);
  public static final int BASE_REWARD = 1000;
  public static final int TURRET_REWARD = (int) Math.round(0.25 * TURRET_COST);
  public static final int MINE_REWARD = (int) Math.round(0.25 * MINE_COST);
  public static final int WALL_REWARD = (int) Math.round(0.25 * WALL_COST);

  public static final int SCAFFOLD_REGENERATION_RATE = TICK_PERIOD / 2;

  static final int LEADERBOARD_SIZE = 10;

  public static final int MAX_PLAYERS = 50;

  private Constants() {
  }

  public enum MESSAGE_TYPE {
    CONNECT, UPDATE, ATTACK, CREATE, INITIALIZE, SELL, ERROR, GAMEOVER
  }

  public enum OBJECT_TYPE {
    WALL, TURRET, ATTACKER, MINE, SCAFFOLD
  }
}
