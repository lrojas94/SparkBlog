package Classes.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
/**
 * Created by MEUrena on 5/31/16.
 * All rights reserved.
 */
@DatabaseTable(tableName = "users")
public class User {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "username", canBeNull = false)
    private String username;
    @DatabaseField(columnName = "name", canBeNull = false)
    private String name;
    @DatabaseField(columnName = "password", canBeNull = false)
    private String password;
    @DatabaseField(columnName = "isAdministrator")
    private Boolean administrator;
    @DatabaseField(columnName = "isAuthor")
    private Boolean author;

    public User() {}

    public User(String username, String name, String password, Boolean administrator, Boolean author) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.administrator = administrator;
        this.author = author;
    }

    public int getId() {
        return id;
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

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

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
