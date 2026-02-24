package hProjekt.view;

import java.util.List;
import java.util.function.Consumer;

import hProjekt.model.Player;
import hProjekt.model.cards.PathCard;
import hProjekt.model.cards.TreasureCard;
import hProjekt.view.overlays.ColorSelectionOverlay;
import hProjekt.view.overlays.ConfirmationOverlay;
import hProjekt.view.overlays.GameInformationOverlay;
import hProjekt.view.overlays.PathCardsOverlay;
import hProjekt.view.overlays.PlayerActionsOverlay;
import hProjekt.view.overlays.SelectionOverlay;
import hProjekt.view.overlays.TreasureDeckOverlay;
import hProjekt.view.overlays.TreasureTrailOverlay;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Builder;

/**
 * The builder for the game board.
 * It creates the layout for the game board, including the map, overlays, and
 * the end game button.
 */
public class GameBoardBuilder implements Builder<Region> {

    private final Region map;
    private final Consumer<ActionEvent> endButtonAction;
    private final Property<Player> activePlayer;
    private final IntegerProperty currentRound;
    private final ObservableMap<Color, List<PathCard>> treasureTrails;
    private final ObservableList<TreasureCard> treasureCards;
    private final PathCardsOverlay pathCardsOverlay;
    private final ColorSelectionOverlay colorSelectionOverlay = new ColorSelectionOverlay();
    private final ConfirmationOverlay confirmationOverlay = new ConfirmationOverlay();
    private final Pane centerContainer = new VBox(10);
    private final Runnable centerAction;
    private final PlayerActionsOverlay playerActionsOverlay;
    private final Property<Color> selectedTrail;
    private SelectionOverlay<?> selectionOverlay;

    /**
     * Creates a new game board builder.
     *
     * @param map             the map to display
     * @param endButtonAction the action to execute when the end button is
     *                        pressed
     * @param activePlayer    the active player property
     * @param currentRound    the current round property
     * @param treasureTrails  the treasure trails map
     * @param pathCards       the set of path cards
     * @param treasureCards   the list of treasure cards
     * @param centerAction    the action to execute when centering the view
     * @param selectedTrail   the selected trail property
     */
    public GameBoardBuilder(final Region map,
            final Consumer<ActionEvent> endButtonAction,
            final Property<Player> activePlayer,
            final IntegerProperty currentRound,
            final ObservableMap<Color, List<PathCard>> treasureTrails,
            final ObservableSet<PathCard> pathCards,
            final ObservableList<TreasureCard> treasureCards,
            final Runnable centerAction,
            final Property<Color> selectedTrail,
            final PlayerActionsOverlay playerActionsOverlay) {
        this.map = map;
        this.endButtonAction = endButtonAction;
        this.activePlayer = activePlayer;
        this.currentRound = currentRound;
        this.treasureTrails = treasureTrails;
        this.treasureCards = treasureCards;
        this.pathCardsOverlay = new PathCardsOverlay(pathCards);
        this.centerAction = centerAction;
        this.playerActionsOverlay = playerActionsOverlay;
        this.selectedTrail = selectedTrail;
    }

