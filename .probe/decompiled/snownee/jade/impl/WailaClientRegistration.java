package snownee.jade.impl;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.Accessor;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.IJadeProvider;
import snownee.jade.api.IToggleableProvider;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.callback.JadeAfterRenderCallback;
import snownee.jade.api.callback.JadeBeforeRenderCallback;
import snownee.jade.api.callback.JadeItemModNameCallback;
import snownee.jade.api.callback.JadeRayTraceCallback;
import snownee.jade.api.callback.JadeRenderBackgroundCallback;
import snownee.jade.api.callback.JadeTooltipCollectedCallback;
import snownee.jade.api.config.IWailaConfig;
import snownee.jade.api.config.TargetBlocklist;
import snownee.jade.api.platform.CustomEnchantPower;
import snownee.jade.api.view.EnergyView;
import snownee.jade.api.view.FluidView;
import snownee.jade.api.view.IClientExtensionProvider;
import snownee.jade.api.view.ItemView;
import snownee.jade.api.view.ProgressView;
import snownee.jade.gui.PluginsConfigScreen;
import snownee.jade.impl.config.PluginConfig;
import snownee.jade.impl.config.entry.BooleanConfigEntry;
import snownee.jade.impl.config.entry.EnumConfigEntry;
import snownee.jade.impl.config.entry.FloatConfigEntry;
import snownee.jade.impl.config.entry.IntConfigEntry;
import snownee.jade.impl.config.entry.StringConfigEntry;
import snownee.jade.overlay.DatapackBlockManager;
import snownee.jade.util.ClientProxy;
import snownee.jade.util.CommonProxy;
import snownee.jade.util.JadeCodecs;
import snownee.jade.util.JsonConfig;

public class WailaClientRegistration implements IWailaClientRegistration {

    public static final WailaClientRegistration INSTANCE = new WailaClientRegistration();

    public final HierarchyLookup<IBlockComponentProvider> blockIconProviders;

    public final HierarchyLookup<IBlockComponentProvider> blockComponentProviders;

    public final HierarchyLookup<IEntityComponentProvider> entityIconProviders;

    public final HierarchyLookup<IEntityComponentProvider> entityComponentProviders;

    public final Set<Block> hideBlocks = Sets.newHashSet();

    public ImmutableSet<Block> hideBlocksReloadable = ImmutableSet.of();

    public final Set<EntityType<?>> hideEntities = Sets.newHashSet();

    public ImmutableSet<EntityType<?>> hideEntitiesReloadable = ImmutableSet.of();

    public final Set<Block> pickBlocks = Sets.newHashSet();

    public final Set<EntityType<?>> pickEntities = Sets.newHashSet();

    public final CallbackContainer<JadeAfterRenderCallback> afterRenderCallback = new CallbackContainer<>();

    public final CallbackContainer<JadeBeforeRenderCallback> beforeRenderCallback = new CallbackContainer<>();

    public final CallbackContainer<JadeRayTraceCallback> rayTraceCallback = new CallbackContainer<>();

    public final CallbackContainer<JadeTooltipCollectedCallback> tooltipCollectedCallback = new CallbackContainer<>();

    public final CallbackContainer<JadeItemModNameCallback> itemModNameCallback = new CallbackContainer<>();

    public final CallbackContainer<JadeRenderBackgroundCallback> renderBackgroundCallback = new CallbackContainer<>();

    public final Map<Block, CustomEnchantPower> customEnchantPowers = Maps.newHashMap();

    public final Map<ResourceLocation, IClientExtensionProvider<ItemStack, ItemView>> itemStorageProviders = Maps.newHashMap();

    public final Map<ResourceLocation, IClientExtensionProvider<CompoundTag, FluidView>> fluidStorageProviders = Maps.newHashMap();

    public final Map<ResourceLocation, IClientExtensionProvider<CompoundTag, EnergyView>> energyStorageProviders = Maps.newHashMap();

    public final Map<ResourceLocation, IClientExtensionProvider<CompoundTag, ProgressView>> progressProviders = Maps.newHashMap();

