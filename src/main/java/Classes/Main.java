package Classes;

import static spark.Spark.*;

import Classes.HelperClasses.ArticleAuthorFilter;
import Classes.HelperClasses.AuthFilter;
import Classes.HelperClasses.AuthRoles;
import Classes.HelperClasses.DatabaseHandler;
import Classes.data.*;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.sun.xml.internal.bind.v2.model.core.ID;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateEngine;
import spark.template.freemarker.FreeMarkerEngine;

import java.util.*;
import java.util.prefs.Preferences;

/**
 * Created by luis on 5/30/16.
 */
public class Main {
    public final static String MODEL_PARAM = "model";
    public static final String BASE_LAYOUT = "header_footer_layout.ftl";

    public static void redirectWrongAddress(Response response){
        response.cookie("message_type","danger");
        response.cookie("message","La direccion a la que intento acceder es incorrecta.");
        response.redirect("/");
    }

    public static void main(String[] args) throws Exception {

        staticFiles.location("/public");

        DatabaseHandler dbHandler = DatabaseHandler.getInstance();

        TemplateEngine renderer = new FreeMarkerEngine();

        Preferences userPrefs = Preferences.userRoot();
        Boolean isFirstRun = userPrefs.getBoolean("first_run", true);
        if (isFirstRun) {
            System.out.println("running for the first time");
            dbHandler.getConnection();
            dbHandler.createAllTables();
            User firstUser = new User("admin", "Administrator", "admin", true, true);
            Dao<User, Integer> userDao = dbHandler.getUserDao();
            userDao.create(firstUser);
            dbHandler.closeConnection();
            userPrefs.putBoolean("first_run", false);
        }

        before((request, response) -> {
            //Add base model to everything:
            Map<String,Object> attributes = new HashMap<String, Object>();
            attributes.put("logged_in",request.session(true).attribute("user") != null);
            attributes.put("user",request.session(true).attribute("user"));
            request.attribute(MODEL_PARAM,attributes);
        });

        get("/",(request,response) -> {
            ConnectionSource conn = dbHandler.getConnection();
            List<Article> articles = dbHandler.getArticlesWithLimit(0, 20);
            conn.close();
            Map<String,Object> attributes = request.attribute(MODEL_PARAM);
            attributes.put("template_name","./main/index.ftl");
            attributes.put("articles", articles);

            if(request.cookie("message_type") != null){ //Redirect messages

                attributes.put("message_type",request.cookie("message_type"));
                attributes.put("message",request.cookie("message"));
                response.removeCookie("message_type");
                response.removeCookie("message");

            }

            return renderer.render(new ModelAndView(attributes, BASE_LAYOUT));
        });

        get("/signup",(request, response) -> {
            Map<String,Object> attributes = request.attribute(MODEL_PARAM);
            //by default is empty I guess xD!?
            attributes.put("template_name","./users/add.ftl");
            return renderer.render(new ModelAndView(attributes, BASE_LAYOUT));
        });

        post("/signup",(request, response) -> {
            Map<String,Object> attributes = request.attribute(MODEL_PARAM);
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
                        attributes.put("template_name","./users/add.ftl");
                        return renderer.render(new ModelAndView(attributes, BASE_LAYOUT));
                    }
                }
                else{
                    attributes.put("errors",errors);
                    attributes.put("template_name","./users/add.ftl");
                    return renderer.render(new ModelAndView(attributes, BASE_LAYOUT));
                }

            }catch (Exception e){
                System.out.println(e.getMessage());
            }finally {
                conn.close();
            }

