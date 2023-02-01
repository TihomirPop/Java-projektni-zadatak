package hr.java.projekt.main;

import hr.java.projekt.db.DataBase;
import hr.java.projekt.entitet.*;
import hr.java.projekt.exceptions.BazaPodatakaException;
import hr.java.projekt.exceptions.KriviInputException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;

public class ShowViewController {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    @FXML
    private ImageView imageView;
    @FXML
    private TextArea opis;
    @FXML
    private Label orginalniNaslov;
    @FXML
    private Label prevedeniNaslov;
    @FXML
    private Label tip;
    @FXML
    private Label epizode;
    @FXML
    private Label pocetak;
    @FXML
    private Label kraj;
    @FXML
    private Label studio;
    @FXML
    private Label zanrovi;
    @FXML
    private ComboBox<Score> ocjena;
    @FXML
    private TextField epizodeTextField;
    @FXML
    private Button spremi;
    @FXML
    private Button obrisi;
    @FXML
    private Label prosjek;
    @FXML
    private Label nastavakLabel;
    @FXML
    private Label nastavak;
    @FXML
    private Label prethodnikLabel;
    @FXML
    private Label prethodnik;

    private Show show;
    private UserShow userShow;
    private Show prethodnikShow, nastavakShow;
    @FXML
    private void initialize(){
        try {
            Platform.runLater(() -> focus());
            show = Main.currentShow;

            imageView.setImage(new Image(Path.of(show.getSlika()).toAbsolutePath().toString(), 0, 1024, true, true));
            imageView.setPreserveRatio(true);

            orginalniNaslov.setText(show.getOrginalniNaslov());
            prevedeniNaslov.setText(show.getPrevedeniNaslov());
            opis.setText(show.getOpis());
            studio.setText(studio.getText() + " " + show.getStudio());
            ocjena.setItems(FXCollections.observableList(Arrays.stream(Score.values()).toList()));
            prosjek.setText(String.format("%.2f", show.getProsjek()));

            List<Genre> genres = show.getGenres().stream().toList();
            String string = zanrovi.getText() + " " + genres.get(0).toString().substring(0, 1) + genres.get(0).toString().substring(1).toLowerCase();
            for (int i = 1; i < genres.size(); i++)
                string += ", " + genres.get(i).toString().substring(0, 1) + genres.get(i).toString().substring(1).toLowerCase();
            zanrovi.setText(string.replaceAll("_", " "));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.YYYY.");
            if (show instanceof Series series) {
                tip.setText(tip.getText() + " Serija");
                epizode.setText(epizode.getText() + " " + series.getNumberOfEpisodes().toString());
                pocetak.setText(pocetak.getText() + " " + series.getStartEndDate().startDate().format(formatter));
                kraj.setText(kraj.getText() + " " + series.getStartEndDate().endDate().format(formatter));
                epizodeTextField.setPromptText("0/" + series.getNumberOfEpisodes().toString());
            } else if (show instanceof Movie movie) {
                tip.setText(tip.getText() + " Film");
                pocetak.setText(pocetak.getText() + " " + movie.getReleaseDate().format(formatter));
                epizodeTextField.setPromptText("0/1");
                epizode.setVisible(false);
                epizode.setManaged(false);
                kraj.setVisible(false);
                kraj.setManaged(false);
            }

            if (Main.currentUser == null) {
                ocjena.setDisable(true);
                epizodeTextField.setDisable(true);
                spremi.setDisable(true);
                obrisi.setDisable(true);
            } else {
                userShow = DataBase.getUserShow(Main.currentUser, show);
                if (userShow != null) {
                    ocjena.getSelectionModel().select(userShow.getScore());
                    epizodeTextField.setText(userShow.getWatched().toString());
                }
            }

            List<Show> shows = DataBase.getShowSequence(show);
            for (int i = 0; i < show.getIdSeqience().size(); i++)
                if (shows.get(i).getId().equals(show.getId())) {
                    if ((i - 1) >= 0) {
                        prethodnik.setText(shows.get(i - 1).getPrevedeniNaslov());
                        prethodnikShow = shows.get(i - 1);
                    } else {
                        prethodnik.setVisible(false);
                        prethodnik.setManaged(false);
                        prethodnikLabel.setVisible(false);
                        prethodnikLabel.setManaged(false);
                    }
                    if ((i + 1) < show.getIdSeqience().size()) {
                        nastavak.setText(shows.get(i + 1).getPrevedeniNaslov());
                        nastavakShow = shows.get(i + 1);
                    } else {
                        nastavak.setVisible(false);
                        nastavak.setManaged(false);
                        nastavakLabel.setVisible(false);
                        nastavakLabel.setManaged(false);
                    }
                }
        } catch (BazaPodatakaException e){

        }
    }

