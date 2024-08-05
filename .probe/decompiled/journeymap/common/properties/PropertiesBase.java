package journeymap.common.properties;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.io.Files;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import journeymap.client.model.GridSpec;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;
import journeymap.common.properties.catagory.Category;
import journeymap.common.properties.catagory.CategorySet;
import journeymap.common.properties.config.BooleanField;
import journeymap.common.properties.config.ConfigField;
import journeymap.common.properties.config.CustomField;
import journeymap.common.properties.config.EnumField;
import journeymap.common.properties.config.FloatField;
import journeymap.common.properties.config.GsonHelper;
import journeymap.common.properties.config.IntegerField;
import journeymap.common.properties.config.StringField;
import journeymap.common.version.Version;

public abstract class PropertiesBase {

    protected static final Charset UTF8 = Charset.forName("UTF-8");

    protected Version configVersion = null;

    protected CategorySet categories = new CategorySet();

    protected transient File sourceFile = null;

    protected transient PropertiesBase.State currentState = PropertiesBase.State.New;

    protected transient Map<String, ConfigField<?>> configFields;

    protected PropertiesBase() {
    }

    public Gson getGson(boolean verbose) {
        GsonBuilder gb = new GsonBuilder().setPrettyPrinting().serializeNulls().registerTypeAdapter(FloatField.class, new GsonHelper.FloatFieldSerializer(verbose)).registerTypeAdapter(BooleanField.class, new GsonHelper.BooleanFieldSerializer(verbose)).registerTypeAdapter(IntegerField.class, new GsonHelper.IntegerFieldSerializer(verbose)).registerTypeAdapter(StringField.class, new GsonHelper.StringFieldSerializer(verbose)).registerTypeAdapter(CustomField.class, new GsonHelper.TextFieldSerializer(verbose)).registerTypeAdapter(EnumField.class, new GsonHelper.EnumFieldSerializer(verbose)).registerTypeAdapter(CategorySet.class, new GsonHelper.CategorySetSerializer(verbose)).registerTypeAdapter(Version.class, new GsonHelper.VersionSerializer(verbose)).registerTypeAdapter(GridSpec.class, new GsonHelper.GridSpecSerializer(verbose)).registerTypeAdapter(Map.class, new GsonHelper.MapFieldSerializer(verbose, true));
        List<ExclusionStrategy> exclusionStrategies = this.getExclusionStrategies(verbose);
        if (exclusionStrategies != null && !exclusionStrategies.isEmpty()) {
            gb.setExclusionStrategies((ExclusionStrategy[]) exclusionStrategies.toArray(new ExclusionStrategy[exclusionStrategies.size()]));
        }
        return gb.create();
    }

    public <T extends PropertiesBase> T fromJsonString(String jsonString, Class<T> propertiesClass, boolean verbose) {
        return (T) this.getGson(verbose).fromJson(jsonString, propertiesClass);
    }

    public abstract String getName();

    public abstract File getFile();

    public abstract String[] getHeaders();

    public abstract String getFileName();

    public boolean isCurrent() {
        return Journeymap.JM_VERSION.equals(this.configVersion);
    }

    public <T extends PropertiesBase> T load() {
        return this.load(false);
    }

    public <T extends PropertiesBase> T load(boolean verbose) {
        return this.load(this.getFile(), verbose);
    }

    public <T extends PropertiesBase> T load(File configFile, boolean verbose) {
        this.ensureInit();
        boolean saveNeeded = false;
        if (configFile.canRead() && configFile.length() != 0L) {
            try {
                String jsonString = Files.toString(configFile, UTF8);
                T jsonInstance = this.fromJsonString(jsonString, this.getClass(), verbose);
                this.updateFrom(jsonInstance);
                this.postLoad(false);
                this.currentState = PropertiesBase.State.FileLoaded;
                saveNeeded = !this.isValid(false);
            } catch (Exception var7) {
                this.error(String.format("Can't load config file %s", configFile), var7);
                try {
                    File badPropFile = new File(configFile.getParentFile(), configFile.getName() + ".bad");
                    configFile.renameTo(badPropFile);
                } catch (Exception var6) {
                    this.error(String.format("Can't rename config file %s: %s", configFile, var6.getMessage()));
                }
            }
        } else {
            this.postLoad(true);
            this.currentState = PropertiesBase.State.FirstLoaded;
            saveNeeded = true;
        }
        if (saveNeeded) {
            this.save(configFile, verbose);
        }
        return (T) this;
    }

