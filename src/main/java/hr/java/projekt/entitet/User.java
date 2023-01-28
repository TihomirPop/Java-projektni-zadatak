package hr.java.projekt.entitet;

import hr.java.projekt.util.Hash;

public class User extends Entitet{
    private String email;
    private String username;
    private Long password;
    private Integer role;
    private Boolean verified;

    public User(Long id, String email, String username, Long password, Integer role, Boolean verified) {
        super(id);
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
        this.verified = verified;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = Hash.hash(password);
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }
}
