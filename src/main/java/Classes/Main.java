package Classes;

import static spark.Spark.*;

import Classes.HelperClasses.DatabaseHandler;
import Classes.data.User;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * Created by luis on 5/30/16.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        staticFiles.location("/public");
        
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();

        try {
            ConnectionSource conn = dbHandler.getConnection();
            dbHandler.createAllTables();
            Dao<User, Integer> userDao = dbHandler.getUserDao();
            List<User> users = userDao.queryForAll();

            System.out.println(users);

            dbHandler.closeConnection();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        get("/",(request,response) -> {
            Map<String,Object> attributes = new HashMap<String, Object>();
            attributes.put("template_name","./main/index.ftl");
            return new ModelAndView(attributes,"header_footer_layout.ftl");
        },new FreeMarkerEngine());
    }
}