    @Override
    public Region build() {
        final BorderPane mapRoot = new BorderPane();
        mapRoot.setCenter(map);

        final Button endScreenButton = new Button("Stop Game");
        endScreenButton.setOnAction(endButtonAction::accept);

        final VBox topRightContainer = new VBox(endScreenButton);
        topRightContainer.setMaxWidth(Region.USE_PREF_SIZE);
        topRightContainer.setMaxHeight(Region.USE_PREF_SIZE);

        final VBox topLeftContainer = new VBox(10);
        topLeftContainer.setMaxWidth(Region.USE_PREF_SIZE);

        final TreasureTrailOverlay treasureTrailOverlay = new TreasureTrailOverlay(treasureTrails, selectedTrail);
        topLeftContainer.getChildren().add(treasureTrailOverlay);

        final VBox bottomCenterContainer = new VBox(10);
        bottomCenterContainer.setMaxWidth(Region.USE_PREF_SIZE);
        bottomCenterContainer.setMaxHeight(Region.USE_PREF_SIZE);
        bottomCenterContainer.setAlignment(Pos.BOTTOM_CENTER);

        final Pane cardsContainer = new HBox(10);
        cardsContainer.getChildren().addAll(
                pathCardsOverlay,
                new TreasureDeckOverlay(treasureCards));
        cardsContainer.setMaxWidth(Region.USE_PREF_SIZE);
        cardsContainer.setMaxHeight(Region.USE_PREF_SIZE);

        playerActionsOverlay.setAlignment(Pos.BOTTOM_CENTER);
        bottomCenterContainer.getChildren().addAll(playerActionsOverlay, cardsContainer);
        bottomCenterContainer.setPickOnBounds(false);

        final Pane topCenterContainer = new GameInformationOverlay(activePlayer, currentRound);
        topCenterContainer.setMaxHeight(Region.USE_PREF_SIZE);
        topCenterContainer.setMaxWidth(Region.USE_PREF_SIZE);

        final VBox bottomLeftContainer = new VBox();
        bottomLeftContainer.setMaxWidth(Region.USE_PREF_SIZE);
        bottomLeftContainer.setMaxHeight(Region.USE_PREF_SIZE);
        bottomLeftContainer.setSpacing(10);
        bottomCenterContainer.setAlignment(Pos.BOTTOM_LEFT);

        final Button centerButton = new Button("Center Map");
        centerButton.setOnAction(e -> centerAction.run());

        bottomLeftContainer.getChildren().addAll(centerButton);

        centerContainer.setMaxHeight(Region.USE_PREF_SIZE);
        centerContainer.setMaxWidth(Region.USE_PREF_SIZE);

        final StackPane topLeftWrapper = new StackPane(topLeftContainer);
        topLeftWrapper.setAlignment(Pos.TOP_LEFT);
        topLeftWrapper.setPickOnBounds(false);

        final StackPane topRightWrapper = new StackPane(topRightContainer);
        topRightWrapper.setAlignment(Pos.TOP_RIGHT);
        topRightWrapper.setPickOnBounds(false);

        final NumberBinding maxTopWidth = Bindings.max(topLeftContainer.widthProperty(),
                topRightContainer.widthProperty());
        topLeftWrapper.minWidthProperty().bind(maxTopWidth);
        topRightWrapper.minWidthProperty().bind(maxTopWidth);

        final BorderPane topContainer = new BorderPane();
        topContainer.setLeft(topLeftWrapper);
        topContainer.setRight(topRightWrapper);
        topContainer.setCenter(topCenterContainer);
        topContainer.setPickOnBounds(false);
        BorderPane.setAlignment(topCenterContainer, Pos.TOP_CENTER);

        final BorderPane bottomContainer = new BorderPane();
        bottomContainer.setLeft(bottomLeftContainer);
        bottomContainer.setCenter(bottomCenterContainer);
        bottomContainer.setPickOnBounds(false);
        BorderPane.setAlignment(bottomCenterContainer, Pos.BOTTOM_CENTER);
        BorderPane.setAlignment(bottomLeftContainer, Pos.BOTTOM_LEFT);

        final BorderPane overlayContainer = new BorderPane();
        overlayContainer.getStyleClass().add("container");
        overlayContainer.setTop(topContainer);
        overlayContainer.setBottom(bottomContainer);
        overlayContainer.setPickOnBounds(false);

        StackPane.setAlignment(centerContainer, Pos.CENTER);

        topRightContainer.setPickOnBounds(false);
        topLeftContainer.setPickOnBounds(false);
        topCenterContainer.setPickOnBounds(false);
        cardsContainer.setPickOnBounds(false);
        bottomLeftContainer.setPickOnBounds(false);

        final StackPane root = new StackPane(mapRoot, overlayContainer, centerContainer);
        root.getStylesheets().add("css/main.css");

        return root;
    }

    /**
     * Adds the {@link ColorSelectionOverlay} to the center container if it's not
     * already present.
     * If the overlay is already present, this method does nothing.
     */
    public void addColorSelectionOverlay() {
        if (centerContainer.getChildren().contains(colorSelectionOverlay)) {
            return;
        }
        centerContainer.getChildren().add(colorSelectionOverlay);
    }

