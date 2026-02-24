package hProjekt.view.utils;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * A box that displays error messages.
 * It contains a label for the error message and a close button.
 */
public class ErrorBox extends HBox {
    private final Label errorLabel = new Label();

    /**
     * Creates a new ErrorBox.
     */
    public ErrorBox() {
        getStylesheets().add("css/main.css");
        getStyleClass().addAll("box", "error-box");
        setSpacing(10);
        setVisible(false);
        setMaxWidth(400);
        setAlignment(Pos.CENTER);

        final Button closeErrorBoxButton = new Button(String.format("%c", 0xF0156));
        closeErrorBoxButton.setOnAction(e -> clearError());
        closeErrorBoxButton.getStyleClass().add("close-button");

        errorLabel.setWrapText(true);
        errorLabel.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(errorLabel, Priority.ALWAYS);

        getChildren().addAll(errorLabel, closeErrorBoxButton);
    }

    /**
     * Sets the error message and shows the box.
     *
     * @param text the error message
     */
    public void setError(final String text) {
        errorLabel.setText(text);
        setVisible(true);
    }

    /**
     * Clears the error message and hides the box.
     */
    public void clearError() {
        errorLabel.setText("");
        setVisible(false);
    }
}
