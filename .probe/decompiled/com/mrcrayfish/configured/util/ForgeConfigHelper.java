package com.mrcrayfish.configured.util;

import com.electronwill.nightconfig.core.AbstractConfig;
import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.UnmodifiableConfig;
import com.electronwill.nightconfig.core.file.FileConfig;
import com.electronwill.nightconfig.core.utils.UnmodifiableConfigWrapper;
import com.google.common.collect.ImmutableList;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ConfigTracker;
import net.minecraftforge.fml.config.IConfigEvent;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.apache.commons.lang3.tuple.Pair;

public class ForgeConfigHelper {

    private static final Method MOD_CONFIG_SET_CONFIG_DATA = ObfuscationReflectionHelper.findMethod(ModConfig.class, "setConfigData", new Class[] { CommentedConfig.class });

    private static final Method MOD_CONFIG_FIRE_EVENT = ObfuscationReflectionHelper.findMethod(ModConfig.class, "fireEvent", new Class[] { IConfigEvent.class });

    public static List<Pair<ForgeConfigSpec.ConfigValue<?>, ForgeConfigSpec.ValueSpec>> gatherAllForgeConfigValues(UnmodifiableConfig config, ForgeConfigSpec spec) {
        List<Pair<ForgeConfigSpec.ConfigValue<?>, ForgeConfigSpec.ValueSpec>> values = new ArrayList();
        gatherValuesFromForgeConfig(config, spec, values);
        return ImmutableList.copyOf(values);
    }

    private static void gatherValuesFromForgeConfig(UnmodifiableConfig config, ForgeConfigSpec spec, List<Pair<ForgeConfigSpec.ConfigValue<?>, ForgeConfigSpec.ValueSpec>> values) {
        config.valueMap().forEach((s, o) -> {
            if (o instanceof AbstractConfig) {
                gatherValuesFromForgeConfig((UnmodifiableConfig) o, spec, values);
            } else if (o instanceof ForgeConfigSpec.ConfigValue<?> configValue) {
                ForgeConfigSpec.ValueSpec valueSpec = (ForgeConfigSpec.ValueSpec) spec.getRaw(configValue.getPath());
                values.add(Pair.of(configValue, valueSpec));
            }
        });
    }

    public static void setForgeConfigData(ModConfig config, @Nullable CommentedConfig configData) {
        try {
            MOD_CONFIG_SET_CONFIG_DATA.invoke(config, configData);
            if (configData instanceof FileConfig) {
                config.save();
            }
        } catch (IllegalAccessException | InvocationTargetException var3) {
            var3.printStackTrace();
        }
    }

    @Nullable
    public static ModConfig getForgeConfig(String fileName) {
        return (ModConfig) ConfigTracker.INSTANCE.fileMap().get(fileName);
    }

    public static List<Pair<ForgeConfigSpec.ConfigValue<?>, ForgeConfigSpec.ValueSpec>> gatherAllForgeConfigValues(ModConfig config) {
        return gatherAllForgeConfigValues(((ForgeConfigSpec) config.getSpec()).getValues(), (ForgeConfigSpec) config.getSpec());
    }

    public static void fireForgeConfigEvent(ModConfig config, ModConfigEvent event) {
        try {
            MOD_CONFIG_FIRE_EVENT.invoke(config, event);
        } catch (IllegalAccessException | InvocationTargetException var3) {
            var3.printStackTrace();
        }
    }

    @Nullable
    public static ForgeConfigSpec findConfigSpec(UnmodifiableConfig config) {
        if (config instanceof ForgeConfigSpec) {
            return (ForgeConfigSpec) config;
        } else {
            if (config instanceof UnmodifiableConfigWrapper) {
                try {
                    Field field = UnmodifiableConfigWrapper.class.getDeclaredField("config");
                    field.setAccessible(true);
                    return findConfigSpec((UnmodifiableConfig) MethodHandles.lookup().unreflectGetter(field).invoke(config));
                } catch (Throwable var2) {
                }
            }
            return null;
        }
    }
}