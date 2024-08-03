package de.keksuccino.fancymenu.util.properties;

import de.keksuccino.fancymenu.util.file.FileUtils;
import de.keksuccino.konkrete.input.StringUtils;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Map.Entry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PropertiesParser {

    private static final Logger LOGGER = LogManager.getLogger();

    @Nullable
    public static PropertyContainerSet deserializeSetFromFile(@NotNull String filePath) {
        try {
            File f = new File((String) Objects.requireNonNull(filePath));
            if (f.exists() && f.isFile()) {
                String content = "";
                for (String s : FileUtils.getFileLines(f)) {
                    content = content + s + "\n";
                }
                return deserializeSetFromFancyString(content);
            }
            LOGGER.error("[FANCYMENU] Failed to deserialize PropertyContainerSet! File not found!");
        } catch (Exception var5) {
            var5.printStackTrace();
        }
        return null;
    }

    @Nullable
    public static PropertyContainerSet deserializeSetFromFancyString(@NotNull String serializedFancyString) {
        try {
            String[] lines = StringUtils.splitLines(((String) Objects.requireNonNull(serializedFancyString)).replace("\r", "\n"), "\n");
            List<PropertyContainer> data = new ArrayList();
            String propertiesType = null;
            PropertyContainer currentContainer = null;
            boolean insideData = false;
            for (String line : lines) {
                String compactLine = line.replace(" ", "");
                if (compactLine.startsWith("type=") && !insideData) {
                    propertiesType = compactLine.split("=", 2)[1];
                } else if (compactLine.endsWith("{")) {
                    if (!insideData) {
                        insideData = true;
                    } else {
                        LOGGER.warn("[FANCYMENU] Broken PropertyContainer found! Leaking container, missing '}': " + (currentContainer != null ? serializeContainerToFancyString(currentContainer).replace("\n", "").replace("\r", "") : "null"));
                        data.add(currentContainer);
                    }
                    currentContainer = new PropertyContainer(compactLine.split("[{]")[0]);
                } else if (compactLine.startsWith("}") && insideData) {
                    data.add(currentContainer);
                    insideData = false;
                } else if (insideData && compactLine.contains("=")) {
                    String value = line.split("=", 2)[1];
                    if (value.startsWith(" ")) {
                        value = value.substring(1);
                    }
                    currentContainer.putProperty(compactLine.split("=", 2)[0], value);
                }
            }
            if (propertiesType != null) {
                PropertyContainerSet set = new PropertyContainerSet(propertiesType);
                for (PropertyContainer d : data) {
                    set.putContainer(d);
                }
                return set;
            }
            LOGGER.error("[FANCYMENU] Failed to deserialize PropertyContainerSet! Missing type: " + serializedFancyString.replace("\n", "").replace("\r", ""));
        } catch (Exception var12) {
            var12.printStackTrace();
        }
        return null;
    }

    @Nullable
    public static PropertyContainerSet deserializeSetFromStream(@NotNull InputStream in) {
        try {
            List<String> dbTextLines = FileUtils.readTextLinesFrom(in);
            String fancyString = buildFancyStringFromList(dbTextLines);
            return deserializeSetFromFancyString(fancyString);
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static void serializeSetToFile(@NotNull PropertyContainerSet set, @NotNull String filePath) {
        try {
            File f = new File(filePath);
            File parentDir = f.getParentFile();
            if (parentDir != null && !parentDir.isDirectory()) {
                parentDir.mkdirs();
            }
            f.createNewFile();
            FileUtils.writeTextToFile(f, false, new String[] { serializeSetToFancyString(set) });
        } catch (Exception var4) {
            var4.printStackTrace();
        }
    }

    @NotNull
    public static String serializeContainerToFancyString(@NotNull PropertyContainer container) {
        String s = ((PropertyContainer) Objects.requireNonNull(container)).getType() + " {\n";
        for (Entry<String, String> e : container.getProperties().entrySet()) {
            s = s + "  " + (String) e.getKey() + " = " + (String) e.getValue() + "\n";
        }
        return s + "}";
    }

    @NotNull
    public static String serializeSetToFancyString(@NotNull PropertyContainerSet set) {
        String s = "type = " + ((PropertyContainerSet) Objects.requireNonNull(set)).getType() + "\n\n";
        for (PropertyContainer c : set.getContainers()) {
            s = s + serializeContainerToFancyString(c);
            s = s + "\n\n";
        }
        return s;
    }

    @NotNull
    public static String buildFancyStringFromList(@NotNull List<String> list) {
        String fancy = "";
        for (String s : list) {
            fancy = fancy + s + "\n";
        }
        return fancy;
    }

    @NotNull
    public static String stringifyFancyString(@NotNull String fancyString) {
        return ((String) Objects.requireNonNull(fancyString)).replace("\n", "$prop_line_break$").replace("\r", "$prop_line_break$").replace("{", "$prop_brackets_open$").replace("}", "$prop_brackets_close$");
    }

    @NotNull
    public static String unstringify(@NotNull String stringified) {
        return ((String) Objects.requireNonNull(stringified)).replace("$prop_line_break$", "\n").replace("$prop_brackets_open$", "{").replace("$prop_brackets_close$", "}");
    }
}