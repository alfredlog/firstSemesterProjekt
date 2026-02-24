package hProjekt.controller.gui.overlays;

import java.util.Map;
import java.util.function.Consumer;

import hProjekt.view.overlays.SelectionOverlay;

/**
 * Controller class for managing the logic and interactions of a
 * {@link SelectionOverlay}.
 * <p>
 * This controller acts as an intermediary between the {@code SelectionOverlay}
 * view component and the application logic.
 * It provides methods to configure callbacks for user actions (confirm,
 * cancel), manage error states, and populate the overlay with selectable items.
 * </p>
 *
 * @param <SelectionType> The type of objects being selected within the overlay.
 */
public class SelectionOverlayController<SelectionType> {

    /**
     * The underlying overlay view component managed by this controller.
     */
    private final SelectionOverlay<SelectionType> overlay;

    /**
     * Default constructor.
     * <p>
     * Initializes a new instance of {@link SelectionOverlay} to be managed by this
     * controller.
     * </p>
     */
    public SelectionOverlayController() {
        overlay = new SelectionOverlay<>();
    }

    /**
     * Constructor allowing the injection of an existing, specific
     * {@link SelectionOverlay}.
     *
     * @param overlay The specific {@code SelectionOverlay} instance to be
     *                controlled.
     */
    public SelectionOverlayController(final SelectionOverlay<SelectionType> overlay) {
        this.overlay = overlay;
    }

    /**
     * Retrieves the overlay view component associated with this controller.
     *
     * @return The managed {@link SelectionOverlay} instance.
     */
    public SelectionOverlay<SelectionType> getOverlay() {
        return overlay;
    }

    /**
     * Sets the action to be executed when the user confirms their selection.
     *
     * @param action A {@link Consumer} that accepts the selected
     *               item of type {@code SelectionType}.
     */
    public void setOnConfirmAction(final Consumer<SelectionType> action) {
        overlay.setOnConfirmAction(action);
    }

    /**
     * Sets the action to be executed when the user cancels the selection process.
     *
     * @param action A {@link Runnable} to be executed on cancellation.
     */
    public void setOnCancelAction(final Runnable action) {
        overlay.setOnCancelAction(action);
    }

    /**
     * Displays an error message on the overlay.
     *
     * @param text The error message string to display.
     */
    public void setError(final String text) {
        overlay.setError(text);
    }

    /**
     * Clears any currently displayed error message on the overlay.
     */
    public void clearError() {
        overlay.clearError();
    }

    /**
     * Populates the overlay with a set of selectable options.
     *
     * @param selections A map where the keys are the selectable objects of type
     *                   {@code SelectionType}
     *                   and the values are the display strings representing them.
     */
    public void setSelection(final Map<SelectionType, String> selections) {
        overlay.setSelection(selections);
    }
}
