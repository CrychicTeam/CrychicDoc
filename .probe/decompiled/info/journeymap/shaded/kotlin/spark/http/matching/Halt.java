package info.journeymap.shaded.kotlin.spark.http.matching;

import info.journeymap.shaded.kotlin.spark.HaltException;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletResponse;

public class Halt {

    public static void modify(HttpServletResponse httpResponse, Body body, HaltException halt) {
        httpResponse.setStatus(halt.statusCode());
        if (halt.body() != null) {
            body.set(halt.body());
        } else {
            body.set("");
        }
    }
}