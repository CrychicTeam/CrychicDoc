package dev.xkmc.l2complements.init.data;

import net.minecraft.client.KeyMapping;

public enum LCKeys {

    DIG("key.l2mods.dig", "Range Digging Toggle", 96);

    public final String id;

    public final String def;

    public final int key;

    public final KeyMapping map;

    private LCKeys(String id, String def, int key) {
        this.id = id;
        this.def = def;
        this.key = key;
        this.map = new KeyMapping(id, key, "key.categories.l2mods");
    }
}