    protected void postLoad(boolean isNew) {
        this.ensureInit();
    }

    public <T extends PropertiesBase> void updateFrom(T otherInstance) {
        for (Entry<String, ConfigField<?>> otherEntry : otherInstance.getConfigFields().entrySet()) {
            String fieldName = (String) otherEntry.getKey();
            ConfigField<?> otherField = (ConfigField<?>) otherEntry.getValue();
            if (Strings.isNullOrEmpty(fieldName) || otherField == null) {
                this.warn("Bad configField entry during updateFrom(): " + otherEntry);
            } else if (otherField.getAttributeMap() == null) {
                this.warn("Bad configField source (no attributes) during updateFrom(): " + fieldName);
            } else {
                ConfigField<?> myField = this.getConfigField(fieldName);
                if (myField == null) {
                    this.warn("configField target doesn't exist during updateFrom(): " + fieldName);
                } else if (myField.getAttributeMap() == null) {
                    this.warn("Bad configField target (no attributes) during updateFrom(): " + fieldName);
                } else {
                    myField.getAttributeMap().putAll(otherField.getAttributeMap());
                }
            }
        }
        this.configVersion = otherInstance.configVersion;
    }

    protected void ensureInit() {
        if (this.configFields == null) {
            this.getConfigFields();
            this.currentState = PropertiesBase.State.Initialized;
        }
    }

    protected void preSave() {
        this.ensureInit();
    }

    public boolean save() {
        return this.save(this.getFile(), false);
    }

    public boolean save(File configFile, boolean verbose) {
        this.preSave();
        boolean saved = false;
        boolean canSave = this.isValid(true);
        if (!canSave) {
            this.error(String.format("Can't save invalid config to file: %s", this.getFileName()));
        } else {
            try {
                if (!configFile.exists()) {
                    this.info(String.format("Creating config file: %s", configFile));
                    if (!configFile.getParentFile().exists()) {
                        configFile.getParentFile().mkdirs();
                    }
                } else if (!this.isCurrent()) {
                    if (this.configVersion != null) {
                        this.info(String.format("Updating config file from version \"%s\" to \"%s\": %s", this.configVersion, Journeymap.JM_VERSION, configFile));
                    }
                    this.configVersion = Journeymap.JM_VERSION;
                }
                StringBuilder sb = new StringBuilder();
                String lineEnding = System.getProperty("line.separator");
                for (String line : this.getHeaders()) {
                    sb.append(line).append(lineEnding);
                }
                String header = sb.toString();
                String json = this.toJsonString(verbose);
                Files.write(header + json, configFile, UTF8);
                saved = true;
            } catch (Exception var11) {
                this.error(String.format("Can't save config file %s: %s", configFile, var11), var11);
            }
        }
        this.currentState = saved ? PropertiesBase.State.SavedOk : PropertiesBase.State.SavedError;
        return saved;
    }

    public String toJsonString(boolean verbose) {
        this.ensureInit();
        return this.getGson(verbose).toJson(this);
    }

    public boolean isValid(boolean fix) {
        this.ensureInit();
        boolean valid = this.validateFields(fix);
        if (!this.isCurrent()) {
            if (fix) {
                this.configVersion = Journeymap.JM_VERSION;
                this.info(String.format("Setting config file to version \"%s\": %s", this.configVersion, this.getFileName()));
            } else {
                valid = false;
                this.info(String.format("Config file isn't current, has version \"%s\": %s", this.configVersion, this.getFileName()));
            }
        }
        this.currentState = valid ? PropertiesBase.State.Valid : PropertiesBase.State.Invalid;
        return valid;
    }

    protected ConfigField<?> getConfigField(String fieldName) {
        return (ConfigField<?>) this.getConfigFields().get(fieldName);
    }

