package hProjekt.model.grid;

import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;
import org.tudalgo.algoutils.student.annotation.DoNotTouch;

import hProjekt.model.grid.TilePosition.EdgeDirection;
import javafx.scene.paint.Color;

/**
 * Represents a single tile within a {@link HexGrid}.
 * <p>
 * A tile holds information about its specific position in the grid coordinates,
 * its {@link Type}, and its relationship to neighbouring tiles and edges.
 * </p>
 * <p>
 * This interface provides methods to:
 * <ul>
 * <li>Navigate the grid (get neighbours, get specific edges).</li>
 * <li>Query tile properties (is it at the coast, what grid does it belong
 * to).</li>
 * <li>Manage game mechanics related to areas and visibility.</li>
 * <li>Handle game objects placed on the tile, such as amulets or
 * structures.</li>
 * </ul>
 */
@DoNotTouch
public interface Tile {

    /**
     * Returns the position of this tile.
     *
     * @return the position of this tile
     */
    TilePosition getPosition();

    /**
     * Returns the type of this tile.
     *
     * @return the type of this tile
     */
    Type getType();

    /**
     * Returns the hex grid this tile is part of.
     *
     * @return the hex grid this tile is part of
     */
    HexGrid getHexGrid();

    /**
     * Returns all neighbours of this tile.
     *
     * @return all neighbours of this tile
     */
    Set<Tile> getNeighbours();

    /**
     * Returns all neighbouring tiles connected by the given edges.
     *
     * @param connectingEdges the edges connecting the tiles
     * @return all neighbouring tiles connected by the given edges
     */
    Set<Tile> getConnectedNeighbours(Set<Edge> connectingEdges);

    /**
     * Returns the tile next in the given direction.
     *
     * @param direction the direction of the edge
     * @return the neighbouring tile
     */
    Tile getNeighbour(final EdgeDirection direction);

    /**
     * Returns whether this tile is at the coast.
     *
     * @return whether this tile is at the coast
     */
    boolean isAtCoast();

    /**
     * Returns the edge in the given direction.
     *
     * @param direction the direction of the edge
     * @return the edge in the given direction
     */
    Edge getEdge(EdgeDirection direction);

    /**
     * Returns all edges connected to this tile.
     *
     * @return all edges connected to this tile
     */
    Set<Edge> getEdges();

    /**
     * Returns the area this tile is part of.
     *
     * @return the area this tile is part of
     */
    Set<TilePosition> getArea();

    /**
     * Sets the area this tile is part of.
     *
     * @param area the area to set
     */
    void setArea(Set<TilePosition> area);

    /**
     * Checks if at least one adjacent tile satisfies the given {@link Predicate}.
     * <p>
     * This method iterates through the neighboring positions of the current tile
     * and applies the provided {@link Predicate} to each neighbor. The neighbor is
     * passed as an {@link Optional} which contains the tile if present, or is empty
     * if the position is the ocean ({@code null}).
     * </p>
     *
     * @param tileFilter a {@link Predicate} to test adjacent tiles (wrapped in
     *                   {@link Optional}). Returns {@code true} if the neighbor
     *                   satisfies the condition.
     * @return {@code true} if at least one adjacent tile satisfies the
     *         {@code tileFilter}, {@code false} otherwise.
     */
    boolean nextTo(Predicate<Optional<Tile>> tileFilter);

    /**
     * Checks if this tile belongs to the largest connected area of the specified
     * {@link Type}.
     *
     * @param tileType the {@link Type} of the tile to check against the largest
     *                 area.
     * @return {@code true} if this tile is part of the biggest area of the given
     *         type, {@code false} otherwise.
     */
    boolean inBiggestArea(Tile.Type tileType);

    /**
     * Checks if a tile matching the given {@link Predicate} is visible from this
     * tile's position.
     * <p>
     * A tile is considered "visible" if it is within a two-tile radius from the
     * current tile's position.
     * </p>
     *
     * @param tileFilter a {@link Predicate} that tests an {@link Optional}
     *                   containing a tile. The predicate should return
     *                   {@code true} if the tile being inspected matches the
     *                   desired criteria. An empty Optional represents the ocean.
     * @return {@code true} if a tile satisfying the {@code tileFilter} is within a
     *         two-tile radius, {@code false} otherwise.
     */
    boolean canSeeTileType(Predicate<Optional<Tile>> tileFilter);

    /**
     * Checks if this tile has an amulet.
     *
     * @return true if the tile has an amulet, false otherwise
     */
    boolean hasAmulet();

    /**
     * Sets whether this tile has an amulet.
     *
     * @param hasAmulet true if the tile should have an amulet, false otherwise
     */
    void setHasAmulet(boolean hasAmulet);

    /**
     * Returns the {@link Structure} currently placed on this tile.
     *
     * @return the {@link Structure} on the tile, or null if no structure is present
     */
    @Nullable
    Structure getStructure();

    /**
     * An enumeration containing all available tile types.
     * Custom tile types need to be added to this list manually.
     * <p>
     * {@code null} is used to represent the ocean.
     */
    enum Type implements Types {
        PLAINS(Color.web("#CAE0AB")),
        MOUNTAIN(Color.web("#777777")),
        JUNGLE(Color.web("#4EB265")),
        RIVER(Color.web("#7BAFDE")),
        LAKE(Color.web("#437DBF")),
        BEACH(Color.web("#F7F056"));

        /**
         * The color of the tile.
         */
        public final Color color;

        Type(final Color color) {
            this.color = color;
        }
    }

}
