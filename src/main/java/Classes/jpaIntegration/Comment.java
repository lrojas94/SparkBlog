package Classes.jpaIntegration;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import javax.persistence.*;

/**
 * Created by MEUrena on 5/31/16.
 * All rights reserved.
 */
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue
    @Expose
    private int id;
    @Column(name = "description",nullable = false)
    @Expose
    private String description;
    @ManyToOne
    @JoinColumn(name = "author",nullable = false)
    @Expose
    private User author;
    @ManyToOne
    @JoinColumn(name = "article",nullable = false)
    @Expose
    private Article article;
    @Column(name = "likes")
    private Integer likes;
    @Column(name = "dislikes")
    private Integer dislikes;

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

    public Integer getLikes() { return likes; }

    public void setLikes(Integer likes) { this.likes = likes; }

    public Integer getDislikes() { return dislikes; }

    public void setDislikes(Integer dislikes) { this.dislikes = dislikes; }

}
