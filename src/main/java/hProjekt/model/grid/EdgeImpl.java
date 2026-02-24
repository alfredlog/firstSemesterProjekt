package hProjekt.model.grid;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Default implementation of {@link Edge}.
 *
 * @param grid      the HexGrid instance this edge is placed in
 * @param position1 the first position
 * @param position2 the second position
 */
public record EdgeImpl(
        HexGrid grid,
        TilePosition position1,
        TilePosition position2)
        implements Edge {

    @Override
    public HexGrid getHexGrid() {
        return grid;
    }

    @Override
    public TilePosition getPosition1() {
        return position1;
    }

    @Override
    public TilePosition getPosition2() {
        return position2;
    }

    @Override
    public boolean connectsTo(final Edge other) {
        return getAdjacentTilePositions().contains(other.getPosition1())
                || getAdjacentTilePositions().contains(other.getPosition2());
    }

    @Override
    public Set<TilePosition> getAdjacentTilePositions() {
        return Set.of(position1, position2);
    }

    @Override
    public Set<Edge> getConnectedEdges() {
        return grid.getEdges().values().stream()
                .filter(this::connectsTo).collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public String toString() {
        return "EdgeImpl[" +
                "position1=" + position1 +
                ", position2=" + position2 +
                "]";
    }
}