    @FXML
    private void spremi(){
        Score userOcjena = ocjena.getValue();
        String userEpizode = epizodeTextField.getText();
        List<String> greske = new ArrayList<>();

        if(userOcjena == null)
            greske.add("ocjena");
        if(userEpizode.isEmpty() || !userEpizode.matches("[0-9]+"))
            greske.add("epizode");

        try {
            if (greske.isEmpty()) {
                Integer epizodeInt = Integer.parseInt(userEpizode);
                if (show instanceof Series series) {
                    if (epizodeInt >= 0 && epizodeInt <= series.getNumberOfEpisodes()) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Spremanje");
                        alert.setHeaderText("Želite li spremiti show?");
                        ButtonType daButton = new ButtonType("Da", ButtonBar.ButtonData.YES);
                        ButtonType neButton = new ButtonType("Ne", ButtonBar.ButtonData.NO);
                        alert.getButtonTypes().setAll(daButton, neButton);
                        alert.showAndWait().ifPresent(response -> {
                            if (response == daButton)
                                addUserShow(userOcjena, epizodeInt);
                        });
                    } else {
                        try {
                            greske.add("epizode");
                            Main.pogresanUnosPodataka(greske);
                        } catch (KriviInputException e) {

                        }
                    }
                } else if (show instanceof Movie) {
                    if (epizodeInt >= 0 && epizodeInt <= 1) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Spremanje");
                        alert.setHeaderText("Želite li spremiti show?");
                        ButtonType daButton = new ButtonType("Da", ButtonBar.ButtonData.YES);
                        ButtonType neButton = new ButtonType("Ne", ButtonBar.ButtonData.NO);
                        alert.getButtonTypes().setAll(daButton, neButton);
                        alert.showAndWait().ifPresent(response -> {
                            if (response == daButton)
                                addUserShow(userOcjena, epizodeInt);
                        });
                    } else {
                        greske.add("epizode");
                        Main.pogresanUnosPodataka(greske);
                    }
                }
            } else
                Main.pogresanUnosPodataka(greske);
            prosjek.setText(String.format("%.2f", show.getProsjek()));
        } catch (KriviInputException e){
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }

    private void addUserShow(Score userOcjena, Integer epizodeInt) {
        try {
            if (userShow == null) {
                UserShowBuilder userShowBuilder = new UserShowBuilder(Main.currentUser);
                userShow = userShowBuilder.saShow(show).saScore(userOcjena).saWatched(epizodeInt).build();
                DataBase.addUserShow(userShow);
                userShow = DataBase.getUserShow(Main.currentUser, show);
            } else {
                userShow.setScore(userOcjena);
                userShow.setWatched(epizodeInt);
                DataBase.updateUserShow(userShow);
            }
        } catch (BazaPodatakaException e){
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }

    @FXML
    private void obrisi() {
        if (userShow != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Brisanje");
            alert.setHeaderText("Želite li obrisati show?");
            ButtonType daButton = new ButtonType("Da", ButtonBar.ButtonData.YES);
            ButtonType neButton = new ButtonType("Ne", ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(daButton, neButton);
            alert.showAndWait().ifPresent(response -> {
                if (response == daButton)
                    deleteShow();
            });
        }
    }
    private void deleteShow() {
        try {
            DataBase.deleteUserShow(userShow);
            userShow = null;
            ocjena.getSelectionModel().clearSelection();
            epizodeTextField.setText("");
            prosjek.setText(String.format("%.2f", show.getProsjek()));
        } catch (BazaPodatakaException e){
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }

    @FXML
    private void focus(){
        imageView.requestFocus();
    }

    @FXML
    private void goToPrethodnik(){
        Main.currentShow = prethodnikShow;
        Main.prikaziScene(new FXMLLoader(Main.class.getResource("showView.fxml")));
    }
    @FXML
    private void goToNastavak(){
        Main.currentShow = nastavakShow;
        Main.prikaziScene(new FXMLLoader(Main.class.getResource("showView.fxml")));
    }
}
