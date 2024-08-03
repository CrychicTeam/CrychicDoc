package com.simibubi.create.infrastructure.config;

import com.simibubi.create.content.kinetics.BlockStressValues;
import com.simibubi.create.foundation.config.ConfigBase;
import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;

@EventBusSubscriber(bus = Bus.MOD)
public class AllConfigs {

    private static final Map<Type, ConfigBase> CONFIGS = new EnumMap(Type.class);

    private static CClient client;

    private static CCommon common;

    private static CServer server;

    public static CClient client() {
        return client;
    }

    public static CCommon common() {
        return common;
    }

    public static CServer server() {
        return server;
    }

    public static ConfigBase byType(Type type) {
        return (ConfigBase) CONFIGS.get(type);
    }

    private static <T extends ConfigBase> T register(Supplier<T> factory, Type side) {
        Pair<T, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(builder -> {
            T configx = (T) factory.get();
            configx.registerAll(builder);
            return configx;
        });
        T config = (T) specPair.getLeft();
        config.specification = (ForgeConfigSpec) specPair.getRight();
        CONFIGS.put(side, config);
        return config;
    }

    public static void register(ModLoadingContext context) {
        client = register(CClient::new, Type.CLIENT);
        common = register(CCommon::new, Type.COMMON);
        server = register(CServer::new, Type.SERVER);
        for (Entry<Type, ConfigBase> pair : CONFIGS.entrySet()) {
            context.registerConfig((Type) pair.getKey(), ((ConfigBase) pair.getValue()).specification);
        }
        BlockStressValues.registerProvider(context.getActiveNamespace(), server().kinetics.stressValues);
    }

    @SubscribeEvent
    public static void onLoad(ModConfigEvent.Loading event) {
        for (ConfigBase config : CONFIGS.values()) {
            if (config.specification == event.getConfig().getSpec()) {
                config.onLoad();
            }
        }
    }

    @SubscribeEvent
    public static void onReload(ModConfigEvent.Reloading event) {
        for (ConfigBase config : CONFIGS.values()) {
            if (config.specification == event.getConfig().getSpec()) {
                config.onReload();
            }
        }
    }
}