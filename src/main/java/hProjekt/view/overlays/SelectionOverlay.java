package hProjekt.view.overlays;

import java.util.Map;
import java.util.function.Consumer;

import hProjekt.view.utils.ErrorBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * An overlay that allows the user to make a selection from a set of options.
 * <p>
 * This generic class provides a UI for selecting an item of type
 * {@code SelectionType}.
 * It includes a title, a group of toggle buttons for the available choices, an
 * error display area, and "Confirm" and "Cancel" buttons.
 * </p>
 *
 * @param <SelectionType> the type of the items being selected
 */
public class SelectionOverlay<SelectionType> extends VBox {
    private final ErrorBox errorBox = new ErrorBox();
    protected final ToggleGroup selectionToggleGroup = new ToggleGroup();
    protected final HBox selectionButtonBox = new HBox(10);
    private final Button confirmButton = new Button("Confirm");
    private final Button cancelButton = new Button("Cancel");

    /**
     * Constructs a new {@code SelectionOverlay}.
     * <p>
     * Sets up the layout, stylesheets, and initializes the title, selection
     * container, error box, and control buttons (Confirm, Cancel).
     * </p>
     */
    public SelectionOverlay() {
        getStylesheets().add("css/main.css");
        getStyleClass().add("box");
        setSpacing(10);
        setAlignment(Pos.CENTER);

        final Label title = new Label("Select a color:");
        title.getStyleClass().add("text-title");
        getChildren().add(title);

        selectionButtonBox.setAlignment(Pos.CENTER);
        getChildren().add(selectionButtonBox);

        getChildren().add(errorBox);

        final BorderPane buttonBox = new BorderPane();
        BorderPane.setAlignment(confirmButton, Pos.CENTER);
        BorderPane.setAlignment(cancelButton, Pos.CENTER);
        BorderPane.setMargin(confirmButton, new Insets(0, 20, 0, 0));
        BorderPane.setMargin(cancelButton, new Insets(0, 0, 0, 20));

        buttonBox.setLeft(confirmButton);
        buttonBox.setRight(cancelButton);
        getChildren().add(buttonBox);
    }

    /**
     * Displays an error message in the overlay.
     *
     * @param text the error message to display
     */
    public void setError(final String text) {
        errorBox.setError(text);
    }

    /**
     * Clears the currently displayed error message.
     */
    public void clearError() {
        errorBox.clearError();
    }

    /**
     * Sets the action to be executed when the confirm button is clicked.
     * <p>
     * The action is only executed if a selection has been made. If no item is
     * selected, an error message is displayed and the action is not run.
     * </p>
     *
     * @param action a {@link Consumer} that accepts the selected item of type
     *               {@code SelectionType}
     */
    @SuppressWarnings("unchecked")
    public void setOnConfirmAction(final Consumer<SelectionType> action) {
        confirmButton.setOnAction(e -> {
            if (selectionToggleGroup.getSelectedToggle() == null) {
                setError("No color selected!");
                return;
            }

            clearError();
            action.accept((SelectionType) selectionToggleGroup.getSelectedToggle().getUserData());
        });
    }

    /**
     * Sets the action to be executed when the cancel button is clicked.
     *
     * @param action the {@link Runnable} to execute on cancellation
     */
    public void setOnCancelAction(final Runnable action) {
        cancelButton.setOnAction(e -> {
            clearError();
            action.run();
        });
    }

    /**
     * Populates the overlay with selectable options.
     * <p>
     * Creates a toggle button for each entry in the provided map. The map values
     * are used as button labels, and the map keys are stored as user data for the
     * buttons.
     * </p>
     *
     * @param selections a {@link Map} where the key is the selection item and the
     *                   value is the display text
     */
    public void setSelection(final Map<SelectionType, String> selections) {
        selectionButtonBox.getChildren().clear();

        for (final Map.Entry<SelectionType, String> entry : selections.entrySet()) {
            final ToggleButton selectionButton = new ToggleButton();
            selectionButton.setText(entry.getValue());
            selectionButton.setToggleGroup(selectionToggleGroup);
            selectionButton.setUserData(entry.getKey());
            selectionButtonBox.getChildren().add(selectionButton);
        }
    }
}
