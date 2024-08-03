package info.journeymap.shaded.kotlin.spark.embeddedserver.jetty;

import info.journeymap.shaded.kotlin.spark.embeddedserver.EmbeddedServer;
import info.journeymap.shaded.kotlin.spark.embeddedserver.EmbeddedServerFactory;
import info.journeymap.shaded.kotlin.spark.http.matching.MatcherFilter;
import info.journeymap.shaded.kotlin.spark.route.Routes;
import info.journeymap.shaded.kotlin.spark.staticfiles.StaticFilesConfiguration;

public class EmbeddedJettyFactory implements EmbeddedServerFactory {

    private final JettyServerFactory serverFactory;

    public EmbeddedJettyFactory() {
        this.serverFactory = JettyServer::create;
    }

    public EmbeddedJettyFactory(JettyServerFactory serverFactory) {
        this.serverFactory = serverFactory;
    }

    @Override
    public EmbeddedServer create(Routes routeMatcher, StaticFilesConfiguration staticFilesConfiguration, boolean hasMultipleHandler) {
        MatcherFilter matcherFilter = new MatcherFilter(routeMatcher, staticFilesConfiguration, false, hasMultipleHandler);
        matcherFilter.init(null);
        JettyHandler handler = new JettyHandler(matcherFilter);
        return new EmbeddedJettyServer(this.serverFactory, handler);
    }
}