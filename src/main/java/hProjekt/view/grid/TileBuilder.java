package hProjekt.view.grid;

import java.util.function.Consumer;

import hProjekt.Config;
import hProjekt.model.grid.Tile;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.util.Builder;

/**
 * A Builder to create views for {@link Tile}s.
 * Renders the {@link Tile} as a hexagon.
 * Has methods to highlight and unhighlight the tile.
 */
public class TileBuilder implements Builder<Region> {

    private final Tile tile;
    private TilePane pane;
    private final AmuletPane amuletPane = new AmuletPane();
    private final Circle treasureMarker = new Circle(10, Color.TRANSPARENT);
    private final HBox structureContainer = new HBox();
    private StructurePane structurePane;
    private boolean hasMouseClickedHandler = false;

    /**
     * Creates a new TileBuilder for the given {@link Tile}.
     *
     * @param tile the tile to render
     */
    public TileBuilder(final Tile tile) {
        this.tile = tile;
    }

    /**
     * Returns the {@link Tile} this builder renders.
     *
     * @return the tile
     */
    public Tile getTile() {
        return tile;
    }

    @Override
    public Region build() {
        pane = new TilePane(tile.getType(), Config.TILE_SIZE);

        final VBox labelBox = new VBox(5); // Add spacing between labels
        labelBox.setAlignment(Pos.CENTER);

        structureContainer.setAlignment(Pos.CENTER);

        treasureMarker.setStrokeWidth(2.5);

        // Uncomment to display position and type labels. Makes everything quite
        // laggy
        // final Label positionLabel = createPositionLabel();
        // final Label typeLabel = createTypeLabel();
        // labelBox.getChildren().addAll(positionLabel, typeLabel);
        pane.getChildren().addAll(amuletPane, structureContainer, treasureMarker, labelBox);
        amuletPane.setVisible(tile.hasAmulet());

        return pane;
    }

    /**
     * Creates a label displaying the tile's position in the format (q, r, s).
     *
     * @return the position label
     */
    private Label createPositionLabel() {
        final Label positionLabel = new Label(tile.getPosition().toString());
        positionLabel.setFont(new Font("Arial", 12));
        positionLabel.getStyleClass().add("tile-position-label");
        return positionLabel;
    }

    /**
     * Creates a label displaying the tile's type (e.g., PLAIN, MOUNTAIN).
     *
     * @return the type label
     */
    private Label createTypeLabel() {
        final Label typeLabel = new Label(tile.getType().toString());
        typeLabel.setFont(new Font("Arial", 12));
        typeLabel.getStyleClass().add("tile-resource-label");
        return typeLabel;
    }

    public void markTreasure(final Color color) {
        treasureMarker.setStroke(Color.BLACK);
        treasureMarker.setFill(color);
    }

    public void unmarkTreasure() {
        treasureMarker.setFill(Color.TRANSPARENT);
        treasureMarker.setStroke(Color.TRANSPARENT);
    }

    /**
     * Highlights the tile and sets a handler for mouse clicks.
     *
     * @param handler the handler to call when the tile is clicked
     */
    public void highlight(final Runnable handler) {
        pane.getStyleClass().addAll("selectable");
        setMouseClickedHandler(e -> handler.run());
    }

    /**
     * Removes the highlight and the handler for mouse clicks.
     */
    public void unhighlight() {
        pane.getStyleClass().removeAll("selectable");
        removeMouseClickedHandler();
    }

    /**
     * Sets a handler for when the mouse enters the tile.
     *
     * @param handler the handler to call when the mouse enters the tile
     */
    public void setMouseEnteredHandler(final Consumer<MouseEvent> handler) {
        pane.setOnMouseEntered(handler::accept);
    }

    /**
     * Removes the handler for when the mouse enters the tile.
     */
    public void removeMouseEnteredHandler() {
        pane.setOnMouseEntered(null);
    }

    /**
     * Sets a handler for when the tile is clicked.
     *
     * @param handler the handler to call when the tile is clicked
     */
    public void setMouseClickedHandler(final Consumer<MouseEvent> handler) {
        hasMouseClickedHandler = true;
        pane.setOnMouseClicked(handler::accept);
    }

    /**
     * Removes the handler for when the tile is clicked.
     */
    public void removeMouseClickedHandler() {
        hasMouseClickedHandler = false;
        pane.setOnMouseClicked(null);
    }

    /**
     * Returns whether the tile has a handler for mouse clicks.
     *
     * @return true if the tile has a handler for mouse clicks, false otherwise
     */
    public boolean hasMouseClickedHandler() {
        return hasMouseClickedHandler;
    }

    public StackPane getPane() {
        return pane;
    }

    public void updateType() {
        pane.updateType(tile.getType());
    }

    public void update() {
        amuletPane.setVisible(tile.hasAmulet());
        updateStructurePaneRotation();
    }

    public void setStructurePane(final StructurePane structurePane) {
        this.structurePane = structurePane;
        if (structurePane == null) {
            structureContainer.getChildren().clear();
            return;
        }
        structureContainer.getChildren().setAll(structurePane);
    }

    public void updateStructurePaneRotation() {
        if (structurePane != null) {
            structurePane.updateRotation();
        }
    }
}