    public final Set<ResourceLocation> clientFeatures = Sets.newHashSet();

    public final Map<Class<Accessor<?>>, Accessor.ClientHandler<Accessor<?>>> accessorHandlers = Maps.newIdentityHashMap();

    WailaClientRegistration() {
        this.blockIconProviders = new HierarchyLookup<>(Block.class);
        this.blockComponentProviders = new HierarchyLookup<>(Block.class);
        this.entityIconProviders = new HierarchyLookup<>(Entity.class);
        this.entityComponentProviders = new HierarchyLookup<>(Entity.class);
    }

    public static WailaClientRegistration instance() {
        return INSTANCE;
    }

    public static JsonConfig<TargetBlocklist> createEntityBlocklist() {
        return new JsonConfig<>("jade/hide-entities", JadeCodecs.TARGET_BLOCKLIST_CODEC, null, () -> {
            TargetBlocklist blocklist = new TargetBlocklist();
            blocklist.values = Stream.of(EntityType.AREA_EFFECT_CLOUD, EntityType.FIREWORK_ROCKET, EntityType.INTERACTION, EntityType.TEXT_DISPLAY, EntityType.LIGHTNING_BOLT).map(EntityType::m_20613_).map(Object::toString).toList();
            return blocklist;
        });
    }

    public static JsonConfig<TargetBlocklist> createBlockBlocklist() {
        return new JsonConfig<>("jade/hide-blocks", JadeCodecs.TARGET_BLOCKLIST_CODEC, null, () -> {
            TargetBlocklist blocklist = new TargetBlocklist();
            blocklist.values = List.of("minecraft:barrier");
            return blocklist;
        });
    }

    @Override
    public void registerBlockIcon(IBlockComponentProvider provider, Class<? extends Block> block) {
        this.blockIconProviders.register(block, provider);
        this.tryAddConfig(provider);
    }

    @Override
    public void registerBlockComponent(IBlockComponentProvider provider, Class<? extends Block> block) {
        this.blockComponentProviders.register(block, provider);
        this.tryAddConfig(provider);
    }

    @Override
    public void registerEntityIcon(IEntityComponentProvider provider, Class<? extends Entity> entity) {
        this.entityIconProviders.register(entity, provider);
        this.tryAddConfig(provider);
    }

    @Override
    public void registerEntityComponent(IEntityComponentProvider provider, Class<? extends Entity> entity) {
        this.entityComponentProviders.register(entity, provider);
        this.tryAddConfig(provider);
    }

    public List<IBlockComponentProvider> getBlockProviders(Block block, Predicate<IBlockComponentProvider> filter) {
        return this.blockComponentProviders.get(block).stream().filter(filter).toList();
    }

    public List<IBlockComponentProvider> getBlockIconProviders(Block block, Predicate<IBlockComponentProvider> filter) {
        return this.blockIconProviders.get(block).stream().filter(filter).toList();
    }

    public List<IEntityComponentProvider> getEntityProviders(Entity entity, Predicate<IEntityComponentProvider> filter) {
        return this.entityComponentProviders.get(entity).stream().filter(filter).toList();
    }

    public List<IEntityComponentProvider> getEntityIconProviders(Entity entity, Predicate<IEntityComponentProvider> filter) {
        return this.entityIconProviders.get(entity).stream().filter(filter).toList();
    }

    @Override
    public void hideTarget(Block block) {
        Objects.requireNonNull(block);
        this.hideBlocks.add(block);
    }

    @Override
    public void hideTarget(EntityType<?> entityType) {
        Objects.requireNonNull(entityType);
        this.hideEntities.add(entityType);
    }

    @Override
    public void usePickedResult(Block block) {
        Objects.requireNonNull(block);
        this.pickBlocks.add(block);
    }

    @Override
    public void usePickedResult(EntityType<?> entityType) {
        Objects.requireNonNull(entityType);
        this.pickEntities.add(entityType);
    }

    @Override
    public boolean shouldHide(BlockState state) {
        return this.hideBlocksReloadable.contains(state.m_60734_());
    }

