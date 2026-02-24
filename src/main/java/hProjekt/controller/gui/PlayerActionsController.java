package hProjekt.controller.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.tudalgo.algoutils.student.annotation.DoNotTouch;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import hProjekt.controller.AmuletAction;
import hProjekt.controller.GameController;
import hProjekt.controller.PlayerController;
import hProjekt.controller.PlayerObjective;
import hProjekt.controller.actions.AcceptCurse;
import hProjekt.controller.actions.AcceptTreasure;
import hProjekt.controller.actions.CollectAmulet;
import hProjekt.controller.actions.CollectTreasure;
import hProjekt.controller.actions.ConfirmTreasureCards;
import hProjekt.controller.actions.DrawTreasureCards;
import hProjekt.controller.actions.DriveAction;
import hProjekt.controller.actions.EndTurn;
import hProjekt.controller.actions.PlayPathCard;
import hProjekt.controller.actions.PlayerAction;
import hProjekt.controller.actions.SelectPosition;
import hProjekt.controller.actions.SelectTileToRemove;
import hProjekt.controller.actions.StartDrive;
import hProjekt.controller.actions.UseAmulet;
import hProjekt.controller.gui.grid.EdgeController;
import hProjekt.controller.gui.grid.HexGridController;
import hProjekt.controller.gui.grid.TileController;
import hProjekt.controller.gui.overlays.PlayerActionsOverlayController;
import hProjekt.controller.gui.overlays.SelectionOverlayController;
import hProjekt.model.GameState;
import hProjekt.model.Player;
import hProjekt.model.PlayerState;
import hProjekt.model.cards.PathCard;
import hProjekt.model.cards.TreasureCard;
import hProjekt.model.grid.Edge;
import hProjekt.view.overlays.ConfirmationOverlay;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import javafx.scene.paint.Color;
import javafx.util.Subscription;

/**
 * This class is responsible for handling all player actions performed through
 * the UI. It ensures that the correct buttons are enabled and disabled based on
 * the current player objective and state.
 * It also ensures that the correct actions are triggered when a button is
 * clicked and that the user is prompted when a action requires user input.
 * Additionally it triggers the respective actions based on the user input.
 * <p>
 * <b>Do not touch any of the given attributes these are constructed in a way to
 * ensure thread safety.</b>
 */
public class PlayerActionsController {
    private final Property<PlayerController> playerControllerProperty = new SimpleObjectProperty<>();
    private final Property<PlayerState> playerStateProperty = new SimpleObjectProperty<>();
    private Subscription playerStateSubscription = Subscription.EMPTY;
    private final GameBoardController gameBoardController;

    private final ObservableMap<Color, List<PathCard>> treasureTrails;
    private final Property<Color> selectedTrail;

    private final ObservableSet<PathCard> pathCards;
    private final ObservableList<TreasureCard> treasureCards;

    /**
     * Constructs a new PlayerActionsController, initializing the attributes and
     * linking properties to their respective counterparts in the
     * {@link GameController} thread.
     *
     * <p>
     * It ensures that the JavaFX thread is synced with the {@link GameController}
     * and {@link PlayerController}. Additionally it ensures that the UI is updated
     * to display a consistent state.
     * </p>
     *
     * @param playerControllerProperty The property holding the current active
     *                                 {@link PlayerController}.
     * @param gameBoardController      The controller responsible for game board
     *                                 interactions.
     * @param treasureTrails           An observable map of all treasure trails.
     * @param pathCards                An observable set of {@link PathCard}s to be
     *                                 displayed by the UI.
     * @param treasureCards            An observable list of {@link TreasureCard}s
     *                                 to be displayed by the UI.
     * @param selectedTrail            A property representing the currently
     *                                 selected trail color.
     */
    @DoNotTouch("This constructor ensures thread safety! Do not modify it!")
    public PlayerActionsController(
            final Property<PlayerController> playerControllerProperty,
            final GameBoardController gameBoardController, final ObservableMap<Color, List<PathCard>> treasureTrails,
            final ObservableSet<PathCard> pathCards, final ObservableList<TreasureCard> treasureCards,
            final Property<Color> selectedTrail) {
        this.gameBoardController = gameBoardController;
        this.playerControllerProperty.setValue(playerControllerProperty.getValue());
        this.treasureTrails = treasureTrails;
        this.pathCards = pathCards;
        this.treasureCards = treasureCards;
        this.selectedTrail = selectedTrail;

        this.playerControllerProperty.subscribe((oldValue, newValue) -> Platform.runLater(() -> {
            playerStateSubscription.unsubscribe();
            playerStateSubscription = newValue.getPlayerStateProperty().subscribe(
                    (oldState, newState) -> Platform.runLater(() -> playerStateProperty.setValue(newState)));
            playerStateProperty.setValue(newValue.getPlayerStateProperty().getValue());
        }));

        playerControllerProperty.subscribe((oldValue, newValue) -> Platform.runLater(() -> {
            if (newValue == null) {
                return;
            }
            this.playerControllerProperty.setValue(newValue);
        }));

        playerStateProperty.subscribe((oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            updateUIBasedOnObjective(newValue.playerObjective());
        });

        Platform.runLater(() -> {
            this.playerControllerProperty.setValue(playerControllerProperty.getValue());
            if (getPlayerController() != null) {
                playerStateProperty
                        .setValue(getPlayerController().getPlayerState());
                updateUIBasedOnObjective(getPlayerObjective());
            }
        });
    }

