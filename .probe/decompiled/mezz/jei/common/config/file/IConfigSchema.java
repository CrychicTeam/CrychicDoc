package mezz.jei.common.config.file;

import mezz.jei.api.runtime.config.IJeiConfigFile;
import mezz.jei.common.config.ConfigManager;

public interface IConfigSchema extends IJeiConfigFile {

    void register(FileWatcher var1, ConfigManager var2);

    void loadIfNeeded();

    void markDirty();
}