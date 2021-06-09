
package edu.brown.cs.stonefall.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.HashSet;

import com.google.common.collect.ImmutableList;

import edu.brown.cs.stonefall.game.GridTuple;
import edu.brown.cs.stonefall.gamebean.AttackerBean;
import edu.brown.cs.stonefall.gamebean.BaseBean;
import edu.brown.cs.stonefall.gamebean.MineBean;
import edu.brown.cs.stonefall.gamebean.ResourceBean;
import edu.brown.cs.stonefall.gamebean.ScaffoldBean;
import edu.brown.cs.stonefall.gamebean.TurretBean;
import edu.brown.cs.stonefall.gamebean.WallBean;
import edu.brown.cs.stonefall.map.Grid;
import edu.brown.cs.stonefall.network.WebSockets;
import edu.brown.cs.stonefall.structure.Mine;
import edu.brown.cs.stonefall.structure.Resource;
import edu.brown.cs.stonefall.structure.Scaffold;
import edu.brown.cs.stonefall.structure.Turret;
import edu.brown.cs.stonefall.structure.Wall;
import edu.brown.cs.stonefall.structure.Base;
import edu.brown.cs.stonefall.interfaces.Killable;
import edu.brown.cs.stonefall.interfaces.Entity;
import edu.brown.cs.stonefall.interfaces.Attacker;
import edu.brown.cs.stonefall.pathing.Direction;


/**
 * A class representing the state of the Game class for one Player.
 */
public class GameState {

  private Game game;
  private Player me;

  private List<Player> leaders;

  private List<WallBean> walls;
  private List<TurretBean> turrets;
  private List<AttackerBean> attackers;
  private List<MineBean> mines;
  private List<ScaffoldBean> scaffolds;
  private List<BaseBean> bases;
  private List<ResourceBean> resources;

  private GridTuple[][] prevGrid; 
  private GridTuple[][] nextGrid; 

  /**
   * Constructs a GameState in the specified Game, for the specified Player.
   *
   * @param game
   *          The Game to associate this GameState with.
   * @param me
   *          The Player to associate this GameState with.
   */
  GameState(Game game, Player me) {
    this.game = game;
    this.me = me;

    walls = new ArrayList<>();
    attackers = new ArrayList<>();
    turrets = new ArrayList<>();
    mines = new ArrayList<>();
    scaffolds = new ArrayList<>();
    bases = new ArrayList<>();
    resources = new ArrayList<>();
    nextGrid = new GridTuple[Constants.BOARD_WIDTH][Constants.BOARD_HEIGHT]; 
    // fullUpdate();
    update();
  }

  // public synchronized void fullUpdate(){
  //   //update grids (hand over hand passoff)
  //   prevGrid = nextGrid;

  //   for (Player player : game.getPlayers().values()) {
  //     // if player is dead, tell frontend to terminate game for them
  //     if (player.isDead()) {
  //       WebSockets.gameOver(player.getId());
  //       game.removePlayer(player);
  //       continue;
  //     }

  //     //store the base
  //     Base playerBase = player.getBase();
  //     nextGrid[playerBase.getX()][playerBase.getY()] = new GridTuple(playerBase, player.getId());
  //     bases.add(new BaseBean(player));

  //     //store resources 
  //     for (Map.Entry<String, Resource> resource : game.getResources()
  //         .entrySet()) {
  //       resources.add(new ResourceBean(resource.getValue()));
  //     }

  //     //store walls
  //     for (Map.Entry<String, Wall> wall : player.getWalls().entrySet()) {
  //       if (player.getWalls().containsKey(wall.getKey())) {
  //         Wall playerWall = wall.getValue();
  //         nextGrid[playerWall.getX()][playerWall.getY()] = new GridTuple(playerWall, player.getId());

  //         walls.add(new WallBean(wall.getKey(),  player));

  //         if (wall.getValue().isDead()) {
  //           player.remove(Constants.OBJECT_TYPE.WALL.ordinal(),
  //               wall.getKey());
  //         }
  //       }
  //     }

  //     //store mines
  //     for (Map.Entry<String, Mine> mine : player.getMines()
  //         .entrySet()) {
  //       if (player.getMines().containsKey(mine.getKey())) {
  //         Mine playerMine = mine.getValue();
  //         nextGrid[playerMine.getX()][playerMine.getY()] = new GridTuple(playerMine, player.getId());

  //         mines.add(new MineBean(mine.getKey(), player));

  //         if (mine.getValue().isDead()) {
  //           player.remove(Constants.OBJECT_TYPE.MINE.ordinal(),
  //               mine.getKey());
  //         }
  //       }
  //     }

  //     //store scaffolds
  //     for (Map.Entry<String, Scaffold> scaffold : player.getScaffolds().entrySet()) {
  //       if (player.getScaffolds().containsKey(scaffold.getKey())) {
  //         Scaffold playerScaffold = scaffold.getValue();
  //         nextGrid[playerScaffold.getX()][playerScaffold.getY()] = new GridTuple(playerScaffold, player.getId());

  //         scaffolds.add(new ScaffoldBean(scaffold.getKey(), player));
  //         scaffold.getValue().update();

  //         if (scaffold.getValue().isDead()) {
  //           player.remove(Constants.OBJECT_TYPE.SCAFFOLD.ordinal(),
  //               scaffold.getKey());
  //         }
  //       }
  //     }

