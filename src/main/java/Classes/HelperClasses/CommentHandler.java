package Classes.HelperClasses;

import Classes.jpaIntegration.Comment;

import javax.persistence.EntityManager;

/**
 * Created by MEUrena on 6/7/16.
 * All rights reserved.
 */
public class CommentHandler extends DatabaseHandler<Comment> {

    private static CommentHandler instance;

    private CommentHandler() { super(Comment.class); }

    public static CommentHandler getInstance() {
        if (instance == null) {
            instance = new CommentHandler();
        }
        return instance;
    }

    public int deleteCommentById(int id) {
        int success = 0;
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        try {
            success = em.createNamedQuery(Comment.QUERY_NAME_DELETE_COMMENT_BY_ID)
                    .setParameter("id", id)
                    .executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            success = 0;
            e.printStackTrace();
        } finally {
            em.close();
            return success;
        }
    }
}
