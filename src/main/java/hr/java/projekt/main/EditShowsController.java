package hr.java.projekt.main;

import hr.java.projekt.entitet.Show;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class EditShowsController {

    @FXML
    ChoiceBox<Show> showChoiceBox;
    @FXML
    Button editButton;

    @FXML
    public void initialize() {
        Show show;
        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream("dat/shows/steinsGate"))) {
            show = (Show)in.readObject();
            ArrayList<Show> list = new ArrayList<>();
            list.add(show);
            showChoiceBox.setItems(FXCollections.observableList(list));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
