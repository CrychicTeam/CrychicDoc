package info.journeymap.shaded.kotlin.spark.embeddedserver.jetty;

import info.journeymap.shaded.org.eclipse.jetty.server.Server;

@FunctionalInterface
public interface JettyServerFactory {

    Server create(int var1, int var2, int var3);
}