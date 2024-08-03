package net.minecraftforge.registries;

import com.mojang.serialization.Codec;
import java.util.function.Supplier;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.Bootstrap;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.StatType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.schedule.Schedule;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.StructureModifier;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.holdersets.HolderSetType;

public class ForgeRegistries {

    public static final IForgeRegistry<Block> BLOCKS = RegistryManager.ACTIVE.getRegistry(ForgeRegistries.Keys.BLOCKS);

    public static final IForgeRegistry<Fluid> FLUIDS = RegistryManager.ACTIVE.getRegistry(ForgeRegistries.Keys.FLUIDS);

    public static final IForgeRegistry<Item> ITEMS = RegistryManager.ACTIVE.getRegistry(ForgeRegistries.Keys.ITEMS);

    public static final IForgeRegistry<MobEffect> MOB_EFFECTS = RegistryManager.ACTIVE.getRegistry(ForgeRegistries.Keys.MOB_EFFECTS);

    public static final IForgeRegistry<SoundEvent> SOUND_EVENTS = RegistryManager.ACTIVE.getRegistry(ForgeRegistries.Keys.SOUND_EVENTS);

    public static final IForgeRegistry<Potion> POTIONS = RegistryManager.ACTIVE.getRegistry(ForgeRegistries.Keys.POTIONS);

    public static final IForgeRegistry<Enchantment> ENCHANTMENTS = RegistryManager.ACTIVE.getRegistry(ForgeRegistries.Keys.ENCHANTMENTS);

    public static final IForgeRegistry<EntityType<?>> ENTITY_TYPES = RegistryManager.ACTIVE.getRegistry(ForgeRegistries.Keys.ENTITY_TYPES);

    public static final IForgeRegistry<BlockEntityType<?>> BLOCK_ENTITY_TYPES = RegistryManager.ACTIVE.getRegistry(ForgeRegistries.Keys.BLOCK_ENTITY_TYPES);

    public static final IForgeRegistry<ParticleType<?>> PARTICLE_TYPES = RegistryManager.ACTIVE.getRegistry(ForgeRegistries.Keys.PARTICLE_TYPES);

    public static final IForgeRegistry<MenuType<?>> MENU_TYPES = RegistryManager.ACTIVE.getRegistry(ForgeRegistries.Keys.MENU_TYPES);

    public static final IForgeRegistry<PaintingVariant> PAINTING_VARIANTS = RegistryManager.ACTIVE.getRegistry(ForgeRegistries.Keys.PAINTING_VARIANTS);

    public static final IForgeRegistry<RecipeType<?>> RECIPE_TYPES = RegistryManager.ACTIVE.getRegistry(ForgeRegistries.Keys.RECIPE_TYPES);

    public static final IForgeRegistry<RecipeSerializer<?>> RECIPE_SERIALIZERS = RegistryManager.ACTIVE.getRegistry(ForgeRegistries.Keys.RECIPE_SERIALIZERS);

    public static final IForgeRegistry<Attribute> ATTRIBUTES = RegistryManager.ACTIVE.getRegistry(ForgeRegistries.Keys.ATTRIBUTES);

    public static final IForgeRegistry<StatType<?>> STAT_TYPES = RegistryManager.ACTIVE.getRegistry(ForgeRegistries.Keys.STAT_TYPES);

    public static final IForgeRegistry<ArgumentTypeInfo<?, ?>> COMMAND_ARGUMENT_TYPES = RegistryManager.ACTIVE.getRegistry(ForgeRegistries.Keys.COMMAND_ARGUMENT_TYPES);

    public static final IForgeRegistry<VillagerProfession> VILLAGER_PROFESSIONS = RegistryManager.ACTIVE.getRegistry(ForgeRegistries.Keys.VILLAGER_PROFESSIONS);

    public static final IForgeRegistry<PoiType> POI_TYPES = RegistryManager.ACTIVE.getRegistry(ForgeRegistries.Keys.POI_TYPES);

    public static final IForgeRegistry<MemoryModuleType<?>> MEMORY_MODULE_TYPES = RegistryManager.ACTIVE.getRegistry(ForgeRegistries.Keys.MEMORY_MODULE_TYPES);

    public static final IForgeRegistry<SensorType<?>> SENSOR_TYPES = RegistryManager.ACTIVE.getRegistry(ForgeRegistries.Keys.SENSOR_TYPES);

    public static final IForgeRegistry<Schedule> SCHEDULES = RegistryManager.ACTIVE.getRegistry(ForgeRegistries.Keys.SCHEDULES);

    public static final IForgeRegistry<Activity> ACTIVITIES = RegistryManager.ACTIVE.getRegistry(ForgeRegistries.Keys.ACTIVITIES);

