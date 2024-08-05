package mezz.jei.common.config;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import mezz.jei.api.runtime.config.IJeiConfigFile;
import mezz.jei.api.runtime.config.IJeiConfigManager;
import org.jetbrains.annotations.Unmodifiable;

public class ConfigManager implements IJeiConfigManager {

    private final Map<Path, IJeiConfigFile> configFiles = new HashMap();

    public void registerConfigFile(IJeiConfigFile configFile) {
        this.configFiles.put(configFile.getPath(), configFile);
    }

    @Unmodifiable
    @Override
    public Collection<IJeiConfigFile> getConfigFiles() {
        return Collections.unmodifiableCollection(this.configFiles.values());
    }
}