    /**
     * Returns the player actions overlay.
     *
     * @return the player actions overlay
     */
    @DoNotTouch
    private PlayerActionsOverlayController getPlayerActionsOverlayController() {
        return gameBoardController.getPlayerActionsOverlayController();
    }

    /**
     * Returns the player controller that is currently active.
     * Please do not use this method to get the playerState or playerObjective.
     * Use the {@link #getPlayerState()} and {@link #getPlayerObjective()} instead.
     *
     * @return the player controller that is currently active
     */
    @DoNotTouch
    private PlayerController getPlayerController() {
        return playerControllerProperty.getValue();
    }

    /**
     * Returns the player state of the player that is currently active.
     *
     * @return the player state of the player that is currently active
     */
    @DoNotTouch
    private PlayerState getPlayerState() {
        return playerStateProperty.getValue();
    }

    /**
     * Returns the player objective of the player that is currently active.
     *
     * @return the player objective of the player that is currently active
     */
    @DoNotTouch
    private PlayerObjective getPlayerObjective() {
        return getPlayerState().playerObjective();
    }

    /**
     * Returns the player that is currently active.
     *
     * @return the player that is currently active
     */
    @DoNotTouch
    private Player getPlayer() {
        return getPlayerController().getPlayer();
    }

    /**
     * Returns the hex grid controller.
     *
     * @return the hex grid controller
     */
    @DoNotTouch
    private HexGridController getHexGridController() {
        return gameBoardController.getHexGridController();
    }

    /**
     * Updates the UI based on the given objective and its allowed actions. This
     * includes enabling and disabling buttons and prompting the user if necessary.
     * Also redraws the game board and updates the player information.
     *
     * @param objective the objective to update the UI for
     */
    @DoNotTouch
    private void updateUIBasedOnObjective(final PlayerObjective objective) {
        System.out.println("objective: " + objective);
        resetUiToBaseState();
        updatePlayerInformation();
        getHexGridController().getTileControllers().forEach(tc -> tc.getBuilder().update());

        if (getPlayer().isAi()) {
            return;
        }

        final Set<Class<? extends PlayerAction>> allowedActions = getPlayerObjective().getAllowedActions();

        if (allowedActions.contains(DriveAction.class)) {
            updateDriveableTiles();
        }
        if (allowedActions.contains(PlayPathCard.class)) {
            playPathCard();
        }
        if (allowedActions.contains(DrawTreasureCards.class)) {
            drawTreasureCards();
        }
        if (allowedActions.contains(ConfirmTreasureCards.class)) {
            confirmTreasureCards();
        }
        if (allowedActions.contains(AcceptTreasure.class)) {
            acceptTreasure();
        }
        if (allowedActions.contains(AcceptCurse.class)) {
            acceptCurse();
        }
        if (allowedActions.contains(SelectPosition.class)) {
            selectPosition();
        }
        if (allowedActions.contains(StartDrive.class)) {
            getPlayerActionsOverlayController().driveEnabledProperty().set(true);
        }
        if (allowedActions.contains(CollectAmulet.class)
                && getHexGridController().getHexGrid().getTileAt(getPlayer().getPosition()).hasAmulet()) {
            getPlayerActionsOverlayController().collectAmuletEnabledProperty().set(true);
        }
        if (allowedActions.contains(CollectTreasure.class) && !getPlayerState().collectableTreasures().isEmpty()) {
            getPlayerActionsOverlayController().collectTreasureEnabledProperty().set(true);
        }
        if (allowedActions.contains(EndTurn.class)) {
            getPlayerActionsOverlayController().endTurnEnabledProperty().set(true);
        }
        if (allowedActions.contains(UseAmulet.class) && getPlayer().getAmulets() > 0) {
            getPlayerActionsOverlayController().useAmuletEnabledProperty().set(true);
        }
        if (allowedActions.contains(SelectTileToRemove.class)) {
            selectTileToRemove();
        }
    }

