package info.journeymap.shaded.kotlin.spark.staticfiles;

import info.journeymap.shaded.kotlin.spark.utils.StringUtils;
import info.journeymap.shaded.org.slf4j.Logger;
import info.journeymap.shaded.org.slf4j.LoggerFactory;
import java.nio.file.Paths;

public class StaticFilesFolder {

    private static final Logger LOG = LoggerFactory.getLogger(StaticFilesFolder.class);

    private static volatile String local;

    private static volatile String external;

    public static final void localConfiguredTo(String folder) {
        local = StringUtils.removeLeadingAndTrailingSlashesFrom(folder);
    }

    public static final void externalConfiguredTo(String folder) {
        String unixLikeFolder = Paths.get(folder).toAbsolutePath().toString().replace("\\", "/");
        LOG.warn("Registering external static files folder [{}] as [{}].", folder, unixLikeFolder);
        external = StringUtils.removeLeadingAndTrailingSlashesFrom(unixLikeFolder);
    }

    public static final String local() {
        return local;
    }

    public static final String external() {
        return external;
    }
}