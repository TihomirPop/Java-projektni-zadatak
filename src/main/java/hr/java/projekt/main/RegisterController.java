package hr.java.projekt.main;

import hr.java.projekt.entitet.User;
import hr.java.projekt.util.Datoteke;
import hr.java.projekt.util.Hash;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalLong;

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
        String email = emailTextField.getText();
        String username = usernameTextField.getText();
        String password = passwordField.getText();
        String passwordConfirm = passwordConfirmField.getText();
        List<User> users = Datoteke.getUsers();
        List<String> greske = new ArrayList<>();

        if(email.isEmpty())
            greske.add("email");
        if(username.isEmpty())
            greske.add("username");
        if(password.isEmpty())
            greske.add("password");
        if(passwordConfirm.isEmpty())
            greske.add("confirm password");

        if(!greske.isEmpty()) {
            Main.pogresanUnosPodataka(greske);
            return;
        }
        List<User> sameUser = users.stream().filter(u -> (u.getUsername().equals(username)) || (u.getEmail().equals(email))).toList();
        if(!sameUser.isEmpty()) {
            logger.warn("To korisnicko ime ili lozinka se vec koristi");
            System.out.println("To korisnicko ime ili lozinka se vec koristi");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Pogrešan unos podataka");
            alert.setHeaderText("Korisnicko ime ili lozinka se vec koristi");
            alert.showAndWait();
            return;
        }
        if(password.length() < 8) {
            logger.warn("Duzina lozinke mora biti barem 8");
            System.out.println("Duzina lozinke mora biti barem 8");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Pogrešan unos podataka");
            alert.setHeaderText("Duzina lozinke mora biti barem 8");
            alert.showAndWait();
            return;
        }
        if (!password.equals(passwordConfirm)) {
            logger.warn("Passwords se moraju podudarati");
            System.out.println("Passwords se moraju podudarati");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Pogrešan unos podataka");
            alert.setHeaderText("Lozinke se moraju podudarati");
            alert.showAndWait();
            return;
        }
        try (BufferedWriter out = new BufferedWriter(new FileWriter(Datoteke.USERS_PATH, true))) {
            OptionalLong optionalId = users.stream().mapToLong(p -> p.getId()).max();
            Long id = optionalId.getAsLong() + 1;
            out.write('\n' + id.toString());
            out.write('\n' + email);
            out.write('\n' + username);
            out.write('\n' + Hash.hash(password).toString());
            out.write("\n1");
            Main.prikaziScene(new FXMLLoader(Main.class.getResource("login.fxml")));
        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void backToLogin(){
        Main.prikaziScene(new FXMLLoader(Main.class.getResource("login.fxml")));
    }
}
