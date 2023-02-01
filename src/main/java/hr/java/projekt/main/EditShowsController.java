package hr.java.projekt.main;

import hr.java.projekt.db.DataBase;
import hr.java.projekt.entitet.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.util.Callback;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class EditShowsController {
    @FXML
    private TextField traziTextField;
    @FXML
    private ComboBox<Show> showComboBox;
    @FXML
    private Button obrisiButton;
    @FXML
    private Button spremiButton;
    @FXML
    private TextField orginalniNazivTextField;
    @FXML
    private TextField prevedeniNazivTextField;
    @FXML
    private Label lokacijaSlikeLabel;
    @FXML
    private TextField studioTextField;
    @FXML
    private TextField traziNastavkeTextField;
    @FXML
    private ComboBox<Show> showNastavciComboBox;
    @FXML
    private ListView<Show> nastavciListView;
    @FXML
    private TextArea opisTextArea;
    @FXML
    private ListView<Genre> zanroviListView;
    @FXML
    private ComboBox<String> tipComboBox;
    @FXML
    private DatePicker pocetakDatePicker;
    @FXML
    private DatePicker krajDatePicker;
    @FXML
    private TextField brojEpizodaTextField;
    @FXML
    private Label krajLabel;
    @FXML
    private Label brojEpizodaLabel;
    private FileChooser fileChooser = new FileChooser();
    private List<Show> shows;
    private List<Show> showsWithNewShow;

    public void initialize() {
        refresh();
        fileChooser.setTitle("Odabir slike");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPG/PNG", "*.jpg", "*.png"));
        ArrayList<String> tipovi = new ArrayList<>(2);
        tipovi.add("Serija");
        tipovi.add("Film");
        tipComboBox.setItems(FXCollections.observableArrayList(tipovi));
        tipComboBox.getSelectionModel().selectFirst();
        zanroviListView.setItems(FXCollections.observableArrayList(List.of(Genre.values())));
        zanroviListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }
    private void refresh(){
        shows = DataBase.getShows();
        showsWithNewShow = new ArrayList<>();
        showsWithNewShow.add(new Series(-1l, "<novi show>", "<novi show>", null, null, null, null, new ArrayList<>(1), null, null));
        showsWithNewShow.get(0).getIdSeqience().add(-1l);
        showsWithNewShow.addAll(shows);
        showComboBox.setItems(FXCollections.observableArrayList(showsWithNewShow));
        showComboBox.getSelectionModel().selectFirst();
        showNastavciComboBox.setItems(FXCollections.observableArrayList(shows));
        nastavciListView.setItems(FXCollections.observableArrayList(showsWithNewShow.stream().filter(show -> show.getId().equals(-1l)).toList()));
        traziTextField.clear();
        traziNastavkeTextField.clear();
    }
    @FXML
    private void edit(){
        Show show = showComboBox.getValue();
        if(show == null)
            return;
        if(!show.getId().equals(-1l)){
            orginalniNazivTextField.setText(show.getOrginalniNaslov());
            prevedeniNazivTextField.setText(show.getPrevedeniNaslov());
            lokacijaSlikeLabel.setText(show.getSlika());
            studioTextField.setText(show.getStudio());
            showNastavciComboBox.setItems(FXCollections.observableArrayList(shows.stream().filter(s -> !show.getIdSeqience().contains(s.getId())).toList()));
            nastavciListView.setItems(FXCollections.observableArrayList(show.getIdSeqience().stream().map(id -> shows.stream().filter(s -> s.getId().equals(id)).toList().get(0)).collect(Collectors.toList())));
            opisTextArea.setText(show.getOpis());
            zanroviListView.getSelectionModel().clearSelection();
            for(Genre genre: show.getGenres())
                zanroviListView.getSelectionModel().select(genre);
            if(show instanceof Series series){
                tipComboBox.getSelectionModel().selectFirst();
                pocetakDatePicker.setValue(series.getStartEndDate().startDate());
                krajDatePicker.setValue(series.getStartEndDate().endDate());
                brojEpizodaTextField.setText(series.getNumberOfEpisodes().toString());
            }
            else if(show instanceof Movie movie){
                tipComboBox.getSelectionModel().selectLast();
                pocetakDatePicker.setValue(movie.getReleaseDate());
            }
        }
        else {
            orginalniNazivTextField.setText("");
            prevedeniNazivTextField.setText("");
            lokacijaSlikeLabel.setText("");
            studioTextField.setText("");
            showNastavciComboBox.setItems(FXCollections.observableArrayList(shows));
            nastavciListView.setItems(FXCollections.observableArrayList(show));
            opisTextArea.setText("");
            zanroviListView.getSelectionModel().clearSelection();
            tipComboBox.getSelectionModel().selectFirst();
            pocetakDatePicker.setValue(null);
            krajDatePicker.setValue(null);
            brojEpizodaTextField.setText("");
        }
    }
    @FXML
    private void tipChange(){
        if(tipComboBox.getValue().equals("Serija")){
            krajDatePicker.setVisible(true);
            brojEpizodaTextField.setVisible(true);
            krajLabel.setVisible(true);
            brojEpizodaLabel.setVisible(true);
        }
        else {
            krajDatePicker.setVisible(false);
            brojEpizodaTextField.setVisible(false);
            krajLabel.setVisible(false);
            brojEpizodaLabel.setVisible(false);
        }
    }
    @FXML
    private void odabirSlike(){
        File file = fileChooser.showOpenDialog(Main.mainStage);
        if (file != null) {
            lokacijaSlikeLabel.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void showSearch(){
        String search = traziTextField.getText();

        if(search.equals(""))
            showComboBox.setItems(FXCollections.observableArrayList(showsWithNewShow));
        else
            showComboBox.setItems(FXCollections.observableArrayList(showsWithNewShow.stream().filter(show -> show.getOrginalniNaslov().toLowerCase().contains(search.toLowerCase()) || show.getPrevedeniNaslov().toLowerCase().contains(search.toLowerCase())).toList()));
    }
    @FXML
    private void sequelSearch(){
        String search = traziNastavkeTextField.getText();

        showNastavciComboBox.setItems(FXCollections.observableArrayList(shows.stream().filter(s -> !nastavciListView.getItems().stream().mapToLong(Show::getId).boxed().toList().contains(s.getId())).toList()));
        if(!search.isEmpty())
            showNastavciComboBox.setItems(FXCollections.observableArrayList(showNastavciComboBox.getItems().stream().filter(s -> (s.getOrginalniNaslov().toLowerCase().contains(search.toLowerCase()) || s.getPrevedeniNaslov().toLowerCase().contains(search.toLowerCase()))).toList()));
    }
    @FXML
    private void addSequel(){
        if(!showNastavciComboBox.getSelectionModel().isEmpty()) {
            if(showNastavciComboBox.getValue().getIdSeqience().contains(showComboBox.getValue().getId())){
                nastavciListView.getItems().add(showNastavciComboBox.getValue());
                showNastavciComboBox.getItems().remove(showNastavciComboBox.getValue());
            }
            else {
                ArrayList<Show> showsSequence = new ArrayList<>();
                for (Long id : showNastavciComboBox.getValue().getIdSeqience())
                    showsSequence.add(shows.stream().filter(show -> show.getId().equals(id)).toList().get(0));
                nastavciListView.getItems().addAll(showsSequence);
                showNastavciComboBox.getItems().removeAll(showsSequence);
            }
            sequelSearch();
        }
    }
    @FXML
    private void removeSequel(){
        if(nastavciListView.getSelectionModel().getSelectedItem() != null && !nastavciListView.getSelectionModel().getSelectedItem().getId().equals(showComboBox.getValue().getId())) {
            if(nastavciListView.getSelectionModel().getSelectedItem().getIdSeqience().contains(showComboBox.getValue().getId())){
                showNastavciComboBox.getItems().add(nastavciListView.getSelectionModel().getSelectedItem());
                nastavciListView.getItems().remove(nastavciListView.getSelectionModel().getSelectedItem());
            }
            else {
                ArrayList<Show> showsSequence = new ArrayList<>();
                for (Long id : nastavciListView.getSelectionModel().getSelectedItem().getIdSeqience())
                    showsSequence.add(shows.stream().filter(show -> show.getId().equals(id)).toList().get(0));
                nastavciListView.getItems().removeAll(showsSequence);
                showNastavciComboBox.getItems().addAll(showsSequence);
            }
            sequelSearch();
        }
    }
    @FXML
    private void moveSequelUp() {
        if (nastavciListView.getSelectionModel().getSelectedItem() != null && nastavciListView.getSelectionModel().getSelectedIndex() > 0) {
            Show temp = nastavciListView.getSelectionModel().getSelectedItem();
            nastavciListView.getItems().set(nastavciListView.getSelectionModel().getSelectedIndex(), nastavciListView.getItems().get(nastavciListView.getSelectionModel().getSelectedIndex() - 1));
            nastavciListView.getItems().set(nastavciListView.getSelectionModel().getSelectedIndex() - 1, temp);
            nastavciListView.getSelectionModel().clearAndSelect(nastavciListView.getSelectionModel().getSelectedIndex() - 1);
        }
    }
    @FXML
    private void moveSequelDown(){
        if (nastavciListView.getSelectionModel().getSelectedItem() != null && nastavciListView.getSelectionModel().getSelectedIndex() < (nastavciListView.getItems().size() - 1)) {
            Show temp = nastavciListView.getSelectionModel().getSelectedItem();
            nastavciListView.getItems().set(nastavciListView.getSelectionModel().getSelectedIndex(), nastavciListView.getItems().get(nastavciListView.getSelectionModel().getSelectedIndex() + 1));
            nastavciListView.getItems().set(nastavciListView.getSelectionModel().getSelectedIndex() + 1, temp);
            nastavciListView.getSelectionModel().clearAndSelect(nastavciListView.getSelectionModel().getSelectedIndex() + 1);
        }
    }
    @FXML
    private void delete(){
        if(showComboBox.getValue() != null && !showComboBox.getValue().getId().equals(-1l)){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Brisanje");
            alert.setHeaderText("Želite li obrisati show?");
            ButtonType daButton = new ButtonType("Da", ButtonBar.ButtonData.YES);
            ButtonType neButton = new ButtonType("Ne", ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(daButton, neButton);
            alert.showAndWait().ifPresent(response -> {
                if(response == daButton) {
                    DataBase.deleteShow(showComboBox.getValue());
                    refresh();
                }
            });
        }
    }
    @FXML
    private void save() {
        if (!showComboBox.getSelectionModel().isEmpty()) {
            String orginalniNaziv = orginalniNazivTextField.getText();
            String prevedeniNaziv = prevedeniNazivTextField.getText();
            String slika = lokacijaSlikeLabel.getText();
            String studio = studioTextField.getText();
            List<Long> sequels = nastavciListView.getItems().stream().mapToLong(Show::getId).boxed().collect(Collectors.toList());
            String opis = opisTextArea.getText();
            Set<Genre> genres = new HashSet<>(zanroviListView.getSelectionModel().getSelectedItems());
            String tip = tipComboBox.getValue();
            LocalDate pocetak = pocetakDatePicker.getValue();
            LocalDate kraj = krajDatePicker.getValue();
            String brojEpizoda = brojEpizodaTextField.getText();
            List<String> greske = new ArrayList<>();

            if (orginalniNaziv.isEmpty())
                greske.add("orginalni naziv");
            if (prevedeniNaziv.isEmpty())
                greske.add("prevedeni naziv");
            if (slika.isEmpty())
                greske.add("slika");
            if (studio.isEmpty())
                greske.add("studio");
            if (sequels.isEmpty())
                greske.add("nastavci");
            if (opis.isEmpty())
                greske.add("opis");
            if (genres.isEmpty())
                greske.add("žanrovi");
            if (pocetak == null)
                greske.add("pocetak");

            if (tip.equals("Serija")) {
                if (kraj == null)
                    greske.add("kraj");
                if (brojEpizoda.isEmpty() || !brojEpizoda.matches("[0-9]+"))
                    greske.add("broj epizoda");

                if (greske.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Spremanje");
                    alert.setHeaderText("Želite li spremiti show?");
                    ButtonType daButton = new ButtonType("Da", ButtonBar.ButtonData.YES);
                    ButtonType neButton = new ButtonType("Ne", ButtonBar.ButtonData.NO);
                    alert.getButtonTypes().setAll(daButton, neButton);
                    alert.showAndWait().ifPresent(response -> {
                        if (response == daButton) {
                            if (showComboBox.getValue().getId().equals(-1l)) {
                                DataBase.addShow(new Series(
                                        -1l,
                                        orginalniNaziv,
                                        prevedeniNaziv,
                                        opis,
                                        slika,
                                        studio,
                                        genres,
                                        sequels,
                                        new StartEndDate(
                                                pocetak,
                                                kraj),
                                        Integer.parseInt(brojEpizoda)
                                ));
                            } else DataBase.updateShow(new Series(
                                    showComboBox.getValue().getId(),
                                    orginalniNaziv,
                                    prevedeniNaziv,
                                    opis,
                                    slika,
                                    studio,
                                    genres,
                                    sequels,
                                    new StartEndDate(
                                            pocetak,
                                            kraj),
                                    Integer.parseInt(brojEpizoda)
                            ));
                            refresh();
                        }
                    });
                } else
                    Main.pogresanUnosPodataka(greske);
            } else {
                if (greske.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Spremanje");
                    alert.setHeaderText("Želite li spremiti show?");
                    ButtonType daButton = new ButtonType("Da", ButtonBar.ButtonData.YES);
                    ButtonType neButton = new ButtonType("Ne", ButtonBar.ButtonData.NO);
                    alert.getButtonTypes().setAll(daButton, neButton);
                    alert.showAndWait().ifPresent(response -> {
                        if (response == daButton) {
                            if (showComboBox.getValue().getId().equals(-1l)) {
                                DataBase.addShow(new Movie(
                                        -1l,
                                        orginalniNaziv,
                                        prevedeniNaziv,
                                        opis,
                                        slika,
                                        studio,
                                        genres,
                                        sequels,
                                        pocetak
                                ));
                            } else {
                                DataBase.updateShow(new Movie(
                                        showComboBox.getValue().getId(),
                                        orginalniNaziv,
                                        prevedeniNaziv,
                                        opis,
                                        slika,
                                        studio,
                                        genres,
                                        sequels,
                                        pocetak
                                ));
                            }
                            refresh();
                        }
                    });
                } else
                    Main.pogresanUnosPodataka(greske);
            }
        }
    }
}
