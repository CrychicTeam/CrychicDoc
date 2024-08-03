package com.sihenzhang.crockpot.data;

import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "crockpot", bus = Bus.MOD)
public class DataGen {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> providerFuture = event.getLookupProvider();
        CrockPotBlockTagsProvider blockTagsProvider = new CrockPotBlockTagsProvider(packOutput, providerFuture, helper);
        generator.addProvider(event.includeServer(), blockTagsProvider);
        generator.addProvider(event.includeServer(), new CrockPotItemTagsProvider(packOutput, providerFuture, blockTagsProvider.m_274426_(), helper));
        generator.addProvider(event.includeServer(), new CrockPotDamageTypeTagsProvider(packOutput, providerFuture, helper));
        generator.addProvider(event.includeServer(), new CrockPotEntityTypeTagsProvider(packOutput, providerFuture, helper));
        generator.addProvider(event.includeServer(), new CrockPotAdvancementProvider(packOutput, providerFuture, helper));
        generator.addProvider(event.includeServer(), new CrockPotLootTableProvider(packOutput));
        generator.addProvider(event.includeServer(), new CrockPotGlobalLootModifierProvider(packOutput));
        generator.addProvider(event.includeServer(), new CrockPotRecipeProvider(packOutput));
        CrockPotBlockStateProvider blockStateProvider = new CrockPotBlockStateProvider(packOutput, helper);
        generator.addProvider(event.includeClient(), blockStateProvider);
        generator.addProvider(event.includeClient(), new CrockPotItemModelProvider(packOutput, blockStateProvider.models().existingFileHelper));
        generator.addProvider(event.includeClient(), new CrockPotSoundDefinitionsProvider(packOutput, helper));
    }
}