  //     //store turrets 
  //     for (Map.Entry<String, Turret> turret : player.getTurrets().entrySet()) {
  //       if (player.getTurrets().containsKey(turret.getKey())) {
  //         Turret playerTurret = turret.getValue();
  //         nextGrid[playerTurret.getX()][playerTurret.getY()] = new GridTuple(playerTurret, player.getId());

  //         turrets.add(new TurretBean(turret.getKey(), player));

  //         turretAttack(turret.getValue(), player);
  //         if (turret.getValue().isDead()) {
  //           player.remove(Constants.OBJECT_TYPE.TURRET.ordinal(),
  //               turret.getKey());
  //         }
  //       }
  //     }

  //     //store attackers
  //     player.getAttackers().forEach((key, attacker) -> {
  //       if (player.getAttackers().containsKey(key)) {
  //         nextGrid[attacker.getX()][attacker.getY()] = new GridTuple(attacker, player.getId());
  //         //if attacker isn't doing anything, autoattack
  //         if(!attacker.inMotion() && !attacker.getAttackStatus() && !attacker.getTarget().isPresent()){
  //           //set direction
  //           //set target
  //           Optional<Killable> target = getAttackableInRange(attacker, Constants.MELEE_ATTACKER_RANGE, player.getId());
  //           if(target.isPresent() && target.get().getBlock().isFull()){
  //             attacker.setDirection(Direction.findDirection(attacker.getBlock(), ((Entity) target.get()).getBlock()));
  //             attacker.setTarget(Optional.of(target.get()));
  //           }
  //         } 
  //         //then do the standard stuff teo & david wrote
  //         if (attacker.inMotion()) {
  //           attacker.updateMotion();
  //         }
  //         // System.out.println("update: set attack status --> false"); 
  //         attacker.setAttackStatus(false);
  //         if (player.validateTarget(attacker.getTarget(), attacker)) {
  //           attacker.attack();
  //           attacker.setAttackStatus(true);
  //           if (attacker.getTarget().get().isDead()) {
  //             player.setResourceCount(
  //                 player.getResourceCount() + player.multiplyByScoreLogistically(
  //                     attacker.getTarget().get().getReward()));
  //             attacker.setAttackStatus(false);
  //             attacker.setTarget(Optional.empty());
  //           }
  //         } else {
  //           if (attacker.getTarget().isPresent()
  //               && attacker.getTarget().get() instanceof Attacker
  //               && !((Attacker) attacker.getTarget().get()).inMotion()
  //               && !Grid.isWithinNBlocks(Constants.MELEE_ATTACKER_RANGE,
  //                   attacker.getBlock(),
  //                   attacker.getTarget().get().getBlock())
  //               && !attacker.inMotion()) {
  //             attacker.startChase((Attacker) attacker.getTarget().get());
  //           } 
  //         }

  //         attackers.add(new AttackerBean(key,  player));

  //         if (attacker.isDead()) {
  //           player.remove(Constants.OBJECT_TYPE.ATTACKER.ordinal(),
  //               key);
  //         }
  //       }
  //     });


  //   }
  // }





  /**
   * Updates all Entities for this GameState.
   */
  public synchronized void update() {
    //update grids
    prevGrid = nextGrid;
    nextGrid = new GridTuple[Constants.BOARD_WIDTH][Constants.BOARD_HEIGHT]; 

    //new object lists
    walls = new ArrayList<>();
    attackers = new ArrayList<>();
    turrets = new ArrayList<>();
    mines = new ArrayList<>();
    scaffolds = new ArrayList<>();
    bases = new ArrayList<>();
    resources = new ArrayList<>();
    leaders = new ArrayList<>();
    // updateBases();
    updateResources();
    updateBases();
    updateWalls();
    updateScaffolds();
    updateAttackers();
    updateTurrets();
    updateMines();
    updateLeaders();
  }


  // public synchronized void addBase(Player player){
  //   System.out.println("base added");
  //   bases.add(new BaseBean(player));
  // }
  

  //check for dead bases 
  private synchronized void updateBases() {
    // System.out.println("update bases");
    for (Player player : game.getPlayers().values()) {
      Base playerBase = player.getBase();
      nextGrid[playerBase.getX()][playerBase.getY()] = new GridTuple(playerBase, player.getId());
      // if player is dead, tell frontend to terminate game for them, and remove their base from the game state
      if (player.isDead()) {
        WebSockets.gameOver(player.getId());
        game.removePlayer(player);
        bases.remove(new BaseBean(player));
        continue;
      }
      bases.add(new BaseBean(player));
    }
  }


  private synchronized void updateLeaders() {
    // priority queue based on player scores
    PriorityQueue<Player> pq = new PriorityQueue<>(Constants.LEADERBOARD_SIZE,
        Comparator.comparing((Player p) -> (p.getScore())));
    game.getPlayers().forEach((key, thisPlayer) -> {
      pq.add(thisPlayer);
      if (pq.size() > Constants.LEADERBOARD_SIZE) {
        pq.poll();
      }
    });

    while (!pq.isEmpty()) {
      leaders.add(pq.poll());
    }
    Collections.reverse(leaders);
  }

