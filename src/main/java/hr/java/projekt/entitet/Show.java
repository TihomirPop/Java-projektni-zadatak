package hr.java.projekt.entitet;

import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.Serializable;
import java.util.List;

public abstract class Show extends Entitet implements Serializable {
    private String orginalniNaslov;
    private String prevedeniNaslov;
    private String opis;
    private String slika;
    private String studio;
    private List<Genre> genres;
    private List<Show> sequence;
    private List<Long> idSeqience;
    //private List<List<Show>> alternatives;
    //List<Review> reviews;


    public Show(Long id, String orginalniNaslov, String prevedeniNaslov, String opis, String slika, String studio, List<Genre> genres, List<Long> idSeqience/*List<List<Show>> alternatives*/) {
        super(id);
        this.orginalniNaslov = orginalniNaslov;
        this.prevedeniNaslov = prevedeniNaslov;
        this.opis = opis;
        this.slika = slika;
        this.studio = studio;
        this.genres = genres;
        this.idSeqience = idSeqience;
    }

    public String getOrginalniNaslov() {
        return orginalniNaslov;
    }

    public void setOrginalniNaslov(String orginalniNaslov) {
        this.orginalniNaslov = orginalniNaslov;
    }

    public String getPrevedeniNaslov() {
        return prevedeniNaslov;
    }

    public void setPrevedeniNaslov(String prevedeniNaslov) {
        this.prevedeniNaslov = prevedeniNaslov;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getSlika() {
        return slika;
    }

    public void setSlika(String slika) {
        this.slika = slika;
    }

    public String getStudio() {
        return studio;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public List<Show> getSequence() {
        return sequence;
    }

    public void setSequence(List<Show> sequence) {
        this.sequence = sequence;
    }

    public List<Long> getIdSeqience() {
        return idSeqience;
    }

    public void setIdSeqience(List<Long> idSeqience) {
        this.idSeqience = idSeqience;
    }

    /*
    public List<List<Show>> getAlternatives() {
        return alternatives;
    }

    public void setAlternatives(List<List<Show>> alternatives) {
        this.alternatives = alternatives;
    }*/

    @Override
    public String toString() {
        return getId().toString() + " - " + orginalniNaslov;
    }
}
