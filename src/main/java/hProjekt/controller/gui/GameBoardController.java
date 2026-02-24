package hProjekt.controller.gui;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import org.tudalgo.algoutils.student.annotation.DoNotTouch;

import hProjekt.controller.PlayerController;
import hProjekt.controller.gui.grid.HexGridController;
import hProjekt.controller.gui.grid.TileController;
import hProjekt.controller.gui.overlays.PlayerActionsOverlayController;
import hProjekt.controller.gui.overlays.SelectionOverlayController;
import hProjekt.model.GameState;
import hProjekt.model.Player;
import hProjekt.model.cards.PathCard;
import hProjekt.model.cards.TreasureCard;
import hProjekt.model.grid.Tile;
import hProjekt.view.GameBoardBuilder;
import hProjekt.view.overlays.ColorSelectionOverlay;
import hProjekt.view.overlays.ConfirmationOverlay;
import hProjekt.view.overlays.PlayerActionsOverlay;
import hProjekt.view.overlays.SelectionOverlay;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.util.Builder;

/**
 * Controller for the main game board scene.
 * <p>
 * This controller serves as the central hub for the gameplay view, delegating
 * specific UI building tasks to various UI controllers and builders and player
 * interactions to {@link PlayerActionsController}.
 */
@DoNotTouch
public class GameBoardController implements SceneController {
    private final HexGridController hexGridController;

    private final GameBoardBuilder builder;

    private final GameState gameState;
    private final Map<Player, PlayerAnimationController> playerAnimationControllers = new HashMap<>();
    private final IntegerProperty roundCounterProperty = new SimpleIntegerProperty(0);
    private final Property<Color> selectedTrail = new SimpleObjectProperty<>(null);
    private final PlayerActionsOverlayController playerActionsOverlayController;

    /**
     * Creates a new game board controller.
     * <p>
     * This constructor initializes all necessary controllers and builders. It also
     * creates bindings between the {@link GameState}, the active player, and the UI
     * components to ensure that the view reflects the current state of the game.
     * <p>
     * <b>Do not touch this constructor!</b>
     *
     * @param gameState                      the game state
     * @param activePlayerControllerProperty the active player controller property
     * @param roundCounterProperty           the round counter property
     */
    @DoNotTouch("This is setup to correctly handle thread synchronization.")
    public GameBoardController(final GameState gameState,
            final Property<PlayerController> activePlayerControllerProperty,
            final IntegerProperty roundCounterProperty) {
        this.gameState = gameState;
        final Property<Player> activePlayer = new SimpleObjectProperty<>();
        final ObservableMap<Color, List<PathCard>> treasureTrails = new SimpleMapProperty<>(
                FXCollections.observableMap(new HashMap<>(gameState.getTreasureTrails())));
        final ObservableSet<PathCard> pathCards = FXCollections.observableSet(new HashSet<>());
        final ObservableList<TreasureCard> treasureCards = new SimpleListProperty<>(
                FXCollections.observableArrayList());

        hexGridController = new HexGridController(gameState.getGrid());

        selectedTrail.subscribe(this::markTreasureTrail);

        final PlayerActionsOverlay playerActionsOverlay = new PlayerActionsOverlay();
        playerActionsOverlayController = new PlayerActionsOverlayController(playerActionsOverlay);

        builder = new GameBoardBuilder(hexGridController.buildView(),
                event -> SceneController.loadEndScreenScene(), activePlayer, this.roundCounterProperty, treasureTrails,
                pathCards, treasureCards, hexGridController::centerPaneHandler, selectedTrail, playerActionsOverlay);

        final PlayerActionsController playerActionsController = new PlayerActionsController(
                activePlayerControllerProperty,
                this, treasureTrails, pathCards, treasureCards, selectedTrail);

        playerActionsOverlayController.setDriveAction(playerActionsController::startDrive);
        playerActionsOverlayController.setCollectTreasureAction(playerActionsController::collectTreasure);
        playerActionsOverlayController.setEndTurnAction(playerActionsController::endTurn);
        playerActionsOverlayController.setCollectAmuletAction(playerActionsController::collectAmulet);
        playerActionsOverlayController.setUseAmuletAction(playerActionsController::useAmulet);

        for (final Player player : gameState.getPlayers()) {
            playerAnimationControllers.put(player,
                    new PlayerAnimationController(hexGridController.getBuilder(), player.getColor()));
        }

        activePlayerControllerProperty.subscribe((oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            System.out.println("Active player: " + newValue.getPlayer().getName());
            Platform.runLater(() -> activePlayer.setValue(newValue.getPlayer()));
        });
        roundCounterProperty.subscribe((oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            Platform.runLater(() -> this.roundCounterProperty.setValue(newValue));
        });
        gameState.getWinnerProperty().subscribe((oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            Platform.runLater(SceneController::loadEndScreenScene);
        });
    }

