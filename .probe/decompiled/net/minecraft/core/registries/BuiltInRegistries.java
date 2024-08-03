package net.minecraft.core.registries;

import com.google.common.collect.Maps;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.Util;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.core.DefaultedMappedRegistry;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.StatType;
import net.minecraft.stats.Stats;
import net.minecraft.util.valueproviders.FloatProviderType;
import net.minecraft.util.valueproviders.IntProviderType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.animal.CatVariant;
import net.minecraft.world.entity.animal.FrogVariant;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.decoration.PaintingVariants;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.schedule.Schedule;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.Instruments;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.BiomeSources;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BannerPatterns;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.DecoratedPotPatterns;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkGenerators;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.PositionSourceType;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicateType;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.featuresize.FeatureSizeType;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacerType;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraft.world.level.levelgen.heightproviders.HeightProviderType;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import net.minecraft.world.level.levelgen.structure.templatesystem.PosRuleTestType;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.RuleBlockEntityModifierType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntries;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
import net.minecraft.world.level.storage.loot.providers.nbt.LootNbtProviderType;
import net.minecraft.world.level.storage.loot.providers.nbt.NbtProviders;
import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
import net.minecraft.world.level.storage.loot.providers.score.LootScoreProviderType;
import net.minecraft.world.level.storage.loot.providers.score.ScoreboardNameProviders;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;

public class BuiltInRegistries {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final Map<ResourceLocation, Supplier<?>> LOADERS = Maps.newLinkedHashMap();

    public static final ResourceLocation ROOT_REGISTRY_NAME = new ResourceLocation("root");

    private static final WritableRegistry<WritableRegistry<?>> WRITABLE_REGISTRY = new MappedRegistry<>(ResourceKey.createRegistryKey(ROOT_REGISTRY_NAME), Lifecycle.stable());

    public static final DefaultedRegistry<GameEvent> GAME_EVENT = registerDefaultedWithIntrusiveHolders(Registries.GAME_EVENT, "step", p_260052_ -> GameEvent.STEP);

    public static final Registry<SoundEvent> SOUND_EVENT = registerSimple(Registries.SOUND_EVENT, p_260167_ -> SoundEvents.ITEM_PICKUP);

    public static final DefaultedRegistry<Fluid> FLUID = registerDefaultedWithIntrusiveHolders(Registries.FLUID, "empty", p_259453_ -> Fluids.EMPTY);

    public static final Registry<MobEffect> MOB_EFFECT = registerSimple(Registries.MOB_EFFECT, p_259689_ -> MobEffects.LUCK);

    public static final DefaultedRegistry<Block> BLOCK = registerDefaultedWithIntrusiveHolders(Registries.BLOCK, "air", p_259909_ -> Blocks.AIR);

    public static final Registry<Enchantment> ENCHANTMENT = registerSimple(Registries.ENCHANTMENT, p_259104_ -> Enchantments.BLOCK_FORTUNE);

    public static final DefaultedRegistry<EntityType<?>> ENTITY_TYPE = registerDefaultedWithIntrusiveHolders(Registries.ENTITY_TYPE, "pig", p_259175_ -> EntityType.PIG);

    public static final DefaultedRegistry<Item> ITEM = registerDefaultedWithIntrusiveHolders(Registries.ITEM, "air", p_260227_ -> Items.AIR);

    public static final DefaultedRegistry<Potion> POTION = registerDefaulted(Registries.POTION, "empty", p_259869_ -> Potions.EMPTY);

    public static final Registry<ParticleType<?>> PARTICLE_TYPE = registerSimple(Registries.PARTICLE_TYPE, p_260266_ -> ParticleTypes.BLOCK);

    public static final Registry<BlockEntityType<?>> BLOCK_ENTITY_TYPE = registerSimple(Registries.BLOCK_ENTITY_TYPE, p_259434_ -> BlockEntityType.FURNACE);

    public static final DefaultedRegistry<PaintingVariant> PAINTING_VARIANT = registerDefaulted(Registries.PAINTING_VARIANT, "kebab", PaintingVariants::m_218942_);

