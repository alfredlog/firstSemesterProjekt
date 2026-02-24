package hProjekt.controller.gui.menus;

import hProjekt.controller.gui.SceneController;
import hProjekt.view.menus.MainMenuBuilder;
import javafx.scene.layout.Region;
import javafx.util.Builder;

/**
 * The main menu scene controller.
 */
public class MainMenuSceneController implements SceneController {

    private final Builder<Region> builder;

    /**
     * Creates a new main menu scene controller.
     */
    public MainMenuSceneController() {
        builder = new MainMenuBuilder(
                SceneController::loadSetupGameScene,
                SceneController::loadLeaderboardScene,
                SceneController::quit,
                SceneController::loadAboutScene,
                SceneController::loadMapEditorScene);
    }

    @Override
    public String getTitle() {
        return "Main Menu";
    }

    @Override
    public Builder<Region> getBuilder() {
        return builder;
    }
}
