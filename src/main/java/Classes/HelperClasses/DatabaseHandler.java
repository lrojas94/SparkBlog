package Classes.HelperClasses;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Classes.data.*;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
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

    public Dao<User, Integer> getUserDao() {
        return userDao;
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

    public void createAllTables() {
        try {
            TableUtils.createTableIfNotExists(cs, User.class);
            TableUtils.createTableIfNotExists(cs, Tag.class);
            TableUtils.createTableIfNotExists(cs, Comment.class);
            TableUtils.createTableIfNotExists(cs, Article.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void setupDao() {
        try {
            userDao = DaoManager.createDao(cs, User.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
