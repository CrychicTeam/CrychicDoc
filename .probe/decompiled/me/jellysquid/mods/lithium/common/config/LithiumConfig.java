package me.jellysquid.mods.lithium.common.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import me.jellysquid.mods.lithium.common.LithiumMod;
import me.jellysquid.mods.lithium.common.compat.worldedit.WorldEditCompat;
import net.caffeinemc.caffeineconfig.AbstractCaffeineConfigMixinPlugin;
import net.caffeinemc.caffeineconfig.CaffeineConfig;
import net.caffeinemc.caffeineconfig.Option;
import net.caffeinemc.caffeineconfig.CaffeineConfig.Builder;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.loading.LoadingModList;

public class LithiumConfig extends AbstractCaffeineConfigMixinPlugin {

    private CaffeineConfig applyLithiumCompat(CaffeineConfig config) {
        if (LoadingModList.get().getModFileById("ferritecore") != null) {
            config.getOption("mixin.alloc.blockstate").addModOverride(false, "ferritecore");
        }
        Option option = config.getOption("mixin.block.hopper.worldedit_compat");
        if (!option.isEnabled() && WorldEditCompat.WORLD_EDIT_PRESENT) {
            option.addModOverride(true, "radium");
        }
        if (!LoadingModList.get().getErrors().isEmpty()) {
            for (Option op : config.getOptions().values()) {
                op.addModOverride(false, "fml-loading-error");
            }
        }
        return config;
    }

    public LithiumConfig() {
        LithiumMod.CONFIG = this;
    }

    protected CaffeineConfig createConfig() {
        Builder builder = CaffeineConfig.builder("Radium").withInfoUrl("https://github.com/jellysquid3/lithium-fabric/wiki/Configuration-File").withSettingsKey("lithium:options");
        InputStream defaultPropertiesStream = LithiumConfig.class.getResourceAsStream("/assets/lithium/lithium-mixin-config-default.properties");
        if (defaultPropertiesStream == null) {
            throw new IllegalStateException("Lithium mixin config default properties could not be read!");
        } else {
            try {
                BufferedReader propertiesReader = new BufferedReader(new InputStreamReader(defaultPropertiesStream));
                try {
                    Properties properties = new Properties();
                    properties.load(propertiesReader);
                    properties.forEach((ruleName, enabled) -> builder.addMixinRule((String) ruleName, Boolean.parseBoolean((String) enabled)));
                } catch (Throwable var11) {
                    try {
                        propertiesReader.close();
                    } catch (Throwable var8) {
                        var11.addSuppressed(var8);
                    }
                    throw var11;
                }
                propertiesReader.close();
            } catch (IOException var12) {
                var12.printStackTrace();
                throw new IllegalStateException("Lithium mixin config default properties could not be read!");
            }
            InputStream dependenciesStream = LithiumConfig.class.getResourceAsStream("/assets/lithium/lithium-mixin-config-dependencies.properties");
            if (dependenciesStream == null) {
                throw new IllegalStateException("Lithium mixin config dependencies could not be read!");
            } else {
                try {
                    BufferedReader propertiesReader = new BufferedReader(new InputStreamReader(dependenciesStream));
                    try {
                        Properties properties = new Properties();
                        properties.load(propertiesReader);
                        properties.forEach((o1, o2) -> {
                            String rulename = (String) o1;
                            String dependencies = (String) o2;
                            String[] dependenciesSplit = dependencies.split(",");
                            for (String dependency : dependenciesSplit) {
                                String[] split = dependency.split(":");
                                if (split.length != 2) {
                                    return;
                                }
                                String dependencyName = split[0];
                                String requiredState = split[1];
                                builder.addRuleDependency(rulename, dependencyName, Boolean.parseBoolean(requiredState));
                            }
                        });
                    } catch (Throwable var9) {
                        try {
                            propertiesReader.close();
                        } catch (Throwable var7) {
                            var9.addSuppressed(var7);
                        }
                        throw var9;
                    }
                    propertiesReader.close();
                } catch (IOException var10) {
                    var10.printStackTrace();
                    throw new IllegalStateException("Lithium mixin config dependencies could not be read!");
                }
                return this.applyLithiumCompat(builder.build(FMLPaths.CONFIGDIR.get().resolve("lithium.properties")));
            }
        }
    }

    protected String mixinPackageRoot() {
        return "me.jellysquid.mods.lithium.mixin.";
    }
}