    /**
     * Constructs a new GameBoardController with the specified game state and hex
     * grid controller.
     * <p>
     * This constructor is primarily intended for testing purposes.
     *
     * @param gameState         the current state of the game
     * @param hexGridController the controller responsible for managing the
     *                          hexagonal grid
     */
    public GameBoardController(final GameState gameState, final HexGridController hexGridController,
            final PlayerActionsOverlayController playerActionsOverlayController) {
        this.gameState = gameState;
        this.hexGridController = hexGridController;
        builder = null;
        this.playerActionsOverlayController = playerActionsOverlayController;
    }

    /**
     * Returns the {@link HexGridController}.
     *
     * @return the {@link HexGridController}
     */
    public HexGridController getHexGridController() {
        return hexGridController;
    }

    /**
     * Returns the {@link PlayerAnimationController} for the given player.
     *
     * @param player the player
     * @return the {@link PlayerAnimationController} for the given player
     */
    public PlayerAnimationController getPlayerAnimationController(final Player player) {
        return playerAnimationControllers.get(player);
    }

    /**
     * Returns the current {@link GameState}.
     *
     * @return the current {@link GameState}
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * Returns the selected treasure trail property.
     *
     * @return the selected treasure trail property
     */
    public Property<Color> getSelectedTrail() {
        return selectedTrail;
    }

    /**
     * Marks all tiles that are part of the treasure trail with the given color.
     *
     * @param color the color of the treasure trail to mark
     *
     * @see TileController#markTreasure(Color)
     */
    public void markTreasureTrail(final Color color) {
        hexGridController.getTileControllers().forEach(TileController::unmarkTreasure);
        if (color == null) {
            return;
        }

        final Set<Tile> trail = gameState.evaluateTreasureTrail(color);
        final Map<Tile, TileController> tileControllersMap = hexGridController.getTileControllersMap();
        for (final Tile tile : trail) {
            tileControllersMap.get(tile).markTreasure(color);
        }
    }

    /**
     * Highlights the {@link PathCard}s and sets the action to be executed when a
     * {@link PathCard} is selected.
     *
     * @param action The action to be executed when a {@link PathCard} is selected.
     *
     * @see GameBoardBuilder#highlightPathCards(Consumer)
     */
    public void highlightPathCards(final Consumer<PathCard> action) {
        builder.highlightPathCards(action);
    }

    /**
     * Unhighlights the {@link PathCard}s.
     *
     * @see GameBoardBuilder#unhighlightPathCards()
     */
    public void unhighlightPathCards() {
        builder.unhighlightPathCards();
    }

    /**
     * Shows the {@link ColorSelectionOverlay}.
     * <p>
     * For the {@link ColorSelectionOverlay} to function properly, the available
     * colors and the actions to be executed on color selection and cancellation
     * must be set using {@link #setAvailableColors(List)},
     * {@link #setOnColorSelected(Consumer)}, and
     * {@link #setOnColorSelectionCancelled(Runnable)}.
     *
     * @see GameBoardBuilder#addColorSelectionOverlay()
     */
    public void showColorSelectionOverlay() {
        builder.addColorSelectionOverlay();
    }

