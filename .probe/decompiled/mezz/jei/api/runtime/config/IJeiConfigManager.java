package mezz.jei.api.runtime.config;

import java.util.Collection;
import org.jetbrains.annotations.Unmodifiable;

public interface IJeiConfigManager {

    @Unmodifiable
    Collection<IJeiConfigFile> getConfigFiles();
}