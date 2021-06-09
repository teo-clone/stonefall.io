package edu.brown.cs.stonefall.network;

import java.util.List;

public class JacksonJSON {
  private My my;
  private Others others;
  private List<Resources> resources;
  private Integer id;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public My getMy() {
    return my;
  }

  public void setMy(My my) {
    this.my = my;
  }

  public Others getOthers() {
    return others;
  }

  public void setOthers(Others others) {
    this.others = others;
  }

  public List<Resources> getResources() {
    return resources;
  }

  public void setResources(List<Resources> resources) {
    this.resources = resources;
  }

}

class Message {
  private JacksonJSON payload;
  private int type;

  public JacksonJSON getPayload() {
    return payload;
  }

  public void setPayload(JacksonJSON myJson) {
    this.payload = myJson;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }
}

class My {
  private List<Killable> walls;
  private JBase base;
  private Statistics statistics;
  private List<JTurret> turrets;
  private List<JAttacker> attackers;

  public List<Killable> getWalls() {
    return walls;
  }

  public void setWalls(List<Killable> walls) {
    this.walls = walls;
  }

  public JBase getBase() {
    return base;
  }

  public void setBase(JBase base) {
    this.base = base;
  }

  public Statistics getStatistics() {
    return statistics;
  }

  public void setStatistics(Statistics statistics) {
    this.statistics = statistics;
  }

  public List<JTurret> getTurrets() {
    return turrets;
  }

  public void setTurrets(List<JTurret> turrets) {
    this.turrets = turrets;
  }

  public List<JAttacker> getAttackers() {
    return attackers;
  }

  public void setAttackers(List<JAttacker> attackers) {
    this.attackers = attackers;
  }

  public List<Killable> getMines() {
    return mines;
  }

  public void setMines(List<Killable> mines) {
    this.mines = mines;
  }

  public List<Killable> getScaffoldings() {
    return scaffoldings;
  }

  public void setScaffoldings(List<Killable> scaffoldings) {
    this.scaffoldings = scaffoldings;
  }

  private List<Killable> mines;
  private List<Killable> scaffoldings;
}

class Others {
  private List<JBase> bases;
  private List<Killable> walls;
  private List<JTurret> turrets;
  private List<JAttacker> attackers;

  public List<JBase> getBases() {
    return bases;
  }

  public void setBases(List<JBase> bases) {
    this.bases = bases;
  }

  public List<Killable> getWalls() {
    return walls;
  }

  public void setWalls(List<Killable> walls) {
    this.walls = walls;
  }

  public List<JTurret> getTurrets() {
    return turrets;
  }

  public void setTurrets(List<JTurret> turrets) {
    this.turrets = turrets;
  }

  public List<JAttacker> getAttackers() {
    return attackers;
  }

  public void setAttackers(List<JAttacker> attackers) {
    this.attackers = attackers;
  }

  public List<Killable> getMines() {
    return mines;
  }

  public void setMines(List<Killable> mines) {
    this.mines = mines;
  }

  public List<Killable> getScaffoldings() {
    return scaffoldings;
  }

  public void setScaffoldings(List<Killable> scaffoldings) {
    this.scaffoldings = scaffoldings;
  }

  private List<Killable> mines;
  private List<Killable> scaffoldings;
}

class Resources {
  private int x;
  private int y;
  private double health;

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public double getHealth() {
    return health;
  }

  public void setHealth(double health) {
    this.health = health;
  }

  public double getMaxHealth() {
    return maxHealth;
  }

  public void setMaxHealth(double maxHealth) {
    this.maxHealth = maxHealth;
  }

  private double maxHealth;
}

class Killable {
  private int x;
  private int y;

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public double getHealth() {
    return health;
  }

  public void setHealth(double health) {
    this.health = health;
  }

  public double getMaxHealth() {
    return maxHealth;
  }