    public static final IForgeRegistry<WorldCarver<?>> WORLD_CARVERS = RegistryManager.ACTIVE.getRegistry(ForgeRegistries.Keys.WORLD_CARVERS);

    public static final IForgeRegistry<Feature<?>> FEATURES = RegistryManager.ACTIVE.getRegistry(ForgeRegistries.Keys.FEATURES);

    public static final IForgeRegistry<ChunkStatus> CHUNK_STATUS = RegistryManager.ACTIVE.getRegistry(ForgeRegistries.Keys.CHUNK_STATUS);

    public static final IForgeRegistry<BlockStateProviderType<?>> BLOCK_STATE_PROVIDER_TYPES = RegistryManager.ACTIVE.getRegistry(ForgeRegistries.Keys.BLOCK_STATE_PROVIDER_TYPES);

    public static final IForgeRegistry<FoliagePlacerType<?>> FOLIAGE_PLACER_TYPES = RegistryManager.ACTIVE.getRegistry(ForgeRegistries.Keys.FOLIAGE_PLACER_TYPES);

    public static final IForgeRegistry<TreeDecoratorType<?>> TREE_DECORATOR_TYPES = RegistryManager.ACTIVE.getRegistry(ForgeRegistries.Keys.TREE_DECORATOR_TYPES);

    public static final IForgeRegistry<Biome> BIOMES = RegistryManager.ACTIVE.getRegistry(ForgeRegistries.Keys.BIOMES);

