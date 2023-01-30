package hr.java.projekt.main;

import hr.java.projekt.db.DataBase;
import hr.java.projekt.entitet.ImageShow;
import hr.java.projekt.entitet.ImageShows;
import hr.java.projekt.entitet.Show;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class MainListController {
    @FXML
    private TableView<ImageShow<Show>> showTableView;
    @FXML
    private TableColumn<ImageShow<Show>, String> idTableColumn;
    @FXML
    private TableColumn<ImageShow<Show>, String> naslovaTableColumn;
    @FXML
    private TableColumn<ImageShow<Show>, String> imgTableColumn;
    @FXML
    private Label selectedNaslov;

    @FXML
    private ImageView imageView;

    public void initialize() {
        imgTableColumn.setCellValueFactory(new PropertyValueFactory<>("imageView"));
        idTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getShow().getId().toString()));
        showTableView.setItems(FXCollections.observableList((ImageShows.toImageShowList(DataBase.getShows()))));
        showTableView.setRowFactory(tableView -> {
            final TableRow<ImageShow<Show>> row = new TableRow<>();

            row.hoverProperty().addListener((observable) -> {
                final ImageShow<Show> imageShow = row.getItem();
                if(imageShow == null)
                    return;
                Timeline timeline = new Timeline();
                if(row.isHover()) {
                    KeyValue keyValue = new KeyValue(imageShow.getImageView().fitHeightProperty(), 128, Interpolator.LINEAR);
                    timeline.getKeyFrames().add(new KeyFrame(Duration.millis(50), keyValue));
                    timeline.play();
                }
                else{
                    KeyValue keyValue = new KeyValue(imageShow.getImageView().fitHeightProperty(), 64, Interpolator.LINEAR);
                    timeline.getKeyFrames().add(new KeyFrame(Duration.millis(50), keyValue));
                    timeline.play();
                }
            });

            return row;
        });

//        showTableView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Show> observable, Show oldValue, Show newValue) -> {
//            try{
//
//            }catch (NullPointerException e){
//                System.out.println(e.getMessage());
//            }
//        });
    }
}
