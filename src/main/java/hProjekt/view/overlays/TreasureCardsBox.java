package hProjekt.view.overlays;

import java.util.HashMap;
import java.util.Map;

import hProjekt.model.cards.TreasureCard;
import hProjekt.view.cards.CardPane;
import hProjekt.view.cards.TreasureCardPane;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * A UI component that displays a collection of treasure cards.
 * <p>
 * This class extends {@link ScrollPane} and displays {@link TreasureCard}s in a
 * horizontal box.
 * It groups identical cards and displays the count for each unique card type.
 * The display automatically updates when the underlying list of treasure
 * cards changes.
 * </p>
 */
public class TreasureCardsBox extends ScrollPane {

    /**
     * Constructs a new {@code TreasureCardsBox}.
     * <p>
     * Initializes the layout and sets up a listener on the provided list of
     * treasure cards to update the view whenever the list changes.
     * </p>
     *
     * @param treasureCards the observable list of {@link TreasureCard}s to display
     */
    public TreasureCardsBox(final ObservableList<TreasureCard> treasureCards) {
        setPrefHeight(CardPane.defaultCardWidth * 1.5 + 50);
        setPrefWidth((CardPane.defaultCardWidth + 10) * 4);
        setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        setFitToWidth(true);
        setFitToHeight(true);

        final HBox treasureCardsBox = new HBox(10);
        setContent(treasureCardsBox);
        treasureCardsBox.setAlignment(Pos.CENTER);

        treasureCards.addListener((ListChangeListener.Change<? extends TreasureCard> change) -> updateTreasureCardPanes(
                change.getList(), treasureCardsBox));

        updateTreasureCardPanes(treasureCards, treasureCardsBox);
    }

    /**
     * Updates the displayed treasure cards based on the provided list.
     * <p>
     * This method clears the current visual representation of treasure cards and
     * repopulates it by grouping identical cards and displaying the count for each
     * unique card type.
     * Each unique card is wrapped in a {@link TreasureCardPane} along with a label
     * showing the quantity.
     * </p>
     *
     * @param treasureCards    the observable list of {@link TreasureCard}s to
     *                         display
     * @param treasureCardsBox the HBox container where the treasure card panes are
     *                         added
     */
    private void updateTreasureCardPanes(final ObservableList<? extends TreasureCard> treasureCards,
            final HBox treasureCardsBox) {
        final Map<TreasureCard, Integer> amountToTreasureCard = new HashMap<>();
        treasureCardsBox.getChildren().clear();

        for (final TreasureCard treasureCard : treasureCards) {
            amountToTreasureCard.put(treasureCard, amountToTreasureCard.getOrDefault(treasureCard, 0) + 1);
        }

        for (final Map.Entry<TreasureCard, Integer> entry : amountToTreasureCard.entrySet()) {
            final VBox treasureCardBox = new VBox(5);
            treasureCardBox.setAlignment(Pos.CENTER);
            final CardPane cardPane = new TreasureCardPane(entry.getKey());
            treasureCardBox.getChildren().add(cardPane);
            treasureCardBox.getChildren().add(new Label(entry.getValue().toString()));
            treasureCardsBox.getChildren().add(treasureCardBox);
        }
    }
}
