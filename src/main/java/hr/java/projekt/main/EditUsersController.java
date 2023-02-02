package hr.java.projekt.main;

import hr.java.projekt.entitet.Promjena;
import hr.java.projekt.entitet.User;
import hr.java.projekt.exceptions.DatotekaException;
import hr.java.projekt.exceptions.KriviInputException;
import hr.java.projekt.exceptions.PromjeneException;
import hr.java.projekt.util.Datoteke;
import hr.java.projekt.util.Promjene;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.ls.LSException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EditUsersController {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    @FXML
    private TextField traziKorisnickoImeTextField;
    @FXML
    private TextField traziEmailTextField;
    @FXML
    private RadioButton verificiranRadioButton;
    @FXML
    private RadioButton nijeVerificiranRadioButton;
    @FXML
    private ComboBox<Integer> filtrirajRazinuPravaComboBox;
    @FXML
    private TextField korisnickoImeTextfield;
    @FXML
    private ComboBox<Integer> razinaPravaComboBox;
    @FXML
    private Button spremiButton;
    @FXML
    private Button obrisiButton;
    @FXML
    private TableView<User> userTableView;
    @FXML
    private TableColumn<User, String> korisnickoImeColumn;
    @FXML
    private TableColumn<User, String> emailColumn;
    @FXML
    private TableColumn<User, String> verifikacijaColumn;
    @FXML
    private TableColumn<User, String> razinaPravaColumn;
    @FXML
    private GridPane takeFocus;
    private final ToggleGroup tipToggleGroup = new ToggleGroup();
    private List<User> users;
    private User selectedUser = null;

    public void initialize() {
        try {
            users = Datoteke.getUsers();
            korisnickoImeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUsername()));
            emailColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
            verifikacijaColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getVerified() ? "Verificiran" : "Nije verificiran"));
            razinaPravaColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRole().toString()));
            userTableView.setItems(FXCollections.observableList(users));
            userTableView.setFixedCellSize(60.0);

            verificiranRadioButton.setToggleGroup(tipToggleGroup);
            nijeVerificiranRadioButton.setToggleGroup(tipToggleGroup);

            List<Integer> razinePrava = new ArrayList<>(List.of(1, 2, 3));
            filtrirajRazinuPravaComboBox.setItems(FXCollections.observableList(razinePrava));
            razinaPravaComboBox.setItems(FXCollections.observableList(razinePrava));

            korisnickoImeTextfield.setDisable(true);
            razinaPravaComboBox.setDisable(true);
            spremiButton.setDisable(true);
            obrisiButton.setDisable(true);

            Platform.runLater(() -> takeFocus.requestFocus());
        } catch (DatotekaException e){
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }

    @FXML
    private void filter(){
        String korisnickoIme = traziKorisnickoImeTextField.getText();
        String email = traziEmailTextField.getText();
        Boolean verificiran = verificiranRadioButton.isSelected();
        Boolean nijeVerificiran = nijeVerificiranRadioButton.isSelected();
        Integer razinaPrava = filtrirajRazinuPravaComboBox.getValue();
        List<User> filteredUsers = users;

        if(!korisnickoIme.isEmpty())
            filteredUsers = filteredUsers.stream().filter(user -> user.getUsername().toLowerCase().contains(korisnickoIme.toLowerCase())).toList();
        if(!email.isEmpty())
            filteredUsers = filteredUsers.stream().filter(user -> user.getEmail().toLowerCase().contains(email.toLowerCase())).toList();
        if(verificiran)
            filteredUsers = filteredUsers.stream().filter(user -> user.getVerified()).toList();
        else if(nijeVerificiran)
            filteredUsers = filteredUsers.stream().filter(user -> !user.getVerified()).toList();
        if(razinaPrava != null)
            filteredUsers = filteredUsers.stream().filter(user -> user.getRole().equals(razinaPrava)).toList();

        userTableView.setItems(FXCollections.observableList(filteredUsers));
    }

    @FXML
    private void verificiranClick(MouseEvent event){
        if(event.getButton().equals(MouseButton.SECONDARY))
            verificiranRadioButton.setSelected(false);
        filter();
    }
    @FXML
    private void nijeVerificiranClick(MouseEvent event){
        if(event.getButton().equals(MouseButton.SECONDARY))
            nijeVerificiranRadioButton.setSelected(false);
        filter();
    }
    @FXML
    private void deselectFilterRazinaPrava(MouseEvent event){
        if(event.getButton().equals(MouseButton.SECONDARY)) {
            filtrirajRazinuPravaComboBox.getSelectionModel().clearSelection();
            takeFocus.requestFocus();
            filter();
        }
    }

    @FXML
    private void selectUser(){
        selectedUser = userTableView.getSelectionModel().getSelectedItem();
        if(selectedUser != null){
            korisnickoImeTextfield.setDisable(false);
            razinaPravaComboBox.setDisable(false);
            spremiButton.setDisable(false);
            obrisiButton.setDisable(false);
            korisnickoImeTextfield.setText(selectedUser.getUsername());
            razinaPravaComboBox.getSelectionModel().select(selectedUser.getRole());
        }
    }
    @FXML
    private void spremi(){
        try {
            String korisnickoIme = korisnickoImeTextfield.getText();
            Integer razinaPrava = razinaPravaComboBox.getValue();
            List<String> greske = new ArrayList<>();

            if (korisnickoIme.isEmpty())
                greske.add("korisnicko ime");
            if (razinaPrava == null)
                greske.add("razina prava");

            if (!greske.isEmpty()) {
                Main.pogresanUnosPodataka(greske);
                return;
            }
            List<User> sameUser = users.stream().filter(u -> (u.getUsername().equals(korisnickoIme))).toList();
            if (!(sameUser.isEmpty() || korisnickoIme.equals(selectedUser.getUsername()))) {
                logger.warn("To korisnicko ime ili email se vec koristi", new KriviInputException("To korisnicko ime ili email se vec koristi"));
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Pogrešan unos podataka");
                alert.setHeaderText("Korisnicko ime ili email se vec koristi");
                alert.showAndWait();
                return;
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Spremanje");
            alert.setHeaderText("Želite li spremiti podatke o korisniku?");
            ButtonType daButton = new ButtonType("Da", ButtonBar.ButtonData.YES);
            ButtonType neButton = new ButtonType("Ne", ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(daButton, neButton);
            alert.showAndWait().ifPresent(response -> {
                try {
                    if (response == daButton){
                        List<Promjena> promjene = new ArrayList<>();
                        if(!korisnickoIme.equals(selectedUser.getUsername()))
                            promjene.add(new Promjena(
                                    null,
                                    "Korisničko ime od " + selectedUser.getUsername(),
                                    selectedUser.getUsername(),
                                    korisnickoIme,
                                    Main.currentUser.getRole(),
                                    LocalDateTime.now()
                            ));

                        if(!razinaPrava.equals(selectedUser.getRole()))
                            promjene.add(new Promjena(
                                    null,
                                    "Razina prava od " + selectedUser.getUsername(),
                                    selectedUser.getRole().toString(),
                                    razinaPrava.toString(),
                                    Main.currentUser.getRole(),
                                    LocalDateTime.now()
                            ));

                        if(!promjene.isEmpty())
                            Promjene.addPromjene(promjene);

                        selectedUser.setUsername(korisnickoIme);
                        selectedUser.setRole(razinaPrava);
                        Datoteke.editUser(selectedUser);
                        users = Datoteke.getUsers();
                    }
                } catch (DatotekaException | PromjeneException e){
                    logger.error(e.getMessage(), e);
                    e.printStackTrace();
                }
            });

        } catch (KriviInputException e){
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }

    @FXML
    private void obrisi(){
        if(selectedUser != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Brisanje");
            alert.setHeaderText("Želite li obrisati korisnika?");
            ButtonType daButton = new ButtonType("Da", ButtonBar.ButtonData.YES);
            ButtonType neButton = new ButtonType("Ne", ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(daButton, neButton);
            alert.showAndWait().ifPresent(response -> {
                try {
                    if (response == daButton) {
                        Datoteke.deleteUser(selectedUser);

                        Promjene.addPromjena(new Promjena(
                                null,
                                "Obriši korisnika",
                                selectedUser.getUsername(),
                                "OBRISANO",
                                Main.currentUser.getRole(),
                                LocalDateTime.now()
                        ));

                        users = Datoteke.getUsers();
                        userTableView.setItems(FXCollections.observableList(users));
                        korisnickoImeTextfield.setText("");
                        razinaPravaComboBox.getSelectionModel().clearSelection();
                        korisnickoImeTextfield.setDisable(true);
                        razinaPravaComboBox.setDisable(true);
                        spremiButton.setDisable(true);
                        obrisiButton.setDisable(true);
                    }
                } catch (DatotekaException | PromjeneException e){
                    logger.error(e.getMessage(), e);
                    e.printStackTrace();
                }
            });
        }
    }
}
