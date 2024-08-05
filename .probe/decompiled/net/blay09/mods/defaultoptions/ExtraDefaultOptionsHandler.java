package net.blay09.mods.defaultoptions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import net.blay09.mods.defaultoptions.api.DefaultOptionsAPI;
import net.blay09.mods.defaultoptions.api.DefaultOptionsCategory;
import net.blay09.mods.defaultoptions.api.DefaultOptionsHandler;
import net.blay09.mods.defaultoptions.api.DefaultOptionsLoadStage;
import org.apache.commons.io.FileUtils;

public class ExtraDefaultOptionsHandler implements DefaultOptionsHandler {

    private File getCustomDefaultOptionsFolder() {
        File customDefaultOptionsFolder = new File(DefaultOptionsAPI.getDefaultOptionsFolder(), "extra");
        if (!customDefaultOptionsFolder.exists() && !customDefaultOptionsFolder.mkdirs()) {
            throw new IllegalStateException("Could not create default options extra directory.");
        } else {
            return customDefaultOptionsFolder;
        }
    }

    @Override
    public String getId() {
        return "extra-folder";
    }

    @Override
    public DefaultOptionsCategory getCategory() {
        return DefaultOptionsCategory.OPTIONS;
    }

    @Override
    public DefaultOptionsLoadStage getLoadStage() {
        return DefaultOptionsLoadStage.PRE_LOAD;
    }

    @Override
    public void saveCurrentOptions() {
    }

    @Override
    public void saveCurrentOptionsAsDefault() {
    }

    @Override
    public boolean hasDefaults() {
        File[] files = this.getCustomDefaultOptionsFolder().listFiles();
        return files != null && files.length > 0;
    }

    @Override
    public boolean shouldLoadDefaults() {
        return true;
    }

    @Override
    public void loadDefaults() throws DefaultOptionsHandlerException {
        Path defaultOptionsPath = this.getCustomDefaultOptionsFolder().toPath();
        try {
            for (Path path : Files.walk(defaultOptionsPath).toList()) {
                File defaultOptionsFile = path.toFile();
                if (defaultOptionsFile.isFile()) {
                    Path relativeDefaultOptionsPath = defaultOptionsPath.relativize(defaultOptionsFile.toPath());
                    File optionsFile = new File(DefaultOptions.getMinecraftDataDir(), relativeDefaultOptionsPath.toString());
                    if (!optionsFile.exists()) {
                        FileUtils.copyFile(defaultOptionsFile, optionsFile);
                    }
                }
            }
        } catch (IOException var8) {
            throw new DefaultOptionsHandlerException(this, var8);
        }
    }
}