    @Override
    public boolean shouldPick(BlockState state) {
        return this.pickBlocks.contains(state.m_60734_());
    }

    @Override
    public boolean shouldHide(Entity entity) {
        return this.hideEntitiesReloadable.contains(entity.getType());
    }

    @Override
    public boolean shouldPick(Entity entity) {
        return this.pickEntities.contains(entity.getType());
    }

    @Override
    public void addConfig(ResourceLocation key, boolean defaultValue) {
        PluginConfig.INSTANCE.addConfig(new BooleanConfigEntry(key, defaultValue));
    }

    @Override
    public void addConfig(ResourceLocation key, Enum<?> defaultValue) {
        Objects.requireNonNull(defaultValue);
        PluginConfig.INSTANCE.addConfig(new EnumConfigEntry<>(key, defaultValue));
    }

    @Override
    public void addConfig(ResourceLocation key, String defaultValue, Predicate<String> validator) {
        Objects.requireNonNull(defaultValue);
        Objects.requireNonNull(validator);
        PluginConfig.INSTANCE.addConfig(new StringConfigEntry(key, defaultValue, validator));
    }

    @Override
    public void addConfig(ResourceLocation key, int defaultValue, int min, int max, boolean slider) {
        PluginConfig.INSTANCE.addConfig(new IntConfigEntry(key, defaultValue, min, max, slider));
    }

    @Override
    public void addConfig(ResourceLocation key, float defaultValue, float min, float max, boolean slider) {
        PluginConfig.INSTANCE.addConfig(new FloatConfigEntry(key, defaultValue, min, max, slider));
    }

    @Override
    public void addConfigListener(ResourceLocation key, Consumer<ResourceLocation> listener) {
        PluginConfig.INSTANCE.addConfigListener(key, listener);
    }

    private void tryAddConfig(IToggleableProvider provider) {
        if (!CommonProxy.isBlockedUid(provider) && !provider.isRequired() && !PluginConfig.INSTANCE.containsKey(provider.getUid())) {
            this.addConfig(provider.getUid(), provider.enabledByDefault());
        }
    }

    public void loadComplete() {
        this.reloadBlocklists();
        PriorityStore<ResourceLocation, IJadeProvider> priorities = WailaCommonRegistration.INSTANCE.priorities;
        this.blockComponentProviders.loadComplete(priorities);
        this.blockIconProviders.loadComplete(priorities);
        this.entityComponentProviders.loadComplete(priorities);
        this.entityIconProviders.loadComplete(priorities);
        Stream.of(this.afterRenderCallback, this.beforeRenderCallback, this.rayTraceCallback, this.tooltipCollectedCallback, this.itemModNameCallback).forEach(CallbackContainer::sort);
    }

    public void reloadBlocklists() {
        this.hideEntitiesReloadable = Util.make(ImmutableSet.builder(), it -> {
            it.addAll(this.hideEntities);
            for (String id : createEntityBlocklist().get().values) {
                BuiltInRegistries.ENTITY_TYPE.m_6612_(ResourceLocation.tryParse(id)).ifPresent(it::add);
            }
        }).build();
        this.hideBlocksReloadable = Util.make(ImmutableSet.builder(), it -> {
            it.addAll(this.hideBlocks);
            for (String id : createBlockBlocklist().get().values) {
                BuiltInRegistries.BLOCK.m_6612_(ResourceLocation.tryParse(id)).ifPresent(it::add);
            }
        }).build();
    }

    @Override
    public void addAfterRenderCallback(int priority, JadeAfterRenderCallback callback) {
        this.afterRenderCallback.add(priority, callback);
    }

    @Override
    public void addBeforeRenderCallback(int priority, JadeBeforeRenderCallback callback) {
        this.beforeRenderCallback.add(priority, callback);
    }

    @Override
    public void addRayTraceCallback(int priority, JadeRayTraceCallback callback) {
        this.rayTraceCallback.add(priority, callback);
    }

    @Override
    public void addTooltipCollectedCallback(int priority, JadeTooltipCollectedCallback callback) {
        this.tooltipCollectedCallback.add(priority, callback);
    }

