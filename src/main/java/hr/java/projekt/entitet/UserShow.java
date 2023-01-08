package hr.java.projekt.entitet;

import java.io.Serializable;

public class UserShow extends Entitet implements Serializable {
    private User user;
    private Show show;
    private Score score;
    private String comment;

    public UserShow(Long id, User user, Show show, Score score, String comment) {
        super(id);
        this.user = user;
        this.show = show;
        this.score = score;
        this.comment = comment;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
