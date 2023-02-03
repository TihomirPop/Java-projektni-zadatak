package hr.java.projekt.threads;

import hr.java.projekt.entitet.Promjena;
import hr.java.projekt.exceptions.PromjeneException;
import hr.java.projekt.util.DataBase;
import hr.java.projekt.util.Datoteke;
import hr.java.projekt.util.Promjene;
import javafx.collections.FXCollections;
import javafx.scene.control.TableView;

import java.util.List;

public class GetPromjeneThread implements Runnable{
    private final List<Promjena> promjene;
    private final TableView<Promjena> tableView;

    public GetPromjeneThread(List<Promjena> promjene, TableView<Promjena> tableView) {
        this.promjene = promjene;
        this.tableView = tableView;
    }
    @Override
    public void run() {
        try {
            promjene.addAll(Promjene.getPromjene());
            tableView.setItems(FXCollections.observableList(promjene));
        } catch (PromjeneException e) {
            throw new RuntimeException(e);
        }
    }
}
