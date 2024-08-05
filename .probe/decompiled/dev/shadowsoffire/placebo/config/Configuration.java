package dev.shadowsoffire.placebo.config;

import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.primitives.Floats;
import dev.shadowsoffire.placebo.Placebo;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PushbackInputStream;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Configuration {

    private static final Logger LOGGER = LogManager.getLogger();

    public static final String CATEGORY_GENERAL = "general";

    public static final String CATEGORY_CLIENT = "client";

    public static final String ALLOWED_CHARS = "._-";

    public static final String DEFAULT_ENCODING = "UTF-8";

    public static final String CATEGORY_SPLITTER = ".";

    private static final Pattern CONFIG_START = Pattern.compile("START: \"([^\\\"]+)\"");

    private static final Pattern CONFIG_END = Pattern.compile("END: \"([^\\\"]+)\"");

    public static final CharMatcher allowedProperties = CharMatcher.forPredicate(Character::isLetterOrDigit).or(CharMatcher.anyOf("._-"));

    File file;

    private Map<String, ConfigCategory> categories = new LinkedHashMap();

    private boolean caseSensitiveCustomCategories;

    public String defaultEncoding = "UTF-8";

    private String fileName = null;

    public boolean isChild = false;

    private boolean changed = false;

    private String title = "";

    private String mainComment = null;

    public Configuration(File file) {
        this.file = file;
        try {
            this.load();
        } catch (Throwable var4) {
            File fileBak = new File(file.getAbsolutePath() + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".errored");
            Placebo.LOGGER.fatal("An exception occurred while loading config file {}. This file will be renamed to {} and a new config file will be generated.", file.getName(), fileBak.getName(), var4);
            file.renameTo(fileBak);
            this.load();
        }
    }

    public Configuration(String modid) {
        this(new File(FMLPaths.CONFIGDIR.get().toFile(), modid + ".cfg"));
    }

    public void setTitle(String title) {
        this.title = (String) Preconditions.checkNotNull(title);
    }

    public void setComment(String comment) {
        this.mainComment = comment;
    }

    public String toString() {
        return this.file.getAbsolutePath();
    }

    public Property get(String category, String key, boolean defaultValue) {
        return this.get(category, key, defaultValue, null);
    }

    public Property get(String category, String key, boolean defaultValue, String comment) {
        Property prop = this.get(category, key, Boolean.toString(defaultValue), comment, Property.Type.BOOLEAN);
        prop.setDefaultValue(Boolean.toString(defaultValue));
        if (!prop.isBooleanValue()) {
            prop.setValue(defaultValue);
        }
        return prop;
    }

    public Property get(String category, String key, boolean[] defaultValues) {
        return this.get(category, key, defaultValues, null);
    }

    public Property get(String category, String key, boolean[] defaultValues, String comment) {
        return this.get(category, key, defaultValues, comment, false, -1);
    }

    public Property get(String category, String key, boolean[] defaultValues, String comment, boolean isListLengthFixed, int maxListLength) {
        String[] values = new String[defaultValues.length];
        for (int i = 0; i < defaultValues.length; i++) {
            values[i] = Boolean.toString(defaultValues[i]);
        }
        Property prop = this.get(category, key, values, comment, Property.Type.BOOLEAN);
        prop.setDefaultValues(values);
        prop.setIsListLengthFixed(isListLengthFixed);
        prop.setMaxListLength(maxListLength);
        if (!prop.isBooleanList()) {
            prop.setValues(values);
        }
        return prop;
    }

    public Property get(String category, String key, int defaultValue) {
        return this.get(category, key, defaultValue, null, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public Property get(String category, String key, int defaultValue, String comment) {
        return this.get(category, key, defaultValue, comment, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public Property get(String category, String key, int defaultValue, String comment, int minValue, int maxValue) {
        Property prop = this.get(category, key, Integer.toString(defaultValue), comment, Property.Type.INTEGER);
        prop.setDefaultValue(Integer.toString(defaultValue));
        prop.setMinValue(minValue);
        prop.setMaxValue(maxValue);
        if (!prop.isIntValue()) {
            prop.setValue(defaultValue);
        }
        return prop;
    }

    public Property get(String category, String key, int[] defaultValues) {
        return this.get(category, key, defaultValues, null);
    }

    public Property get(String category, String key, int[] defaultValues, String comment) {
        return this.get(category, key, defaultValues, comment, Integer.MIN_VALUE, Integer.MAX_VALUE, false, -1);
    }

    public Property get(String category, String key, int[] defaultValues, String comment, int minValue, int maxValue) {
        return this.get(category, key, defaultValues, comment, minValue, maxValue, false, -1);
    }

    public Property get(String category, String key, int[] defaultValues, String comment, int minValue, int maxValue, boolean isListLengthFixed, int maxListLength) {
        String[] values = new String[defaultValues.length];
        for (int i = 0; i < defaultValues.length; i++) {
            values[i] = Integer.toString(defaultValues[i]);
        }
        Property prop = this.get(category, key, values, comment, Property.Type.INTEGER);
        prop.setDefaultValues(values);
        prop.setMinValue(minValue);
        prop.setMaxValue(maxValue);
        prop.setIsListLengthFixed(isListLengthFixed);
        prop.setMaxListLength(maxListLength);
        if (!prop.isIntList()) {
            prop.setValues(values);
        }
        return prop;
    }

    public Property get(String category, String key, double defaultValue) {
        return this.get(category, key, defaultValue, null);
    }

    public Property get(String category, String key, double defaultValue, String comment) {
        return this.get(category, key, defaultValue, comment, -Double.MAX_VALUE, Double.MAX_VALUE);
    }

    public Property get(String category, String key, double defaultValue, String comment, double minValue, double maxValue) {
        Property prop = this.get(category, key, Double.toString(defaultValue), comment, Property.Type.DOUBLE);
        prop.setDefaultValue(Double.toString(defaultValue));
        prop.setMinValue(minValue);
        prop.setMaxValue(maxValue);
        if (!prop.isDoubleValue()) {
            prop.setValue(defaultValue);
        }
        return prop;
    }

    public Property get(String category, String key, double[] defaultValues) {
        return this.get(category, key, defaultValues, null);
    }

    public Property get(String category, String key, double[] defaultValues, String comment) {
        return this.get(category, key, defaultValues, comment, -Double.MAX_VALUE, Double.MAX_VALUE, false, -1);
    }

    public Property get(String category, String key, double[] defaultValues, String comment, double minValue, double maxValue) {
        return this.get(category, key, defaultValues, comment, minValue, maxValue, false, -1);
    }

    public Property get(String category, String key, double[] defaultValues, String comment, double minValue, double maxValue, boolean isListLengthFixed, int maxListLength) {
        String[] values = new String[defaultValues.length];
        for (int i = 0; i < defaultValues.length; i++) {
            values[i] = Double.toString(defaultValues[i]);
        }
        Property prop = this.get(category, key, values, comment, Property.Type.DOUBLE);
        prop.setDefaultValues(values);
        prop.setMinValue(minValue);
        prop.setMaxValue(maxValue);
        prop.setIsListLengthFixed(isListLengthFixed);
        prop.setMaxListLength(maxListLength);
        if (!prop.isDoubleList()) {
            prop.setValues(values);
        }
        return prop;
    }

    public Property get(String category, String key, String defaultValue) {
        return this.get(category, key, defaultValue, null);
    }

    public Property get(String category, String key, String defaultValue, String comment) {
        return this.get(category, key, defaultValue, comment, Property.Type.STRING);
    }

    public Property get(String category, String key, String defaultValue, String comment, Pattern validationPattern) {
        Property prop = this.get(category, key, defaultValue, comment, Property.Type.STRING);
        prop.setValidationPattern(validationPattern);
        return prop;
    }

    public Property get(String category, String key, String defaultValue, String comment, String[] validValues) {
        Property prop = this.get(category, key, defaultValue, comment, Property.Type.STRING);
        prop.setValidValues(validValues);
        return prop;
    }

    public Property get(String category, String key, String[] defaultValues) {
        return this.get(category, key, defaultValues, null, false, -1, null);
    }

    public Property get(String category, String key, String[] defaultValues, String comment) {
        return this.get(category, key, defaultValues, comment, false, -1, null);
    }

    public Property get(String category, String key, String[] defaultValues, String comment, Pattern validationPattern) {
        return this.get(category, key, defaultValues, comment, false, -1, validationPattern);
    }

    public Property get(String category, String key, String[] defaultValues, String comment, boolean isListLengthFixed, int maxListLength, Pattern validationPattern) {
        Property prop = this.get(category, key, defaultValues, comment, Property.Type.STRING);
        prop.setIsListLengthFixed(isListLengthFixed);
        prop.setMaxListLength(maxListLength);
        prop.setValidationPattern(validationPattern);
        return prop;
    }

    public Property get(String category, String key, String defaultValue, String comment, Property.Type type) {
        ConfigCategory cat = this.getCategory(category);
        if (cat.containsKey(key)) {
            Property prop = cat.get(key);
            if (prop.getType() == null) {
                prop = new Property(prop.getName(), prop.getString(), type);
                cat.put(key, prop);
            }
            prop.setDefaultValue(defaultValue);
            prop.setComment(comment);
            return prop;
        } else if (defaultValue != null) {
            Property prop = new Property(key, defaultValue, type);
            prop.setValue(defaultValue);
            cat.put(key, prop);
            prop.setDefaultValue(defaultValue);
            prop.setComment(comment);
            return prop;
        } else {
            return null;
        }
    }

    public Property get(String category, String key, String[] defaultValues, String comment, Property.Type type) {
        ConfigCategory cat = this.getCategory(category);
        if (cat.containsKey(key)) {
            Property prop = cat.get(key);
            if (prop.getType() == null) {
                prop = new Property(prop.getName(), prop.getString(), type);
                cat.put(key, prop);
            }
            prop.setDefaultValues(defaultValues);
            prop.setComment(comment);
            return prop;
        } else if (defaultValues != null) {
            Property prop = new Property(key, defaultValues, type);
            prop.setDefaultValues(defaultValues);
            prop.setComment(comment);
            cat.put(key, prop);
            return prop;
        } else {
            return null;
        }
    }

    public boolean hasCategory(String category) {
        if (!this.caseSensitiveCustomCategories) {
            category = category.toLowerCase(Locale.ENGLISH);
        }
        return this.categories.get(category) != null;
    }

    public boolean hasKey(String category, String key) {
        if (!this.caseSensitiveCustomCategories) {
            category = category.toLowerCase(Locale.ENGLISH);
        }
        ConfigCategory cat = (ConfigCategory) this.categories.get(category);
        return cat != null && cat.containsKey(key);
    }

    public void load() {
        BufferedReader buffer = null;
        Configuration.UnicodeInputStreamReader input = null;
        try {
            if (this.file.getParentFile() != null) {
                this.file.getParentFile().mkdirs();
            }
            if (!this.file.exists()) {
                this.categories.clear();
                if (!this.file.createNewFile()) {
                    return;
                }
            }
            if (this.file.canRead()) {
                input = new Configuration.UnicodeInputStreamReader(new FileInputStream(this.file), this.defaultEncoding);
                this.defaultEncoding = input.getEncoding();
                buffer = new BufferedReader(input);
                ConfigCategory currentCat = null;
                Property.Type type = null;
                ArrayList<String> tmpList = null;
                int lineNum = 0;
                String name = null;
                while (true) {
                    lineNum++;
                    String line = buffer.readLine();
                    if (line == null) {
                        break;
                    }
                    Matcher start = CONFIG_START.matcher(line);
                    Matcher end = CONFIG_END.matcher(line);
                    if (start.matches()) {
                        this.fileName = start.group(1);
                        this.categories = new TreeMap();
                    } else if (!end.matches()) {
                        int nameStart = -1;
                        int nameEnd = -1;
                        boolean skip = false;
                        boolean quoted = false;
                        boolean isFirstNonWhitespaceCharOnLine = true;
                        for (int i = 0; i < line.length() && !skip; i++) {
                            if (!Character.isLetterOrDigit(line.charAt(i)) && "._-".indexOf(line.charAt(i)) == -1 && (!quoted || line.charAt(i) == '"')) {
                                if (!Character.isWhitespace(line.charAt(i))) {
                                    switch(line.charAt(i)) {
                                        case '"':
                                            if (tmpList == null) {
                                                if (quoted) {
                                                    quoted = false;
                                                }
                                                if (!quoted && nameStart == -1) {
                                                    quoted = true;
                                                }
                                            }
                                            break;
                                        case '#':
                                            if (tmpList == null) {
                                                skip = true;
                                                continue;
                                            }
                                            break;
                                        case ':':
                                            if (tmpList == null) {
                                                type = Property.Type.tryParse(line.substring(nameStart, nameEnd + 1).charAt(0));
                                                nameEnd = -1;
                                                nameStart = -1;
                                            }
                                            break;
                                        case '<':
                                            if (tmpList != null && i + 1 == line.length() || tmpList == null && i + 1 != line.length()) {
                                                throw new RuntimeException(String.format("Malformed list property \"%s:%d\"", this.fileName, lineNum));
                                            }
                                            if (i + 1 == line.length()) {
                                                name = line.substring(nameStart, nameEnd + 1);
                                                if (currentCat == null) {
                                                    throw new RuntimeException(String.format("'%s' has no scope in '%s:%d'", name, this.fileName, lineNum));
                                                }
                                                tmpList = new ArrayList();
                                                skip = true;
                                            }
                                            break;
                                        case '=':
                                            if (tmpList == null) {
                                                name = line.substring(nameStart, nameEnd + 1);
                                                if (currentCat == null) {
                                                    throw new RuntimeException(String.format("'%s' has no scope in '%s:%d'", name, this.fileName, lineNum));
                                                }
                                                Property prop = new Property(name, line.substring(i + 1), type, true);
                                                i = line.length();
                                                currentCat.put(name, prop);
                                            }
                                            break;
                                        case '>':
                                            if (tmpList == null) {
                                                throw new RuntimeException(String.format("Malformed list property \"%s:%d\"", this.fileName, lineNum));
                                            }
                                            if (isFirstNonWhitespaceCharOnLine) {
                                                currentCat.put(name, new Property(name, (String[]) tmpList.toArray(new String[tmpList.size()]), type));
                                                name = null;
                                                tmpList = null;
                                                type = null;
                                            }
                                            break;
                                        case '{':
                                            if (tmpList == null) {
                                                name = line.substring(nameStart, nameEnd + 1);
                                                if (!this.caseSensitiveCustomCategories) {
                                                    name = name.toLowerCase(Locale.ENGLISH);
                                                }
                                                String qualifiedName = ConfigCategory.getQualifiedName(name, currentCat);
                                                ConfigCategory cat = (ConfigCategory) this.categories.get(qualifiedName);
                                                if (cat == null) {
                                                    currentCat = new ConfigCategory(name, currentCat);
                                                    this.categories.put(qualifiedName, currentCat);
                                                } else {
                                                    currentCat = cat;
                                                }
                                                name = null;
                                            }
                                            break;
                                        case '}':
                                            if (tmpList == null) {
                                                if (currentCat == null) {
                                                    throw new RuntimeException(String.format("Config file corrupt, attempted to close to many categories '%s:%d'", this.fileName, lineNum));
                                                }
                                                currentCat = currentCat.parent;
                                            }
                                            break;
                                        case '~':
                                            if (tmpList != null) {
                                            }
                                            break;
                                        default:
                                            if (tmpList == null) {
                                                throw new RuntimeException(String.format("Unknown character '%s' in '%s:%d'", line.charAt(i), this.fileName, lineNum));
                                            }
                                    }
                                    isFirstNonWhitespaceCharOnLine = false;
                                }
                            } else {
                                if (nameStart == -1) {
                                    nameStart = i;
                                }
                                nameEnd = i;
                                isFirstNonWhitespaceCharOnLine = false;
                            }
                        }
                        if (quoted) {
                            throw new RuntimeException(String.format("Unmatched quote in '%s:%d'", this.fileName, lineNum));
                        }
                        if (tmpList != null && !skip) {
                            tmpList.add(line.trim());
                        }
                    } else {
                        this.fileName = end.group(1);
                    }
                }
            }
        } catch (IOException var23) {
            LOGGER.error("Error while loading config {}.", this.fileName, var23);
        } finally {
            IOUtils.closeQuietly(buffer);
            IOUtils.closeQuietly(input);
        }
        this.resetChangedState();
    }

    public void save() {
        try {
            if (this.file.getParentFile() != null) {
                this.file.getParentFile().mkdirs();
            }
            if (!this.file.exists() && !this.file.createNewFile()) {
                return;
            }
            if (this.file.canWrite()) {
                FileOutputStream fos = new FileOutputStream(this.file);
                BufferedWriter buffer = new BufferedWriter(new OutputStreamWriter(fos, this.defaultEncoding));
                buffer.write("# File Specification: https://gist.github.com/Shadows-of-Fire/88ac714a758636c57a52e32ace5474c1");
                buffer.newLine();
                buffer.newLine();
                buffer.write(String.format("# %s", this.title != null ? this.title : "Configuration File"));
                buffer.newLine();
                buffer.newLine();
                if (this.mainComment != null) {
                    writeComment(buffer, this.mainComment);
                    buffer.newLine();
                    buffer.newLine();
                }
                this.save(buffer);
                buffer.close();
                fos.close();
            }
        } catch (IOException var3) {
            LOGGER.error("Error while saving config {}.", this.fileName, var3);
        }
    }

    public static void writeComment(BufferedWriter writer, String comment) throws IOException {
        if (comment != null && !comment.isEmpty()) {
            String[] split = comment.split("\\n");
            for (int i = 0; i < split.length; i++) {
                writer.write("# " + split[i]);
                writer.newLine();
            }
        }
    }

    private void save(BufferedWriter out) throws IOException {
        for (ConfigCategory cat : this.categories.values()) {
            if (!cat.isChild()) {
                cat.write(out, 0);
                out.newLine();
            }
        }
    }

    public ConfigCategory getCategory(String category) {
        if (!this.caseSensitiveCustomCategories) {
            category = category.toLowerCase(Locale.ENGLISH);
        }
        ConfigCategory ret = (ConfigCategory) this.categories.get(category);
        if (ret == null) {
            if (category.contains(".")) {
                String[] hierarchy = category.split("\\.");
                ConfigCategory parent = (ConfigCategory) this.categories.get(hierarchy[0]);
                if (parent == null) {
                    parent = new ConfigCategory(hierarchy[0]);
                    this.categories.put(parent.getQualifiedName(), parent);
                    this.changed = true;
                }
                for (int i = 1; i < hierarchy.length; i++) {
                    String name = ConfigCategory.getQualifiedName(hierarchy[i], parent);
                    ConfigCategory child = (ConfigCategory) this.categories.get(name);
                    if (child == null) {
                        child = new ConfigCategory(hierarchy[i], parent);
                        this.categories.put(name, child);
                        this.changed = true;
                    }
                    ret = child;
                    parent = child;
                }
            } else {
                ret = new ConfigCategory(category);
                this.categories.put(category, ret);
                this.changed = true;
            }
        }
        return ret;
    }

    public void removeCategory(ConfigCategory category) {
        for (ConfigCategory child : category.getChildren()) {
            this.removeCategory(child);
        }
        if (this.categories.containsKey(category.getQualifiedName())) {
            this.categories.remove(category.getQualifiedName());
            if (category.parent != null) {
                category.parent.removeChild(category);
            }
            this.changed = true;
        }
    }

    public Configuration setCategoryComment(String category, String comment) {
        this.getCategory(category).setComment(comment);
        return this;
    }

    public Configuration setCategoryLanguageKey(String category, String langKey) {
        this.getCategory(category).setLanguageKey(langKey);
        return this;
    }

    public Configuration setCategoryRequiresWorldRestart(String category, boolean requiresWorldRestart) {
        this.getCategory(category).setRequiresWorldRestart(requiresWorldRestart);
        return this;
    }

    public Configuration setCategoryRequiresMcRestart(String category, boolean requiresMcRestart) {
        this.getCategory(category).setRequiresMcRestart(requiresMcRestart);
        return this;
    }

    public Configuration setCategoryPropertyOrder(String category, List<String> propOrder) {
        this.getCategory(category).setPropertyOrder(propOrder);
        return this;
    }

    public boolean hasChanged() {
        if (this.changed) {
            return true;
        } else {
            for (ConfigCategory cat : this.categories.values()) {
                if (cat.hasChanged()) {
                    return true;
                }
            }
            return false;
        }
    }

    private void resetChangedState() {
        this.changed = false;
        for (ConfigCategory cat : this.categories.values()) {
            cat.resetChangedState();
        }
    }

    public Set<String> getCategoryNames() {
        return ImmutableSet.copyOf(this.categories.keySet());
    }

    public boolean renameProperty(String category, String oldPropName, String newPropName) {
        if (this.hasCategory(category)) {
            ConfigCategory cat = this.getCategory(category);
            if (cat.containsKey(oldPropName) && !oldPropName.equalsIgnoreCase(newPropName)) {
                Property prop = cat.remove(oldPropName);
                prop.setName(newPropName);
                cat.put(newPropName, prop);
                return true;
            }
        }
        return false;
    }

    public boolean moveProperty(String oldCategory, String propName, String newCategory) {
        if (!oldCategory.equals(newCategory) && this.hasCategory(oldCategory) && this.getCategory(oldCategory).containsKey(propName)) {
            this.getCategory(newCategory).put(propName, this.getCategory(oldCategory).remove(propName));
            return true;
        } else {
            return false;
        }
    }

    public void copyCategoryProps(Configuration fromConfig, String[] ctgys) {
        if (ctgys == null) {
            ctgys = (String[]) this.getCategoryNames().toArray(new String[this.getCategoryNames().size()]);
        }
        for (String ctgy : ctgys) {
            if (fromConfig.hasCategory(ctgy) && this.hasCategory(ctgy)) {
                ConfigCategory thiscc = this.getCategory(ctgy);
                ConfigCategory fromcc = fromConfig.getCategory(ctgy);
                for (Entry<String, Property> entry : thiscc.getValues().entrySet()) {
                    if (fromcc.containsKey((String) entry.getKey())) {
                        thiscc.put((String) entry.getKey(), fromcc.get((String) entry.getKey()));
                    }
                }
            }
        }
    }

    public String getString(String name, String category, String defaultValue, String comment) {
        return this.getString(name, category, defaultValue, comment, name, null);
    }

    public String getString(String name, String category, String defaultValue, String comment, String langKey) {
        return this.getString(name, category, defaultValue, comment, langKey, null);
    }

    public String getString(String name, String category, String defaultValue, String comment, Pattern pattern) {
        return this.getString(name, category, defaultValue, comment, name, pattern);
    }

    public String getString(String name, String category, String defaultValue, String comment, String langKey, Pattern pattern) {
        Property prop = this.get(category, name, defaultValue);
        prop.setLanguageKey(langKey);
        prop.setValidationPattern(pattern);
        prop.setComment(comment + "\nDefault: " + defaultValue);
        return prop.getString();
    }

    public String getString(String name, String category, String defaultValue, String comment, String[] validValues) {
        return this.getString(name, category, defaultValue, comment, validValues, name);
    }

    public String getString(String name, String category, String defaultValue, String comment, String[] validValues, String langKey) {
        Property prop = this.get(category, name, defaultValue);
        prop.setValidValues(validValues);
        prop.setLanguageKey(langKey);
        prop.setComment(comment + "\nDefault: " + defaultValue);
        return prop.getString();
    }

    public String[] getStringList(String name, String category, String[] defaultValues, String comment) {
        return this.getStringList(name, category, defaultValues, comment, null, name);
    }

    public String[] getStringList(String name, String category, String[] defaultValue, String comment, String[] validValues) {
        return this.getStringList(name, category, defaultValue, comment, validValues, name);
    }

    public String[] getStringList(String name, String category, String[] defaultValue, String comment, String[] validValues, String langKey) {
        Property prop = this.get(category, name, defaultValue);
        prop.setLanguageKey(langKey);
        prop.setValidValues(validValues);
        prop.setComment(comment + "\nDefault: " + toComment(prop.getDefaults()));
        return prop.getStringList();
    }

    private static String toComment(String[] values) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < values.length; i++) {
            sb.append(values[i].toString());
            if (i != values.length - 1) {
                sb.append("], [");
            } else {
                sb.append("]");
            }
        }
        return sb.toString();
    }

    public boolean getBoolean(String name, String category, boolean defaultValue, String comment) {
        return this.getBoolean(name, category, defaultValue, comment, name);
    }

    public boolean getBoolean(String name, String category, boolean defaultValue, String comment, String langKey) {
        Property prop = this.get(category, name, defaultValue);
        prop.setLanguageKey(langKey);
        prop.setComment(comment + "\nDefault: " + defaultValue);
        return prop.getBoolean(defaultValue);
    }

    public int getInt(String name, String category, int defaultValue, int minValue, int maxValue, String comment) {
        return this.getInt(name, category, defaultValue, minValue, maxValue, comment, name);
    }

    public int getInt(String name, String category, int defaultValue, int minValue, int maxValue, String comment, String langKey) {
        Property prop = this.get(category, name, defaultValue);
        prop.setLanguageKey(langKey);
        prop.setComment(comment + "\nDefault: " + defaultValue + "; Range: [" + minValue + " ~ " + maxValue + "]");
        prop.setMinValue(minValue);
        prop.setMaxValue(maxValue);
        return prop.getInt(defaultValue) < minValue ? minValue : (prop.getInt(defaultValue) > maxValue ? maxValue : prop.getInt(defaultValue));
    }

    public float getFloat(String name, String category, float defaultValue, float minValue, float maxValue, String comment) {
        return this.getFloat(name, category, defaultValue, minValue, maxValue, comment, name);
    }

    public float getFloat(String name, String category, float defaultValue, float minValue, float maxValue, String comment, String langKey) {
        Property prop = this.get(category, name, Float.toString(defaultValue), name);
        prop.setLanguageKey(langKey);
        prop.setComment(comment + "\nDefault: " + defaultValue + "; Range: [" + minValue + " ~ " + maxValue + "]");
        prop.setMinValue((double) minValue);
        prop.setMaxValue((double) maxValue);
        try {
            float parseFloat = Float.parseFloat(prop.getString());
            return Floats.constrainToRange(parseFloat, minValue, maxValue);
        } catch (Exception var10) {
            LOGGER.error("Failed to get float for {}/{}", name, category, var10);
            return defaultValue;
        }
    }

    public File getConfigFile() {
        return this.file;
    }

    public static class UnicodeInputStreamReader extends Reader {

        private final InputStreamReader input;

        public UnicodeInputStreamReader(InputStream source, String encoding) throws IOException {
            String enc = encoding;
            byte[] data = new byte[4];
            PushbackInputStream pbStream = new PushbackInputStream(source, data.length);
            int read = pbStream.read(data, 0, data.length);
            int size = 0;
            int bom16 = (data[0] & 255) << 8 | data[1] & 255;
            int bom24 = bom16 << 8 | data[2] & 255;
            int bom32 = bom24 << 8 | data[3] & 255;
            if (bom24 == 15711167) {
                enc = "UTF-8";
                size = 3;
            } else if (bom16 == 65279) {
                enc = "UTF-16BE";
                size = 2;
            } else if (bom16 == 65534) {
                enc = "UTF-16LE";
                size = 2;
            } else if (bom32 == 65279) {
                enc = "UTF-32BE";
                size = 4;
            } else if (bom32 == -131072) {
                enc = "UTF-32LE";
                size = 4;
            }
            if (size < read) {
                pbStream.unread(data, size, read - size);
            }
            this.input = new InputStreamReader(pbStream, enc);
        }

        public String getEncoding() {
            return this.input.getEncoding();
        }

        public int read(char[] cbuf, int off, int len) throws IOException {
            return this.input.read(cbuf, off, len);
        }

        public void close() throws IOException {
            this.input.close();
        }
    }
}