  private synchronized void updateResources() {
    for (Map.Entry<String, Resource> resource : game.getResources()
        .entrySet()) {
      resources.add(new ResourceBean(resource.getValue()));
    }
  }



  private synchronized void updateScaffolds() {
    game.getPlayers().forEach((key, player) -> {
      for (Map.Entry<String, Scaffold> scaffold : player.getScaffolds()
          .entrySet()) {
        if (player.getScaffolds().containsKey(scaffold.getKey())) {
          Scaffold playerScaffold = scaffold.getValue();
          nextGrid[playerScaffold.getX()][playerScaffold.getY()] = new GridTuple(playerScaffold, player.getId());

          scaffolds.add(new ScaffoldBean(scaffold.getKey(), player));
          scaffold.getValue().update();

          if (scaffold.getValue().isDead()) {
            player.remove(Constants.OBJECT_TYPE.SCAFFOLD.ordinal(),
                scaffold.getKey());
          }
        }
      }
    });
  }

  private synchronized void updateWalls() {
    for (Map.Entry<String, Player> player : game.getPlayers().entrySet()) {
      for (Map.Entry<String, Wall> wall : player.getValue().getWalls()
          .entrySet()) {
        if (player.getValue().getWalls().containsKey(wall.getKey())) {
          Wall playerWall = wall.getValue();
          nextGrid[playerWall.getX()][playerWall.getY()] = new GridTuple(playerWall, player.getValue().getId());

          walls.add(new WallBean(wall.getKey(),  player.getValue()));

          if (wall.getValue().isDead()) {
            player.getValue().remove(Constants.OBJECT_TYPE.WALL.ordinal(),
                wall.getKey());
          }
        }
      }
    }
  }

  private synchronized void updateMines() {
    for (Map.Entry<String, Player> player : game.getPlayers().entrySet()) {
      for (Map.Entry<String, Mine> mine : player.getValue().getMines()
          .entrySet()) {
        if (player.getValue().getMines().containsKey(mine.getKey())) {
          Mine playerMine = mine.getValue();
          nextGrid[playerMine.getX()][playerMine.getY()] = new GridTuple(playerMine, player.getValue().getId());

          mines.add(new MineBean(mine.getKey(), player.getValue()));

          if (mine.getValue().isDead()) {
            player.getValue().remove(Constants.OBJECT_TYPE.MINE.ordinal(),
                mine.getKey());
          }
        }
      }
    }
  }

  private synchronized void updateAttackers() {
    for (Map.Entry<String, Player> player : game.getPlayers().entrySet()) {
      player.getValue().getAttackers().forEach((key, attacker) -> {
        if (player.getValue().getAttackers().containsKey(key)) {
          nextGrid[attacker.getX()][attacker.getY()] = new GridTuple(attacker, player.getValue().getId());
          //if attacker isn't doing anything, autoattack
                    //set target
          // if(attacker.getTarget().isPresent()){
          //   System.out.println("attacker target" + attacker.getTarget().isPresent());
          //   System.out.println("attaker target is: " + attacker.getTarget());
          // }
          // if(attacker.inMotion()){
          //   System.out.println("attacker motion" + attacker.inMotion());
          // }
          // if(attacker.getAttackStatus()){
          //   System.out.println("attacker status" + attacker.getAttackStatus());
          // }
          if(!attacker.inMotion() && !attacker.getAttackStatus() && !attacker.getTarget().isPresent()){
            //set direction
            Optional<Killable> target = getAttackableInRange(attacker, Constants.MELEE_ATTACKER_RANGE, player.getValue().getId());
            if(target.isPresent() && target.get().getBlock().isFull()){
              attacker.setDirection(Direction.findDirection(attacker.getBlock(), ((Entity) target.get()).getBlock()));
              attacker.setTarget(Optional.of(target.get()));
            }
          } 
          //then do the standard stuff teo & david wrote
          if (attacker.inMotion()) {
            attacker.updateMotion();
          }
          // System.out.println("update: set attack status --> false"); 
          attacker.setAttackStatus(false);
          if (player.getValue().validateTarget(attacker.getTarget(), attacker)) {
            attacker.attack();
            attacker.setAttackStatus(true);
            if (attacker.getTarget().get().isDead()) {
              player.getValue().setResourceCount(
                  player.getValue().getResourceCount() + player.getValue().multiplyByScoreLogistically(
                      attacker.getTarget().get().getReward()));
              attacker.setAttackStatus(false);
              attacker.setTarget(Optional.empty());
            }
          } else {
            if (attacker.getTarget().isPresent()
                && attacker.getTarget().get() instanceof Attacker
                && !((Attacker) attacker.getTarget().get()).inMotion()
                && !Grid.isWithinNBlocks(Constants.MELEE_ATTACKER_RANGE,
                    attacker.getBlock(),
                    attacker.getTarget().get().getBlock())
                && !attacker.inMotion()) {
              attacker.startChase((Attacker) attacker.getTarget().get());
            } 
          }

          attackers.add(new AttackerBean(key,  player.getValue()));

          if (attacker.isDead()) {
            player.getValue().remove(Constants.OBJECT_TYPE.ATTACKER.ordinal(),
                key);
          }
        }
      });
    }
  }

