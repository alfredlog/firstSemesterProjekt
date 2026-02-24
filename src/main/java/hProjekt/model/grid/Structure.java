package hProjekt.model.grid;

/**
 * Represents a structure placed on a specific tile within the game world.
 * Structures have a defined position on the {@link HexGrid} and a specific
 * type.
 */
public interface Structure {
    /**
     * Returns the {@link TilePosition} of the structure.
     *
     * @return the {@link TilePosition} of the structure
     */
    TilePosition getPosition();

    /**
     * Returns the {@link HexGrid} instance this structure is placed in.
     *
     * @return the {@link HexGrid} instance this structure is placed in
     */
    HexGrid getHexGrid();

    /**
     * Returns the {@link Structure.Type} of this structure.
     *
     * @return the {@link Structure.Type} of the structure
     */
    Structure.Type getType();

    /**
     * Defines the possible types of structures that can exist in the game.
     */
    enum Type implements Types {
        HUT,
        PALM,
        STATUE
    }
}
