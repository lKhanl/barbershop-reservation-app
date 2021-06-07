package edu.eskisehir;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * JavaFX App
 */
public class Main extends Application {

    private static Scene scene;
    private static MediaPlayer audio;
    private static boolean isMute;

    @Override
    public void start(Stage stage) throws IOException {
        play();
        scene = new Scene(loadFXML("Main"));
        stage.setScene(scene);
        stage.setTitle("Hello Customer!");
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResource("media/customer.png")).toString()));
        stage.setResizable(false);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Paths.get("src/main/resources/edu/eskisehir/fxml/" + fxml + ".fxml").toUri().toURL());
        return fxmlLoader.load();
    }

    public static Stage openNewStage(String fxml, String icon) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Paths.get("src/main/resources/edu/eskisehir/fxml/" + fxml + ".fxml").toUri().toURL());
        Parent root = fxmlLoader.load();
        Stage newStage = new Stage();
        Scene newScene = new Scene(root);
        if (!icon.equals("none")) {
            Image img = new Image(new FileInputStream("src/main/resources/edu/eskisehir/media/" + icon));
            newStage.getIcons().add(img);
        }
        newStage.setScene(newScene);
        return newStage;
    }

    public static void play() {
        URL path = Main.class.getResource("audio/lobby.mp3");
        Media media = new Media(path.toString());
        audio = new MediaPlayer(media);
        audio.setVolume(0.1);
//        audio.setAutoPlay(true);
        audio.play();
    }

    public static void pauseAndPlay() {
        isMute = !isMute;
        if (isMute) {
            audio.pause();
        } else {
            audio.play();
        }
    }

    public static void main(String[] args) {
        launch();
    }

}