    static final DeferredRegister<EntityDataSerializer<?>> DEFERRED_ENTITY_DATA_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS.location().getNamespace());

    public static final Supplier<IForgeRegistry<EntityDataSerializer<?>>> ENTITY_DATA_SERIALIZERS = DEFERRED_ENTITY_DATA_SERIALIZERS.makeRegistry(GameData::getDataSerializersRegistryBuilder);

    static final DeferredRegister<Codec<? extends IGlobalLootModifier>> DEFERRED_GLOBAL_LOOT_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS.location().getNamespace());

    public static final Supplier<IForgeRegistry<Codec<? extends IGlobalLootModifier>>> GLOBAL_LOOT_MODIFIER_SERIALIZERS = DEFERRED_GLOBAL_LOOT_MODIFIER_SERIALIZERS.makeRegistry(GameData::getGLMSerializersRegistryBuilder);

    static final DeferredRegister<Codec<? extends BiomeModifier>> DEFERRED_BIOME_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS.location().getNamespace());

    public static final Supplier<IForgeRegistry<Codec<? extends BiomeModifier>>> BIOME_MODIFIER_SERIALIZERS = DEFERRED_BIOME_MODIFIER_SERIALIZERS.makeRegistry(GameData::getBiomeModifierSerializersRegistryBuilder);

    static final DeferredRegister<Codec<? extends StructureModifier>> DEFERRED_STRUCTURE_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.STRUCTURE_MODIFIER_SERIALIZERS, ForgeRegistries.Keys.STRUCTURE_MODIFIER_SERIALIZERS.location().getNamespace());

    public static final Supplier<IForgeRegistry<Codec<? extends StructureModifier>>> STRUCTURE_MODIFIER_SERIALIZERS = DEFERRED_STRUCTURE_MODIFIER_SERIALIZERS.makeRegistry(GameData::getStructureModifierSerializersRegistryBuilder);

    static final DeferredRegister<FluidType> DEFERRED_FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, ForgeRegistries.Keys.FLUID_TYPES.location().getNamespace());

    public static final Supplier<IForgeRegistry<FluidType>> FLUID_TYPES = DEFERRED_FLUID_TYPES.makeRegistry(GameData::getFluidTypeRegistryBuilder);

    static final DeferredRegister<HolderSetType> DEFERRED_HOLDER_SET_TYPES = DeferredRegister.create(ForgeRegistries.Keys.HOLDER_SET_TYPES, "forge");

    public static final Supplier<IForgeRegistry<HolderSetType>> HOLDER_SET_TYPES = DEFERRED_HOLDER_SET_TYPES.makeRegistry(GameData::makeUnsavedAndUnsynced);

    static final DeferredRegister<ItemDisplayContext> DEFERRED_DISPLAY_CONTEXTS = DeferredRegister.create(ForgeRegistries.Keys.DISPLAY_CONTEXTS, "forge");

    public static final Supplier<IForgeRegistry<ItemDisplayContext>> DISPLAY_CONTEXTS = DEFERRED_DISPLAY_CONTEXTS.makeRegistry(GameData::getItemDisplayContextRegistryBuilder);

    private static void init() {
        ForgeRegistries.Keys.init();
        GameData.init();
        Bootstrap.bootStrap();
        Tags.init();
    }

    static {
        init();
    }

    public static final class Keys {

        public static final ResourceKey<Registry<Block>> BLOCKS = key("block");

        public static final ResourceKey<Registry<Fluid>> FLUIDS = key("fluid");

        public static final ResourceKey<Registry<Item>> ITEMS = key("item");

        public static final ResourceKey<Registry<MobEffect>> MOB_EFFECTS = key("mob_effect");

        public static final ResourceKey<Registry<Potion>> POTIONS = key("potion");

        public static final ResourceKey<Registry<Attribute>> ATTRIBUTES = key("attribute");

        public static final ResourceKey<Registry<StatType<?>>> STAT_TYPES = key("stat_type");

        public static final ResourceKey<Registry<ArgumentTypeInfo<?, ?>>> COMMAND_ARGUMENT_TYPES = key("command_argument_type");

        public static final ResourceKey<Registry<SoundEvent>> SOUND_EVENTS = key("sound_event");

        public static final ResourceKey<Registry<Enchantment>> ENCHANTMENTS = key("enchantment");

        public static final ResourceKey<Registry<EntityType<?>>> ENTITY_TYPES = key("entity_type");

        public static final ResourceKey<Registry<PaintingVariant>> PAINTING_VARIANTS = key("painting_variant");

        public static final ResourceKey<Registry<ParticleType<?>>> PARTICLE_TYPES = key("particle_type");

        public static final ResourceKey<Registry<MenuType<?>>> MENU_TYPES = key("menu");

        public static final ResourceKey<Registry<BlockEntityType<?>>> BLOCK_ENTITY_TYPES = key("block_entity_type");

        public static final ResourceKey<Registry<RecipeType<?>>> RECIPE_TYPES = key("recipe_type");

        public static final ResourceKey<Registry<RecipeSerializer<?>>> RECIPE_SERIALIZERS = key("recipe_serializer");

        public static final ResourceKey<Registry<VillagerProfession>> VILLAGER_PROFESSIONS = key("villager_profession");

        public static final ResourceKey<Registry<PoiType>> POI_TYPES = key("point_of_interest_type");

        public static final ResourceKey<Registry<MemoryModuleType<?>>> MEMORY_MODULE_TYPES = key("memory_module_type");

        public static final ResourceKey<Registry<SensorType<?>>> SENSOR_TYPES = key("sensor_type");

        public static final ResourceKey<Registry<Schedule>> SCHEDULES = key("schedule");

        public static final ResourceKey<Registry<Activity>> ACTIVITIES = key("activity");

        public static final ResourceKey<Registry<WorldCarver<?>>> WORLD_CARVERS = key("worldgen/carver");

        public static final ResourceKey<Registry<Feature<?>>> FEATURES = key("worldgen/feature");

        public static final ResourceKey<Registry<ChunkStatus>> CHUNK_STATUS = key("chunk_status");

        public static final ResourceKey<Registry<BlockStateProviderType<?>>> BLOCK_STATE_PROVIDER_TYPES = key("worldgen/block_state_provider_type");

        public static final ResourceKey<Registry<FoliagePlacerType<?>>> FOLIAGE_PLACER_TYPES = key("worldgen/foliage_placer_type");

        public static final ResourceKey<Registry<TreeDecoratorType<?>>> TREE_DECORATOR_TYPES = key("worldgen/tree_decorator_type");

        public static final ResourceKey<Registry<Biome>> BIOMES = key("worldgen/biome");

        public static final ResourceKey<Registry<EntityDataSerializer<?>>> ENTITY_DATA_SERIALIZERS = key("forge:entity_data_serializers");

        public static final ResourceKey<Registry<Codec<? extends IGlobalLootModifier>>> GLOBAL_LOOT_MODIFIER_SERIALIZERS = key("forge:global_loot_modifier_serializers");

        public static final ResourceKey<Registry<Codec<? extends BiomeModifier>>> BIOME_MODIFIER_SERIALIZERS = key("forge:biome_modifier_serializers");

        public static final ResourceKey<Registry<Codec<? extends StructureModifier>>> STRUCTURE_MODIFIER_SERIALIZERS = key("forge:structure_modifier_serializers");

        public static final ResourceKey<Registry<FluidType>> FLUID_TYPES = key("forge:fluid_type");

        public static final ResourceKey<Registry<HolderSetType>> HOLDER_SET_TYPES = key("forge:holder_set_type");

        public static final ResourceKey<Registry<ItemDisplayContext>> DISPLAY_CONTEXTS = key("forge:display_contexts");

        public static final ResourceKey<Registry<BiomeModifier>> BIOME_MODIFIERS = key("forge:biome_modifier");

        public static final ResourceKey<Registry<StructureModifier>> STRUCTURE_MODIFIERS = key("forge:structure_modifier");

        private static <T> ResourceKey<Registry<T>> key(String name) {
            return ResourceKey.createRegistryKey(new ResourceLocation(name));
        }

        private static void init() {
        }
    }
}