    /**
     * Resets the UI to the base state. This includes disabling all buttons and
     * hiding all overlays.
     * Also resets listeners and subscriptions.
     */
    @DoNotTouch
    public void resetUiToBaseState() {
        getHexGridController().getEdgeControllers().forEach(EdgeController::hideLabel);
        gameBoardController.hideColorSelectionOverlay();
        gameBoardController.hideConfirmationOverlay();
        gameBoardController.unhighlightPathCards();
        getPlayerActionsOverlayController().driveEnabledProperty().set(false);
        getPlayerActionsOverlayController().collectAmuletEnabledProperty().set(false);
        getPlayerActionsOverlayController().collectTreasureEnabledProperty().set(false);
        getPlayerActionsOverlayController().endTurnEnabledProperty().set(false);
        gameBoardController.setOnColorSelected(c -> {});
        gameBoardController.setOnColorSelectionCancelled(() -> {});

        removeAllHighlights();
    }

    /**
     * Updates the player information (e.g. path cards, treasure cards) and the
     * state of the treasure trails in the UI.
     */
    @DoNotTouch
    public void updatePlayerInformation() {
        pathCards.clear();
        pathCards.addAll(getPlayer().getPathCards());

        treasureTrails.clear();
        treasureTrails.putAll(gameBoardController.getGameState().getTreasureTrails());

        treasureCards.clear();
        treasureCards.addAll(getPlayer().getGoldCards());

        gameBoardController.markTreasureTrail(selectedTrail.getValue());
    }

    /**
     * Removes all highlights from the game board.
     */
    @DoNotTouch
    public void removeAllHighlights() {
        getHexGridController().getEdgeControllers().forEach(EdgeController::unhighlight);
        getHexGridController().unhighlightTiles();
        getHexGridController().getTileControllers().forEach(TileController::removeMouseEnteredHandler);
        gameBoardController.unhighlightPathCards();
    }

    /**
     * Triggers the {@link EndTurn} action for the current player.
     */
    public void endTurn() {
        getPlayerController().triggerAction(new EndTurn());
    }

    /**
     * Triggers the {@link StartDrive} action for the current player.
     */
    public void startDrive() {
        getPlayerController().triggerAction(new StartDrive());
    }

    /**
     * Triggers the {@link CollectAmulet} action for the current player.
     */
    public void collectAmulet() {
        getPlayerController().triggerAction(new CollectAmulet());
    }

    /**
     * This method highlights all tiles a player can drive to and makes them
     * clickable.
     * <p>
     * If a tile is clicked the player is animated to drive to that tile, a
     * {@link DriveAction} is triggered and all tiles are unhighlighted.
     */
    @DoNotTouch
    public void updateDriveableTiles() {
        getPlayerState().drivableTiles()
                .forEach(tile -> getHexGridController().getTileControllersMap().get(tile).highlight(event -> {
                    getHexGridController().unhighlightTiles();
                    try {
                        gameBoardController.getPlayerAnimationController(getPlayer())
                                .animatePlayer(getHexGridController()
                                        .getHexGrid()
                                        .findPath(getPlayer().getPosition(), tile.getPosition(),
                                                Set.of(getHexGridController().getHexGrid().getEdges().values()
                                                        .toArray(Edge[]::new)),
                                                (x, y) -> 1))
                                .setOnFinished(actionEvent -> getPlayerController()
                                        .triggerAction(new DriveAction(tile)));
                    } catch (final IllegalArgumentException e) {
                        getPlayerController().triggerAction(new DriveAction(tile));
                    }
                }));
    }

