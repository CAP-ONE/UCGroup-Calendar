package fr.ucgroup.calendar.models;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject  {

    public String token;
    @PrimaryKey
    public String username;
    public String password;
    public String firstname;
    public String lastname;

    public User() {
    }

    public User(String token, String username, String password, String firstname, String lastname) {
        this.token = token;
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    @Override
    public String toString() {
        return "User[Username: "+getUsername()+"; Firstname: "+getFirstname()+"; Lastname: "+getLastname()+"; Token: "+getToken()+"]";
    }
}