    public static final Registry<ResourceLocation> CUSTOM_STAT = registerSimple(Registries.CUSTOM_STAT, p_259833_ -> Stats.JUMP);

    public static final DefaultedRegistry<ChunkStatus> CHUNK_STATUS = registerDefaulted(Registries.CHUNK_STATUS, "empty", p_259971_ -> ChunkStatus.EMPTY);

    public static final Registry<RuleTestType<?>> RULE_TEST = registerSimple(Registries.RULE_TEST, p_259641_ -> RuleTestType.ALWAYS_TRUE_TEST);

    public static final Registry<RuleBlockEntityModifierType<?>> RULE_BLOCK_ENTITY_MODIFIER = registerSimple(Registries.RULE_BLOCK_ENTITY_MODIFIER, p_277237_ -> RuleBlockEntityModifierType.PASSTHROUGH);

    public static final Registry<PosRuleTestType<?>> POS_RULE_TEST = registerSimple(Registries.POS_RULE_TEST, p_259262_ -> PosRuleTestType.ALWAYS_TRUE_TEST);

    public static final Registry<MenuType<?>> MENU = registerSimple(Registries.MENU, p_259341_ -> MenuType.ANVIL);

    public static final Registry<RecipeType<?>> RECIPE_TYPE = registerSimple(Registries.RECIPE_TYPE, p_259086_ -> RecipeType.CRAFTING);

    public static final Registry<RecipeSerializer<?>> RECIPE_SERIALIZER = registerSimple(Registries.RECIPE_SERIALIZER, p_260230_ -> RecipeSerializer.SHAPELESS_RECIPE);

    public static final Registry<Attribute> ATTRIBUTE = registerSimple(Registries.ATTRIBUTE, p_260300_ -> Attributes.LUCK);

    public static final Registry<PositionSourceType<?>> POSITION_SOURCE_TYPE = registerSimple(Registries.POSITION_SOURCE_TYPE, p_259113_ -> PositionSourceType.BLOCK);

    public static final Registry<ArgumentTypeInfo<?, ?>> COMMAND_ARGUMENT_TYPE = registerSimple(Registries.COMMAND_ARGUMENT_TYPE, ArgumentTypeInfos::m_235384_);

    public static final Registry<StatType<?>> STAT_TYPE = registerSimple(Registries.STAT_TYPE, p_259967_ -> Stats.ITEM_USED);

    public static final DefaultedRegistry<VillagerType> VILLAGER_TYPE = registerDefaulted(Registries.VILLAGER_TYPE, "plains", p_259473_ -> VillagerType.PLAINS);

    public static final DefaultedRegistry<VillagerProfession> VILLAGER_PROFESSION = registerDefaulted(Registries.VILLAGER_PROFESSION, "none", p_259037_ -> VillagerProfession.NONE);

    public static final Registry<PoiType> POINT_OF_INTEREST_TYPE = registerSimple(Registries.POINT_OF_INTEREST_TYPE, PoiTypes::m_218082_);

    public static final DefaultedRegistry<MemoryModuleType<?>> MEMORY_MODULE_TYPE = registerDefaulted(Registries.MEMORY_MODULE_TYPE, "dummy", p_259248_ -> MemoryModuleType.DUMMY);

    public static final DefaultedRegistry<SensorType<?>> SENSOR_TYPE = registerDefaulted(Registries.SENSOR_TYPE, "dummy", p_259757_ -> SensorType.DUMMY);

    public static final Registry<Schedule> SCHEDULE = registerSimple(Registries.SCHEDULE, p_259540_ -> Schedule.EMPTY);

    public static final Registry<Activity> ACTIVITY = registerSimple(Registries.ACTIVITY, p_260197_ -> Activity.IDLE);

    public static final Registry<LootPoolEntryType> LOOT_POOL_ENTRY_TYPE = registerSimple(Registries.LOOT_POOL_ENTRY_TYPE, p_260042_ -> LootPoolEntries.EMPTY);

