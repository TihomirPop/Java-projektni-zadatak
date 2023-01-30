package hr.java.projekt.main;

import hr.java.projekt.db.DataBase;
import hr.java.projekt.entitet.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
    private List<Show> showsWithNewShow = new ArrayList<>();

    public void initialize() {
        shows = DataBase.getShows();
        showsWithNewShow.add(new Series(-1l, "<novi show>", "<novi show>", null, null, null, null, new ArrayList<>(1), null, null));
        showsWithNewShow.get(0).getIdSeqience().add(-1l);
        showsWithNewShow.addAll(shows);
        showComboBox.setItems(FXCollections.observableArrayList(showsWithNewShow));
        showComboBox.getSelectionModel().selectFirst();
        fileChooser.setTitle("Odabir slike");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPG/PNG", "*.jpg", "*.png"));
        showNastavciComboBox.setItems(FXCollections.observableArrayList(shows));
        nastavciListView.setItems(FXCollections.observableArrayList(showsWithNewShow.stream().filter(show -> show.getId().equals(-1l)).toList()));
        ArrayList<String> tipovi = new ArrayList<>(2);
        tipovi.add("Serija");
        tipovi.add("Film");
        tipComboBox.setItems(FXCollections.observableArrayList(tipovi));
        tipComboBox.getSelectionModel().selectFirst();
        zanroviListView.setItems(FXCollections.observableArrayList(List.of(Genre.values())));
        zanroviListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    @FXML
    private void edit(){
        Show show = showComboBox.getValue();
        if(!show.getId().equals(-1l)){
            orginalniNazivTextField.setText(show.getOrginalniNaslov());
            prevedeniNazivTextField.setText(show.getPrevedeniNaslov());
            lokacijaSlikeLabel.setText(show.getSlika());
            studioTextField.setText(show.getStudio());
            showNastavciComboBox.setItems(FXCollections.observableArrayList(shows.stream().filter(s -> !show.getIdSeqience().contains(s.getId())).toList()));
            nastavciListView.setItems(FXCollections.observableArrayList(shows.stream().filter(s -> show.getIdSeqience().contains(s.getId())).collect(Collectors.toList())));
            opisTextArea.setText(show.getOpis());
            int prviZanar = show.getGenres().get(0).ordinal();
            int ostaliZanrovi[] = show.getGenres().stream().skip(1).mapToInt(Enum::ordinal).toArray();
            zanroviListView.getSelectionModel().selectIndices(prviZanar, ostaliZanrovi);
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

        if(search.equals(""))
            showNastavciComboBox.setItems(FXCollections.observableArrayList(shows.stream().filter(s -> !showComboBox.getValue().getIdSeqience().contains(s.getId())).toList()));
        else
            showNastavciComboBox.setItems(FXCollections.observableArrayList(shows.stream().filter(s -> (!showComboBox.getValue().getIdSeqience().contains(s.getId())) && (s.getOrginalniNaslov().toLowerCase().contains(search.toLowerCase()) || s.getPrevedeniNaslov().toLowerCase().contains(search.toLowerCase()))).toList()));
    }
    @FXML
    private void addSequel(){
        if(!showNastavciComboBox.getSelectionModel().isEmpty()){
            nastavciListView.getItems().add(showNastavciComboBox.getValue());
        }
    }
    @FXML
    private void save(){
        if(!showComboBox.getSelectionModel().isEmpty()) {
            String orginalniNaziv = orginalniNazivTextField.getText();
            String prevedeniNaziv = prevedeniNazivTextField.getText();
            String slika = lokacijaSlikeLabel.getText();
            String studio = studioTextField.getText();
            List<Long> sequels = nastavciListView.getItems().stream().mapToLong(Show::getId).boxed().collect(Collectors.toList());
            String opis = opisTextArea.getText();
            List<Genre> genres = zanroviListView.getSelectionModel().getSelectedItems();
            String tip = tipComboBox.getValue();
            LocalDate pocetak = pocetakDatePicker.getValue();
            LocalDate kraj = krajDatePicker.getValue();
            String brojEpizoda = brojEpizodaTextField.getText();
            List<String> greske = new ArrayList<>();

            if(orginalniNaziv.isEmpty())
                greske.add("orginalni naziv");
            if(prevedeniNaziv.isEmpty())
                greske.add("prevedeni naziv");
            if(slika.isEmpty())
                greske.add("slika");
            if(studio.isEmpty())
                greske.add("studio");
            if(sequels.isEmpty())
                greske.add("nastavci");
            if(opis.isEmpty())
                greske.add("opis");
            if(genres.isEmpty())
                greske.add("žanrovi");
            if(pocetak == null)
                greske.add("pocetak");

            if(tip.equals("Serija")){
                if(kraj == null)
                    greske.add("kraj");
                if(brojEpizoda.isEmpty() || !brojEpizoda.matches("[0-9]+"))
                    greske.add("broj epizoda");

                if(greske.isEmpty()){
                    if(showComboBox.getValue().getId().equals(-1l)) {
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
                    }
                    else DataBase.updateShow(new Series(
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
                }
                else
                    Main.pogresanUnosPodataka(greske);
            }
            else {
                if (greske.isEmpty()) {
                    if(showComboBox.getValue().getId().equals(-1l)) {
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
                    }
                    else {
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
                }
                else
                    Main.pogresanUnosPodataka(greske);
            }
        }
    }
}
