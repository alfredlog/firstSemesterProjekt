package hProjekt.view.menus;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

import hProjekt.Config;
import hProjekt.model.PlayerImpl;
import hProjekt.view.utils.ErrorBox;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Builder;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * Builder for the Setup Game menu.
 */
public class SetupGameBuilder implements Builder<Region> {
    private final Runnable loadMainMenuAction;
    private final ObservableList<PlayerImpl.Builder> players;
    private final Supplier<Boolean> startGameHandler;
    private final SimpleObjectProperty<String> selectedMap;
    private final ErrorBox errorBox = new ErrorBox();
    private final List<String> availableMaps;

    /**
     * Constructor for the SetupGameBuilder.
     *
     * @param loadMainMenuAction the action to load the main menu
     * @param players            the list of player builders
     * @param startGameHandler   the handler to start the game
     * @param selectedMap        the property for the selected map
     * @param availableMaps      the list of available maps
     */
    public SetupGameBuilder(final Runnable loadMainMenuAction,
            final ObservableList<PlayerImpl.Builder> players, final Supplier<Boolean> startGameHandler,
            final SimpleObjectProperty<String> selectedMap, final List<String> availableMaps) {
        this.loadMainMenuAction = loadMainMenuAction;
        this.players = players;
        this.startGameHandler = startGameHandler;
        this.selectedMap = selectedMap;
        this.availableMaps = availableMaps;
    }

    @Override
    public Region build() {
        final BorderPane root = new BorderPane();
        root.getStylesheets().add("css/main.css");
        root.getStylesheets().add("css/menu.css");

        final Button backButton = new Button("Back to Main Menu");
        backButton.getStyleClass().add("button-back");
        backButton.setOnAction(event -> loadMainMenuAction.run());

        final Button startGameButton = new Button("Start Game");
        startGameButton.getStyleClass().add("start-game-button");
        startGameButton.setOnAction(event -> {
            errorBox.clearError();
            if (!startGameHandler.get()) {
                errorBox.setError("Cannot start game");
            }
        });

        final StackPane topBar = new StackPane();
        topBar.setPadding(new Insets(10));
        topBar.getChildren().addAll(backButton, startGameButton);
        StackPane.setAlignment(startGameButton, Pos.TOP_RIGHT);
        StackPane.setAlignment(backButton, Pos.TOP_LEFT);

        final VBox mainContent = new VBox(20);
        mainContent.setAlignment(Pos.CENTER);
        mainContent.setPadding(new Insets(20));

        final Label titleLabel = new Label("Setup Game");
        titleLabel.getStyleClass().add("text-title");

        final VBox playerContainer = new VBox(10);

        final Button addPlayerButton = new Button(String.format("%c  Add Player", 0xF0417));
        addPlayerButton.setOnAction(e -> {
            errorBox.clearError();
            if (players.size() >= Config.MAX_PLAYERS) {
                errorBox.setError("Cannot add more players");
                return;
            }
            players.add(nextPlayerBuilder());
        });

        players.subscribe(() -> {
            addPlayerButton.setDisable(players.size() >= Config.MAX_PLAYERS);

            updatePlayerInputs(playerContainer);
        });
        updatePlayerInputs(playerContainer);

        final HBox mapSelectionContainer = new HBox(10);
        mapSelectionContainer.setAlignment(Pos.CENTER);
        mapSelectionContainer.setPadding(new Insets(10, 20, 10, 20));

        final Label mapLabel = new Label("Select a Map:");
        mapLabel.setTextFill(Color.WHITE);

        final ComboBox<String> mapSelector = new ComboBox<>();
        mapSelector.getItems().addAll(availableMaps);
        mapSelector.setMaxWidth(200);
        mapSelector.setConverter(new StringConverter<>() {
            @Override
            public String toString(final String mapName) {
                return Objects.requireNonNullElse(mapName, "Select a map");
            }

            @Override
            public String fromString(final String string) {
                throw new UnsupportedOperationException("Unused method");
            }
        });
        mapSelector.setValue(null);
        mapSelector.valueProperty().bindBidirectional(selectedMap);

        mapSelectionContainer.getChildren().addAll(mapLabel, mapSelector);

        mainContent.getChildren().addAll(titleLabel, errorBox, playerContainer, addPlayerButton, mapSelectionContainer);

        final ScrollPane centerPane = new ScrollPane(mainContent);
        centerPane.setFitToWidth(true);
        centerPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        centerPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        root.setTop(topBar);
        root.setCenter(centerPane);

        return root;
    }

