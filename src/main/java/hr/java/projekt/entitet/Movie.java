package hr.java.projekt.entitet;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public final class Movie extends Show implements Movies{
    private LocalDate releaseDate;

    public Movie(Long id, String orginalniNaslov, String prevedeniNaslov, String opis, String slika, String studio, Set<Genre> genres, List<Long> idSeqience, LocalDate releaseDate) {
        super(id, orginalniNaslov, prevedeniNaslov, opis, slika, studio, genres, idSeqience);
        this.releaseDate = releaseDate;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }
    public static Movie najnovijiFilm(List<Movie> movies) {
        return movies.stream().max(Comparator.comparing(Movie::getReleaseDate)).get();
    }
    @Override
    public Boolean isNajnoviji(List<Movie> movies) {
        return releaseDate.isEqual(najnovijiFilm(movies).getReleaseDate());
    }
}
