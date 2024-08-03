package icyllis.modernui.resources;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

public class ResourceTable {

    private static final Object2IntOpenHashMap<String> sIdMap = new Object2IntOpenHashMap();

    public static int lookup(String pack, String type, String name, boolean onlyPublic) {
        String key = name + type + pack + (onlyPublic ? "1" : "0");
        return sIdMap.getInt(key);
    }
}