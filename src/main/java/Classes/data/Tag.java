package Classes.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by MEUrena on 5/31/16.
 * All rights reserved.
 */
@DatabaseTable(tableName = "tags")
public class Tag {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "description", canBeNull = false)
    private String description;
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private Article article;

    public Tag() {}

    public Tag(String description, Article article) {
        this.description = description;
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

    public Article getArticle() { return article; }
}
