package Classes.HelperClasses;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;

import Classes.data.*;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;

import static Classes.data.Constants.*;

/**
 * Created by MEUrena on 5/30/16.
 * All rights reserved.
 */
public class DatabaseHandler {

    private static ConnectionSource cs = null;
    private static Dao<User, Integer> userDao = null;
    private static Dao<Article, Integer> articleDao = null;
    private static Dao<Comment, Integer> commentDao = null;
    private static Dao<Tag, Integer> tagDao = null;

    private static DatabaseHandler instance = null;
    protected DatabaseHandler() {}

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

    public static void insertNewUser(User user) {
        try {
            userDao.create(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static User getUserWithId(int id) {
        User user = new User();
        try {
            user = userDao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    public static void insertNewArticle(Article article) {
        try {
            articleDao.create(article);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteArticle(Article article) {
        try {
            articleDao.delete(article);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertTags(List<Tag> tags) {
        try {
            for (Tag tag : tags) {
                tagDao.create(tag);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertNewCOmment(Comment comment) {
        try {
            commentDao.create(comment);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteComment(Comment comment) {
        try {
            commentDao.delete(comment);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
