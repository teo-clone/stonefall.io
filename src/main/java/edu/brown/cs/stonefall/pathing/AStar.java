package edu.brown.cs.stonefall.pathing;

import edu.brown.cs.stonefall.custom_exceptions.UnreachableVertexException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * A class representing an AStar search.
 *
 * @param <V>
 *          The vertex object for this AStar search.
 * @param <E>
 *          The edge object for this AStar search.
 *
 * @author doyeka
 */
public class AStar<V extends Vertex<V, E> & Cartesian, E extends Edge<V, E>> {
  /**
   * Returns the shortest path between two Vertexes.
   *
   * @param src
   *          The vertex to start from.
   * @param dest
   *          The vertex to find a path to.
   * @return A list of edges in between the two Vertexes representing the
   *         shortest path.
   * @throws UnreachableVertexException If dest is an UnreachableVertex in the
   * search.
   */
  public List<E> shortestPath(V src, V dest) throws UnreachableVertexException {
    Set<V> visited = new HashSet<>();
    HashMap<V, Double> distances = new HashMap<>();
    // key is the vertex, value is the edge you took to get to it
    HashMap<V, E> parents = new HashMap<>();
    // parent
    PriorityQueue<Pair<V>> toVisit = new PriorityQueue<>();
    List<E> path = new ArrayList<>();
    Vertex<V, E> closest = null;
    if (!dest.getEdges().isEmpty()) {
      closest = dest.getEdges().get(0).getSrc();
    }
    double closestDist = Double.MAX_VALUE;
    double distFromStart = Double.MAX_VALUE;

    distances.put(src, 0.0);
    parents.put(src, null);
    for (E edge : src.getEdges()) {
      V next = edge.getDest();
      double dist = edge.getWeight();
      double heuristic = next.getDistance(dest);

      toVisit.add(new Pair<>(next, dist + heuristic));
      distances.put(next, dist);
      parents.put(next, edge);
    }
    visited.add(src);
    V cur;

    while (!toVisit.isEmpty()) {
      cur = Objects.requireNonNull(toVisit.poll()).getItem();
      if (!visited.contains(cur)) {
        for (E edge : cur.getEdges()) {
          V next = edge.getDest();
          Double distSoFar = distances.get(cur);
          Double weight = edge.getWeight();
          Double nextDist = distances.get(next);
          Double newDist = distSoFar + weight;
          if (nextDist == null || newDist < nextDist) {
            toVisit.add(new Pair<>(next, newDist + next.getDistance(dest)));
            distances.put(next, newDist);
            parents.put(next, edge);
          }
          // Creating a backup destination.
          double heuristic = next.getDistance(dest);
          double newDistFromStart = next.getDistance(src);
          if (Double.compare(heuristic, closestDist) == -1
              || (Double.compare(heuristic, closestDist) == 0)
              && (Double.compare(newDistFromStart, distFromStart) == -1)) {
            closest = next;
            closestDist = heuristic;
            distFromStart = newDistFromStart;
          }
          if (cur.equals(dest)) {
            E edgeTaken = parents.get(cur);
            while (edgeTaken != null) {
              path.add(0, edgeTaken);
              cur = edgeTaken.getSrc();
              edgeTaken = parents.get(cur);
            }
            return path;
          }
          visited.add(cur);
        }
      }
    }
    /* If no path is found, we'll allow the caller to get to the closest
       possible Vertex to their location. NOTE: Closest could be null on sparse
       graphs.
    */
    if (closest != null) {
      throw new UnreachableVertexException(Optional.of(closest));
    } else {
      throw new UnreachableVertexException(Optional.empty());
    }
  }

  /**
   * A class to store values and priority for the AStar algorithm.
   *
   * @param <T>
   *          The item to order.
   */
  private static class Pair<T> implements Comparable<T> {
    private T item;
    private double priority;

    /**
     * Constructs a Pair Object.
     *
     * @param item
     *          The item to store in this Pair object.
     * @param priority
     *          The priority of the item.
     */
    Pair(T item, double priority) {
      this.item = item;
      this.priority = priority;
    }

    /**
     * Returns the item stored in this pair object.
     *
     * @return The item stored in this pair object.
     */
    T getItem() {
      return item;
    }

    /**
     * Returns the priority stored in this pair object.
     *
     * @return A double representing the priority of this item.
     */
    double getPriority() {
      return priority;
    }

    @Override
    public int compareTo(Object arg0) {
      Pair<T> other = (Pair<T>) arg0;
      return Double.compare(getPriority(), other.getPriority());
    }
  }
}
