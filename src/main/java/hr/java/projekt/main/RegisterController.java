package hr.java.projekt.main;

import hr.java.projekt.entitet.Promjena;
import hr.java.projekt.entitet.User;
import hr.java.projekt.exceptions.DatotekaException;
import hr.java.projekt.exceptions.KriviInputException;
import hr.java.projekt.threads.AddPromjenaThread;
import hr.java.projekt.util.Datoteke;
import hr.java.projekt.util.Hash;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RegisterController {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField passwordConfirmField;

    @FXML
    public void register(){
        try {
            String email = emailTextField.getText();
            String username = usernameTextField.getText();
            String password = passwordField.getText();
            String passwordConfirm = passwordConfirmField.getText();
            List<User> users = Datoteke.getUsers();
            List<String> greske = new ArrayList<>();

            if (email.isEmpty())
                greske.add("email");
            if (username.isEmpty())
                greske.add("username");
            if (password.isEmpty())
                greske.add("password");
            if (passwordConfirm.isEmpty())
                greske.add("confirm password");

            if (!greske.isEmpty()) {
                Main.pogresanUnosPodataka(greske);
                return;
            }
            List<User> sameUser = users.stream().filter(u -> (u.getUsername().equals(username)) || (u.getEmail().equals(email))).toList();
            if (!sameUser.isEmpty()) {
                logger.warn("To korisnicko ime ili email se vec koristi", new KriviInputException("To korisnicko ime ili email se vec koristi"));
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Pogre??an unos podataka");
                alert.setHeaderText("Korisnicko ime ili email se vec koristi");
                alert.showAndWait();
                return;
            }
            if (password.length() < 6) {
                logger.warn("Duzina lozinke mora biti barem 6", new KriviInputException("Duzina lozinke mora biti barem 6"));
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Pogre??an unos podataka");
                alert.setHeaderText("Duzina lozinke mora biti barem 6");
                alert.showAndWait();
                return;
            }
            if (!password.equals(passwordConfirm)) {
                logger.warn("Lozinke se moraju podudarati", new KriviInputException("Lozinke se moraju podudarati"));
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Pogre??an unos podataka");
                alert.setHeaderText("Lozinke se moraju podudarati");
                alert.showAndWait();
                return;
            }

            byte[] salt = Hash.generateRandomSalt();
            Datoteke.addUser(new User(null, email, username, Hash.hash(password, salt), Hash.bytesToHex(salt), 1, false));

            new Thread( new AddPromjenaThread(new Promjena(
                    null,
                    "Dodaj korisnika",
                    "NE POSTOJI",
                    username,
                    1,
                    LocalDateTime.now()
            ))).start();

        } catch (DatotekaException e){
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        } catch (KriviInputException e){
            logger.warn(e.getMessage(), e);
        }
    }

    @FXML
    public void backToLogin(){
        Main.prikaziScene(new FXMLLoader(Main.class.getResource("login.fxml")));
    }
}
