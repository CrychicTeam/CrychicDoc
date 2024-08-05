package de.keksuccino.fancymenu.customization.variables;

import de.keksuccino.fancymenu.FancyMenu;
import de.keksuccino.fancymenu.util.properties.PropertiesParser;
import de.keksuccino.fancymenu.util.properties.PropertyContainer;
import de.keksuccino.fancymenu.util.properties.PropertyContainerSet;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VariableHandler {

    protected static final File VARIABLES_FILE = new File(FancyMenu.MOD_DIR.getPath() + "/user_variables.db");

    protected static final Map<String, Variable> VARIABLES = new HashMap();

    public static void init() {
        readFromFile();
        for (Variable v : getVariables()) {
            if (v.resetOnLaunch) {
                v.value = "";
            }
        }
        writeToFile();
    }

    public static void setVariable(@NotNull String name, @Nullable String value) {
        Variable v = getVariable(name);
        if (v == null) {
            v = new Variable(name);
            VARIABLES.put(name, v);
        }
        v.setValue(value);
        writeToFile();
    }

    public static void removeVariable(String name) {
        VARIABLES.remove(name);
        writeToFile();
    }

    @Nullable
    public static Variable getVariable(String name) {
        return (Variable) VARIABLES.get(name);
    }

    @NotNull
    public static List<Variable> getVariables() {
        return new ArrayList(VARIABLES.values());
    }

    @NotNull
    public static List<String> getVariableNames() {
        return new ArrayList(VARIABLES.keySet());
    }

    public static void clearVariables() {
        VARIABLES.clear();
        writeToFile();
    }

    public static boolean variableExists(@NotNull String name) {
        return getVariable(name) != null;
    }

    protected static void writeToFile() {
        try {
            if (!VARIABLES_FILE.exists()) {
                VARIABLES_FILE.createNewFile();
            }
            PropertyContainerSet set = new PropertyContainerSet("user_variables");
            for (Variable v : VARIABLES.values()) {
                set.putContainer(v.serialize());
            }
            PropertiesParser.serializeSetToFile(set, VARIABLES_FILE.getPath());
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    protected static void readFromFile() {
        try {
            if (!VARIABLES_FILE.exists()) {
                writeToFile();
            }
            VARIABLES.clear();
            PropertyContainerSet set = PropertiesParser.deserializeSetFromFile(VARIABLES_FILE.getPath());
            if (set != null) {
                if (set.getType().equals("cached_variables")) {
                    readFromLegacyFile();
                } else {
                    for (PropertyContainer c : set.getContainersOfType("variable")) {
                        Variable v = Variable.deserialize(c);
                        if (v != null) {
                            VARIABLES.put(v.name, v);
                        }
                    }
                }
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        }
    }

    protected static void readFromLegacyFile() {
        try {
            if (!VARIABLES_FILE.exists()) {
                writeToFile();
            }
            VARIABLES.clear();
            PropertyContainerSet set = PropertiesParser.deserializeSetFromFile(VARIABLES_FILE.getPath());
            if (set != null) {
                List<PropertyContainer> secs = set.getContainersOfType("variables");
                if (!secs.isEmpty()) {
                    PropertyContainer sec = (PropertyContainer) secs.get(0);
                    for (Entry<String, String> m : sec.getProperties().entrySet()) {
                        Variable v = new Variable((String) m.getKey());
                        v.value = (String) m.getValue();
                        VARIABLES.put((String) m.getKey(), v);
                    }
                }
            }
        } catch (Exception var6) {
            var6.printStackTrace();
        }
    }
}