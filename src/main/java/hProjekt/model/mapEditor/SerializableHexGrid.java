package hProjekt.model.mapEditor;

import java.io.Serializable;
import java.util.Map;

import hProjekt.model.grid.HexGrid;
import hProjekt.model.grid.Structure;
import hProjekt.model.grid.Tile;
import hProjekt.model.grid.TilePosition;

/**
 * A serializable representation of a {@link HexGrid}.
 * <p>
 * This class is used to persist the state of a hex map to a file. Instead of
 * storing complete {@link Tile} or {@link Structure} objects, this object
 * stores mappings of positions to their corresponding enumeration types.
 *
 * @param tiles      Map associating {@link TilePosition}s with
 *                   {@link Tile.Type}.
 * @param structures Map associating {@link TilePosition}s with
 *                   {@link Structure.Type}.
 * @see Serializable
 */
public record SerializableHexGrid(Map<TilePosition, Tile.Type> tiles, Map<TilePosition, Structure.Type> structures)
        implements Serializable {
    /**
     * Retrieves the map of tiles.
     *
     * @return a map linking {@link TilePosition} to {@link Tile.Type}
     */
    public Map<TilePosition, Tile.Type> tiles() {
        return tiles;
    }

    /**
     * Retrieves the map of structures.
     *
     * @return a map linking {@link TilePosition} to {@link Structure.Type}
     */
    public Map<TilePosition, Structure.Type> structures() {
        return structures;
    }
}
