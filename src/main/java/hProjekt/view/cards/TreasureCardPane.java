package hProjekt.view.cards;

import hProjekt.model.cards.CurseCard;
import hProjekt.model.cards.GoldCard;
import hProjekt.model.cards.TreasureCard;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * A specialized {@link CardPane} used to display treasure cards.
 * <p>
 * This pane sets a specific background color (brown) and displays the value of
 * the treasure (for {@link GoldCard}s) or a skull symbol (for
 * {@link CurseCard}s).
 */
public class TreasureCardPane extends CardPane {

    /**
     * Constructs a new {@code TreasureCardPane} for the specified treasure card.
     * <p>
     * Initializes the card with a brown background and vertical orientation.
     * It adds a label displaying the card's value if it is a {@link GoldCard},
     * or a skull symbol for {@link CurseCard}s.
     *
     * @param card The {@link TreasureCard} to be displayed.
     */
    public TreasureCardPane(final TreasureCard card) {
        super(Color.BROWN, false);

        final Label treasureLabel = new Label(
                card instanceof GoldCard ? Integer.toString(((GoldCard) card).value()) : String.format("%c", 0xF0BC6));
        treasureLabel.getStyleClass().add("highlighted-label");
        StackPane.setAlignment(treasureLabel, Pos.CENTER);
        getChildren().add(treasureLabel);
    }
}
