package snownee.kiwi.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.resources.ResourceLocation;
import snownee.kiwi.Kiwi;
import snownee.kiwi.loader.Platform;

public class KiwiConfigManager {

    public static final List<ConfigHandler> allConfigs = Lists.newLinkedList();

    public static final Map<ResourceLocation, ConfigHandler.Value<Boolean>> modules = Maps.newHashMap();

    private static final Map<Class<?>, ConfigHandler> clazz2Configs = Maps.newHashMap();

    public static synchronized void register(ConfigHandler configHandler) {
        allConfigs.add(configHandler);
        clazz2Configs.put(configHandler.getClazz(), configHandler);
    }

    public static void init() {
        allConfigs.sort(Comparator.comparing(ConfigHandler::getFileName));
        Set<String> settledMods = Sets.newHashSet();
        for (ConfigHandler config : allConfigs) {
            if (config.hasModules()) {
                settledMods.add(config.getModId());
            }
        }
        for (ConfigHandler configx : allConfigs) {
            configx.init();
        }
        for (ResourceLocation rl : Kiwi.defaultOptions.keySet()) {
            if (!settledMods.contains(rl.getNamespace())) {
                settledMods.add(rl.getNamespace());
                ConfigHandler configx = new ConfigHandler(rl.getNamespace(), rl.getNamespace() + "-modules", KiwiConfig.ConfigType.COMMON, null, true);
                configx.init();
            }
        }
        if (Platform.isPhysicalClient() && Platform.isModLoaded("cloth_config")) {
            ClothConfigIntegration.init();
        }
    }

    public static void defineModules(String modId, ConfigHandler builder, boolean subcategory) {
        String prefix = subcategory ? "modules." : "";
        for (Entry<ResourceLocation, Boolean> entry : Kiwi.defaultOptions.entrySet()) {
            ResourceLocation rl = (ResourceLocation) entry.getKey();
            if (rl.getNamespace().equals(modId)) {
                ConfigHandler.Value<Boolean> value = builder.define(prefix + rl.getPath(), (Boolean) entry.getValue(), null, "%s.config.modules.%s".formatted(modId, rl.getPath()));
                value.requiresRestart = true;
                modules.put(rl, value);
            }
        }
    }

    public static void refresh() {
        allConfigs.forEach(ConfigHandler::refresh);
    }

    public static boolean refresh(String fileName) {
        if (fileName.endsWith(".yaml")) {
            fileName = fileName.substring(0, fileName.length() - ".yaml".length());
        }
        for (ConfigHandler config : allConfigs) {
            if (config.getFileName().equals(fileName)) {
                config.refresh();
                return true;
            }
        }
        return false;
    }

    public static ConfigHandler getHandler(Class<?> clazz) {
        return (ConfigHandler) clazz2Configs.get(clazz);
    }
}