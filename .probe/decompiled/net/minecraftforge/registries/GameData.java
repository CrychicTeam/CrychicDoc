package net.minecraftforge.registries;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import com.google.common.collect.UnmodifiableIterator;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import net.minecraft.core.IdMapper;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.levelgen.DebugLevelSource;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.CreativeModeTabRegistry;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.util.LogMessageAdapter;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.StructureModifier;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.StartupMessageManager;
import net.minecraftforge.fml.util.EnhancedRuntimeException;
import net.minecraftforge.fml.util.EnhancedRuntimeException.WrappedPrintStream;
import net.minecraftforge.fml.util.thread.EffectiveSide;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public class GameData {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final Marker REGISTRIES = ForgeRegistry.REGISTRIES;

    private static final int MAX_VARINT = 2147483646;

    private static final ResourceLocation BLOCK_TO_ITEM = new ResourceLocation("minecraft:blocktoitemmap");

    private static final ResourceLocation BLOCKSTATE_TO_ID = new ResourceLocation("minecraft:blockstatetoid");

    private static final ResourceLocation BLOCKSTATE_TO_POINT_OF_INTEREST_TYPE = new ResourceLocation("minecraft:blockstatetopointofinteresttype");

    private static boolean hasInit = false;

    private static final boolean DISABLE_VANILLA_REGISTRIES = Boolean.parseBoolean(System.getProperty("forge.disableVanillaGameData", "false"));

    private static final BiConsumer<ResourceLocation, ForgeRegistry<?>> LOCK_VANILLA = (name, reg) -> reg.slaves.values().stream().filter(o -> o instanceof ILockableRegistry).forEach(o -> ((ILockableRegistry) o).lock());

    public static void init() {
        if (DISABLE_VANILLA_REGISTRIES) {
            LOGGER.warn(REGISTRIES, "DISABLING VANILLA REGISTRY CREATION AS PER SYSTEM VARIABLE SETTING! forge.disableVanillaGameData");
        } else if (!hasInit) {
            hasInit = true;
            makeRegistry(ForgeRegistries.Keys.BLOCKS, "air").addCallback(GameData.BlockCallbacks.INSTANCE).legacyName("blocks").intrusiveHolderCallback(Block::m_204297_).create();
            makeRegistry(ForgeRegistries.Keys.FLUIDS, "empty").intrusiveHolderCallback(Fluid::m_205069_).create();
            makeRegistry(ForgeRegistries.Keys.ITEMS, "air").addCallback(GameData.ItemCallbacks.INSTANCE).legacyName("items").intrusiveHolderCallback(Item::m_204114_).create();
            makeRegistry(ForgeRegistries.Keys.MOB_EFFECTS).legacyName("potions").create();
            makeRegistry(ForgeRegistries.Keys.SOUND_EVENTS).legacyName("soundevents").create();
            makeRegistry(ForgeRegistries.Keys.POTIONS, "empty").legacyName("potiontypes").create();
            makeRegistry(ForgeRegistries.Keys.ENCHANTMENTS).legacyName("enchantments").create();
            makeRegistry(ForgeRegistries.Keys.ENTITY_TYPES, "pig").legacyName("entities").intrusiveHolderCallback(EntityType::m_204041_).create();
            makeRegistry(ForgeRegistries.Keys.BLOCK_ENTITY_TYPES).disableSaving().legacyName("blockentities").create();
            makeRegistry(ForgeRegistries.Keys.PARTICLE_TYPES).disableSaving().create();
            makeRegistry(ForgeRegistries.Keys.MENU_TYPES).disableSaving().create();
            makeRegistry(ForgeRegistries.Keys.PAINTING_VARIANTS, "kebab").create();
            makeRegistry(ForgeRegistries.Keys.RECIPE_TYPES).disableSaving().disableSync().create();
            makeRegistry(ForgeRegistries.Keys.RECIPE_SERIALIZERS).disableSaving().create();
            makeRegistry(ForgeRegistries.Keys.ATTRIBUTES).onValidate(GameData.AttributeCallbacks.INSTANCE).disableSaving().disableSync().create();
            makeRegistry(ForgeRegistries.Keys.STAT_TYPES).create();
            makeRegistry(ForgeRegistries.Keys.COMMAND_ARGUMENT_TYPES).disableSaving().create();
            makeRegistry(ForgeRegistries.Keys.VILLAGER_PROFESSIONS, "none").create();
            makeRegistry(ForgeRegistries.Keys.POI_TYPES).addCallback(GameData.PointOfInterestTypeCallbacks.INSTANCE).disableSync().create();
            makeRegistry(ForgeRegistries.Keys.MEMORY_MODULE_TYPES, "dummy").disableSync().create();
            makeRegistry(ForgeRegistries.Keys.SENSOR_TYPES, "dummy").disableSaving().disableSync().create();
            makeRegistry(ForgeRegistries.Keys.SCHEDULES).disableSaving().disableSync().create();
            makeRegistry(ForgeRegistries.Keys.ACTIVITIES).disableSaving().disableSync().create();
            makeRegistry(ForgeRegistries.Keys.WORLD_CARVERS).disableSaving().disableSync().create();
            makeRegistry(ForgeRegistries.Keys.FEATURES).disableSaving().disableSync().create();
            makeRegistry(ForgeRegistries.Keys.CHUNK_STATUS, "empty").disableSaving().disableSync().create();
            makeRegistry(ForgeRegistries.Keys.BLOCK_STATE_PROVIDER_TYPES).disableSaving().disableSync().create();
            makeRegistry(ForgeRegistries.Keys.FOLIAGE_PLACER_TYPES).disableSaving().disableSync().create();
            makeRegistry(ForgeRegistries.Keys.TREE_DECORATOR_TYPES).disableSaving().disableSync().create();
            makeRegistry(ForgeRegistries.Keys.BIOMES).disableSync().create();
        }
    }

    static RegistryBuilder<EntityDataSerializer<?>> getDataSerializersRegistryBuilder() {
        return makeRegistry(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, 256, 2147483646).disableSaving().disableOverrides();
    }

    static RegistryBuilder<Codec<? extends IGlobalLootModifier>> getGLMSerializersRegistryBuilder() {
        return makeRegistry(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS).disableSaving().disableSync();
    }

    static RegistryBuilder<Codec<? extends BiomeModifier>> getBiomeModifierSerializersRegistryBuilder() {
        return new RegistryBuilder<Codec<? extends BiomeModifier>>().disableSaving().disableSync();
    }

    static RegistryBuilder<Codec<? extends StructureModifier>> getStructureModifierSerializersRegistryBuilder() {
        return new RegistryBuilder<Codec<? extends StructureModifier>>().disableSaving().disableSync();
    }

    static RegistryBuilder<FluidType> getFluidTypeRegistryBuilder() {
        return makeRegistry(ForgeRegistries.Keys.FLUID_TYPES).disableSaving();
    }

    static <T> RegistryBuilder<T> makeUnsavedAndUnsynced() {
        return RegistryBuilder.<T>of().disableSaving().disableSync();
    }

    static RegistryBuilder<ItemDisplayContext> getItemDisplayContextRegistryBuilder() {
        return new RegistryBuilder<ItemDisplayContext>().setMaxID(256).disableOverrides().disableSaving().setDefaultKey(new ResourceLocation("minecraft:none")).onAdd(ItemDisplayContext.ADD_CALLBACK);
    }

    private static <T> RegistryBuilder<T> makeRegistry(ResourceKey<? extends Registry<T>> key) {
        return new RegistryBuilder<T>().setName(key.location()).setMaxID(2147483646).hasWrapper();
    }

    private static <T> RegistryBuilder<T> makeRegistry(ResourceKey<? extends Registry<T>> key, int min, int max) {
        return new RegistryBuilder<T>().setName(key.location()).setIDRange(min, max).hasWrapper();
    }

    private static <T> RegistryBuilder<T> makeRegistry(ResourceKey<? extends Registry<T>> key, String _default) {
        return new RegistryBuilder<T>().setName(key.location()).setMaxID(2147483646).hasWrapper().setDefaultKey(new ResourceLocation(_default));
    }

    public static <T> MappedRegistry<T> getWrapper(ResourceKey<? extends Registry<T>> key, Lifecycle lifecycle) {
        IForgeRegistry<T> reg = RegistryManager.ACTIVE.getRegistry(key);
        Validate.notNull(reg, "Attempted to get vanilla wrapper for unknown registry: " + key.toString(), new Object[0]);
        MappedRegistry<T> ret = reg.getSlaveMap(NamespacedWrapper.Factory.ID, NamespacedWrapper.class);
        Validate.notNull(ret, "Attempted to get vanilla wrapper for registry created incorrectly: " + key.toString(), new Object[0]);
        return ret;
    }

    public static <T> MappedRegistry<T> getWrapper(ResourceKey<? extends Registry<T>> key, Lifecycle lifecycle, String defKey) {
        IForgeRegistry<T> reg = RegistryManager.ACTIVE.getRegistry(key);
        Validate.notNull(reg, "Attempted to get vanilla wrapper for unknown registry: " + key.toString(), new Object[0]);
        MappedRegistry<T> ret = reg.getSlaveMap(NamespacedDefaultedWrapper.Factory.ID, NamespacedDefaultedWrapper.class);
        Validate.notNull(ret, "Attempted to get vanilla wrapper for registry created incorrectly: " + key.toString(), new Object[0]);
        return ret;
    }

    public static Map<Block, Item> getBlockItemMap() {
        return RegistryManager.ACTIVE.getRegistry(ForgeRegistries.Keys.ITEMS).getSlaveMap(BLOCK_TO_ITEM, Map.class);
    }

    public static IdMapper<BlockState> getBlockStateIDMap() {
        return RegistryManager.ACTIVE.getRegistry(ForgeRegistries.Keys.BLOCKS).getSlaveMap(BLOCKSTATE_TO_ID, IdMapper.class);
    }

    public static Map<BlockState, PoiType> getBlockStatePointOfInterestTypeMap() {
        return RegistryManager.ACTIVE.getRegistry(ForgeRegistries.Keys.POI_TYPES).getSlaveMap(BLOCKSTATE_TO_POINT_OF_INTEREST_TYPE, Map.class);
    }

    public static void vanillaSnapshot() {
        LOGGER.debug(REGISTRIES, "Creating vanilla freeze snapshot");
        for (Entry<ResourceLocation, ForgeRegistry<?>> r : RegistryManager.ACTIVE.registries.entrySet()) {
            loadRegistry((ResourceLocation) r.getKey(), RegistryManager.ACTIVE, RegistryManager.VANILLA, true);
        }
        RegistryManager.VANILLA.registries.forEach((name, reg) -> {
            reg.validateContent(name);
            reg.freeze();
        });
        RegistryManager.VANILLA.registries.forEach(LOCK_VANILLA);
        RegistryManager.ACTIVE.registries.forEach(LOCK_VANILLA);
        LOGGER.debug(REGISTRIES, "Vanilla freeze snapshot created");
    }

    public static void unfreezeData() {
        LOGGER.debug(REGISTRIES, "Unfreezing vanilla registries");
        BuiltInRegistries.REGISTRY.stream().filter(r -> r instanceof MappedRegistry).forEach(r -> ((MappedRegistry) r).unfreeze());
    }

    public static void freezeData() {
        LOGGER.debug(REGISTRIES, "Freezing registries");
        BuiltInRegistries.REGISTRY.stream().filter(rx -> rx instanceof MappedRegistry).forEach(rx -> ((MappedRegistry) rx).freeze());
        for (Entry<ResourceLocation, ForgeRegistry<?>> r : RegistryManager.ACTIVE.registries.entrySet()) {
            loadRegistry((ResourceLocation) r.getKey(), RegistryManager.ACTIVE, RegistryManager.FROZEN, true);
        }
        RegistryManager.FROZEN.registries.forEach((name, reg) -> {
            reg.validateContent(name);
            reg.freeze();
        });
        RegistryManager.ACTIVE.registries.forEach((name, reg) -> {
            reg.freeze();
            reg.bake();
            reg.dump(name);
        });
        fireRemapEvent(ImmutableMap.of(), true);
        LOGGER.debug(REGISTRIES, "All registries frozen");
    }

    public static void revertToFrozen() {
        revertTo(RegistryManager.FROZEN, true);
    }

    public static void revertTo(RegistryManager target, boolean fireEvents) {
        if (target.registries.isEmpty()) {
            LOGGER.warn(REGISTRIES, "Can't revert to {} GameData state without a valid snapshot.", target.getName());
        } else {
            RegistryManager.ACTIVE.registries.forEach((name, reg) -> reg.resetDelegates());
            LOGGER.debug(REGISTRIES, "Reverting to {} data state.", target.getName());
            for (Entry<ResourceLocation, ForgeRegistry<?>> r : RegistryManager.ACTIVE.registries.entrySet()) {
                loadRegistry((ResourceLocation) r.getKey(), target, RegistryManager.ACTIVE, true);
            }
            RegistryManager.ACTIVE.registries.forEach((name, reg) -> reg.bake());
            if (fireEvents) {
                fireRemapEvent(ImmutableMap.of(), true);
                ObjectHolderRegistry.applyObjectHolders();
            }
            LOGGER.debug(REGISTRIES, "{} state restored.", target.getName());
        }
    }

    public static void revert(RegistryManager state, ResourceLocation registry, boolean lock) {
        LOGGER.debug(REGISTRIES, "Reverting {} to {}", registry, state.getName());
        loadRegistry(registry, state, RegistryManager.ACTIVE, lock);
        LOGGER.debug(REGISTRIES, "Reverting complete");
    }

    public static void postRegisterEvents() {
        Set<ResourceLocation> keySet = new HashSet(RegistryManager.ACTIVE.registries.keySet());
        keySet.addAll(RegistryManager.getVanillaRegistryKeys());
        Set<ResourceLocation> ordered = new LinkedHashSet(MappedRegistry.getKnownRegistries());
        ordered.retainAll(keySet);
        ordered.addAll(keySet.stream().sorted(ResourceLocation::compareNamespaced).toList());
        RuntimeException aggregate = new RuntimeException();
        for (ResourceLocation rootRegistryName : ordered) {
            try {
                ResourceKey<? extends Registry<?>> registryKey = ResourceKey.createRegistryKey(rootRegistryName);
                ForgeRegistry<?> forgeRegistry = RegistryManager.ACTIVE.getRegistry(rootRegistryName);
                Registry<?> vanillaRegistry = (Registry<?>) BuiltInRegistries.REGISTRY.get(rootRegistryName);
                RegisterEvent registerEvent = new RegisterEvent(registryKey, forgeRegistry, vanillaRegistry);
                StartupMessageManager.modLoaderConsumer().ifPresent(s -> s.accept("REGISTERING " + registryKey.location()));
                if (forgeRegistry != null) {
                    forgeRegistry.unfreeze();
                }
                ModLoader.get().postEventWrapContainerInModOrder(registerEvent);
                if (forgeRegistry != null) {
                    forgeRegistry.freeze();
                }
                LOGGER.debug(REGISTRIES, "Applying holder lookups: {}", registryKey.location());
                ObjectHolderRegistry.applyObjectHolders(registryKey.location()::equals);
                LOGGER.debug(REGISTRIES, "Holder lookups applied: {}", registryKey.location());
            } catch (Throwable var9) {
                aggregate.addSuppressed(var9);
            }
        }
        if (aggregate.getSuppressed().length > 0) {
            LOGGER.fatal("Failed to register some entries, see suppressed exceptions for details", aggregate);
            LOGGER.fatal("Detected errors during registry event dispatch, rolling back to VANILLA state");
            revertTo(RegistryManager.VANILLA, false);
            LOGGER.fatal("Detected errors during registry event dispatch, roll back to VANILLA complete");
            throw aggregate;
        } else {
            ForgeHooks.modifyAttributes();
            SpawnPlacements.fireSpawnPlacementEvent();
            CreativeModeTabRegistry.sortTabs();
        }
    }

    private static <T> void loadRegistry(final ResourceLocation registryName, final RegistryManager from, final RegistryManager to, boolean freeze) {
        ForgeRegistry<T> fromRegistry = from.getRegistry(registryName);
        if (fromRegistry == null) {
            ForgeRegistry<T> toRegistry = to.getRegistry(registryName);
            if (toRegistry == null) {
                throw new EnhancedRuntimeException("Could not find registry to load: " + registryName) {

                    private static final long serialVersionUID = 1L;

                    protected void printStackTrace(WrappedPrintStream stream) {
                        stream.println("Looking For: " + registryName);
                        stream.println("Found From:");
                        for (ResourceLocation name : from.registries.keySet()) {
                            stream.println("  " + name);
                        }
                        stream.println("Found To:");
                        for (ResourceLocation name : to.registries.keySet()) {
                            stream.println("  " + name);
                        }
                    }
                };
            }
        } else {
            ForgeRegistry<T> toRegistry = to.getRegistry(registryName, from);
            toRegistry.sync(registryName, fromRegistry);
            if (freeze) {
                toRegistry.isFrozen = true;
            }
        }
    }

    public static Multimap<ResourceLocation, ResourceLocation> injectSnapshot(Map<ResourceLocation, ForgeRegistry.Snapshot> snapshot, boolean injectFrozenData, boolean isLocalWorld) {
        LOGGER.info(REGISTRIES, "Injecting existing registry data into this {} instance", EffectiveSide.get());
        RegistryManager.ACTIVE.registries.forEach((name, reg) -> reg.validateContent(name));
        RegistryManager.ACTIVE.registries.forEach((name, reg) -> reg.dump(name));
        RegistryManager.ACTIVE.registries.forEach((name, reg) -> reg.resetDelegates());
        snapshot = (Map<ResourceLocation, ForgeRegistry.Snapshot>) snapshot.entrySet().stream().sorted(Entry.comparingByKey()).collect(Collectors.toMap(e -> RegistryManager.ACTIVE.updateLegacyName((ResourceLocation) e.getKey()), Entry::getValue, (k1, k2) -> k1, LinkedHashMap::new));
        if (isLocalWorld) {
            ResourceLocation[] missingRegs = (ResourceLocation[]) snapshot.keySet().stream().filter(name -> !RegistryManager.ACTIVE.registries.containsKey(name)).toArray(ResourceLocation[]::new);
            if (missingRegs.length > 0) {
                String header = "Forge Mod Loader detected missing/unknown registrie(s).\n\nThere are " + missingRegs.length + " missing registries in this save.\nIf you continue the missing registries will get removed.\nThis may cause issues, it is advised that you create a world backup before continuing.\n\n";
                StringBuilder text = new StringBuilder("Missing Registries:\n");
                for (ResourceLocation s : missingRegs) {
                    text.append(s).append("\n");
                }
                LOGGER.warn(REGISTRIES, header);
                LOGGER.warn(REGISTRIES, text.toString());
            }
        }
        RegistryManager STAGING = new RegistryManager();
        Map<ResourceLocation, Map<ResourceLocation, IdMappingEvent.IdRemapping>> remaps = new HashMap();
        LinkedHashMap<ResourceLocation, Object2IntMap<ResourceLocation>> missing = new LinkedHashMap();
        snapshot.forEach((key, value) -> {
            remaps.put(key, new LinkedHashMap());
            missing.put(key, new Object2IntLinkedOpenHashMap());
            loadPersistentDataToStagingRegistry(RegistryManager.ACTIVE, STAGING, (Map<ResourceLocation, IdMappingEvent.IdRemapping>) remaps.get(key), (Object2IntMap<ResourceLocation>) missing.get(key), key, value);
        });
        int count = missing.values().stream().mapToInt(Map::size).sum();
        if (count > 0) {
            LOGGER.debug(REGISTRIES, "There are {} mappings missing - attempting a mod remap", count);
            Multimap<ResourceLocation, ResourceLocation> defaulted = ArrayListMultimap.create();
            Multimap<ResourceLocation, ResourceLocation> failed = ArrayListMultimap.create();
            missing.entrySet().stream().filter(e -> !((Object2IntMap) e.getValue()).isEmpty()).forEach(m -> {
                ResourceLocation name = (ResourceLocation) m.getKey();
                ForgeRegistry<?> reg = STAGING.getRegistry(name);
                Object2IntMap<ResourceLocation> missingIds = (Object2IntMap<ResourceLocation>) m.getValue();
                MissingMappingsEvent event = reg.getMissingEvent(name, missingIds);
                MinecraftForge.EVENT_BUS.post(event);
                List<MissingMappingsEvent.Mapping<?>> lst = (List<MissingMappingsEvent.Mapping<?>>) event.getAllMappings(reg.getRegistryKey()).stream().filter(e -> e.action == MissingMappingsEvent.Action.DEFAULT).sorted(Comparator.comparing(Object::toString)).collect(Collectors.toList());
                if (!lst.isEmpty()) {
                    LOGGER.error(REGISTRIES, () -> LogMessageAdapter.adapt(sb -> {
                        sb.append("Unidentified mapping from registry ").append(name).append('\n');
                        lst.stream().sorted().forEach(map -> sb.append('\t').append(map.key).append(": ").append(map.id).append('\n'));
                    }));
                }
                event.getAllMappings(reg.getRegistryKey()).stream().filter(e -> e.action == MissingMappingsEvent.Action.FAIL).forEach(fail -> failed.put(name, fail.key));
                processMissing(name, STAGING, event, missingIds, (Map<ResourceLocation, IdMappingEvent.IdRemapping>) remaps.get(name), defaulted.get(name), failed.get(name), !isLocalWorld);
            });
            if (!defaulted.isEmpty() && !isLocalWorld) {
                return defaulted;
            }
            if (!defaulted.isEmpty()) {
                String header = "Forge Mod Loader detected missing registry entries.\n\nThere are " + defaulted.size() + " missing entries in this save.\nIf you continue the missing entries will get removed.\nA world backup will be automatically created in your saves directory.\n\n";
                StringBuilder buf = new StringBuilder();
                defaulted.asMap().forEach((name, entries) -> {
                    buf.append("Missing ").append(name).append(":\n");
                    entries.stream().sorted(ResourceLocation::compareNamespaced).forEach(rl -> buf.append("    ").append(rl).append("\n"));
                    buf.append("\n");
                });
                LOGGER.warn(REGISTRIES, header);
                LOGGER.warn(REGISTRIES, buf.toString());
            }
            if (!defaulted.isEmpty() && isLocalWorld) {
                LOGGER.error(REGISTRIES, "There are unidentified mappings in this world - we are going to attempt to process anyway");
            }
        }
        if (injectFrozenData) {
            RegistryManager.ACTIVE.registries.forEach((name, reg) -> loadFrozenDataToStagingRegistry(STAGING, name, (Map<ResourceLocation, IdMappingEvent.IdRemapping>) remaps.get(name)));
        }
        STAGING.registries.forEach((name, reg) -> reg.validateContent(name));
        RegistryManager.ACTIVE.registries.forEach((key, value) -> loadRegistry(key, STAGING, RegistryManager.ACTIVE, true));
        RegistryManager.ACTIVE.registries.forEach((name, reg) -> {
            reg.bake();
            reg.dump(name);
        });
        fireRemapEvent(remaps, false);
        ObjectHolderRegistry.applyObjectHolders();
        return ArrayListMultimap.create();
    }

    private static void fireRemapEvent(Map<ResourceLocation, Map<ResourceLocation, IdMappingEvent.IdRemapping>> remaps, boolean isFreezing) {
        MinecraftForge.EVENT_BUS.post(new IdMappingEvent(remaps, isFreezing));
    }

    private static <T> void loadPersistentDataToStagingRegistry(RegistryManager pool, RegistryManager to, Map<ResourceLocation, IdMappingEvent.IdRemapping> remaps, Object2IntMap<ResourceLocation> missing, ResourceLocation name, ForgeRegistry.Snapshot snap) {
        ForgeRegistry<T> active = pool.getRegistry(name);
        if (active != null) {
            ForgeRegistry<T> _new = to.getRegistry(name, RegistryManager.ACTIVE);
            snap.aliases.forEach(_new::addAlias);
            snap.blocked.forEach(_new::block);
            _new.loadIds(snap.ids, snap.overrides, missing, remaps, active, name);
        }
    }

    private static <T> void processMissing(ResourceLocation name, RegistryManager STAGING, MissingMappingsEvent e, Object2IntMap<ResourceLocation> missing, Map<ResourceLocation, IdMappingEvent.IdRemapping> remaps, Collection<ResourceLocation> defaulted, Collection<ResourceLocation> failed, boolean injectNetworkDummies) {
        List<MissingMappingsEvent.Mapping<T>> mappings = e.getAllMappings(ResourceKey.createRegistryKey(name));
        ForgeRegistry<T> active = RegistryManager.ACTIVE.getRegistry(name);
        ForgeRegistry<T> staging = STAGING.getRegistry(name);
        staging.processMissingEvent(name, active, mappings, missing, remaps, defaulted, failed, injectNetworkDummies);
    }

    private static <T> void loadFrozenDataToStagingRegistry(RegistryManager STAGING, ResourceLocation name, Map<ResourceLocation, IdMappingEvent.IdRemapping> remaps) {
        ForgeRegistry<T> frozen = RegistryManager.FROZEN.getRegistry(name);
        ForgeRegistry<T> newRegistry = STAGING.getRegistry(name, RegistryManager.FROZEN);
        Object2IntMap<ResourceLocation> _new = new Object2IntLinkedOpenHashMap();
        frozen.getKeys().stream().filter(key -> !newRegistry.containsKey(key)).forEach(key -> _new.put(key, frozen.getID(key)));
        newRegistry.loadIds(_new, frozen.getOverrideOwners(), new Object2IntLinkedOpenHashMap(), remaps, frozen, name);
    }

    public static ResourceLocation checkPrefix(String name, boolean warnOverrides) {
        int index = name.lastIndexOf(58);
        String oldPrefix = index == -1 ? "" : name.substring(0, index).toLowerCase(Locale.ROOT);
        name = index == -1 ? name : name.substring(index + 1);
        String prefix = ModLoadingContext.get().getActiveNamespace();
        if (warnOverrides && !oldPrefix.equals(prefix) && !oldPrefix.isEmpty()) {
            LogManager.getLogger().debug("Mod `{}` attempting to register `{}` to the namespace `{}`. This could be intended, but likely means an EventBusSubscriber without a modid.", prefix, name, oldPrefix);
            prefix = oldPrefix;
        }
        return new ResourceLocation(prefix, name);
    }

    static {
        init();
    }

    private static class AttributeCallbacks implements IForgeRegistry.ValidateCallback<Attribute> {

        static final GameData.AttributeCallbacks INSTANCE = new GameData.AttributeCallbacks();

        public void onValidate(IForgeRegistryInternal<Attribute> owner, RegistryManager stage, int id, ResourceLocation key, Attribute obj) {
            if (stage != RegistryManager.VANILLA) {
                DefaultAttributes.validate();
            }
        }
    }

    private static class BlockCallbacks implements IForgeRegistry.AddCallback<Block>, IForgeRegistry.ClearCallback<Block>, IForgeRegistry.BakeCallback<Block>, IForgeRegistry.CreateCallback<Block> {

        static final GameData.BlockCallbacks INSTANCE = new GameData.BlockCallbacks();

        public void onAdd(IForgeRegistryInternal<Block> owner, RegistryManager stage, int id, ResourceKey<Block> key, Block block, @Nullable Block oldBlock) {
            if (oldBlock != null) {
                StateDefinition<Block, BlockState> oldContainer = oldBlock.getStateDefinition();
                StateDefinition<Block, BlockState> newContainer = block.getStateDefinition();
                if (key.location().getNamespace().equals("minecraft") && !oldContainer.getProperties().equals(newContainer.getProperties())) {
                    String oldSequence = (String) oldContainer.getProperties().stream().map(s -> String.format(Locale.ENGLISH, "%s={%s}", s.getName(), s.getPossibleValues().stream().map(Object::toString).collect(Collectors.joining(",")))).collect(Collectors.joining(";"));
                    String newSequence = (String) newContainer.getProperties().stream().map(s -> String.format(Locale.ENGLISH, "%s={%s}", s.getName(), s.getPossibleValues().stream().map(Object::toString).collect(Collectors.joining(",")))).collect(Collectors.joining(";"));
                    GameData.LOGGER.error(GameData.REGISTRIES, () -> LogMessageAdapter.adapt(sb -> {
                        sb.append("Registry replacements for vanilla block '").append(key.location()).append("' must not change the number or order of blockstates.\n");
                        sb.append("\tOld: ").append(oldSequence).append('\n');
                        sb.append("\tNew: ").append(newSequence);
                    }));
                    throw new RuntimeException("Invalid vanilla replacement. See log for details.");
                }
            }
        }

        @Override
        public void onClear(IForgeRegistryInternal<Block> owner, RegistryManager stage) {
            owner.<GameData.ClearableObjectIntIdentityMap>getSlaveMap(GameData.BLOCKSTATE_TO_ID, GameData.ClearableObjectIntIdentityMap.class).clear();
        }

        @Override
        public void onCreate(IForgeRegistryInternal<Block> owner, RegistryManager stage) {
            GameData.ClearableObjectIntIdentityMap<BlockState> idMap = new GameData.ClearableObjectIntIdentityMap<BlockState>() {

                public int getId(BlockState key) {
                    return this.f_122654_.containsKey(key) ? this.f_122654_.getInt(key) : -1;
                }
            };
            owner.setSlaveMap(GameData.BLOCKSTATE_TO_ID, idMap);
            owner.setSlaveMap(GameData.BLOCK_TO_ITEM, new HashMap());
        }

        @Override
        public void onBake(IForgeRegistryInternal<Block> owner, RegistryManager stage) {
            GameData.ClearableObjectIntIdentityMap<BlockState> blockstateMap = owner.getSlaveMap(GameData.BLOCKSTATE_TO_ID, GameData.ClearableObjectIntIdentityMap.class);
            for (Block block : owner) {
                UnmodifiableIterator var6 = block.getStateDefinition().getPossibleStates().iterator();
                while (var6.hasNext()) {
                    BlockState state = (BlockState) var6.next();
                    blockstateMap.m_122667_(state);
                    state.m_60611_();
                }
                block.m_60589_();
            }
            DebugLevelSource.initValidStates();
        }
    }

    private static class ClearableObjectIntIdentityMap<I> extends IdMapper<I> {

        void clear() {
            this.f_122654_.clear();
            this.f_122655_.clear();
            this.f_122653_ = 0;
        }

        void remove(I key) {
            boolean hadId = this.f_122654_.containsKey(key);
            int prev = this.f_122654_.removeInt(key);
            if (hadId) {
                this.f_122655_.set(prev, null);
            }
        }
    }

    private static class ItemCallbacks implements IForgeRegistry.AddCallback<Item>, IForgeRegistry.ClearCallback<Item>, IForgeRegistry.CreateCallback<Item> {

        static final GameData.ItemCallbacks INSTANCE = new GameData.ItemCallbacks();

        public void onAdd(IForgeRegistryInternal<Item> owner, RegistryManager stage, int id, ResourceKey<Item> key, Item item, @Nullable Item oldItem) {
            if (oldItem instanceof BlockItem) {
                Map<Block, Item> blockToItem = owner.getSlaveMap(GameData.BLOCK_TO_ITEM, Map.class);
                ((BlockItem) oldItem).removeFromBlockToItemMap(blockToItem, item);
            }
            if (item instanceof BlockItem) {
                Map<Block, Item> blockToItem = owner.getSlaveMap(GameData.BLOCK_TO_ITEM, Map.class);
                ((BlockItem) item).registerBlocks(blockToItem, item);
            }
        }

        @Override
        public void onClear(IForgeRegistryInternal<Item> owner, RegistryManager stage) {
            owner.<Map>getSlaveMap(GameData.BLOCK_TO_ITEM, Map.class).clear();
        }

        @Override
        public void onCreate(IForgeRegistryInternal<Item> owner, RegistryManager stage) {
            Map<?, ?> map = stage.getRegistry(ForgeRegistries.Keys.BLOCKS).getSlaveMap(GameData.BLOCK_TO_ITEM, Map.class);
            owner.setSlaveMap(GameData.BLOCK_TO_ITEM, map);
        }
    }

    private static class PointOfInterestTypeCallbacks implements IForgeRegistry.AddCallback<PoiType>, IForgeRegistry.ClearCallback<PoiType>, IForgeRegistry.CreateCallback<PoiType> {

        static final GameData.PointOfInterestTypeCallbacks INSTANCE = new GameData.PointOfInterestTypeCallbacks();

        public void onAdd(IForgeRegistryInternal<PoiType> owner, RegistryManager stage, int id, ResourceKey<PoiType> key, PoiType obj, @Nullable PoiType oldObj) {
            Map<BlockState, PoiType> map = owner.getSlaveMap(GameData.BLOCKSTATE_TO_POINT_OF_INTEREST_TYPE, Map.class);
            if (oldObj != null) {
                oldObj.matchingStates().forEach(map::remove);
            }
            obj.matchingStates().forEach(state -> {
                PoiType oldType = (PoiType) map.put(state, obj);
                if (oldType != null) {
                    throw new IllegalStateException(String.format(Locale.ENGLISH, "Point of interest types %s and %s both list %s in their blockstates, this is not allowed. Blockstates can only have one point of interest type each.", oldType, obj, state));
                }
            });
        }

        @Override
        public void onClear(IForgeRegistryInternal<PoiType> owner, RegistryManager stage) {
            owner.<Map>getSlaveMap(GameData.BLOCKSTATE_TO_POINT_OF_INTEREST_TYPE, Map.class).clear();
        }

        @Override
        public void onCreate(IForgeRegistryInternal<PoiType> owner, RegistryManager stage) {
            owner.setSlaveMap(GameData.BLOCKSTATE_TO_POINT_OF_INTEREST_TYPE, new HashMap());
        }
    }
}