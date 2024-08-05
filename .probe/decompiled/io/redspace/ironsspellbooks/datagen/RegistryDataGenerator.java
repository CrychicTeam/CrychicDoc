package io.redspace.ironsspellbooks.datagen;

import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import io.redspace.ironsspellbooks.registries.FeatureRegistry;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class RegistryDataGenerator extends DatapackBuiltinEntriesProvider {

    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder().add(ForgeRegistries.Keys.BIOME_MODIFIERS, FeatureRegistry::bootstrapBiomeModifier).add(Registries.CONFIGURED_FEATURE, FeatureRegistry::bootstrapConfiguredFeature).add(Registries.PLACED_FEATURE, FeatureRegistry::bootstrapPlacedFeature).add(Registries.DAMAGE_TYPE, ISSDamageTypes::bootstrap);

    private RegistryDataGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider, BUILDER, Set.of("minecraft", "irons_spellbooks"));
    }

    public static void addProviders(boolean isServer, DataGenerator generator, PackOutput output, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper helper) {
        generator.addProvider(isServer, new RegistryDataGenerator(output, provider));
        generator.addProvider(isServer, new DamageTypeTagGenerator(output, provider.thenApply(r -> append(r, BUILDER)), helper));
    }

    private static HolderLookup.Provider append(HolderLookup.Provider original, RegistrySetBuilder builder) {
        return builder.buildPatch(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY), original);
    }
}