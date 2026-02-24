package hProjekt;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.function.Consumer;

import org.tudalgo.algoutils.student.annotation.DoNotTouch;

import hProjekt.controller.GameController;
import hProjekt.controller.gui.SceneSwitcher;
import hProjekt.controller.gui.SceneSwitcher.SceneType;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * The main application of the game.
 * <p>
 * <b>Only touch when you really know what you are doing!</b>
 */
@DoNotTouch
public class MyApplication extends Application {
    private final Consumer<GameController> gameLoopStart = gc -> {
        final Thread gameLoopThread = new Thread(gc::startGame);
        gameLoopThread.setName("GameLoopThread");
        gameLoopThread.setDaemon(true);
        gameLoopThread.start();
    };

    @Override
    public void start(final Stage stage) {
        // Don't ask JavaFX does some weird shit and sucks up errors otherwise...
        System.setErr(new PrintStream(new OutputStream() {
            @Override
            public void write(final int b) {
                System.out.write(b);
            }
        }));

        stage.setMinWidth(1000);
        stage.setMinHeight(520);
        stage.setWidth(1280);
        stage.setHeight(720);

        // Set custom icon in the task bar
        final Image appIcon = new Image("/images/stage-icon.png");
        stage.getIcons().add(appIcon);
        stage.show();

        SceneSwitcher.getInstance(stage, gameLoopStart).loadScene(SceneType.MAIN_MENU);
    }

    /**
     * The main method of the application.
     *
     * @param args The launch arguments of the application.
     */
    public static void main(final String[] args) {
        Application.launch(args);
    }
}
