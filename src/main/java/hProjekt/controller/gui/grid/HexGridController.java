package hProjekt.controller.gui.grid;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.tudalgo.algoutils.student.annotation.DoNotTouch;

import hProjekt.controller.gui.Controller;
import hProjekt.model.grid.Edge;
import hProjekt.model.grid.HexGrid;
import hProjekt.model.grid.Tile;
import hProjekt.view.grid.HexGridBuilder;
import hProjekt.view.grid.StructurePane;
import javafx.application.Platform;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Region;

/**
 * The controller for the hex grid.
 */
@DoNotTouch
public class HexGridController implements Controller {
    private final HexGrid hexGrid;
    private final HexGridBuilder builder;
    private final Map<Edge, EdgeController> edgeControllers;
    private final Map<Tile, TileController> tileControllers;

    private double lastX, lastY;

    /**
     * Creates a new hex grid controller.
     *
     * @param hexGrid the hex grid to render
     */
    public HexGridController(final HexGrid hexGrid) {
        edgeControllers = hexGrid.getEdges().values().stream().map(EdgeController::new)
                .collect(Collectors.toMap(EdgeController::getEdge, controller -> controller));
        tileControllers = hexGrid.getTiles().values().stream().map(TileController::new)
                .collect(Collectors.toMap(TileController::getTile, controller -> controller));
        hexGrid.getStructures().values().stream().map(StructurePane::new).forEach(sp -> tileControllers
                .get(hexGrid.getTileAt(sp.getStructure().getPosition())).getBuilder().setStructurePane(sp));

        builder = new HexGridBuilder(hexGrid,
                edgeControllers.values().stream().map(EdgeController::getEdgeLine).collect(Collectors.toSet()),
                tileControllers.values().stream().map(TileController::getBuilder).collect(Collectors.toSet()),
                this::zoomHandler, this::mousePressedHandler, this::mouseDraggedHandler);
        this.hexGrid = hexGrid;
    }

    /**
     * Constructs a new HexGridController for testing purposes without a builder.
     *
     * @param hexGrid         The underlying {@link HexGrid}
     * @param edgeControllers A map associating specific edges in the grid with
     *                        their corresponding controllers.
     * @param tileControllers A map associating specific tiles in the grid with
     *                        their corresponding controllers.
     */
    public HexGridController(final HexGrid hexGrid, final Map<Edge, EdgeController> edgeControllers,
            final Map<Tile, TileController> tileControllers) {
        this.edgeControllers = edgeControllers;
        this.tileControllers = tileControllers;
        builder = null;
        this.hexGrid = hexGrid;
    }

    /**
     * The handler for the center pane button.
     * <p>
     * Centers the given pane.
     *
     */
    public void centerPaneHandler() {
        builder.getHexGridPane().setTranslateX(0);
        builder.getHexGridPane().setTranslateY(0);
    }

    /**
     * The handler for the mouse dragged event.
     * <p>
     * Moves the pane when the mouse is dragged.
     *
     * @param event the event that triggered the handler
     * @param pane  the pane to move
     */
    private void mouseDraggedHandler(final MouseEvent event, final Region pane) {
        if (event.isPrimaryButtonDown()) {
            pane.setTranslateX(pane.getTranslateX() + (event.getX() - lastX));
            pane.setTranslateY(pane.getTranslateY() + (event.getY() - lastY));
        }
        lastX = event.getX();
        lastY = event.getY();
    }

    /**
     * The handler for the mouse pressed event.
     * <p>
     * Saves the last x and y position of the mouse. Used for moving the pane.
     *
     * @param event the event that triggered the handler
     */
    private void mousePressedHandler(final MouseEvent event) {
        lastX = event.getX();
        lastY = event.getY();
    }

    /**
     * The handler for the zoom event.
     * <p>
     * Zooms the pane in and out.
     *
     * @param event the event that triggered the handler
     * @param pane  the pane to zoom
     */
    private void zoomHandler(final ScrollEvent event, final Region pane) {
        final double minTileScale = 0.1;

        if (pane.getScaleX() <= minTileScale && event.getDeltaY() < 0 || event.getDeltaY() == 0) {
            return;
        }
        pane.setScaleX(pane.getScaleX() + event.getDeltaY() / 500);
        pane.setScaleY(pane.getScaleY() + event.getDeltaY() / 500);
    }

    /**
     * Returns the edge controllers.
     *
     * @return the edge controllers
     *
     * @see EdgeController
     */
    public Set<EdgeController> getEdgeControllers() {
        return new HashSet<>(edgeControllers.values());
    }

    /**
     * Returns the edge controllers as a map.
     *
     * @return the edge controllers as a map
     *
     * @see EdgeController
     */
    public Map<Edge, EdgeController> getEdgeControllersMap() {
        return Collections.unmodifiableMap(edgeControllers);
    }

    /**
     * Returns the tile controllers.
     *
     * @return the tile controllers
     *
     * @see TileController
     */
    public Set<TileController> getTileControllers() {
        return new HashSet<>(tileControllers.values());
    }

    /**
     * Returns the tile controllers as a map of tiles to tile controllers.
     *
     * @return the tile controllers as a map
     *
     * @see TileController
     */
    public Map<Tile, TileController> getTileControllersMap() {
        return Collections.unmodifiableMap(tileControllers);
    }

    /**
     * Returns the hex grid.
     *
     * @return the hex grid
     *
     * @see HexGrid
     */
    public HexGrid getHexGrid() {
        return hexGrid;
    }

    /**
     * Highlights the tiles.
     *
     * @param handler the handler to call when a tile is clicked
     *
     * @see TileController#highlight(Consumer)
     */
    public void highlightTiles(final Consumer<Tile> handler) {
        tileControllers.values().forEach(controller -> controller.highlight(handler));
    }

    /**
     * Unhighlights the tiles.
     *
     * @see TileController#unhighlight()
     */
    public void unhighlightTiles() {
        tileControllers.values().forEach(TileController::unhighlight);
    }

    /**
     * Draws all tiles again.
     *
     * @see HexGridBuilder#drawTiles()
     */
    public void drawTiles() {
        Platform.runLater(builder::drawTiles);
    }

    /**
     * Draws all edges again.
     *
     * @see HexGridBuilder#drawEdges()
     */
    public void drawEdges() {
        Platform.runLater(builder::drawEdges);
    }

    @Override
    public HexGridBuilder getBuilder() {
        return builder;
    }
}
