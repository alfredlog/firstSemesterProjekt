package hProjekt.view.cards;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * A specialized pane used to display game cards within the UI.
 * This pane extends {@link StackPane} and provides styling and sizing options
 * based on the card's background color, size, and orientation.
 */
public class CardPane extends StackPane {
    public static final int defaultCardWidth = 80;

    /**
     * Constructs a new {@code CardPane} with the specified background color and
     * orientation, using the default card width.
     *
     * @param backgroundColor The background color of the card pane.
     * @param horizontal      {@code true} if the card is oriented horizontally
     *                        (width > height), {@code false} if oriented vertically
     *                        (height > width).
     */
    public CardPane(final Color backgroundColor, final boolean horizontal) {
        this(backgroundColor, CardPane.defaultCardWidth, horizontal);
    }

    /**
     * Constructs a new {@code CardPane} with the specified background color, size
     * base, and orientation.
     * <p>
     * The dimensions of the pane are calculated based on the {@code cardSize}.
     * If {@code horizontal} is true, the dimensions are (cardSize * 1.5) x
     * cardSize.
     * If vertical, the dimensions are cardSize x (cardSize * 1.5).
     *
     * @param backgroundColor The background color of the card pane.
     * @param cardSize        The base dimension size for the card. If less than or
     *                        equal to 0, {@link #defaultCardWidth} is used.
     * @param horizontal      {@code true} if the card is oriented horizontally,
     *                        {@code false} for vertical orientation.
     */
    public CardPane(final Color backgroundColor, int cardSize, final boolean horizontal) {
        super();
        getStyleClass().add("card-pane");
        if (cardSize <= 0) {
            cardSize = CardPane.defaultCardWidth;
        }

        if (horizontal) {
            setMaxWidth(cardSize * 1.5);
            setMinWidth(cardSize * 1.5);
            setMaxHeight(cardSize);
            setMinHeight(cardSize);
        } else {
            setMaxWidth(cardSize);
            setMinWidth(cardSize);
            setMaxHeight(cardSize * 1.5);
            setMinHeight(cardSize * 1.5);
        }
        setStyle("-fx-background-color: " + backgroundColor.toString().replace("0x", "#") + ";");
    }

    /**
     * Highlights this pane to indicate it is selectable and registers a click
     * action.
     * <p>
     * This method adds the "selectable" CSS style class and sets a mouse click
     * handler.
     *
     * @param action The {@link Runnable} to execute when the pane is clicked.
     */
    public void highlight(final Runnable action) {
        setOnMouseClicked(e -> action.run());
        getStyleClass().add("selectable");
    }

    /**
     * Removes the highlight effect and the click action from this pane.
     * <p>
     * This method removes the "selectable" CSS style class and sets the mouse click
     * handler to {@code null}.
     */
    public void unhighlight() {
        setOnMouseClicked(null);
        getStyleClass().removeAll("selectable");
    }
}
