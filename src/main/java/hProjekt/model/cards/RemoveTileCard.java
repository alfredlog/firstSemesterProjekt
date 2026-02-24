package hProjekt.model.cards;

import hProjekt.model.grid.Tile;
import hProjekt.model.grid.TilePosition;

/**
 * Represents a card that allows the removal of a specific tile from a treasure
 * trail.
 */
public class RemoveTileCard extends PathCard {

    /**
     * Constructs a {@code RemoveTileCard} that is used to filter and target
     * {@link Tile}s based on their {@link TilePosition}.
     *
     * @param tilePosition the {@link TilePosition} of the {@link Tile} that this
     *                     card is intended to remove
     */
    public RemoveTileCard(final TilePosition tilePosition) {
        super(null, (tile -> !tile.getPosition().equals(tilePosition)), null);
    }
}
