package hr.java.projekt.db;

import hr.java.projekt.entitet.Series;

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

    public static void addSeries(Series series){
        try(Connection connection = spajanjeNaBazu()) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO SERIES (ORGINALNI_NASLOV, PREVEDENI_NASLOV, OPIS, SLIKA, STUDIO, START_DATE, END_DATE, NUMBER_OF_EPISODES) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            preparedStatement.setString(1, series.getOrginalniNaslov());
            preparedStatement.setString(2, series.getPrevedeniNaslov());
            preparedStatement.setString(3, series.getOpis());
            preparedStatement.setString(4, series.getSlika());
            preparedStatement.setString(5, series.getStudio());
            preparedStatement.setDate(6, Date.valueOf(series.getStartEndDate().startDate()));
            preparedStatement.setDate(7, Date.valueOf(series.getStartEndDate().endDate()));
            preparedStatement.setInt(8, series.getNumberOfEpisodes());

            preparedStatement.executeUpdate();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
