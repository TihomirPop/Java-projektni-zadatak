package hr.java.projekt.threads;

import hr.java.projekt.entitet.Promjena;
import hr.java.projekt.exceptions.PromjeneException;
import hr.java.projekt.util.Promjene;
import javafx.collections.FXCollections;
import javafx.scene.control.TableView;

public class GetPromjeneThread implements Runnable{
    private TableView<Promjena> tableView;

    public GetPromjeneThread(TableView<Promjena> tableView) {
        this.tableView = tableView;
    }
    @Override
    public void run() {
        try {
            tableView.setItems(FXCollections.observableList(Promjene.getPromjene()));
        } catch (PromjeneException e) {
            throw new RuntimeException(e);
        }
    }
}
