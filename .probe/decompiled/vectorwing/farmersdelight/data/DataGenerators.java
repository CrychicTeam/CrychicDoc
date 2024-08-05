package vectorwing.farmersdelight.data;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import vectorwing.farmersdelight.data.loot.FDBlockLoot;
import vectorwing.farmersdelight.data.tools.StructureUpdater;

@EventBusSubscriber(modid = "farmersdelight", bus = Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();
        BlockTags blockTags = new BlockTags(output, lookupProvider, helper);
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(event.includeServer(), new ItemTags(output, lookupProvider, blockTags.m_274426_(), helper));
        generator.addProvider(event.includeServer(), new EntityTags(output, lookupProvider, helper));
        generator.addProvider(event.includeServer(), new Recipes(output));
        generator.addProvider(event.includeServer(), new Advancements(output, lookupProvider, helper));
        generator.addProvider(event.includeServer(), new LootTableProvider(output, Collections.emptySet(), List.of(new LootTableProvider.SubProviderEntry(FDBlockLoot::new, LootContextParamSets.BLOCK))));
        generator.addProvider(event.includeServer(), new StructureUpdater("structures/village/houses", "farmersdelight", helper, output));
        BlockStates blockStates = new BlockStates(output, helper);
        generator.addProvider(event.includeClient(), blockStates);
        generator.addProvider(event.includeClient(), new ItemModels(output, blockStates.models().existingFileHelper));
    }
}