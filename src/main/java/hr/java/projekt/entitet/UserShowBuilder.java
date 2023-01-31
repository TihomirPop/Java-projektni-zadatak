package hr.java.projekt.entitet;

public class UserShowBuilder {
    private Long id;
    private User user;
    private Show show;
    private Score score;
    private Integer watched;

    UserShowBuilder(Long id){
        this.id = id;
    }
    public UserShowBuilder saId(Long id) {
        this.id = id;
        return this;
    }

    public UserShowBuilder saUser(User user) {
        this.user = user;
        return this;
    }

    public UserShowBuilder saShow(Show show) {
        this.show = show;
        return this;
    }

    public UserShowBuilder saScore(Score score) {
        this.score = score;
        return this;
    }

    public UserShowBuilder saWatched(Integer watched) {
        this.watched = watched;
        return this;
    }

    public UserShow createUserShow() {
        return new UserShow(id, user, show, score, watched);
    }
}