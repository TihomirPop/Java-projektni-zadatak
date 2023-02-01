package hr.java.projekt.entitet;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Promjena extends Entitet implements Serializable {
    private String podatak;
    private String staraVrijednost;
    private String novaVrijednost;
    private Integer role;
    private LocalDateTime vrijeme;

    public Promjena(Long id, String podatak, String staraVrijednost, String novaVrijednost, Integer role, LocalDateTime vrijeme) {
        super(id);
        this.podatak = podatak;
        this.staraVrijednost = staraVrijednost;
        this.novaVrijednost = novaVrijednost;
        this.role = role;
        this.vrijeme = vrijeme;
    }

    public String getPodatak() {
        return podatak;
    }

    public void setPodatak(String podatak) {
        this.podatak = podatak;
    }

    public String getStaraVrijednost() {
        return staraVrijednost;
    }

    public void setStaraVrijednost(String staraVrijednost) {
        this.staraVrijednost = staraVrijednost;
    }

    public String getNovaVrijednost() {
        return novaVrijednost;
    }

    public void setNovaVrijednost(String novaVrijednost) {
        this.novaVrijednost = novaVrijednost;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public LocalDateTime getVrijeme() {
        return vrijeme;
    }

    public void setVrijeme(LocalDateTime vrijeme) {
        this.vrijeme = vrijeme;
    }
}
