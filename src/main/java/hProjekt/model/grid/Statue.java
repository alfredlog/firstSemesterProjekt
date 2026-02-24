package hProjekt.model.grid;

import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

/**
 * Represents a {@link Statue} structure on the hex grid.
 * <p>
 * A statue is a special type of structure that faces a specific
 * {@link TilePosition.EdgeDirection}.
 * It has the ability to {@link #turn() rotate} and {@link #spawnAmulet() spawn
 * amulets} on far-reaching tiles in its facing direction.
 *
 * @see StructureImpl
 */
public class Statue extends StructureImpl {
    private TilePosition.EdgeDirection direction;

    /**
     * Constructs a new {@code Statue} at the specified position on the grid, facing
     * a specific {@link TilePosition.EdgeDirection}.
     * <p>
     * This constructor initializes the statue by calling the superclass constructor
     * with the type {@link Structure.Type#STATUE} and setting the facing direction.
     *
     * @param tilePosition The {@link TilePosition} where the statue is located.
     * @param hexGrid      The {@link HexGrid} to which this statue belongs.
     * @param direction    The {@link TilePosition.EdgeDirection} the statue is
     *                     facing.
     */
    public Statue(final TilePosition tilePosition, final HexGrid hexGrid, final TilePosition.EdgeDirection direction) {
        super(tilePosition, hexGrid, Structure.Type.STATUE);
        this.direction = direction;
    }

    /**
     * Returns the {@link TilePosition.EdgeDirection} the statue is facing.
     *
     * @return the direction the statue is facing
     */
    public TilePosition.EdgeDirection getDirection() {
        return direction;
    }

    /**
     * Rotates the statue clockwise by changing its direction to the previous one in
     * the enumeration.
     */
    @StudentImplementationRequired("P1.3")
    public void turn() {
        // TODO: P1.3
        org.tudalgo.algoutils.student.Student.crash("P1.3 - Remove if implemented");
    }

    /**
     * Spawns an amulet at the farthest reachable {@link Tile} in the
     * current
     * direction of the statue.
     * <p>
     * The method traverses {@link Tile}s in the direction the statue is facing
     * until it reaches the ocean ({@code null}).
     * If a valid position is found, an amulet is placed on that tile.
     */
    @StudentImplementationRequired("P1.3")
    public void spawnAmulet() {
        // TODO: P1.3
        org.tudalgo.algoutils.student.Student.crash("P1.3 - Remove if implemented");
    }
}
