package info.journeymap.shaded.kotlin.spark.embeddedserver.jetty;

import info.journeymap.shaded.org.eclipse.jetty.server.Server;
import info.journeymap.shaded.org.eclipse.jetty.util.thread.QueuedThreadPool;

class JettyServer {

    public static Server create(int maxThreads, int minThreads, int threadTimeoutMillis) {
        Server server;
        if (maxThreads > 0) {
            int max = maxThreads > 0 ? maxThreads : 200;
            int min = minThreads > 0 ? minThreads : 8;
            int idleTimeout = threadTimeoutMillis > 0 ? threadTimeoutMillis : '\uea60';
            server = new Server(new QueuedThreadPool(max, min, idleTimeout));
        } else {
            server = new Server();
        }
        return server;
    }
}