package Classes.jpaIntegration;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;
import org.eclipse.jetty.util.annotation.Name;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.io.Serializable;


/**
 * Created by MEUrena on 5/31/16.
 * All rights reserved.
 */
@Entity
@Table(name = "articles")
@NamedQueries({
        @NamedQuery(
                name = "findArticlesByTitle",
                query = "Select a FROM Article a WHERE a.title = :title"
        ),
        @NamedQuery(
                name = "findArticlesInDescOrder",
                query = "SELECT a FROM Article a ORDER BY a.datePublished DESC"
        )
})
public class Article implements Serializable {

    public static String QUERY_NAME_FIND_ARTICLES_BY_TITLE = "findArticlesByTitle";
    public static String QUERY_NAME_FIND_ARTICLES_IN_DESC_ORDER = "findArticlesInDescOrder";

    @Id
    @GeneratedValue
    @Expose
    private int id;
    @Column(name = "title",unique = true)
    @Expose
    private String title;
    @Column(name = "body",length = 1000)
    private String body;
    @Column(name = "date_published",nullable = false)
    private Date datePublished;
    @ManyToOne
    @JoinColumn(name = "author",nullable = false)
    private User author;
    @ManyToMany
    @JoinTable(name = "article_tags")
    private Set<Tag> tags = new HashSet<Tag>();
    @OneToMany(mappedBy = "article",cascade = CascadeType.ALL)
    private Set<Comment> comments = new HashSet<Comment>();;
    @Column(name = "likes")
    private Integer likes = 0;
    @Column(name = "dislikes")
    private Integer dislikes = 0;

    public Article() {}

    public Article(String title, String body, Date datePublished, User author) {
        this.title = title;
        this.body = body;
        this.datePublished = datePublished;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(Date datePublished) {
        this.datePublished = datePublished;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }


    public Integer getLikes() { return likes; }

    public void setLikes(Integer likes) { this.likes = likes; }

    public Integer getDislikes() { return dislikes; }

    public void setDislikes(Integer dislikes) { this.dislikes = dislikes; }

    public String getFormattedTags(){

        if(getTags() == null){
            return  "";
        }
        return getTags().stream().map((tag -> tag.getDescription())).collect(Collectors.joining(","));
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }
}
