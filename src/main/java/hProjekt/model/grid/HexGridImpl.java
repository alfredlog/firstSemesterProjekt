package hProjekt.model.grid;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.function.BiFunction;

import org.jetbrains.annotations.Nullable;
import org.tudalgo.algoutils.student.annotation.DoNotTouch;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import hProjekt.Config;
import javafx.util.Pair;

/**
 * Default implementation of {@link HexGrid}.
 */
public class HexGridImpl implements HexGrid {

    protected final Map<TilePosition, Tile> tiles = new HashMap<>();
    protected final Map<Set<TilePosition>, Edge> edges = new HashMap<>();
    protected final Map<TilePosition, Structure> structures = new HashMap<>();

    protected final Random random = Config.RANDOM;

    protected final Map<Tile.Type, Set<TilePosition>> biggestAreas = new HashMap<>();

    /**
     * Creates a new empty HexGrid.
     */
    @DoNotTouch
    public HexGridImpl() {}

    /**
     * Creates a new HexGrid based on maps of tile and structure types.
     *
     * @param tile_types      A {@link Map} associating {@link TilePosition}s with
     *                        {@link Tile.Type}s.
     * @param structure_types A {@link Map} associating {@link TilePosition}s with
     *                        {@link Structure.Type}s.
     */
    @DoNotTouch
    public HexGridImpl(final Map<TilePosition, Tile.Type> tile_types,
            final Map<TilePosition, Structure.Type> structure_types) {
        tile_types.forEach(this::addTile);
        structure_types.forEach((position, type) -> structures.put(position, switch (type) {
            case STATUE -> new Statue(position, this, TilePosition.EdgeDirection.values()[random
                    .nextInt(TilePosition.EdgeDirection.values().length)]);
            default -> new StructureImpl(position, this, type);
        }));

        initEdges();
        initAreas();
    }

    /**
     * Initializes the edges in this grid.
     */
    @DoNotTouch
    protected void initEdges() {
        for (final Tile tile : tiles.values()) {
            Arrays.stream(TilePosition.EdgeDirection.values())
                    .filter(ed -> tiles.containsKey(TilePosition.neighbour(tile.getPosition(), ed)))
                    .forEach(
                            ed -> edges.putIfAbsent(
                                    Set.of(
                                            tile.getPosition(),
                                            TilePosition.neighbour(tile.getPosition(), ed)),
                                    new EdgeImpl(
                                            this,
                                            tile.getPosition(),
                                            TilePosition.neighbour(tile.getPosition(), ed))));
        }
    }

    /**
     * Initializes tile areas for all tiles in this grid.
     * <p>
     * This method identifies contiguous areas of tiles of the same type and marks
     * the largest area of each type as the "biggest" area. It uses a breadth-first
     * search approach to traverse tiles.
     */
    @DoNotTouch
    public void initAreas() {
        biggestAreas.clear();
        final Queue<Tile> queue = new ArrayDeque<>(tiles.values());
        final Map<Tile.Type, List<Set<TilePosition>>> candidates = new HashMap<>();

        while (!queue.isEmpty()) {
            final Tile tile = queue.poll();
            final Set<TilePosition> areaTiles = new HashSet<>();
            tile.setArea(areaHelper(tile, areaTiles, queue));

            final List<Set<TilePosition>> typeCandidates = candidates.computeIfAbsent(tile.getType(),
                    k -> new ArrayList<>());

            if (typeCandidates.isEmpty() || areaTiles.size() > typeCandidates.getFirst().size()) {
                typeCandidates.clear();
                typeCandidates.add(areaTiles);
            } else if (areaTiles.size() == typeCandidates.getFirst().size()) {
                typeCandidates.add(areaTiles);
            }
        }

        candidates.forEach((type, list) -> {
            if (list.size() > 1) {
                throw new UnsupportedOperationException(
                        "Two areas of the same type with the same size are not supposed to exist.");
            }
            biggestAreas.put(type, list.getFirst());
        });
    }

