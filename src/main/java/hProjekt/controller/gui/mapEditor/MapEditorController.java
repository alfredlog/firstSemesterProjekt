package hProjekt.controller.gui.mapEditor;

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import hProjekt.controller.MapSaveController;
import hProjekt.controller.gui.SceneController;
import hProjekt.controller.gui.grid.HexGridController;
import hProjekt.controller.gui.grid.TileController;
import hProjekt.model.grid.HexGrid;
import hProjekt.model.grid.Structure;
import hProjekt.model.grid.StructureImpl;
import hProjekt.model.grid.Tile;
import hProjekt.model.grid.TilePosition;
import hProjekt.model.grid.Types;
import hProjekt.model.mapEditor.EditableHexGrid;
import hProjekt.model.mapEditor.EditableTile;
import hProjekt.model.mapEditor.MapEditorTools;
import hProjekt.view.grid.StructurePane;
import hProjekt.view.mapEditor.MapEditor;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Builder;

/**
 * Controller class for the {@link MapEditor} scene.
 * <p>
 * This class manages the interaction between the map editing logic
 * ({@link HexGrid}) and the user interface ({@link MapEditor}).
 * It handles tool selection, mouse events on the grid, and the logic for saving
 * custom maps.
 * </p>
 * <p>
 * Key responsibilities include:
 * <ul>
 * <li>Initializing the {@link EditableHexGrid} with existing tiles or a blank
 * canvas.</li>
 * <li>Setting up event handlers for specific editing tools (Brush, Eraser,
 * Move).</li>
 * <li>Managing drag-and-drop painting functionality on the hex grid.</li>
 * <li>Filtering and validating save actions.</li>
 * </ul>
 */
public class MapEditorController implements SceneController {
    private final MapEditor mapEditor;
    private final Property<MapEditorTools> selectedToolProperty = new SimpleObjectProperty<>(MapEditorTools.MOVE);
    private final Property<Types> selectedTypeProperty = new SimpleObjectProperty<>(null);
    private final StringProperty mapNameProperty = new SimpleStringProperty("");
    private final HexGridController hexGridController;
    private final HexGrid hexGrid;

    /**
     * Constructs a new {@code MapEditorController} initialized with a given
     * hexadecimal grid and map name.
     * <p>
     * This constructor initializes the editor by extracting tile and structure
     * types from the provided {@link HexGrid}. It ensures a minimum map area of
     * 20x20 by filling missing positions with {@code null}.
     * An {@link EditableHexGrid} is created from this data, along with a
     * corresponding {@link HexGridController}.
     * </p>
     * <p>
     * It sets up user interaction handlers for the map view:
     * <ul>
     * <li>Dragging and clicking on tiles triggers the currently selected tool via
     * {@link #handleTool(TileController)}.</li>
     * <li>Navigation events are strictly bound to the
     * {@link MapEditorTools#MOVE} tool. When any other tool is active, these
     * navigation interactors are disabled to prevent conflict with editing
     * actions.</li>
     * </ul>
     *
     * @param hexGrid        The initial {@link HexGrid} data to load into the
     *                       editor. It serves as the source for existing tile and
     *                       structure types.
     * @param initialMapName The name to be assigned to the map being edited, which
     *                       sets the initial value of the {@link #mapNameProperty}.
     */
    public MapEditorController(@NotNull final HexGrid hexGrid, final String initialMapName) {
        final Map<TilePosition, Tile.Type> tile_types = new HashMap<>();
        final Map<TilePosition, Structure.Type> structure_types = new HashMap<>();
        hexGrid.getTiles().forEach((pos, tile) -> tile_types.put(pos, tile.getType()));
        hexGrid.getStructures().forEach((pos, structure) -> structure_types.put(pos, structure.getType()));

        final int mapWidth = 20;
        final int mapHeight = 20;
        for (int q = -mapWidth / 2; q < mapWidth / 2; q++) {
            for (int r = -mapHeight / 2; r < mapHeight / 2; r++) {
                tile_types.putIfAbsent(new TilePosition(q, r), null);
            }
        }

        this.hexGrid = new EditableHexGrid(tile_types, structure_types);
        hexGridController = new HexGridController(this.hexGrid);

        final Region map = hexGridController.buildView();
        for (final TileController tileController : hexGridController.getTileControllers()) {
            final StackPane pane = tileController.getBuilder().getPane();
            pane.setOnDragDetected(e -> pane.startFullDrag());
            pane.setOnMouseDragEntered(e -> handleTool(tileController));
            pane.setOnMouseClicked(e -> handleTool(tileController));
        }

        final EventHandler<? super MouseEvent> originalDraggedHandler = map.getOnMouseDragged();
        final EventHandler<? super MouseEvent> originalPressedHandler = map.getOnMousePressed();
        final EventHandler<? super ScrollEvent> originalScrollHandler = map.getOnScroll();

        selectedToolProperty.subscribe(tool -> {
            if (tool == MapEditorTools.MOVE) {
                map.setOnMouseDragged(originalDraggedHandler);
                map.setOnMousePressed(originalPressedHandler);
                map.setOnScroll(originalScrollHandler);
            } else {
                map.setOnMouseDragged(null);
                map.setOnMousePressed(null);
                map.setOnScroll(null);
            }
        });

        mapEditor = new MapEditor(map, selectedToolProperty, selectedTypeProperty, mapNameProperty,
                this::saveAction, SceneController::loadMainMenuScene);
        mapNameProperty.set(initialMapName);
    }

