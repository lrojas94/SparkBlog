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
@Table(name = "comments")
@NamedQueries({
        @NamedQuery(
                name = "deleteCommentById",
                query = "DELETE FROM Comment c WHERE c.id = :id"
        )
})
public class Comment implements Serializable {

    public static String QUERY_NAME_DELETE_COMMENT_BY_ID = "deleteCommentById";

    @Id
    @GeneratedValue
    @Expose
    private int id;
    @Column(name = "description",nullable = false)
    @Expose
    private String description;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "author",nullable = false)
    @Expose
    private User author;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "article",nullable = false)
    @Expose
    private Article article;
    @OneToMany(mappedBy = "comment")
    private Set<CommentPreference> commentPreferenceSet = new HashSet<>();

    public Comment() {}

    public Comment(String description, User author, Article article) {
        this.description = description;
        this.author = author;
        this.article = article;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }


    public Set<CommentPreference> getCommentPreferenceSet() {
        return commentPreferenceSet;
    }

    public void setCommentPreferenceSet(Set<CommentPreference> commentPreferenceSet) {
        this.commentPreferenceSet = commentPreferenceSet;
    }
}
