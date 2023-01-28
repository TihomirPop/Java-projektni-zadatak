package hr.java.projekt.main;

import hr.java.projekt.entitet.User;
import hr.java.projekt.util.EmailVerification;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class Main extends Application {
    static Stage mainStage;
    static User currentUser = null;
    static Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), screenSize.getWidth(), screenSize.getHeight());
        scene.getStylesheets().add("style.css");
        stage.setTitle("MML");
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.show();
    }

    public static void prikaziScene(FXMLLoader fxmlLoader){
        try {
            Scene scene = new Scene(fxmlLoader.load(), screenSize.getWidth(), screenSize.getHeight());
            scene.getStylesheets().add("style.css");
            mainStage.setMaximized(true);
            mainStage.setScene(scene);
            mainStage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void pogresanUnosPodataka(List<String> podaci){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Pogrešan unos podataka");
        alert.setHeaderText("Molimo ispravite sljedeće pogreške:");

        String greska = podaci.get(0);
        for(int i = 1; i < podaci.size(); i++)
            greska += ", " + podaci.get(i);
        greska = greska.substring(0, 1).toUpperCase() + greska.substring(1);
        if(podaci.size() == 1)
            alert.setContentText(greska + " je obvezan podatak!");
        else
            alert.setContentText(greska + " su obavezni podaci!");

        alert.showAndWait();
    }

    public static void main(String[] args) {
        /*try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("dat/shows/steinsGate"))){
            List<Genre> genres = new ArrayList<>();
            genres.add(Genre.DRAMA);
            genres.add(Genre.SCI_FI);
            genres.add(Genre.SUSPENSE);

            List<Long> idSequence = new ArrayList<>();
            idSequence.add(1l);

            Show show = new Series(
                    1l,
                    "Steins;Gate",
                    "Steins;Gate",
                    "Eccentric scientist Rintarou Okabe has a never-ending thirst for scientific exploration. Together with his ditzy but well-meaning friend Mayuri Shiina and his roommate Itaru Hashida, Rintarou founds the Future Gadget Laboratory in the hopes of creating technological innovations that baffle the human psyche. Despite claims of grandeur, the only notable \"gadget\" the trio have created is a microwave that has the mystifying power to turn bananas into green goo.\n" +
                            "\n" +
                            "However, when Rintarou decides to attend neuroscientist Kurisu Makise's conference on time travel, he experiences a series of strange events that lead him to believe that there is more to the \"Phone Microwave\" gadget than meets the eye. Apparently able to send text messages into the past using the microwave, Rintarou dabbles further with the \"time machine,\" attracting the ire and attention of the mysterious organization SERN.\n" +
                            "\n" +
                            "Due to the novel discovery, Rintarou and his friends find themselves in an ever-present danger. As he works to mitigate the damage his invention has caused to the timeline, he is not only fighting a battle to save his loved ones, but also one against his degrading sanity.",
                    "dat/img/steinsGate.jpg",
                    "White Fox",
                    genres,
                    idSequence,
                    new StartEndDate(LocalDate.parse("2011-04-06"), LocalDate.parse("2011-09-14")),
                    24);
            out.writeObject(show);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

        launch();}
}
