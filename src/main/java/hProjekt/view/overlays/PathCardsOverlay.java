package hProjekt.view.overlays;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import hProjekt.model.cards.PathCard;
import hProjekt.view.cards.PathCardPane;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;

/**
 * An overlay component that displays a collection of {@link PathCard}s in a
 * horizontal scrollable view.
 * <p>
 * This class extends {@link HBox} and serves as a visual container for the
 * player's or game's available path cards. It observes an {@link ObservableSet}
 * of cards, automatically updating the UI whenever cards are added or removed.
 * </p>
 * <p>
 * Key features include:
 * <ul>
 * <li>Horizontal scrolling support via a constrained {@link ScrollPane}.</li>
 * <li>Automatic synchronization with the underlying data model using a
 * {@link SetChangeListener}.</li>
 * <li>Methods to highlight cards and attach interaction logic (e.g., selection
 * handlers).</li>
 * </ul>
 *
 * @see PathCard
 * @see PathCardPane
 */
public class PathCardsOverlay extends HBox {
    private final List<PathCardPane> pathCardPanes = new ArrayList<>();

    /**
     * Constructs a new overlay for displaying path cards.
     * <p>
     * This constructor initializes the overlay with specific styling and layout
     * configurations.
     * It creates a {@link ScrollPane} to hold the path cards, ensuring that the
     * container is transparent and fits the dimensions of the cards. The cards are
     * arranged horizontally within an {@link HBox}.
     * </p>
     * <p>
     * A listener is attached to the provided {@code ObservableSet} of path cards to
     * automatically update the visuals whenever the set changes (i.e., when cards
     * are added or removed).
     * </p>
     *
     * @param pathCards The observable set of {@link PathCard} objects to be
     *                  displayed in this overlay.
     *                  Changes to this set are monitored to update the UI
     *                  dynamically.
     */
    public PathCardsOverlay(final ObservableSet<PathCard> pathCards) {
        getStylesheets().add("css/main.css");
        getStyleClass().add("box");
        final ScrollPane pathCardContainer = new ScrollPane();
        pathCardContainer.setPrefHeight(PathCardPane.cardSize + 20);
        pathCardContainer.setPrefWidth((PathCardPane.cardSize * 1.5 + 10) * 4);
        pathCardContainer.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        pathCardContainer.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pathCardContainer.setFitToHeight(true);
        pathCardContainer.setFitToWidth(true);

        final HBox pathCardBox = new HBox(10);
        pathCardBox.setAlignment(Pos.CENTER);
        pathCardContainer.setContent(pathCardBox);
        getChildren().add(pathCardContainer);

        pathCards.addListener(
                (SetChangeListener.Change<? extends PathCard> cards) -> updatePathCards(cards.getSet(), pathCardBox));
        updatePathCards(pathCards, pathCardBox);
    }

    /**
     * Updates the displayed path cards based on the provided set.
     * <p>
     * This method clears the current visual representation of path cards and
     * repopulates it using the cards from the given {@code ObservableSet}.
     * Each card is wrapped in a {@link PathCardPane} for consistent styling and
     * interaction handling.
     * </p>
     *
     * @param pathCards   The observable set of {@link PathCard} objects to display.
     * @param pathCardBox The HBox container where the path card panes are added.
     */
    private void updatePathCards(final ObservableSet<? extends PathCard> pathCards, final HBox pathCardBox) {
        pathCardBox.getChildren().clear();
        pathCardPanes.clear();
        for (final PathCard pathCard : pathCards) {
            final PathCardPane pathCardPane = new PathCardPane(pathCard, false);
            pathCardPanes.add(pathCardPane);
            pathCardBox.getChildren().add(pathCardPane);
        }
    }

    /**
     * Highlights all path cards managed by this overlay and assigns a click action
     * to them.
     * <p>
     * This method iterates through all {@link PathCardPane} components within the
     * overlay and triggers their individual highlight mechanism. When a highlighted
     * card is interacted with, the specified action determines how the underlying
     * {@link PathCard} is handled.
     * </p>
     *
     * @param action the consumer function to be executed when a highlighted path
     *               card is selected; it accepts the corresponding {@link PathCard}
     *               as an argument.
     */
    public void highlightCards(final Consumer<PathCard> action) {
        for (final PathCardPane pathCardPane : pathCardPanes) {
            pathCardPane.highlight(action);
        }
    }

    /**
     * Removes highlighting from all path cards managed by this overlay.
     * <p>
     * This method iterates through all {@link PathCardPane} components within the
     * overlay and triggers their individual unhighlight mechanism, reverting any
     * visual emphasis previously applied.
     * </p>
     */
    public void unhighlightCards() {
        for (final PathCardPane pathCardPane : pathCardPanes) {
            pathCardPane.unhighlight();
        }
    }
}
