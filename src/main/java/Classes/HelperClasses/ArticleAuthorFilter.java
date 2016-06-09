package Classes.HelperClasses;

import Classes.Main;
import Classes.jpaIntegration.Article;
import Classes.jpaIntegration.User;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import spark.*;

import java.sql.SQLException;
import java.util.Map;

/**
 * Created by luis on 6/1/16.
 */

public class ArticleAuthorFilter extends CustomFilter {

    boolean adminCanModify = true;
    Article article;

    public ArticleAuthorFilter(TemplateEngine templateEngine,boolean adminCanModify){

        super(templateEngine);
        this.adminCanModify = adminCanModify;
    }

    @Override
    public void handle(Request request, Response response) throws Exception {

        ArticleHandler articleHandler = ArticleHandler.getInstance();

        try{

            int articleId = Integer.parseInt(request.params("id"));
            article = articleHandler.findObjectWithId(articleId);
            User user = request.session().attribute("user");
            Map<String,Object> attributes = request.attribute(Main.MODEL_PARAM);

            attributes.put("template_name",this.forbiddenTemplate);

            if(user == null){
                attributes.put("forbidden_message","Usted no ha iniciado sesion.");
                Spark.halt(401,templateEngine.render(new ModelAndView(attributes,Main.BASE_LAYOUT)));
            }
            if(!article.getAuthor().getUsername().equals(user.getUsername()) ||
                    (!article.getAuthor().getUsername().equals(user.getUsername()) && !user.getAuthor()) ||
                    (adminCanModify && user.getAdministrator())){
                attributes.put("forbidden_message","Este articulo no le pertenece.");
                Spark.halt(401,templateEngine.render(new ModelAndView(attributes,Main.BASE_LAYOUT)));
            }

        }
        catch (NumberFormatException e){
            Main.redirectWrongAddress(request,response);
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }



    }
}
