package Classes.data;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
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
    @ForeignCollectionField
    private ForeignCollection<ArticleTag> articleTags;

    public Tag() {}

    public Tag(String description, Article article) {
        this.description = description;
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


    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Tag){
            return ((Tag) obj).getDescription().equals(description);
        }
        return super.equals(obj);
    }
}