    private void updatePlayerInputs(final VBox playerContainer) {
        playerContainer.getChildren().clear();
        for (final PlayerImpl.Builder playerBuilder : players) {
            playerContainer.getChildren()
                    .add(createPlayerInputs(playerBuilder, players.size() > Config.MIN_PLAYERS));
        }
    }

    private Region createPlayerInputs(final PlayerImpl.Builder playerBuilder, final boolean canRemove) {
        final HBox inputsBox = new HBox(10);
        inputsBox.setAlignment(Pos.CENTER);

        final TextField playerNameTextField = new TextField(playerBuilder.nameOrDefault());
        playerNameTextField.setOnKeyPressed(e -> {
            final String newName = playerNameTextField.getText();
            if (newName.isBlank()) {
                playerBuilder.name(null);
                playerNameTextField.setText(playerBuilder.nameOrDefault());
                playerNameTextField.selectAll();
            } else {
                playerBuilder.name(newName);
            }
        });

        final ComboBox<Config.AvailableAiControllers> cpuSelection = new ComboBox<>();
        cpuSelection.getStyleClass().add("combo-box");
        cpuSelection.getItems().add(null);
        cpuSelection.getItems().addAll(Config.AvailableAiControllers.values());
        final Callback<ListView<Config.AvailableAiControllers>, ListCell<Config.AvailableAiControllers>> cpuSelectionCellFactory = p -> new ListCell<>() {
            @Override
            protected void updateItem(final Config.AvailableAiControllers item, final boolean empty) {
                super.updateItem(item, empty);
                playerBuilder.ai(item);
                if (empty || item == null) {
                    setText("Human Player");
                } else {
                    setText(item.name().substring(0, 1).toUpperCase() + item.name().substring(1).toLowerCase());
                }
            }
        };
        cpuSelection.setCellFactory(cpuSelectionCellFactory);
        cpuSelection.setButtonCell(cpuSelectionCellFactory.call(null));
        cpuSelection.setValue(null);

        final ColorPicker playerColorPicker = new ColorPicker(playerBuilder.getColor());
        playerColorPicker.getStyleClass().add(ColorPicker.STYLE_CLASS_BUTTON);
        playerColorPicker.setOnAction(e -> {
            final Color newColor = playerColorPicker.getValue();
            if (players
                    .stream()
                    .filter(Predicate.not(playerBuilder::equals))
                    .anyMatch(x -> x.getColor().equals(newColor))) {
                new Alert(Alert.AlertType.ERROR, "Two Players cannot have the same color!").showAndWait();
                playerColorPicker.setValue(playerBuilder.getColor());
            } else {
                playerBuilder.color(newColor);
            }
        });

        final Button removePlayerButton = new Button(String.format("%c", 0xF0A79));
        removePlayerButton.setOnAction(e -> removePlayer(playerBuilder.getId()));
        removePlayerButton.setDisable(!canRemove);

        inputsBox.getChildren().addAll(playerNameTextField, cpuSelection, playerColorPicker, removePlayerButton);
        return inputsBox;
    }

    /**
     * Removes the player with the given id and updates the ids of the remaining
     * players.
     *
     * @param id the id of the player to remove
     */
    private void removePlayer(final int id) {
        for (int i = id - 1; i < players.size(); i++) {
            players.get(i).id(i);
        }
        players.remove(id - 1);
    }

    /**
     * Returns a new {@link PlayerImpl.Builder} for the player with the next id.
     *
     * @return a new {@link PlayerImpl.Builder} for the player with the next id
     */
    public PlayerImpl.Builder nextPlayerBuilder() {
        return new PlayerImpl.Builder(players.size() + 1);
    }
}
