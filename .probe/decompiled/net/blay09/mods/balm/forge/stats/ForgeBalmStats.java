package net.blay09.mods.balm.forge.stats;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.blay09.mods.balm.api.stats.BalmStats;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ForgeBalmStats implements BalmStats {

    private final Map<String, ForgeBalmStats.Registrations> registrations = new ConcurrentHashMap();

    @Override
    public void registerCustomStat(ResourceLocation identifier) {
        this.getActiveRegistrations().customStats.add(identifier);
    }

    public void register() {
        FMLJavaModLoadingContext.get().getModEventBus().register(this.getActiveRegistrations());
    }

    private ForgeBalmStats.Registrations getActiveRegistrations() {
        return (ForgeBalmStats.Registrations) this.registrations.computeIfAbsent(ModLoadingContext.get().getActiveNamespace(), it -> new ForgeBalmStats.Registrations());
    }

    private static class Registrations {

        public final List<ResourceLocation> customStats = new ArrayList();

        @SubscribeEvent
        public void commonSetup(FMLCommonSetupEvent event) {
            event.enqueueWork(() -> this.customStats.forEach(it -> {
                Registry.register(BuiltInRegistries.CUSTOM_STAT, it.getPath(), it);
                Stats.CUSTOM.get(it, StatFormatter.DEFAULT);
            }));
        }
    }
}