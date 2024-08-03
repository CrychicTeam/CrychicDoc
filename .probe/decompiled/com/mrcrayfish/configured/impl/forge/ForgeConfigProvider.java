package com.mrcrayfish.configured.impl.forge;

import com.mrcrayfish.configured.Constants;
import com.mrcrayfish.configured.api.IModConfig;
import com.mrcrayfish.configured.api.IModConfigProvider;
import com.mrcrayfish.configured.api.ModContext;
import com.mrcrayfish.configured.util.ForgeConfigHelper;
import com.mrcrayfish.configured.util.OptiFineHelper;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ConfigTracker;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.Type;

public class ForgeConfigProvider implements IModConfigProvider {

    @Override
    public Set<IModConfig> getConfigurationsForMod(ModContext context) {
        Set<IModConfig> configs = new HashSet();
        addForgeConfigSetToMap(context, Type.CLIENT, configs::add);
        addForgeConfigSetToMap(context, Type.COMMON, configs::add);
        addForgeConfigSetToMap(context, Type.SERVER, configs::add);
        return configs;
    }

    private static void addForgeConfigSetToMap(ModContext context, Type type, Consumer<IModConfig> consumer) {
        if (type == Type.CLIENT && OptiFineHelper.isLoaded() && context.modId().equals("forge")) {
            Constants.LOG.info("Ignoring Forge's client config since OptiFine was detected");
        } else {
            for (ModConfig config : (Set) ConfigTracker.INSTANCE.configSets().get(type)) {
                if (config.getModId().equals(context.modId())) {
                    ForgeConfigSpec spec = ForgeConfigHelper.findConfigSpec(config.getSpec());
                    if (spec != null) {
                        consumer.accept(new ForgeConfig(config, spec));
                    }
                }
            }
        }
    }
}