            return null;
        });

        //--------------------------- ARTICLE CRUD START ----------------------------------------//

        before("/article/add",new AuthFilter(renderer, new HashSet<AuthRoles>() {{
            add(AuthRoles.AUTHOR);
        }}));

        get("/article/add", (request, response) -> {
            Map<String, Object> attributes = request.attribute(MODEL_PARAM);
            attributes.put("template_name","./articles/add_edit.ftl");
            return renderer.render(new ModelAndView(attributes, BASE_LAYOUT));
        });

        post("/article/add", (request, response) -> {
            return articleAddEdit(request,response,null,false);
        });

        before("/article/edit/:id",new ArticleAuthorFilter(renderer));

        get("/article/edit/:id",(request, response) -> {
            Map<String,Object> attributes = request.attribute(MODEL_PARAM);
            ConnectionSource conn = dbHandler.getConnection();
            try{

                int id = Integer.parseInt(request.params("id"));
                Dao<Article,Integer> articleDao = dbHandler.getArticleDao();
                Article article = articleDao.queryForId(id);

                if(article == null){
                    response.cookie("message_type","danger");
                    response.cookie("message","Articulo no encontrado.");
                    response.redirect("/");
                }

                attributes.put("article",article);
                attributes.put("is_edit",true);
                attributes.put("template_name","./articles/add_edit.ftl");

                return renderer.render(new ModelAndView(attributes, BASE_LAYOUT));
            }
            catch (Exception e){
                if(e instanceof NumberFormatException){
                    //id is not a number...
                    redirectWrongAddress(response);
                }
            }finally {
                conn.close();
            }
            return null;
        });

        post("/article/edit/:id",(request, response) -> {
            ConnectionSource conn = dbHandler.getConnection();
            try {

                int id = Integer.parseInt(request.params("id"));
                Dao<Article, Integer> articleDao = dbHandler.getArticleDao();
                Article article = articleDao.queryForId(id);

                if (article == null) {
                    response.cookie("message_type", "danger");
                    response.cookie("message", "Articulo no encontrado.");
                    response.redirect("/");
                }

                return renderer.render(articleAddEdit(request,response,article,true));

            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }

            return null;
        });

        //--------------------------- ARTICLE CRUD FINISH ----------------------------------------//

        post("/login", (request, response) -> {
            Map<String,Object> attributes = request.attribute(MODEL_PARAM);
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
            Map<String,Object> attributes = request.attribute(MODEL_PARAM);
            //Get the user:
            int userId = Integer.parseInt(request.params("id"));
            ConnectionSource con = dbHandler.getConnection();
            Dao<User,Integer> userDao = dbHandler.getUserDao();
            Dao<Article,Integer> articleDao = dbHandler.getArticleDao();
            try{
                User user = userDao.queryForId(userId);
                List<Article> articles = new ArrayList<Article>();
                articles = articleDao.queryForEq("author_id",user.getId());
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

            return renderer.render(new ModelAndView(attributes, BASE_LAYOUT));
        });
        // -------------------------------- FINISH USER CRUD -------------------------------------------------------------- //
    }

    private static ModelAndView articleAddEdit(Request request, Response response,Article article, boolean is_edit){
        if(article == null){
            article = new Article();
        }

        DatabaseHandler dbHandler = DatabaseHandler.getInstance();

        Map<String,Object> attributes = request.attribute(MODEL_PARAM);
        //get fields:
        String articleTitle = request.queryParams("article_title");
        String articleBody = request.queryParams("article_body");
        List<String> articleTags = Arrays.asList(request.queryParams("article_tags").split("\\s*(,|\\s)\\s*"));

        //Prepare errors:
        ArrayList<String> errors = new ArrayList<String>();


        ConnectionSource conn = dbHandler.getConnection();
        Dao<Article,Integer> articleDao = dbHandler.getArticleDao();
        Dao<Tag,Integer> tagDao = dbHandler.getTagDao();
        Dao<ArticleTag,ID> articleTagDao = dbHandler.getArticleTagDao();
        try {
            if (articleTitle == null || articleTitle.equals("") ) {
                errors.add("No es posible dejar el campo de título vacio.");
            }
            if (articleBody == null || articleBody.equals("")) {
                errors.add("No es posible dejar el campo de cuerpo vacio.");
            }

            if (errors.size() == 0) {
                User user = request.session().attribute("user");
                Date publishedDate = new Date();
                if(!is_edit){
                    article = new Article(articleTitle, articleBody, publishedDate, user);
                }

                if (is_edit ? articleDao.update(article) >= 0 : articleDao.create(article) == 1) {
                    for (String tagTitle : articleTags) {
                        //First, check if tag exist:
                        List<Tag> tags = tagDao.queryForEq("description",tagTitle);
                        Tag tag = null;
                        if(tags.size() != 0) {
                            //The tag exists
                            tag = tags.get(0);
                        }
                        else{
                            tag = new Tag(tagTitle, article);
                            tagDao.create(tag);
                        }

                        Map<String,Object> fieldValues = new HashMap<>();
                        fieldValues.put("article",article);
                        fieldValues.put("tag",tag);

                        if(is_edit && articleTagDao.queryForFieldValues(fieldValues).size() != 0)
                            continue;

                        ArticleTag articleTag = new ArticleTag(article,tag);
                        articleTagDao.create(articleTag);
                    }

                    if(is_edit && article.getArticleTags() != null){
                        //Delete removed tags:
                        for(ArticleTag articleTag : article.getArticleTags()){
                            if(!articleTags.contains(articleTag.getTag().getDescription()))
                                articleTagDao.delete(articleTag);
                        }
                    }

                    response.redirect("/");
                } else {
                    errors.add("ERROR EN BASE DE DATOS");
                    attributes.put("errors", errors);
                    attributes.put("article",article);
                    attributes.put("template_name","./articles/add_edit.ftl");
                    return new ModelAndView(attributes, BASE_LAYOUT);
                }
            } else {

                conn.close();
                article.setTitle(articleTitle);
                article.setBody(articleBody);
                attributes.put("article",article);
                attributes.put("tags",request.queryParams("article_tags"));
                attributes.put("errors", errors);
                attributes.put("template_name","./articles/add_edit.ftl");
                if(is_edit) {
                    attributes.put("is_edit",true);
                }
                return new ModelAndView(attributes, BASE_LAYOUT);
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println(e.getClass());
            e.printStackTrace();
        }

        return null;

    }

}
