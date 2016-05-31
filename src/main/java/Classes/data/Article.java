package Classes.data;

import java.util.Date;
import java.util.List;

/**
 * Created by MEUrena on 5/31/16.
 * All rights reserved.
 */
public class Article {
    private int id;
    private String title;
    private String body;
    private Date datePublished;
    private User author;
    private List<Tag> tags;
    private List<Comment> comments;

    public Article() {}

    public Article(int id, String title, String body, Date datePublished, User author, List<Tag> tags) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.datePublished = datePublished;
        this.author = author;
        this.tags = tags;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
