package Classes;

import static spark.Spark.*;

import Classes.HelperClasses.DatabaseHandler;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

import javax.xml.crypto.Data;
import java.sql.Connection;

/**
 * Created by luis on 5/30/16.
 */
public class Main {
    public static void main(String[] args) {
        staticFiles.location("/public");

        get("/",(request,response) -> {
            return new ModelAndView(null,"header_footer_layout.ftl");
        },new FreeMarkerEngine());
    }
}
