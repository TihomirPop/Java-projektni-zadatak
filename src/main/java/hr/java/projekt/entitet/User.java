package hr.java.projekt.entitet;

import hr.java.projekt.util.Hash;

public class User extends Entitet{
    private String email;
    private String username;
    private String password;
    private String salt;
    private Integer role;
    private Boolean verified;

    public User(Long id, String email, String username, String password, String salt, Integer role, Boolean verified) {
        super(id);
        this.email = email;
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.role = role;
        this.verified = verified;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
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
