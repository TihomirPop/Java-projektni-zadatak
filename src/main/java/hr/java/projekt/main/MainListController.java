package hr.java.projekt.main;

import hr.java.projekt.db.DataBase;
import hr.java.projekt.entitet.*;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleListProperty;
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
import java.util.Set;

public class MainListController {
    @FXML
    private TableView<ImageShow<Show>> showTableView;

    @FXML
    private TableColumn<ImageShow<Show>, String> imgTableColumn;
    @FXML
    private TableColumn<ImageShow<Show>, String> naslovTableColumn;
    @FXML
    private TableColumn<ImageShow<Show>, String> zanroviTableColumn;
    @FXML
    private TableColumn<ImageShow<Show>, String> tipTableColumn;
    @FXML
    private TableColumn<ImageShow<Show>, String> prosjekTableColumn;
    @FXML
    private ComboBox<Score> ocjeneComboBox;
    @FXML
    private ListView<Genre> zanroviListView;
    @FXML
    private TextField nazivTextField;
    @FXML
    private RadioButton serijaRadioButton;
    @FXML
    private RadioButton filmRadioButton;
    @FXML
    private RadioButton veceRadioButton;
    @FXML
    private RadioButton manjeRadioButton;

    public void initialize() {
        imgTableColumn.setCellValueFactory(new PropertyValueFactory<>("imageView"));
        naslovTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getShow().getPrevedeniNaslov()));
        zanroviTableColumn.setCellValueFactory(data -> {
            List<Genre> genres = data.getValue().getShow().getGenres().stream().toList();
            String string = genres.get(0).toString().substring(0,1) + genres.get(0).toString().substring(1).toLowerCase();
            for(int i = 1; i < genres.size(); i++)
                string += ", " + genres.get(i).toString().substring(0,1) + genres.get(i).toString().substring(1).toLowerCase();
            string = string.replaceAll("_", " ");
            return new SimpleStringProperty(string);
        });
        tipTableColumn.setCellValueFactory(data ->{
            String string = "N/A";
            if(data.getValue().getShow() instanceof Series series)
                string = "Serija - " + series.getNumberOfEpisodes().toString() + " ep";
            else if(data.getValue().getShow() instanceof Movie)
                string = "Film";
            return new SimpleStringProperty(string);
        });
        prosjekTableColumn.setCellValueFactory(data -> new SimpleStringProperty("9.99"));
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
