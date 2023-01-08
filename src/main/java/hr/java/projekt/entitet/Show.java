package hr.java.projekt.entitet;

import javafx.scene.image.Image;

import java.io.Serializable;
import java.util.List;

public abstract class Show extends Entitet implements Serializable {
    private String orginalniNaslov;
    private String prevedeniNaslov;
    private String opis;
    private Image slika;
    private String studio;
    private List<String> genres;
    private List<Show> sequence;
    private List<Long> idSeqience;
    //private List<List<Show>> alternatives;
    //List<Review> reviews;


    public Show(Long id, String orginalniNaslov, String prevedeniNaslov, String opis, Image slika, String studio, List<String> genres, List<Show> sequence, List<Long> idSeqience/*List<List<Show>> alternatives*/) {
        super(id);
        this.orginalniNaslov = orginalniNaslov;
        this.prevedeniNaslov = prevedeniNaslov;
        this.opis = opis;
        this.slika = slika;
        this.studio = studio;
        this.genres = genres;
        this.sequence = sequence;
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

    public Image getSlika() {
        return slika;
    }

    public void setSlika(Image slika) {
        this.slika = slika;
    }

    public String getStudio() {
        return studio;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
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
}
