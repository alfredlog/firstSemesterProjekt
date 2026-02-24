package hProjekt.model.grid;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.jetbrains.annotations.Nullable;
import org.tudalgo.algoutils.student.annotation.DoNotTouch;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import hProjekt.model.grid.TilePosition.EdgeDirection;

/**
 * Holds information on a tile.
 *
 * @see Tile
 * @see TilePosition
 */
public class TileImpl implements Tile {

    private final TilePosition position;
    private final Type type;
    private final HexGrid hexGrid;
    private boolean hasAmulet = false;

    private @Nullable Set<TilePosition> area;

    /**
     * Constructor for a tile with a given position, type, height and width
     *
     * @param position the position of this tile in the grid
     * @param type     the type of this tile
     * @param hexGrid  the grid this tile is placed in
     */
    public TileImpl(final TilePosition position, final Type type, final HexGrid hexGrid) {
        this.position = position;
        this.type = type;
        this.hexGrid = hexGrid;
    }

    /**
     * Alternative constructor with q- and r-coordinates instead of a
     * {@link TilePosition}.
     *
     * @param q       the q-coordinate of this tile in the grid
     * @param r       the r-coordinate of this tile in the grid
     * @param type    the type of this tile
     * @param hexGrid the grid this tile is placed in
     */
    @DoNotTouch
    public TileImpl(final int q, final int r, final Type type, final HexGrid hexGrid) {
        this(new TilePosition(q, r), type, hexGrid);
    }

    @Override
    public TilePosition getPosition() {
        return position;
    }

    @Override
    public HexGrid getHexGrid() {
        return hexGrid;
    }

    @Override
    public Edge getEdge(final EdgeDirection direction) {
        final TilePosition neighbour = TilePosition.neighbour(position, direction);
        return hexGrid.getEdges().get(Set.of(position, neighbour));
    }

    @Override
    public Set<Edge> getEdges() {
        return EdgeDirection.stream().map(this::getEdge).filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public @Nullable Set<TilePosition> getArea() {
        return area;
    }

    public void setArea(final Set<TilePosition> area) {
        this.area = area;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    @StudentImplementationRequired("P1.4")
    public Set<Tile> getNeighbours() {
        // TODO: P1.4
        return org.tudalgo.algoutils.student.Student.crash("P1.4 - Remove if implemented");
    }

    @Override
    public Tile getNeighbour(final EdgeDirection direction) {
        return hexGrid.getTileAt(TilePosition.neighbour(position, direction));
    }

    @Override
    public boolean isAtCoast() {
        return getNeighbours().size() < 6;
    }

    @Override
    public Set<Tile> getConnectedNeighbours(final Set<Edge> connectingEdges) {
        return getNeighbours().stream()
                .filter(neighbour -> connectingEdges.stream().anyMatch(edge -> edge.getAdjacentTilePositions()
                        .containsAll(Set.of(neighbour.getPosition(), position))))
                .collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return "TileImpl[" +
                "position=" + position +
                ", type=" + type +
                "]";
    }

    /**
     * Checks if there is a tile within the specified radius that satisfies the
     * given {@link Predicate}.
     *
     * @param predicate a {@link Predicate} that tests tiles wrapped in
     *                  {@link Optional}. The Optional will be empty if no tile
     *                  exists at a given position (the ocean), or contain the tile
     *                  if one exists at that position
     * @param radius    the maximum distance to search from this tile's position.
     *                  A radius of 0 searches no tiles, radius of 1 includes
     *                  immediate neighbors, etc.
     * @return {@code true} if any tile within the specified radius satisfies the
     *         predicate, {@code false} otherwise
     *
     * @see TilePosition#forEachSpiral(TilePosition, int, Predicate, boolean)
     */
    @StudentImplementationRequired("P1.4")
    private boolean isNear(final Predicate<Optional<Tile>> predicate, final int radius) {
        // TODO: P1.4
        return org.tudalgo.algoutils.student.Student.crash("P1.4 - Remove if implemented");
    }

    @Override
    public boolean nextTo(final Predicate<Optional<Tile>> tileFilter) {
        return isNear(tileFilter, 1);
    }

    @Override
    public boolean inBiggestArea(final Type tileType) {
        if (area == null) {
            return false;
        }

        return hexGrid.getBiggestAreas().getOrDefault(tileType, Set.of()).equals(area);
    }

    @Override
    public boolean canSeeTileType(final Predicate<Optional<Tile>> tileFilter) {
        return isNear(tileFilter, 2);
    }

    @Override
    public boolean hasAmulet() {
        return hasAmulet;
    }

    @Override
    public void setHasAmulet(final boolean hasAmulet) {
        this.hasAmulet = hasAmulet;
    }

    @Override
    public Structure getStructure() {
        return hexGrid.getStructureAt(position);
    }
}