    /**
     * Prompts the user to select a treasure trail from the
     * {@link PlayerState#collectableTreasures() collectableTreasures} to collect if
     * there are available treasures by displaying a color selection overlay
     * ({@link GameBoardController#showColorSelectionOverlay()}).
     * When a color is selected the {@link CollectTreasure} action is triggered.
     * <p>
     * After a color is selected or the selection is cancelled, the color selection
     * overlay gets hidden.
     *
     * @see PlayerController#triggerAction(PlayerAction)
     * @see PlayerState#collectableTreasures()
     * @see GameBoardController#showColorSelectionOverlay()
     */
    @StudentImplementationRequired("P3.1")
    public void collectTreasure() {
        // TODO: P3.1
        org.tudalgo.algoutils.student.Student.crash("P3.1 - Remove if implemented");
    }

    /**
     * Prompts the user to select an {@link AmuletAction} to use by displaying a
     * {@link SelectionOverlayController}.
     * <p>
     * The selection overlay is populated with all available amulet actions and
     * their human readable strings. The user can select one of the actions or
     * cancel the selection.
     * <p>
     * When an action is selected the {@link UseAmulet} action is triggered.
     * <p>
     * After an action is selected or the selection is cancelled, the selection
     * overlay gets hidden.
     *
     * @see AmuletAction
     * @see UseAmulet
     * @see GameBoardController#addSelectionOverlay(SelectionOverlayController)
     * @see SelectionOverlayController
     */
    @StudentImplementationRequired("P3.2")
    public void useAmulet() {
        // TODO: P3.2
        org.tudalgo.algoutils.student.Student.crash("P3.2 - Remove if implemented");
    }

    /**
     * Prompts the user to select a position on the hex grid by highlighting all
     * tiles ({@link HexGridController#highlightTiles(Consumer)}) and making them
     * clickable.
     * <p>
     * When a tile is clicked all tile highlights are removed, the player's position
     * is updated and shown, and the {@link SelectPosition} action is triggered.
     */
    @DoNotTouch
    private void selectPosition() {
        getHexGridController().highlightTiles(tile -> {
            getHexGridController().unhighlightTiles();
            gameBoardController.getPlayerAnimationController(getPlayer()).setPosition(tile.getPosition());
            gameBoardController.getPlayerAnimationController(getPlayer()).showPlayer();
            getPlayerController().triggerAction(new SelectPosition(tile.getPosition()));
        });
    }

    /**
     * Prompts the user to select a tile to remove from the
     * {@link GameBoardController#getSelectedTrail() selected treasure trail}
     * by highlighting all valid tiles ({@link TileController#highlight(Consumer)})
     * of the selected treasure trail and making them clickable.
     * <p>
     * When a tile is clicked all tile highlights are removed and the
     * {@link SelectTileToRemove} action is triggered.
     *
     * @see GameState#evaluateTreasureTrail(Color)
     * @see SelectTileToRemove
     * @see HexGridController#getTileControllers()
     * @see GameBoardController#getSelectedTrail()
     * @see TileController#highlight(Consumer)
     */
    @StudentImplementationRequired("P3.2")
    private void selectTileToRemove() {
        // TODO: P3.2
        org.tudalgo.algoutils.student.Student.crash("P3.2 - Remove if implemented");
    }

    /**
     * Prompts the user to play a {@link PathCard} by highlighting all PathCards
     * ({@link GameBoardController#highlightPathCards(Consumer)}). If a card is
     * selected, the user is prompted to select a color (Treasure Trail) to add the
     * card to.
     * <p>
     * If the selected card can be added to the selected treasure trail, the
     * {@link PlayPathCard} action is triggered. If not, an error message is shown
     * and the user is prompted to select a card again.
     */
    private void playPathCard() {
        final AtomicReference<PathCard> selectedCard = new AtomicReference<>(null);
        final AtomicReference<Color> selectedColor = new AtomicReference<>(null);

        gameBoardController.highlightPathCards(card -> {
            selectedCard.set(card);
            gameBoardController.unhighlightPathCards();
            gameBoardController.setAvailableColors(new ArrayList<>(treasureTrails.keySet()));
            gameBoardController.setOnColorSelected(color -> {
                if (!getPlayerState().validPathCards().getOrDefault(color, List.of()).contains(selectedCard.get())) {
                    gameBoardController.setColorError("Card cannot be added to selected Treasuretrail!");
                    return;
                }
                selectedColor.set(color);
                gameBoardController.hideColorSelectionOverlay();
                getPlayerController().triggerAction(new PlayPathCard(selectedCard.get(), selectedColor.get()));
            });
            gameBoardController.setOnColorSelectionCancelled(() -> {
                gameBoardController.hideColorSelectionOverlay();
                playPathCard();
            });
            gameBoardController.showColorSelectionOverlay();
        });
    }

