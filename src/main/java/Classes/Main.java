package Classes;

import static spark.Spark.*;
import static spark.debug.DebugScreen.enableDebugScreen;

import Classes.HelperClasses.*;
import Classes.JsonClasses.*;
import Classes.JsonClasses.Comment;
import Classes.data.*;
import Classes.data.Article;
import Classes.data.Tag;
import Classes.data.User;
import Classes.jpaIntegration.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.query.In;
import com.j256.ormlite.support.ConnectionSource;
import com.sun.xml.internal.bind.v2.model.core.ID;
import org.hibernate.Hibernate;
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

    public static void redirectWrongAddress(Request request,Response response){
        request.session(true).attribute("message_type","danger");
        request.session(true).attribute("message","Direccion no encontrada.");

        response.redirect("/");
    }

    public static void main(String[] args) throws Exception {

        staticFiles.location("/public");
        // Remove on production
        enableDebugScreen();

        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        TemplateEngine renderer = new FreeMarkerEngine();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.excludeFieldsWithoutExposeAnnotation().create();

        Preferences userPrefs = Preferences.userRoot();
//        userPrefs.putBoolean("first_run", true);
        Boolean isFirstRun = userPrefs.getBoolean("first_run", true);

        if (isFirstRun) {
            System.out.println("running for the first time");
            dbHandler.getConnection();
//            dbHandler.createAllTables();
            Classes.jpaIntegration.User firstUser = new Classes.jpaIntegration.User("admin", "Administrator", "admin", true, true);
            UserHandler userHandler = UserHandler.getInstance();
            userHandler.insertIntoDatabase(firstUser);
            userPrefs.putBoolean("first_run", false);
        }

        get("/logout", (request, response) -> {
            request.session(true).attribute("user",null);
            request.session(true).attribute("logged_in",false);
            response.redirect("/");
            return "";
        });

        before((request, response) -> {
            //Add base model to everything:
            Map<String,Object> attributes = new HashMap<String, Object>();
            attributes.put("logged_in",request.session(true).attribute("user") != null);
            attributes.put("user",request.session(true).attribute("user"));

            if(request.session().attribute("message_type") != null){ //Redirect messages

                attributes.put("message_type",request.session().attribute("message_type"));
                attributes.put("message",request.session().attribute("message"));

            }

            request.session().attribute("message_type",null);
            request.session().attribute("message",null);

            request.attribute(MODEL_PARAM,attributes);
        });

        get("/",(request,response) -> {
            ArticleHandler articleHandler = ArticleHandler.getInstance();
            List<Classes.jpaIntegration.Article> articles = articleHandler.getAllObjects();
            Map<String,Object> attributes = request.attribute(MODEL_PARAM);
            attributes.put("template_name","./main/index.ftl");
            attributes.put("articles", articles);


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
            String  username = request.queryParams("username"),
                    fullname = request.queryParams("fullname"),
                    password = request.queryParams("password"),
                    password2 = request.queryParams("password2");
            //Prepare errors:
            ArrayList<String> errors = new ArrayList<String>();

            //Check if username exists:
            UserHandler userHandler = UserHandler.getInstance();

            try {
                attributes.put("username",username);
                attributes.put("fullname",fullname);
                if(userHandler.findUserByUsername(username) != null){
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

                    Classes.jpaIntegration.User user = new Classes.jpaIntegration.User();
                    user.setUsername(username);
                    user.setName(fullname);
                    user.setPassword(password);
                    user.setAdministrator(false);
                    user.setAuthor(false);

                    userHandler.insertIntoDatabase(user);
                    request.session(true).attribute("user",user);
                    response.redirect("/");
                }
                else{
                    attributes.put("errors",errors);
                    attributes.put("template_name","./users/add.ftl");
                    return renderer.render(new ModelAndView(attributes, BASE_LAYOUT));
                }

            }catch (Exception e) {
                System.out.println(e.getMessage());
            }

            return null;
        });

        //--------------------------- ARTICLE CRUD START ----------------------------------------//

        get("/article/view/:id",(request, response) -> {
            Map<String, Object> attributes = request.attribute(MODEL_PARAM);
            attributes.put("template_name","./articles/view.ftl");
            try{
                int id = Integer.parseInt(request.params("id"));
                ArticleHandler articleHandler = ArticleHandler.getInstance();
                Classes.jpaIntegration.Article article = articleHandler.findObjectWithId(id);

                if(article  == null){
                    request.session(true).attribute("message_type","danger");
                    request.session(true).attribute("message","Articulo no encontrado.");
                    response.redirect("/");
                    return null;
                }

                attributes.put("article",article);

                return renderer.render(new ModelAndView(attributes,BASE_LAYOUT));
            }catch (Exception e){
                if(e instanceof NumberFormatException){
                    redirectWrongAddress(request,response);
                }
            }
            return  "";
        });

        before("/article/add",new AuthFilter(renderer, new HashSet<AuthRoles>() {{
            add(AuthRoles.AUTHOR);
        }}));

        get("/article/add", (request, response) -> {
            Map<String, Object> attributes = request.attribute(MODEL_PARAM);
            attributes.put("template_name","./articles/add_edit.ftl");
            return renderer.render(new ModelAndView(attributes, BASE_LAYOUT));
        });

        post("/article/add", (request, response) -> {
            ModelAndView mav = articleAddEdit(request,response,null,false);
            if(mav != null)
                return renderer.render(mav);
            else
                return "";

        });

        before("/article/edit/:id",new ArticleAuthorFilter(renderer,true));

        get("/article/edit/:id",(request, response) -> {
            Map<String,Object> attributes = request.attribute(MODEL_PARAM);
            ConnectionSource conn = dbHandler.getConnection();
            try{

                int id = Integer.parseInt(request.params("id"));
                ArticleHandler articleHandler = ArticleHandler.getInstance();
                Classes.jpaIntegration.Article article = articleHandler.findObjectWithId(id);

                if(article == null){
                    request.session(true).attribute("message_type","danger");
                    request.session(true).attribute("message","Articulo no encontrado.");
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
                    redirectWrongAddress(request,response);
                }
            }finally {
                conn.close();
            }
            return "";
        });

        post("/article/edit/:id",(request, response) -> {
            ConnectionSource conn = dbHandler.getConnection();
            try {

                int id = Integer.parseInt(request.params("id"));
                ArticleHandler articleHandler = ArticleHandler.getInstance();
                Classes.jpaIntegration.Article article = articleHandler.findObjectWithId(id);

                if (article == null) {
                    request.session(true).attribute("message_type", "danger");
                    request.session(true).attribute("message", "Articulo no encontrado.");
                    response.redirect("/");
                }
                ModelAndView mav = (articleAddEdit(request,response,article,true));
                if(mav != null)
                    return renderer.render(mav);
                else
                    return "";

            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }

            return "";
        });

        before("/article/delete/:id",new ArticleAuthorFilter(renderer,true));

        get("/article/delete/:id",(request, response) -> {
           int id = Integer.parseInt(request.params("id"));
            ArticleHandler articleHandler = ArticleHandler.getInstance();
            articleHandler.deleteObject(articleHandler.findObjectWithId(id));
            //Delete by id:
            request.session(true).attribute("message_type","success");
            request.session(true).attribute("message","Articulo borrado correctamente");
            System.out.println(request.headers("Referer"));
            response.redirect(request.headers("Referer"));

            return "";
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
                UserHandler userHandler = UserHandler.getInstance();
                Classes.jpaIntegration.User user = userHandler.loginUser(username,password);
                if(user!= null){
                    //Create session:
                    request.session(true).attribute("user",user);
                }
                else {
                    request.session(true).attribute("message_type","danger");
                    request.session(true).attribute("message","Usuario No Encontrado");
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
            UserHandler userHandler = UserHandler.getInstance();
            Classes.jpaIntegration.User user = userHandler.findObjectWithId(userId);
            try{
                attributes.put("User",user);
                attributes.put("articles",user.getArticles());

            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }

            attributes.put("template_name","./users/show.ftl");

            return renderer.render(new ModelAndView(attributes, BASE_LAYOUT));
        });

        before("/user/delete/*", new AuthFilter(renderer,new HashSet<AuthRoles>(){{
            add(AuthRoles.ADMIN);
        }}));

        get("/user/delete/:id",(request, response) -> {
            int id = Integer.parseInt(request.params("id"));
            UserHandler userHandler = UserHandler.getInstance();
            Classes.jpaIntegration.User toDelete = userHandler.findObjectWithId(id);

            if(toDelete != null){
                userHandler.deleteObject(toDelete);
                request.session(true).attribute("message_type","success");
                request.session(true).attribute("message","Borrado exitoso");
                response.redirect("/admin");
            }
            else{
                request.session(true).attribute("message_type","info");
                request.session(true).attribute("message","Borrado no exitoso");
                response.redirect("/admin");
            }

            return "";

        });
        // -------------------------------- FINISH USER CRUD -------------------------------------------------------------- //
        // -------------------------------- COMMENTS CRUD ---------------------------------------------------
        post("/comment/add","application/json",(request, response) -> {
            Map<String,Object> attributes = request.attribute(MODEL_PARAM);
            ConnectionSource conn = null;
            Classes.JsonClasses.Comment jsonComment = gson.fromJson(request.body(), Comment.class);
            Dao<Article,Integer> articlesDao = dbHandler.getArticleDao();
            Dao<Classes.data.Comment,Integer> commentDao = dbHandler.getCommentDao();
            //Add comment now:
            Article article = articlesDao.queryForId(jsonComment.getArticleId());
            User user = request.session().attribute("user");
            ActionStatus status = new ActionStatus();

            //Set respose headers:

            if(user == null){
                status.setStatus("error");
                status.getErrors().add("Usted no ha inciado sesion.");
                return status;

            }

            if(article == null){
                status.setStatus("error");
                status.getErrors().add("Articulo no encontrado");

                return status;
            }

            if(jsonComment.getComment().length() <= 10){
                status.setStatus("error");
                status.getErrors().add("Comentario muy corto");

                return status;
            }

            Classes.data.Comment comment = new Classes.data.Comment(jsonComment.getComment(),user,article);

            if(commentDao.create(comment) == 1){
                //Status ok:
                status.setStatus("success");
                status.setReturnObject(comment);
                return status;
            }
            else{
                status.setStatus("error");
                status.getErrors().add("Error insertando en la base de datos.");
                return status;
            }
        },gson::toJson);

        post("/comment/delete/:id",(request, response) -> {
            int id = Integer.parseInt(request.params("id"));


            ConnectionSource conn = dbHandler.getConnection();
            Dao<Classes.data.Comment,Integer> commentDao = dbHandler.getCommentDao();
            Classes.data.Comment comment = commentDao.queryForId(id);
            if(commentDao.delete(comment) == 1){
                ActionStatus status = new ActionStatus();
                status.setStatus("success");
                status.setReturnObject(comment);
                return status;
            }
            else{
                ActionStatus status = new ActionStatus();
                status.setStatus("error");
                return  status;
            }
        },gson::toJson);

        get("/tags/:tag", (request, response) -> {
            Map<String, Object> attributes = request.attribute(MODEL_PARAM);
            int tagId = Integer.parseInt(request.params(":tag"));

            ConnectionSource conn = dbHandler.getConnection();
            Dao<Tag, Integer> tagDao = dbHandler.getTagDao();
            Dao<Article, Integer> articleDao = dbHandler.getArticleDao();
            Tag tagToLook = null;
            List<Article> articlesFromTag = null;
            try {
                tagToLook = tagDao.queryForId(tagId);
                articlesFromTag = dbHandler.lookupArticlesForTag(tagToLook);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conn.close();
            }

            attributes.put("articles", articlesFromTag);
            attributes.put("tag", tagToLook);
            attributes.put("template_name", "./tags/show.ftl");
            return renderer.render(new ModelAndView(attributes, BASE_LAYOUT));
        });

        before("/admin",new AuthFilter(renderer,new HashSet<AuthRoles>(){{
            add(AuthRoles.ADMIN);
        }}));

        get("/admin",(request, response) -> {
            Map<String,Object> attributes = request.attribute(MODEL_PARAM);
            attributes.put("template_name","./admin/show.ftl");
            UserHandler userHandler = UserHandler.getInstance();
            List<Classes.jpaIntegration.User> users = userHandler.getAllObjects();
            attributes.put("users",users);

            return renderer.render(new ModelAndView(attributes,Main.BASE_LAYOUT));
        });

        post("/admin",(request, response) -> {
            int id = Integer.parseInt(request.queryParams("userId"));

            try{
                UserHandler userHandler = UserHandler.getInstance();
                Classes.jpaIntegration.User user = userHandler.findObjectWithId(id);
                String admin = request.queryParams("admin");
                String autor = request.queryParams("author");
                user.setAdministrator(admin != null && admin.equals("on"));
                user.setAuthor(autor != null && autor.equals("on"));
                userHandler.updateObject(user);
                request.session(true).attribute("message_type","success");
                request.session(true).attribute("message","Actualizacion completada");
                response.redirect("/admin");

            }catch (Exception e){
                System.out.println(e.getMessage());
            }


            return "";

        });
    }

    private static ModelAndView articleAddEdit(Request request, Response response, Classes.jpaIntegration.Article article, boolean is_edit){
        if(article == null){
            article = new Classes.jpaIntegration.Article();
        }
        ArticleHandler articleHandler = ArticleHandler.getInstance();
        TagHandler tagHandler = TagHandler.getInstance();

        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        Map<String,Object> attributes = request.attribute(MODEL_PARAM);
        //get fields:
        String articleTitle = request.queryParams("article_title");
        String articleBody = request.queryParams("article_body");
        List<String> articleTags = Arrays.asList(request.queryParams("article_tags").split("\\s*(,|\\s)\\s*"));

        //Prepare errors:
        ArrayList<String> errors = new ArrayList<String>();

        try {
            Classes.jpaIntegration.Article sameArticle = articleHandler.findArticleByTitle(articleTitle);
            if ((!is_edit && sameArticle != null ) ||
                    (sameArticle != null && is_edit && article.getTitle() != null && sameArticle.getId() != article.getId())) {
                // The article already exists
                errors.add("Lo sentimos, pero este artículo ya existe.");
            }

            if (articleTitle == null || articleTitle.equals("") ) {
                errors.add("No es posible dejar el campo de título vacio.");
            }
            if (articleBody == null || articleBody.equals("")) {
                errors.add("No es posible dejar el campo de cuerpo vacio.");
            }

            if (errors.size() == 0) {
                Classes.jpaIntegration.User user = request.session().attribute("user");
                Date publishedDate = new Date();

                if(!is_edit){
                    article = new Classes.jpaIntegration.Article(articleTitle, articleBody, publishedDate, user);
                }
                else{
                    article.setTitle(articleTitle);
                    article.setBody(articleBody);
                }
                //PERSIST.
                if(is_edit){
                    articleHandler.updateObject(article);
                }
                else{
                    articleHandler.insertIntoDatabase(article);
                }

                for (String tagTitle : articleTags) {
                    //First, check if tag exist:
                    Classes.jpaIntegration.Tag existingTag = tagHandler.findByDescription(tagTitle);
                    Classes.jpaIntegration.Tag tag = null;
                    if(existingTag != null) {
                        //The tag exists
                        tag = existingTag;
                    }
                    else{
                        tag = new Classes.jpaIntegration.Tag(tagTitle);
                        tagHandler.insertIntoDatabase(tag);
                        tag.getArticles().add(article);
                        article.getTags().add(tag);
                        continue;
                    }

                    if(is_edit && tag.getArticles().contains(article))
                        continue;

                    article.getTags().add(tag);
                    tag.getArticles().add(article);
//                    tagHandler.updateObject(tag);




                }

                if(is_edit && article.getTags() != null){
                    //Delete removed tags:
                    List<Classes.jpaIntegration.Tag> removeTags = new ArrayList<Classes.jpaIntegration.Tag>();

                    for(Classes.jpaIntegration.Tag aTag : article.getTags()){
                        if(!articleTags.contains(aTag.getDescription())){
                            removeTags.add(aTag);
                        }
                    }

                    for(Classes.jpaIntegration.Tag aTag : removeTags){
                        aTag.getArticles().remove(article);
                        article.getTags().remove(aTag);
                    }
                }

                articleHandler.updateObject(article);

                request.session(true).attribute("message_type","success");

                if(is_edit)
                    request.session(true).attribute("message","Articulo editado satisfactoriamente");
                else
                    request.session(true).attribute("message","Articulo agregado satisfactoriamente");

                response.redirect("/article/view/" + article.getId());
            }
            else
            {
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
