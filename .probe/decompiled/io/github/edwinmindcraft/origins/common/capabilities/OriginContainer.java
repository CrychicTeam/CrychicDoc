package io.github.edwinmindcraft.origins.common.capabilities;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.collect.ImmutableMap.Builder;
import io.github.apace100.origins.Origins;
import io.github.apace100.origins.component.OriginComponent;
import io.github.apace100.origins.component.PlayerOriginComponent;
import io.github.apace100.origins.util.ChoseOriginCriterion;
import io.github.edwinmindcraft.apoli.api.ApoliAPI;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.calio.api.registry.ICalioDynamicRegistryManager;
import io.github.edwinmindcraft.origins.api.OriginsAPI;
import io.github.edwinmindcraft.origins.api.capabilities.IOriginContainer;
import io.github.edwinmindcraft.origins.api.origin.IOriginCallbackPower;
import io.github.edwinmindcraft.origins.api.origin.Origin;
import io.github.edwinmindcraft.origins.api.origin.OriginLayer;
import io.github.edwinmindcraft.origins.api.registry.OriginsDynamicRegistries;
import io.github.edwinmindcraft.origins.common.OriginsCommon;
import io.github.edwinmindcraft.origins.common.network.S2COpenWaitingForPowersScreen;
import io.github.edwinmindcraft.origins.common.network.S2CSynchronizeOrigin;
import io.github.edwinmindcraft.origins.common.registry.OriginRegisters;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OriginContainer implements IOriginContainer, ICapabilitySerializable<Tag> {

    public static final ResourceLocation ID = Origins.identifier("origins");

    private final Player player;

    private final Map<ResourceKey<OriginLayer>, ResourceKey<Origin>> layers;

    private final AtomicBoolean synchronization;

    private final AtomicBoolean hadAllOrigins;

    private boolean cleanupPowers = true;

    @Deprecated
    private final Lazy<OriginComponent> component = Lazy.of(() -> new PlayerOriginComponent(this));

    private final LazyOptional<IOriginContainer> thisOptional = LazyOptional.of(() -> this);

    private final AtomicInteger syncCooldown = new AtomicInteger(0);

    public OriginContainer(Player player) {
        this.player = player;
        this.layers = new ConcurrentHashMap();
        this.synchronization = new AtomicBoolean();
        this.hadAllOrigins = new AtomicBoolean();
    }

    @Override
    public void setOrigin(@NotNull ResourceKey<OriginLayer> layer, @NotNull ResourceKey<Origin> origin) {
        this.setOriginInternal(layer, origin, true);
    }

    public void setOriginInternal(@NotNull ResourceKey<OriginLayer> layer, @NotNull ResourceKey<Origin> origin, boolean handlePowers) {
        Optional<Holder.Reference<OriginLayer>> layerHolder = OriginsAPI.getLayersRegistry().getHolder(layer);
        Optional<Holder.Reference<Origin>> originHolder = OriginsAPI.getOriginsRegistry().getHolder(origin);
        if (layerHolder.isEmpty() || !((Holder.Reference) layerHolder.get()).isBound()) {
            Origins.LOGGER.error("Tried to assign missing layer {} to player {}", layer, this.player.getScoreboardName());
        } else if (!originHolder.isEmpty() && ((Holder.Reference) originHolder.get()).isBound()) {
            ResourceKey<Origin> previous = (ResourceKey<Origin>) this.layers.put(layer, origin);
            if (!Objects.equals(origin, previous) || !handlePowers) {
                if (handlePowers) {
                    IPowerContainer.get(this.player).ifPresent(container -> {
                        this.grantPowers(container, origin, (Holder<Origin>) originHolder.get());
                        if (previous != null) {
                            container.removeAllPowersFromSource(OriginsAPI.getPowerSource(previous));
                        }
                    });
                    if (this.hasAllOrigins()) {
                        this.hadAllOrigins.set(true);
                    }
                    if (this.player instanceof ServerPlayer sp) {
                        ChoseOriginCriterion.INSTANCE.trigger(sp, origin);
                    }
                }
                this.synchronize();
            }
        } else {
            Origins.LOGGER.error("Tried to assign missing origin {} to player {}", origin, this.player.getScoreboardName());
        }
    }

    private void grantPowers(IPowerContainer container, @NotNull ResourceKey<Origin> origin, Holder<Origin> holder) {
        ResourceLocation powerSource = OriginsAPI.getPowerSource(origin);
        Registry<ConfiguredPower<?, ?>> powers = ApoliAPI.getPowers(this.player.m_20194_());
        for (HolderSet<ConfiguredPower<?, ?>> holderSet : holder.value().getPowers()) {
            for (Holder<ConfiguredPower<?, ?>> power : holderSet) {
                if (power.isBound()) {
                    ((Optional) power.unwrap().map(Optional::of, powers::m_7854_)).ifPresent(powerKey -> {
                        if (!container.hasPower(powerKey, powerSource)) {
                            container.addPower(powerKey, powerSource);
                        }
                    });
                }
            }
        }
    }

    @NotNull
    @Override
    public ResourceKey<Origin> getOrigin(@NotNull ResourceKey<OriginLayer> layer) {
        return (ResourceKey<Origin>) this.layers.getOrDefault(layer, OriginRegisters.EMPTY.getKey());
    }

    @Override
    public boolean hasOrigin(@NotNull ResourceKey<OriginLayer> layer) {
        return !Objects.equals(this.getOrigin(layer), OriginRegisters.EMPTY.getKey());
    }

    @Override
    public boolean hadAllOrigins() {
        return this.hadAllOrigins.get();
    }

    @NotNull
    @Override
    public Map<ResourceKey<OriginLayer>, ResourceKey<Origin>> getOrigins() {
        return ImmutableMap.copyOf(this.layers);
    }

    @Override
    public void synchronize() {
        this.synchronization.compareAndSet(false, true);
    }

    @Override
    public boolean shouldSync() {
        return this.synchronization.get();
    }

    @NotNull
    @Override
    public Player getOwner() {
        return this.player;
    }

    @Override
    public void tick() {
        if (this.cleanupPowers) {
            this.cleanupPowers = false;
            IPowerContainer.get(this.player).ifPresent(this::applyCleanup);
        }
        if (this.shouldSync() && !this.player.m_9236_().isClientSide() && this.syncCooldown.decrementAndGet() <= 0) {
            OriginsCommon.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> this.player), this.getSynchronizationPacket());
            this.syncCooldown.set(20);
            ApoliAPI.synchronizePowerContainer(this.player);
        }
    }

    private void applyCleanup(@NotNull IPowerContainer container) {
        MappedRegistry<Origin> originsRegistry = OriginsAPI.getOriginsRegistry();
        MappedRegistry<OriginLayer> layersRegistry = OriginsAPI.getLayersRegistry();
        Iterator<Entry<ResourceKey<OriginLayer>, ResourceKey<Origin>>> iterator = this.layers.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<ResourceKey<OriginLayer>, ResourceKey<Origin>> entry = (Entry<ResourceKey<OriginLayer>, ResourceKey<Origin>>) iterator.next();
            ResourceKey<OriginLayer> layer = (ResourceKey<OriginLayer>) entry.getKey();
            ResourceKey<Origin> origin = (ResourceKey<Origin>) entry.getValue();
            ResourceLocation powerSource = OriginsAPI.getPowerSource(origin);
            if (!layersRegistry.containsKey(layer)) {
                iterator.remove();
                container.removeAllPowersFromSource(powerSource);
                Origins.LOGGER.debug("CLEANUP: Removed missing layer {} on player {}", layer, this.player.getScoreboardName());
            } else if (!originsRegistry.containsKey(origin)) {
                Origins.LOGGER.debug("CLEANUP: Removed missing origin {} on player {}", origin, this.player.getScoreboardName());
                container.removeAllPowersFromSource(powerSource);
                entry.setValue(OriginRegisters.EMPTY.getKey());
            } else {
                Set<ResourceKey<ConfiguredPower<?, ?>>> currentPowers = ImmutableSet.copyOf(container.getPowersFromSource(powerSource));
                Registry<ConfiguredPower<?, ?>> registry = ApoliAPI.getPowers(this.player.m_20194_());
                Holder<Origin> originHolder = originsRegistry.createRegistrationLookup().getOrThrow(origin);
                if (originHolder.isBound()) {
                    Set<ResourceKey<ConfiguredPower<?, ?>>> newPowers = (Set<ResourceKey<ConfiguredPower<?, ?>>>) originHolder.value().getValidPowers().flatMap(holder -> {
                        if (!holder.isBound()) {
                            return Stream.empty();
                        } else {
                            Optional<ResourceKey<ConfiguredPower<?, ?>>> key = (Optional<ResourceKey<ConfiguredPower<?, ?>>>) holder.unwrap().map(Optional::of, registry::m_7854_);
                            if (key.isEmpty()) {
                                return Stream.empty();
                            } else {
                                HashSet<ResourceKey<ConfiguredPower<?, ?>>> names = new HashSet();
                                names.add((ResourceKey) key.get());
                                names.addAll(((ConfiguredPower) holder.value()).getChildrenKeys());
                                return names.stream();
                            }
                        }
                    }).collect(ImmutableSet.toImmutableSet());
                    Set<ResourceKey<ConfiguredPower<?, ?>>> toRemove = (Set<ResourceKey<ConfiguredPower<?, ?>>>) currentPowers.stream().filter(x -> !newPowers.contains(x)).collect(Collectors.toSet());
                    Set<ResourceKey<ConfiguredPower<?, ?>>> toAdd = (Set<ResourceKey<ConfiguredPower<?, ?>>>) newPowers.stream().filter(x -> !currentPowers.contains(x)).collect(Collectors.toSet());
                    if (!toRemove.isEmpty()) {
                        toRemove.forEach(power -> container.removePower(power, powerSource));
                        Origins.LOGGER.debug("CLEANUP: Revoked {} removed powers for origin {} on player {}", toRemove.size(), origin, this.player.getScoreboardName());
                    }
                    if (!toAdd.isEmpty()) {
                        toAdd.forEach(power -> container.addPower(power, powerSource));
                        Origins.LOGGER.debug("CLEANUP: Granted {} missing powers for origin {} on player {}", toAdd.size(), origin, this.player.getScoreboardName());
                    }
                }
            }
        }
    }

    @NotNull
    @Override
    public S2CSynchronizeOrigin getSynchronizationPacket() {
        return new S2CSynchronizeOrigin(this.player.m_19879_(), this.getLayerMap(), this.hadAllOrigins());
    }

    @NotNull
    private Map<ResourceLocation, ResourceLocation> getLayerMap() {
        Builder<ResourceLocation, ResourceLocation> builder = ImmutableMap.builder();
        this.layers.forEach((layer, origin) -> builder.put(layer.location(), origin.location()));
        return builder.build();
    }

    @Override
    public boolean checkAutoChoosingLayers(boolean includeDefaults) {
        boolean choseOneAutomatically = false;
        for (Holder.Reference<OriginLayer> layer : OriginsAPI.getActiveLayers()) {
            boolean shouldContinue = false;
            if (!this.hasOrigin(layer.key())) {
                if (includeDefaults && layer.value().hasDefaultOrigin() && !layer.value().defaultOrigin().is(OriginRegisters.EMPTY.getId())) {
                    this.setOrigin(layer, layer.value().defaultOrigin());
                    choseOneAutomatically = true;
                    shouldContinue = true;
                } else {
                    Optional<Holder<Origin>> automaticOrigin = layer.value().getAutomaticOrigin(this.player);
                    if (automaticOrigin.isPresent()) {
                        this.setOrigin(layer, (Holder<Origin>) automaticOrigin.get());
                        choseOneAutomatically = true;
                        shouldContinue = true;
                    } else if (layer.value().getOriginOptionCount(this.player) == 0) {
                        shouldContinue = true;
                    }
                }
            } else {
                shouldContinue = true;
            }
            if (!shouldContinue) {
                break;
            }
        }
        return choseOneAutomatically;
    }

    @Override
    public void onChosen(@NotNull ResourceKey<Origin> origin, boolean isOrb) {
        Set<ResourceKey<ConfiguredPower<?, ?>>> set = Sets.newHashSet();
        IPowerContainer.get(this.player).ifPresent(container -> container.getPowersFromSource(OriginsAPI.getPowerSource(origin)).stream().map(container::getPower).filter(Objects::nonNull).forEach(power -> {
            if (power.isBound() && ((ConfiguredPower) power.value()).getFactory() instanceof IOriginCallbackPower callbackPower) {
                if (!callbackPower.isReady((ConfiguredPower) power.value(), this.player, isOrb)) {
                    callbackPower.prepare((ConfiguredPower) power.value(), this.player, isOrb);
                    power.unwrapKey().ifPresent(k -> set.add(k));
                } else {
                    callbackPower.onChosen((ConfiguredPower) power.value(), this.player, isOrb);
                }
            }
        }));
        if (!set.isEmpty() && !this.player.m_9236_().isClientSide()) {
            OriginsCommon.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) this.player), new S2COpenWaitingForPowersScreen(isOrb, set));
        }
    }

    @Override
    public void onChosen(boolean isOrb) {
        Set<ResourceKey<ConfiguredPower<?, ?>>> set = Sets.newHashSet();
        IPowerContainer.get(this.player).ifPresent(container -> container.getPowers().forEach(x -> {
            if (x.isBound() && ((ConfiguredPower) x.value()).getFactory() instanceof IOriginCallbackPower callbackPower) {
                if (!callbackPower.isReady((ConfiguredPower) x.value(), this.player, isOrb)) {
                    callbackPower.prepare((ConfiguredPower) x.value(), this.player, isOrb);
                    x.unwrapKey().ifPresent(set::add);
                } else {
                    callbackPower.onChosen((ConfiguredPower) x.value(), this.player, isOrb);
                }
            }
        }));
        if (!set.isEmpty() && !this.player.m_9236_().isClientSide()) {
            OriginsCommon.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) this.player), new S2COpenWaitingForPowersScreen(isOrb, set));
        }
    }

    @Override
    public void onReload(@NotNull ICalioDynamicRegistryManager registry) {
        this.cleanupPowers = true;
    }

    @Deprecated
    @NotNull
    @Override
    public OriginComponent asLegacyComponent() {
        return (OriginComponent) this.component.get();
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return OriginsAPI.ORIGIN_CONTAINER.orEmpty(cap, this.thisOptional);
    }

    public void acceptSynchronization(Map<ResourceLocation, ResourceLocation> map, boolean hadAllOrigins) {
        this.layers.clear();
        Registry<OriginLayer> layers = OriginsAPI.getLayersRegistry(this.player.m_20194_());
        Registry<Origin> origins = OriginsAPI.getOriginsRegistry(this.player.m_20194_());
        for (Entry<ResourceLocation, ResourceLocation> entry : map.entrySet()) {
            ResourceKey<OriginLayer> layer = ResourceKey.create(OriginsDynamicRegistries.LAYERS_REGISTRY, (ResourceLocation) entry.getKey());
            ResourceKey<Origin> origin = ResourceKey.create(OriginsDynamicRegistries.ORIGINS_REGISTRY, (ResourceLocation) entry.getValue());
            if (layers.containsKey(layer) && origins.containsKey(origin)) {
                this.layers.put(layer, origin);
            }
        }
        this.hadAllOrigins.set(hadAllOrigins);
    }

    @Override
    public Tag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        CompoundTag layers = new CompoundTag();
        Registry<Origin> originsRegistry = OriginsAPI.getOriginsRegistry(this.player.m_20194_());
        Registry<OriginLayer> layersRegistry = OriginsAPI.getLayersRegistry(this.player.m_20194_());
        for (Entry<ResourceKey<OriginLayer>, ResourceKey<Origin>> entry : this.getOrigins().entrySet()) {
            if (layersRegistry.containsKey((ResourceKey<OriginLayer>) entry.getKey()) && originsRegistry.containsKey((ResourceKey<Origin>) entry.getValue())) {
                layers.putString(((ResourceKey) entry.getKey()).location().toString(), ((ResourceKey) entry.getValue()).location().toString());
            } else {
                Origins.LOGGER.warn("Removed missing entry {}: {}", entry.getKey(), entry.getValue());
            }
        }
        tag.put("Origins", layers);
        tag.putBoolean("HadAllOrigins", this.hasAllOrigins());
        return tag;
    }

    @Override
    public void deserializeNBT(Tag nbt) {
        this.layers.clear();
        CompoundTag tag = (CompoundTag) nbt;
        CompoundTag layers = tag.getCompound("Origins");
        Registry<OriginLayer> layersRegistry = OriginsAPI.getLayersRegistry(this.player.m_20194_());
        Registry<Origin> originsRegistry = OriginsAPI.getOriginsRegistry(this.player.m_20194_());
        for (String key : layers.getAllKeys()) {
            String origin = layers.getString(key);
            if (!origin.isBlank()) {
                ResourceLocation orig = ResourceLocation.tryParse(origin);
                if (orig == null) {
                    Origins.LOGGER.warn("Invalid origin {} found for layer {} on entity {}", origin, key, this.player.getScoreboardName());
                } else {
                    ResourceKey<Origin> originKey = ResourceKey.create(OriginsDynamicRegistries.ORIGINS_REGISTRY, orig);
                    Optional<Holder.Reference<Origin>> origin1 = originsRegistry.getHolder(originKey);
                    if (!origin1.isEmpty() && ((Holder.Reference) origin1.get()).isBound()) {
                        ResourceLocation rl = ResourceLocation.tryParse(key);
                        if (rl == null) {
                            Origins.LOGGER.warn("Invalid layer found {} on entity {}", key, this.player.getScoreboardName());
                            IPowerContainer.get(this.player).ifPresent(container -> container.removeAllPowersFromSource(OriginsAPI.getPowerSource((Holder<Origin>) origin1.get())));
                        } else {
                            ResourceKey<OriginLayer> layerKey = ResourceKey.create(OriginsDynamicRegistries.LAYERS_REGISTRY, rl);
                            Optional<Holder.Reference<OriginLayer>> layer = layersRegistry.getHolder(layerKey);
                            if (!layer.isEmpty() && ((Holder.Reference) layer.get()).isBound()) {
                                this.setOriginInternal(layerKey, originKey, false);
                            } else {
                                Origins.LOGGER.warn("Missing layer {} on entity {}", rl, this.player.getScoreboardName());
                                IPowerContainer.get(this.player).ifPresent(container -> container.removeAllPowersFromSource(OriginsAPI.getPowerSource((Holder<Origin>) origin1.get())));
                            }
                        }
                    } else {
                        Origins.LOGGER.warn("Missing origin {} found for layer {} on entity {}", origin, key, this.player.getScoreboardName());
                        IPowerContainer.get(this.player).ifPresent(container -> container.removeAllPowersFromSource(OriginsAPI.getPowerSource(orig)));
                    }
                }
            }
        }
        this.hadAllOrigins.set(tag.getBoolean("HadAllOrigins"));
    }

    @Override
    public void validateSynchronization() {
        this.synchronization.compareAndSet(true, false);
        this.syncCooldown.set(0);
    }
}