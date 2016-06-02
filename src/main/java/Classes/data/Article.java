package Classes.data;

import java.util.Date;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by MEUrena on 5/31/16.
 * All rights reserved.
 */
@DatabaseTable(tableName = "articles")
public class Article {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "title", canBeNull = false)
    private String title;
    @DatabaseField(columnName = "body", canBeNull = false,dataType = DataType.LONG_STRING)
    private String body;
    @DatabaseField(columnName = "date_published", canBeNull = false)
    private Date datePublished;
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private User author;
    @ForeignCollectionField
    private ForeignCollection<Tag> tags;
    @ForeignCollectionField
    private ForeignCollection<Comment> comments;

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

    public ForeignCollection<Tag> getTags() {
        return tags;
    }

    public ForeignCollection<Comment> getComments() { return comments; }
}
