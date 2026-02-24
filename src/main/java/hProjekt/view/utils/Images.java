package hProjekt.view.utils;

import java.util.Map;

import hProjekt.model.grid.Structure;
import hProjekt.model.grid.Tile;
import javafx.scene.image.Image;

/**
 * A collection of images used in the application.
 */
public final class Images {
    /**
     * A map mapping tile types to their corresponding images.
     */
    public static final Map<Tile.Type, Image> TILE_TYPE_ICONS = Map.of(
            Tile.Type.BEACH, new Image("images/tiles/beach.png"),
            Tile.Type.JUNGLE, new Image("images/tiles/jungle.png"),
            Tile.Type.LAKE, new Image("images/tiles/lake.png"),
            Tile.Type.RIVER, new Image("images/tiles/river.png"),
            Tile.Type.PLAINS, new Image("images/tiles/plains.png"),
            Tile.Type.MOUNTAIN, new Image("images/tiles/mountain.png"));

    /**
     * The icon used for the ocean.
     */
    public static final Image OCEAN_ICON = new Image("images/tiles/ocean.png");

    /**
     * A map mapping structure types to their corresponding images.
     */
    public static final Map<Structure.Type, Image> STRUCTURE_ICONS = Map.of(
            Structure.Type.HUT, new Image("images/structures/hut.png"),
            Structure.Type.PALM, new Image("images/structures/palm.png"),
            Structure.Type.STATUE, new Image("images/structures/statue.png"));

    /**
     * The icon used for the player.
     */
    public static final Image PLAYER_ICON = new Image("/images/jeep.png");

    /**
     * The icon used for the amulet.
     */
    public static final Image AMULET_ICON = new Image("images/amulet.png");

    /**
     * The title image used in the application.
     */
    public static final Image TITLE_IMAGE = new Image("images/title.png");
}
