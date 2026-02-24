package hProjekt.view.overlays;

import java.util.List;

import hProjekt.model.cards.TreasureCard;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

/**
 * Represents a reusable UI overlay used for displaying confirmation dialogs or
 * information prompts to the user.
 * <p>
 * This class extends {@link VBox} and provides a consistent layout containing:
 * <ul>
 * <li>A message label for displaying prompts or descriptions.</li>
 * <li>A display area for {@link TreasureCard}s involved in the decision
 * (optional).</li>
 * <li>An action button area usually containing a "Yes" (confirm) and optionally
 * a "No" (cancel) button.</li>
 * </ul>
 * <p>
 * The overlay is designed to be dynamically configured via its setter methods
 * to adapt to different game situations, such as confirming card selection or
 * verifying user intent.
 */
public class ConfirmationOverlay extends VBox {
    private final Button yesButton = new Button();
    private final Button noButton = new Button();
    private final ObservableList<TreasureCard> treasureCards = FXCollections.observableArrayList();

    private final Label messageLabel = new Label();
    private final HBox buttonBox = new HBox(10);

    /**
     * Constructs a new ConfirmationOverlay.
     * <p>
     * This overlay initializes its layout with specific CSS classes ("box",
     * "confirmation-overlay"), sets centering alignment, and configures the
     * internal components. It sets up listeners for the {@link #messageLabel} to
     * toggle visibility based on text content and configures a
     * {@link TreasureCardsBox} that updates its display based on changes to the
     * observable {@link #treasureCards} list.
     * </p>
     * <p>
     * The overlay also arranges action buttons within a container, applying
     * specific style classes to the confirmation buttons.
     * </p>
     */
    public ConfirmationOverlay() {
        getStylesheets().add("css/main.css");
        getStyleClass().addAll("box", "confirmation-overlay");
        setSpacing(10);
        setAlignment(Pos.CENTER);

        getChildren().add(messageLabel);
        messageLabel.textProperty().subscribe(text -> messageLabel.setVisible(text != null && !text.isBlank()));
        messageLabel.setAlignment(Pos.CENTER);
        messageLabel.setTextAlignment(TextAlignment.CENTER);
        messageLabel.setWrapText(true);

        final Region treasureCardBox = new TreasureCardsBox(treasureCards);
        final Pane treasureCardWrapper = new Pane();
        getChildren().add(treasureCardWrapper);
        treasureCards.addListener((final ListChangeListener.Change<? extends TreasureCard> c) -> {
            if (treasureCards.isEmpty()) {
                treasureCardWrapper.getChildren().clear();
            } else {
                treasureCardWrapper.getChildren().setAll(treasureCardBox);
            }
        });

        yesButton.getStyleClass().add("button-yes");
        noButton.getStyleClass().add("button-no");

        buttonBox.getChildren().addAll(yesButton);
        buttonBox.setAlignment(Pos.CENTER);
        getChildren().add(buttonBox);
    }

    /**
     * Updates the list of treasure cards displayed in the overlay.
     * <p>
     * This method replaces the current content of the {@link #treasureCards}
     * observable list with the provided list of cards.
     *
     * @param cards the new list of {@link TreasureCard}s to display.
     */
    public void setTreasureCards(final List<TreasureCard> cards) {
        treasureCards.setAll(cards);
    }

    /**
     * Configures the "Yes" or confirmation button with specific text and an action
     * to execute.
     *
     * @param text   the text to display on the button.
     * @param action the {@link Runnable} to execute when the button is clicked.
     */
    public void setYesButtonAction(final String text, final Runnable action) {
        yesButton.setText(text);
        yesButton.setOnAction(e -> action.run());
    }

    /**
     * Configures the "No" or negative action button for the overlay.
     * <p>
     * This method sets the text label and the action to be performed when the
     * button is clicked.
     * If the provided {@code action} is {@code null}, or if the {@code text} is
     * {@code null} or blank, the button is removed from the view to hide it.
     * Otherwise, the button is added (if not already present), updated with the
     * specified text, and assigned the given runnable action.
     *
     * @param text   the text to display on the button if null or blank, the button
     *               is hidden
     * @param action the {@link Runnable} to execute when the button is pressed; if
     *               null, the button is hidden
     */
    public void setNoButtonAction(final String text, final Runnable action) {
        if (action == null || text == null || text.isBlank()) {
            buttonBox.getChildren().remove(noButton);
            return;
        }
        buttonBox.getChildren().add(noButton);
        noButton.setText(text);
        noButton.setOnAction(e -> action.run());
    }

    /**
     * Sets the text to be displayed on the confirmation overlay.
     *
     * @param message the message string to be shown to the user on the overlay
     */
    public void setMessage(final String message) {
        messageLabel.setText(message);
    }

    /**
     * Clears the list of collected treasure cards.
     * This method removes all elements from the internal treasure cards collection.
     */
    public void clearTreasureCards() {
        treasureCards.clear();
    }

    /**
     * Resets the overlay to its initial state.
     * <p>
     * This method clears the displayed message, removes any associated treasure
     * cards, and resets the actions for both the "Yes" and "No" buttons to null.
     */
    public void resetOverlay() {
        setMessage("");
        setTreasureCards(List.of());
        setYesButtonAction(null, null);
        setNoButtonAction(null, null);
    }
}
