package hr.java.projekt.threads;

import hr.java.projekt.db.DataBase;
import hr.java.projekt.entitet.ImageShow;
import hr.java.projekt.entitet.ImageShows;
import hr.java.projekt.entitet.Show;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.scene.control.TableView;

import java.util.List;
import java.util.Map;

public class LoadShowsThread extends Task<Void> {
    private List<Show> showList;
    private Map<Show, Double> prosjekMap;
    private TableView<ImageShow<Show>> showTableView;
    public LoadShowsThread(List<Show> showList, Map<Show, Double> prosjekMap, TableView<ImageShow<Show>> showTableView) {
        this.showList = showList;
        this.prosjekMap = prosjekMap;
        this.showTableView = showTableView;
    }
    @Override
    protected Void call() throws Exception {
        showList = DataBase.getShows();
        for(Show show: showList)
            prosjekMap.put(show, show.getProsjek());
        showTableView.setItems(FXCollections.observableList((ImageShows.toImageShowList(showList))));
        return null;
    }
}