    @Override
    public void addItemModNameCallback(int priority, JadeItemModNameCallback callback) {
        this.itemModNameCallback.add(priority, callback);
    }

    @Override
    public void addRenderBackgroundCallback(int priority, JadeRenderBackgroundCallback callback) {
        this.renderBackgroundCallback.add(priority, callback);
    }

    @Override
    public BlockAccessor.Builder blockAccessor() {
        Minecraft mc = Minecraft.getInstance();
        return new BlockAccessorImpl.Builder().level(mc.level).player(mc.player).serverConnected(this.isServerConnected()).serverData(this.getServerData()).showDetails(this.isShowDetailsPressed());
    }

    @Override
    public EntityAccessor.Builder entityAccessor() {
        Minecraft mc = Minecraft.getInstance();
        return new EntityAccessorImpl.Builder().level(mc.level).player(mc.player).serverConnected(this.isServerConnected()).serverData(this.getServerData()).showDetails(this.isShowDetailsPressed());
    }

    @Override
    public void registerCustomEnchantPower(Block block, CustomEnchantPower customEnchantPower) {
        this.customEnchantPowers.put(block, customEnchantPower);
    }

    @Override
    public Screen createPluginConfigScreen(@Nullable Screen parent, @Nullable String namespace) {
        return PluginsConfigScreen.createPluginConfigScreen(parent, namespace, false);
    }

    @Override
    public void registerItemStorageClient(IClientExtensionProvider<ItemStack, ItemView> provider) {
        Objects.requireNonNull(provider.getUid());
        this.itemStorageProviders.put(provider.getUid(), provider);
    }

    @Override
    public void registerFluidStorageClient(IClientExtensionProvider<CompoundTag, FluidView> provider) {
        Objects.requireNonNull(provider.getUid());
        this.fluidStorageProviders.put(provider.getUid(), provider);
    }

    @Override
    public void registerEnergyStorageClient(IClientExtensionProvider<CompoundTag, EnergyView> provider) {
        Objects.requireNonNull(provider.getUid());
        this.energyStorageProviders.put(provider.getUid(), provider);
    }

    @Override
    public void registerProgressClient(IClientExtensionProvider<CompoundTag, ProgressView> provider) {
        Objects.requireNonNull(provider.getUid());
        this.progressProviders.put(provider.getUid(), provider);
    }

    @Override
    public boolean isServerConnected() {
        return ObjectDataCenter.serverConnected;
    }

    @Override
    public boolean isShowDetailsPressed() {
        return ClientProxy.isShowDetailsPressed();
    }

    @Override
    public CompoundTag getServerData() {
        return ObjectDataCenter.getServerData();
    }

    @Override
    public void setServerData(CompoundTag tag) {
        ObjectDataCenter.setServerData(tag);
    }

    @Override
    public ItemStack getBlockCamouflage(LevelAccessor level, BlockPos pos) {
        return DatapackBlockManager.getFakeBlock(level, pos);
    }

    @Override
    public void markAsClientFeature(ResourceLocation uid) {
        this.clientFeatures.add(uid);
    }

    @Override
    public void markAsServerFeature(ResourceLocation uid) {
        this.clientFeatures.remove(uid);
    }

    @Override
    public boolean isClientFeature(ResourceLocation uid) {
        return this.clientFeatures.contains(uid);
    }

    @Override
    public <T extends Accessor<?>> void registerAccessorHandler(Class<T> clazz, Accessor.ClientHandler<T> handler) {
        this.accessorHandlers.put(clazz, handler);
    }

    @Override
    public Accessor.ClientHandler<Accessor<?>> getAccessorHandler(Class<? extends Accessor<?>> clazz) {
        return (Accessor.ClientHandler<Accessor<?>>) Objects.requireNonNull((Accessor.ClientHandler) this.accessorHandlers.get(clazz), () -> "No accessor handler for " + clazz);
    }

    @Override
    public boolean maybeLowVisionUser() {
        return ClientProxy.maybeLowVisionUser || IWailaConfig.get().getGeneral().shouldEnableTextToSpeech();
    }
}