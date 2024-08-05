package com.github.einjerjar.mc.keymap.keys.extrakeybind;

import com.github.einjerjar.mc.keymap.Keymap;
import com.github.einjerjar.mc.keymap.config.KeymapConfig;
import com.github.einjerjar.mc.keymap.cross.Services;
import com.github.einjerjar.mc.keymap.keys.sources.keymap.KeymapSource;
import com.github.einjerjar.mc.keymap.keys.sources.keymap.KeymapSources;
import com.github.einjerjar.mc.keymap.keys.wrappers.keys.KeyHolder;
import com.github.einjerjar.mc.keymap.keys.wrappers.keys.VanillaKeymap;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.client.KeyMapping;
import org.jetbrains.annotations.NotNull;

public class KeymapRegistry {

    public static final Type TYPE_BIND_MAP_KEY = (new TypeToken<Map<String, KeyComboData>>() {
    }).getType();

    protected static final List<Integer> MODIFIER_KEYS = Arrays.asList(342, 346, 340, 344, 341, 345);

    protected static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    protected static final BiMap<KeyMapping, KeyComboData> bindMap = HashBiMap.create();

    protected static Map<String, KeyComboData> bindMapKey = new HashMap();

    protected static File cfgFile = null;

    protected static boolean loaded = false;

    private KeymapRegistry() {
    }

    protected static synchronized File cfgFile() {
        return cfgFile != null ? cfgFile : Services.PLATFORM.config("keymap-keys.json");
    }

    public static BiMap<KeyMapping, KeyComboData> bindMap() {
        return ImmutableBiMap.copyOf(bindMap);
    }

    public static Map<String, KeyComboData> bindMapKey() {
        return Collections.unmodifiableMap(bindMapKey);
    }

    public static synchronized void load() {
        load(false);
    }

    public static synchronized void load(boolean force) {
        if (!loaded || force) {
            try {
                FileReader reader = new FileReader(cfgFile());
                try {
                    bindMapKey = (Map<String, KeyComboData>) gson.fromJson(reader, TYPE_BIND_MAP_KEY);
                    Set<String> keys = bindMapKey.keySet();
                    for (KeymapSource source : KeymapSources.sources()) {
                        if (source.canUseSource()) {
                            List<KeyHolder> keyHolders = source.getKeyHolders();
                            if (!keyHolders.isEmpty() && keyHolders.get(0) instanceof VanillaKeymap) {
                                for (VanillaKeymap v : keyHolders.stream().map(VanillaKeymap.class::cast).toList()) {
                                    if (keys.contains(v.map().getName())) {
                                        add(v.map(), (KeyComboData) bindMapKey.get(v.map().getName()));
                                    }
                                }
                            }
                        }
                    }
                } catch (Throwable var16) {
                    try {
                        reader.close();
                    } catch (Throwable var15) {
                        var16.addSuppressed(var15);
                    }
                    throw var16;
                }
                reader.close();
            } catch (FileNotFoundException var17) {
                Keymap.logger().info("No keymap complex-key save found");
            } catch (Exception var18) {
                Keymap.logger().error("!! Can't read keymap's complex-key save !!", var18);
            } finally {
                loaded = true;
            }
        }
    }

    public static synchronized void save() {
        try {
            FileWriter writer = new FileWriter(cfgFile());
            try {
                gson.toJson(bindMapKey(), writer);
            } catch (Throwable var4) {
                try {
                    writer.close();
                } catch (Throwable var3) {
                    var4.addSuppressed(var3);
                }
                throw var4;
            }
            writer.close();
        } catch (Exception var5) {
            Keymap.logger().error("!! Can't save key config !!", var5);
        }
    }

    public static void add(@NotNull KeyMapping kb, @NotNull KeyComboData kd) {
        if (bindMap.containsKey(kb)) {
            String msg = "Attempted to add an already existing keybind";
            if (KeymapConfig.instance().crashOnProblematicError()) {
                throw new RuntimeException(msg);
            }
            Keymap.logger().error(msg);
        }
        bindMap.put(kb, kd);
        bindMapKey.put(kb.getName(), kd);
        Keymap.logger().info("Registered complex keybinding [{}]", kd);
        save();
    }

    public static void put(@NotNull KeyMapping kb, @NotNull KeyComboData kd) {
        remove(kd);
        if (!bindMap.containsKey(kb)) {
            add(kb, kd);
        } else {
            bindMapKey.put(kb.getName(), kd);
            bindMap.put(kb, kd);
            Keymap.logger().info("Updated complex keybinding [{}]", kd);
            save();
        }
    }

    public static void remove(@NotNull KeyComboData kd) {
        if (bindMap.inverse().containsKey(kd)) {
            remove((KeyMapping) bindMap.inverse().get(kd));
        }
    }

    public static void remove(@NotNull KeyMapping kb) {
        if (!bindMap.containsKey(kb)) {
            Keymap.logger().warn("Remove ignored, Keymapping [{}] not registered!", kb.getName());
        } else {
            KeyComboData kd = (KeyComboData) bindMap.get(kb);
            bindMap.remove(kb);
            bindMapKey.remove(kb.getName());
            Keymap.logger().info("Removed complex keybinding [{}]", kd);
            save();
        }
    }

    public static void resetAll() {
        bindMap.clear();
        bindMapKey.clear();
        save();
    }

    public static boolean contains(@NotNull KeyMapping kb) {
        return bindMap.containsKey(kb);
    }

    public static boolean containsKey(int k) {
        for (Entry<KeyMapping, KeyComboData> entry : bindMap.entrySet()) {
            if (((KeyComboData) entry.getValue()).keyCode == k) {
                return true;
            }
        }
        return false;
    }

    public static List<KeyMapping> getMappings(int k) {
        List<KeyMapping> m = new ArrayList();
        for (Entry<KeyMapping, KeyComboData> entry : bindMap.entrySet()) {
            if (((KeyComboData) entry.getValue()).keyCode == k) {
                m.add((KeyMapping) entry.getKey());
            }
        }
        return m;
    }

    public static List<Integer> MODIFIER_KEYS() {
        return MODIFIER_KEYS;
    }
}