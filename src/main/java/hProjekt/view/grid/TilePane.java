package hProjekt.view.grid;

import org.jetbrains.annotations.Nullable;

import hProjekt.model.grid.Tile;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * A pane that displays a tile.
 * It renders the tile type icon and the background color.
 */
public class TilePane extends StackPane {
    private ImageView typeSymbol;

    /**
     * Creates a new TilePane for the given tile type and size.
     *
     * @param type     tile type
     * @param tileSize size of the tile
     */
    public TilePane(@Nullable final Tile.Type type, final double tileSize) {
        getStylesheets().add("css/main.css");
        getStyleClass().add("hex-tile");
        setMaxHeight(tileSize * 2);
        setMinHeight(tileSize * 2);
        setMaxWidth(Math.sqrt(3) * tileSize);
        setMinWidth(Math.sqrt(3) * tileSize);

        updateType(type);
    }

    /**
     * Updates the tile type displayed by this pane.
     *
     * @param type the new tile type
     */
    public void updateType(@Nullable final Tile.Type type) {
        getChildren().remove(typeSymbol);
        if (type == null) {
            typeSymbol = null;
            setBackground(Background.fill(Color.TRANSPARENT));
            return;
        }
        setBackground(Background.fill(type.color));
        typeSymbol = new TileIconView(type, getMaxWidth() * 0.6);
        getChildren().addFirst(typeSymbol);
    }
}
