package Classes.jpaIntegration;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import javax.persistence.*;
import java.io.Serializable;
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
            name = "User.findUserByUsername",
            query = "SELECT u FROM User u WHERE u.username = :username"
    ),
    @NamedQuery(
            name = "User.findUserByUsernameAndPassword",
            query = "SELECT u FROM User u WHERE u.username = :username AND u.password = :password"
    )
})
public class User implements Serializable {
    public static String QUERY_NAME_FIND_BY_USERNAME = "User.findUserByUsername";
    public static String QUERY_NAME_FIND_BY_USERNAME_AND_PASSWORD = "User.findUserByUsernameAndPassword";


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
    @OneToMany(mappedBy = "author")
    private Set<Comment> comments = new HashSet<Comment>();
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private Set<ArticlePreference> articlePreferences = new HashSet<ArticlePreference>();
    @OneToMany(mappedBy = "user")
    private Set<CommentPreference> commentPreferences = new HashSet<CommentPreference>();

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


    public Set<ArticlePreference> getArticlePreferences() {
        return articlePreferences;
    }

    public void setArticlePreferences(Set<ArticlePreference> articlePreferences) {
        this.articlePreferences = articlePreferences;
    }

    public Set<CommentPreference> getCommentPreferences() {
        return commentPreferences;
    }

    public void setCommentPreferences(Set<CommentPreference> commentPreferences) {
        this.commentPreferences = commentPreferences;
    }

    public boolean likes(Article article){
        return articlePreferences.stream()
                .filter(ap ->
                     ap.getArticle().getId() == article.getId() && ap.getPreference() == Preference.LIKE
                ).findFirst().isPresent();
    }

    public boolean dislikes(Article article){
        return articlePreferences.stream()
                .filter(ap -> {
                    return ap.getArticle().getId() == article.getId() && ap.getPreference() == Preference.DISLIKE;
                }).findAny().isPresent();
    }

    public boolean hasPreference(Article article){
        return articlePreferences.stream()
                .filter(ap ->
                     ap.getArticle().getId() == article.getId() && ap.getPreference() != Preference.NEUTRAL
                ).findAny().isPresent();
    }

    public boolean likes(Comment comment){
        return commentPreferences.stream()
                .filter(cp ->
                        cp.getComment().getId() == comment.getId() && cp.getPreference() == Preference.LIKE
                ).findAny().isPresent();
    }

    public boolean dislikes(Comment comment){
        return commentPreferences.stream()
                .filter(cp -> {
                    return cp.getComment().getId() == comment.getId() && cp.getPreference() == Preference.DISLIKE;
                }).findAny().isPresent();
    }

    public boolean hasPreference(Comment comment){
        return commentPreferences.stream()
                .filter(cp ->
                        cp.getComment().getId() == comment.getId() && cp.getPreference() != Preference.NEUTRAL
                ).findAny().isPresent();
    }
}
