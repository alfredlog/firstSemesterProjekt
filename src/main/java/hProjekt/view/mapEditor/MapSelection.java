package hProjekt.view.mapEditor;

import java.util.List;
import java.util.function.Supplier;

import hProjekt.view.utils.ErrorBox;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

/**
 * A JavaFX component representing a selection screen for loading existing maps
 * or creating new ones.
 * <p>
 * This class extends {@link BorderPane} and serves as a view within the Map
 * Editor module.
 * It displays a scrollable list ({@link ToggleGroup}) of available saved maps
 * and an option to create a new map.
 * Users can select a map or the creation option, and navigate back to the main
 * menu or proceed with the selection.
 * </p>
 *
 * <p>
 * The view consists of:
 * </p>
 * <ul>
 * <li>A top bar containing a "Back" button, a title label, and a "Load/Create
 * Map" action button.</li>
 * <li>A central scrollable area displaying buttons for each saved map and a
 * "Create New Map" button.</li>
 * <li>An {@link ErrorBox} for displaying validation errors during the loading
 * process.</li>
 * </ul>
 *
 * <p>
 * State changes (which map is selected, whether to create a new one) are
 * propagated via property bindings passed to the constructor.
 * </p>
 *
 * @see BorderPane
 * @see ToggleButton
 * @see ToggleGroup
 */
public class MapSelection extends BorderPane {
    private final ErrorBox errorBox = new ErrorBox();

    /**
     * Constructs a new {@code MapSelection} view.
     * <p>
     * Initializes the layout with a top bar, a scrollable list of saved maps, and
     * action buttons.
     * Binds the selected map and creation option to the provided properties.
     *
     * @param savedMaps          A list of names of saved maps available for
     *                           selection.
     * @param selectedMap        A {@link StringProperty} to hold the name of the
     *                           selected map.
     * @param createMap          A {@link BooleanProperty} indicating whether to
     *                           create a new map.
     * @param loadMainMenuAction A {@link Runnable} to execute when the user wants
     *                           to return to the main menu.
     * @param loadAction         A {@link Supplier} that attempts to load the
     *                           selected map or create a new one, returning an
     *                           error message if unsuccessful.
     */
    public MapSelection(final List<String> savedMaps, final StringProperty selectedMap, final BooleanProperty createMap,
            final Runnable loadMainMenuAction, final Supplier<String> loadAction) {
        getStylesheets().add("css/main.css");
        getStylesheets().add("css/menu.css");

        final GridPane topBar = new GridPane();
        final Button backButton = new Button("Back to Main Menu");
        backButton.getStyleClass().add("button-back");
        backButton.setOnAction(event -> loadMainMenuAction.run());

        final Button loadMapButton = new Button("Load/Create Map");
        loadMapButton.getStyleClass().add("start-game-button");
        loadMapButton.setOnAction(event -> {
            errorBox.clearError();
            final String error = loadAction.get();
            if (error != null && !error.isBlank()) {
                errorBox.setError(error);
            }
        });

        final Label title = new Label("Load or Create Map");
        title.getStyleClass().add("text-title");

        topBar.add(backButton, 0, 0);
        topBar.add(title, 1, 0);
        topBar.add(loadMapButton, 2, 0);

        topBar.setAlignment(Pos.CENTER);
        topBar.setHgap(10);
        final ColumnConstraints column0 = new ColumnConstraints();
        column0.setPercentWidth(20);
        column0.setHalignment(HPos.LEFT);
        final ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(60);
        column1.setHalignment(HPos.CENTER);
        final ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(20);
        column2.setHalignment(HPos.RIGHT);
        topBar.getColumnConstraints().addAll(column0, column1, column2);

        final VBox mainContent = new VBox();
        mainContent.setAlignment(Pos.CENTER);
        mainContent.setSpacing(10);
        mainContent.setPadding(new Insets(20));

        final ToggleGroup mapToggleGroup = new ToggleGroup();
        mapToggleGroup.selectedToggleProperty().subscribe(newToggle -> {
            if (newToggle == null) {
                selectedMap.set("");
                createMap.set(false);
                return;
            }
            final Pair<String, Boolean> mapData = (Pair<String, Boolean>) newToggle.getUserData();
            selectedMap.set(mapData.getKey());
            createMap.set(mapData.getValue());
        });

        for (final String mapName : savedMaps) {
            final ToggleButton mapButton = new ToggleButton(mapName);
            mapButton.setToggleGroup(mapToggleGroup);
            mapButton.setMinWidth(200);
            mapButton.setMaxWidth(200);
            mapButton.setUserData(new Pair<>(mapName, false));

            mainContent.getChildren().add(mapButton);
        }
        final ToggleButton createMapButton = new ToggleButton("Create New Map");
        createMapButton.setToggleGroup(mapToggleGroup);
        createMapButton.setMinWidth(200);
        createMapButton.setMaxWidth(200);
        createMapButton.setUserData(new Pair<>("", true));

        final Separator separator = new Separator(Orientation.HORIZONTAL);
        separator.setPadding(new Insets(10, 0, 10, 0));
        separator.setVisible(false);

        mainContent.getChildren().addAll(separator, createMapButton, errorBox);

        final ScrollPane centerPane = new ScrollPane(mainContent);
        centerPane.setFitToWidth(true);
        centerPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        centerPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        setTop(topBar);
        setCenter(centerPane);
    }
}
