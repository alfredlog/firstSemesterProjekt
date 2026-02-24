package hProjekt.model.grid;

import java.util.Set;

/**
 * Holds information on an edge connecting two tile centers.
 * An edge is defined by two adjacent {@link TilePosition}s.
 */
public interface Edge {

    /**
     * Returns the HexGrid instance this edge is placed in.
     *
     * @return the HexGrid instance this edge is placed in
     */
    HexGrid getHexGrid();

    /**
     * Returns the first position.
     *
     * @return the first position
     */
    TilePosition getPosition1();

    /**
     * Returns the second position.
     *
     * @return the second position
     */
    TilePosition getPosition2();

    /**
     * Returns {@code true} if the given edge connects to this edge and
     * {@code false} otherwise.
     *
     * @param other the other edge
     * @return whether the two edges are connected
     */
    boolean connectsTo(Edge other);

    /**
     * Returns the {@link TilePosition}s that this edge lies between.
     *
     * @return the adjacent tile positions
     */
    Set<TilePosition> getAdjacentTilePositions();

    /**
     * Returns all edges that connect to this edge in the grid.
     *
     * @return all edges connected to this one
     */
    Set<Edge> getConnectedEdges();
}
