package hr.java.projekt.entitet;

public abstract class Entitet {
    private Long id;

    public Entitet(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
