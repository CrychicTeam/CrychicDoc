package io.github.edwinmindcraft.origins.data;

import io.github.edwinmindcraft.origins.data.generator.OriginsBlockStateProvider;
import io.github.edwinmindcraft.origins.data.generator.OriginsBlockTagProvider;
import io.github.edwinmindcraft.origins.data.generator.OriginsItemModelProvider;
import io.github.edwinmindcraft.origins.data.generator.OriginsPowerProvider;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class OriginsData {

    public static void initialize() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(OriginsData::gatherData);
    }

    private static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        generator.addProvider(event.includeClient(), new OriginsBlockStateProvider(generator, existingFileHelper));
        generator.addProvider(event.includeClient(), new OriginsItemModelProvider(generator, existingFileHelper));
        generator.addProvider(event.includeServer(), new OriginsBlockTagProvider(generator, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new OriginsPowerProvider(generator, existingFileHelper));
    }
}