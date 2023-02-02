package hr.java.projekt.main;

import hr.java.projekt.util.DataBase;
import hr.java.projekt.entitet.*;
import hr.java.projekt.exceptions.BazaPodatakaException;
import hr.java.projekt.exceptions.KriviInputException;
import hr.java.projekt.exceptions.PromjeneException;
import hr.java.projekt.util.Promjene;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class EditShowsController {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

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
        try {
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
        } catch (BazaPodatakaException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
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
                    try {
                        DataBase.deleteShow(showComboBox.getValue());
                        Promjene.addPromjena(new Promjena(
                                null,
                                "Obriši show",
                                showComboBox.getValue().getOrginalniNaslov(),
                                "OBRISANO",
                                Main.currentUser.getRole(),
                                LocalDateTime.now()
                        ));
                        refresh();
                    } catch (BazaPodatakaException | PromjeneException e) {
                        logger.error(e.getMessage(), e);
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @FXML
    private void save() {
        try {
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
                            try {
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
                                        Promjene.addPromjena(new Promjena(
                                                null,
                                                "Dodaj seriju",
                                                "NE POSTOJI",
                                                orginalniNaziv,
                                                Main.currentUser.getRole(),
                                                LocalDateTime.now()
                                        ));
                                    } else {
                                        Show show = showComboBox.getValue();
                                        DataBase.updateShow(new Series(
                                                show.getId(),
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

                                        updateShowPromjene(show);
                                    }
                                    refresh();
                                }
                            } catch (BazaPodatakaException | PromjeneException e) {
                                logger.error(e.getMessage(), e);
                                e.printStackTrace();
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
                            try {
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
                                        Promjene.addPromjena(new Promjena(
                                                null,
                                                "Dodaj film",
                                                "NE POSTOJI",
                                                orginalniNaziv,
                                                Main.currentUser.getRole(),
                                                LocalDateTime.now()
                                        ));
                                    } else {
                                        Show show = showComboBox.getValue();
                                        DataBase.updateShow(new Movie(
                                                show.getId(),
                                                orginalniNaziv,
                                                prevedeniNaziv,
                                                opis,
                                                slika,
                                                studio,
                                                genres,
                                                sequels,
                                                pocetak
                                        ));

                                        updateShowPromjene(show);
                                    }
                                    refresh();
                                }
                            } catch (BazaPodatakaException | PromjeneException e) {
                                logger.error(e.getMessage(), e);
                                e.printStackTrace();
                            }
                        });
                    } else
                        Main.pogresanUnosPodataka(greske);
                }
            }
        } catch (KriviInputException e){
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }

    private void updateShowPromjene(Show show) throws PromjeneException{
        List<String> podatak = new ArrayList<>();
        List<String> staraVrijednost = new ArrayList<>();
        List<String> novaVrijednost = new ArrayList<>();

        if(!orginalniNazivTextField.getText().equals(show.getOrginalniNaslov())){
            podatak.add("Orginalni naslov od " + show.getOrginalniNaslov());
            staraVrijednost.add(show.getOrginalniNaslov());
            novaVrijednost.add(orginalniNazivTextField.getText());
        }

        if(!prevedeniNazivTextField.getText().equals(show.getPrevedeniNaslov())){
            podatak.add("Prevedeni naslov od " + show.getOrginalniNaslov());
            staraVrijednost.add(show.getOrginalniNaslov());
            novaVrijednost.add(prevedeniNazivTextField.getText());
        }

        if(!opisTextArea.getText().equals(show.getOpis())){
            podatak.add("Opis od " + show.getOrginalniNaslov());
            staraVrijednost.add(show.getOpis());
            novaVrijednost.add(opisTextArea.getText());
        }

        if(!lokacijaSlikeLabel.getText().equals(show.getSlika())){
            podatak.add("Slika od " + show.getOrginalniNaslov());
            staraVrijednost.add(show.getSlika());
            novaVrijednost.add(lokacijaSlikeLabel.getText());
        }

        if(!studioTextField.getText().equals(show.getStudio())){
            podatak.add("Studio od " + show.getOrginalniNaslov());
            staraVrijednost.add(show.getStudio());
            novaVrijednost.add(studioTextField.getText());
        }

        Set<Genre> zanrovi = new HashSet<>(zanroviListView.getSelectionModel().getSelectedItems());
        if(!(zanrovi.equals(show.getGenres()))){
            podatak.add("Žanrovi od " + show.getOrginalniNaslov());
            staraVrijednost.add(show.getGenres().toString());
            novaVrijednost.add(zanrovi.toString());
        }

        List<Long> nastavci = nastavciListView.getItems().stream().mapToLong(Show::getId).boxed().toList();
        if(!nastavci.equals(show.getIdSeqience())){
            podatak.add("Id nastavaka od " + show.getOrginalniNaslov());
            staraVrijednost.add(show.getIdSeqience().toString());
            novaVrijednost.add(nastavci.toString());
        }

        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.YYYY.");
        if(show instanceof Series series){
            if(tipComboBox.getValue().equals("Serija")) {
                if (!pocetakDatePicker.getValue().isEqual(series.getStartEndDate().startDate())) {
                    podatak.add("Pocetak od " + show.getOrginalniNaslov());
                    staraVrijednost.add(series.getStartEndDate().startDate().format(format));
                    novaVrijednost.add(pocetakDatePicker.getValue().format(format));
                }

                if (!krajDatePicker.getValue().isEqual(series.getStartEndDate().endDate())) {
                    podatak.add("Kraj od " + show.getOrginalniNaslov());
                    staraVrijednost.add(series.getStartEndDate().endDate().format(format));
                    novaVrijednost.add(krajDatePicker.getValue().format(format));
                }

                Integer brojEpizoda = Integer.parseInt(brojEpizodaTextField.getText());
                if (!brojEpizoda.equals(series.getNumberOfEpisodes())) {
                    podatak.add("Broj epizoda od " + show.getOrginalniNaslov());
                    staraVrijednost.add(series.getNumberOfEpisodes().toString());
                    novaVrijednost.add(brojEpizoda.toString());
                }
            }
            else {
                if (!pocetakDatePicker.getValue().isEqual(series.getStartEndDate().startDate())) {
                    podatak.add("Pocetak od " + show.getOrginalniNaslov());
                    staraVrijednost.add(series.getStartEndDate().startDate().format(format));
                    novaVrijednost.add(pocetakDatePicker.getValue().format(format));
                }

                podatak.add("Serija u film - " + show.getOrginalniNaslov());
                staraVrijednost.add("Serija");
                novaVrijednost.add("Film");
            }
        }
        else if(show instanceof Movie movie){
            if(tipComboBox.getValue().equals("Film")) {
                if (!pocetakDatePicker.getValue().isEqual(movie.getReleaseDate())) {
                    podatak.add("Pocetak od " + show.getOrginalniNaslov());
                    staraVrijednost.add(movie.getReleaseDate().format(format));
                    novaVrijednost.add(pocetakDatePicker.getValue().format(format));
                }
            }
            else {
                if (!pocetakDatePicker.getValue().isEqual(movie.getReleaseDate())) {
                    podatak.add("Pocetak od " + show.getOrginalniNaslov());
                    staraVrijednost.add(movie.getReleaseDate().format(format));
                    novaVrijednost.add(pocetakDatePicker.getValue().format(format));
                }

                podatak.add("Kraj od " + show.getOrginalniNaslov());
                staraVrijednost.add("NE POSTOJI");
                novaVrijednost.add(krajDatePicker.getValue().format(format));

                podatak.add("Broj epizoda od " + show.getOrginalniNaslov());
                staraVrijednost.add("NE POSTOJI");
                novaVrijednost.add(brojEpizodaTextField.getText());

                podatak.add("Film u seriju - " + show.getOrginalniNaslov());
                staraVrijednost.add("Film");
                novaVrijednost.add("Serija");
            }
        }

        List<Promjena> promjene = new ArrayList<>(podatak.size());
        for(int i = 0; i < podatak.size(); i++){
            promjene.add(new Promjena(
                    null,
                    podatak.get(i),
                    staraVrijednost.get(i),
                    novaVrijednost.get(i),
                    Main.currentUser.getRole(),
                    LocalDateTime.now()
            ));
        }
        Promjene.addPromjene(promjene);
    }
}
