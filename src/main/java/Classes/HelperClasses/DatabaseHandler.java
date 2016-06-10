package Classes.HelperClasses;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Classes.data.*;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

import static Classes.data.Constants.*;

/**
 * Created by MEUrena on 5/30/16.
 * All rights reserved.
 */
public class DatabaseHandler<T> {

    private static EntityManagerFactory emf = null;
    private Class<T> entityClass = null;

    private static ConnectionSource cs = null;
    private static Dao<User, Integer> userDao = null;
    private static Dao<Article, Integer> articleDao = null;
    private static Dao<Comment, Integer> commentDao = null;
    private static Dao<Tag, Integer> tagDao = null;
    private static Dao<ArticleTag, Integer> articleTagDao = null;

    private static DatabaseHandler instance = null;
    protected DatabaseHandler() {}

    protected DatabaseHandler(Class<T> entityClass) {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory("org.hibernate.sparkblog.jpa");
        }
        this.entityClass = entityClass;
    }

    public EntityManager getEntityManager() { return emf.createEntityManager(); }

    /*
    New Handler Classes.
     */


    public boolean insertIntoDatabase(T entity) throws Exception {
        boolean success = false;
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(entity);
            em.getTransaction().commit();
            success = true;
        } catch (Exception ex) {
            em.getTransaction().rollback();
            success = false;
            throw ex;
        } finally {
            em.close();
            return success;
        }
    }

    public T findObjectWithId(Object id) throws Exception {
        EntityManager em = getEntityManager();
        try {
            return em.find(entityClass, id);
        } catch (Exception ex) {
            throw ex;
        } finally {
            em.close();
        }
    }

    public List<T> getAllObjects() throws Exception {
        EntityManager em = getEntityManager();
        try{
            CriteriaQuery<T> criteriaQuery = em.getCriteriaBuilder().createQuery(entityClass);
            criteriaQuery.select(criteriaQuery.from(entityClass));
            return em.createQuery(criteriaQuery).getResultList();
        } catch (Exception ex) {
            throw ex;
        } finally {
            em.close();
        }
    }

    public void updateObject(T entity) throws Exception {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        try {
            em.merge(entity);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    public boolean deleteObjectWithId(Object id) throws Exception {
        boolean success = false;
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        try {
            T entity = em.find(entityClass, id);
            em.remove(entity);
            em.getTransaction().commit();
            success = true;
        } catch (Exception ex) {
            em.getTransaction().rollback();
            success = false;
            throw ex;
        } finally {
            em.close();
            return success;
        }
    }


    public static DatabaseHandler getInstance() {
        if (instance == null) {
            instance = new DatabaseHandler();
        }
        return instance;
    }

    private static void startDatabaseConnection() {
        try {
            cs = new JdbcConnectionSource(DB_URL, DB_USER, DB_PASSWORD);
            setupDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ConnectionSource getConnection() {
        if (cs == null) {
            startDatabaseConnection();
        }

        return cs;
    }

    public static void closeConnection() {
        if (cs == null) {
            System.out.println("Error from DatabaseHandler: No connection found");
        } else {
            try {
                cs.close();
                cs = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Dao<User, Integer> getUserDao() {
        if (userDao == null) {
            try {
                userDao = DaoManager.createDao(cs, User.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return userDao;
    }

    public Dao<Article, Integer> getArticleDao() {
        if (articleDao == null) {
            try {
                articleDao = DaoManager.createDao(cs, Article.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return articleDao;
    }

    public Dao<Tag, Integer> getTagDao() {
        if (tagDao == null) {
            try {
                tagDao = DaoManager.createDao(cs, Tag.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return tagDao;
    }

    public Dao<ArticleTag, Integer> getArticleTagDao() {
        if (articleTagDao == null) {
            try {
                articleTagDao = DaoManager.createDao(cs, ArticleTag.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return articleTagDao;
    }

    public Dao<Comment, Integer> getCommentDao() {
        if (commentDao == null) {
            try {
                commentDao = DaoManager.createDao(cs, Comment.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return commentDao;
    }

    public void createAllTables() {
        try {
            TableUtils.createTableIfNotExists(cs, User.class);
            TableUtils.createTableIfNotExists(cs, Article.class);
            TableUtils.createTableIfNotExists(cs, Tag.class);
            TableUtils.createTableIfNotExists(cs, Comment.class);
            TableUtils.createTableIfNotExists(cs, ArticleTag.class);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void setupDao() {
        try {
            userDao = DaoManager.createDao(cs, User.class);
            articleDao = DaoManager.createDao(cs, Article.class);
            tagDao = DaoManager.createDao(cs, Tag.class);
            commentDao = DaoManager.createDao(cs, Comment.class);
            articleTagDao = DaoManager.createDao(cs, ArticleTag.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Article> getArticlesWithLimit(int start, int limit) {
        List<Article> results = new ArrayList<>();
        try {
            QueryBuilder query = articleDao.queryBuilder();
            query.offset((long)start).limit((long)limit);
            query.orderBy("date_published", false);
            results = articleDao.query(query.prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    /**
     * Convenience methods to build and run our prepared queries.
     */

    private static PreparedQuery<Article> articlesForTagQuery = null;

    public static List<Article> lookupArticlesForTag(Tag tag) throws SQLException {
        if (articlesForTagQuery == null) {
            articlesForTagQuery = makePostsForUserQuery();
        }
        articlesForTagQuery.setArgumentHolderValue(0, tag);
        return articleDao.query(articlesForTagQuery);
    }

    /**
     * Build our query for Post objects that match a User.
     */
    private static PreparedQuery<Article> makePostsForUserQuery() throws SQLException {
        // build our inner query for UserPost objects
        QueryBuilder<ArticleTag, Integer> articleTagQb = articleTagDao.queryBuilder();
        // just select the post-id field
        articleTagQb.selectColumns("article_id");
        SelectArg userSelectArg = new SelectArg();
        // you could also just pass in user1 here
        articleTagQb.where().eq("tag_id", userSelectArg);

        // build our outer query for Post objects
        QueryBuilder<Article, Integer> postQb = articleDao.queryBuilder();
        // where the id matches in the post-id from the inner query
        postQb.where().in("id", articleTagQb);
        postQb.orderBy("date_published", false);
        return postQb.prepare();
    }

}
