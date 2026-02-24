package hProjekt.controller.gui.grid;

import java.util.function.Consumer;

import hProjekt.controller.gui.Controller;
import hProjekt.model.grid.Tile;
import hProjekt.view.grid.TileBuilder;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * The controller for a tile.
 */
public class TileController implements Controller {
    private final TileBuilder builder;

    /**
     * Creates a new tile controller.
     *
     * @param tile the tile to render
     */
    public TileController(final Tile tile) {
        builder = new TileBuilder(tile);
    }

    public TileController() {
        builder = null;
    }

    /**
     * Returns the tile.
     *
     * @return the tile
     */
    public Tile getTile() {
        return builder.getTile();
    }

    /**
     * Marks the treasure associated with this tile with a specific color.
     *
     * @param color the {@link Color} to be used for marking the treasure.
     *
     * @see TileBuilder#markTreasure(Color)
     */
    public void markTreasure(final Color color) {
        builder.markTreasure(color);
    }

    /**
     * Removes the treasure marking from this tile.
     *
     * @see TileBuilder#unmarkTreasure()
     */
    public void unmarkTreasure() {
        builder.unmarkTreasure();
    }

    /**
     * Highlights the tile and overwrites the click handler.
     *
     * @param handler the handler to call when the tile is clicked
     *
     * @see TileBuilder#highlight(Runnable)
     */
    public void highlight(final Consumer<Tile> handler) {
        builder.highlight(() -> handler.accept(getTile()));
    }

    /**
     * Unhighlights the tile and removes the click handler.
     *
     * @see TileBuilder#unhighlight()
     */
    public void unhighlight() {
        builder.unhighlight();
    }

    /**
     * Sets the mouse entered handler.
     *
     * @param handler the handler to call when the mouse enters the tile
     *
     * @see TileBuilder#setMouseEnteredHandler(Consumer)
     */
    public void setMouseEnteredHandler(final Consumer<MouseEvent> handler) {
        builder.setMouseEnteredHandler(handler);
    }

    /**
     * Removes the mouse entered handler.
     *
     * @see TileBuilder#removeMouseEnteredHandler()
     */
    public void removeMouseEnteredHandler() {
        builder.removeMouseEnteredHandler();
    }

    /**
     * Sets the mouse clicked handler.
     *
     * @param handler the handler to call when the tile is clicked
     *
     * @see TileBuilder#setMouseClickedHandler(Consumer)
     */
    public void setMouseClickedHandler(final Consumer<MouseEvent> handler) {
        builder.setMouseClickedHandler(handler);
    }

    /**
     * Removes the mouse clicked handler.
     *
     * @see TileBuilder#removeMouseClickedHandler()
     */
    public void removeMouseClickedHandler() {
        builder.removeMouseClickedHandler();
    }

    /**
     * Returns whether the tile has a mouse clicked handler.
     *
     * @return true if the tile has a mouse clicked handler, false otherwise
     *
     * @see TileBuilder#hasMouseClickedHandler()
     */
    public boolean hasMouseClickedHandler() {
        return builder.hasMouseClickedHandler();
    }

    @Override
    public TileBuilder getBuilder() {
        return builder;
    }
}
