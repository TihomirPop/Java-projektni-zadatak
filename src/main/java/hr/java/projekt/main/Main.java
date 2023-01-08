package hr.java.projekt.main;

import hr.java.projekt.entitet.Series;
import hr.java.projekt.entitet.Show;
import hr.java.projekt.entitet.StartEndDate;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main extends Application {
    static Stage mainStage;
    static Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), screenSize.getWidth(), screenSize.getHeight());
        scene.getStylesheets().add("style.css");
        stage.setTitle("MML");
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.show();
    }

    public static void prikaziScene(FXMLLoader fxmlLoader){
        try {
            Scene scene = new Scene(fxmlLoader.load(), screenSize.getWidth(), screenSize.getHeight());
            scene.getStylesheets().add("style.css");
            mainStage.setMaximized(true);
            mainStage.setScene(scene);
            mainStage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void pogresanUnosPodataka(List<String> podaci){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Pogrešan unos podataka");
        alert.setHeaderText("Molimo ispravite sljedeće pogreške:");

        String greska = podaci.get(0);
        for(int i = 1; i < podaci.size(); i++)
            greska += ", " + podaci.get(i);
        greska = greska.substring(0, 1).toUpperCase() + greska.substring(1);
        if(podaci.size() == 1)
            alert.setContentText(greska + " je obvezan podatak!");
        else
            alert.setContentText(greska + " su obavezni podaci!");

        alert.showAndWait();
    }
    public static void main(String[] args) {launch();}
}