    /**
     * Handles the application of the currently selected tool to a specific tile on
     * the map.
     * <p>
     * This method performs different actions based on the
     * {@link #selectedToolProperty}:
     * <ul>
     * <li>{@link MapEditorTools#BRUSH}: Applies the selected {@code type} (either a
     * {@link Tile.Type} or a {@link Structure.Type}) to the target tile.
     * <ul>
     * <li>If the type is a {@link Tile.Type}, it updates the tile's base terrain
     * type.</li>
     * <li>If the type is a {@link Structure.Type}, it checks validity (e.g.,
     * ensuring a STATUE
     * has valid neighbors and the tile has a base type) before creating a new
     * {@link StructureImpl}
     * and placing it on the grid.</li>
     * </ul>
     * </li>
     * <li>{@link MapEditorTools#ERASER}: Removes content from the tile.
     * <ul>
     * <li>It removes any existing structure at the given position.</li>
     * <li>Unless the selected type filter is strictly for structures, it also
     * resets the tile's base type to {@code null}.</li>
     * </ul>
     * </li>
     * </ul>
     * After the tool action is performed, the visual representation of the tile is
     * updated.
     *
     * @param tileController The controller managing the specific tile interaction.
     */
    private void handleTool(final TileController tileController) {
        final MapEditorTools tool = selectedToolProperty.getValue();
        final Types type = selectedTypeProperty.getValue();
        final TilePosition position = tileController.getTile().getPosition();
        switch (tool) {
            case BRUSH:
                switch (type) {
                    case final Tile.Type tileType:
                        ((EditableTile) tileController.getTile()).setType(tileType);
                        break;
                    case final Structure.Type structureType:
                        if ((structureType == Structure.Type.STATUE && tileController.getTile().getNeighbours().stream()
                                .anyMatch((tile) -> tile == null || tile.getType() == null))
                                || tileController.getTile().getType() == null) {
                            break;
                        }

                        final StructureImpl structure = new StructureImpl(position, hexGrid, structureType);
                        hexGrid.getStructures().put(position, structure);
                        tileController.getBuilder().setStructurePane(new StructurePane(structure));
                        break;
                    default:
                        break;
                }
                break;
            case ERASER:
                if (hexGrid.getStructures().containsKey(position)) {
                    tileController.getBuilder().setStructurePane(null);
                }
                if (type instanceof Structure.Type) {
                    break;
                }
                ((EditableTile) tileController.getTile()).setType(null);
                break;
            default:
                break;
        }
        tileController.getBuilder().updateType();
    }

    /**
     * Validates the current map data and saves it under the specified name.
     * <p>
     * This method performs the following steps:
     * <ol>
     * <li>Checks whether a valid map name has been provided.</li>
     * <li>Verifies that the map name does not contain invalid characters.</li>
     * <li>Initializes the areas of the {@link HexGrid} and catches possible
     * errors.</li>
     * <li>Extracts and filters the {@link Tile}s and {@link Structure}s of the
     * {@link HexGrid}.</li>
     * <li>Attempts to save the map using the {@link MapSaveController}.</li>
     * </ol>
     *
     * @return an error message as a {@code String} if validation or saving fails,
     *         or {@code null} if the save operation was successful.
     */
    private String saveAction() {
        final String mapName = mapNameProperty.getValueSafe();
        if (mapName.isBlank()) {
            return "Please provide a map name.";
        }
        try {
            Paths.get(mapName);
        } catch (final InvalidPathException e) {
            return "The map name contains invalid characters.";
        }
        try {
            hexGridController.getHexGrid().initAreas();
        } catch (final UnsupportedOperationException e) {
            return e.getMessage();
        }
        final Map<TilePosition, Tile.Type> tiles = hexGridController.getHexGrid().getTiles().entrySet().stream()
                .filter(e -> e.getValue().getType() != null)
                .collect(HashMap::new, (m, e) -> m.put(e.getKey(), e.getValue().getType()), HashMap::putAll);
        final Map<TilePosition, Structure.Type> structures = hexGridController.getHexGrid().getStructures().entrySet()
                .stream().collect(HashMap::new, (m, e) -> m.put(e.getKey(), e.getValue().getType()), HashMap::putAll);

        final boolean success = MapSaveController.saveMap(mapName, tiles, structures);
        if (!success) {
            return "The map could not be saved.";
        }
        return null;
    }

    @Override
    public Builder<Region> getBuilder() {
        throw new UnsupportedOperationException("Unimplemented method 'getBuilder'");
    }

    @Override
    public String getTitle() {
        return "Map Editor";
    }

    @Override
    public Region buildView() {
        return mapEditor;
    }
}
