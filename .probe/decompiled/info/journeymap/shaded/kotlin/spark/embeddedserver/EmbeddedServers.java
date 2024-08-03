package info.journeymap.shaded.kotlin.spark.embeddedserver;

import info.journeymap.shaded.kotlin.spark.embeddedserver.jetty.EmbeddedJettyFactory;
import info.journeymap.shaded.kotlin.spark.route.Routes;
import info.journeymap.shaded.kotlin.spark.staticfiles.StaticFilesConfiguration;
import java.util.HashMap;
import java.util.Map;

public class EmbeddedServers {

    private static Map<Object, EmbeddedServerFactory> factories = new HashMap();

    public static void initialize() {
        if (!factories.containsKey(EmbeddedServers.Identifiers.JETTY)) {
            add(EmbeddedServers.Identifiers.JETTY, new EmbeddedJettyFactory());
        }
    }

    public static EmbeddedServers.Identifiers defaultIdentifier() {
        return EmbeddedServers.Identifiers.JETTY;
    }

    public static EmbeddedServer create(Object identifier, Routes routeMatcher, StaticFilesConfiguration staticFilesConfiguration, boolean multipleHandlers) {
        EmbeddedServerFactory factory = (EmbeddedServerFactory) factories.get(identifier);
        if (factory != null) {
            return factory.create(routeMatcher, staticFilesConfiguration, multipleHandlers);
        } else {
            throw new RuntimeException("No embedded server matching the identifier");
        }
    }

    public static void add(Object identifier, EmbeddedServerFactory factory) {
        factories.put(identifier, factory);
    }

    public static enum Identifiers {

        JETTY
    }
}