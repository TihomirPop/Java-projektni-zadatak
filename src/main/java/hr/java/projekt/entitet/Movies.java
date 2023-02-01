package hr.java.projekt.entitet;

import java.util.List;

public sealed interface Movies permits Movie{
    Boolean isNajnoviji(List<Movie> movies);
}
