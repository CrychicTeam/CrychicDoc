package info.journeymap.shaded.kotlin.spark.globalstate;

import java.util.concurrent.atomic.AtomicBoolean;

public class ServletFlag {

    private static AtomicBoolean isRunningFromServlet = new AtomicBoolean(false);

    public static void runFromServlet() {
        isRunningFromServlet.set(true);
    }

    public static boolean isRunningFromServlet() {
        return isRunningFromServlet.get();
    }
}