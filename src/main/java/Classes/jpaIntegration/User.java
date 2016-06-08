package Classes.jpaIntegration;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by MEUrena on 5/31/16.
 * All rights reserved.
 */
@Entity
@Table(name = "users")
@NamedQueries({
    @NamedQuery(
            name = "findUserByUsername",
            query = "SELECT u FROM User u WHERE u.username = :username"
    ),
    @NamedQuery(
            name = "findUserByUsernameAndPassword",
            query = "SELECT u FROM User u WHERE u.username = :username AND u.password = :password"
    )
})
public class User {
    public static String QUERY_NAME_FIND_BY_USERNAME = "findUserByUsername";
    public static String QUERY_NAME_FIND_BY_USERNAME_AND_PASSWORD = "findUserByUsernameAndPassword";


    @Id
    @GeneratedValue
    @Expose
    private int id;
    @Column(name = "username",nullable = false,unique = true)
    @Expose
    private String username;
    @Column(name = "name")
    @Expose
    private String name;
    @Column(name = "password",nullable = false)
    private String password;
    @Column(name = "isAdministrator")
    private Boolean administrator;
    @Column(name = "isAuthor")
    private Boolean author;
    @OneToMany(mappedBy = "author",cascade = CascadeType.ALL)
    private Set<Article> articles = new HashSet<Article>();
    @OneToMany(mappedBy = "author",cascade = CascadeType.ALL)
    private Set<Comment> comments = new HashSet<Comment>();

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



    @Override
    public String toString() {
        return username;
    }

    public Set<Article> getArticles() {
        return articles;
    }

    public void setArticles(Set<Article> articles) {
        this.articles = articles;
    }


    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }
}
