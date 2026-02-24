package hProjekt.view.overlays;

import hProjekt.model.cards.TreasureCard;
import javafx.collections.ObservableList;
import javafx.scene.layout.HBox;

/**
 * An overlay that displays the deck of treasure cards.
 * <p>
 * This class serves as a container for {@link TreasureCardsBox}, with styling
 * applied.
 * </p>
 */
public class TreasureDeckOverlay extends HBox {
    /**
     * Constructs a new {@code TreasureDeckOverlay}.
     *
     * @param treasureCards the observable list of {@link TreasureCard}s to be
     *                      displayed in the overlay
     */
    public TreasureDeckOverlay(final ObservableList<TreasureCard> treasureCards) {
        getStylesheets().add("css/main.css");
        getStyleClass().add("box");

        getChildren().add(new TreasureCardsBox(treasureCards));
    }
}
