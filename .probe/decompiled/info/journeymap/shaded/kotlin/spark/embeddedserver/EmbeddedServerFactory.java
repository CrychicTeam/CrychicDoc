package info.journeymap.shaded.kotlin.spark.embeddedserver;

import info.journeymap.shaded.kotlin.spark.route.Routes;
import info.journeymap.shaded.kotlin.spark.staticfiles.StaticFilesConfiguration;

public interface EmbeddedServerFactory {

    EmbeddedServer create(Routes var1, StaticFilesConfiguration var2, boolean var3);
}