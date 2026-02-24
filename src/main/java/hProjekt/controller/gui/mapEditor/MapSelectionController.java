package hProjekt.controller.gui.mapEditor;

import hProjekt.controller.MapSaveController;
import hProjekt.controller.gui.SceneController;
import hProjekt.controller.gui.SceneSwitcher;
import hProjekt.model.grid.HexGrid;
import hProjekt.model.grid.HexGridImpl;
import hProjekt.view.mapEditor.MapSelection;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.layout.Region;
import javafx.util.Builder;

/**
 * Controller class for the Map Selection scene in the Map Editor.
 * <p>
 * This controller manages the user interaction for selecting an existing map to
 * edit or initiating
 * the creation of a new map. It handles the logic for validating the selection,
 * loading the
 * map data, and transitioning to the Map Editor scene.
 * </p>
 */
public class MapSelectionController implements SceneController {
    private final MapSelection mapSelection;
    private final SimpleStringProperty selectedMap = new SimpleStringProperty("");
    private final BooleanProperty createMap = new SimpleBooleanProperty(false);

    /**
     * Constructs a new {@code MapSelectionController}.
     * <p>
     * Initializes the {@link MapSelection} view component with necessary
     * dependencies,
     * including the list of saved maps, property bindings for selection state, and
     * callbacks for navigation actions (returning to main menu or proceeding with
     * selection).
     * </p>
     */
    public MapSelectionController() {
        mapSelection = new MapSelection(MapSaveController.getSavedMaps(), selectedMap, createMap,
                SceneController::loadMainMenuScene, this::selectAction);
    }

    /**
     * Handles the action when the user confirms their map selection.
     * <p>
     * This method performs the following steps:
     * <ul>
     * <li>Validates if a map is selected or if the "create new map" flag is
     * set.</li>
     * <li>If creating a new map, initializes an empty {@link HexGrid}.</li>
     * <li>If loading an existing map, attempts to load the grid data using
     * {@link MapSaveController}.</li>
     * <li>If successful, switches the scene to the
     * {@link MapEditorController}.</li>
     * </ul>
     *
     * @return A string containing an error message if the selection is invalid or
     *         loading fails;
     *         {@code null} if the operation was successful.
     */
    private String selectAction() {
        if (selectedMap.getValueSafe().isBlank() && !createMap.get()) {
            return "No map selected!";
        }

        final HexGrid grid;

        if (createMap.get()) {
            grid = new HexGridImpl();
        } else {
            grid = MapSaveController.loadMap(selectedMap.get());
            if (grid == null) {
                return "Could not load map!";
            }
        }

        SceneSwitcher.getInstance().loadScene(new MapEditorController(grid, selectedMap.get()));

        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @throws UnsupportedOperationException because this controller builds its view
     *                                       directly via {@link #buildView()}
     *                                       rather than providing a separate
     *                                       Builder.
     */
    @Override
    public Builder<Region> getBuilder() {
        throw new UnsupportedOperationException("Unimplemented method 'getBuilder'");
    }

    @Override
    public String getTitle() {
        return "Map Selection";
    }

    @Override
    public Region buildView() {
        return mapSelection;
    }
}
