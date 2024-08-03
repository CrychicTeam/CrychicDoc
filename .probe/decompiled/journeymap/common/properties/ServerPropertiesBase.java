package journeymap.common.properties;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import java.io.File;
import java.io.Serializable;
import java.util.List;
import journeymap.common.CommonConstants;
import journeymap.common.properties.catagory.Category;

public abstract class ServerPropertiesBase extends PropertiesBase implements Cloneable, Serializable {

    protected final String displayName;

    protected final String description;

    private boolean onClient = false;

    protected ServerPropertiesBase(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    @Override
    public String[] getHeaders() {
        return new String[] { "// JourneyMap server configuration file. Modify at your own risk!", "// To restore the default settings, simply delete this file before starting Minecraft server", "// For more information, go to: http://journeymap.info/JourneyMapServer", "//", String.format("// %s : %s ", this.displayName, this.description) };
    }

    @Override
    public <T extends PropertiesBase> void updateFrom(T otherInstance) {
        super.updateFrom(otherInstance);
    }

    public <T extends PropertiesBase> T load(String jsonString, boolean verbose) {
        this.ensureInit();
        try {
            T jsonInstance = this.fromJsonString(jsonString, this.getClass(), verbose);
            this.updateFrom(jsonInstance);
            this.postLoad(false);
            this.currentState = PropertiesBase.State.FileLoaded;
            return (T) (!this.onClient && !this.isValid(true) ? null : this);
        } catch (Exception var4) {
            this.error(String.format("Can't load JSON string: %s", jsonString), var4);
            return null;
        }
    }

    @Override
    public Category getCategoryByName(String name) {
        Category category = super.getCategoryByName(name);
        if (category == null) {
            category = ServerCategory.valueOf(name);
        }
        return category;
    }

    @Override
    public List<ExclusionStrategy> getExclusionStrategies(boolean verbose) {
        List<ExclusionStrategy> strategies = super.getExclusionStrategies(verbose);
        if (!verbose) {
            strategies.add(new ExclusionStrategy() {

                public boolean shouldSkipField(FieldAttributes f) {
                    return !f.getDeclaringClass().equals(ServerPropertiesBase.class) ? false : f.getName().equals("displayName") || f.getName().equals("description") || f.getName().equalsIgnoreCase("onClient");
                }

                public boolean shouldSkipClass(Class<?> clazz) {
                    return false;
                }
            });
        }
        return strategies;
    }

    @Override
    public boolean isValid(boolean fix) {
        return super.isValid(fix);
    }

    @Override
    public String getFileName() {
        return String.format("journeymap.server.%s.config", this.getName());
    }

    @Override
    public File getFile() {
        if (this.sourceFile == null) {
            this.sourceFile = new File(CommonConstants.getServerConfigDir(), this.getFileName());
        }
        return this.sourceFile;
    }

    @Override
    public boolean save() {
        return this.onClient ? true : super.save();
    }

    public <T extends PropertiesBase> T loadForClient(String jsonString, boolean verbose) {
        this.onClient = true;
        return this.load(jsonString, verbose);
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}