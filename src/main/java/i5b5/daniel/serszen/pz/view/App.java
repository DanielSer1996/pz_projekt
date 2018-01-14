package i5b5.daniel.serszen.pz.view;

import i5b5.daniel.serszen.pz.view.delegates.ViewDelegate;
import i5b5.daniel.serszen.pz.view.scenes.StartingScene;
import javafx.application.Application;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class App  extends Application{

    private static final double SCREEN_WIDTH = Screen.getScreens().get(0).getBounds().getWidth();
    private static final double SCREEN_HEIGHT = Screen.getScreens().get(0).getBounds().getHeight();
    private final Logger log = LogManager.getLogger(App.class);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        ViewDelegate viewDelegate = ViewDelegate.getInstance(primaryStage);

        primaryStage.setTitle("Katalog części samochodowych");

        primaryStage.setResizable(true);
        viewDelegate.changeScene(viewDelegate.chooseSceneByName(StartingScene.class.getSimpleName()),null);

        primaryStage.show();
    }

    public static double getScreenWidth() {
        return SCREEN_WIDTH;
    }

    public static double getScreenHeight() {
        return SCREEN_HEIGHT;
    }
}