    public Map<String, ConfigField<?>> getConfigFields() {
        if (this.configFields == null) {
            HashMap<String, ConfigField<?>> map = new HashMap();
            try {
                for (Field field : this.getClass().getFields()) {
                    Class<?> fieldType = field.getType();
                    if (ConfigField.class.isAssignableFrom(fieldType)) {
                        ConfigField configField = (ConfigField) field.get(this);
                        if (configField != null) {
                            configField.setOwner(field.getName(), this);
                            Category category = configField.getCategory();
                            if (category != null) {
                                this.categories.add(category);
                            }
                        }
                        map.put(field.getName(), configField);
                    }
                }
            } catch (Throwable var9) {
                this.error("Unexpected error getting fields: " + LogFormatter.toString(var9));
            }
            this.configFields = Collections.unmodifiableMap(map);
        }
        return this.configFields;
    }

    public Category getCategoryByName(String name) {
        for (Category category : this.categories) {
            if (category.getName().equalsIgnoreCase(name)) {
                return category;
            }
        }
        return null;
    }

    protected boolean validateFields(boolean fix) {
        try {
            boolean valid = true;
            for (Entry<String, ConfigField<?>> entry : this.getConfigFields().entrySet()) {
                ConfigField<?> configField = (ConfigField<?>) entry.getValue();
                if (configField == null) {
                    this.warn(String.format("%s.%s is null", this.getClass().getSimpleName(), entry.getKey()));
                    valid = false;
                } else {
                    boolean fieldValid = configField.validate(fix);
                    if (!fieldValid) {
                        valid = false;
                    }
                }
            }
            return valid;
        } catch (Throwable var7) {
            this.error("Unexpected error in validateFields: " + LogFormatter.toPartialString(var7));
            return false;
        }
    }

    public List<ExclusionStrategy> getExclusionStrategies(boolean verbose) {
        ArrayList strategies = new ArrayList();
        if (!verbose) {
            strategies.add(new ExclusionStrategy() {

                public boolean shouldSkipField(FieldAttributes f) {
                    return f.getDeclaringClass().equals(PropertiesBase.class) ? f.getName().equals("categories") : false;
                }

                public boolean shouldSkipClass(Class<?> clazz) {
                    return false;
                }
            });
        }
        return strategies;
    }

    public long lastModified() {
        File file = this.getFile();
        return file.canRead() ? file.lastModified() : 0L;
    }

    protected ToStringHelper toStringHelper() {
        return MoreObjects.toStringHelper(this).add("state", this.currentState).add("file", this.getFileName()).add("configVersion", this.configVersion);
    }

    public String toString() {
        ToStringHelper toStringHelper = this.toStringHelper();
        for (Entry<String, ConfigField<?>> entry : this.getConfigFields().entrySet()) {
            ConfigField<?> configField = (ConfigField<?>) entry.getValue();
            toStringHelper.add((String) entry.getKey(), configField.get());
        }
        return toStringHelper.toString();
    }

    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        } else {
            return !(o instanceof PropertiesBase that) ? false : Objects.equal(this.getFileName(), that.getFileName());
        }
    }

    public final int hashCode() {
        return Objects.hashCode(new Object[] { this.getConfigFields() });
    }

    protected void info(String message) {
        Journeymap.getLogger().info(String.format("%s (%s) %s", this.getName(), this.currentState, message));
    }

    protected void warn(String message) {
        Journeymap.getLogger().warn(String.format("%s (%s) %s", this.getName(), this.currentState, message));
    }

    protected void error(String message) {
        Journeymap.getLogger().error(String.format("%s (%s) %s", this.getName(), this.currentState, message));
    }

    protected void error(String message, Throwable throwable) {
        Journeymap.getLogger().error(String.format("%s (%s) %s : %s", this.getName(), this.currentState, message, LogFormatter.toString(throwable)), throwable);
    }

    protected static enum State {

        New,
        Initialized,
        FirstLoaded,
        FileLoaded,
        Valid,
        Invalid,
        SavedOk,
        SavedError
    }
}