  private synchronized void updateTurrets() {

    for (Map.Entry<String, Player> player : game.getPlayers().entrySet()) {
      for (Map.Entry<String, Turret> turret : player.getValue().getTurrets()
          .entrySet()) {
        if (player.getValue().getTurrets().containsKey(turret.getKey())) {
          Turret playerTurret = turret.getValue();
          nextGrid[playerTurret.getX()][playerTurret.getY()] = new GridTuple(playerTurret, player.getValue().getId());

          turrets.add(new TurretBean(turret.getKey(), player.getValue()));

          turretAttack(turret.getValue(), player.getValue());
          if (turret.getValue().isDead()) {
            player.getValue().remove(Constants.OBJECT_TYPE.TURRET.ordinal(),
                turret.getKey());
          }
        }
      }
    }
  }

  private synchronized void turretAttack(Turret turret, Player owner) {
    Optional<Killable> target = getAttackableInRange(turret, Constants.TURRET_RANGE, owner.getId());
    if(target.isPresent()){
      turret.attack(target.get());
      turret.setAttackStatus(true);
      turret.setTarget(target);
      if(target.get().isDead()){
        turret.setAttackStatus(false);
        turret.setTarget(Optional.empty());
        owner.setResourceCount(owner.getResourceCount() + owner.multiplyByScoreLogistically(target.get().getReward()));
      }
    } else {
      turret.setAttackStatus(false);
      turret.setTarget(Optional.empty());
    }

  }


  //prefers turrets
  //then attackers
  //then equal
  private synchronized Optional<Killable> getAttackableInRange(
      Killable k, int range, String player) {
    int offset = 1;
    int x = k.getX();
    int y = k.getY();
    Optional<Killable> potentialTarget = Optional.empty();
    Optional<Killable> newTarget = Optional.empty();
    while (offset <= range) {
      for (int i = -offset + x; i < 1 + offset + x; i++) {
        for (int j = -offset + y; j < 1 + offset + y; j++) {
          if(i < Constants.BOARD_WIDTH && j < Constants.BOARD_HEIGHT && i > 0 && j > 0){
            if(prevGrid[i][j] != null){
              if(!prevGrid[i][j].getKillable().isDead() 
                && !(prevGrid[i][j].getKillable() instanceof Resource) 
                && !player.equals(prevGrid[i][j].getPlayerId())){
                newTarget = Optional.of((Killable) prevGrid[i][j].getKillable());
                if(potentialTarget.isPresent()){
                  if((potentialTarget.get() instanceof Turret)){
                    return potentialTarget;
                  }
                  if(!(potentialTarget.get() instanceof Attacker)){
                    potentialTarget = newTarget;
                  }
                } else {
                  potentialTarget = newTarget;
                }

              }
            }
          }
        }
      }
      offset++;
    }
    return potentialTarget;
  }

  /**
   * Returns the List of score leaders.
   *
   * @return The list of score leaders.
   */
  public List<Player> getLeaders() {
    return leaders;
  }

  /**
   * Returns the List of this player's WallBeans for this GameState.
   *
   * @return The List of this player's WallBeans for this GameState.
   */
  public synchronized List<WallBean> getWalls() {
    return ImmutableList.copyOf(walls);
  }

  /**
   * Returns the List of this player's AttackerBeans for this GameState.
   *
   * @return The List of this player's AttackerBeans for this GameState.
   */
  public synchronized List<AttackerBean> getAttackers() {
    return ImmutableList.copyOf(attackers);
  }

  /**
   * Returns the List of this player's TurretBeans for this GameState.
   *
   * @return The List of this player's TurretBeans for this GameState.
   */
  public synchronized List<TurretBean> getTurrets() {
    return ImmutableList.copyOf(turrets);
  }

  /**
   * Returns the List of this player's MineBeans for this GameState.
   *
   * @return The List of this player's MineBeans for this GameState.
   */
  public synchronized List<MineBean> getMines() {
    return ImmutableList.copyOf(mines);
  }

  /**
   * Returns the List of enemy players' BaseBeans for this GameState.
   *
   * @return The List of enemy players' BaseBeans for this GameState.
   */
  public synchronized List<BaseBean> getBases() {
    return ImmutableList.copyOf(bases);
  }

  /**
   * Returns the List of ResourceBeans for this GameState.
   *
   * @return The List of ResourceBeans for this GameState.
   */
  public synchronized List<ResourceBean> getResources() {
    return ImmutableList.copyOf(resources);
  }

  /**
   * Returns the List of this player's ScaffoldBeans for this GameState.
   *
   * @return The List of this player's ScaffoldBeans for this GameState.
   */
  public synchronized List<ScaffoldBean> getScaffolds() {
    return ImmutableList.copyOf(scaffolds);
  }

}










// --------------------------------------------------------------------------
// --------------------------------------------------------------------------
// --------------------------------------------------------------------------
// --------------------------------------------------------------------------
// --------------------------------------------------------------------------
// --------------------------------------------------------------------------
// --------------------------------------------------------------------------
// --------------------------------------------------------------------------
// --------------------------------------------------------------------------
// --------------------------------------------------------------------------










// package edu.brown.cs.stonefall.game;

// import java.util.ArrayList;
// import java.util.Collections;
// import java.util.Comparator;
// import java.util.List;
// import java.util.Map;
// import java.util.Optional;
// import java.util.PriorityQueue;

