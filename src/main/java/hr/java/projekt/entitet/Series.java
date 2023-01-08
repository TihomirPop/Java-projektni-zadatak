package hr.java.projekt.entitet;

import javafx.scene.image.Image;

import java.time.LocalDate;
import java.util.List;

public class Series extends Show{
    private StartEndDate startEndDate;

    public Series(Long id, String orginalniNaslov, String prevedeniNaslov, String opis, Image slika, String studio, List<String> genres, List<Show> sequence, List<Long> idSeqience, StartEndDate startEndDate) {
        super(id, orginalniNaslov, prevedeniNaslov, opis, slika, studio, genres, sequence, idSeqience);
        this.startEndDate = startEndDate;
    }

    public StartEndDate getStartEndDate() {
        return startEndDate;
    }

    public void setStartEndDate(StartEndDate startEndDate) {
        this.startEndDate = startEndDate;
    }
}
