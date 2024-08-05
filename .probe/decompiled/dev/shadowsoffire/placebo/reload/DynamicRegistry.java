package dev.shadowsoffire.placebo.reload;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import dev.shadowsoffire.placebo.Placebo;
import dev.shadowsoffire.placebo.codec.CodecMap;
import dev.shadowsoffire.placebo.codec.CodecProvider;
import dev.shadowsoffire.placebo.json.JsonUtil;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus.Internal;

public abstract class DynamicRegistry<R extends CodecProvider<? super R>> extends SimpleJsonResourceReloadListener {

    protected final Logger logger;

    protected final String path;

    protected final boolean synced;

    protected final boolean subtypes;

    protected final CodecMap<R> codecs;

    protected final Codec<DynamicHolder<R>> holderCodec;

    protected BiMap<ResourceLocation, R> registry = ImmutableBiMap.of();

    private final Map<ResourceLocation, R> staged = new HashMap();

    private final Map<ResourceLocation, DynamicHolder<? extends R>> holders = new ConcurrentHashMap();

    private final Set<RegistryCallback<R>> callbacks = new HashSet();

    private WeakReference<ICondition.IContext> context;

    public DynamicRegistry(Logger logger, String path, boolean synced, boolean subtypes) {
        super(new GsonBuilder().setLenient().create(), path);
        this.logger = logger;
        this.path = path;
        this.synced = synced;
        this.subtypes = subtypes;
        this.codecs = new CodecMap<>(path);
        this.registerBuiltinCodecs();
        if (this.codecs.isEmpty()) {
            throw new RuntimeException("Attempted to create a dynamic registry for " + path + " with no built-in codecs!");
        } else {
            this.holderCodec = ResourceLocation.CODEC.xmap(this::holder, DynamicHolder::getId);
        }
    }

    protected final void apply(Map<ResourceLocation, JsonElement> objects, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        this.beginReload();
        objects.forEach((key, ele) -> {
            try {
                if (JsonUtil.checkAndLogEmpty(ele, key, this.path, this.logger) && JsonUtil.checkConditions(ele, key, this.path, this.logger, this.getContext())) {
                    JsonObject obj = ele.getAsJsonObject();
                    R deserialized = (R) ((Pair) this.codecs.decode(JsonOps.INSTANCE, obj).getOrThrow(false, this::logCodecError)).getFirst();
                    Preconditions.checkNotNull(deserialized.getCodec(), "A " + this.path + " with id " + key + " is not declaring a codec.");
                    Preconditions.checkNotNull(this.codecs.getKey(deserialized.getCodec()), "A " + this.path + " with id " + key + " is declaring an unregistered codec.");
                    this.register(key, deserialized);
                }
            } catch (Exception var5) {
                this.logger.error("Failed parsing {} file {}.", this.path, key);
                this.logger.error("Underlying Exception: ", var5);
            }
        });
        this.onReload();
    }

    protected abstract void registerBuiltinCodecs();

    protected void beginReload() {
        this.callbacks.forEach(l -> l.beginReload(this));
        this.registry = HashBiMap.create();
        this.holders.values().forEach(DynamicHolder::unbind);
    }

    protected void onReload() {
        this.registry = ImmutableBiMap.copyOf(this.registry);
        this.logger.info("Registered {} {}.", this.registry.size(), this.path);
        this.callbacks.forEach(l -> l.onReload(this));
        this.holders.values().forEach(DynamicHolder::bind);
    }

    public Set<ResourceLocation> getKeys() {
        return this.registry.keySet();
    }

    public Collection<R> getValues() {
        return this.registry.values();
    }

    @Nullable
    public R getValue(ResourceLocation key) {
        return (R) this.registry.get(key);
    }

    @Nullable
    public ResourceLocation getKey(R value) {
        return (ResourceLocation) this.registry.inverse().get(value);
    }

    public R getOrDefault(ResourceLocation key, R defValue) {
        return (R) this.registry.getOrDefault(key, defValue);
    }

    public void registerToBus() {
        if (this.synced) {
            DynamicRegistry.SyncManagement.registerForSync(this);
        }
        MinecraftForge.EVENT_BUS.addListener(this::addReloader);
    }

    public <T extends R> DynamicHolder<T> holder(ResourceLocation id) {
        return (DynamicHolder<T>) this.holders.computeIfAbsent(id, k -> new DynamicHolder<>(this, k));
    }

    public <T extends R> DynamicHolder<T> holder(T t) {
        ResourceLocation key = this.getKey((R) t);
        return this.holder(key == null ? DynamicHolder.EMPTY : key);
    }

    public DynamicHolder<R> emptyHolder() {
        return this.holder(DynamicHolder.EMPTY);
    }

    public Codec<DynamicHolder<R>> holderCodec() {
        return this.holderCodec;
    }

    public final void registerCodec(ResourceLocation key, Codec<? extends R> codec) {
        if (!this.subtypes) {
            throw new UnsupportedOperationException("Attempted to call registerCodec on a registry which does not support subtypes.");
        } else {
            this.codecs.register(key, codec);
        }
    }

