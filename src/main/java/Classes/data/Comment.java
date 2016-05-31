package Classes.data;

/**
 * Created by MEUrena on 5/31/16.
 * All rights reserved.
 */
public class Comment {
    private int id;
    private String description;
    private User author;
    private Article article;

    public Comment() {}

    public Comment(int id, String description, User author, Article article) {
        this.id = id;
        this.description = description;
        this.author = author;
        this.article = article;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
