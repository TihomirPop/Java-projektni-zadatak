package hr.java.projekt.db;

import hr.java.projekt.entitet.*;
import hr.java.projekt.main.Main;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.sql.Date;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

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

    public static List<Show> getShows(){
        try(Connection connection = spajanjeNaBazu()) {
            List<Show> shows = new ArrayList<>();
            List<Long> ids = new ArrayList<>();
            List<String> orginalniNaslovi = new ArrayList<>();
            List<String> prevedeniNaslovi = new ArrayList<>();
            List<String> opisi = new ArrayList<>();
            List<String> slike = new ArrayList<>();
            List<String> studiji = new ArrayList<>();

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM SHOWS");
            while (rs.next()){
                ids.add(rs.getLong("ID"));
                orginalniNaslovi.add(rs.getString("ORGINALNI_NASLOV"));
                prevedeniNaslovi.add(rs.getString("PREVEDENI_NASLOV"));
                opisi.add(rs.getString("OPIS"));
                slike.add(rs.getString("SLIKA"));
                studiji.add(rs.getString("STUDIO"));
            }

            for(int i = 0; i < ids.size(); i++) {
                PreparedStatement seriesPS = connection.prepareStatement("SELECT * FROM SERIES WHERE ID = ?");
                seriesPS.setLong(1, ids.get(i));
                rs = seriesPS.executeQuery();
                if (rs.next()) {
                    shows.add(new Series(
                            ids.get(i),
                            orginalniNaslovi.get(i),
                            prevedeniNaslovi.get(i),
                            opisi.get(i),
                            slike.get(i),
                            studiji.get(i),
                            new HashSet<>(),
                            null,
                            new StartEndDate(
                                    rs.getDate("START_DATE").toLocalDate(),
                                    rs.getDate("END_DATE").toLocalDate()),
                            rs.getInt("NUMBER_OF_EPISODES")
                    ));
                    continue;
                }
                PreparedStatement moviePS = connection.prepareStatement("SELECT * FROM MOVIES WHERE ID = ?");
                moviePS.setLong(1, ids.get(i));
                rs = moviePS.executeQuery();
                if (rs.next()) {
                    shows.add(new Movie(
                            ids.get(i),
                            orginalniNaslovi.get(i),
                            prevedeniNaslovi.get(i),
                            opisi.get(i),
                            slike.get(i),
                            studiji.get(i),
                            new HashSet<>(),
                            new ArrayList<>(),
                            rs.getDate("RELEASE_DATE").toLocalDate()
                    ));
                }
            }

            for(Show show: shows){
                PreparedStatement genresPS = connection.prepareStatement("SELECT * FROM SHOWS_GENRES WHERE ID = ?");
                genresPS.setLong(1, show.getId());
                rs = genresPS.executeQuery();
                while (rs.next())
                    show.getGenres().add(Genre.valueOf(rs.getString("GENRE")));
            }

            Map<Long, Long> sequelMap = new HashMap<>();
            rs = connection.createStatement().executeQuery("SELECT * FROM SEQUELS");
            while (rs.next())
                sequelMap.put(rs.getLong("SHOW_ID"), rs.getLong("SEQUEL_ID"));

            for(Show show: shows){
                Long pocetak = show.getId();
                ArrayList<Long> idSequence = new ArrayList<>();
                while(sequelMap.containsValue(pocetak)) {
                    Long finalPocetak = pocetak;
                    pocetak = sequelMap.keySet().stream().filter(key -> sequelMap.get(key).equals(finalPocetak)).toList().get(0);
                }
                idSequence.add(pocetak);
                while(sequelMap.containsKey(pocetak)){
                    pocetak = sequelMap.get(pocetak);
                    idSequence.add(pocetak);
                }
                show.setIdSeqience(idSequence);
                if(show.getIdSeqience().isEmpty())
                    show.getIdSeqience().add(show.getId());
            }

            return shows;
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
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

    public static void updateShow(Show show){
        try(Connection connection = spajanjeNaBazu()) {
            PreparedStatement showPS = connection.prepareStatement("UPDATE SHOWS SET ORGINALNI_NASLOV = ?, PREVEDENI_NASLOV = ?, OPIS = ?, SLIKA = ?, STUDIO = ? WHERE ID = ?");
            showPS.setString(1, show.getOrginalniNaslov());
            showPS.setString(2, show.getPrevedeniNaslov());
            showPS.setString(3, show.getOpis());
            showPS.setString(4, show.getSlika());
            showPS.setString(5, show.getStudio());
            showPS.setLong(6, show.getId());
            showPS.executeUpdate();

            connection.createStatement().executeUpdate("DELETE FROM SHOWS_GENRES WHERE ID = " + show.getId().toString());
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
                PreparedStatement seriesPS = connection.prepareStatement("UPDATE SERIES SET START_DATE = ?, END_DATE = ?, NUMBER_OF_EPISODES = ? WHERE ID = ?");
                seriesPS.setDate(1, Date.valueOf(series.getStartEndDate().startDate()));
                seriesPS.setDate(2, Date.valueOf(series.getStartEndDate().endDate()));
                seriesPS.setInt(3, series.getNumberOfEpisodes());
                seriesPS.setLong(4, series.getId());
                seriesPS.executeUpdate();
            }
            else if(show instanceof Movie movie){
                PreparedStatement moviePS = connection.prepareStatement("UPDATE MOVIES SET RELEASE_DATE = ? WHERE ID = ?");
                moviePS.setDate(1, Date.valueOf(movie.getReleaseDate()));
                moviePS.setLong(2, movie.getId());
                moviePS.executeUpdate();
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

        updateSequals(show);
    }
    public static void deleteShow(Show show){
        try(Connection connection = spajanjeNaBazu()) {
            Files.delete(Path.of(show.getSlika()));
            connection.createStatement().executeUpdate("DELETE FROM USER_SHOWS WHERE SHOW_ID = " + show.getId().toString());
            connection.createStatement().executeUpdate("DELETE FROM SHOWS_GENRES WHERE ID = " + show.getId().toString());

            if(show instanceof Series series)
                connection.createStatement().executeUpdate("DELETE FROM SERIES WHERE ID = " + series.getId().toString());
            else if(show instanceof Movie movie)
                connection.createStatement().executeUpdate("DELETE FROM MOVIES WHERE ID = " + movie.getId().toString());

            List<Long> idSequence = new ArrayList<>();
            for(Long id: show.getIdSeqience()){
                if(id.equals(show.getId()))
                    continue;
                idSequence.add(id);
            }
            show.setIdSeqience(idSequence);
            connection.createStatement().executeUpdate("DELETE FROM SEQUELS WHERE SHOW_ID = " + show.getId().toString());
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

        updateSequals(show);

        try(Connection connection = spajanjeNaBazu()) {
            connection.createStatement().executeUpdate("DELETE FROM SHOWS WHERE ID = " + show.getId().toString());
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
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
    public static void addUserShow(UserShow userShow){
        try (Connection connection = spajanjeNaBazu()) {
            PreparedStatement userShowPS = connection.prepareStatement("INSERT INTO USER_SHOWS (USER_ID, SHOW_ID, SCORE, WATCHED) VALUES (?, ?, ?, ?)");
            userShowPS.setLong(1, userShow.getUser().getId());
            userShowPS.setLong(2, userShow.getShow().getId());
            userShowPS.setInt(3, userShow.getScore().getScore());
            userShowPS.setInt(4, userShow.getWatched());
            userShowPS.executeUpdate();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void deleteUserShow(UserShow userShow){
        try (Connection connection = spajanjeNaBazu()) {
            connection.createStatement().executeUpdate("DELETE FROM USER_SHOWS WHERE ID = " + userShow.getId().toString());
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void updateUserShow(UserShow userShow){
        try (Connection connection = spajanjeNaBazu()) {
            PreparedStatement userShowPS = connection.prepareStatement("UPDATE USER_SHOWS SET SCORE = ?, WATCHED = ? WHERE ID = ?");
            userShowPS.setInt(1, userShow.getScore().getScore());
            userShowPS.setInt(2, userShow.getWatched());
            userShowPS.setLong(3, userShow.getId());
            userShowPS.executeUpdate();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static UserShow getUserShow(User user, Show show){
        try (Connection connection = spajanjeNaBazu()) {
            PreparedStatement userShowPS = connection.prepareStatement("SELECT * FROM USER_SHOWS WHERE USER_ID = ? AND SHOW_ID = ?");
            userShowPS.setLong(1, user.getId());
            userShowPS.setLong(2, show.getId());
            ResultSet rs = userShowPS.executeQuery();
            if(rs.next()){
                UserShowBuilder userShowBuilder = new UserShowBuilder(user);
                userShowBuilder.saShow(show);
                userShowBuilder.saId(rs.getLong("ID"));
                userShowBuilder.saScore(Score.getScoreFromInt(rs.getInt("SCORE")));
                userShowBuilder.saWatched(rs.getInt("WATCHED"));
                return userShowBuilder.build();
            }
            else
                return null;
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
