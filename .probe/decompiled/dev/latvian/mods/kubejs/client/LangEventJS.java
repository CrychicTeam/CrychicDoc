package dev.latvian.mods.kubejs.client;

import dev.latvian.mods.kubejs.event.EventJS;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class LangEventJS extends EventJS {

    public static final Pattern PATTERN = Pattern.compile("[a-z_]+");

    public final String lang;

    public final Map<LangEventJS.Key, String> map;

    public LangEventJS(String lang, Map<LangEventJS.Key, String> map) {
        this.lang = lang;
        this.map = map;
    }

    public void add(String namespace, String key, String value) {
        if (namespace != null && key != null && value != null && !namespace.isEmpty() && !key.isEmpty() && !value.isEmpty()) {
            this.map.put(new LangEventJS.Key(namespace, this.lang, key), value);
        } else {
            throw new IllegalArgumentException("Invalid namespace, key or value: [" + namespace + ", " + key + ", " + value + "]");
        }
    }

    public void addAll(String namespace, Map<String, String> map) {
        for (Entry<String, String> e : map.entrySet()) {
            this.add(namespace, (String) e.getKey(), (String) e.getValue());
        }
    }

    public void add(String key, String value) {
        this.add("minecraft", key, value);
    }

    public void addAll(Map<String, String> map) {
        this.addAll("minecraft", map);
    }

    public void renameItem(ItemStack item, String name) {
        if (item != null && !item.isEmpty()) {
            String d = item.getDescriptionId();
            if (d != null && !d.isEmpty()) {
                this.add(item.kjs$getMod(), d, name);
            }
        }
    }

    public void renameBlock(Block block, String name) {
        if (block != null && block != Blocks.AIR) {
            String d = block.getDescriptionId();
            if (d != null && !d.isEmpty()) {
                this.add(block.kjs$getMod(), d, name);
            }
        }
    }

    public void renameEntity(ResourceLocation id, String name) {
        this.add(id.getNamespace(), "entity." + id.getNamespace() + "." + id.getPath().replace('/', '.'), name);
    }

    public void renameBiome(ResourceLocation id, String name) {
        this.add(id.getNamespace(), "biome." + id.getNamespace() + "." + id.getPath().replace('/', '.'), name);
    }

    public static record Key(String namespace, String lang, String key) {
    }
}