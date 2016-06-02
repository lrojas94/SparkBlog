package Classes;

import static spark.Spark.*;

import Classes.HelperClasses.AuthFilter;
import Classes.HelperClasses.DatabaseHandler;
import Classes.data.*;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.ObjectCache;
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
        final String modelParam = "model";
        final String baseLayout = "header_footer_layout.ftl";
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
            request.attribute(modelParam,attributes);
        });

        get("/",(request,response) -> {
            ConnectionSource conn = dbHandler.getConnection();
            List<Article> articles = dbHandler.getArticlesWithLimit(0, 20);
            conn.close();
            Map<String,Object> attributes = request.attribute(modelParam);
            attributes.put("template_name","./main/index.ftl");
            attributes.put("articles", articles);

            if(request.cookie("message_type") != null){ //Redirect messages

                attributes.put("message_type",request.cookie("message_type"));
                attributes.put("message",request.cookie("message"));
                response.removeCookie("message_type");
                response.removeCookie("message");

            }

            return renderer.render(new ModelAndView(attributes,baseLayout));
        });

        get("/signup",(request, response) -> {
            Map<String,Object> attributes = request.attribute(modelParam);
            //by default is empty I guess xD!?
            attributes.put("template_name","./users/signup.ftl");
            return renderer.render(new ModelAndView(attributes,baseLayout));
        });

        post("/signup",(request, response) -> {
            Map<String,Object> attributes = request.attribute(modelParam);
            //get fields:
            String username = request.queryParams("username"),
                    fullname = request.queryParams("fullname"),
                    password = request.queryParams("password"),
                    password2 = request.queryParams("password2");
            //Prepare errors:
            ArrayList<String> errors = new ArrayList<String>();

            //Check if username exists:
            ConnectionSource conn = dbHandler.getConnection();
            Dao<User,Integer> userDao = dbHandler.getUserDao();
            try {
                attributes.put("username",username);
                attributes.put("fullname",fullname);

                if(userDao.queryForEq("username",username).size() != 0){
                    //cant use that.
                    errors.add("El nombre de usuario ya existe. Intente con algun otro.");
                }
                if(username.length() < 6){
                    errors.add("El nombre de usuario ha de tener almenos seis (6) caracteres.");
                }
                if(fullname == null || fullname.equals("")){
                    errors.add("No es posible dejar el campo de nombre vacio.");
                }
                if(!password.equals(password2)){
                    errors.add("Las contrasenas insertadas no son iguales. Revise de nuevo.");
                }
                if(password.length() < 6){
                    errors.add("La contrasena debe contener almenos seis (6) caracteres.");
                }
                
                if(errors.size() == 0){

                    User user = new User();
                    user.setUsername(username);
                    user.setName(fullname);
                    user.setPassword(password);
                    user.setAdministrator(false);
                    user.setAuthor(false);

                    if(userDao.create(user) == 1){
                        //Set as login:
                        request.session(true).attribute("user",user);
                        response.redirect("/");

                    }
                    else{
                        errors.add("ERROR EN BASE DE DATOS");
                        attributes.put("errors",errors);
                        attributes.put("template_name","./users/signup.ftl");
                        return renderer.render(new ModelAndView(attributes,baseLayout));
                    }
                }
                else{
                    attributes.put("errors",errors);
                    attributes.put("template_name","./users/signup.ftl");
                    return renderer.render(new ModelAndView(attributes,baseLayout));
                }

            }catch (Exception e){
                System.out.println(e.getMessage());
            }finally {
                conn.close();
            }

            return null;
        });

        post("/login", (request, response) -> {
            Map<String,Object> attributes = request.attribute(modelParam);
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

        // -------------------------------- USER CRUD -------------------------------------------------------------- //
        get("/user/:id",(request, response) -> {
            Map<String,Object> attributes = request.attribute(modelParam);
            //Get the user:
            int userId = Integer.parseInt(request.params("id"));
            ConnectionSource con = dbHandler.getConnection();
            Dao<User,Integer> userDao = dbHandler.getUserDao();
            Dao<Article,Integer> articleDao = null;
            try{
                User user = userDao.queryForId(userId);
                ArrayList<Article> articles = new ArrayList<Article>();
                //---- TEST ----//
                Article test = new Article();
                test.setTitle("Test Title");
                test.setDatePublished(new Date());
                articles.add(test);
                articles.add(test);
                articles.add(test);
                articles.add(test);
                articles.add(test);
                articles.add(test);
                articles.add(test);
                articles.add(test);
                articles.add(test);
                articles.add(test);
                articles.add(test);
                articles.add(test);
                articles.add(test);
                articles.add(test);
                //--- END TEST ---//
                //articles = articleDao.queryForEq("author",user.getId()); //TODO: Uncomment this after Dao is working.
                attributes.put("User",user);
                attributes.put("articles",articles);

            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
            finally{
                con.close();
            }

            attributes.put("template_name","./users/show.ftl");

            return renderer.render(new ModelAndView(attributes,baseLayout));
        });
        // -------------------------------- FINISH USER CRUD -------------------------------------------------------------- //


    }

}
