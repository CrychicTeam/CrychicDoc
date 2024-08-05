package journeymap.common.migrate;

import java.util.concurrent.Callable;
import journeymap.common.version.Version;

public interface MigrationTask extends Callable<Boolean> {

    boolean isActive(Version var1);
}