// import com.google.common.collect.ImmutableList;

// import edu.brown.cs.stonefall.game.GridTuple;
// import edu.brown.cs.stonefall.gamebean.AttackerBean;
// import edu.brown.cs.stonefall.gamebean.BaseBean;
// import edu.brown.cs.stonefall.gamebean.MineBean;
// import edu.brown.cs.stonefall.gamebean.PlayerStatsBean;
// import edu.brown.cs.stonefall.gamebean.ResourceBean;
// import edu.brown.cs.stonefall.gamebean.ScaffoldBean;
// import edu.brown.cs.stonefall.gamebean.TurretBean;
// import edu.brown.cs.stonefall.gamebean.WallBean;
// import edu.brown.cs.stonefall.map.Grid;
// import edu.brown.cs.stonefall.network.WebSockets;
// import edu.brown.cs.stonefall.structure.Mine;
// import edu.brown.cs.stonefall.structure.Resource;
// import edu.brown.cs.stonefall.structure.Scaffold;
// import edu.brown.cs.stonefall.structure.Turret;
// import edu.brown.cs.stonefall.structure.Wall;
// import edu.brown.cs.stonefall.structure.Base;
// import edu.brown.cs.stonefall.interfaces.Killable;
// import edu.brown.cs.stonefall.interfaces.Entity;
// import edu.brown.cs.stonefall.interfaces.Attacker;
// import edu.brown.cs.stonefall.pathing.Direction;


// /**
//  * A class representing the state of the Game class for one Player.
//  */
// public class GameState {

//   private Game game;
//   private Player me;

//   private List<Player> leaders;

//   private PlayerStatsBean stats;

//   private List<WallBean> myWalls;
//   private List<WallBean> enemyWalls;

//   private List<TurretBean> myTurrets;
//   private List<TurretBean> enemyTurrets;

//   private List<AttackerBean> myAttackers;
//   private List<AttackerBean> enemyAttackers;

//   private List<MineBean> myMines;
//   private List<MineBean> enemyMines;

//   private List<ScaffoldBean> myScaffolds;
//   private List<ScaffoldBean> enemyScaffolds;

//   private BaseBean myBase;
//   private List<BaseBean> enemyBases;

//   private List<ResourceBean> resources;

//   private GridTuple[][] prevGrid; 
//   private GridTuple[][] nextGrid; 

//   /**
//    * Constructs a GameState in the specified Game, for the specified Player.
//    *
//    * @param game
//    *          The Game to associate this GameState with.
//    * @param me
//    *          The Player to associate this GameState with.
//    */
//   GameState(Game game, Player me) {
//     this.game = game;
//     this.me = me;

//     // stats = new PlayerStatsBean(me);

//     myWalls = new ArrayList<>();
//     enemyWalls = new ArrayList<>();

//     myAttackers = new ArrayList<>();
//     enemyAttackers = new ArrayList<>();

//     myTurrets = new ArrayList<>();
//     enemyTurrets = new ArrayList<>();

//     myMines = new ArrayList<>();
//     enemyMines = new ArrayList<>();

//     myScaffolds = new ArrayList<>();
//     enemyScaffolds = new ArrayList<>();

//     myBase = null; 
//     enemyBases = new ArrayList<>();

//     resources = new ArrayList<>();

//     nextGrid = new GridTuple[Constants.BOARD_WIDTH][Constants.BOARD_HEIGHT]; 


//     update();
//   }

//   /**
//    * Updates all Entities for this GameState.
//    */
//   public synchronized void update() {
//     //update grids
//     prevGrid = nextGrid;
//     nextGrid = new GridTuple[Constants.BOARD_WIDTH][Constants.BOARD_HEIGHT]; 

//     //new object lists
//     myWalls = new ArrayList<>();
//     enemyWalls = new ArrayList<>();
//     myAttackers = new ArrayList<>();
//     enemyAttackers = new ArrayList<>();
//     myTurrets = new ArrayList<>();
//     enemyTurrets = new ArrayList<>();
//     myMines = new ArrayList<>();
//     enemyMines = new ArrayList<>();
//     myScaffolds = new ArrayList<>();
//     enemyScaffolds = new ArrayList<>();
//     enemyBases = new ArrayList<>();
//     resources = new ArrayList<>();
//     leaders = new ArrayList<>();
//     // updateStats();
//     updateBases();
//     updateResources();
//     updateWalls();
//     updateScaffolds();
//     updateAttackers();
//     updateTurrets();
//     updateMines();
//     updateLeaders();
//   }

//   private synchronized void updateLeaders() {
//     // priority queue based on player scores
//     PriorityQueue<Player> pq = new PriorityQueue<>(Constants.LEADERBOARD_SIZE,
//         Comparator.comparing((Player p) -> (p.getScore())));
//     game.getPlayers().forEach((key, thisPlayer) -> {
//       pq.add(thisPlayer);
//       if (pq.size() > Constants.LEADERBOARD_SIZE) {
//         pq.poll();
//       }
//     });

//     while (!pq.isEmpty()) {
//       leaders.add(pq.poll());
//     }
//     Collections.reverse(leaders);
//   }

//   private synchronized void updateStats() {
//     stats.update();
//   }

//   private synchronized void updateResources() {
//     for (Map.Entry<String, Resource> resource : game.getResources()
//         .entrySet()) {
//       resources.add(new ResourceBean(resource.getValue()));
//     }
//   }

