package hr.java.projekt.entitet;

public class Admin extends User{
    private Integer role;

    public Admin(Long id, String username, String email, String password, Integer role) {
        super(id, username, email, password);
        this.role = role;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }
}
