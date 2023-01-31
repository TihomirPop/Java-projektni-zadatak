package hr.java.projekt.entitet;

import java.io.Serializable;

public class UserShow extends Entitet implements Serializable {
    private User user;
    private Show show;
    private Score score;
    private Integer watched;

    UserShow(Long id, User user, Show show, Score score, Integer watched) {
        super(id);
        this.user = user;
        this.show = show;
        this.score = score;
        this.watched = watched;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
    }

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }

    public Integer getWatched() {
        return watched;
    }

    public void setWatched(Integer watched) {
        this.watched = watched;
    }
}