    /**
     * Removes the {@link ColorSelectionOverlay} from the center container if it's
     * present.
     * If the overlay is not present, this method does nothing.
     */
    public void removeColorSelectionOverlay() {
        centerContainer.getChildren().remove(colorSelectionOverlay);
        colorSelectionOverlay.setOnConfirmAction(null);
        colorSelectionOverlay.clearError();
    }

    /**
     * Sets the handler to call when a color is selected in the
     * {@link ColorSelectionOverlay}.
     *
     * @param action the action to execute when a color is selected. The selected
     *               color is passed as an argument to the action.
     */
    public void setOnColorSelected(final Consumer<Color> action) {
        colorSelectionOverlay.setOnConfirmAction(action);
    }

    /**
     * Sets the handler to call when the color selection is cancelled.
     *
     * @param action the action to execute when the color selection is cancelled
     */
    public void setOnColorSelectionCancelled(final Runnable action) {
        colorSelectionOverlay.setOnCancelAction(action);
    }

    /**
     * Sets the available colors for selection in the {@link ColorSelectionOverlay}.
     *
     * @param colors the list of available colors
     */
    public void setAvailableColors(final List<Color> colors) {
        colorSelectionOverlay.setColors(colors);
    }

    /**
     * Sets an error message in the {@link ColorSelectionOverlay}.
     *
     * @param text the error message to display
     */
    public void setColorError(final String text) {
        colorSelectionOverlay.setError(text);
    }

    /**
     * Adds the given {@link SelectionOverlay} to the center container if it's not
     * already present.
     * If the overlay is already present, this method does nothing.
     *
     * @param overlay the {@link SelectionOverlay} to add
     */
    public void addSelectionOverlay(final SelectionOverlay<?> overlay) {
        if (centerContainer.getChildren().contains(overlay)) {
            return;
        }
        selectionOverlay = overlay;
        centerContainer.getChildren().add(overlay);
    }

    /**
     * Removes the {@link SelectionOverlay} from the center container if it's
     * present.
     * If the overlay is not present, this method does nothing.
     */
    public void removeSelectionOverlay() {
        centerContainer.getChildren().remove(selectionOverlay);
        selectionOverlay.setOnConfirmAction(null);
        selectionOverlay.clearError();
        selectionOverlay = null;
    }

    /**
     * Highlights the {@link PathCard}s and sets a handler for when a card is
     * selected.
     *
     * @param action the action to execute when a card is selected. The selected
     *               card is passed as an argument to the action.
     */
    public void highlightPathCards(final Consumer<PathCard> action) {
        pathCardsOverlay.highlightCards(action);
    }

    /**
     * Unhighlights the {@link PathCard}s and removes the handler for when a card is
     * selected.
     */
    public void unhighlightPathCards() {
        pathCardsOverlay.unhighlightCards();
    }

    public void addConfirmationOverlay() {
        if (centerContainer.getChildren().contains(confirmationOverlay)) {
            return;
        }
        centerContainer.getChildren().add(confirmationOverlay);
    }

    /**
     * Removes the {@link ConfirmationOverlay} from the center container if it's
     * present and clears its state.
     */
    public void removeConfirmationOverlay() {
        centerContainer.getChildren().remove(confirmationOverlay);
        confirmationOverlay.clearTreasureCards();
        confirmationOverlay.setYesButtonAction(null, null);
        confirmationOverlay.setNoButtonAction(null, null);
        confirmationOverlay.setMessage("");
    }

    /**
     * Returns the {@link ConfirmationOverlay} instance used in this builder.
     *
     * @return the {@link ConfirmationOverlay} instance used in this builder
     */
    public ConfirmationOverlay getConfirmationOverlay() {
        return confirmationOverlay;
    }

    /**
     * Returns the {@link PlayerActionsOverlay} instance used in this builder.
     *
     * @return the {@link PlayerActionsOverlay} instance used in this builder
     */
    public PlayerActionsOverlay getPlayerActionsOverlay() {
        return playerActionsOverlay;
    }
}
