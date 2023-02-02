package hr.java.projekt.main;

import hr.java.projekt.entitet.Promjena;
import hr.java.projekt.exceptions.PromjeneException;
import hr.java.projekt.util.Promjene;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class PromjeneController {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

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
    private List<Promjena> promjene;

    @FXML
    private void initialize() {
        try {
            promjene = Promjene.getPromjene();

            promjenaTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPodatak()));
            staraTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStaraVrijednost()));
            novaTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNovaVrijednost()));
            roleTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRole().toString()));
            vrijemeTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getVrijeme().format(DateTimeFormatter.ofPattern("dd.MM.YYYY. HH:mm"))));

            tableView.setItems(FXCollections.observableList(promjene));
        } catch (PromjeneException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }
}
