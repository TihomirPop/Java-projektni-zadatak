package hr.java.projekt.main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class IzbornikController {
    @FXML
    HBox hBox;
    @FXML
    Button showsButton;
    @FXML
    Button mojaListaButton;
    @FXML
    Button mojProfilButton;
    @FXML
    Button editShowsButton;
    @FXML
    Button editUsersButton;
    @FXML
    Button backToLoginButton;
    @FXML
    Button promjeneButton;

    @FXML
    public void initialize() {
        hBox.prefWidthProperty().bind(Main.mainStage.widthProperty().multiply(0.6));
        
        if(Main.currentUser == null){
            hBox.getChildren().remove(mojaListaButton);
            hBox.getChildren().remove(mojProfilButton);
            hBox.getChildren().remove(editShowsButton);
            hBox.getChildren().remove(editUsersButton);
            hBox.getChildren().remove(promjeneButton);
            showsButton.prefWidthProperty().bind(hBox.widthProperty().multiply(0.5));
            backToLoginButton.prefWidthProperty().bind(hBox.widthProperty().multiply(0.5));
        } else if (Main.currentUser.getRole().equals(1)) {
            hBox.getChildren().remove(editShowsButton);
            hBox.getChildren().remove(editUsersButton);
            hBox.getChildren().remove(promjeneButton);
            showsButton.prefWidthProperty().bind(hBox.widthProperty().multiply(0.25));
            backToLoginButton.prefWidthProperty().bind(hBox.widthProperty().multiply(0.25));
            mojaListaButton.prefWidthProperty().bind(hBox.widthProperty().multiply(0.25));
            mojProfilButton.prefWidthProperty().bind(hBox.widthProperty().multiply(0.25));
        } else if (Main.currentUser.getRole().equals(2)) {
            hBox.getChildren().remove(editUsersButton);
            showsButton.prefWidthProperty().bind(hBox.widthProperty().divide(6));
            backToLoginButton.prefWidthProperty().bind(hBox.widthProperty().divide(6));
            mojaListaButton.prefWidthProperty().bind(hBox.widthProperty().divide(6));
            mojProfilButton.prefWidthProperty().bind(hBox.widthProperty().divide(6));
            editShowsButton.prefWidthProperty().bind(hBox.widthProperty().divide(6));
            promjeneButton.prefWidthProperty().bind(hBox.widthProperty().divide(6));
        } else if (Main.currentUser.getRole().equals(3)) {
            showsButton.prefWidthProperty().bind(hBox.widthProperty().divide(7));
            backToLoginButton.prefWidthProperty().bind(hBox.widthProperty().divide(7));
            mojaListaButton.prefWidthProperty().bind(hBox.widthProperty().divide(7));
            mojProfilButton.prefWidthProperty().bind(hBox.widthProperty().divide(7));
            editShowsButton.prefWidthProperty().bind(hBox.widthProperty().divide(7));
            editUsersButton.prefWidthProperty().bind(hBox.widthProperty().divide(7));
            promjeneButton.prefWidthProperty().bind(hBox.widthProperty().divide(7));
        }
    }

    @FXML
    private void shows(){
        Main.prikaziScene(new FXMLLoader(Main.class.getResource("mainList.fxml")));
    }
    @FXML
    private void myShows(){
        Main.prikaziScene(new FXMLLoader(Main.class.getResource("myList.fxml")));
    }
    @FXML
    private void mojProfil(){
        Main.prikaziScene(new FXMLLoader(Main.class.getResource("mojProfil.fxml")));
    }
    @FXML
    private void editShows(){
        Main.prikaziScene(new FXMLLoader(Main.class.getResource("editShows.fxml")));
    }
    @FXML
    private void editUsers() { Main.prikaziScene(new FXMLLoader(Main.class.getResource("editUsers.fxml"))); }
    @FXML
    private void promjene() { Main.prikaziScene(new FXMLLoader(Main.class.getResource("promjene.fxml"))); }
    @FXML
    private void backToLogin(){
        Main.currentUser = null;
        Main.prikaziScene(new FXMLLoader(Main.class.getResource("login.fxml")));
    }
}
