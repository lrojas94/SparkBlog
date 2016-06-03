package Classes.data;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by MEUrena on 5/31/16.
 * All rights reserved.
 */
@DatabaseTable(tableName = "comments")
public class Comment {
    @DatabaseField(generatedId = true)
    @Expose
    private int id;
    @DatabaseField(columnName = "description", canBeNull = false)
    @Expose
    private String description;
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    @Expose
    private User author;
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    @Expose
    private Article article;

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
}
