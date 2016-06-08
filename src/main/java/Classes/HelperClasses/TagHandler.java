package Classes.HelperClasses;

import Classes.jpaIntegration.Tag;

import javax.persistence.EntityManager;

/**
 * Created by MEUrena on 6/7/16.
 * All rights reserved.
 */
public class TagHandler extends DatabaseHandler<Tag> {

    private static TagHandler instance;

    private TagHandler() { super(Tag.class); }

    public static TagHandler getInstance() {
        if (instance == null) {
            instance = new TagHandler();
        }
        return instance;
    }

    public Tag findByDescription(String description){
        EntityManager em = getEntityManager();
        try {
            return (Tag) em.createNamedQuery(Tag.QUERY_NAME_TAG_BY_DESC)
                    .setParameter("description",description)
                    .getSingleResult();
        } catch (Exception ex) {
            throw ex;
        } finally {
            em.close();
        }
    }
}
