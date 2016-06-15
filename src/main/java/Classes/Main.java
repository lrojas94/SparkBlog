package Classes;

import static spark.Spark.*;
import static spark.debug.DebugScreen.enableDebugScreen;

import Classes.DataTables.ReturnData;
import Classes.DataTables.SentParameters;
import Classes.HelperClasses.*;
import Classes.JsonClasses.*;
import Classes.JsonClasses.Comment;
import Classes.jpaIntegration.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.j256.ormlite.support.ConnectionSource;
import org.eclipse.jetty.websocket.api.Session;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateEngine;
import spark.template.freemarker.FreeMarkerEngine;

import java.io.IOException;
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
        // userPrefs.putBoolean("first_run", true);
        Boolean isFirstRun = userPrefs.getBoolean("first_run", true);

        if (isFirstRun) {
            System.out.println("running for the first time");
            Classes.jpaIntegration.User firstUser = new Classes.jpaIntegration.User("admin", "Administrator", "admin", true, true);
            UserHandler userHandler = UserHandler.getInstance();
            userHandler.insertIntoDatabase(firstUser);
            userPrefs.putBoolean("first_run", false);
        }

        webSocket("/chatRoom", WebSocketHandler.class);

        get("/chatRoom", (request, response) -> {
            Map<String,Object> attributes = request.attribute(MODEL_PARAM);
            attributes.put("template_name","./chat/chat_room.ftl");
            return renderer.render(new ModelAndView(attributes, BASE_LAYOUT));
        });

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

        after((request, response) -> {
            //Update user after requests:
            if(request.session(true).attribute("user") != null){
                //update user:
                Classes.jpaIntegration.User currentUser = request.session().attribute("user");
                UserHandler userHandler = UserHandler.getInstance();
                Classes.jpaIntegration.User user = userHandler.findObjectWithId(currentUser.getId());
                request.session(true).attribute("user",user);
            }
        });

        get("/",(request,response) -> {
            ArticleHandler articleHandler = ArticleHandler.getInstance();
            List<Classes.jpaIntegration.Article> articles = articleHandler.findArticlesInDescOrder();
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
        post("/datatable/articles",(request, response) -> {
            try{
                System.out.println("PARAMETERS HERE o/");
                SentParameters dtParameters = gson.fromJson(request.body(),SentParameters.class);
                ArticleHandler articleHandler = ArticleHandler.getInstance();
                ReturnData dtReturnData = new ReturnData();
                List<Article> articles = null;
                if(!dtParameters.getTag().equals("")){
                    int tag = Integer.parseInt(dtParameters.getTag());
                    articles = articleHandler.findArticlesByTag(tag,dtParameters.getLength(),dtParameters.getStart());
                    dtReturnData.setRecordsTotal(articleHandler.articleTagCount(tag));
                }
                else{
                    articles = articleHandler.findArticlesWithLimit(dtParameters.getLength(),dtParameters.getStart());
                    dtReturnData.setRecordsTotal(articleHandler.articleCount());
                }
                dtReturnData.setData(articles.toArray());
                dtReturnData.setDraw(dtParameters.getDraw());
                dtReturnData.setRecordsFiltered(dtReturnData.getRecordsTotal());
                dtReturnData.setError(null);
                return dtReturnData;
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return "";
        },gson::toJson);

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
            Article article = articleHandler.findObjectWithId(id);
            articleHandler.deleteObjectWithId(article.getId());
            //Delete by id:
            request.session(true).attribute("message_type","success");
            request.session(true).attribute("message","Articulo borrado correctamente");
            System.out.println(request.headers("Referer"));
            response.redirect(request.headers("Referer"));

            return "";
        });

        post("/article/preference",(request, response) -> {
            UserPreference userPreference = gson.fromJson(request.body(),UserPreference.class);
            ArticleHandler articleHandler = ArticleHandler.getInstance();
            UserHandler userHandler = UserHandler.getInstance();
            ArticlePreferenceHandler articlePreferenceHandler = ArticlePreferenceHandler.getInstance();

            ActionStatus status = new ActionStatus();
            if(!userPreference.isArticle()){
                status.setStatus("error");
                status.getErrors().add("Esta ruta solo trabaja con articulos.");
                return status;
            }

            Classes.jpaIntegration.User user = userHandler.findObjectWithId(userPreference.getUserId());
            Classes.jpaIntegration.Article article = articleHandler.findObjectWithId(userPreference.getPreferenceId());
            ArticlePreference articlePreference = articlePreferenceHandler.findByUserArticle(user.getId(),article.getId());
            if(articlePreference != null){
                //Exists
                articlePreference.setPreference(userPreference.getPreference());
                articlePreferenceHandler.updateObject(articlePreference);
            }
            else{
                //Create new
                articlePreference = new ArticlePreference();
                articlePreference.setPreference(userPreference.getPreference());
                articlePreference.setArticle(article);
                articlePreference.setAuthor(user);
                article.getArticlePreferences().add(articlePreference);
                user.getArticlePreferences().add(articlePreference);
                articlePreferenceHandler.insertIntoDatabase(articlePreference);

            }

            status.setStatus("success");
            HashMap<String,Object> returnData = new HashMap<String, Object>();
            returnData.put("likesCount",article.getLikes());
            returnData.put("dislikesCount",article.getDislikes());
            status.setReturnObject(returnData);

            return status;
        },gson::toJson);

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
                userHandler.deleteObjectWithId(toDelete);
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
            Comment jsonComment = gson.fromJson(request.body(), Comment.class);

            ArticleHandler articleHandler = ArticleHandler.getInstance();
            CommentHandler commentHandler = CommentHandler.getInstance();

            //Add comment now:
            Classes.jpaIntegration.Article article = articleHandler.findObjectWithId(jsonComment.getArticleId());
            Classes.jpaIntegration.User user = request.session().attribute("user");
            ActionStatus status = new ActionStatus();

            //Set response headers:

            if(user == null){
                status.setStatus("error");
                status.getErrors().add("Usted no ha iniciado sesion.");

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

            Classes.jpaIntegration.Comment comment = new Classes.jpaIntegration.Comment(jsonComment.getComment(),user,article);

            if(commentHandler.insertIntoDatabase(comment)){
                //Status ok:
                status.setStatus("success");
                status.setReturnObject(comment);

                return status;
            } else {
                status.setStatus("error");
                status.getErrors().add("Error insertando en la base de datos.");

                return status;
            }
        }, gson::toJson);

        post("/comment/delete/:id",(request, response) -> {
            int id = Integer.parseInt(request.params("id"));

            CommentHandler commentHandler = CommentHandler.getInstance();

            Classes.jpaIntegration.Comment comment = commentHandler.findObjectWithId(id);

            if(commentHandler.deleteObjectWithId(id)) {
                ActionStatus status = new ActionStatus();
                status.setStatus("success");
                status.setReturnObject(comment);
                return status;
            } else {
                ActionStatus status = new ActionStatus();
                status.setStatus("error");
                return  status;
            }
        },gson::toJson);

        post("/comment/preference",(request, response) -> {
            UserPreference userPreference = gson.fromJson(request.body(),UserPreference.class);
            CommentHandler commentHandler = CommentHandler.getInstance();
            UserHandler userHandler = UserHandler.getInstance();
            CommentPreferenceHandler commentPreferenceHandler = CommentPreferenceHandler.getInstance();

            ActionStatus status = new ActionStatus();
            if(userPreference.isArticle()){
                status.setStatus("error");
                status.getErrors().add("Esta ruta solo trabaja con comentarios.");
                return status;
            }

            Classes.jpaIntegration.User user = userHandler.findObjectWithId(userPreference.getUserId());
            Classes.jpaIntegration.Comment comment = commentHandler.findObjectWithId(userPreference.getPreferenceId());
            CommentPreference commentPreference = commentPreferenceHandler.findByUserComment(user.getId(),comment.getId());
            if(commentPreference != null){
                //Exists
                commentPreference.setPreference(userPreference.getPreference());
                commentPreferenceHandler.updateObject(commentPreference);
            }
            else{
                //Create new
                commentPreference = new CommentPreference();
                commentPreference.setPreference(userPreference.getPreference());
                commentPreference.setComment(comment);
                commentPreference.setAuthor(user);
                comment.getCommentPreferenceSet().add(commentPreference);
                user.getCommentPreferences().add(commentPreference);
                commentPreferenceHandler.insertIntoDatabase(commentPreference);

            }

            status.setStatus("success");
            HashMap<String,Object> returnData = new HashMap<String, Object>();
            returnData.put("likesCount",comment.getLikes());
            returnData.put("dislikesCount",comment.getDislikes());
            status.setReturnObject(returnData);

            return status;
        },gson::toJson);
        // ---------------- END OF COMMENT CRUD -----------------------------------------

        get("/tags/:tag", (request, response) -> {
            Map<String, Object> attributes = request.attribute(MODEL_PARAM);
            int tagId = Integer.parseInt(request.params(":tag"));

            TagHandler tagHandler = TagHandler.getInstance();

            Classes.jpaIntegration.Tag tagToLook = null;
            Set<Classes.jpaIntegration.Article> articlesFromTag = null;
            try {
                tagToLook = tagHandler.findObjectWithId(tagId);
                articlesFromTag = tagToLook.getArticles();
            } catch (Exception e) {
                e.printStackTrace();
            }

            List<Classes.jpaIntegration.Article> orderedArticles = new ArrayList<>(articlesFromTag);
            orderedArticles.sort((o1, o2) -> o2.getDatePublished().compareTo(o1.getDatePublished()));

            attributes.put("articles", orderedArticles);
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
                } else {
                    article.setTitle(articleTitle);
                    article.setBody(articleBody);
                }
                //PERSIST.
                if(is_edit){
                    articleHandler.updateObject(article);
                } else {
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
