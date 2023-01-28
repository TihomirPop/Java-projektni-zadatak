package hr.java.projekt.main;

import hr.java.projekt.entitet.Show;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.ArrayList;

public class EditShowsController {

    @FXML
    private ComboBox<Show> showComboBox;
    @FXML
    private Label lokacijaSlike;
    FileChooser fileChooser = new FileChooser();

    public void initialize() {
        Show show;
        fileChooser.setTitle("Odabir slike");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPG/PNG", "*.jpg", "*.png"));
        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream("dat/shows/steinsGate"))) {
            show = (Show)in.readObject();
            ArrayList<Show> list = new ArrayList<>();
            list.add(show);
            showComboBox.setItems(FXCollections.observableList(list));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void odabirSlike(){
        File file = fileChooser.showOpenDialog(Main.mainStage);
        if (file != null) {
            lokacijaSlike.setText(file.getAbsolutePath());
        }
    }
}
