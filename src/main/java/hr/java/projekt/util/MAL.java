package hr.java.projekt.util;

import dev.katsute.mal4j.MyAnimeList;
import dev.katsute.mal4j.anime.Anime;
import dev.katsute.mal4j.anime.property.AnimeType;
import hr.java.projekt.entitet.*;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

public class MAL {
    private static final String MAL_API_FILE = "mal.properties";

    public static List<Show> getAnime(String search){
        try {
            Properties malProperties = new Properties();
            malProperties.load(new FileReader(MAL_API_FILE));

            MyAnimeList mal = MyAnimeList.withClientID(malProperties.getProperty("key"));

            List<Anime> anime =mal.getAnime()
                    .withQuery(search)
                    .withLimit(10)
                    .includeNSFW(false)
                    .search();

            return anime.stream().map(a -> animeToShow(a)).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Show animeToShow(Anime anime){
        Long id = -1l;
        String orginalniNaslov = anime.getTitle();
        String prevedeniNaslov = anime.getAlternativeTitles().getEnglish();
        String opis = anime.getSynopsis().replaceAll("\\\\n", "\n");
        String slika = anime.getMainPicture().getLargeURL();
        String studio = anime.getStudios()[0].getName();
        List<String> animeGenres = Arrays.stream(anime.getGenres()).map(g -> g.getName().toUpperCase().replaceAll(" ", "_")).toList();
        List<String> genreNames = Arrays.stream(Genre.values()).map(Genre::toString).toList();
        Set<Genre> genres = new HashSet<>();
        for(String animeGenre: animeGenres)
            if(genreNames.contains(animeGenre))
                genres.add(Genre.valueOf(animeGenre));

        List<Long> idSequence = new ArrayList<>(1);
        idSequence.add(id);

        if(anime.getEpisodes() > 1){
            LocalDate startDate = anime.getStartDate().getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate endDate = anime.getEndDate().getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            Integer brojEpizoda = anime.getEpisodes();

            return new Series(
                    id,
                    orginalniNaslov,
                    prevedeniNaslov,
                    opis,
                    slika,
                    studio,
                    genres,
                    idSequence,
                    new StartEndDate(
                            startDate,
                            endDate
                    ),
                    brojEpizoda
            );
        }
        else {
            LocalDate relaseDate = anime.getStartDate().getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            return new Movie(
                    id,
                    orginalniNaslov,
                    prevedeniNaslov,
                    opis,
                    slika,
                    studio,
                    genres,
                    idSequence,
                    relaseDate
            );
        }
    }
}
