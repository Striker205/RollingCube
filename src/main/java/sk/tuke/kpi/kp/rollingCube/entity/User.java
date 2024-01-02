package sk.tuke.kpi.kp.rollingCube.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "users")
@NamedQuery( name = "User.registration",
        query = "SELECT u FROM User u WHERE u.login = :login")
@NamedQuery( name = "User.login",
        query = "SELECT u FROM User u WHERE u.login = :login AND u.password = :password")
@NamedQuery( name = "User.save",
        query = "UPDATE User SET level = :level WHERE login = :login")
@NamedQuery( name = "User.load",
        query = "SELECT u.level FROM User u WHERE login = :login")
@NamedQuery( name = "User.resetUsers",
        query = "DELETE FROM User")
public class User implements Serializable {
    @Id
    @GeneratedValue
    private int ident;

    @Column(unique = true)
    private String login;

    private String password;

    private int level;

    public User() {}

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public int getIdent() {
        return ident;
    }

    public void setIdent(int ident) {
        this.ident = ident;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
