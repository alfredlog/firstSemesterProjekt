package hProjekt.model.mapEditor;

import java.util.Map;

import hProjekt.model.grid.HexGridImpl;
import hProjekt.model.grid.Structure;
import hProjekt.model.grid.Tile;
import hProjekt.model.grid.Tile.Type;
import hProjekt.model.grid.TilePosition;
import hProjekt.view.mapEditor.MapEditor;

/**
 * Represents a hexagonal grid that allows for modifications, used in the
 * {@link MapEditor}.
 * <p>
 * This class extends {@link HexGridImpl} to provide capabilities for creating
 * and managing a collection of editable tiles and structures. It supports
 * initialization from a set of existing editable tiles or from maps defining
 * tile and structure types.
 * </p>
 */
public class EditableHexGrid extends HexGridImpl {
    /**
     * Constructs a new {@code EditableHexGrid} based on maps of tile and structure
     * types.
     * <p>
     * This constructor initializes the grid by calling the superclass constructor,
     * creating a grid layout defined by the provided positions and types.
     * </p>
     *
     * @param tile_types      A {@link Map} associating {@link TilePosition}s with
     *                        {@link Tile.Type}s. Defines the layout and terrain of
     *                        the grid.
     * @param structure_types A {@link Map} associating {@link TilePosition}s with
     *                        {@link Structure.Type}s. Defines the initial placement
     *                        of structures on the grid.
     */
    public EditableHexGrid(final Map<TilePosition, Tile.Type> tile_types,
            final Map<TilePosition, Structure.Type> structure_types) {
        super(tile_types, structure_types);
    }

    /**
     * Retrieves the map of structures present on this grid.
     *
     * @return A {@link Map} where the key is the {@link TilePosition} and the value
     *         is the {@link Structure} located at that position.
     */
    @Override
    public Map<TilePosition, Structure> getStructures() {
        return structures;
    }

    /**
     * Adds a new tile to the grid at the specified position with the given type.
     * <p>
     * This implementation creates a new {@link EditableTile} instance and places it
     * into the grid's tile map.
     * </p>
     *
     * @param position The {@link TilePosition} where the new tile should be
     *                 located.
     * @param type     The {@link Tile.Type} of the new tile.
     */
    @Override
    protected void addTile(final TilePosition position, final Type type) {
        tiles.put(position, new EditableTile(position, type, this));
    }
}
