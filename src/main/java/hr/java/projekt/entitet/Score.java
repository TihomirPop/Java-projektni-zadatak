package hr.java.projekt.entitet;

public enum Score {
    APPALLING(1, "appalling"),
    HORRIBLE(2, "horrible"),
    VERYBAD(3, "very bad"),
    BAD(4, "bad"),
    AVERAGE(5, "average"),
    FINE(6, "fine"),
    GOOD(7, "good"),
    VERYGOOD(8, "very good"),
    GREAT(9, "great"),
    MASTERPIECE(10, "masterpiece");

    private final Integer score;
    private final String opis;

    Score(Integer score, String opis) {
        this.score = score;
        this.opis = opis;
    }

    public Integer getScore() {
        return score;
    }

    public String getOpis() {
        return opis;
    }

    @Override
    public String toString() {
        return score.toString() + " - " + opis;
    }
}
