package hr.java.projekt.main;

import hr.java.projekt.entitet.Promjena;
import hr.java.projekt.entitet.User;
import hr.java.projekt.exceptions.DatotekaException;
import hr.java.projekt.exceptions.PromjeneException;
import hr.java.projekt.threads.AddPromjeneThread;
import hr.java.projekt.threads.SendVerificationEmailThread;
import hr.java.projekt.util.Datoteke;
import hr.java.projekt.util.Hash;
import hr.java.projekt.util.Promjene;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MojProfilController {
    Logger logger = LoggerFactory.getLogger(Main.class);
    @FXML
    private VBox passwordVbox;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField newPasswordTextField;
    @FXML
    private PasswordField confirmPasswordTextField;
    @FXML
    private Button verificirajButton;
    @FXML
    private Label verifikacijaLabel;

    @FXML
    private void initialize(){
        Platform.runLater( () -> passwordVbox.requestFocus() );
        passwordVbox.setSpacing(20);
        usernameTextField.setText(Main.currentUser.getUsername());
        emailTextField.setText(Main.currentUser.getEmail());
        if(Main.currentUser.getVerified()){
            verifikacijaLabel.setText("Email je verificiran!");
            verificirajButton.setDisable(true);
        }
        else {
            verifikacijaLabel.setText("OPREZ!\nEmail nije verificiran!");
            verificirajButton.setDisable(false);
        }
    }

    @FXML
    private void save(){
        try {
            String username = usernameTextField.getText();
            String email = emailTextField.getText();
            String password = newPasswordTextField.getText();
            String confirmPassword = confirmPasswordTextField.getText();

            List<User> sameUser = Datoteke.getUsers();
            if (!username.isEmpty())
                sameUser = sameUser.stream().filter(user -> user.getUsername().equals(username)).toList();
            if (!email.isEmpty())
                sameUser = sameUser.stream().filter(user -> user.getEmail().equals(email)).toList();

            if (!sameUser.isEmpty() && (!username.equals(Main.currentUser.getUsername()) || !email.equals(Main.currentUser.getEmail()))) {
                logger.warn("To korisnicko ime ili email se vec koristi");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Pogrešan unos podataka");
                alert.setHeaderText("Korisnicko ime ili email se vec koristi");
                alert.showAndWait();
                return;
            }
            if (password.length() < 6 && !password.isEmpty()) {
                logger.warn("Duzina lozinke mora biti barem 6");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Pogrešan unos podataka");
                alert.setHeaderText("Duzina lozinke mora biti barem 6");
                alert.showAndWait();
                return;
            }
            if (!password.equals(confirmPassword) && !password.isEmpty()) {
                logger.warn("Lozinke se moraju podudarati");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Pogrešan unos podataka");
                alert.setHeaderText("Lozinke se moraju podudarati");
                alert.showAndWait();
                return;
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Spremanje");
            alert.setHeaderText("Želite li spremiti podatke?");
            ButtonType daButton = new ButtonType("Da", ButtonBar.ButtonData.YES);
            ButtonType neButton = new ButtonType("Ne", ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(daButton, neButton);
            alert.showAndWait().ifPresent(response -> {
                if (response == daButton)
                    spremiUserPodatke(username, email, password);
            });
        } catch (DatotekaException e){
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }

    private void spremiUserPodatke(String username, String email, String password) {
        try {
            List<Promjena> promjene = new ArrayList<>();
            if(!username.equals(Main.currentUser.getUsername()))
                promjene.add(new Promjena(
                        null,
                        "Korisničko ime od " + Main.currentUser.getUsername(),
                        Main.currentUser.getUsername(),
                        username,
                        Main.currentUser.getRole(),
                        LocalDateTime.now()
                ));

            if(!email.equals(Main.currentUser.getEmail())) {
                promjene.add(new Promjena(
                        null,
                        "Email od " + Main.currentUser.getUsername(),
                        Main.currentUser.getEmail(),
                        email,
                        Main.currentUser.getRole(),
                        LocalDateTime.now()
                ));

                Main.currentUser.setVerified(false);
                verifikacijaLabel.setText("OPREZ!\nEmail nije verificiran!");
                verificirajButton.setDisable(false);
            }

            if(!password.isEmpty() && !Hash.hash(password).equals(Main.currentUser.getPassword()))
                promjene.add(new Promjena(
                        null,
                        "Lozinka od " + Main.currentUser.getUsername(),
                        Main.currentUser.getPassword().toString(),
                        Hash.hash(password).toString(),
                        Main.currentUser.getRole(),
                        LocalDateTime.now()
                ));

            if(!promjene.isEmpty())
                new Thread(new AddPromjeneThread(promjene)).start();

            if (!password.isEmpty())
                Main.currentUser.setPassword(password);
            Main.currentUser.setEmail(email);
            Main.currentUser.setUsername(username);
            Datoteke.editUser(Main.currentUser);
        } catch (DatotekaException e){
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }

    @FXML
    private void verify(){
        try {
            String kod = generirajKod();
            new Thread(new SendVerificationEmailThread(Main.currentUser.getEmail(), kod)).start();
            TextInputDialog td = new TextInputDialog("******");
            td.setTitle("Verifikacija");
            td.setHeaderText("Verifikacijski kod je poslan na vašu email adresu");
            Optional<String> uneseniKod = td.showAndWait();
            if (uneseniKod.isEmpty())
                return;
            while (uneseniKod.isEmpty() || !uneseniKod.get().toUpperCase().equals(kod)) {
                if (uneseniKod.isEmpty())
                    return;
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Krivi kod");
                alert.setHeaderText("Krivi kod!\nPonovite unos.");
                alert.showAndWait();
                uneseniKod = td.showAndWait();
            }
            Main.currentUser.setVerified(true);
            Datoteke.editUser(Main.currentUser);
            verifikacijaLabel.setText("Email je verificiran!");
            verificirajButton.setDisable(true);
        } catch (DatotekaException e){
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }

    private String generirajKod(){
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder kodBuilder = new StringBuilder(6);
        for(int i = 0; i < 6; i++)
            kodBuilder.append(AlphaNumericString.charAt((int)(AlphaNumericString.length() * Math.random())));
        return kodBuilder.toString();
    }
}
