package de.keksuccino.konkrete.config;

import de.keksuccino.konkrete.config.exceptions.InvalidValueException;
import de.keksuccino.konkrete.math.MathUtils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;

public class Config {

    private String path;

    private File config;

    private Map<String, ConfigEntry> values = new HashMap();

    private List<String> registeredValues = new ArrayList();

    private String name = null;

    private List<String> categorys = new ArrayList();

    public Config(String path) {
        this.path = path;
        this.config = new File(path);
        if (this.config.isFile()) {
            File f = this.config.getParentFile();
            if (f != null && !f.exists()) {
                f.mkdirs();
            }
        }
        this.init();
    }

    private void init() {
        List<String> l = this.getTextFileData();
        if (!l.isEmpty()) {
            String category = null;
            String desc = null;
            String valueName = null;
            ConfigEntry.EntryType type = null;
            String value = null;
            Boolean b = false;
            for (String s : l) {
                if (b) {
                    if (new StringBuilder(s).reverse().toString().replace(" ", "").startsWith(";'")) {
                        value = value + "\n" + new StringBuilder(new StringBuilder(s).reverse().toString().split(";", 2)[1].substring(1)).reverse().toString();
                        if (category != null && valueName != null && type != null && !this.valueExists(valueName)) {
                            this.values.put(valueName, new ConfigEntry(valueName, value, type, category, desc));
                            if (!this.categorys.contains(category)) {
                                this.categorys.add(category);
                            }
                        }
                        desc = null;
                        valueName = null;
                        type = null;
                        value = null;
                        b = false;
                    } else {
                        value = value + "\n" + s;
                    }
                }
                if (s.startsWith("##[")) {
                    if (s.contains("]")) {
                        category = new StringBuilder(new StringBuilder(s.split("[\\[]", 2)[1]).reverse().toString().split("[\\]]")[1]).reverse().toString();
                    }
                } else if (s.startsWith("[")) {
                    if (s.contains("]")) {
                        desc = new StringBuilder(new StringBuilder(s.split("[\\[]", 2)[1]).reverse().toString().split("[\\]]")[1]).reverse().toString();
                    }
                } else if (s.length() > 0 && s.substring(1).startsWith(":") && s.contains("=") && s.contains("'")) {
                    valueName = s.split("[:]", 2)[1].replace(" ", "").split("=")[0];
                    if (s.startsWith("I:")) {
                        type = ConfigEntry.EntryType.INTEGER;
                    }
                    if (s.startsWith("S:")) {
                        type = ConfigEntry.EntryType.STRING;
                    }
                    if (s.startsWith("B:")) {
                        type = ConfigEntry.EntryType.BOOLEAN;
                    }
                    if (s.startsWith("L:")) {
                        type = ConfigEntry.EntryType.LONG;
                    }
                    if (s.startsWith("D:")) {
                        type = ConfigEntry.EntryType.DOUBLE;
                    }
                    if (s.startsWith("F:")) {
                        type = ConfigEntry.EntryType.FLOAT;
                    }
                    if (new StringBuilder(s).reverse().toString().replace(" ", "").startsWith(";'")) {
                        value = new StringBuilder(new StringBuilder(s.split("'", 2)[1]).reverse().toString().split(";", 2)[1].substring(1)).reverse().toString();
                        if (category != null && valueName != null && type != null && !this.valueExists(valueName)) {
                            this.values.put(valueName, new ConfigEntry(valueName, value, type, category, desc));
                            if (!this.categorys.contains(category)) {
                                this.categorys.add(category);
                            }
                        }
                        desc = null;
                        valueName = null;
                        type = null;
                        value = null;
                    } else {
                        value = s.split("'", 2)[1];
                        b = true;
                    }
                }
            }
        }
    }

    public List<String> getCategorys() {
        List<String> l = new ArrayList();
        l.addAll(this.categorys);
        return l;
    }

    public String getConfigName() {
        return this.name;
    }

    public void setConfigName(String name) {
        this.name = name;
    }

