package Classes.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by MEUrena on 5/31/16.
 * All rights reserved.
 */
@DatabaseTable(tableName = "article_tag")
public class ArticleTag {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "tag", canBeNull = false,foreign = true,foreignAutoRefresh = true)
    private Tag tag;
    @DatabaseField(columnName = "article", canBeNull = false,foreign = true,foreignAutoRefresh = true)
    private Article article;

    public ArticleTag(){}

    public ArticleTag(Article article,Tag tag) {
        this.article = article;
        this.tag = tag;
    }

    public int getId(){
        return id;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }
}
