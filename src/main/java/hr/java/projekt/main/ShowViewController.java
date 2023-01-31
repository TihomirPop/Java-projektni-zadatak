package hr.java.projekt.main;

import hr.java.projekt.entitet.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ShowViewController {
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
    private Button dodajMakniButton;

    private Show show;
    @FXML
    private void initialize(){
        Platform.runLater( () -> focus());
        show = Main.currentShow;

        imageView.setImage(new Image(Path.of(show.getSlika()).toAbsolutePath().toString(), 0, 1024, true, true));
        imageView.setPreserveRatio(true);

        orginalniNaslov.setText(show.getOrginalniNaslov());
        prevedeniNaslov.setText(show.getPrevedeniNaslov());
        opis.setText(show.getOpis());
        studio.setText(studio.getText() + " " + show.getStudio());

        List<Genre> genres = show.getGenres().stream().toList();
        String string = zanrovi.getText() + " " + genres.get(0).toString().substring(0,1) + genres.get(0).toString().substring(1).toLowerCase();
        for(int i = 1; i < genres.size(); i++)
            string += ", " + genres.get(i).toString().substring(0,1) + genres.get(i).toString().substring(1).toLowerCase();
        zanrovi.setText(string.replaceAll("_", " "));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.YYYY.");
        if(show instanceof Series series) {
            tip.setText(tip.getText() + " Serija");
            epizode.setText(epizode.getText() + " " + series.getNumberOfEpisodes().toString());
            pocetak.setText(pocetak.getText() + " " + series.getStartEndDate().startDate().format(formatter));
            kraj.setText(kraj.getText() + " " + series.getStartEndDate().endDate().format(formatter));
        }
        else if(show instanceof Movie movie) {
            tip.setText(tip.getText() + " Film");
            pocetak.setText(pocetak.getText() + " " + movie.getReleaseDate().format(formatter));
            epizode.setVisible(false);
            epizode.setManaged(false);
            kraj.setVisible(false);
            kraj.setManaged(false);
        }

    }

    @FXML
    private void focus(){
        imageView.requestFocus();
    }
}