    public static final Registry<LootItemFunctionType> LOOT_FUNCTION_TYPE = registerSimple(Registries.LOOT_FUNCTION_TYPE, p_259836_ -> LootItemFunctions.SET_COUNT);

    public static final Registry<LootItemConditionType> LOOT_CONDITION_TYPE = registerSimple(Registries.LOOT_CONDITION_TYPE, p_259742_ -> LootItemConditions.INVERTED);

    public static final Registry<LootNumberProviderType> LOOT_NUMBER_PROVIDER_TYPE = registerSimple(Registries.LOOT_NUMBER_PROVIDER_TYPE, p_259329_ -> NumberProviders.CONSTANT);

    public static final Registry<LootNbtProviderType> LOOT_NBT_PROVIDER_TYPE = registerSimple(Registries.LOOT_NBT_PROVIDER_TYPE, p_259862_ -> NbtProviders.CONTEXT);

    public static final Registry<LootScoreProviderType> LOOT_SCORE_PROVIDER_TYPE = registerSimple(Registries.LOOT_SCORE_PROVIDER_TYPE, p_259313_ -> ScoreboardNameProviders.CONTEXT);

    public static final Registry<FloatProviderType<?>> FLOAT_PROVIDER_TYPE = registerSimple(Registries.FLOAT_PROVIDER_TYPE, p_260093_ -> FloatProviderType.CONSTANT);

    public static final Registry<IntProviderType<?>> INT_PROVIDER_TYPE = registerSimple(Registries.INT_PROVIDER_TYPE, p_259607_ -> IntProviderType.CONSTANT);

    public static final Registry<HeightProviderType<?>> HEIGHT_PROVIDER_TYPE = registerSimple(Registries.HEIGHT_PROVIDER_TYPE, p_259663_ -> HeightProviderType.CONSTANT);

    public static final Registry<BlockPredicateType<?>> BLOCK_PREDICATE_TYPE = registerSimple(Registries.BLOCK_PREDICATE_TYPE, p_260006_ -> BlockPredicateType.NOT);

    public static final Registry<WorldCarver<?>> CARVER = registerSimple(Registries.CARVER, p_260200_ -> WorldCarver.CAVE);

    public static final Registry<Feature<?>> FEATURE = registerSimple(Registries.FEATURE, p_259143_ -> Feature.ORE);

    public static final Registry<StructurePlacementType<?>> STRUCTURE_PLACEMENT = registerSimple(Registries.STRUCTURE_PLACEMENT, p_259179_ -> StructurePlacementType.RANDOM_SPREAD);

    public static final Registry<StructurePieceType> STRUCTURE_PIECE = registerSimple(Registries.STRUCTURE_PIECE, p_259722_ -> StructurePieceType.MINE_SHAFT_ROOM);

    public static final Registry<StructureType<?>> STRUCTURE_TYPE = registerSimple(Registries.STRUCTURE_TYPE, p_259466_ -> StructureType.JIGSAW);

    public static final Registry<PlacementModifierType<?>> PLACEMENT_MODIFIER_TYPE = registerSimple(Registries.PLACEMENT_MODIFIER_TYPE, p_260335_ -> PlacementModifierType.COUNT);

    public static final Registry<BlockStateProviderType<?>> BLOCKSTATE_PROVIDER_TYPE = registerSimple(Registries.BLOCK_STATE_PROVIDER_TYPE, p_259345_ -> BlockStateProviderType.SIMPLE_STATE_PROVIDER);

    public static final Registry<FoliagePlacerType<?>> FOLIAGE_PLACER_TYPE = registerSimple(Registries.FOLIAGE_PLACER_TYPE, p_260329_ -> FoliagePlacerType.BLOB_FOLIAGE_PLACER);

    public static final Registry<TrunkPlacerType<?>> TRUNK_PLACER_TYPE = registerSimple(Registries.TRUNK_PLACER_TYPE, p_259690_ -> TrunkPlacerType.STRAIGHT_TRUNK_PLACER);

    public static final Registry<RootPlacerType<?>> ROOT_PLACER_TYPE = registerSimple(Registries.ROOT_PLACER_TYPE, p_259493_ -> RootPlacerType.MANGROVE_ROOT_PLACER);

