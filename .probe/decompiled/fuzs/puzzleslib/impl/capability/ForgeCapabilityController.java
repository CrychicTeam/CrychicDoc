package fuzs.puzzleslib.impl.capability;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import fuzs.puzzleslib.api.capability.v2.CapabilityController;
import fuzs.puzzleslib.api.capability.v2.data.CapabilityComponent;
import fuzs.puzzleslib.api.capability.v2.data.CapabilityKey;
import fuzs.puzzleslib.api.capability.v2.data.PlayerCapabilityKey;
import fuzs.puzzleslib.api.capability.v2.data.PlayerRespawnCopyStrategy;
import fuzs.puzzleslib.api.capability.v2.data.SyncStrategy;
import fuzs.puzzleslib.api.core.v1.ModContainerHelper;
import fuzs.puzzleslib.impl.capability.data.CapabilityHolder;
import fuzs.puzzleslib.impl.capability.data.ForgeCapabilityKey;
import fuzs.puzzleslib.impl.capability.data.ForgePlayerCapabilityKey;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.Nullable;

public final class ForgeCapabilityController implements CapabilityController {

    private final String namespace;

    private final Multimap<Class<?>, ForgeCapabilityController.CapabilityData<?, ?>> capabilityTypes = Multimaps.newListMultimap(Maps.newIdentityHashMap(), Lists::newArrayList);

    public ForgeCapabilityController(String namespace) {
        this.namespace = namespace;
        ModContainerHelper.getOptionalModEventBus(namespace).ifPresent(eventBus -> eventBus.addListener(this::onRegisterCapabilities));
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public <T extends Entity, C extends CapabilityComponent> CapabilityKey<C> registerEntityCapability(String capabilityKey, Class<C> capabilityType, Function<T, C> capabilityFactory, Class<T> entityType) {
        return this.registerCapability(Entity.class, capabilityKey, capabilityType, capabilityFactory, entityType::isInstance);
    }

    @Override
    public <C extends CapabilityComponent> PlayerCapabilityKey<C> registerPlayerCapability(String capabilityKey, Class<C> capabilityType, Function<Player, C> capabilityFactory, PlayerRespawnCopyStrategy respawnStrategy) {
        return this.registerCapability(Entity.class, capabilityKey, capabilityType, capabilityFactory, Player.class::isInstance, ForgePlayerCapabilityKey::new).setRespawnStrategy(respawnStrategy);
    }

    @Override
    public <C extends CapabilityComponent> PlayerCapabilityKey<C> registerPlayerCapability(String capabilityKey, Class<C> capabilityType, Function<Player, C> capabilityFactory, PlayerRespawnCopyStrategy respawnStrategy, SyncStrategy syncStrategy) {
        return ((ForgePlayerCapabilityKey) this.<C>registerPlayerCapability(capabilityKey, capabilityType, capabilityFactory, respawnStrategy)).setSyncStrategy(syncStrategy);
    }

    @Override
    public <T extends BlockEntity, C extends CapabilityComponent> CapabilityKey<C> registerBlockEntityCapability(String capabilityKey, Class<C> capabilityType, Function<T, C> capabilityFactory, Class<T> blockEntityType) {
        return this.registerCapability(BlockEntity.class, capabilityKey, capabilityType, capabilityFactory, blockEntityType::isInstance);
    }

    @Override
    public <C extends CapabilityComponent> CapabilityKey<C> registerLevelChunkCapability(String capabilityKey, Class<C> capabilityType, Function<ChunkAccess, C> capabilityFactory) {
        return this.registerCapability(LevelChunk.class, capabilityKey, capabilityType, capabilityFactory, o -> true);
    }

    @Override
    public <C extends CapabilityComponent> CapabilityKey<C> registerLevelCapability(String capabilityKey, Class<C> capabilityType, Function<Level, C> capabilityFactory) {
        return this.registerCapability(Level.class, capabilityKey, capabilityType, capabilityFactory, o -> true);
    }

    private <T, C extends CapabilityComponent> CapabilityKey<C> registerCapability(Class<? extends ICapabilityProvider> providerType, String capabilityKey, Class<C> capabilityType, Function<T, C> capabilityFactory, Predicate<Object> filter) {
        return this.registerCapability(providerType, capabilityKey, capabilityType, capabilityFactory, filter, ForgeCapabilityKey::new);
    }

    private <T, C1 extends CapabilityComponent, C2 extends CapabilityKey<C1>> C2 registerCapability(Class<? extends ICapabilityProvider> providerType, String capabilityKey, Class<C1> capabilityType, Function<T, C1> capabilityFactory, Predicate<Object> filter, ForgeCapabilityKey.ForgeCapabilityKeyFactory<C1, C2> capabilityKeyFactory) {
        if (!VALID_CAPABILITY_TYPES.contains(providerType)) {
            throw new IllegalArgumentException(providerType + " is an invalid type");
        } else {
            ResourceLocation key = new ResourceLocation(this.namespace, capabilityKey);
            ForgeCapabilityController.CapabilityData<T, C1> capabilityData = new ForgeCapabilityController.CapabilityData<>(key, capabilityType, filter);
            this.capabilityTypes.put(providerType, capabilityData);
            return capabilityKeyFactory.apply(key, capabilityType, token -> {
                Capability<C1> capability = CapabilityManager.get(token);
                capabilityData.setFactory(o -> new CapabilityHolder<>(capability, (C1) capabilityFactory.apply(o)));
                return capability;
            });
        }
    }

    private void onRegisterCapabilities(RegisterCapabilitiesEvent evt) {
        for (ForgeCapabilityController.CapabilityData<?, ?> data : this.capabilityTypes.values()) {
            evt.register(data.getType());
        }
    }

    @SubscribeEvent
    public <T> void onAttachCapabilities(AttachCapabilitiesEvent<?> evt) {
        Class<?> providerClazz = (Class<?>) evt.getGenericType();
        for (ForgeCapabilityController.CapabilityData<?, ?> data : this.capabilityTypes.get(providerClazz)) {
            if (data.test(evt.getObject())) {
                evt.addCapability(data.getKey(), ((ForgeCapabilityController.CapabilityData<Object, ?>) data).make(evt.getObject()));
            }
        }
    }

    private static final class CapabilityData<T, C extends CapabilityComponent> {

        private final ResourceLocation key;

        private final Class<C> type;

        private final Predicate<Object> filter;

        @Nullable
        private Function<T, CapabilityHolder<C>> factory;

        private CapabilityData(ResourceLocation key, Class<C> type, Predicate<Object> filter) {
            this.key = key;
            this.type = type;
            this.filter = filter;
        }

        public ResourceLocation getKey() {
            return this.key;
        }

        public Class<C> getType() {
            return this.type;
        }

        public void setFactory(Function<T, CapabilityHolder<C>> factory) {
            Preconditions.checkState(this.factory == null, "Capability factory for %s already set".formatted(this.key));
            this.factory = factory;
        }

        public CapabilityHolder<C> make(T t) {
            Objects.requireNonNull(this.factory, "Found no capability factory for " + this.key);
            return (CapabilityHolder<C>) this.factory.apply(t);
        }

        public boolean test(Object o) {
            return this.filter.test(o);
        }
    }
}