package hProjekt.model.grid;

import java.util.Objects;

/**
 * An implementation of the {@link Structure} interface representing a structure
 * on the {@link HexGrid}.
 * <p>
 * This class holds information about the structure's {@link TilePosition}, the
 * {@link HexGrid} it belongs to, and its {@link Structure.Type}.
 * It is immutable once created.
 */
public class StructureImpl implements Structure {
    private final TilePosition tilePosition;
    private final HexGrid hexGrid;
    private final Structure.Type type;

    /**
     * Constructs a new {@code StructureImpl} instance with the specified
     * {@link TilePosition}, {@link HexGrid}, and {@link Structure.Type}.
     *
     * @param tilePosition the {@link TilePosition} where this structure is located
     * @param hexGrid      the {@link HexGrid} to which this structure belongs
     * @param type         the specific {@link Structure.Type} of the structure
     */
    public StructureImpl(final TilePosition tilePosition, final HexGrid hexGrid, final Structure.Type type) {
        this.tilePosition = tilePosition;
        this.hexGrid = hexGrid;
        this.type = type;
    }

    @Override
    public TilePosition getPosition() {
        return tilePosition;
    }

    @Override
    public HexGrid getHexGrid() {
        return hexGrid;
    }

    @Override
    public Structure.Type getType() {
        return type;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof final StructureImpl that)) {
            return false;
        }
        return Objects.equals(tilePosition, that.tilePosition)
                && Objects.equals(hexGrid, that.hexGrid)
                && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tilePosition, hexGrid, type);
    }

    @Override
    public String toString() {
        return "StructureImpl[" +
                "tilePosition=" + tilePosition +
                ", hexGrid=" + hexGrid +
                ", type=" + type +
                ']';
    }
}
