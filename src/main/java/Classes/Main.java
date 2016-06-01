package Classes;

import static spark.Spark.*;

import Classes.HelperClasses.AuthFilter;
import Classes.HelperClasses.AuthRoles;
import Classes.HelperClasses.DatabaseHandler;
import Classes.data.User;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Template;
import spark.ModelAndView;
import spark.TemplateEngine;
import spark.template.freemarker.FreeMarkerEngine;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by luis on 5/30/16.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        staticFiles.location("/public");
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        TemplateEngine renderer = new FreeMarkerEngine();

        try {
            ConnectionSource conn = dbHandler.getConnection();
            dbHandler.createAllTables();
            dbHandler.closeConnection();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        get("/",(request,response) -> {
            Map<String,Object> attributes = new HashMap<String, Object>();
            attributes.put("template_name","./main/index.ftl");
            return renderer.render(new ModelAndView(attributes,"header_footer_layout.ftl"));
        });

        post("/login", (request, response) -> {
            Map<String,Object> attributes = new HashMap<String, Object>();
            //Get variables:
            String username = request.queryParams("username");
            String password = request.queryParams("password");
            //Encrypt (?)
            
            response.redirect("/");
            return null;
        });

        before("/test",new AuthFilter(new FreeMarkerEngine()));
    }
}
