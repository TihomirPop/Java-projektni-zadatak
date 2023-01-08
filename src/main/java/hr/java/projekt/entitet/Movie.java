package hr.java.projekt.entitet;

import javafx.scene.image.Image;

import java.time.LocalDate;
import java.util.List;

public class Movie extends Show{
    private LocalDate releaseDate;

    public Movie(Long id, String orginalniNaslov, String prevedeniNaslov, String opis, Image slika, String studio, List<String> genres, List<Show> sequence, List<Long> idSeqience, LocalDate releaseDate) {
        super(id, orginalniNaslov, prevedeniNaslov, opis, slika, studio, genres, sequence, idSeqience);
        this.releaseDate = releaseDate;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }
}
