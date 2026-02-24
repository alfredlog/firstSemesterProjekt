package hProjekt.view.grid;

import hProjekt.Config;
import hProjekt.model.grid.Statue;
import hProjekt.model.grid.Structure;
import hProjekt.view.utils.IconView;
import hProjekt.view.utils.Images;

/**
 * A pane that displays a structure.
 */
public class StructurePane extends IconView {
    private final Structure structure;

    /**
     * Creates a new StructurePane for the given structure with a default width.
     *
     * @param structure the structure to display
     */
    public StructurePane(final Structure structure) {
        this(structure, Config.TILE_WIDTH * 0.4);
    }

    /**
     * Creates a new StructurePane for the given structure with the given width.
     *
     * @param structure the structure to display
     * @param width     the width of the structure
     */
    public StructurePane(final Structure structure, final double width) {
        super(Images.STRUCTURE_ICONS.get(structure.getType()), width);
        this.structure = structure;
        if (Structure.Type.STATUE.equals(structure.getType())) {
            updateRotation();
        }
        setMouseTransparent(true);
    }

    /**
     * Returns the structure displayed by this pane.
     *
     * @return the structure
     */
    public Structure getStructure() {
        return structure;
    }

    /**
     * Updates the rotation of the structure if it is a statue.
     */
    public void updateRotation() {
        if (!(structure instanceof final Statue statue)) {
            return;
        }
        setRotate(((statue.getDirection().ordinal() * 60) + 90) % 360);
    }
}