    public static final Registry<TreeDecoratorType<?>> TREE_DECORATOR_TYPE = registerSimple(Registries.TREE_DECORATOR_TYPE, p_259122_ -> TreeDecoratorType.LEAVE_VINE);

    public static final Registry<FeatureSizeType<?>> FEATURE_SIZE_TYPE = registerSimple(Registries.FEATURE_SIZE_TYPE, p_259370_ -> FeatureSizeType.TWO_LAYERS_FEATURE_SIZE);

    public static final Registry<Codec<? extends BiomeSource>> BIOME_SOURCE = registerSimple(Registries.BIOME_SOURCE, Lifecycle.stable(), BiomeSources::m_220586_);

    public static final Registry<Codec<? extends ChunkGenerator>> CHUNK_GENERATOR = registerSimple(Registries.CHUNK_GENERATOR, Lifecycle.stable(), ChunkGenerators::m_223242_);

    public static final Registry<Codec<? extends SurfaceRules.ConditionSource>> MATERIAL_CONDITION = registerSimple(Registries.MATERIAL_CONDITION, SurfaceRules.ConditionSource::m_204624_);

    public static final Registry<Codec<? extends SurfaceRules.RuleSource>> MATERIAL_RULE = registerSimple(Registries.MATERIAL_RULE, SurfaceRules.RuleSource::m_204630_);

    public static final Registry<Codec<? extends DensityFunction>> DENSITY_FUNCTION_TYPE = registerSimple(Registries.DENSITY_FUNCTION_TYPE, DensityFunctions::m_208342_);

    public static final Registry<StructureProcessorType<?>> STRUCTURE_PROCESSOR = registerSimple(Registries.STRUCTURE_PROCESSOR, p_259305_ -> StructureProcessorType.BLOCK_IGNORE);

    public static final Registry<StructurePoolElementType<?>> STRUCTURE_POOL_ELEMENT = registerSimple(Registries.STRUCTURE_POOL_ELEMENT, p_259361_ -> StructurePoolElementType.EMPTY);

    public static final Registry<CatVariant> CAT_VARIANT = registerSimple(Registries.CAT_VARIANT, CatVariant::m_255364_);

    public static final Registry<FrogVariant> FROG_VARIANT = registerSimple(Registries.FROG_VARIANT, p_259261_ -> FrogVariant.TEMPERATE);

    public static final Registry<BannerPattern> BANNER_PATTERN = registerSimple(Registries.BANNER_PATTERN, BannerPatterns::m_222754_);

    public static final Registry<Instrument> INSTRUMENT = registerSimple(Registries.INSTRUMENT, Instruments::m_220148_);

    public static final Registry<String> DECORATED_POT_PATTERNS = registerSimple(Registries.DECORATED_POT_PATTERNS, DecoratedPotPatterns::m_271825_);

    public static final Registry<CreativeModeTab> CREATIVE_MODE_TAB = registerSimple(Registries.CREATIVE_MODE_TAB, CreativeModeTabs::m_280294_);

    public static final Registry<? extends Registry<?>> REGISTRY = WRITABLE_REGISTRY;

    private static <T> Registry<T> registerSimple(ResourceKey<? extends Registry<T>> resourceKeyExtendsRegistryT0, BuiltInRegistries.RegistryBootstrap<T> builtInRegistriesRegistryBootstrapT1) {
        return registerSimple(resourceKeyExtendsRegistryT0, Lifecycle.stable(), builtInRegistriesRegistryBootstrapT1);
    }

    private static <T> DefaultedRegistry<T> registerDefaulted(ResourceKey<? extends Registry<T>> resourceKeyExtendsRegistryT0, String string1, BuiltInRegistries.RegistryBootstrap<T> builtInRegistriesRegistryBootstrapT2) {
        return registerDefaulted(resourceKeyExtendsRegistryT0, string1, Lifecycle.stable(), builtInRegistriesRegistryBootstrapT2);
    }

