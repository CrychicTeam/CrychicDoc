package dev.ftb.mods.ftblibrary.icon;

import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

public class IconProperties {

    private final Map<String, String> map = new LinkedHashMap();

    public void set(String key, String value) {
        this.map.remove(key);
        this.map.put(key, value);
    }

    public String getString(String key, String def) {
        return (String) this.map.getOrDefault(key, def);
    }

    public int getInt(String key, int def, int min, int max) {
        return this.map.containsKey(key) ? Mth.clamp(Integer.parseInt((String) this.map.get(key)), min, max) : def;
    }

    public int getInt(String key, int def) {
        return this.getInt(key, def, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public double getDouble(String key, double def, double min, double max) {
        return this.map.containsKey(key) ? Mth.clamp(Double.parseDouble((String) this.map.get(key)), min, max) : def;
    }

    public double getDouble(String key, double def) {
        return this.getDouble(key, def, -Double.MAX_VALUE, Double.MAX_VALUE);
    }

    public boolean getBoolean(String key, boolean def) {
        return this.map.containsKey(key) ? ((String) this.map.get(key)).equals("1") : def;
    }

    @Nullable
    public Color4I getColor(String key) {
        String s = (String) this.map.get(key);
        return s == null ? null : Color4I.fromString(s);
    }
}