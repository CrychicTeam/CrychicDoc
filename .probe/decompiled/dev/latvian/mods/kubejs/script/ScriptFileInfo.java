package dev.latvian.mods.kubejs.script;

import dev.architectury.platform.Platform;
import dev.latvian.mods.kubejs.CommonProperties;
import dev.latvian.mods.kubejs.util.UtilsJS;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.resources.ResourceLocation;

public class ScriptFileInfo {

    private static final Pattern FILE_FIXER = Pattern.compile("[^\\w./]");

    private static final Pattern PROPERTY_PATTERN = Pattern.compile("^(\\w+)\\s*[:=]?\\s*(-?\\w+)$");

    public final ScriptPackInfo pack;

    public final String file;

    public final ResourceLocation id;

    public final String location;

    private final Map<String, List<String>> properties;

    private int priority;

    private boolean ignored;

    private String packMode;

    private final Set<String> requiredMods;

    public String[] lines;

    public ScriptFileInfo(ScriptPackInfo p, String f) {
        this.pack = p;
        this.file = f;
        this.id = new ResourceLocation(this.pack.namespace, FILE_FIXER.matcher(this.pack.pathStart + this.file).replaceAll("_").toLowerCase());
        this.location = UtilsJS.getID(this.pack.namespace + ":" + this.pack.pathStart + this.file);
        this.properties = new HashMap();
        this.priority = 0;
        this.ignored = false;
        this.packMode = "";
        this.requiredMods = new HashSet(0);
        this.lines = UtilsJS.EMPTY_STRING_ARRAY;
    }

    public void preload(ScriptSource source) throws Throwable {
        this.properties.clear();
        this.priority = 0;
        this.ignored = false;
        this.lines = (String[]) source.readSource(this).toArray(UtilsJS.EMPTY_STRING_ARRAY);
        for (int i = 0; i < this.lines.length; i++) {
            String tline = this.lines[i].trim();
            if (tline.isEmpty() || tline.startsWith("import ")) {
                this.lines[i] = "";
            } else if (tline.startsWith("//")) {
                Matcher matcher = PROPERTY_PATTERN.matcher(tline.substring(2).trim());
                if (matcher.find()) {
                    ((List) this.properties.computeIfAbsent(matcher.group(1).trim(), k -> new ArrayList())).add(matcher.group(2).trim());
                }
                this.lines[i] = "";
            }
        }
        this.priority = Integer.parseInt(this.getProperty("priority", "0"));
        this.ignored = this.getProperty("ignored", "false").equals("true") || this.getProperty("ignore", "false").equals("true");
        this.packMode = this.getProperty("packmode", "");
        this.requiredMods.addAll(this.getProperties("requires"));
    }

    public List<String> getProperties(String s) {
        return (List<String>) this.properties.getOrDefault(s, List.of());
    }

    public String getProperty(String s, String def) {
        List<String> l = this.getProperties(s);
        return l.isEmpty() ? def : (String) l.get(l.size() - 1);
    }

    public int getPriority() {
        return this.priority;
    }

    public String skipLoading() {
        if (this.ignored) {
            return "Ignored";
        } else if (!this.packMode.isEmpty() && !this.packMode.equals(CommonProperties.get().packMode)) {
            return "Pack mode mismatch";
        } else {
            if (!this.requiredMods.isEmpty()) {
                for (String mod : this.requiredMods) {
                    if (!Platform.isModLoaded(mod)) {
                        return "Mod " + mod + " is not loaded";
                    }
                }
            }
            return "";
        }
    }
}