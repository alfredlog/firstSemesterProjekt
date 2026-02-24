package hProjekt.controller.gui.menus;

import java.util.List;

import hProjekt.controller.GameController;
import hProjekt.controller.LeaderboardController;
import hProjekt.controller.gui.SceneController;
import hProjekt.model.Player;
import hProjekt.view.menus.EndScreenBuilder;
import javafx.scene.layout.Region;
import javafx.util.Builder;

/**
 * The controller for the end screen scene.
 */
public class EndScreenSceneController implements SceneController {

    private final Builder<Region> builder;

    /**
     * Creates a new end screen scene controller.
     * Saves the data of all the players to the leaderboard.
     *
     * @param players the players to display on the end screen
     */
    public EndScreenSceneController(final List<Player> players, final GameController gameController) {
        for (final Player player : players) {
            LeaderboardController.savePlayerData(player.getName(), player.getTotalGoldCardValue(), player.isAi());
        }
        builder = new EndScreenBuilder(SceneController::loadMainMenuScene, players);

        gameController.stop();
    }

    @Override
    public String getTitle() {
        return "End Screen";
    }

    @Override
    public Builder<Region> getBuilder() {
        return builder;
    }
}
