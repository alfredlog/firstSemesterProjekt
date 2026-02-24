package hProjekt.model.grid;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

import org.jetbrains.annotations.Nullable;
import org.tudalgo.algoutils.student.annotation.DoNotTouch;

/**
 * Represents a hexagonal grid system that manages the spatial relationships and
 * elements of the game world.
 * <p>
 * This interface defines the core functionalities for accessing and
 * manipulating the grid's components,
 * including {@link Tile}s, {@link Edge}s, and {@link Structure}s. It allows
 * retrieval of grid elements based on {@link TilePosition} or direct
 * references.
 * </p>
 * <p>
 * Key responsibilities include:
 * <ul>
 * <li>Maintaining the state of all tiles and their positions.</li>
 * <li>Tracking structures placed on the grid.</li>
 * <li>Calculating game mechanics related to area sizes and amulet
 * spawning.</li>
 * <li>Providing pathfinding capabilities between two positions on the
 * grid.</li>
 * </ul>
 */
@DoNotTouch
public interface HexGrid {

    // Tiles

    /**
     * Returns all tiles of the grid as a set.
     *
     * @return all tiles of the grid as a set
     */
    Map<TilePosition, Tile> getTiles();

    /**
     * Returns the tile at the given q and r coordinate.
     *
     * @param q the q-coordinate of the tile
     * @param r the r-coordinate of the tile
     * @return the tile at the given row and column
     */
    @Nullable
    Tile getTileAt(int q, int r);

    /**
     * Returns the tile at the given position.
     *
     * @param position the position of the tile
     * @return the tile at the given position
     */
    @Nullable
    Tile getTileAt(TilePosition position);

    // Edges

    /**
     * Returns all edges of the grid.
     *
     * @return all edges of the grid
     */
    Map<Set<TilePosition>, Edge> getEdges();

    /**
     * Returns the edge between the given positions.
     *
     * @param position0 the first position
     * @param position1 the second position
     * @return the edge between the given intersections
     */
    @Nullable
    Edge getEdge(TilePosition position0, TilePosition position1);

    /**
     * Returns all structures of the grid.
     *
     * @return all structures of the grid
     */
    Map<TilePosition, Structure> getStructures();

    /**
     * Returns the structure at the given position or {@code null} if there is no
     * structure.
     *
     * @param position the position of the structure
     * @return the structure at the given position or {@code null} if there is no
     *         structure
     */
    @Nullable
    Structure getStructureAt(TilePosition position);

    /**
     * Returns the biggest areas of the grid.
     *
     * @return a map of the biggest areas of the grid, where the key is the type of
     *         the area
     */
    Map<Tile.Type, Set<TilePosition>> getBiggestAreas();

    /**
     * Spawns amulets at the farthest reachable tiles for all statues in the game
     * grid and rotates the statues to their next direction counter-clockwise.
     */
    void spawnAmulets();

    /**
     * Finds the shortest path between start and end using the available edges and
     * the edgeCostFunction.
     *
     * @param start         the start position
     * @param end           the end position
     * @param availabeEdges the edges to search for the path
     * @return the list of tiles representing the path from start to end
     */
    List<Tile> findPath(TilePosition start, TilePosition end, Set<Edge> availabeEdges,
            BiFunction<TilePosition, TilePosition, Integer> edgeCostFunction);

    /**
     * Calculates and initializes the areas of connected tiles of the same type.
     */
    void initAreas();
}
