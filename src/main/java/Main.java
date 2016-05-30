import spark.Spark;

/**
 * Created by luis on 5/30/16.
 */

import spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        Spark.staticFiles.location("/public");

    }
}
