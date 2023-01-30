package hr.java.projekt.entitet;

import java.util.List;
import java.util.stream.Collectors;

public interface ImageShows {
    static List<ImageShow<Show>> toImageShowList(List<Show> shows){
        return shows.stream().map(s -> new ImageShow<>(s)).collect(Collectors.toList());
    }
}
