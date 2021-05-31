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
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * JavaFX App
 */
public class Main extends Application {

    private static Scene scene;

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
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static Stage openNewStage(String fxml,Class C,String icon) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(C.getResource(fxml+".fxml"));
        Parent root = fxmlLoader.load();
        Stage newStage = new Stage();
        Scene newScene = new Scene(root);
        if (!icon.equals("none")){
            newStage.getIcons().add(new Image(Objects.requireNonNull(C.getResource("media/"+icon)).toString()));
        }
        newStage.setScene(newScene);
        return newStage;
    }

    public void play(){
        URL path = getClass().getResource("audio/lobby.mp3");
        Media media = new Media(path.toString());
        MediaPlayer audio = new MediaPlayer(media);
        audio.setVolume(0.5);
//        audio.setAutoPlay(true);
        audio.play();
    }

    public static void main(String[] args) {
        launch();
    }

}