    protected final void registerDefaultCodec(ResourceLocation key, Codec<? extends R> codec) {
        if (this.codecs.getDefaultCodec() != null) {
            throw new UnsupportedOperationException("Attempted to register a second " + this.path + " default codec with key " + key + " but subtypes are not supported!");
        } else {
            this.codecs.register(key, codec);
            this.codecs.setDefaultCodec(codec);
        }
    }

    public final boolean addCallback(RegistryCallback<R> callback) {
        return this.callbacks.add(callback);
    }

    public final boolean removeCallback(RegistryCallback<R> callback) {
        return this.callbacks.remove(callback);
    }

    protected final void register(ResourceLocation key, R value) {
        if (this.registry.containsKey(key)) {
            throw new UnsupportedOperationException("Attempted to register a " + this.path + " with a duplicate registry ID! Key: " + key);
        } else {
            this.validateItem(key, value);
            this.registry.put(key, value);
            this.holders.computeIfAbsent(key, k -> new DynamicHolder<>(this, k));
        }
    }

    protected void validateItem(ResourceLocation key, R value) {
    }

    protected final ICondition.IContext getContext() {
        return this.context.get() != null ? (ICondition.IContext) this.context.get() : ICondition.IContext.EMPTY;
    }

    private void addReloader(AddReloadListenerEvent e) {
        e.addListener(this);
        this.context = new WeakReference(e.getConditionContext());
    }

    private void pushStagedToLive() {
        this.beginReload();
        this.staged.forEach(this::register);
        this.onReload();
    }

    private void logCodecError(String msg) {
        Placebo.LOGGER.error("Codec failure for type {}, message: {}", this.path, msg);
    }

    private void sync(OnDatapackSyncEvent e) {
        ServerPlayer player = e.getPlayer();
        PacketDistributor.PacketTarget target = player == null ? PacketDistributor.ALL.noArg() : PacketDistributor.PLAYER.with(() -> player);
        Placebo.CHANNEL.send(target, new ReloadListenerPacket.Start(this.path));
        this.registry.forEach((k, v) -> Placebo.CHANNEL.send(target, new ReloadListenerPacket.Content(this.path, k, v)));
        Placebo.CHANNEL.send(target, new ReloadListenerPacket.End(this.path));
    }

    @Internal
    static class SyncManagement {

        private static final Map<String, DynamicRegistry<?>> SYNC_REGISTRY = new LinkedHashMap();

        static void registerForSync(DynamicRegistry<?> listener) {
            if (!listener.synced) {
                throw new UnsupportedOperationException("Attempted to register the non-synced JSON Reload Listener " + listener.path + " as a synced listener!");
            } else {
                synchronized (SYNC_REGISTRY) {
                    if (SYNC_REGISTRY.containsKey(listener.path)) {
                        throw new UnsupportedOperationException("Attempted to register the JSON Reload Listener for syncing " + listener.path + " but one already exists!");
                    } else {
                        if (SYNC_REGISTRY.isEmpty()) {
                            MinecraftForge.EVENT_BUS.addListener(DynamicRegistry.SyncManagement::syncAll);
                        }
                        SYNC_REGISTRY.put(listener.path, listener);
                    }
                }
            }
        }

        static void initSync(String path) {
            ifPresent(path, registry -> registry.staged.clear());
            Placebo.LOGGER.info("Starting sync for {}", path);
        }

        static <V extends CodecProvider<? super V>> void writeItem(String path, V value, FriendlyByteBuf buf) {
            ifPresent(path, registry -> {
                Codec<V> c = registry.codecs;
                buf.writeNbt((CompoundTag) c.encodeStart(NbtOps.INSTANCE, value).getOrThrow(false, registry::logCodecError));
            });
        }

        static <V> V readItem(String path, FriendlyByteBuf buf) {
            DynamicRegistry<? extends CodecProvider<?>> registry = (DynamicRegistry<? extends CodecProvider<?>>) SYNC_REGISTRY.get(path);
            if (registry == null) {
                throw new RuntimeException("Received sync packet for unknown registry!");
            } else {
                Codec<V> c = (Codec<V>) registry.codecs;
                return (V) ((Pair) c.decode(NbtOps.INSTANCE, buf.readNbt()).getOrThrow(false, registry::logCodecError)).getFirst();
            }
        }

        static <V> void acceptItem(String path, ResourceLocation key, V value) {
            ifPresent(path, registry -> registry.staged.put(key, value));
        }

        static void endSync(String path) {
            if (ServerLifecycleHooks.getCurrentServer() == null) {
                ifPresent(path, DynamicRegistry::pushStagedToLive);
            }
        }

        private static void ifPresent(String path, Consumer<DynamicRegistry<?>> consumer) {
            DynamicRegistry<?> value = (DynamicRegistry<?>) SYNC_REGISTRY.get(path);
            if (value != null) {
                consumer.accept(value);
            }
        }

        private static void syncAll(OnDatapackSyncEvent e) {
            SYNC_REGISTRY.values().forEach(r -> r.sync(e));
        }
    }
}