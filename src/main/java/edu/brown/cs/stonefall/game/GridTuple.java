package edu.brown.cs.stonefall.game;

import edu.brown.cs.stonefall.interfaces.Killable;

/**
 * 
 */
public class GridTuple {

  public Killable killable;
  public String playerId;

  /**
   * 
   *
   */
    GridTuple(Killable killable, String playerId) {
        this.killable = killable;
        this.playerId = playerId;
    }

  /**
   * 
   */
    public synchronized Killable getKillable() {
        return killable;
    }

    public synchronized String getPlayerId() {
      return playerId;
    }

}
