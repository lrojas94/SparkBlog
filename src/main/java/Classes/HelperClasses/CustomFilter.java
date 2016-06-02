package Classes.HelperClasses;

import spark.Filter;
import spark.Request;
import spark.Response;
import spark.TemplateEngine;

/**
 * Created by luis on 6/2/16.
 */
public class CustomFilter implements Filter{
    protected TemplateEngine templateEngine;
    protected final String forbiddenTemplate = "./forbidden.ftl";

    public CustomFilter(TemplateEngine templateEngine){
        this.templateEngine = templateEngine;
    }

    @Override
    public void handle(Request request, Response response) throws Exception {
        // let elements implement this.
    }
}
