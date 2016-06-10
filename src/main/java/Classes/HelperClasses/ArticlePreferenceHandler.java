package Classes.HelperClasses;

import Classes.jpaIntegration.Article;
import Classes.jpaIntegration.ArticlePreference;
import Classes.jpaIntegration.Tag;

import javax.persistence.EntityManager;

/**
 * Created by MEUrena on 6/7/16.
 * All rights reserved.
 */
public class ArticlePreferenceHandler extends DatabaseHandler<ArticlePreference> {

    private static ArticlePreferenceHandler instance;

    private ArticlePreferenceHandler() { super(ArticlePreference.class); }

    public static ArticlePreferenceHandler getInstance() {
        if (instance == null) {
            instance = new ArticlePreferenceHandler();
        }
        return instance;
    }

    public ArticlePreference findByUserArticle(int userId,int articleId){
        EntityManager em = getEntityManager();
        try {
            return (ArticlePreference) em.createNamedQuery(ArticlePreference.QUERY_NAME_GET_BY_USER_ARTICLE)
                    .setParameter("userId",userId)
                    .setParameter("articleId",articleId)
                    .getSingleResult();
        } catch (Exception ex) {
            throw ex;
        } finally {
            em.close();
        }
    }
}