    /**
     * Hides the {@link ColorSelectionOverlay}.
     *
     * @see GameBoardBuilder#removeColorSelectionOverlay()
     */
    public void hideColorSelectionOverlay() {
        builder.removeColorSelectionOverlay();
    }

    /**
     * Sets the error message for the {@link ColorSelectionOverlay}.
     *
     * @param text The error message to be displayed.
     *
     * @see GameBoardBuilder#setColorError(String)
     */
    public void setColorError(final String text) {
        builder.setColorError(text);
    }

    /**
     * Returns the {@link ConfirmationOverlay}.
     *
     * @return the {@link ConfirmationOverlay}
     */
    public ConfirmationOverlay getConfirmationOverlay() {
        return builder.getConfirmationOverlay();
    }

    /**
     * Shows the {@link ConfirmationOverlay}.
     *
     * @see GameBoardBuilder#addConfirmationOverlay()
     */
    public void showConfirmationOverlay() {
        builder.addConfirmationOverlay();
    }

    /**
     * Hides the {@link ConfirmationOverlay}.
     *
     * @see GameBoardBuilder#removeConfirmationOverlay()
     */
    public void hideConfirmationOverlay() {
        builder.removeConfirmationOverlay();
    }

    /**
     * Adds a {@link SelectionOverlay} to the game board.
     * <p>
     * The provided {@link SelectionOverlayController} should already have the
     * confirm and cancel actions set up, as well as any necessary error handling.
     * This method simply adds the overlay to the view; it does not configure its
     * behavior or content.
     *
     * @param overlay The {@link SelectionOverlay} to be added.
     *
     * @see GameBoardBuilder#addSelectionOverlay(SelectionOverlay)
     */
    public void addSelectionOverlay(final SelectionOverlayController<?> overlay) {
        builder.addSelectionOverlay(overlay.getOverlay());
    }

    /**
     * Removes the current {@link SelectionOverlay} from the game board.
     *
     * @see GameBoardBuilder#removeSelectionOverlay()
     */
    public void hideSelectionOverlay() {
        builder.removeSelectionOverlay();
    }

    /**
     * Sets the available colors for selection in the {@link ColorSelectionOverlay}.
     *
     * @param colors The list of available colors.
     *
     * @see GameBoardBuilder#setAvailableColors(List)
     */
    public void setAvailableColors(final List<Color> colors) {
        builder.setAvailableColors(colors);
    }

    /**
     * Sets the action to be executed when a color is selected in the
     * {@link ColorSelectionOverlay}.
     * <p>
     * It is likely that {@link #hideColorSelectionOverlay()} should be called at
     * the end of the provided action to hide the overlay after a color is selected.
     *
     * @param action The action to be executed when a color is selected.
     *
     * @see GameBoardBuilder#setOnColorSelected(Consumer)
     */
    public void setOnColorSelected(final Consumer<Color> action) {
        builder.setOnColorSelected(action);
    }

    /**
     * Sets the action to be executed when color selection is cancelled in the
     * {@link ColorSelectionOverlay}.
     * <p>
     * It is likely that {@link #hideColorSelectionOverlay()} should be called at
     * the end of the provided action to hide the overlay after cancellation.
     *
     * @param action The action to be executed on cancellation.
     *
     * @see GameBoardBuilder#setOnColorSelectionCancelled(Runnable)
     */
    public void setOnColorSelectionCancelled(final Runnable action) {
        builder.setOnColorSelectionCancelled(action);
    }

    /**
     * Returns the {@link PlayerActionsOverlay}.
     *
     * @return the {@link PlayerActionsOverlay}
     *
     * @see GameBoardBuilder#getPlayerActionsOverlay()
     */
    public PlayerActionsOverlayController getPlayerActionsOverlayController() {
        return playerActionsOverlayController;
    }

    @Override
    public Builder<Region> getBuilder() {
        return builder;
    }

    @Override
    public String getTitle() {
        return "Map";
    }
}
