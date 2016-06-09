package Classes.HelperClasses;

import Classes.jpaIntegration.Article;

import javax.persistence.EntityManager;
import java.util.List;

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

    public Article findArticleByTitle(String title){
        EntityManager em = getEntityManager();
        try {
            return (Article) em.createNamedQuery(Article.QUERY_NAME_FIND_ARTICLES_BY_TITLE)
                    .setParameter("title",title)
                    .getSingleResult();
        } catch (Exception ex) {
            throw ex;
        } finally {
            em.close();
        }
    }

    public List<Article> findArticlesInDescOrder() {
        EntityManager em = getEntityManager();
        try {
            return (List<Article>) em.createNamedQuery(Article.QUERY_NAME_FIND_ARTICLES_IN_DESC_ORDER).getResultList();
        } catch (Exception ex) {
            throw ex;
        } finally {
            em.close();
        }
    }
}
