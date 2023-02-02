package hr.java.projekt.main;

import hr.java.projekt.entitet.Promjena;
import hr.java.projekt.exceptions.PromjeneException;
import hr.java.projekt.threads.GetPromjeneThread;
import hr.java.projekt.util.Promjene;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class PromjeneController {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    @FXML
    private TextField promjenaTextField;
    @FXML
    private TextField staraTextField;
    @FXML
    private TextField novaTextField;
    @FXML
    private ComboBox<Integer> roleComboBox;
    @FXML
    private DatePicker datumDatePicker;
    @FXML
    private TextField vrijemeTextField;
    @FXML
    private RadioButton prijeRadioButton;
    @FXML
    private RadioButton kasnijeRadioButton;
    @FXML
    private TableView<Promjena> tableView;
    @FXML
    private TableColumn<Promjena, String > promjenaTableColumn;
    @FXML
    private TableColumn<Promjena, String > staraTableColumn;
    @FXML
    private TableColumn<Promjena, String > novaTableColumn;
    @FXML
    private TableColumn<Promjena, String > roleTableColumn;
    @FXML
    private TableColumn<Promjena, String > vrijemeTableColumn;
    @FXML
    private GridPane takeFocus;
    private List<Promjena> promjene;
    private ToggleGroup vrijemeToggleGroup = new ToggleGroup();


    @FXML
    private void initialize() {
        roleComboBox.setItems(FXCollections.observableList(new ArrayList<>(List.of(1, 2, 3))));

        kasnijeRadioButton.setToggleGroup(vrijemeToggleGroup);
        prijeRadioButton.setToggleGroup(vrijemeToggleGroup);

        promjenaTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPodatak()));
        staraTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStaraVrijednost()));
        novaTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNovaVrijednost()));
        roleTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRole().toString()));
        vrijemeTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getVrijeme().format(DateTimeFormatter.ofPattern("dd.MM.YYYY. HH:mm"))));

        new Thread(new GetPromjeneThread(tableView)).start();
    }

    @FXML
    private void filter(){
        String podatak = promjenaTextField.getText();
        String staraVrijednost = staraTextField.getText();
        String novaVrijednost = novaTextField.getText();
        Integer role = roleComboBox.getValue();
        LocalDate datum = datumDatePicker.getValue();
        String vrijeme = vrijemeTextField.getText();
        Boolean prije = prijeRadioButton.isSelected();
        Boolean kasnije = kasnijeRadioButton.isSelected();
        List<Promjena> filteredPromjene = promjene;

        if(!podatak.isEmpty())
            filteredPromjene = filteredPromjene.stream().filter(p -> p.getPodatak().toLowerCase().contains(podatak.toLowerCase())).toList();
        if(!staraVrijednost.isEmpty())
            filteredPromjene = filteredPromjene.stream().filter(p -> p.getStaraVrijednost().toLowerCase().contains(staraVrijednost.toLowerCase())).toList();
        if(!novaVrijednost.isEmpty())
            filteredPromjene = filteredPromjene.stream().filter(p -> p.getNovaVrijednost().toLowerCase().contains(novaVrijednost.toLowerCase())).toList();
        if(role != null)
            filteredPromjene = filteredPromjene.stream().filter(p -> p.getRole().equals(role)).toList();
        if(datum != null){
            try{
                LocalDateTime dateTime;

                if (vrijeme.isEmpty()) {
                    if (prije)
                        dateTime = datum.atStartOfDay();
                    else if (kasnije)
                        dateTime = datum.plusDays(1).atStartOfDay();
                    else {
                        dateTime = null;
                    }
                } else
                    dateTime = LocalDateTime.parse(datum.format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")) + vrijeme, DateTimeFormatter.ofPattern("dd.MM.yyyy.HH:mm"));

                if (prije)
                    filteredPromjene = filteredPromjene.stream().filter(p -> p.getVrijeme().isBefore(dateTime)).toList();
                else if (kasnije)
                    filteredPromjene = filteredPromjene.stream().filter(p -> p.getVrijeme().isAfter(dateTime)).toList();
            } catch (DateTimeParseException e){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Datum i vrijeme");
                alert.setHeaderText("Krivi format datuma i vremena");
                alert.showAndWait();
            }
        }

        tableView.setItems(FXCollections.observableList(filteredPromjene));
    }

    @FXML
    private void clearRole(MouseEvent event){
        if(event.getButton().equals(MouseButton.SECONDARY)){
            roleComboBox.getSelectionModel().clearSelection();
            takeFocus.requestFocus();
            filter();
        }
    }
    @FXML
    private void clickPrije(MouseEvent event){
        if(event.getButton().equals(MouseButton.SECONDARY))
            prijeRadioButton.setSelected(false);
        filter();
    }
    @FXML
    private void clickKasnije(MouseEvent event){
        if(event.getButton().equals(MouseButton.SECONDARY))
            kasnijeRadioButton.setSelected(false);
        filter();
    }

    @FXML
    private void clickRow(){
        Promjena promjena = tableView.getSelectionModel().getSelectedItem();
        if(promjena != null){
            StringSelection selection = new StringSelection(
                    "Promjena{" +
                        "podatak='" + promjena.getPodatak() + '\'' +
                        ", staraVrijednost='" + promjena.getStaraVrijednost() + '\'' +
                        ", novaVrijednost='" + promjena.getNovaVrijednost() + '\'' +
                        ", role=" + promjena.getRole() +
                        ", vrijeme=" + promjena.getVrijeme() +
                        '}');

            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, null);
        }
    }
}
