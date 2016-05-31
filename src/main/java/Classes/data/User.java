package Classes.data;

/**
 * Created by MEUrena on 5/31/16.
 * All rights reserved.
 */
public class User {
    private String username;
    private String name;
    private Boolean administrator;
    private Boolean author;

    public User() {}

    public User(String username, String name) {
        this.username = username;
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getAdministrator() {
        return administrator;
    }

    public void setAdministrator(Boolean administrator) {
        this.administrator = administrator;
    }

    public Boolean getAuthor() {
        return author;
    }

    public void setAuthor(Boolean author) {
        this.author = author;
    }
}
