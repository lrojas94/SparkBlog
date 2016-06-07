package Classes.HelperClasses;

import Classes.data.Article;

/**
 * Created by MEUrena on 6/7/16.
 * All rights reserved.
 */
public class ArticleHandler extends DatabaseHandler<Article> {

    private static ArticleHandler instance;

    private ArticleHandler() { super(Article.class); }

    public static ArticleHandler getInstance() {
        if (instance == null) {
            instance = new ArticleHandler();
        }
        return instance;
    }
}
