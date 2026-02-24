package hProjekt.controller.gui;

import java.util.List;

import hProjekt.model.grid.Tile;
import hProjekt.model.grid.TilePosition;
import hProjekt.view.grid.HexGridBuilder;
import hProjekt.view.utils.ColoredImageView;
import hProjekt.view.utils.Images;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

/**
 * Controls the player animation, moving a circle along a path of Tiles with a
 * car icon inside.
 */
public class PlayerAnimationController {

    private final HexGridBuilder hexGridBuilder;
    private final StackPane playerContainer;
    private final ImageView playerImage;

    /**
     * Creates a new PlayerAnimationController.
     *
     * @param hexGridBuilder the HexGridBuilder to calculate tile positions
     * @param playerColor    the color of the player's circle
     */
    public PlayerAnimationController(final HexGridBuilder hexGridBuilder, final Color playerColor) {
        this.hexGridBuilder = hexGridBuilder;

        // Create the train ImageView
        playerImage = new ColoredImageView(Images.PLAYER_ICON, playerColor);
        playerImage.setFitWidth(42);
        playerImage.setFitHeight(42);
        playerImage.setPreserveRatio(true);

        // Create a StackPane to center the image on the circle
        playerContainer = new StackPane(playerImage);
        playerContainer.setTranslateX(0);
        playerContainer.setTranslateY(0);
        playerContainer.setMouseTransparent(true);
        this.hexGridBuilder.getHexGridPane().getChildren().add(playerContainer);
        hidePlayer();
    }

    /**
     * Default constructor for PlayerAnimationController with uninitialized fields.
     * Mainly used for testing purposes.
     */
    public PlayerAnimationController() {
        hexGridBuilder = null;
        playerImage = null;
        playerContainer = null;
    }

    /**
     * Animates the playerCircle along the given list of Tiles.
     *
     * @param tiles the list of Tiles to follow
     * @return the Animation object representing the full animation
     */
    public Animation animatePlayer(final List<Tile> tiles) {
        if (tiles == null || tiles.size() < 2) {
            throw new IllegalArgumentException("At least two tiles are required for the animation.");
        }
        // Sequential Transition to combine multiple path transitions
        final SequentialTransition animationSequence = new SequentialTransition();
        final Path path = new Path();

        for (int i = 0; i < tiles.size() - 1; i++) {
            final Tile currentTile = tiles.get(i);
            final Tile nextTile = tiles.get(i + 1);

            // Get the center points of the current and next tile
            final TilePosition currentPosition = currentTile.getPosition();
            final TilePosition nextPosition = nextTile.getPosition();

            final Point2D currentCenter = hexGridBuilder.calculatePositionCenterOffset(currentPosition);
            final Point2D nextCenter = hexGridBuilder.calculatePositionCenterOffset(nextPosition);

            // Create a path between the two tile centers
            path.getElements().add(new MoveTo(currentCenter.getX(),
                    currentCenter.getY()));
            path.getElements().add(new LineTo(nextCenter.getX(), nextCenter.getY()));

        }
        // Create a PathTransition for this segment
        final PathTransition transition = new PathTransition();
        transition.setNode(playerContainer);
        transition.setPath(path);
        transition.setDuration(Duration.seconds(1));
        transition.setInterpolator(Interpolator.EASE_BOTH);
        transition.setCycleCount(1);

        // Add the transition to the sequence
        animationSequence.getChildren().add(transition);

        // Add pulsating animation before the main animation
        final ScaleTransition pulseBefore = createPulseTransition();

        // Add pulsating animation after the main animation
        final ScaleTransition pulseAfter = createPulseTransition();

        // Combine all animations into a single SequentialTransition
        final SequentialTransition fullAnimation = new SequentialTransition(pulseBefore,
                animationSequence, pulseAfter);
        showPlayer();
        fullAnimation.play();
        return fullAnimation;
    }

    /**
     * Sets the position of the playerCircle to the center of the given
     * TilePosition.
     *
     * @param position the TilePosition to move the playerCircle to
     */
    public void setPosition(final TilePosition position) {
        if (position == null) {
            return;
        }
        final Point2D center = hexGridBuilder.calculatePositionCenterOffset(position);
        playerContainer.setTranslateX(center.getX() - playerContainer.getWidth() / 2);
        playerContainer.setTranslateY(center.getY() - playerContainer.getHeight() / 2);
    }

    /**
     * Shows the car icon.
     */
    public void showPlayer() {
        playerContainer.setVisible(true);
    }

    /**
     * Hides the car icon.
     */
    public void hidePlayer() {
        playerContainer.setVisible(false);
    }

    /**
     * Creates a pulsating scale transition for the playerCircle.
     *
     * @return the ScaleTransition animation
     */
    private ScaleTransition createPulseTransition() {
        final ScaleTransition pulse = new ScaleTransition(Duration.seconds(0.2), playerContainer);
        pulse.setFromX(1.0);
        pulse.setFromY(1.0);
        pulse.setToX(1.8); // Scale up to 180% of the original size
        pulse.setToY(1.8);
        pulse.setAutoReverse(true); // Return to the original size
        pulse.setCycleCount(2); // Scale up and down once
        return pulse;
    }
}
