package hProjekt.view.cards;

import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import hProjekt.model.cards.PathCard;
import hProjekt.model.grid.Tile;
import hProjekt.model.grid.Types;
import hProjekt.view.grid.TileIconView;
import hProjekt.view.grid.TilePane;
import hProjekt.view.utils.IconView;
import hProjekt.view.utils.Images;
import hProjekt.view.utils.PlayerLabel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/**
 * A specialized {@link CardPane} used to display path cards.
 * <p>
 * This pane visually represents the criteria of a {@link PathCard}, such as
 * the required tile type, area conditions, or visibility. It also handles
 * the visual representation of negated cards and can optionally display
 * the owner of the card.
 */
public class PathCardPane extends CardPane {
    public static final int cardSize = 100;
    private final PathCard card;

    /**
     * Constructs a new {@code PathCardPane} for the specified card.
     * <p>
     * This constructor enables the player display by default.
     *
     * @param card The {@link PathCard} to be displayed.
     */
    public PathCardPane(final PathCard card) {
        this(card, true);
    }

    /**
     * Constructs a new {@code PathCardPane} for the specified card, with an option
     * to display the player.
     * <p>
     * If {@code displayPlayer} is true, a label indicating the owner of the card
     * is added to the pane.
     *
     * @param card          The {@link PathCard} to be displayed.
     * @param displayPlayer {@code true} to show the player label, {@code false}
     *                      otherwise.
     */
    public PathCardPane(final PathCard card, final boolean displayPlayer) {
        super(Color.BEIGE, PathCardPane.cardSize, true);
        this.card = card;
        final StackPane content = new StackPane();
        content.setMouseTransparent(true);
        @Nullable
        final Types filterType = card.getFilterType();
        final ImageView typeIcon = filterType instanceof Tile.Type || filterType == null
                ? new TileIconView((Tile.Type) filterType, 30)
                : new IconView(Images.STRUCTURE_ICONS.get(filterType), 30);
        typeIcon.setMouseTransparent(true);
        final StackPane areaContainer = new StackPane();
        areaContainer.setMouseTransparent(true);

        content.getChildren().add(areaContainer);
        content.setMaxHeight(Region.USE_PREF_SIZE);

        switch (card.getType()) {
            case IN_BIGGEST_AREA, NOT_IN_BIGGEST_AREA:
                final Circle innerCircle = new Circle(27, Color.TRANSPARENT);
                innerCircle.setStroke(Color.CRIMSON);
                final Circle outerCircle = new Circle(30, Color.TRANSPARENT);
                outerCircle.setStroke(Color.CRIMSON);
                areaContainer.getChildren().addAll(innerCircle, outerCircle);
            case IN_AREA, NOT_IN_AREA:
                final TilePane areaTile = new TilePane((Tile.Type) filterType, 25);
                areaContainer.getChildren().add(areaTile);
                break;
            case NEXT_TO, NOT_NEXT_TO:
                final HBox nextToTiles = new HBox();
                nextToTiles.setAlignment(Pos.CENTER);
                nextToTiles.setPrefHeight(Region.USE_PREF_SIZE);
                nextToTiles.getChildren().add(typeIcon);
                final TilePane emptyTile = new TilePane(null, 25);
                nextToTiles.getChildren().add(emptyTile);
                content.getChildren().add(nextToTiles);
                break;
            case CAN_SEE, NOT_CAN_SEE:
                final HBox canSeeTiles = new HBox();
                canSeeTiles.setPrefHeight(Region.USE_PREF_SIZE);
                canSeeTiles.setAlignment(Pos.CENTER);
                canSeeTiles.getChildren().addAll(typeIcon, new TilePane(null, 25), new TilePane(null, 25));
                content.getChildren().add(canSeeTiles);
                break;
            default:
                content.getChildren().add(new Label(card.getType().name()));
                break;
        }
        content.setAlignment(Pos.TOP_CENTER);
        getChildren().add(content);
        StackPane.setAlignment(content, Pos.TOP_CENTER);

        if (card.getType().negation) {
            final Line strikeThrough = new Line();
            // The line should go from corner to corner, inside the padding
            strikeThrough.startXProperty().bind(widthProperty().multiply(0.1));
            strikeThrough.startYProperty().bind(heightProperty().multiply(0.9));
            strikeThrough.endXProperty().bind(widthProperty().multiply(0.9));
            strikeThrough.endYProperty().bind(heightProperty().multiply(0.1));
            strikeThrough.setStroke(Color.RED);
            strikeThrough.setStrokeWidth(5);
            getChildren().add(strikeThrough);
        }

        if (displayPlayer) {
            final PlayerLabel playerLabel = new PlayerLabel(card.getPlayer());
            // playerLabel.setAlignment(Pos.BOTTOM_RIGHT);
            playerLabel.setPadding(new Insets(5));
            playerLabel.setStyle("-fx-text-fill: black;");
            getChildren().add(playerLabel);
            StackPane.setAlignment(playerLabel, Pos.BOTTOM_RIGHT);
        }
    }

    /**
     * Highlights this pane and registers a consumer action to be executed on click.
     * <p>
     * This method overrides the base {@link CardPane#highlight(Runnable)} to
     * provide the specific {@link PathCard} to the action consumer.
     *
     * @param action The {@link Consumer} to accept the {@link PathCard} when
     *               clicked.
     */
    public void highlight(final Consumer<PathCard> action) {
        super.highlight(() -> action.accept(card));
    }
}
