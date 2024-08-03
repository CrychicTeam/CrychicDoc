package io.github.apace100.origins;

import com.google.common.collect.ImmutableList;
import io.github.apace100.apoli.util.NamespaceAlias;
import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.power.OriginsEntityConditions;
import io.github.apace100.origins.power.OriginsPowerTypes;
import io.github.apace100.origins.registry.ModBlocks;
import io.github.apace100.origins.registry.ModEnchantments;
import io.github.apace100.origins.registry.ModEntities;
import io.github.apace100.origins.registry.ModItems;
import io.github.apace100.origins.registry.ModLoot;
import io.github.apace100.origins.registry.ModTags;
import io.github.apace100.origins.util.ChoseOriginCriterion;
import io.github.edwinmindcraft.calio.api.CalioAPI;
import io.github.edwinmindcraft.origins.common.OriginsCommon;
import io.github.edwinmindcraft.origins.common.OriginsConfigs;
import io.github.edwinmindcraft.origins.common.registry.OriginArgumentTypes;
import io.github.edwinmindcraft.origins.data.OriginsData;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("origins")
public class Origins {

    public static final String MODID = "origins";

    public static String VERSION = "";

    public static final Logger LOGGER = LogManager.getLogger(Origins.class);

    @Deprecated
    public static Origins.ServerConfig config = new Origins.ServerConfig();

    public Origins() {
        VERSION = ModLoadingContext.get().getActiveContainer().getModInfo().getVersion().toString();
        LOGGER.info("Origins " + VERSION + " is initializing. Have fun!");
        ModLoadingContext.get().registerConfig(Type.COMMON, OriginsConfigs.COMMON_SPECS);
        ModLoadingContext.get().registerConfig(Type.CLIENT, OriginsConfigs.CLIENT_SPECS);
        ModLoadingContext.get().registerConfig(Type.SERVER, OriginsConfigs.SERVER_SPECS);
        NamespaceAlias.addAlias("origins", "apoli");
        OriginsPowerTypes.register();
        OriginsEntityConditions.register();
        OriginArgumentTypes.bootstrap();
        ModBlocks.register();
        ModItems.register();
        ModTags.register();
        ModEnchantments.register();
        ModEntities.register();
        ModLoot.register();
        OriginsCommon.initialize();
        OriginsData.initialize();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> OriginsClient::initialize);
        NamespaceAlias.addAlias("origins", "apoli");
        CriteriaTriggers.register(ChoseOriginCriterion.INSTANCE);
    }

    public static ResourceLocation identifier(String path) {
        return new ResourceLocation("origins", path);
    }

    @Deprecated
    public static class ServerConfig {

        @Deprecated
        public boolean isOriginDisabled(ResourceLocation originId) {
            return !OriginsConfigs.COMMON.isOriginEnabled(originId);
        }

        @Deprecated
        public boolean isPowerDisabled(ResourceLocation originId, ResourceLocation powerId) {
            return !OriginsConfigs.COMMON.isPowerEnabled(originId, powerId);
        }

        @Deprecated
        public boolean addToConfig(Origin origin) {
            return OriginsConfigs.COMMON.updateOriginList(CalioAPI.getDynamicRegistries(), ImmutableList.of(origin.getWrapped()));
        }
    }
}