package fuzs.puzzleslib.impl.core;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.mojang.serialization.Codec;
import fuzs.puzzleslib.api.biome.v1.BiomeLoadingContext;
import fuzs.puzzleslib.api.biome.v1.BiomeLoadingPhase;
import fuzs.puzzleslib.api.biome.v1.BiomeModificationContext;
import fuzs.puzzleslib.api.resources.v1.AbstractModPackResources;
import fuzs.puzzleslib.api.resources.v1.PackResourcesHelper;
import fuzs.puzzleslib.impl.biome.BiomeLoadingContextForge;
import fuzs.puzzleslib.impl.biome.ClimateSettingsContextForge;
import fuzs.puzzleslib.impl.biome.GenerationSettingsContextForge;
import fuzs.puzzleslib.impl.biome.MobSpawnSettingsContextForge;
import fuzs.puzzleslib.impl.biome.SpecialEffectsContextForge;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public class BiomeLoadingHandler {

    private static final Map<BiomeModifier.Phase, BiomeLoadingPhase> BIOME_MODIFIER_PHASE_CONVERSIONS = Maps.immutableEnumMap(new HashMap<BiomeModifier.Phase, BiomeLoadingPhase>() {

        {
            this.put(BiomeModifier.Phase.ADD, BiomeLoadingPhase.ADDITIONS);
            this.put(BiomeModifier.Phase.REMOVE, BiomeLoadingPhase.REMOVALS);
            this.put(BiomeModifier.Phase.MODIFY, BiomeLoadingPhase.MODIFICATIONS);
            this.put(BiomeModifier.Phase.AFTER_EVERYTHING, BiomeLoadingPhase.POST_PROCESSING);
        }
    });

    private static final String BIOME_MODIFICATIONS_NAME_KEY = "biome_modifications";

    private static final String BIOME_MODIFIERS_DATA_KEY = ForgeRegistries.Keys.BIOME_MODIFIERS.location().toString().replace(":", "/");

    private static final Function<String, ResourceLocation> BIOME_MODIFICATIONS_FILE_KEY = id -> new ResourceLocation(id, BIOME_MODIFIERS_DATA_KEY + "/" + id + ".json");

    private static final Function<ResourceLocation, String> BIOME_MODIFICATIONS_FILE_CONTENTS = id -> "{\"type\":\"" + id + "\"}";

    public static void register(String modId, IEventBus modEventBus, Multimap<BiomeLoadingPhase, BiomeLoadingHandler.BiomeModification> biomeModifications) {
        DeferredRegister<Codec<? extends BiomeModifier>> deferredRegister = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, modId);
        deferredRegister.register(modEventBus);
        deferredRegister.register("biome_modifications", new BiomeLoadingHandler.BiomeModifierImpl(biomeModifications)::codec);
    }

    public static RepositorySource buildPack(String modId) {
        ResourceLocation id = new ResourceLocation(modId, "biome_modifications");
        return PackResourcesHelper.buildServerPack(id, () -> new AbstractModPackResources() {

            @Override
            public void listResources(PackType packType, String namespace, String path, PackResources.ResourceOutput resourceOutput) {
                if (path.equals(BiomeLoadingHandler.BIOME_MODIFIERS_DATA_KEY)) {
                    resourceOutput.accept((ResourceLocation) BiomeLoadingHandler.BIOME_MODIFICATIONS_FILE_KEY.apply(modId), (IoSupplier) () -> new ByteArrayInputStream(((String) BiomeLoadingHandler.BIOME_MODIFICATIONS_FILE_CONTENTS.apply(id)).getBytes(StandardCharsets.UTF_8)));
                }
            }
        }, false);
    }

    public static record BiomeModification(Predicate<BiomeLoadingContext> selector, Consumer<BiomeModificationContext> modifier) {

        public void tryApply(BiomeLoadingContext filter, BiomeModificationContext context) {
            if (this.selector().test(filter)) {
                this.modifier().accept(context);
            }
        }
    }

    private static record BiomeModifierImpl(Multimap<BiomeLoadingPhase, BiomeLoadingHandler.BiomeModification> biomeModifications, @Nullable Codec<? extends BiomeModifier> codec) implements BiomeModifier {

        private BiomeModifierImpl(Multimap<BiomeLoadingPhase, BiomeLoadingHandler.BiomeModification> biomeModifications, @Nullable Codec<? extends BiomeModifier> codec) {
            this.biomeModifications = biomeModifications;
            this.codec = Codec.unit(this);
        }

        public BiomeModifierImpl(Multimap<BiomeLoadingPhase, BiomeLoadingHandler.BiomeModification> biomeModifications) {
            this(biomeModifications, null);
        }

        private static BiomeModificationContext createBuilderBackedContext(ModifiableBiomeInfo.BiomeInfo.Builder builder) {
            ClimateSettingsContextForge climateSettings = new ClimateSettingsContextForge(builder.getClimateSettings());
            SpecialEffectsContextForge specialEffects = new SpecialEffectsContextForge(builder.getSpecialEffects());
            GenerationSettingsContextForge generationSettings = new GenerationSettingsContextForge(builder.getGenerationSettings());
            MobSpawnSettingsContextForge mobSpawnSettings = new MobSpawnSettingsContextForge(builder.getMobSpawnSettings());
            return new BiomeModificationContext(climateSettings, specialEffects, generationSettings, mobSpawnSettings);
        }

        @Override
        public void modify(Holder<Biome> biome, BiomeModifier.Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
            BiomeLoadingPhase loadingPhase = (BiomeLoadingPhase) BiomeLoadingHandler.BIOME_MODIFIER_PHASE_CONVERSIONS.get(phase);
            if (loadingPhase != null) {
                Collection<BiomeLoadingHandler.BiomeModification> modifications = this.biomeModifications.get(loadingPhase);
                if (!modifications.isEmpty()) {
                    BiomeLoadingContext filter = new BiomeLoadingContextForge(biome);
                    BiomeModificationContext context = createBuilderBackedContext(builder);
                    for (BiomeLoadingHandler.BiomeModification modification : modifications) {
                        modification.tryApply(filter, context);
                    }
                }
            }
        }
    }
}