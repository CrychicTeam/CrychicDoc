package com.mna.apibridge;

import com.mna.ManaAndArtifice;
import com.mna.Registries;
import com.mna.api.ManaAndArtificeMod;
import com.mna.cantrips.CantripRegistry;
import com.mna.capabilities.chunkdata.ChunkMagicProvider;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.capabilities.worlddata.WorldMagicProvider;
import com.mna.config.SpellConfig;
import com.mna.config.SpellConfigProvider;
import com.mna.factions.Factions;
import com.mna.spells.SpellCaster;
import com.mna.tools.PortalHelper;
import com.mna.tools.SummonUtils;
import com.mna.tools.render.WorldRenderUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber(modid = "mna", bus = Bus.MOD)
public class APIBridge {

    public static void earlyInit() {
        ManaAndArtificeMod.setAPIHelper(new ConfigHelper());
    }

    @SubscribeEvent
    public static void onLoadComplete(FMLLoadCompleteEvent event) {
        ManaAndArtificeMod.setAPIHelper(new PortalHelper());
        ManaAndArtificeMod.setAPIHelper(new SpellCaster());
        ManaAndArtificeMod.setAPIHelper(new EntityHelper());
        ManaAndArtificeMod.setMagicCapability(PlayerMagicProvider.MAGIC);
        ManaAndArtificeMod.setProgressionCapability(PlayerProgressionProvider.PROGRESSION);
        ManaAndArtificeMod.setWorldMagicCapability(WorldMagicProvider.MAGIC);
        ManaAndArtificeMod.setChunkMagicCapability(ChunkMagicProvider.MAGIC);
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            ManaAndArtificeMod.setAPIHelper(WorldRenderUtils.INSTANCE);
            ManaAndArtificeMod.setAPIHelper(new GuiRenderHelper());
        });
        ManaAndArtificeMod.setAPIHelper(new SummonUtils());
        ManaAndArtificeMod.setAPIHelper(Factions.INSTANCE);
        ManaAndArtificeMod.setAPIHelper(new InventoryHelper());
        ManaAndArtificeMod.setAPIHelper(new ParticleSerializerHelper());
        ManaAndArtificeMod.setAPIHelper(new AdvancementHelper());
        ManaAndArtificeMod.setAPIHelper(new ItemHelper());
        ManaAndArtificeMod.setAPIHelper(new ContainerHelper());
        ManaAndArtificeMod.setCantripRegistry(CantripRegistry.INSTANCE);
        SpellConfigProvider.initGeneralSpellConfigs(SpellConfig.SPELLS_CONFIG_BUILDER);
        ((IForgeRegistry) Registries.Shape.get()).forEach(s -> s.onRegistered());
        ((IForgeRegistry) Registries.SpellEffect.get()).forEach(s -> s.onRegistered());
        ((IForgeRegistry) Registries.Modifier.get()).forEach(s -> s.onRegistered());
        SpellConfig.finalizeServerConfig();
        ManaAndArtifice.LOGGER.info("M&A API initialized, it is now useable");
    }
}