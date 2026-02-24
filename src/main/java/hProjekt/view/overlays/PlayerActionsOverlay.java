package hProjekt.view.overlays;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

/**
 * An overlay that displays buttons for various player actions.
 * <p>
 * This component provides buttons for actions such as driving, ending the turn,
 * collecting treasures, collecting amulets, and using amulets. Each action
 * is associated with a specific {@link Runnable} that gets executed when the
 * corresponding button is clicked. The buttons can be enabled or disabled
 * via their respective boolean properties.
 * </p>
 */
public class PlayerActionsOverlay extends HBox {
    private Runnable driveAction = () -> {};
    private Runnable endTurnAction = () -> {};
    private Runnable collectTreasureAction = () -> {};
    private Runnable collectAmuletAction = () -> {};
    private Runnable useAmuletAction = () -> {};
    private final BooleanProperty driveEnabled = new SimpleBooleanProperty(false);
    private final BooleanProperty endTurnEnabled = new SimpleBooleanProperty(false);
    private final BooleanProperty collectTreasureEnabled = new SimpleBooleanProperty(false);
    private final BooleanProperty collectAmuletEnabled = new SimpleBooleanProperty(false);
    private final BooleanProperty useAmuletEnabled = new SimpleBooleanProperty(false);

    /**
     * Constructs a new {@code PlayerActionsOverlay}.
     * <p>
     * Initializes the buttons for each action ("Drive", "End Turn", "Collect
     * Treasure", "Collect Amulet", "Use Amulet") and binds their disable property
     * to the inverse of the corresponding enabled property. The buttons are added
     * to this HBox.
     * </p>
     */
    public PlayerActionsOverlay() {
        final Button driveButton = new Button("Drive");
        driveButton.setOnAction(e -> driveAction.run());
        driveButton.disableProperty().bind(driveEnabled.not());

        final Button endTurnButton = new Button("End Turn");
        endTurnButton.setOnAction(e -> endTurnAction.run());
        endTurnButton.disableProperty().bind(endTurnEnabled.not());

        final Button collectTreasureButton = new Button("Collect Treasure");
        collectTreasureButton.setOnAction(e -> collectTreasureAction.run());
        collectTreasureButton.disableProperty().bind(collectTreasureEnabled.not());

        final Button collectAmuletButton = new Button("Collect Amulet");
        collectAmuletButton.setOnAction(e -> collectAmuletAction.run());
        collectAmuletButton.disableProperty().bind(collectAmuletEnabled.not());

        final Button useAmuletButton = new Button("Use Amulet");
        useAmuletButton.setOnAction(e -> useAmuletAction.run());
        useAmuletButton.disableProperty().bind(useAmuletEnabled.not());

        setSpacing(10);
        getChildren().addAll(driveButton, endTurnButton, collectTreasureButton, collectAmuletButton,
                useAmuletButton);
    }

    /**
     * Sets the action to be performed when the "Drive" button is clicked.
     *
     * @param driveAction the {@link Runnable} to execute for the drive action
     */
    public void setDriveAction(final Runnable driveAction) {
        this.driveAction = driveAction;
    }

    /**
     * Sets the action to be performed when the "End Turn" button is clicked.
     *
     * @param endTurnAction the {@link Runnable} to execute for the end turn action
     */
    public void setEndTurnAction(final Runnable endTurnAction) {
        this.endTurnAction = endTurnAction;
    }

    /**
     * Sets the action to be performed when the "Collect Treasure" button is
     * clicked.
     *
     * @param collectTreasureAction the {@link Runnable} to execute for the collect
     *                              treasure action
     */
    public void setCollectTreasureAction(final Runnable collectTreasureAction) {
        this.collectTreasureAction = collectTreasureAction;
    }

    /**
     * Sets the action to be performed when the "Collect Amulet" button is clicked.
     *
     * @param collectAmuletAction the {@link Runnable} to execute for the collect
     *                            amulet action
     */
    public void setCollectAmuletAction(final Runnable collectAmuletAction) {
        this.collectAmuletAction = collectAmuletAction;
    }

    /**
     * Sets the action to be performed when the "Use Amulet" button is clicked.
     *
     * @param useAmuletAction the {@link Runnable} to execute for the use amulet
     *                        action
     */
    public void setUseAmuletAction(final Runnable useAmuletAction) {
        this.useAmuletAction = useAmuletAction;
    }

    /**
     * Returns the boolean property that controls whether the "Drive" button is
     * enabled.
     *
     * @return the {@link BooleanProperty} for the drive action enabled state
     */
    public BooleanProperty driveEnabledProperty() {
        return driveEnabled;
    }

    /**
     * Returns the boolean property that controls whether the "End Turn" button is
     * enabled.
     *
     * @return the {@link BooleanProperty} for the end turn action enabled state
     */
    public BooleanProperty endTurnEnabledProperty() {
        return endTurnEnabled;
    }

    /**
     * Returns the boolean property that controls whether the "Collect Treasure"
     * button is enabled.
     *
     * @return the {@link BooleanProperty} for the collect treasure action enabled
     *         state
     */
    public BooleanProperty collectTreasureEnabledProperty() {
        return collectTreasureEnabled;
    }

    /**
     * Returns the boolean property that controls whether the "Collect Amulet"
     * button is enabled.
     *
     * @return the {@link BooleanProperty} for the collect amulet action enabled
     *         state
     */
    public BooleanProperty collectAmuletEnabledProperty() {
        return collectAmuletEnabled;
    }

    /**
     * Returns the boolean property that controls whether the "Use Amulet" button is
     * enabled.
     *
     * @return the {@link BooleanProperty} for the use amulet action enabled state
     */
    public BooleanProperty useAmuletEnabledProperty() {
        return useAmuletEnabled;
    }
}
