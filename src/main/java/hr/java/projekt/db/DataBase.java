package hr.java.projekt.db;

import hr.java.projekt.entitet.Genre;
import hr.java.projekt.entitet.Movie;
import hr.java.projekt.entitet.Series;
import hr.java.projekt.entitet.Show;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DataBase {
    private static final String DATABASE_FILE = "database.properties";
    private static Connection spajanjeNaBazu() throws SQLException, IOException {
        Properties properties = new Properties();
        properties.load(new FileReader(DATABASE_FILE));
        String dataBaseUrl = properties.getProperty("dataBaseUrl");
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");
        return DriverManager.getConnection(dataBaseUrl, username, password);
    }

    public static void addShow(Show show){
        try(Connection connection = spajanjeNaBazu()) {
            PreparedStatement showPS = connection.prepareStatement("INSERT INTO SHOWS (ORGINALNI_NASLOV, PREVEDENI_NASLOV, OPIS, SLIKA, STUDIO) VALUES (?, ?, ?, ?, ?)");
            showPS.setString(1, show.getOrginalniNaslov());
            showPS.setString(2, show.getPrevedeniNaslov());
            showPS.setString(3, show.getOpis());
            showPS.setString(4, show.getSlika());
            showPS.setString(5, show.getStudio());
            showPS.executeUpdate();

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM SHOWS ORDER BY ID DESC LIMIT 0, 1");
            rs.next();
            show.setId(rs.getLong("ID"));
            for(int i = 0; i < show.getIdSeqience().size(); i++)
                if(show.getIdSeqience().get(i).equals(-1l))
                    show.getIdSeqience().set(i, show.getId());

            connection.setAutoCommit(false);
            PreparedStatement showGeneresPS = connection.prepareStatement("INSERT INTO SHOWS_GENRES (ID, GENRE) VALUES (?, ?)");
            for(Genre genre: show.getGenres()){
                showGeneresPS.setLong(1, show.getId());
                showGeneresPS.setString(2, genre.toString());
                showGeneresPS.executeUpdate();
            }
            connection.commit();
            connection.setAutoCommit(true);

            if(show instanceof Series series){
                PreparedStatement seriesPS = connection.prepareStatement("INSERT INTO SERIES (ID, START_DATE, END_DATE, NUMBER_OF_EPISODES ) VALUES (?, ?, ?, ?)");
                seriesPS.setLong(1, series.getId());
                seriesPS.setDate(2, Date.valueOf(series.getStartEndDate().startDate()));
                seriesPS.setDate(3, Date.valueOf(series.getStartEndDate().endDate()));
                seriesPS.setInt(4, series.getNumberOfEpisodes());
                seriesPS.executeUpdate();
            }
            else if(show instanceof Movie movie){
                PreparedStatement moviePS = connection.prepareStatement("INSERT INTO MOVIES (ID, RELEASE_DATE) VALUES (?, ?)");
                moviePS.setLong(1, movie.getId());
                moviePS.setDate(2, Date.valueOf(movie.getReleaseDate()));
                moviePS.executeUpdate();
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

        updateSequals(show);
    }

    private static void updateSequals(Show show){
        try(Connection connection = spajanjeNaBazu()) {
            connection.setAutoCommit(false);
            PreparedStatement deletePS = connection.prepareStatement("DELETE FROM SEQUELS  WHERE SHOW_ID = ?");
            for(Long id: show.getIdSeqience()){
                deletePS.setLong(1, id);
                deletePS.executeUpdate();
            }
            connection.commit();

            PreparedStatement sequalPS = connection.prepareStatement("INSERT INTO SEQUELS  (SHOW_ID, SEQUEL_ID) VALUES (?, ?)");
            for(int i = 0; i < (show.getIdSeqience().size() - 1); i++){
                sequalPS.setLong(1, show.getIdSeqience().get(i));
                sequalPS.setLong(2, show.getIdSeqience().get(i + 1));
                sequalPS.executeUpdate();
            }
            connection.commit();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
