package edu.brown.cs.stonefall.custom_exceptions;

import edu.brown.cs.stonefall.pathing.Vertex;

import java.util.Optional;

/**
 * Implementation of the Exception class for communicating unreachable
 * vertexes in graph searches.
 */
public class UnreachableVertexException extends Exception {
  private Optional<Vertex> closest;

  /**
   * Constructs an UnreachableVertexException with no closest Vertex. This
   * means there was no closest Vertex found in the graph algorithm,
   * and also that there was no valid edge connecting the destination Vertex
   * to other Vertexes.
   */
  public UnreachableVertexException() {
    super();
  }

  /**
   * Constructs an UnreachableVertexException with a message.
   * @param message The message to store in this Exception.
   */
  public UnreachableVertexException(String message) {
    super(message);
  }

  /**
   * Constructs an UnreachableVertexException with one of the closest Vertexes
   * to the unreachable vertex in graph search.
   * @param closest One of the closest Vertexes
   *                to the unreachable vertex in the graph search.
   */
  public UnreachableVertexException(Optional<Vertex> closest) {
    this.closest = closest;
  }

  /**
   * Returns one of the closest Vertexes to the unreachable vertex in the graph
   * search that threw this Exception.
   * @return The specified Vertex.
   */
  public Optional<Vertex> getClosest() {
    return closest;
  }
}
