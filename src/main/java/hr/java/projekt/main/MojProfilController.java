package hr.java.projekt.main;

import hr.java.projekt.util.Datoteke;
import hr.java.projekt.util.EmailVerification;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.Optional;

public class MojProfilController {
    @FXML
    private VBox passwordVbox;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField newPassword;
    @FXML
    private PasswordField confirmPassword;
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
    private void verify(){
        String kod = generirajKod();
        EmailVerification.sendMail(Main.currentUser.getEmail(), kod);
        TextInputDialog td = new TextInputDialog("******");
        td.setTitle("Verifikacija");
        td.setHeaderText("Verifikacijski kod je poslan na vašu email adresu");
        Optional<String> uneseniKod = td.showAndWait();
        if(!uneseniKod.isPresent())
            return;
        while(!uneseniKod.isPresent() || !uneseniKod.get().equals(kod)){
            if(!uneseniKod.isPresent())
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
