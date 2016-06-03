package Classes.JsonClasses;

import com.google.gson.annotations.Expose;

/**
 * Created by luis on 6/3/16.
 */
public class Comment {
    @Expose
    private int articleId;
    @Expose
    private String comment;


    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
