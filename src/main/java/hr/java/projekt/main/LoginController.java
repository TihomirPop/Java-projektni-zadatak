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

public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordField;

    @FXML
    public void login(){
        String username = usernameTextField.getText();
        String password = passwordField.getText();
        List<User> users = Datoteke.getUsers();
        List<String> greske = new ArrayList<>();

        if(username.isEmpty())
            greske.add("username");
        if(password.isEmpty())
            greske.add("password");

        if(greske.isEmpty()){
            try {
                User user = users.stream().filter(u -> u.getUsername().equals(username)).filter(u -> u.getPassword().equals(Hash.hash(password))).toList().get(0);
                Main.currentUser = user;
                goToMainList();
            }catch (ArrayIndexOutOfBoundsException e){
                logger.warn(e.getMessage(), e);
                System.out.println(e.getMessage());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Pogrešan unos podataka");
                alert.setHeaderText("Korisnicko ime ili lozinka se ne podudaraju");
                alert.showAndWait();
            }
        }
        else
            Main.pogresanUnosPodataka(greske);
    }

    @FXML
    public void goToRegister(){
        Main.prikaziScene(new FXMLLoader(Main.class.getResource("register.fxml")));
    }

    @FXML
    public void goToMainList(){
        Main.prikaziScene(new FXMLLoader(Main.class.getResource("mainList.fxml")));
    }
}