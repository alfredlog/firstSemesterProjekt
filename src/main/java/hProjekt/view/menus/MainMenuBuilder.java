package hProjekt.view.menus;

import hProjekt.view.utils.Images;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Builder;

/**
 * Builder for the Main Menu.
 */
public class MainMenuBuilder implements Builder<Region> {
    private final Runnable loadSetupGameScene;
    private final Runnable loadAboutSceneAction;
    private final Runnable loadLeaderboardAction;
    private final Runnable loadMapEditorAction;
    private final Runnable quitAction;

    /**
     * Constructor for the MainMenuBuilder.
     *
     * @param loadSetupGameScene    the action to load the setup game scene
     * @param loadLeaderboardAction the action to load the leaderboard
     * @param quitAction            the action to quit the application
     * @param loadAboutSceneAction  the action to load the about scene
     */
    public MainMenuBuilder(final Runnable loadSetupGameScene, final Runnable loadLeaderboardAction,
            final Runnable quitAction, final Runnable loadAboutSceneAction, final Runnable loadMapEditorAction) {
        this.loadSetupGameScene = loadSetupGameScene;
        this.loadMapEditorAction = loadMapEditorAction;
        this.quitAction = quitAction;
        this.loadAboutSceneAction = loadAboutSceneAction;
        this.loadLeaderboardAction = loadLeaderboardAction;
    }

    @Override
    public Region build() {
        final StackPane root = new StackPane();

        root.getStylesheets().add("css/main.css");
        root.getStylesheets().add("css/menu.css");

        final VBox centerBox = new VBox(20);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(50));

        ImageView logo;
        try {
            logo = new ImageView(Images.TITLE_IMAGE);
            logo.setFitWidth(280);
            logo.setPreserveRatio(true);
        } catch (final NullPointerException e) {
            logo = new ImageView();
            System.out.println("Error: Logo not found. Make sure the path is correct.");
        }

        final Button startGameButton = new Button("Create Game");
        startGameButton.setMinWidth(200);
        startGameButton.setOnAction(event -> loadSetupGameScene.run());

        final Button leaderboardButton = new Button("Leaderboard");
        leaderboardButton.setMinWidth(200);
        leaderboardButton.setOnAction(event -> loadLeaderboardAction.run());

        final Button mapEditorButton = new Button("Map Editor");
        mapEditorButton.setMinWidth(200);
        mapEditorButton.setOnAction(event -> loadMapEditorAction.run());

        centerBox.getChildren().addAll(logo, startGameButton, mapEditorButton, leaderboardButton);

        final HBox bottomBox = new HBox();
        bottomBox.setPadding(new Insets(20));
        bottomBox.setAlignment(Pos.BOTTOM_LEFT);

        final Button exitButton = new Button("Exit");
        exitButton.setMinWidth(150);
        exitButton.setOnAction(event -> quitAction.run());

        final Button aboutButton = new Button("About");
        aboutButton.setMinWidth(150);
        aboutButton.setOnAction(event -> loadAboutSceneAction.run());

        final Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        bottomBox.getChildren().addAll(exitButton, spacer, aboutButton);

        final BorderPane layout = new BorderPane();
        layout.setCenter(centerBox);
        layout.setBottom(bottomBox);
        root.getChildren().add(layout);

        return root;
    }
}