    private static <T> DefaultedRegistry<T> registerDefaultedWithIntrusiveHolders(ResourceKey<? extends Registry<T>> resourceKeyExtendsRegistryT0, String string1, BuiltInRegistries.RegistryBootstrap<T> builtInRegistriesRegistryBootstrapT2) {
        return registerDefaultedWithIntrusiveHolders(resourceKeyExtendsRegistryT0, string1, Lifecycle.stable(), builtInRegistriesRegistryBootstrapT2);
    }

    private static <T> Registry<T> registerSimple(ResourceKey<? extends Registry<T>> resourceKeyExtendsRegistryT0, Lifecycle lifecycle1, BuiltInRegistries.RegistryBootstrap<T> builtInRegistriesRegistryBootstrapT2) {
        return internalRegister(resourceKeyExtendsRegistryT0, new MappedRegistry<>(resourceKeyExtendsRegistryT0, lifecycle1, false), builtInRegistriesRegistryBootstrapT2, lifecycle1);
    }

    private static <T> DefaultedRegistry<T> registerDefaulted(ResourceKey<? extends Registry<T>> resourceKeyExtendsRegistryT0, String string1, Lifecycle lifecycle2, BuiltInRegistries.RegistryBootstrap<T> builtInRegistriesRegistryBootstrapT3) {
        return internalRegister(resourceKeyExtendsRegistryT0, new DefaultedMappedRegistry<>(string1, resourceKeyExtendsRegistryT0, lifecycle2, false), builtInRegistriesRegistryBootstrapT3, lifecycle2);
    }

    private static <T> DefaultedRegistry<T> registerDefaultedWithIntrusiveHolders(ResourceKey<? extends Registry<T>> resourceKeyExtendsRegistryT0, String string1, Lifecycle lifecycle2, BuiltInRegistries.RegistryBootstrap<T> builtInRegistriesRegistryBootstrapT3) {
        return internalRegister(resourceKeyExtendsRegistryT0, new DefaultedMappedRegistry<>(string1, resourceKeyExtendsRegistryT0, lifecycle2, true), builtInRegistriesRegistryBootstrapT3, lifecycle2);
    }

    private static <T, R extends WritableRegistry<T>> R internalRegister(ResourceKey<? extends Registry<T>> resourceKeyExtendsRegistryT0, R r1, BuiltInRegistries.RegistryBootstrap<T> builtInRegistriesRegistryBootstrapT2, Lifecycle lifecycle3) {
        ResourceLocation $$4 = resourceKeyExtendsRegistryT0.location();
        LOADERS.put($$4, (Supplier) () -> builtInRegistriesRegistryBootstrapT2.run(r1));
        WRITABLE_REGISTRY.register((ResourceKey<WritableRegistry<?>>) resourceKeyExtendsRegistryT0, r1, lifecycle3);
        return r1;
    }

    public static void bootStrap() {
        createContents();
        freeze();
        validate(REGISTRY);
    }

    private static void createContents() {
        LOADERS.forEach((p_259863_, p_259387_) -> {
            if (p_259387_.get() == null) {
                LOGGER.error("Unable to bootstrap registry '{}'", p_259863_);
            }
        });
    }

    private static void freeze() {
        REGISTRY.freeze();
        for (Registry<?> $$0 : REGISTRY) {
            $$0.freeze();
        }
    }

    private static <T extends Registry<?>> void validate(Registry<T> registryT0) {
        registryT0.forEach(p_259410_ -> {
            if (p_259410_.keySet().isEmpty()) {
                Util.logAndPauseIfInIde("Registry '" + registryT0.getKey((T) p_259410_) + "' was empty after loading");
            }
            if (p_259410_ instanceof DefaultedRegistry) {
                ResourceLocation $$2 = ((DefaultedRegistry) p_259410_).getDefaultKey();
                Validate.notNull(p_259410_.get($$2), "Missing default of DefaultedMappedRegistry: " + $$2, new Object[0]);
            }
        });
    }

    @FunctionalInterface
    interface RegistryBootstrap<T> {

        T run(Registry<T> var1);
    }
}