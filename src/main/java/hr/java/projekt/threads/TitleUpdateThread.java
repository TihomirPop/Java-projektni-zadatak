package hr.java.projekt.threads;

import hr.java.projekt.db.DataBase;
import hr.java.projekt.entitet.Movie;
import hr.java.projekt.entitet.Movies;
import hr.java.projekt.entitet.Show;
import hr.java.projekt.entitet.Shows;
import hr.java.projekt.exceptions.BazaPodatakaException;
import hr.java.projekt.main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class TitleUpdateThread implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    @Override
    public void run() {
        try {
            List<Show> shows = DataBase.getShows();
            List<Movie> movies = new ArrayList<>();
            Show najboljiShow = Shows.odrediNajboljiShow(shows);

            for (Show show : shows)
                if (show instanceof Movie movie)
                    movies.add(movie);
            Movie najnovijiFilm = Movie.najnovijiFilm(movies);

            String title = "MSL   -   najbolji show: " + najboljiShow.getPrevedeniNaslov();
            if (najnovijiFilm.isNajnoviji(movies))
                title += "   -   najnoviji film je: " + najnovijiFilm.getPrevedeniNaslov();
            Main.setMainStageTitle(title);
        } catch (BazaPodatakaException e){
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }
}