  public void setMaxHealth(double maxHealth) {
    this.maxHealth = maxHealth;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  private double health;
  private double maxHealth;
  private String id;
  private String color;
}

class JTurret {
  private int x;
  private int y;
  private double health;
  private double maxHealth;
  private String id;
  private String color;
  private Attacking attacking;

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public double getHealth() {
    return health;
  }

  public void setHealth(double health) {
    this.health = health;
  }

  public double getMaxHealth() {
    return maxHealth;
  }

  public void setMaxHealth(double maxHealth) {
    this.maxHealth = maxHealth;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public Attacking getAttacking() {
    return attacking;
  }

  public void setAttacking(Attacking attacking) {
    this.attacking = attacking;
  }
}

class Attacking {
  private int x;
  private int y;
  private int direction;

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public int getDirection() {
    return direction;
  }

  public void setDirection(int direction) {
    this.direction = direction;
  }

  public double getRatio() {
    return ratio;
  }

  public void setRatio(double ratio) {
    this.ratio = ratio;
  }

  private double ratio;
}

class JAttacker {
  private int targetX;
  private int targetY;
  private int x;
  private int y;
  private double health;
  private double maxHealth;

  public int getTargetX() {
    return targetX;
  }

  public void setTargetX(int targetX) {
    this.targetX = targetX;
  }

  public int getTargetY() {
    return targetY;
  }

  public void setTargetY(int targetY) {
    this.targetY = targetY;
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public double getHealth() {
    return health;
  }

  public void setHealth(double health) {
    this.health = health;
  }

  public double getMaxHealth() {
    return maxHealth;
  }

  public void setMaxHealth(double maxHealth) {
    this.maxHealth = maxHealth;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public Movement getMovement() {
    return movement;
  }

  public void setMovement(Movement movement) {
    this.movement = movement;
  }

  public boolean isAttacking() {
    return isAttacking;
  }

  public void setAttacking(boolean isAttacking) {
    this.isAttacking = isAttacking;
  }

  private String id;
  private String color;
  private Movement movement;
  private boolean isAttacking;
}

class Movement {
  private int direction;
  private double ratio;
  private boolean motion;

  public int getDirection() {
    return direction;
  }

  public void setDirection(int direction) {
    this.direction = direction;
  }

  public double getRatio() {
    return ratio;
  }

  public void setRatio(double ratio) {
    this.ratio = ratio;
  }

  public boolean isMotion() {
    return motion;
  }

  public void setMotion(boolean motion) {
    this.motion = motion;
  }
}

class JBase {
  private int x;
  private int y;
  private double health;
  private double maxHealth;

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public double getHealth() {
    return health;
  }

  public void setHealth(double health) {
    this.health = health;
  }

  public double getMaxHealth() {
    return maxHealth;
  }

  public void setMaxHealth(double maxHealth) {
    this.maxHealth = maxHealth;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  private String color;
  private String name;
}

class Statistics {
  private String name;
  private int resources;
  private int score;
  private int mineCost;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getResources() {
    return resources;
  }

  public void setResources(int resources) {
    this.resources = resources;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public int getMineCost() {
    return mineCost;
  }

  public void setMineCost(int mineCost) {
    this.mineCost = mineCost;
  }

  public int getWallCost() {
    return wallCost;
  }

  public void setWallCost(int wallCost) {
    this.wallCost = wallCost;
  }

  public int getAttacker1Cost() {
    return attacker1Cost;
  }

  public void setAttacker1Cost(int attacker1Cost) {
    this.attacker1Cost = attacker1Cost;
  }

  public int getTurret1Cost() {
    return turret1Cost;
  }

  public void setTurret1Cost(int turret1Cost) {
    this.turret1Cost = turret1Cost;
  }

  public List<Leaderboard> getLeaderboard() {
    return leaderboard;
  }

  public void setLeaderboard(List<Leaderboard> leaderboard) {
    this.leaderboard = leaderboard;
  }

  private int wallCost;
  private int attacker1Cost;
  private int turret1Cost;
  private List<Leaderboard> leaderboard;
}

class Leaderboard {
  private String name;
  private int score;
  private String color;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }
}
