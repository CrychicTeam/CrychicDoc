package net.blay09.mods.balm.forge.world;

import com.mojang.serialization.Codec;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.world.BalmWorldGen;
import net.blay09.mods.balm.api.world.BiomePredicate;
import net.blay09.mods.balm.forge.DeferredRegisters;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.server.ServerLifecycleHooks;

public class ForgeBalmWorldGen implements BalmWorldGen {

    public static final Codec<BalmBiomeModifier> BALM_BIOME_MODIFIER_CODEC = Codec.unit(BalmBiomeModifier.INSTANCE);

    private final Map<String, ForgeBalmWorldGen.Registrations> registrations = new ConcurrentHashMap();

    private static final List<BiomeModification> biomeModifications = new ArrayList();

    public ForgeBalmWorldGen() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public <T extends Feature<?>> DeferredObject<T> registerFeature(ResourceLocation identifier, Supplier<T> supplier) {
        DeferredRegister<Feature<?>> register = DeferredRegisters.get(ForgeRegistries.FEATURES, identifier.getNamespace());
        RegistryObject<T> registryObject = register.register(identifier.getPath(), supplier);
        return new DeferredObject<>(identifier, registryObject, registryObject::isPresent);
    }

    @Override
    public <T extends PlacementModifierType<?>> DeferredObject<T> registerPlacementModifier(ResourceLocation identifier, Supplier<T> supplier) {
        DeferredObject<T> deferredObject = new DeferredObject<>(identifier, () -> {
            T placementModifierType = (T) supplier.get();
            Registry.register(BuiltInRegistries.PLACEMENT_MODIFIER_TYPE, identifier, placementModifierType);
            return placementModifierType;
        });
        this.getActiveRegistrations().placementModifiers.add(deferredObject);
        return deferredObject;
    }

    @Override
    public void addFeatureToBiomes(BiomePredicate biomePredicate, GenerationStep.Decoration step, ResourceLocation placedFeatureIdentifier) {
        ResourceKey<PlacedFeature> resourceKey = ResourceKey.create(Registries.PLACED_FEATURE, placedFeatureIdentifier);
        biomeModifications.add(new BiomeModification(biomePredicate, step, resourceKey));
    }

    public void modifyBiome(Holder<Biome> biome, BiomeModifier.Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == BiomeModifier.Phase.ADD) {
            for (BiomeModification biomeModification : biomeModifications) {
                ResourceLocation location = (ResourceLocation) biome.unwrapKey().map(ResourceKey::m_135782_).orElse(null);
                if (location != null && biomeModification.getBiomePredicate().test(location, biome)) {
                    Registry<PlacedFeature> placedFeatures = ServerLifecycleHooks.getCurrentServer().registryAccess().m_175515_(Registries.PLACED_FEATURE);
                    placedFeatures.getHolder(biomeModification.getConfiguredFeatureKey()).ifPresent(placedFeature -> builder.getGenerationSettings().m_255419_(biomeModification.getStep(), placedFeature));
                }
            }
        }
    }

    public void register() {
        FMLJavaModLoadingContext.get().getModEventBus().register(this.getActiveRegistrations());
    }

    private ForgeBalmWorldGen.Registrations getActiveRegistrations() {
        return (ForgeBalmWorldGen.Registrations) this.registrations.computeIfAbsent(ModLoadingContext.get().getActiveNamespace(), it -> new ForgeBalmWorldGen.Registrations());
    }

    public static void initializeBalmBiomeModifiers() {
        DeferredRegister<Codec<? extends BiomeModifier>> registry = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, "balm");
        registry.register("balm", () -> BALM_BIOME_MODIFIER_CODEC);
        registry.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    private static class Registrations {

        public final List<DeferredObject<?>> configuredFeatures = new ArrayList();

        public final List<DeferredObject<?>> placedFeatures = new ArrayList();

        public final List<DeferredObject<?>> placementModifiers = new ArrayList();

        @SubscribeEvent
        public void commonSetup(FMLCommonSetupEvent event) {
            event.enqueueWork(() -> {
                this.placementModifiers.forEach(DeferredObject::resolve);
                this.configuredFeatures.forEach(DeferredObject::resolve);
                this.placedFeatures.forEach(DeferredObject::resolve);
            });
        }
    }
}