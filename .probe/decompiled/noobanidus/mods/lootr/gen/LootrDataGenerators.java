package noobanidus.mods.lootr.gen;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.minecraft.DetectedVersion;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "lootr", bus = Bus.MOD)
public class LootrDataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = event.getGenerator().getPackOutput();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();
        LootrBlockTagProvider blocks;
        generator.addProvider(event.includeServer(), blocks = new LootrBlockTagProvider(output, provider, helper));
        generator.addProvider(event.includeServer(), new LootrItemTagsProvider(output, provider, blocks.m_274426_(), helper));
        generator.addProvider(event.includeClient(), new LootrAtlasGenerator(output, helper));
        generator.addProvider(true, new PackMetadataGenerator(output).add(PackMetadataSection.TYPE, new PackMetadataSection(Component.literal("Resources for Lootr"), DetectedVersion.BUILT_IN.getPackVersion(PackType.CLIENT_RESOURCES), (Map) Arrays.stream(PackType.values()).collect(Collectors.toMap(Function.identity(), DetectedVersion.BUILT_IN::m_264084_)))));
        generator.addProvider(true, LootrLootTableProvider.create(output));
    }
}