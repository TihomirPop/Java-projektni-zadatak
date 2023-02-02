package hr.java.projekt.main;

import hr.java.projekt.util.DataBase;
import hr.java.projekt.entitet.*;
import hr.java.projekt.exceptions.BazaPodatakaException;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class MyListController {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
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
    private TableColumn<ImageShow<Show>, String> ocjenaTableColumn;
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
    @FXML
    private GridPane takeFocus;
    private ToggleGroup tipToggleGroup = new ToggleGroup();
    private ToggleGroup ocjeneFilterToggleGroup = new ToggleGroup();
    private List<Show> shows;
    private Map<Show, Score> ocjeneMap = new HashMap<>();

    public void initialize() {
        try {
            Platform.runLater(() -> takeFocus.requestFocus());

            shows = DataBase.getUsersShows(Main.currentUser);

            for (Show show : shows)
                ocjeneMap.put(show, DataBase.getUserShow(Main.currentUser, show).getScore());

            imgTableColumn.setCellValueFactory(new PropertyValueFactory<>("imageView"));
            naslovTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getShow().getPrevedeniNaslov()));
            zanroviTableColumn.setCellValueFactory(data -> {
                List<Genre> genres = data.getValue().getShow().getGenres().stream().toList();
                String string = genres.get(0).toString().substring(0, 1) + genres.get(0).toString().substring(1).toLowerCase();
                for (int i = 1; i < genres.size(); i++)
                    string += ", " + genres.get(i).toString().substring(0, 1) + genres.get(i).toString().substring(1).toLowerCase();
                string = string.replaceAll("_", " ");
                return new SimpleStringProperty(string);
            });
            tipTableColumn.setCellValueFactory(data -> {
                String string = "N/A";
                if (data.getValue().getShow() instanceof Series series)
                    string = "Serija - " + series.getNumberOfEpisodes().toString() + " ep";
                else if (data.getValue().getShow() instanceof Movie)
                    string = "Film";
                return new SimpleStringProperty(string);
            });
            ocjenaTableColumn.setCellValueFactory(data -> new SimpleStringProperty(ocjeneMap.get(data.getValue().getShow()).getScore().toString()));
            showTableView.setRowFactory(tableView -> {
                final TableRow<ImageShow<Show>> row = new TableRow<>();

                row.hoverProperty().addListener((observable) -> {
                    final ImageShow<Show> imageShow = row.getItem();
                    if (imageShow == null)
                        return;
                    row.setStyle("-fx-cursor: hand;");
                    Timeline timeline = new Timeline();
                    if (row.isHover()) {
                        KeyValue keyValue = new KeyValue(imageShow.getImageView().fitHeightProperty(), 128, Interpolator.LINEAR);
                        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(50), keyValue));
                        timeline.play();
                    } else {
                        KeyValue keyValue = new KeyValue(imageShow.getImageView().fitHeightProperty(), 64, Interpolator.LINEAR);
                        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(50), keyValue));
                        timeline.play();
                    }
                });
                return row;
            });
            showTableView.setItems(FXCollections.observableList((ImageShows.toImageShowList(shows))));

            serijaRadioButton.setToggleGroup(tipToggleGroup);
            filmRadioButton.setToggleGroup(tipToggleGroup);
            veceRadioButton.setToggleGroup(ocjeneFilterToggleGroup);
            manjeRadioButton.setToggleGroup(ocjeneFilterToggleGroup);

            zanroviListView.setItems(FXCollections.observableArrayList(List.of(Genre.values())));
            zanroviListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

            ocjeneComboBox.setItems(FXCollections.observableList(Arrays.stream(Score.values()).toList()));

            showTableView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends ImageShow<Show>> observable, ImageShow<Show> oldValue, ImageShow<Show> newValue) -> {
                if (newValue != null) {
                    Main.currentShow = newValue.getShow();
                    Main.prikaziScene(new FXMLLoader(Main.class.getResource("showView.fxml")));
                }
            });
        } catch (BazaPodatakaException e){
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }

    @FXML
    private void filterList(){
        String naziv = nazivTextField.getText();
        Boolean serija = serijaRadioButton.isSelected();
        Boolean film = filmRadioButton.isSelected();
        List<Genre> zanrovi = zanroviListView.getSelectionModel().getSelectedItems();
        Score ocjena = ocjeneComboBox.getValue();
        Boolean vece = veceRadioButton.isSelected();
        Boolean manje = manjeRadioButton.isSelected();
        List<Show> filteredList = shows;

        if(!naziv.isEmpty())
            filteredList = filteredList.stream().filter(show -> (show.getOrginalniNaslov().toLowerCase().contains(naziv.toLowerCase()) || show.getPrevedeniNaslov().toLowerCase().contains(naziv.toLowerCase()))).toList();
        if(serija)
            filteredList = filteredList.stream().filter(show -> show instanceof Series).toList();
        else if(film)
            filteredList = filteredList.stream().filter(show -> show instanceof Movie).toList();
        if(!zanrovi.isEmpty())
            filteredList = filteredList.stream().filter(show -> show.getGenres().containsAll(zanrovi)).toList();
        if(ocjena != null){
            if(vece)
                filteredList = filteredList.stream().filter(show -> (ocjeneMap.get(show).getScore() >= ocjena.getScore())).toList();
            else if(manje)
                filteredList = filteredList.stream().filter(show -> (ocjeneMap.get(show).getScore() <= ocjena.getScore())).toList();
        }
        showTableView.setItems(FXCollections.observableList(ImageShows.toImageShowList(filteredList)));
    }
    @FXML
    private void clickSeries(MouseEvent event){
        if(event.getButton().equals(MouseButton.SECONDARY))
            serijaRadioButton.setSelected(false);
        filterList();
    }
    @FXML
    private void clickFilm(MouseEvent event){
        if(event.getButton().equals(MouseButton.SECONDARY))
            filmRadioButton.setSelected(false);
        filterList();
    }
    @FXML
    private void clickVece(MouseEvent event){
        if(event.getButton().equals(MouseButton.SECONDARY))
            veceRadioButton.setSelected(false);
        filterList();
    }
    @FXML
    private void clickManje(MouseEvent event){
        if(event.getButton().equals(MouseButton.SECONDARY))
            manjeRadioButton.setSelected(false);
        filterList();
    }
    @FXML
    private void clickZanrovi(MouseEvent event){
        if(event.getButton().equals(MouseButton.SECONDARY))
            zanroviListView.getSelectionModel().clearSelection();
        filterList();
    }
    @FXML
    private void clickOcjena(MouseEvent event){
        if(event.getButton().equals(MouseButton.SECONDARY)) {
            ocjeneComboBox.getSelectionModel().clearSelection();
            takeFocus.requestFocus();
            filterList();
        }
    }
}
