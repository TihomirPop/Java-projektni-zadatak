package hr.java.projekt.entitet;

import javafx.scene.image.Image;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class Series extends Show{
    private StartEndDate startEndDate;
    private Integer numberOfEpisodes;

    public Series(Long id, String orginalniNaslov, String prevedeniNaslov, String opis, String slika, String studio, Set<Genre> genres, List<Long> idSeqience, StartEndDate startEndDate, Integer numberOfEpisodes) {
        super(id, orginalniNaslov, prevedeniNaslov, opis, slika, studio, genres, idSeqience);
        this.startEndDate = startEndDate;
        this.numberOfEpisodes = numberOfEpisodes;
    }

    public StartEndDate getStartEndDate() {
        return startEndDate;
    }

    public void setStartEndDate(StartEndDate startEndDate) {
        this.startEndDate = startEndDate;
    }

    public Integer getNumberOfEpisodes() {
        return numberOfEpisodes;
    }

    public void setNumberOfEpisodes(Integer numberOfEpisodes) {
        this.numberOfEpisodes = numberOfEpisodes;
    }
}
