package hProjekt.view.grid;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.IntBinaryOperator;
import java.util.function.ToIntFunction;

import hProjekt.Config;
import hProjekt.model.grid.HexGrid;
import hProjekt.model.grid.Tile;
import hProjekt.model.grid.TilePosition;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Builder;

/**
 * The builder for the {@link HexGrid}.
 * It creates a pane with the hex grid and handles the placement of the tiles
 * and edges.
 * The hex grid pane can be zoomed, panned and centered.
 */
public class HexGridBuilder implements Builder<Region> {
    private final BiConsumer<ScrollEvent, Region> scrollHandler;
    private final Consumer<MouseEvent> pressedHandler;
    private final BiConsumer<MouseEvent, Region> draggedHandler;

    private final Point2D maxPoint;
    private final Point2D minPoint;

    private final Set<EdgeLine> edgeLines;
    private final Set<TileBuilder> tileBuilders;

    private final Pane hexGridPane = new Pane();

    /**
     * Creates a new hex grid builder with the given hex grid, intersection
     * builders, edge lines, tile builders and event handlers.
     *
     * @param grid           The hex grid.
     * @param edgeLines      The edge lines.
     * @param tileBuilders   The tile builders.
     * @param scrollHandler  The handler for the scroll event. Used for
     *                       zooming.
     * @param pressedHandler The handler for the mouse pressed event.
     * @param draggedHandler The handler for the mouse dragged event. Used for
     *                       panning
     */
    public HexGridBuilder(
            final HexGrid grid,
            final Set<EdgeLine> edgeLines,
            final Set<TileBuilder> tileBuilders,
            final BiConsumer<ScrollEvent, Region> scrollHandler,
            final Consumer<MouseEvent> pressedHandler, final BiConsumer<MouseEvent, Region> draggedHandler) {
        this.edgeLines = edgeLines;
        this.tileBuilders = tileBuilders;

        this.scrollHandler = scrollHandler;
        this.pressedHandler = pressedHandler;
        this.draggedHandler = draggedHandler;

        final BiFunction<ToIntFunction<TilePosition>, IntBinaryOperator, Integer> reduceTiles = (
                positionFunction,
                reduceFunction) -> grid.getTiles().values().stream().map(Tile::getPosition).mapToInt(positionFunction)
                        .reduce(reduceFunction).getAsInt();

        maxPoint = new Point2D(
                calculatePositionTranslation(new TilePosition(reduceTiles.apply(TilePosition::q, Integer::max), 0))
                        .getX(),
                calculatePositionTranslation(new TilePosition(0, reduceTiles.apply(TilePosition::r, Integer::max)))
                        .getY());
        minPoint = new Point2D(
                calculatePositionTranslation(new TilePosition(reduceTiles.apply(TilePosition::q, Integer::min), 0))
                        .getX(),
                calculatePositionTranslation(new TilePosition(0, reduceTiles.apply(TilePosition::r, Integer::min)))
                        .getY());
    }

    @Override
    public Region build() {
        hexGridPane.getChildren().clear();

        hexGridPane.getChildren().addAll(tileBuilders.stream().map(this::placeTile).toList());

        hexGridPane.setMaxWidth(Math.abs(minPoint.getX()) + maxPoint.getX() + Config.TILE_WIDTH);
        hexGridPane.setMaxHeight(Math.abs(minPoint.getY()) + maxPoint.getY() + Config.TILE_HEIGHT);

        hexGridPane.minWidthProperty().bind(hexGridPane.maxWidthProperty());
        hexGridPane.minHeightProperty().bind(hexGridPane.maxHeightProperty());

        edgeLines.forEach(this::placeEdge);

        final StackPane mapPane = new StackPane(hexGridPane);
        mapPane.getStylesheets().add("css/main.css");
        mapPane.getStyleClass().add("hex-grid");
        mapPane.setOnScroll(event -> scrollHandler.accept(event, hexGridPane));
        mapPane.setOnMousePressed(pressedHandler::accept);
        mapPane.setOnMouseDragged(event -> draggedHandler.accept(event, hexGridPane));

        return mapPane;
    }

    /**
     * Draws the tiles on the hex grid.
     */
    public void drawTiles() {
        tileBuilders.forEach(TileBuilder::build);
    }

    /**
     * Places a tile on the hex grid.
     *
     * @param builder The tile builder.
     * @return The region of the tile.
     */
    private Region placeTile(final TileBuilder builder) {
        final Region tileView = builder.build();
        final Tile tile = builder.getTile();
        final TilePosition position = tile.getPosition();
        final Point2D translatedPoint = calculatePositionTranslationOffset(position);
        tileView.setTranslateX(translatedPoint.getX());
        tileView.setTranslateY(translatedPoint.getY());
        return tileView;
    }

    /**
     * Draws the edges on the hex grid.
     */
    public void drawEdges() {
        edgeLines.forEach(EdgeLine::init);
    }

    /**
     * Places an edge on the hex grid.
     *
     * @param edgeLine The edge line.
     */
    private void placeEdge(final EdgeLine edgeLine) {
        final Point2D translatedStart = calculatePositionCenterOffset(edgeLine.getEdge().getPosition1());
        final Point2D translatedEnd = calculatePositionCenterOffset(edgeLine.getEdge().getPosition2());
        edgeLine.setStartX(translatedStart.getX());
        edgeLine.setStartY(translatedStart.getY());
        edgeLine.setEndX(translatedEnd.getX());
        edgeLine.setEndY(translatedEnd.getY());
        edgeLine.init();
        hexGridPane.getChildren().addAll(edgeLine.getOutline());
        hexGridPane.getChildren().add(edgeLine);
    }

    /**
     * Calculates the upper left corner of the tile region.
     *
     * @param position The position of the tile.
     * @return The point of the upper left corner.
     */
    private Point2D calculatePositionTranslation(final TilePosition position) {
        return new Point2D(
                Config.TILE_SIZE * (Math.sqrt(3) * position.q() + Math.sqrt(3) / 2 * position.r()),
                Config.TILE_SIZE * (3.0 / 2 * position.r()));
    }

    /**
     * Calculates the upper left corner of the tile region with an offset to move
     * the coordinate system center to the center of the hex grid.
     *
     * @param position The position of the tile.
     * @return The point of the upper left corner.
     */
    private Point2D calculatePositionTranslationOffset(final TilePosition position) {
        return calculatePositionTranslation(position).add(Math.abs(minPoint.getX()), Math.abs(minPoint.getY()));
    }

    /**
     * Calculates the center of the tile region with an offset to move the
     * coordinate system center to the center of the hex grid.
     *
     * @param position The position of the tile.
     * @return The point of the center.
     */
    public Point2D calculatePositionCenterOffset(final TilePosition position) {
        return calculatePositionTranslationOffset(position).add(Config.TILE_WIDTH / 2, Config.TILE_HEIGHT / 2);
    }

    /**
     * Returns the pane with the hex grid.
     *
     * @return The pane with the hex grid.
     */
    public Pane getHexGridPane() {
        return hexGridPane;
    }
}
