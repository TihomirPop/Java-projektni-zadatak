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

    public static Score getScoreFromInt(Integer intScore){
        return switch (intScore) {
            case 1 -> Score.APPALLING;
            case 2 -> Score.HORRIBLE;
            case 3 -> Score.VERYBAD;
            case 4 -> Score.BAD;
            case 5 -> Score.AVERAGE;
            case 6 -> Score.FINE;
            case 7 -> Score.GOOD;
            case 8 -> Score.VERYGOOD;
            case 9 -> Score.GREAT;
            case 10 -> Score.MASTERPIECE;
            default -> throw new RuntimeException("Critical error, nije moguce doci do ovog dijela koda!");
        };
    }

    @Override
    public String toString() {
        return score.toString() + " - " + opis;
    }
}
