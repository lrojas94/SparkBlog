package Classes.HelperClasses;

import Classes.jpaIntegration.ArticlePreference;
import Classes.jpaIntegration.Comment;
import Classes.jpaIntegration.CommentPreference;

import javax.persistence.EntityManager;

/**
 * Created by MEUrena on 6/7/16.
 * All rights reserved.
 */
public class CommentPreferenceHandler extends DatabaseHandler<CommentPreference> {

    private static CommentPreferenceHandler instance;

    private CommentPreferenceHandler() { super(CommentPreference.class); }

    public static CommentPreferenceHandler getInstance() {
        if (instance == null) {
            instance = new CommentPreferenceHandler();
        }
        return instance;
    }

    public CommentPreference findByUserComment(int userId, int commentId){
        EntityManager em = getEntityManager();
        try {
            return (CommentPreference) em.createNamedQuery(CommentPreference.QUERY_NAME_GET_BY_USER_COMMENT)
                    .setParameter("userId",userId)
                    .setParameter("commentId",commentId)
                    .getSingleResult();
        } catch (Exception ex) {
            throw ex;
        } finally {
            em.close();
        }
    }
}
