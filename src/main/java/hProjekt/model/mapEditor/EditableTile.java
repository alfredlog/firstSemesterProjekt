package hProjekt.model.mapEditor;

import hProjekt.model.grid.HexGrid;
import hProjekt.model.grid.Tile;
import hProjekt.model.grid.TileImpl;
import hProjekt.model.grid.TilePosition;
import hProjekt.view.mapEditor.MapEditor;

/**
 * Represents a tile within the {@link MapEditor} that can be modified.
 * <p>
 * This class extends {@link TileImpl} to allow the {@link Type} to be updated
 * dynamically, which is necessary for editing map layouts.
 */
public class EditableTile extends TileImpl {
    private Tile.Type type;

    /**
     * Constructs a new editable tile with the specified {@link TilePosition},
     * {@link Tile.Type}, and {@link HexGrid}.
     *
     * @param position The {@link TilePosition} of the tile within the
     *                 {@link HexGrid}.
     * @param type     The initial {@link Tile.Type} of the tile.
     * @param hexGrid  The {@link HexGrid} to which this tile belongs.
     */
    public EditableTile(final TilePosition position, final Tile.Type type, final HexGrid hexGrid) {
        super(position, type, hexGrid);
        this.type = type;
    }

    /**
     * Updates the type of this tile.
     *
     * @param type The new {@link Tile.Type} to set for this tile.
     */
    public void setType(final Type type) {
        this.type = type;
    }

    /**
     * Retrieves the current type of this tile.
     *
     * @return The current {@link Tile.Type} of the tile.
     */
    @Override
    public Type getType() {
        return type;
    }
}