//   private synchronized void updateBases() {
//     if(myBase == null){
//       myBase = new BaseBean(me);
//     }

//     for (Player player : game.getPlayers().values()) {
//       Base playerBase = player.getBase();
//       nextGrid[playerBase.getX()][playerBase.getY()] = new GridTuple(playerBase, player.getId());

//       // if player is dead, tell frontend to terminate game for them
//       if (player.isDead()) {
//         WebSockets.gameOver(player.getId());
//         game.removePlayer(player);
//         continue;
//       }

//       if (!player.equals(me)) {
//         enemyBases.add(new BaseBean(player));
//       }
//     }
//   }

//   private synchronized void updateScaffolds() {
//     game.getPlayers().forEach((key, player) -> {
//       for (Map.Entry<String, Scaffold> scaffold : player.getScaffolds()
//           .entrySet()) {
//         if (player.getScaffolds().containsKey(scaffold.getKey())) {
//           Scaffold playerScaffold = scaffold.getValue();
//           nextGrid[playerScaffold.getX()][playerScaffold.getY()] = new GridTuple(playerScaffold, player.getId());

//           if (player.equals(me)) {
//             myScaffolds.add(new ScaffoldBean(scaffold.getKey(), player));
//           } else {
//             enemyScaffolds.add(new ScaffoldBean(scaffold.getKey(), player));
//           }
//           scaffold.getValue().update();

//           if (scaffold.getValue().isDead()) {
//             player.remove(Constants.OBJECT_TYPE.SCAFFOLD.ordinal(),
//                 scaffold.getKey());
//           }
//         }
//       }
//     });
//   }

//   private synchronized void updateWalls() {
//     for (Map.Entry<String, Player> player : game.getPlayers().entrySet()) {
//       for (Map.Entry<String, Wall> wall : player.getValue().getWalls()
//           .entrySet()) {
//         if (player.getValue().getWalls().containsKey(wall.getKey())) {
//           Wall playerWall = wall.getValue();
//           nextGrid[playerWall.getX()][playerWall.getY()] = new GridTuple(playerWall, player.getValue().getId());

//           if (player.getValue().equals(me)) {
//             myWalls.add(new WallBean(wall.getKey(), me));
//           } else {
//             enemyWalls.add(new WallBean(wall.getKey(), player.getValue()));
//           }

//           if (wall.getValue().isDead()) {
//             player.getValue().remove(Constants.OBJECT_TYPE.WALL.ordinal(),
//                 wall.getKey());
//           }
//         }
//       }
//     }
//   }

//   private synchronized void updateMines() {
//     for (Map.Entry<String, Player> player : game.getPlayers().entrySet()) {
//       for (Map.Entry<String, Mine> mine : player.getValue().getMines()
//           .entrySet()) {
//         if (player.getValue().getMines().containsKey(mine.getKey())) {
//           Mine playerMine = mine.getValue();
//           nextGrid[playerMine.getX()][playerMine.getY()] = new GridTuple(playerMine, player.getValue().getId());

//           if (player.getValue().equals(me)) {
//             myMines.add(new MineBean(mine.getKey(), player.getValue()));
//           } else {
//             enemyMines.add(new MineBean(mine.getKey(), player.getValue()));
//           }

//           if (mine.getValue().isDead()) {
//             player.getValue().remove(Constants.OBJECT_TYPE.MINE.ordinal(),
//                 mine.getKey());
//           }
//         }
//       }
//     }
//   }

//   private synchronized void updateAttackers() {
//     for (Map.Entry<String, Player> player : game.getPlayers().entrySet()) {
//       player.getValue().getAttackers().forEach((key, attacker) -> {
//         if (player.getValue().getAttackers().containsKey(key)) {
//           nextGrid[attacker.getX()][attacker.getY()] = new GridTuple(attacker, player.getValue().getId());
//           //if attacker isn't doing anything, autoattack
//           if(!attacker.inMotion() && !attacker.getAttackStatus() && !attacker.getTarget().isPresent()){
//             //set direction
//             //set target
//             Optional<Killable> target = getAttackableInRange(attacker, Constants.MELEE_ATTACKER_RANGE, player.getValue().getId());
//             if(target.isPresent() && target.get().getBlock().isFull()){
//               attacker.setDirection(Direction.findDirection(attacker.getBlock(), ((Entity) target.get()).getBlock()));
//               attacker.setTarget(Optional.of(target.get()));
//             }
//           } 
//           //then do the standard stuff teo & david wrote
//           if (attacker.inMotion()) {
//             attacker.updateMotion();
//           }
//           // System.out.println("update: set attack status --> false"); 
//           attacker.setAttackStatus(false);
//           if (player.getValue().validateTarget(attacker.getTarget(), attacker)) {
//             attacker.attack();
//             attacker.setAttackStatus(true);
//             if (attacker.getTarget().get().isDead()) {
//               player.getValue().setResourceCount(
//                   player.getValue().getResourceCount() + player.getValue().multiplyByScoreLogistically(
//                       attacker.getTarget().get().getReward()));
//               attacker.setAttackStatus(false);
//               attacker.setTarget(Optional.empty());
//             }
//           } else {
//             if (attacker.getTarget().isPresent()
//                 && attacker.getTarget().get() instanceof Attacker
//                 && !((Attacker) attacker.getTarget().get()).inMotion()
//                 && !Grid.isWithinNBlocks(Constants.MELEE_ATTACKER_RANGE,
//                     attacker.getBlock(),
//                     attacker.getTarget().get().getBlock())
//                 && !attacker.inMotion()) {
//               attacker.startChase((Attacker) attacker.getTarget().get());
//             } 
//           }
//           // }




