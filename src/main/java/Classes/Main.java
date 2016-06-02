package Classes;

import static spark.Spark.*;

import Classes.HelperClasses.AuthFilter;
import Classes.HelperClasses.DatabaseHandler;
import Classes.data.*;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.sun.xml.internal.bind.v2.model.core.ID;
import spark.ModelAndView;
import spark.TemplateEngine;
import spark.template.freemarker.FreeMarkerEngine;

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

        dbHandler.getConnection();
        dbHandler.createAllTables();
        dbHandler.closeConnection();

        before("*", (request, response) -> {
            //Add base model to everything:
            Map<String,Object> attributes = new HashMap<String, Object>();
            attributes.put("logged_in",request.session().attribute("user") != null);
            attributes.put("user",request.session().attribute("user"));
            request.attribute("model",attributes);
        });

        get("/",(request,response) -> {
            ConnectionSource conn = dbHandler.getConnection();
            List<Article> articles = dbHandler.getArticlesWithLimit(0, 20);
            conn.close();
            Map<String,Object> attributes = request.attribute("model");
            attributes.put("template_name","./main/index.ftl");
            attributes.put("articles", articles);

            if(request.cookie("message_type") != null){ //Redirect messages

                attributes.put("message_type",request.cookie("message_type"));
                attributes.put("message",request.cookie("message"));
                response.removeCookie("message_type");
                response.removeCookie("message");

            }

            return renderer.render(new ModelAndView(attributes,"header_footer_layout.ftl"));
        });

        post("/login", (request, response) -> {
            Map<String,Object> attributes = request.attribute("model");
            //Get variables:
            String username = request.queryParams("username");
            String password = request.queryParams("password");
            if(username == null || password == null){
                response.redirect("/");
            }
            //Encrypt (?)
            //In the meantime:
            ConnectionSource conn = dbHandler.getConnection();
            try{
                Dao<User,ID> userDao = DaoManager.createDao(conn,User.class);
                QueryBuilder<User,ID> query = userDao.queryBuilder();
                query.where().eq("username",username)
                        .and().eq("password",password);
                User user = userDao.queryForFirst(query.prepare());
                if(user!= null){
                    //Create session:
                    request.session(true).attribute("user",user);
                }
                else {
                    response.cookie("message_type","danger");
                    response.cookie("message","Usuario No Encontrado");
                }
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
            finally {
                conn.close();
                response.redirect("/");
            }

            return null;
        });

        before("/test",new AuthFilter(new FreeMarkerEngine()));

    }

}
