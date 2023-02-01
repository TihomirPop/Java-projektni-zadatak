package hr.java.projekt.main;

import hr.java.projekt.entitet.User;
import hr.java.projekt.threads.SendVerificationEmailThread;
import hr.java.projekt.util.Datoteke;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        String username = usernameTextField.getText();
        String email = emailTextField.getText();
        String password = newPasswordTextField.getText();
        String confirmPassword = confirmPasswordTextField.getText();

        List<User> sameUser = Datoteke.getUsers();
        if(!username.isEmpty())
            sameUser = sameUser.stream().filter(user -> user.getUsername().equals(username)).toList();
        if(!email.isEmpty())
            sameUser = sameUser.stream().filter(user -> user.getEmail().equals(email)).toList();

        if(!sameUser.isEmpty() && (!username.equals(Main.currentUser.getUsername()) || !email.equals(Main.currentUser.getEmail()))) {
            logger.warn("To korisnicko ime ili email se vec koristi");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Pogrešan unos podataka");
            alert.setHeaderText("Korisnicko ime ili email se vec koristi");
            alert.showAndWait();
            return;
        }
        if(password.length() < 6 && !password.isEmpty()) {
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
                spremiEmailiPassword(username, email, password);
        });
    }

    private void spremiEmailiPassword(String username, String email, String password) {
        if(!password.isEmpty())
            Main.currentUser.setPassword(password);
        if(!email.equals(Main.currentUser.getEmail())) {
            Main.currentUser.setVerified(false);
            verifikacijaLabel.setText("OPREZ!\nEmail nije verificiran!");
            verificirajButton.setDisable(false);
        }
        Main.currentUser.setEmail(email);
        Main.currentUser.setUsername(username);
        Datoteke.editUser(Main.currentUser);
    }

    @FXML
    private void verify(){
        String kod = generirajKod();
        new Thread(new SendVerificationEmailThread(Main.currentUser.getEmail(), kod)).start();
        TextInputDialog td = new TextInputDialog("******");
        td.setTitle("Verifikacija");
        td.setHeaderText("Verifikacijski kod je poslan na vašu email adresu");
        Optional<String> uneseniKod = td.showAndWait();
        if(uneseniKod.isEmpty())
            return;
        while(uneseniKod.isEmpty() || !uneseniKod.get().toUpperCase().equals(kod)){
            if(uneseniKod.isEmpty())
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
    }

    private String generirajKod(){
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder kodBuilder = new StringBuilder(6);
        for(int i = 0; i < 6; i++)
            kodBuilder.append(AlphaNumericString.charAt((int)(AlphaNumericString.length() * Math.random())));
        return kodBuilder.toString();
    }
}
