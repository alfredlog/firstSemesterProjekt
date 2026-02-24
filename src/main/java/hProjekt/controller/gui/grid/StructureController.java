package hProjekt.controller.gui.grid;

import hProjekt.model.grid.Structure;
import hProjekt.view.grid.StructurePane;

/**
 * A controller class responsible for managing the logical interaction between
 * the application and the {@link StructurePane}.
 * <p>
 * This controller encapsulates the {@link StructurePane} and provides access to
 * both the visual component and the underlying {@link Structure} model it
 * represents. It acts as an intermediary to handle the initialization and
 * retrieval of the structure-related GUI elements.
 * </p>
 */
public class StructureController {

    /**
     * The visual pane component managed by this controller.
     */
    private final StructurePane structurePane;

    /**
     * Constructs a new {@code StructureController} for a specific data structure.
     * <p>
     * This constructor initializes the {@link StructurePane} using the provided
     * {@link Structure} model.
     * </p>
     *
     * @param structure the {@link Structure} model to be visualized and managed.
     */
    public StructureController(final Structure structure) {
        structurePane = new StructurePane(structure);
    }

    /**
     * Retrieves the visual component managed by this controller.
     *
     * @return the {@link StructurePane} associated with this controller.
     */
    public StructurePane getStructurePane() {
        return structurePane;
    }

    /**
     * Retrieves the underlying data model associated with the visual pane.
     *
     * @return the {@link Structure} object currently being displayed.
     */
    public Structure getStructure() {
        return structurePane.getStructure();
    }
}
