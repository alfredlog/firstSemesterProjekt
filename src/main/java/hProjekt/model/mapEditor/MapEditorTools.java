package hProjekt.model.mapEditor;

import hProjekt.model.grid.Structure;
import hProjekt.model.grid.Tile;
import hProjekt.view.mapEditor.MapEditor;

/**
 * Represents the different tools available in the {@link MapEditor}.
 * These tools define the user's current interaction mode when editing a map.
 *
 * <ul>
 * <li>{@link #BRUSH} - Tool used for placing {@link Tile}s or
 * {@link Structure}s on the map.</li>
 * <li>{@link #ERASER} - Tool used for removing {@link Tile}s or
 * {@link Structure}s from the map.</li>
 * <li>{@link #MOVE} - Tool used for moving the view of the map.</li>
 * </ul>
 */
public enum MapEditorTools {
    BRUSH,
    ERASER,
    MOVE
}
