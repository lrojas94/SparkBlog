package Classes;

import static spark.Spark.*;

import Classes.HelperClasses.DatabaseHandler;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by luis on 5/30/16.
 */
public class Main {
    public static void main(String[] args) {
        staticFiles.location("/public");

        get("/",(request,response) -> {
            Map<String,Object> attributes = new HashMap<String, Object>();
            attributes.put("template_name","./main/index.ftl");
            return new ModelAndView(attributes,"header_footer_layout.ftl");
        },new FreeMarkerEngine());
    }
}
