package Classes.HelperClasses;

import Classes.data.User;
import com.j256.ormlite.dao.Dao;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Template;
import com.sun.xml.internal.bind.v2.model.core.ID;
import spark.*;
import spark.template.freemarker.FreeMarkerEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by luis on 6/1/16.
 */

public class AuthFilter implements Filter {

    private Set<AuthRoles> roles;
    private TemplateEngine templateEngine;

    public AuthFilter(TemplateEngine templateEngine){
        this.templateEngine = templateEngine;
    }

    public AuthFilter(TemplateEngine templateEngine, Set<AuthRoles> roles){
        this.roles = roles;
        this.templateEngine = templateEngine;
    }

    @Override
    public void handle(Request request, Response response) throws Exception {
        //First, check if logged in:
        User user = request.session().attribute("user");
        Map<String,Object> attributes = new HashMap<String,Object>();
        attributes.put("template_name","./forbidden.ftl");
        if(user == null){
            //Its not even logged in
            attributes.put("message","Usted no ha iniciado sesion.");
            spark.Spark.halt(401,templateEngine.render(new ModelAndView(attributes,"header_footer_layout.ftl")));
        }

        for(AuthRoles role : roles){
            switch (role){
                case AUTHOR:
                    if(!user.getAuthor()){
                        attributes.put("message","USTED NO ES UN AUTOR");
                        spark.Spark.halt(401,templateEngine.render(new ModelAndView(attributes,"header_footer_layout.ftl")));
                    }
                    break;
                case ADMIN:
                    if(!user.getAdministrator()){
                        attributes.put("message","USTED NO ES ADMINISTRADOR");
                        spark.Spark.halt(401,templateEngine.render(new ModelAndView(attributes,"header_footer_layout.ftl")));
                    }
                    break;
            }
        }
    }
}
