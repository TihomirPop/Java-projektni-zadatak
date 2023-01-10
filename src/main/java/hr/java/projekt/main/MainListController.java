package hr.java.projekt.main;

import hr.java.projekt.entitet.Show;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class MainListController {
    @FXML
    private TableView<Show> showTableView;
    @FXML
    private TableColumn<Show, String> idTableColumn;
    @FXML
    private TableColumn<Show, String> naslovaTableColumn;
    @FXML
    private Label selectedNaslov;

    @FXML
    private ImageView imageView;

    public void initialize() {
        Show show;
        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream("dat/shows/steinsGate"))) {
            show = (Show)in.readObject();
            idTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getId().toString()));


            naslovaTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getOrginalniNaslov()));

            ArrayList<Show> list = new ArrayList<>();
            list.add(show);
            showTableView.setItems(FXCollections.observableList(list));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

//        showTableView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Show> observable, Show oldValue, Show newValue) -> {
//            try{
//
//            }catch (NullPointerException e){
//                System.out.println(e.getMessage());
//            }
//        });
    }

    @FXML
    void rowSelect(){
        if(!showTableView.getSelectionModel().isEmpty()){
            Show selectedShow = showTableView.getSelectionModel().getSelectedItem();
            File file = new File(showTableView.getSelectionModel().getSelectedItem().getSlika());
            imageView.setImage(new Image(file.getAbsolutePath()));
            selectedNaslov.setText(selectedShow.getOrginalniNaslov());
        }
    }
}
