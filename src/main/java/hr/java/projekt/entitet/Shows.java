package hr.java.projekt.entitet;

import java.util.ArrayList;
import java.util.List;

public interface Shows {
    Double getProsjek();

    static Show odrediNajboljiShow(List<Show> shows) {
        Double maxProsjek = -1.0;
        Show maxShow = null;
        ListPair<Show, Double> showProsjekPair = new ListPair<>(new ArrayList<>(), new ArrayList<>());
        for(Show show: shows)
            showProsjekPair.add(show, show.getProsjek());

        for(int i = 0; i < showProsjekPair.getFirstList().size(); i++) {
            if (showProsjekPair.fromSecondGet(i) > maxProsjek) {
                maxProsjek = showProsjekPair.fromSecondGet(i);
                maxShow = showProsjekPair.fromFirstGet(i);
            }
            else if ((showProsjekPair.fromSecondGet(i) == maxProsjek) && (showProsjekPair.fromFirstGet(i).getPrevedeniNaslov().compareTo(maxShow.getPrevedeniNaslov()) > 0)) {
                maxProsjek = showProsjekPair.fromSecondGet(i);
                maxShow = showProsjekPair.fromFirstGet(i);
            }
        }
        return maxShow;
    }
}
