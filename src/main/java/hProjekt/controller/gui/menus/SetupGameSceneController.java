package hProjekt.controller.gui.menus;

import java.util.List;

import hProjekt.Config;
import hProjekt.controller.MapSaveController;
import hProjekt.controller.gui.SceneController;
import hProjekt.model.GameState;
import hProjekt.model.PlayerImpl;
import hProjekt.model.grid.HexGrid;
import hProjekt.view.menus.SetupGameBuilder;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.util.Builder;

/**
 * The controller for the setup game scene.
 */
public class SetupGameSceneController implements SceneController {
    private final SetupGameBuilder builder;
    private final GameState gameState;
    private final ObservableList<PlayerImpl.Builder> playerBuilderList = FXCollections.observableArrayList();
    private final SimpleObjectProperty<String> selectedMap = new SimpleObjectProperty<>();

    /**
     * Creates a new setup game scene controller.
     *
     * @param gameState the game state
     */
    public SetupGameSceneController(final GameState gameState) {
        this.gameState = gameState;
        final List<String> availableMaps = MapSaveController.getSavedMaps();

        builder = new SetupGameBuilder(SceneController::loadMainMenuScene, playerBuilderList, this::startGameHandler,
                selectedMap, availableMaps);
    }

    /**
     * The handler for the start game button.
     * <p>
     * Tries to start the game with the current players. If there are not enough
     * players, the game will not start.
     *
     * @return true if the game was started, false if not
     */
    private boolean startGameHandler() {
        if (playerBuilderList.size() < Config.MIN_PLAYERS || playerBuilderList.size() > Config.MAX_PLAYERS) {
            return false;
        }
        final HexGrid grid = MapSaveController.loadMap(selectedMap.get());
        if (grid == null) {
            return false;
        }
        playerBuilderList.forEach(p -> gameState.addPlayer(p.build(gameState.getGrid())));

        System.out.println("Selected map: " + selectedMap.get());
        gameState.setGrid(grid);

        SceneController.loadGameScene();
        return true;
    }

    @Override
    public String getTitle() {
        return "Setup Game";
    }

    @Override
    public Builder<Region> getBuilder() {
        return builder;
    }

    @Override
    public Region buildView() {
        playerBuilderList.clear();
        playerBuilderList.add(
                builder.nextPlayerBuilder()
                        .name(System.getProperty("user.name"))
                        .color(Color.AQUA));
        for (int i = 1; i < Config.MIN_PLAYERS; i++) {
            playerBuilderList.add(builder.nextPlayerBuilder());
        }
        return SceneController.super.buildView();
    }
}
