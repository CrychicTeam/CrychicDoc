package net.minecraft.data;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.WorldVersion;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.advancements.packs.VanillaAdvancementProvider;
import net.minecraft.data.info.BiomeParametersDumpReport;
import net.minecraft.data.info.BlockListReport;
import net.minecraft.data.info.CommandsReport;
import net.minecraft.data.info.RegistryDumpReport;
import net.minecraft.data.loot.packs.VanillaLootTableProvider;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.data.models.ModelProvider;
import net.minecraft.data.recipes.packs.BundleRecipeProvider;
import net.minecraft.data.recipes.packs.VanillaRecipeProvider;
import net.minecraft.data.registries.RegistriesDatapackGenerator;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.data.structures.NbtToSnbt;
import net.minecraft.data.structures.SnbtToNbt;
import net.minecraft.data.structures.StructureUpdater;
import net.minecraft.data.tags.BannerPatternTagsProvider;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.data.tags.CatVariantTagsProvider;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.data.tags.FlatLevelGeneratorPresetTagsProvider;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.data.tags.GameEventTagsProvider;
import net.minecraft.data.tags.InstrumentTagsProvider;
import net.minecraft.data.tags.PaintingVariantTagsProvider;
import net.minecraft.data.tags.PoiTypeTagsProvider;
import net.minecraft.data.tags.StructureTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.data.tags.VanillaBlockTagsProvider;
import net.minecraft.data.tags.VanillaItemTagsProvider;
import net.minecraft.data.tags.WorldPresetTagsProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.obfuscate.DontObfuscate;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class Main {

    @DontObfuscate
    public static void main(String[] string0) throws IOException {
        SharedConstants.tryDetectVersion();
        OptionParser $$1 = new OptionParser();
        OptionSpec<Void> $$2 = $$1.accepts("help", "Show the help menu").forHelp();
        OptionSpec<Void> $$3 = $$1.accepts("server", "Include server generators");
        OptionSpec<Void> $$4 = $$1.accepts("client", "Include client generators");
        OptionSpec<Void> $$5 = $$1.accepts("dev", "Include development tools");
        OptionSpec<Void> $$6 = $$1.accepts("reports", "Include data reports");
        OptionSpec<Void> $$7 = $$1.accepts("validate", "Validate inputs");
        OptionSpec<Void> $$8 = $$1.accepts("all", "Include all generators");
        OptionSpec<String> $$9 = $$1.accepts("output", "Output folder").withRequiredArg().defaultsTo("generated", new String[0]);
        OptionSpec<String> $$10 = $$1.accepts("input", "Input folder").withRequiredArg();
        OptionSet $$11 = $$1.parse(string0);
        if (!$$11.has($$2) && $$11.hasOptions()) {
            Path $$12 = Paths.get((String) $$9.value($$11));
            boolean $$13 = $$11.has($$8);
            boolean $$14 = $$13 || $$11.has($$4);
            boolean $$15 = $$13 || $$11.has($$3);
            boolean $$16 = $$13 || $$11.has($$5);
            boolean $$17 = $$13 || $$11.has($$6);
            boolean $$18 = $$13 || $$11.has($$7);
            DataGenerator $$19 = createStandardGenerator($$12, (Collection<Path>) $$11.valuesOf($$10).stream().map(p_129659_ -> Paths.get(p_129659_)).collect(Collectors.toList()), $$14, $$15, $$16, $$17, $$18, SharedConstants.getCurrentVersion(), true);
            $$19.run();
        } else {
            $$1.printHelpOn(System.out);
        }
    }

    private static <T extends DataProvider> DataProvider.Factory<T> bindRegistries(BiFunction<PackOutput, CompletableFuture<HolderLookup.Provider>, T> biFunctionPackOutputCompletableFutureHolderLookupProviderT0, CompletableFuture<HolderLookup.Provider> completableFutureHolderLookupProvider1) {
        return p_255476_ -> (DataProvider) biFunctionPackOutputCompletableFutureHolderLookupProviderT0.apply(p_255476_, completableFutureHolderLookupProvider1);
    }

    public static DataGenerator createStandardGenerator(Path path0, Collection<Path> collectionPath1, boolean boolean2, boolean boolean3, boolean boolean4, boolean boolean5, boolean boolean6, WorldVersion worldVersion7, boolean boolean8) {
        DataGenerator $$9 = new DataGenerator(path0, worldVersion7, boolean8);
        DataGenerator.PackGenerator $$10 = $$9.getVanillaPack(boolean2 || boolean3);
        $$10.addProvider(p_253388_ -> new SnbtToNbt(p_253388_, collectionPath1).addFilter(new StructureUpdater()));
        CompletableFuture<HolderLookup.Provider> $$11 = CompletableFuture.supplyAsync(VanillaRegistries::m_255371_, Util.backgroundExecutor());
        DataGenerator.PackGenerator $$12 = $$9.getVanillaPack(boolean2);
        $$12.addProvider(ModelProvider::new);
        DataGenerator.PackGenerator $$13 = $$9.getVanillaPack(boolean3);
        $$13.addProvider(bindRegistries(RegistriesDatapackGenerator::new, $$11));
        $$13.addProvider(bindRegistries(VanillaAdvancementProvider::m_255090_, $$11));
        $$13.addProvider(VanillaLootTableProvider::m_247452_);
        $$13.addProvider(VanillaRecipeProvider::new);
        TagsProvider<Block> $$14 = $$13.addProvider(bindRegistries(VanillaBlockTagsProvider::new, $$11));
        TagsProvider<Item> $$15 = $$13.addProvider(p_274753_ -> new VanillaItemTagsProvider(p_274753_, $$11, $$14.contentsGetter()));
        $$13.addProvider(bindRegistries(BannerPatternTagsProvider::new, $$11));
        $$13.addProvider(bindRegistries(BiomeTagsProvider::new, $$11));
        $$13.addProvider(bindRegistries(CatVariantTagsProvider::new, $$11));
        $$13.addProvider(bindRegistries(DamageTypeTagsProvider::new, $$11));
        $$13.addProvider(bindRegistries(EntityTypeTagsProvider::new, $$11));
        $$13.addProvider(bindRegistries(FlatLevelGeneratorPresetTagsProvider::new, $$11));
        $$13.addProvider(bindRegistries(FluidTagsProvider::new, $$11));
        $$13.addProvider(bindRegistries(GameEventTagsProvider::new, $$11));
        $$13.addProvider(bindRegistries(InstrumentTagsProvider::new, $$11));
        $$13.addProvider(bindRegistries(PaintingVariantTagsProvider::new, $$11));
        $$13.addProvider(bindRegistries(PoiTypeTagsProvider::new, $$11));
        $$13.addProvider(bindRegistries(StructureTagsProvider::new, $$11));
        $$13.addProvider(bindRegistries(WorldPresetTagsProvider::new, $$11));
        $$13 = $$9.getVanillaPack(boolean4);
        $$13.addProvider(p_253386_ -> new NbtToSnbt(p_253386_, collectionPath1));
        $$13 = $$9.getVanillaPack(boolean5);
        $$13.addProvider(bindRegistries(BiomeParametersDumpReport::new, $$11));
        $$13.addProvider(BlockListReport::new);
        $$13.addProvider(bindRegistries(CommandsReport::new, $$11));
        $$13.addProvider(RegistryDumpReport::new);
        $$13 = $$9.getBuiltinDatapack(boolean3, "bundle");
        $$13.addProvider(BundleRecipeProvider::new);
        $$13.addProvider(p_253392_ -> PackMetadataGenerator.forFeaturePack(p_253392_, Component.translatable("dataPack.bundle.description"), FeatureFlagSet.of(FeatureFlags.BUNDLE)));
        return $$9;
    }
}