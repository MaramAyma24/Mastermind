import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        GameController gameController = new GameController();
        primaryStage.setTitle("MASTERMIND");
        primaryStage.setResizable(false);
        gameController.startTimer();
        VBox layout = gameController.creatMainLayout();
        Scene scene = new Scene(layout , 800 , 600);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css").toExternalForm()));
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("mastermind.JPG"))));
        primaryStage.setScene(scene);
        primaryStage.show();

    }

}