//         //   //if the attacker ain't already attacking somebody, autoattack nearby objects
//         //   if(!attacker.getAttackStatus()){
//         //     Optional<Killable> target = getAttackableInRange(attacker, Constants.MELEE_ATTACKER_RANGE, player.getValue().getId());
//         //     if(target.isPresent()){
//         //       attacker.startCharge(target.get());
//         // //             if (Grid.isWithinNBlocks(Constants.MELEE_ATTACKER_RANGE,
//         // //   attacker.getBlock(), target) && target.isFull()
//         // //   && target.getEntity() instanceof Killable
//         // //   && !(target.getEntity() instanceof Resource)) {
//         // // attacker
//         // //     .setDirection(Direction.findDirection(attacker.getBlock(), target));
//         // // attacker.setTarget(Optional.of((Killable) target.getEntity()));
//         //       // attacker.setDirection(Direction.findDirection(attacker.getBlock(), ((Entity) target.get()).getBlock()));
//         //       // attacker.setTarget(target);
//         //       // attacker.attack();
//         //       // attacker.setAttackStatus(true);
//         //     }
//         //   }

//           if (player.getValue().equals(me)) {
//             myAttackers.add(new AttackerBean(key, me));
//           } else {
//             enemyAttackers.add(new AttackerBean(key, player.getValue()));
//           }

//           if (attacker.isDead()) {
//             player.getValue().remove(Constants.OBJECT_TYPE.ATTACKER.ordinal(),
//                 key);
//           }
//         }
//       });
//     }
//   }

//   private synchronized void updateTurrets() {

//     for (Map.Entry<String, Player> player : game.getPlayers().entrySet()) {
//       for (Map.Entry<String, Turret> turret : player.getValue().getTurrets()
//           .entrySet()) {
//         if (player.getValue().getTurrets().containsKey(turret.getKey())) {
//           Turret playerTurret = turret.getValue();
//           nextGrid[playerTurret.getX()][playerTurret.getY()] = new GridTuple(playerTurret, player.getValue().getId());

//           if (player.getValue().equals(me)) {
//             myTurrets.add(new TurretBean(turret.getKey(), me));
//           } else {
//             enemyTurrets.add(new TurretBean(turret.getKey(), player.getValue()));
//           }
//           turretAttack(turret.getValue(), player.getValue());
//           if (turret.getValue().isDead()) {
//             player.getValue().remove(Constants.OBJECT_TYPE.TURRET.ordinal(),
//                 turret.getKey());
//           }
//         }
//       }
//     }
//   }

//   private synchronized void turretAttack(Turret turret, Player owner) {
//     Optional<Killable> target = getAttackableInRange(turret, Constants.TURRET_RANGE, owner.getId());
//     if(target.isPresent()){
//       turret.attack(target.get());
//       turret.setAttackStatus(true);
//       turret.setTarget(target);
//       if(target.get().isDead()){
//         turret.setAttackStatus(false);
//         turret.setTarget(Optional.empty());
//         owner.setResourceCount(owner.getResourceCount() + owner.multiplyByScoreLogistically(target.get().getReward()));
//       }
//     } else {
//       turret.setAttackStatus(false);
//       turret.setTarget(Optional.empty());
//     }

//     // System.out.println("turret block entity: " + turret.getBlock().getEntity());
//     // boolean attacked = false;

//     // for (Player player : game.getPlayers().values()) {
//     //   if (!player.getId().equals(owner)) {
//     //     for (Attacker attacker : player.getAttackers().values()) {
//     //       turret.setAttackStatus(false);
//     //       if (Grid.isWithinNBlocks(Constants.TURRET_RANGE, attacker.getBlock(),
//     //           turret.getBlock())) {
//     //         turret.attack(attacker);
//     //         turret.setAttackStatus(true);
//     //         turret.setTarget(Optional.of(attacker));
//     //         attacked = true;
//     //         turret.setAttackStatus(true);
//     //         turret.setTarget(Optional.of(attacker));
//     //         if (attacker.isDead()) {
//     //           turret.setAttackStatus(false);
//     //           turret.setTarget(Optional.empty());
//     //           me.setResourceCount(me.getResourceCount()
//     //               + me.multiplyByScoreLogistically(attacker.getReward()));
//     //         }
//     //         break;
//     //       }
//     //     }
//     //     if (attacked) {
//     //       return;
//     //     }
//     //   }
//     // }
//     // turret.setAttackStatus(false);
//     // turret.setTarget(Optional.empty());
//   }


