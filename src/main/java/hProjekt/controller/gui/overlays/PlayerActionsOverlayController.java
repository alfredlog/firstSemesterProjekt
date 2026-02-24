package hProjekt.controller.gui.overlays;

import hProjekt.view.overlays.PlayerActionsOverlay;
import javafx.beans.property.BooleanProperty;

/**
 * Controller class for managing player actions in the game overlay.
 * <p>
 * This controller acts as an intermediary between the game logic and the
 * {@link PlayerActionsOverlay} view component. It provides methods to configure
 * action handlers for various player actions such as driving, ending turns,
 * collecting treasures and amulets, and using amulets.
 * </p>
 * <p>
 * The controller also exposes JavaFX {@link BooleanProperty} instances that
 * allow binding to the enabled/disabled state of each action button in the
 * overlay.
 * </p>
 *
 * @see PlayerActionsOverlay
 */
public class PlayerActionsOverlayController {
    private final PlayerActionsOverlay overlay;

    /**
     * Creates a new instance of {@code PlayerActionsOverlayController} with a
     * default
     * {@link PlayerActionsOverlay}.
     */
    public PlayerActionsOverlayController() {
        this.overlay = new PlayerActionsOverlay();
    }

    /**
     * Creates a new instance of {@code PlayerActionsOverlayController} with the
     * given
     * {@link PlayerActionsOverlay}.
     * <p>
     * This constructor allows for dependency injection of the overlay, enabling
     * greater flexibility and testability.
     * </p>
     *
     * @param overlay the {@link PlayerActionsOverlay} instance to be managed by
     *                this controller
     */
    public PlayerActionsOverlayController(final PlayerActionsOverlay overlay) {
        this.overlay = overlay;
    }

    /**
     * Sets the action to be performed when the drive button is clicked.
     *
     * @param action the {@link Runnable} to execute for the drive action
     */
    public void setDriveAction(final Runnable action) {
        overlay.setDriveAction(action);
    }

    /**
     * Sets the action to be performed when the end turn button is clicked.
     *
     * @param action the {@link Runnable} to execute for ending the turn
     */
    public void setEndTurnAction(final Runnable action) {
        overlay.setEndTurnAction(action);
    }

    /**
     * Sets the action to be performed when the collect treasure button is clicked.
     *
     * @param action the {@link Runnable} to execute for collecting a treasure
     */
    public void setCollectTreasureAction(final Runnable action) {
        overlay.setCollectTreasureAction(action);
    }

    /**
     * Sets the action to be performed when the collect amulet button is clicked.
     *
     * @param action the {@link Runnable} to execute for collecting an amulet
     */
    public void setCollectAmuletAction(final Runnable action) {
        overlay.setCollectAmuletAction(action);
    }

    /**
     * Sets the action to be performed when the use amulet button is clicked.
     *
     * @param action the {@link Runnable} to execute for using an amulet
     */
    public void setUseAmuletAction(final Runnable action) {
        overlay.setUseAmuletAction(action);
    }

    /**
     * Returns the property representing the enabled state of the drive button.
     *
     * @return the {@link BooleanProperty} for the drive button's enabled state
     */
    public BooleanProperty driveEnabledProperty() {
        return overlay.driveEnabledProperty();
    }

    /**
     * Returns the property representing the enabled state of the end turn button.
     *
     * @return the {@link BooleanProperty} for the end turn button's enabled state
     */
    public BooleanProperty endTurnEnabledProperty() {
        return overlay.endTurnEnabledProperty();
    }

    /**
     * Returns the property representing the enabled state of the collect treasure
     * button.
     *
     * @return the {@link BooleanProperty} for the collect treasure button's enabled
     *         state
     */
    public BooleanProperty collectTreasureEnabledProperty() {
        return overlay.collectTreasureEnabledProperty();
    }

    /**
     * Returns the property representing the enabled state of the collect amulet
     * button.
     *
     * @return the {@link BooleanProperty} for the collect amulet button's enabled
     *         state
     */
    public BooleanProperty collectAmuletEnabledProperty() {
        return overlay.collectAmuletEnabledProperty();
    }

    /**
     * Returns the property representing the enabled state of the use amulet button.
     *
     * @return the {@link BooleanProperty} for the use amulet button's enabled state
     */
    public BooleanProperty useAmuletEnabledProperty() {
        return overlay.useAmuletEnabledProperty();
    }
}