    /**
     * Helper method to recursively populate an area with tiles of the same type.
     * This method performs a depth-first search to find all connected tiles of the
     * same type.
     *
     * @param tile  The current {@link Tile} being processed
     * @param tiles The {@link Set} of {@link TilePosition}s already added to the
     *              area
     * @param queue The {@link Queue} of {@link Tile}s to process
     * @return The {@link Set} of {@link TilePosition}s in the area
     */
    @DoNotTouch
    private Set<TilePosition> areaHelper(final Tile tile, final Set<TilePosition> tiles, final Queue<Tile> queue) {
        if (tiles.contains(tile.getPosition())) {
            return tiles;
        }
        tiles.add(tile.getPosition());
        tile.setArea(tiles);

        queue.remove(tile);

        for (final Tile neighbour : tile.getNeighbours()) {
            if (neighbour.getType() == tile.getType() && !tiles.contains(neighbour.getPosition())) {
                areaHelper(neighbour, tiles, queue);
            }
        }
        return tiles;
    }

    // Tiles

    @Override
    public Map<TilePosition, Tile> getTiles() {
        return Collections.unmodifiableMap(tiles);
    }

    @Override
    public @Nullable Tile getTileAt(final int q, final int r) {
        return getTileAt(new TilePosition(q, r));
    }

    @Override
    public @Nullable Tile getTileAt(final TilePosition position) {
        return tiles.get(position);
    }

    /**
     * Adds a new tile to the grid.
     *
     * @param position position of the new tile
     * @param type     type of the new tile
     */
    @DoNotTouch
    protected void addTile(final TilePosition position, final Tile.Type type) {
        tiles.put(position, new TileImpl(position, type, this));
    }

    // Edges / Roads

    @Override
    public Map<Set<TilePosition>, Edge> getEdges() {
        return Collections.unmodifiableMap(edges);
    }

    @Override
    public @Nullable Edge getEdge(final TilePosition position0, final TilePosition position1) {
        return edges.get(Set.of(position0, position1));
    }

    @Override
    public Map<TilePosition, Structure> getStructures() {
        return Collections.unmodifiableMap(structures);
    }

    @Override
    public @Nullable Structure getStructureAt(final TilePosition position) {
        return structures.get(position);
    }

    @Override
    public Map<Tile.Type, Set<TilePosition>> getBiggestAreas() {
        return biggestAreas;
    }

    @Override
    @StudentImplementationRequired("P1.3")
    public void spawnAmulets() {
        // TODO: P1.3
        org.tudalgo.algoutils.student.Student.crash("P1.3 - Remove if implemented");
    }

    @Override
    public List<Tile> findPath(final TilePosition start, final TilePosition target, final Set<Edge> availableEdges,
            final BiFunction<TilePosition, TilePosition, Integer> edgeCostFunction) {
        final PriorityQueue<Pair<TilePosition, Integer>> positionQueue = new PriorityQueue<>(
                Comparator.comparingInt(Pair::getValue));
        final Map<TilePosition, TilePosition> previous = new HashMap<>();
        final Map<TilePosition, Integer> distance = new HashMap<>();
        positionQueue.add(new Pair<>(start, 0));
        previous.put(start, start);
        distance.put(start, 0);

        while (!positionQueue.isEmpty()) {
            final TilePosition current = positionQueue.poll().getKey();
            if (current.equals(target)) {
                break;
            }
            for (final TilePosition next : getTileAt(current).getConnectedNeighbours(availableEdges).stream()
                    .map(Tile::getPosition).toList()) {
                final int newDistance = distance.get(current) + edgeCostFunction.apply(current, next);
                if (!distance.containsKey(next) || newDistance < distance.get(next)) {
                    distance.put(next, newDistance);
                    previous.put(next, current);
                    positionQueue.add(new Pair<>(next, newDistance));
                }
            }
        }

        if (!previous.containsKey(target)) {
            return List.of();
        }

        TilePosition current = target;
        final List<Tile> path = new ArrayList<>();

        while (!current.equals(start)) {
            path.add(getTileAt(current));
            current = previous.get(current);
        }
        path.add(getTileAt(start));

        return path.reversed();
    }

    @Override
    public String toString() {
        return "HexGridImpl [tiles=" + tiles + ", edges=" + edges + "]";
    }
}
