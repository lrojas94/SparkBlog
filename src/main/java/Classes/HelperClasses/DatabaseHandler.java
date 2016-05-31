package Classes.HelperClasses;

import Classes.data.Constants;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MEUrena on 5/30/16.
 * All rights reserved.
 */
public class DatabaseHandler {

    private static Connection connection = null;

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
            Class.forName(Constants.DB_DRIVER);
            connection = DriverManager.getConnection(Constants.DB_URL, Constants.DB_USER, Constants.DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        if (connection == null) {
            startDatabaseConnection();
        }

        return connection;
    }

    public static void closeConnection() {
        if (connection == null) {
            System.out.println("Error from DatabaseHandler: No connection found");
        } else {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
