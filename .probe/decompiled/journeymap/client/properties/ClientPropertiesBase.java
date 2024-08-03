package journeymap.client.properties;

import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import journeymap.client.Constants;
import journeymap.client.io.FileHandler;
import journeymap.common.log.LogFormatter;
import journeymap.common.properties.PropertiesBase;
import journeymap.common.properties.catagory.Category;
import net.minecraft.client.Minecraft;

public abstract class ClientPropertiesBase extends PropertiesBase {

    private static final String[] HEADERS = new String[] { "// " + Constants.getString("jm.config.file_header_1"), "// " + Constants.getString("jm.config.file_header_2", Constants.CONFIG_DIR), "// " + Constants.getString("jm.config.file_header_5", "http://journeymap.info/Options_Manager") };

    @Override
    public String getFileName() {
        return String.format("journeymap.%s.config", this.getName());
    }

    @Override
    public File getFile() {
        if (this.sourceFile == null) {
            this.sourceFile = new File(FileHandler.getWorldConfigDir(false), this.getFileName());
            if (!this.sourceFile.canRead()) {
                this.sourceFile = new File(FileHandler.StandardConfigDirectory, this.getFileName());
            }
        }
        return this.sourceFile;
    }

    public boolean isWorldConfig() {
        if (Minecraft.getInstance() == null) {
            return false;
        } else {
            File worldConfigDir = FileHandler.getWorldConfigDir(false);
            return worldConfigDir != null && worldConfigDir.equals(this.getFile().getParentFile());
        }
    }

    @Override
    public <T extends PropertiesBase> void updateFrom(T otherInstance) {
        super.updateFrom(otherInstance);
    }

    public boolean copyToWorldConfig(boolean overwrite) {
        if (this.isWorldConfig()) {
            throw new IllegalStateException("Can't create World config from itself.");
        } else {
            try {
                File worldConfig = this.getFile();
                if (overwrite || !worldConfig.exists()) {
                    this.save();
                    Files.copy(this.sourceFile, worldConfig);
                    return worldConfig.canRead();
                }
            } catch (IOException var3) {
                this.error("Couldn't copy config to world config: " + var3, var3);
            }
            return false;
        }
    }

    @Override
    public boolean isValid(boolean fix) {
        return super.isValid(fix);
    }

    @Override
    public String[] getHeaders() {
        return HEADERS;
    }

    @Override
    public Category getCategoryByName(String name) {
        Category category = super.getCategoryByName(name);
        if (category == null) {
            category = ClientCategory.valueOf(name);
        }
        return category;
    }

    public boolean copyToStandardConfig() {
        if (this.isWorldConfig()) {
            try {
                this.save();
                File standardConfig = new File(FileHandler.StandardConfigDirectory, this.getFileName());
                Files.copy(this.sourceFile, standardConfig);
                return standardConfig.canRead();
            } catch (IOException var2) {
                this.error("Couldn't copy config to world config: " + LogFormatter.toString(var2));
                return false;
            }
        } else {
            throw new IllegalStateException("Can't replace standard config with itself.");
        }
    }
}