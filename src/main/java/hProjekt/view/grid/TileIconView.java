package hProjekt.view.grid;

import org.jetbrains.annotations.Nullable;

import hProjekt.model.grid.Tile;
import hProjekt.view.utils.IconView;
import hProjekt.view.utils.Images;

/**
 * An IconView that displays a tile icon.
 */
public class TileIconView extends IconView {
    /**
     * Creates a new TileIconView for the given tile type and size.
     * If the type is null, the ocean icon is used.
     *
     * @param type tile type
     * @param size size of the icon
     */
    public TileIconView(@Nullable final Tile.Type type, final double size) {
        super(type != null ? Images.TILE_TYPE_ICONS.get(type) : Images.OCEAN_ICON, size);
    }
}