//   //prefers turrets
//   //then attackers
//   //then equal
//   private synchronized Optional<Killable> getAttackableInRange(
//       Killable k, int range, String player) {
//     int offset = 1;
//     int x = k.getX();
//     int y = k.getY();
//     Optional<Killable> potentialTarget = Optional.empty();
//     Optional<Killable> newTarget = Optional.empty();
//     while (offset <= range) {
//       for (int i = -offset + x; i < 1 + offset + x; i++) {
//         for (int j = -offset + y; j < 1 + offset + y; j++) {
//           if(i < Constants.BOARD_WIDTH && j < Constants.BOARD_HEIGHT && i > 0 && j > 0){
//             if(prevGrid[i][j] != null){
//               if(!prevGrid[i][j].getKillable().isDead() 
//                 && !(prevGrid[i][j].getKillable() instanceof Resource) 
//                 && !player.equals(prevGrid[i][j].getPlayerId())){
//                 newTarget = Optional.of((Killable) prevGrid[i][j].getKillable());
//                 if(potentialTarget.isPresent()){
//                   if((potentialTarget.get() instanceof Turret)){
//                     return potentialTarget;
//                   }
//                   if(!(potentialTarget.get() instanceof Attacker)){
//                     potentialTarget = newTarget;
//                   }
//                 } else {
//                   potentialTarget = newTarget;
//                 }

//               }
//             }
//           }
//         }
//       }
//       offset++;
//     }
//     return potentialTarget;
//   }

//   /**
//    * Returns the List of score leaders.
//    *
//    * @return The list of score leaders.
//    */
//   public List<Player> getLeaders() {
//     return leaders;
//   }

//   /**
//    * Returns the PlayerStatsBean for this GameState.
//    *
//    * @return The PlayerStatsBean for this GameState.
//    */
//   public synchronized PlayerStatsBean getPlayerStats() {
//     return stats;
//   }

//   /**
//    * Returns the List of this player's WallBeans for this GameState.
//    *
//    * @return The List of this player's WallBeans for this GameState.
//    */
//   public synchronized List<WallBean> getMyWalls() {
//     return ImmutableList.copyOf(myWalls);
//   }

//   /**
//    * Returns the List of enemy players' WallBeans for this GameState.
//    *
//    * @return The List of enemy players' WallBeans for this GameState.
//    */
//   public synchronized List<WallBean> getEnemyWalls() {
//     return ImmutableList.copyOf(enemyWalls);
//   }

//   /**
//    * Returns the List of this player's AttackerBeans for this GameState.
//    *
//    * @return The List of this player's AttackerBeans for this GameState.
//    */
//   public synchronized List<AttackerBean> getMyAttackers() {
//     return ImmutableList.copyOf(myAttackers);
//   }

//   /**
//    * Returns the List of enemy players' AttackerBeans for this GameState.
//    *
//    * @return The List of enemy players' AttackerBeans for this GameState.
//    */
//   public synchronized List<AttackerBean> getEnemyAttackers() {
//     return ImmutableList.copyOf(enemyAttackers);
//   }

//   /**
//    * Returns the List of this player's TurretBeans for this GameState.
//    *
//    * @return The List of this player's TurretBeans for this GameState.
//    */
//   public synchronized List<TurretBean> getMyTurrets() {
//     return ImmutableList.copyOf(myTurrets);
//   }

//   /**
//    * Returns the List of enemy players' TurretBeans for this GameState.
//    *
//    * @return The List of enemy players' TurretBeans for this GameState.
//    */
//   public synchronized List<TurretBean> getEnemyTurrets() {
//     return ImmutableList.copyOf(enemyTurrets);
//   }

//   /**
//    * Returns the List of this player's MineBeans for this GameState.
//    *
//    * @return The List of this player's MineBeans for this GameState.
//    */
//   public synchronized List<MineBean> getMyMines() {
//     return ImmutableList.copyOf(myMines);
//   }

//   /**
//    * Returns the List of enemy players' MineBeans for this GameState.
//    *
//    * @return The List of enemy players' MineBeans for this GameState.
//    */
//   public synchronized List<MineBean> getEnemyMines() {
//     return ImmutableList.copyOf(enemyMines);
//   }

//   /**
//    * Returns the BaseBean for this Player.
//    *
//    * @return The BaseBean for this Player.
//    */
//   public synchronized BaseBean getMyBase() {
//     return myBase;
//   }

//   /**
//    * Returns the List of enemy players' BaseBeans for this GameState.
//    *
//    * @return The List of enemy players' BaseBeans for this GameState.
//    */
//   public synchronized List<BaseBean> getEnemyBases() {
//     return ImmutableList.copyOf(enemyBases);
//   }

//   /**
//    * Returns the List of ResourceBeans for this GameState.
//    *
//    * @return The List of ResourceBeans for this GameState.
//    */
//   public synchronized List<ResourceBean> getResources() {
//     return ImmutableList.copyOf(resources);
//   }

//   /**
//    * Returns the List of this player's ScaffoldBeans for this GameState.
//    *
//    * @return The List of this player's ScaffoldBeans for this GameState.
//    */
//   public synchronized List<ScaffoldBean> getMyScaffolds() {
//     return ImmutableList.copyOf(myScaffolds);
//   }

//   /**
//    * Returns the List of enemy players' ScaffoldBeans for this GameState.
//    *
//    * @return The List of enemy players' ScaffoldBeans for this GameState.
//    */
//   public synchronized List<ScaffoldBean> getEnemyScaffolds() {
//     return ImmutableList.copyOf(enemyScaffolds);
//   }
// }
