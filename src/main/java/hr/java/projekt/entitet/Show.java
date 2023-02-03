package hr.java.projekt.entitet;

import hr.java.projekt.util.DataBase;
import hr.java.projekt.exceptions.BazaPodatakaException;
import hr.java.projekt.main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class Show extends Entitet implements Shows {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private String orginalniNaslov;
    private String prevedeniNaslov;
    private String opis;
    private String slika;
    private String studio;
    private Set<Genre> genres;
    private List<Long> idSeqience;

    public Show(Long id, String orginalniNaslov, String prevedeniNaslov, String opis, String slika, String studio, Set<Genre> genres, List<Long> idSeqience) {
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

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    public List<Long> getIdSeqience() {
        return idSeqience;
    }

    public void setIdSeqience(List<Long> idSeqience) {
        this.idSeqience = idSeqience;
    }

    @Override
    public String toString() {
        return prevedeniNaslov;
    }
    @Override
    public Double getProsjek(){
        try {
            return DataBase.getShowProsjek(this);
        } catch (BazaPodatakaException e){
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }
}