    /**
     * Prompts the user to draw treasure cards by displaying the
     * {@link ConfirmationOverlay}. The overlay is cleared before showing.
     * <p>
     * When the user confirms, the {@link DrawTreasureCards} action is triggered.
     */
    private void drawTreasureCards() {
        final ConfirmationOverlay drawTreasureCardsOverlay = gameBoardController.getConfirmationOverlay();
        drawTreasureCardsOverlay.setYesButtonAction("Draw treasure cards",
                () -> getPlayerController().triggerAction(new DrawTreasureCards()));
        drawTreasureCardsOverlay.clearTreasureCards();

        gameBoardController.showConfirmationOverlay();
    }

    /**
     * Prompts the user to confirm the drawn treasure cards by displaying the
     * {@link ConfirmationOverlay}. The drawn treasure cards are set in the overlay.
     * <p>
     * When the user confirms, the {@link ConfirmTreasureCards} action is triggered.
     */
    private void confirmTreasureCards() {
        final ConfirmationOverlay drawTreasureCardsOverlay = gameBoardController.getConfirmationOverlay();
        drawTreasureCardsOverlay.setYesButtonAction("Confirm treasure cards",
                () -> getPlayerController().triggerAction(new ConfirmTreasureCards()));
        drawTreasureCardsOverlay
                .setTreasureCards(getPlayerState().drawnTreasureCards());

        gameBoardController.showConfirmationOverlay();
    }

    /**
     * Prompts the user to accept or decline an offered treasure card by displaying
     * the {@link ConfirmationOverlay}. The offered treasure card is set in the
     * overlay.
     * <p>
     * The {@link AcceptTreasure} action is triggered based on the user's choice.
     */
    private void acceptTreasure() {
        final ConfirmationOverlay confirmationOverlay = gameBoardController.getConfirmationOverlay();
        confirmationOverlay.setYesButtonAction("Accept treasure",
                () -> getPlayerController().triggerAction(new AcceptTreasure(true)));
        confirmationOverlay.setNoButtonAction("Decline treasure",
                () -> getPlayerController().triggerAction(new AcceptTreasure(false)));
        confirmationOverlay.setTreasureCards(List.of(getPlayerState().offeredCard()));
        confirmationOverlay.setMessage("You have been offered a treasure card.\nDo you want to accept it?");
        gameBoardController.showConfirmationOverlay();
    }

    /**
     * Prompts the user to accept a curse by displaying the
     * {@link ConfirmationOverlay}.
     * <p>
     * The {@link AcceptCurse} action is triggered when the user confirms.
     * It is not possible to decline a curse therefore no decline option is shown.
     */
    private void acceptCurse() {
        final ConfirmationOverlay confirmationOverlay = gameBoardController.getConfirmationOverlay();
        confirmationOverlay.setYesButtonAction("Accept Curse",
                () -> getPlayerController().triggerAction(new AcceptCurse()));
        confirmationOverlay.setNoButtonAction(null, null);
        confirmationOverlay.setMessage(
                "Oh no! There was a curse in the treasure.\nIf you have an anmulet it will be used automatically otherwise you will lose your highest value gold card.\n(You have to accept the curse)");
        gameBoardController.showConfirmationOverlay();
    }

    @Override
    public String toString() {
        return "PlayerActionsController[" +
                "playerControllerProperty=" + playerControllerProperty +
                ", playerStateProperty=" + playerStateProperty +
                ", treasureTrails=" + treasureTrails +
                ", pathCards=" + pathCards +
                ", treasureCards=" + treasureCards +
                ", selectedTrail=" + selectedTrail +
                "]";
    }
}