    public void syncConfig() {
        String data = "";
        Boolean b = false;
        if (this.name != null) {
            data = data + "//" + this.name + "\n";
            b = true;
        }
        for (String s : this.getCategorys()) {
            List<ConfigEntry> l = this.getEntrysForCategory(s);
            if (l.isEmpty()) {
                this.categorys.remove(s);
            } else {
                if (!b) {
                    b = true;
                } else {
                    data = data + "\n\n\n";
                }
                data = data + "##[" + s + "]\n";
                for (ConfigEntry e : l) {
                    String value = e.getValue();
                    String valueName = e.getName();
                    String desc = e.getDescription();
                    ConfigEntry.EntryType type = e.getType();
                    if (value != null && valueName != null && type != null) {
                        if (desc != null) {
                            data = data + "\n[" + desc + "]";
                        }
                        if (type == ConfigEntry.EntryType.STRING) {
                            data = data + "\nS:" + valueName + " = '";
                        }
                        if (type == ConfigEntry.EntryType.INTEGER) {
                            data = data + "\nI:" + valueName + " = '";
                        }
                        if (type == ConfigEntry.EntryType.BOOLEAN) {
                            data = data + "\nB:" + valueName + " = '";
                        }
                        if (type == ConfigEntry.EntryType.LONG) {
                            data = data + "\nL:" + valueName + " = '";
                        }
                        if (type == ConfigEntry.EntryType.DOUBLE) {
                            data = data + "\nD:" + valueName + " = '";
                        }
                        if (type == ConfigEntry.EntryType.FLOAT) {
                            data = data + "\nF:" + valueName + " = '";
                        }
                        data = data + value + "';";
                    }
                }
            }
        }
        File oldConfig = this.backupConfig();
        if (oldConfig == null) {
            System.out.println("############################################");
            System.out.println("WARNING: CONFIG BACKUP NOT SUCCESSFULL! (" + this.path + ")");
            System.out.println("############################################");
        }
        if (!this.config.exists()) {
            try {
                this.config.createNewFile();
            } catch (IOException var14) {
                var14.printStackTrace();
            }
        }
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.config), StandardCharsets.UTF_8));
            writer.write(data);
            writer.flush();
            oldConfig.delete();
        } catch (Exception var13) {
            var13.printStackTrace();
        }
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException var12) {
                var12.printStackTrace();
            }
        }
    }

    private File backupConfig() {
        File back = new File(this.config.getAbsolutePath() + ".backup");
        List<String> data = this.getTextFileData();
        String data2 = "";
        if (!back.exists()) {
            try {
                back.createNewFile();
            } catch (IOException var9) {
                var9.printStackTrace();
            }
        }
        Boolean b = false;
        for (String s : data) {
            if (!b) {
                b = true;
            } else {
                data2 = data2 + "\n";
            }
            data2 = data2 + s;
        }
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(back), StandardCharsets.UTF_8));
            writer.write(data2);
            writer.flush();
        } catch (Exception var8) {
            var8.printStackTrace();
        }
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException var7) {
                var7.printStackTrace();
            }
        }
        return back.exists() ? back : null;
    }

    public List<ConfigEntry> getEntrysForCategory(String category) {
        List<ConfigEntry> l = new ArrayList();
        for (Entry<String, ConfigEntry> m : this.values.entrySet()) {
            if (((ConfigEntry) m.getValue()).getCategory().equals(category)) {
                l.add((ConfigEntry) m.getValue());
            }
        }
        return l;
    }

    private List<String> getTextFileData() {
        List<String> l = new ArrayList();
        if (this.config.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(this.config), StandardCharsets.UTF_8));
                String line;
                while ((line = reader.readLine()) != null) {
                    l.add(line);
                }
                reader.close();
            } catch (Exception var4) {
                var4.printStackTrace();
            }
        }
        return l;
    }

    public void registerValue(String uniqueName, Integer defaultValue, String category) throws InvalidValueException {
        this.registerRawValue(uniqueName, String.valueOf(defaultValue), category, ConfigEntry.EntryType.INTEGER, null);
    }

    public void registerValue(String uniqueName, Double defaultValue, String category) throws InvalidValueException {
        this.registerRawValue(uniqueName, String.valueOf(defaultValue), category, ConfigEntry.EntryType.DOUBLE, null);
    }

    public void registerValue(String uniqueName, Long defaultValue, String category) throws InvalidValueException {
        this.registerRawValue(uniqueName, String.valueOf(defaultValue), category, ConfigEntry.EntryType.LONG, null);
    }

    public void registerValue(String uniqueName, Float defaultValue, String category) throws InvalidValueException {
        this.registerRawValue(uniqueName, String.valueOf(defaultValue), category, ConfigEntry.EntryType.FLOAT, null);
    }

    public void registerValue(String uniqueName, Boolean defaultValue, String category) throws InvalidValueException {
        this.registerRawValue(uniqueName, String.valueOf(defaultValue), category, ConfigEntry.EntryType.BOOLEAN, null);
    }

    public void registerValue(String uniqueName, String defaultValue, String category) throws InvalidValueException {
        this.registerRawValue(uniqueName, defaultValue, category, ConfigEntry.EntryType.STRING, null);
    }

    public void registerValue(String uniqueName, Integer defaultValue, String category, @Nullable String description) throws InvalidValueException {
        this.registerRawValue(uniqueName, String.valueOf(defaultValue), category, ConfigEntry.EntryType.INTEGER, description);
    }

    public void registerValue(String uniqueName, Double defaultValue, String category, @Nullable String description) throws InvalidValueException {
        this.registerRawValue(uniqueName, String.valueOf(defaultValue), category, ConfigEntry.EntryType.DOUBLE, description);
    }

    public void registerValue(String uniqueName, Float defaultValue, String category, @Nullable String description) throws InvalidValueException {
        this.registerRawValue(uniqueName, String.valueOf(defaultValue), category, ConfigEntry.EntryType.FLOAT, description);
    }

    public void registerValue(String uniqueName, Long defaultValue, String category, @Nullable String description) throws InvalidValueException {
        this.registerRawValue(uniqueName, String.valueOf(defaultValue), category, ConfigEntry.EntryType.LONG, description);
    }

    public void registerValue(String uniqueName, Boolean defaultValue, String category, @Nullable String description) throws InvalidValueException {
        this.registerRawValue(uniqueName, String.valueOf(defaultValue), category, ConfigEntry.EntryType.BOOLEAN, description);
    }

    public void registerValue(String uniqueName, String defaultValue, String category, @Nullable String description) throws InvalidValueException {
        this.registerRawValue(uniqueName, defaultValue, category, ConfigEntry.EntryType.STRING, description);
    }

    private void registerRawValue(String uniqueName, String defaultValue, String category, ConfigEntry.EntryType type, @Nullable String description) throws InvalidValueException {
        if (uniqueName == null) {
            throw new InvalidValueException("Value name cannot be null!");
        } else if (defaultValue == null) {
            throw new InvalidValueException("Default value cannot be null!");
        } else if (category == null) {
            throw new InvalidValueException("Category cannot be null!");
        } else if (type == null) {
            throw new InvalidValueException("Type cannot be null!");
        } else {
            if (!this.categorys.contains(category)) {
                this.categorys.add(category);
            }
            if (type == ConfigEntry.EntryType.BOOLEAN && !defaultValue.equalsIgnoreCase("true") && !defaultValue.equalsIgnoreCase("false")) {
                throw new InvalidValueException("This value is not a valid BOOLEAN! (" + defaultValue + ")");
            } else if (type == ConfigEntry.EntryType.INTEGER && !MathUtils.isInteger(defaultValue)) {
                throw new InvalidValueException("This value is not a valid INTEGER! (" + defaultValue + ")");
            } else if (type == ConfigEntry.EntryType.DOUBLE && !MathUtils.isDouble(defaultValue)) {
                throw new InvalidValueException("This value is not a valid DOUBLE! (" + defaultValue + ")");
            } else if (type == ConfigEntry.EntryType.FLOAT && !MathUtils.isFloat(defaultValue)) {
                throw new InvalidValueException("This value is not a valid FLOAT! (" + defaultValue + ")");
            } else if (type == ConfigEntry.EntryType.LONG && !MathUtils.isLong(defaultValue)) {
                throw new InvalidValueException("This value is not a valid LONG! (" + defaultValue + ")");
            } else {
                if (!this.valueExists(uniqueName)) {
                    this.values.put(uniqueName, new ConfigEntry(uniqueName, defaultValue, type, category, description));
                }
                this.registeredValues.add(uniqueName);
            }
        }
    }

    public ConfigEntry getAsEntry(String name) {
        return this.valueExists(name) ? (ConfigEntry) this.values.get(name) : null;
    }

    public List<ConfigEntry> getAllAsEntry() {
        List<ConfigEntry> l = new ArrayList();
        l.addAll(this.values.values());
        return l;
    }

    public void setValue(String name, String value) throws InvalidValueException {
        if (this.valueExists(name)) {
            ConfigEntry e = this.getAsEntry(name);
            if (e.getType() == ConfigEntry.EntryType.STRING) {
                e.setValue(value);
                this.syncConfig();
            } else {
                throw new InvalidValueException("This value's type is " + e.getType() + "! It isn't possible to set a STRING value to it!");
            }
        }
    }

    public void setValue(String name, Integer value) throws InvalidValueException {
        if (this.valueExists(name)) {
            ConfigEntry e = this.getAsEntry(name);
            if (e.getType() == ConfigEntry.EntryType.INTEGER) {
                e.setValue(String.valueOf(value));
                this.syncConfig();
            } else {
                throw new InvalidValueException("This value's type is " + e.getType() + "! It isn't possible to set an INTEGER value to it!");
            }
        }
    }

    public void setValue(String name, Boolean value) throws InvalidValueException {
        if (this.valueExists(name)) {
            ConfigEntry e = this.getAsEntry(name);
            if (e.getType() == ConfigEntry.EntryType.BOOLEAN) {
                e.setValue(String.valueOf(value));
                this.syncConfig();
            } else {
                throw new InvalidValueException("This value's type is " + e.getType() + "! It isn't possible to set a BOOLEAN value to it!");
            }
        }
    }

    public void setValue(String name, Float value) throws InvalidValueException {
        if (this.valueExists(name)) {
            ConfigEntry e = this.getAsEntry(name);
            if (e.getType() == ConfigEntry.EntryType.FLOAT) {
                e.setValue(String.valueOf(value));
                this.syncConfig();
            } else {
                throw new InvalidValueException("This value's type is " + e.getType() + "! It isn't possible to set a FLOAT value to it!");
            }
        }
    }

    public void setValue(String name, Double value) throws InvalidValueException {
        if (this.valueExists(name)) {
            ConfigEntry e = this.getAsEntry(name);
            if (e.getType() == ConfigEntry.EntryType.DOUBLE) {
                e.setValue(String.valueOf(value));
                this.syncConfig();
            } else {
                throw new InvalidValueException("This value's type is " + e.getType() + "! It isn't possible to set a DOUBLE value to it!");
            }
        }
    }

    public void setValue(String name, Long value) throws InvalidValueException {
        if (this.valueExists(name)) {
            ConfigEntry e = this.getAsEntry(name);
            if (e.getType() == ConfigEntry.EntryType.LONG) {
                e.setValue(String.valueOf(value));
                this.syncConfig();
            } else {
                throw new InvalidValueException("This value's type is " + e.getType() + "! It isn't possible to set a LONG value to it!");
            }
        }
    }

    public void unregisterValue(String name) {
        if (this.valueExists(name)) {
            this.values.remove(name);
            if (this.registeredValues.contains(name)) {
                this.registeredValues.remove(name);
            }
        }
    }

    public Boolean getBoolean(String name) throws InvalidValueException {
        if (!this.valueExists(name)) {
            throw new InvalidValueException("This value does not exist! (" + name + ")");
        } else {
            ConfigEntry e = this.getAsEntry(name);
            if (e.getType() == ConfigEntry.EntryType.BOOLEAN) {
                if (e.getValue().equalsIgnoreCase("true")) {
                    return true;
                } else if (e.getValue().equalsIgnoreCase("false")) {
                    return false;
                } else {
                    throw new InvalidValueException("This value is not a valid BOOLEAN value!");
                }
            } else {
                throw new InvalidValueException("This value's type is not BOOLEAN!");
            }
        }
    }

    public String getString(String name) throws InvalidValueException {
        if (!this.valueExists(name)) {
            throw new InvalidValueException("This value does not exist! (" + name + ")");
        } else {
            ConfigEntry e = this.getAsEntry(name);
            if (e.getType() == ConfigEntry.EntryType.STRING) {
                return e.getValue();
            } else {
                throw new InvalidValueException("This value's type is not STRING!");
            }
        }
    }

    public Integer getInteger(String name) throws InvalidValueException {
        if (!this.valueExists(name)) {
            throw new InvalidValueException("This value does not exist! (" + name + ")");
        } else {
            ConfigEntry e = this.getAsEntry(name);
            if (e.getType() == ConfigEntry.EntryType.INTEGER) {
                if (MathUtils.isInteger(e.getValue())) {
                    return Integer.parseInt(e.getValue());
                } else {
                    throw new InvalidValueException("This value is not a valid INTEGER value!");
                }
            } else {
                throw new InvalidValueException("This value's type is not INTEGER!");
            }
        }
    }

    public Double getDouble(String name) throws InvalidValueException {
        if (!this.valueExists(name)) {
            throw new InvalidValueException("This value does not exist! (" + name + ")");
        } else {
            ConfigEntry e = this.getAsEntry(name);
            if (e.getType() == ConfigEntry.EntryType.DOUBLE) {
                if (MathUtils.isDouble(e.getValue())) {
                    return Double.parseDouble(e.getValue());
                } else {
                    throw new InvalidValueException("This value is not a valid DOUBLE value!");
                }
            } else {
                throw new InvalidValueException("This value's type is not DOUBLE!");
            }
        }
    }

    public Long getLong(String name) throws InvalidValueException {
        if (!this.valueExists(name)) {
            throw new InvalidValueException("This value does not exist! (" + name + ")");
        } else {
            ConfigEntry e = this.getAsEntry(name);
            if (e.getType() == ConfigEntry.EntryType.LONG) {
                if (MathUtils.isLong(e.getValue())) {
                    return Long.parseLong(e.getValue());
                } else {
                    throw new InvalidValueException("This value is not a valid LONG value!");
                }
            } else {
                throw new InvalidValueException("This value's type is not LONG!");
            }
        }
    }

    public Float getFloat(String name) throws InvalidValueException {
        if (!this.valueExists(name)) {
            throw new InvalidValueException("This value does not exist! (" + name + ")");
        } else {
            ConfigEntry e = this.getAsEntry(name);
            if (e.getType() == ConfigEntry.EntryType.FLOAT) {
                if (MathUtils.isFloat(e.getValue())) {
                    return Float.parseFloat(e.getValue());
                } else {
                    throw new InvalidValueException("This value is not a valid LONG value!");
                }
            } else {
                throw new InvalidValueException("This value's type is not LONG!");
            }
        }
    }

    public void setCategory(String valueName, String category) throws InvalidValueException {
        if (this.valueExists(valueName)) {
            ConfigEntry e = this.getAsEntry(valueName);
            e.setCategory(category);
            if (!this.categorys.contains(category)) {
                this.categorys.add(category);
            }
            this.syncConfig();
        } else {
            throw new InvalidValueException("This values does not exist! (" + valueName + ")");
        }
    }

    public void setDescription(String valueName, String description) throws InvalidValueException {
        if (this.valueExists(valueName)) {
            ConfigEntry e = this.getAsEntry(valueName);
            e.setDescription(description);
            this.syncConfig();
        } else {
            throw new InvalidValueException("This values does not exist! (" + valueName + ")");
        }
    }

    public boolean valueExists(String name) {
        return this.values.containsKey(name);
    }

    public void clearUnusedValues() {
        List<String> l = new ArrayList();
        for (Entry<String, ConfigEntry> m : this.values.entrySet()) {
            if (!this.registeredValues.contains(m.getKey())) {
                l.add((String) m.getKey());
            }
        }
        for (String s : l) {
            this.unregisterValue(s);
        }
        this.syncConfig();
    }

    public <T> T getOrDefault(String valueName, T defaultValue) {
        try {
            if (defaultValue instanceof Integer) {
                return (T) this.getInteger(valueName);
            }
            if (defaultValue instanceof Boolean) {
                return (T) this.getBoolean(valueName);
            }
            if (defaultValue instanceof String) {
                return (T) this.getString(valueName);
            }
            if (defaultValue instanceof Long) {
                return (T) this.getLong(valueName);
            }
            if (defaultValue instanceof Double) {
                return (T) this.getDouble(valueName);
            }
            if (defaultValue instanceof Float) {
                return (T) this.getFloat(valueName);
            }
        } catch (Exception var4) {
        }
        return defaultValue;
    }
}