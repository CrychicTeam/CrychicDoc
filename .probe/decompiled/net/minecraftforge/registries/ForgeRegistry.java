package net.minecraftforge.registries;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Multimap;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntRBTreeSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntRBTreeMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraftforge.common.util.LogMessageAdapter;
import net.minecraftforge.common.util.TablePrinter;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.registries.tags.ITagManager;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public class ForgeRegistry<V> implements IForgeRegistryInternal<V>, IForgeRegistryModifiable<V> {

    public static final Marker REGISTRIES = MarkerManager.getMarker("REGISTRIES");

    private static final Marker REGISTRYDUMP = MarkerManager.getMarker("REGISTRYDUMP");

    private static final Logger LOGGER = LogManager.getLogger();

    private final RegistryManager stage;

    private final BiMap<Integer, V> ids = HashBiMap.create();

    private final BiMap<ResourceLocation, V> names = HashBiMap.create();

    private final BiMap<ResourceKey<V>, V> keys = HashBiMap.create();

    private final Map<ResourceLocation, ResourceLocation> aliases = new HashMap();

    final Map<ResourceLocation, ?> slaves = new HashMap();

    private final ResourceLocation defaultKey;

    private final IForgeRegistry.CreateCallback<V> create;

    private final IForgeRegistry.AddCallback<V> add;

    private final IForgeRegistry.ClearCallback<V> clear;

    private final IForgeRegistry.ValidateCallback<V> validate;

    private final IForgeRegistry.BakeCallback<V> bake;

    private final IForgeRegistry.MissingFactory<V> missing;

    private final BitSet availabilityMap;

    private final IntSet blocked = new IntOpenHashSet();

    private final Multimap<ResourceLocation, V> overrides = ArrayListMultimap.create();

    private final Map<ResourceLocation, Holder.Reference<V>> delegatesByName = new HashMap();

    private final Map<V, Holder.Reference<V>> delegatesByValue = new HashMap();

    private final BiMap<ForgeRegistry.OverrideOwner<V>, V> owners = HashBiMap.create();

    private final ForgeRegistryTagManager<V> tagManager;

    private final int min;

    private final int max;

    private final boolean allowOverrides;

    private final boolean isModifiable;

    private final boolean hasWrapper;

    private V defaultValue = (V) null;

    boolean isFrozen = false;

    private final ResourceLocation name;

    private final ResourceKey<Registry<V>> key;

    private final RegistryBuilder<V> builder;

    private final Codec<V> codec = new ForgeRegistry.RegistryCodec();

    ForgeRegistry(RegistryManager stage, ResourceLocation name, RegistryBuilder<V> builder) {
        this.name = name;
        this.key = ResourceKey.createRegistryKey(name);
        this.builder = builder;
        this.stage = stage;
        this.defaultKey = builder.getDefault();
        this.min = builder.getMinId();
        this.max = builder.getMaxId();
        this.availabilityMap = new BitSet(Math.min(this.max + 1, 4095));
        this.create = builder.getCreate();
        this.add = builder.getAdd();
        this.clear = builder.getClear();
        this.validate = builder.getValidate();
        this.bake = builder.getBake();
        this.missing = builder.getMissingFactory();
        this.allowOverrides = builder.getAllowOverrides();
        this.isModifiable = builder.getAllowModifications();
        this.hasWrapper = builder.getHasWrapper();
        this.tagManager = this.hasWrapper ? new ForgeRegistryTagManager<>(this) : null;
        if (this.create != null) {
            this.create.onCreate(this, stage);
        }
    }

    @Override
    public void register(String key, V value) {
        this.register(GameData.checkPrefix(key, true), value);
    }

    @Override
    public void register(ResourceLocation key, V value) {
        this.add(-1, key, value);
    }

    public Iterator<V> iterator() {
        return new Iterator<V>() {

            int cur = -1;

            V next = (V) null;

            {
                this.next();
            }

            public boolean hasNext() {
                return this.next != null;
            }

            public V next() {
                V ret = this.next;
                do {
                    this.cur = ForgeRegistry.this.availabilityMap.nextSetBit(this.cur + 1);
                    this.next = (V) ForgeRegistry.this.ids.get(this.cur);
                } while (this.next == null && this.cur != -1);
                return ret;
            }
        };
    }

    @Override
    public ResourceLocation getRegistryName() {
        return this.name;
    }

    @Override
    public ResourceKey<Registry<V>> getRegistryKey() {
        return this.key;
    }

    @NotNull
    @Override
    public Codec<V> getCodec() {
        return this.codec;
    }

    @Override
    public boolean containsKey(ResourceLocation key) {
        while (key != null) {
            if (this.names.containsKey(key)) {
                return true;
            }
            key = (ResourceLocation) this.aliases.get(key);
        }
        return false;
    }

    @Override
    public boolean containsValue(V value) {
        return this.names.containsValue(value);
    }

    @Override
    public boolean isEmpty() {
        return this.names.isEmpty();
    }

    int size() {
        return this.names.size();
    }

    @Override
    public V getValue(ResourceLocation key) {
        V ret = (V) this.names.get(key);
        for (ResourceLocation var3 = (ResourceLocation) this.aliases.get(key); ret == null && var3 != null; var3 = (ResourceLocation) this.aliases.get(var3)) {
            ret = (V) this.names.get(var3);
        }
        return ret == null ? this.defaultValue : ret;
    }

    @Override
    public ResourceLocation getKey(V value) {
        return (ResourceLocation) this.getResourceKey(value).map(ResourceKey::m_135782_).orElse(this.defaultKey);
    }

    @NotNull
    @Override
    public Optional<ResourceKey<V>> getResourceKey(V value) {
        return Optional.ofNullable((ForgeRegistry.OverrideOwner) this.owners.inverse().get(value)).map(ForgeRegistry.OverrideOwner::key);
    }

    @Nullable
    NamespacedWrapper<V> getWrapper() {
        if (!this.hasWrapper) {
            return null;
        } else {
            return this.defaultKey != null ? this.getSlaveMap(NamespacedDefaultedWrapper.Factory.ID, NamespacedDefaultedWrapper.class) : this.getSlaveMap(NamespacedWrapper.Factory.ID, NamespacedWrapper.class);
        }
    }

    @NotNull
    NamespacedWrapper<V> getWrapperOrThrow() {
        NamespacedWrapper<V> wrapper = this.getWrapper();
        if (wrapper == null) {
            throw new IllegalStateException("Cannot query wrapper for non-wrapped forge registry!");
        } else {
            return wrapper;
        }
    }

    void onBindTags(Map<TagKey<V>, HolderSet.Named<V>> tags, Set<TagKey<V>> defaultedTags) {
        if (this.tagManager != null) {
            this.tagManager.bind(tags, defaultedTags);
        }
    }

    @NotNull
    @Override
    public Optional<Holder<V>> getHolder(ResourceKey<V> key) {
        return Optional.ofNullable(this.getWrapper()).flatMap(wrapper -> wrapper.getHolder(key));
    }

    @NotNull
    @Override
    public Optional<Holder<V>> getHolder(ResourceLocation location) {
        return Optional.ofNullable(this.getWrapper()).flatMap(wrapper -> wrapper.getHolder(location));
    }

    @NotNull
    @Override
    public Optional<Holder<V>> getHolder(V value) {
        return Optional.ofNullable(this.getWrapper()).flatMap(wrapper -> wrapper.getHolder(value));
    }

    @Nullable
    @Override
    public ITagManager<V> tags() {
        return this.tagManager;
    }

    @NotNull
    @Override
    public Set<ResourceLocation> getKeys() {
        return Collections.unmodifiableSet(this.names.keySet());
    }

    @NotNull
    Set<ResourceKey<V>> getResourceKeys() {
        return Collections.unmodifiableSet(this.keys.keySet());
    }

    @NotNull
    @Override
    public Collection<V> getValues() {
        return Collections.unmodifiableSet(this.names.values());
    }

    @NotNull
    @Override
    public Set<Entry<ResourceKey<V>, V>> getEntries() {
        return Collections.unmodifiableSet(this.keys.entrySet());
    }

    @Override
    public <T> T getSlaveMap(ResourceLocation name, Class<T> type) {
        return (T) this.slaves.get(name);
    }

    @Override
    public void setSlaveMap(ResourceLocation name, Object obj) {
        this.slaves.put(name, obj);
    }

    public int getID(V value) {
        Integer ret = (Integer) this.ids.inverse().get(value);
        if (ret == null && this.defaultValue != null) {
            ret = (Integer) this.ids.inverse().get(this.defaultValue);
        }
        return ret == null ? -1 : ret;
    }

    public int getID(ResourceLocation name) {
        return this.getID((V) this.names.get(name));
    }

    private int getIDRaw(V value) {
        Integer ret = (Integer) this.ids.inverse().get(value);
        return ret == null ? -1 : ret;
    }

    private int getIDRaw(ResourceLocation name) {
        return this.getIDRaw((V) this.names.get(name));
    }

    @Override
    public V getValue(int id) {
        V ret = (V) this.ids.get(id);
        return ret == null ? this.defaultValue : ret;
    }

    @Nullable
    public ResourceKey<V> getKey(int id) {
        V value = this.getValue(id);
        return (ResourceKey<V>) this.keys.inverse().get(value);
    }

    void validateKey() {
        if (this.defaultKey != null) {
            Validate.notNull(this.defaultValue, "Missing default of ForgeRegistry: " + this.defaultKey + " Name: " + this.name, new Object[0]);
        }
    }

    @Nullable
    @Override
    public ResourceLocation getDefaultKey() {
        return this.defaultKey;
    }

    ForgeRegistry<V> copy(RegistryManager stage) {
        return new ForgeRegistry<>(stage, this.name, this.builder);
    }

    @Override
    public void register(int id, ResourceLocation key, V value) {
        this.add(id, key, value, key.getNamespace());
    }

    int add(int id, ResourceLocation key, V value) {
        String owner = ModLoadingContext.get().getActiveNamespace();
        return this.add(id, key, value, owner);
    }

    int add(int id, ResourceLocation key, V value, String owner) {
        Preconditions.checkNotNull(key, "Can't use a null-name for the registry, object %s.", value);
        Preconditions.checkNotNull(value, "Can't add null-object to the registry, name %s.", key);
        int idToUse = id;
        if (id < 0 || this.availabilityMap.get(id)) {
            idToUse = this.availabilityMap.nextClearBit(this.min);
        }
        if (idToUse > this.max) {
            throw new RuntimeException(String.format(Locale.ENGLISH, "Invalid id %d - maximum id range exceeded.", idToUse));
        } else {
            V oldEntry = this.getRaw(key);
            if (oldEntry == value) {
                LOGGER.warn(REGISTRIES, "Registry {}: The object {} has been registered twice for the same name {}.", this.name, value, key);
                return this.getID(value);
            } else {
                if (oldEntry != null) {
                    if (!this.allowOverrides) {
                        throw new IllegalArgumentException(String.format(Locale.ENGLISH, "The name %s has been registered twice, for %s and %s.", key, this.getRaw(key), value));
                    }
                    if (owner == null) {
                        throw new IllegalStateException(String.format(Locale.ENGLISH, "Could not determine owner for the override on %s. Value: %s", key, value));
                    }
                    LOGGER.debug(REGISTRIES, "Registry {} Override: {} {} -> {}", this.name, key, oldEntry, value);
                    idToUse = this.getID(oldEntry);
                }
                Integer foundId = (Integer) this.ids.inverse().get(value);
                if (foundId != null) {
                    V otherThing = (V) this.ids.get(foundId);
                    throw new IllegalArgumentException(String.format(Locale.ENGLISH, "The object %s{%x} has been registered twice, using the names %s and %s. (Other object at this id is %s{%x})", value, System.identityHashCode(value), this.getKey(value), key, otherThing, System.identityHashCode(otherThing)));
                } else if (this.isLocked()) {
                    throw new IllegalStateException(String.format(Locale.ENGLISH, "The object %s (name %s) is being added too late.", value, key));
                } else {
                    if (this.defaultKey != null && this.defaultKey.equals(key)) {
                        if (this.defaultValue != null) {
                            throw new IllegalStateException(String.format(Locale.ENGLISH, "Attemped to override already set default value. This is not allowed: The object %s (name %s)", value, key));
                        }
                        this.defaultValue = value;
                    }
                    ResourceKey<V> rkey = ResourceKey.create(this.key, key);
                    this.names.put(key, value);
                    this.keys.put(rkey, value);
                    this.ids.put(idToUse, value);
                    this.availabilityMap.set(idToUse);
                    this.owners.put(new ForgeRegistry.OverrideOwner<>(owner == null ? key.getNamespace() : owner, rkey), value);
                    if (this.hasWrapper) {
                        this.bindDelegate(rkey, value);
                        if (oldEntry != null) {
                            if (!this.overrides.get(key).contains(oldEntry)) {
                                this.overrides.put(key, oldEntry);
                            }
                            this.overrides.get(key).remove(value);
                        }
                    }
                    if (this.add != null) {
                        this.add.onAdd(this, this.stage, idToUse, rkey, value, oldEntry);
                    }
                    LOGGER.trace(REGISTRIES, "Registry {} add: {} {} {} (req. id {})", this.name, key, idToUse, value, id);
                    return idToUse;
                }
            }
        }
    }

    public V getRaw(ResourceLocation key) {
        V ret = (V) this.names.get(key);
        for (ResourceLocation var3 = (ResourceLocation) this.aliases.get(key); ret == null && var3 != null; var3 = (ResourceLocation) this.aliases.get(var3)) {
            ret = (V) this.names.get(var3);
        }
        return ret;
    }

    public void addAlias(ResourceLocation src, ResourceLocation dst) {
        if (this.isLocked()) {
            throw new IllegalStateException(String.format(Locale.ENGLISH, "Attempted to register the alias %s -> %s too late", src, dst));
        } else if (src.equals(dst)) {
            LOGGER.warn(REGISTRIES, "Registry {} Ignoring invalid alias: {} -> {}", this.name, src, dst);
        } else {
            this.aliases.put(src, dst);
            LOGGER.trace(REGISTRIES, "Registry {} alias: {} -> {}", this.name, src, dst);
        }
    }

    @NotNull
    @Override
    public Optional<Holder.Reference<V>> getDelegate(ResourceKey<V> rkey) {
        return Optional.ofNullable((Holder.Reference) this.delegatesByName.get(rkey.location()));
    }

    @NotNull
    @Override
    public Holder.Reference<V> getDelegateOrThrow(ResourceKey<V> rkey) {
        return (Holder.Reference<V>) this.getDelegate(rkey).orElseThrow(() -> new IllegalArgumentException(String.format(Locale.ENGLISH, "No delegate exists for key %s", rkey)));
    }

    @NotNull
    @Override
    public Optional<Holder.Reference<V>> getDelegate(ResourceLocation key) {
        return Optional.ofNullable((Holder.Reference) this.delegatesByName.get(key));
    }

    @NotNull
    @Override
    public Holder.Reference<V> getDelegateOrThrow(ResourceLocation key) {
        return (Holder.Reference<V>) this.getDelegate(key).orElseThrow(() -> new IllegalArgumentException(String.format(Locale.ENGLISH, "No delegate exists for key %s", key)));
    }

    @NotNull
    @Override
    public Optional<Holder.Reference<V>> getDelegate(V value) {
        return Optional.ofNullable((Holder.Reference) this.delegatesByValue.get(value));
    }

    @NotNull
    @Override
    public Holder.Reference<V> getDelegateOrThrow(V value) {
        return (Holder.Reference<V>) this.getDelegate(value).orElseThrow(() -> new IllegalArgumentException(String.format(Locale.ENGLISH, "No delegate exists for value %s", value)));
    }

    private Holder.Reference<V> bindDelegate(ResourceKey<V> rkey, V value) {
        Holder.Reference<V> delegate = (Holder.Reference<V>) this.delegatesByName.computeIfAbsent(rkey.location(), k -> Holder.Reference.createStandAlone(this.getWrapperOrThrow().m_255331_(), rkey));
        delegate.bindKey(rkey);
        delegate.bindValue(value);
        this.delegatesByValue.put(value, delegate);
        return delegate;
    }

    void resetDelegates() {
        if (this.hasWrapper) {
            for (Entry<ResourceKey<V>, V> entry : this.keys.entrySet()) {
                this.bindDelegate((ResourceKey<V>) entry.getKey(), (V) entry.getValue());
            }
            for (Entry<ResourceLocation, V> entry : this.overrides.entries()) {
                this.bindDelegate(ResourceKey.create(this.key, (ResourceLocation) entry.getKey()), (V) entry.getValue());
            }
        }
    }

    V getDefault() {
        return this.defaultValue;
    }

    void validateContent(ResourceLocation registryName) {
        for (V obj : this) {
            int id = this.getID(obj);
            ResourceLocation name = this.getKey(obj);
            if (name == null) {
                throw new IllegalStateException(String.format(Locale.ENGLISH, "Registry entry for %s %s, id %d, doesn't yield a name.", registryName, obj, id));
            }
            if (id > this.max) {
                throw new IllegalStateException(String.format(Locale.ENGLISH, "Registry entry for %s %s, name %s uses the too large id %d.", registryName, obj, name, id));
            }
            if (this.getValue(id) != obj) {
                throw new IllegalStateException(String.format(Locale.ENGLISH, "Registry entry for id %d, name %s, doesn't yield the expected %s %s.", id, name, registryName, obj));
            }
            if (this.getValue(name) != obj) {
                throw new IllegalStateException(String.format(Locale.ENGLISH, "Registry entry for name %s, id %d, doesn't yield the expected %s %s.", name, id, registryName, obj));
            }
            if (this.getID(name) != id) {
                throw new IllegalStateException(String.format(Locale.ENGLISH, "Registry entry for name %s doesn't yield the expected id %d.", name, id));
            }
            if (this.validate != null) {
                this.validate.onValidate(this, this.stage, id, name, obj);
            }
        }
    }

    public void bake() {
        if (this.bake != null) {
            this.bake.onBake(this, this.stage);
        }
    }

    void sync(ResourceLocation name, ForgeRegistry<V> from) {
        LOGGER.debug(REGISTRIES, "Registry {} Sync: {} -> {}", this.name, this.stage.getName(), from.stage.getName());
        if (this == from) {
            throw new IllegalArgumentException("WTF We are the same!?!?!");
        } else {
            this.isFrozen = false;
            if (this.clear != null) {
                this.clear.onClear(this, this.stage);
            }
            for (Entry<ResourceLocation, ResourceLocation> entry : from.aliases.entrySet()) {
                if (!this.aliases.containsKey(entry.getKey())) {
                    this.aliases.put((ResourceLocation) entry.getKey(), (ResourceLocation) entry.getValue());
                }
            }
            this.ids.clear();
            this.names.clear();
            this.keys.clear();
            this.availabilityMap.clear(0, this.availabilityMap.length());
            this.defaultValue = null;
            this.overrides.clear();
            this.owners.clear();
            boolean errored = false;
            for (Entry<ResourceLocation, V> entryx : from.names.entrySet()) {
                List<V> overrides = new ArrayList(from.overrides.get((ResourceLocation) entryx.getKey()));
                int id = from.getID((ResourceLocation) entryx.getKey());
                if (overrides.isEmpty()) {
                    int realId = this.add(id, (ResourceLocation) entryx.getKey(), (V) entryx.getValue());
                    if (id != realId && id != -1) {
                        LOGGER.warn(REGISTRIES, "Registry {}: Object did not get ID it asked for. Name: {} Expected: {} Got: {}", this.name, entryx.getKey(), id, realId);
                        errored = true;
                    }
                } else {
                    overrides.add(entryx.getValue());
                    for (V value : overrides) {
                        ForgeRegistry.OverrideOwner<V> owner = (ForgeRegistry.OverrideOwner<V>) from.owners.inverse().get(value);
                        if (owner == null) {
                            LOGGER.warn(REGISTRIES, "Registry {}: Override did not have an associated owner object. Name: {} Value: {}", this.name, entryx.getKey(), value);
                            errored = true;
                        } else {
                            int realId = this.add(id, (ResourceLocation) entryx.getKey(), value, owner.owner);
                            if (id != realId && id != -1) {
                                LOGGER.warn(REGISTRIES, "Registry {}: Object did not get ID it asked for. Name: {} Expected: {} Got: {}", this.name, entryx.getKey(), id, realId);
                                errored = true;
                            }
                        }
                    }
                }
            }
            if (errored) {
                throw new RuntimeException("One of more entry values did not copy to the correct id. Check log for details!");
            }
        }
    }

    @Override
    public void clear() {
        if (!this.isModifiable) {
            throw new UnsupportedOperationException("Attempted to clear a non-modifiable Forge Registry");
        } else if (this.isLocked()) {
            throw new IllegalStateException("Attempted to clear the registry to late.");
        } else {
            if (this.clear != null) {
                this.clear.onClear(this, this.stage);
            }
            this.aliases.clear();
            this.ids.clear();
            this.names.clear();
            this.keys.clear();
            this.availabilityMap.clear(0, this.availabilityMap.length());
        }
    }

    @Override
    public V remove(ResourceLocation key) {
        if (!this.isModifiable) {
            throw new UnsupportedOperationException("Attempted to remove from a non-modifiable Forge Registry");
        } else if (this.isLocked()) {
            throw new IllegalStateException("Attempted to remove from the registry to late.");
        } else {
            V value = (V) this.names.remove(key);
            if (value != null) {
                ResourceKey<V> rkey = (ResourceKey<V>) this.keys.inverse().remove(value);
                if (rkey == null) {
                    throw new IllegalStateException("Removed a entry that did not have an associated RegistryKey: " + key + " " + value.toString() + " This should never happen unless hackery!");
                }
                Integer id = (Integer) this.ids.inverse().remove(value);
                if (id == null) {
                    throw new IllegalStateException("Removed a entry that did not have an associated id: " + key + " " + value.toString() + " This should never happen unless hackery!");
                }
                LOGGER.trace(REGISTRIES, "Registry {} remove: {} {}", this.name, key, id);
            }
            return value;
        }
    }

    void block(int id) {
        this.blocked.add(id);
        this.availabilityMap.set(id);
    }

    @Override
    public boolean isLocked() {
        return this.isFrozen;
    }

    public void freeze() {
        this.isFrozen = true;
    }

    public void unfreeze() {
        this.isFrozen = false;
    }

    void dump(ResourceLocation name) {
        if (LOGGER.isDebugEnabled(REGISTRYDUMP)) {
            TablePrinter<ForgeRegistry.DumpRow> tab = new TablePrinter<ForgeRegistry.DumpRow>().header("ID", r -> r.id).header("Key", r -> r.key).header("Value", r -> r.value);
            LOGGER.debug(REGISTRYDUMP, () -> LogMessageAdapter.adapt(sb -> {
                sb.append("Registry Name: ").append(name).append('\n');
                tab.clearRows();
                this.getKeys().stream().map(this::getID).sorted().map(id -> {
                    V val = this.getValue(id);
                    ResourceLocation key = this.getKey(val);
                    return new ForgeRegistry.DumpRow(Integer.toString(id), key.toString(), val.toString());
                }).forEach(tab::add);
                tab.build(sb);
            }));
        }
    }

    public void loadIds(Object2IntMap<ResourceLocation> ids, Map<ResourceLocation, String> overrides, Object2IntMap<ResourceLocation> missing, Map<ResourceLocation, IdMappingEvent.IdRemapping> remapped, ForgeRegistry<V> old, ResourceLocation name) {
        Map<ResourceLocation, String> ovs = new HashMap(overrides);
        ObjectIterator var8 = ids.object2IntEntrySet().iterator();
        while (var8.hasNext()) {
            it.unimi.dsi.fastutil.objects.Object2IntMap.Entry<ResourceLocation> entry = (it.unimi.dsi.fastutil.objects.Object2IntMap.Entry<ResourceLocation>) var8.next();
            ResourceLocation itemName = (ResourceLocation) entry.getKey();
            int newId = entry.getIntValue();
            int currId = old.getIDRaw(itemName);
            if (currId == -1) {
                LOGGER.info(REGISTRIES, "Registry {}: Found a missing id from the world {}", this.name, itemName);
                missing.put(itemName, newId);
            } else {
                if (currId != newId) {
                    LOGGER.debug(REGISTRIES, "Registry {}: Fixed {} id mismatch {}: {} (init) -> {} (map).", this.name, name, itemName, currId, newId);
                    remapped.put(itemName, new IdMappingEvent.IdRemapping(currId, newId));
                }
                V obj = old.getRaw(itemName);
                Preconditions.checkState(obj != null, "objectKey has an ID but no object. Reflection/ASM hackery? Registry bug?");
                List<V> lst = new ArrayList(old.overrides.get(itemName));
                String primaryName = null;
                if (old.overrides.containsKey(itemName)) {
                    if (!overrides.containsKey(itemName)) {
                        lst.add(obj);
                        obj = (V) old.overrides.get(itemName).iterator().next();
                        primaryName = ((ForgeRegistry.OverrideOwner) old.owners.inverse().get(obj)).owner;
                    } else {
                        primaryName = (String) overrides.get(itemName);
                    }
                }
                for (V value : lst) {
                    ForgeRegistry.OverrideOwner<V> owner = (ForgeRegistry.OverrideOwner<V>) old.owners.inverse().get(value);
                    if (owner == null) {
                        LOGGER.warn(REGISTRIES, "Registry {}: Override did not have an associated owner object. Name: {} Value: {}", this.name, entry.getKey(), value);
                    } else if (!primaryName.equals(owner.owner)) {
                        int realId = this.add(newId, itemName, value, owner.owner);
                        if (newId != realId) {
                            LOGGER.warn(REGISTRIES, "Registry {}: Object did not get ID it asked for. Name: {} Expected: {} Got: {}", this.name, entry.getKey(), newId, realId);
                        }
                    }
                }
                int realId = this.add(newId, itemName, obj, primaryName == null ? itemName.getNamespace() : primaryName);
                if (realId != newId) {
                    LOGGER.warn(REGISTRIES, "Registry {}: Object did not get ID it asked for. Name: {} Expected: {} Got: {}", this.name, entry.getKey(), newId, realId);
                }
                ovs.remove(itemName);
            }
        }
        for (Entry<ResourceLocation, String> entry : ovs.entrySet()) {
            ResourceLocation itemName = (ResourceLocation) entry.getKey();
            String owner = (String) entry.getValue();
            String current = ((ForgeRegistry.OverrideOwner) this.owners.inverse().get(this.getRaw(itemName))).owner;
            if (!owner.equals(current)) {
                V _new = (V) this.owners.get(new ForgeRegistry.OverrideOwner<>(owner, ResourceKey.create(this.key, itemName)));
                if (_new == null) {
                    LOGGER.warn(REGISTRIES, "Registry {}: Skipping override for {}, Unknown owner {}", this.name, itemName, owner);
                } else {
                    LOGGER.info(REGISTRIES, "Registry {}: Activating override {} for {}", this.name, owner, itemName);
                    int newId = this.getID(itemName);
                    int realId = this.add(newId, itemName, _new, owner);
                    if (newId != realId) {
                        LOGGER.warn(REGISTRIES, "Registry {}: Object did not get ID it asked for. Name: {} Expected: {} Got: {}", this.name, entry.getKey(), newId, realId);
                    }
                }
            }
        }
    }

    public ForgeRegistry.Snapshot makeSnapshot() {
        ForgeRegistry.Snapshot ret = new ForgeRegistry.Snapshot();
        this.ids.forEach((id, value) -> ret.ids.put(this.getKey((V) value), id));
        ret.aliases.putAll(this.aliases);
        ret.blocked.addAll(this.blocked);
        ret.overrides.putAll(this.getOverrideOwners());
        return ret;
    }

    Map<ResourceLocation, String> getOverrideOwners() {
        Map<ResourceLocation, String> ret = new HashMap();
        for (ResourceLocation key : this.overrides.keySet()) {
            V obj = (V) this.names.get(key);
            ForgeRegistry.OverrideOwner<V> owner = (ForgeRegistry.OverrideOwner<V>) this.owners.inverse().get(obj);
            if (owner == null) {
                LOGGER.debug(REGISTRIES, "Registry {} {}: Invalid override {} {}", this.name, this.stage.getName(), key, obj);
            }
            ret.put(key, owner.owner);
        }
        return ret;
    }

    public MissingMappingsEvent getMissingEvent(ResourceLocation name, Object2IntMap<ResourceLocation> map) {
        List<MissingMappingsEvent.Mapping<V>> lst = new ArrayList();
        ForgeRegistry<V> pool = RegistryManager.ACTIVE.getRegistry(name);
        map.object2IntEntrySet().forEach(entry -> lst.add(new MissingMappingsEvent.Mapping<>(this, pool, (ResourceLocation) entry.getKey(), entry.getIntValue())));
        return new MissingMappingsEvent(ResourceKey.createRegistryKey(name), this, lst);
    }

    void processMissingEvent(ResourceLocation name, ForgeRegistry<V> pool, List<MissingMappingsEvent.Mapping<V>> mappings, Object2IntMap<ResourceLocation> missing, Map<ResourceLocation, IdMappingEvent.IdRemapping> remaps, Collection<ResourceLocation> defaulted, Collection<ResourceLocation> failed, boolean injectNetworkDummies) {
        LOGGER.debug(REGISTRIES, "Processing missing event for {}:", name);
        int ignored = 0;
        for (MissingMappingsEvent.Mapping<V> remap : mappings) {
            MissingMappingsEvent.Action action = remap.action;
            if (action == MissingMappingsEvent.Action.REMAP) {
                int currId = this.getID(remap.target);
                ResourceLocation newName = pool.getKey(remap.target);
                LOGGER.debug(REGISTRIES, "  Remapping {} -> {}.", remap.key, newName);
                missing.removeInt(remap.key);
                int realId = this.add(remap.id, newName, remap.target);
                if (realId != remap.id) {
                    LOGGER.warn(REGISTRIES, "Registered object did not get ID it asked for. Name: {} Expected: {} Got: {}", newName, remap.id, realId);
                }
                this.addAlias(remap.key, newName);
                if (currId != realId) {
                    LOGGER.info(REGISTRIES, "Fixed id mismatch {}: {} (init) -> {} (map).", newName, currId, realId);
                    remaps.put(newName, new IdMappingEvent.IdRemapping(currId, realId));
                }
            } else {
                if (action == MissingMappingsEvent.Action.DEFAULT) {
                    V m = this.missing == null ? null : this.missing.createMissing(remap.key, injectNetworkDummies);
                    if (m == null) {
                        defaulted.add(remap.key);
                    } else {
                        this.add(remap.id, remap.key, m, remap.key.getNamespace());
                    }
                } else if (action == MissingMappingsEvent.Action.IGNORE) {
                    LOGGER.debug(REGISTRIES, "Ignoring {}", remap.key);
                    ignored++;
                } else if (action == MissingMappingsEvent.Action.FAIL) {
                    LOGGER.debug(REGISTRIES, "Failing {}!", remap.key);
                    failed.add(remap.key);
                } else if (action == MissingMappingsEvent.Action.WARN) {
                    LOGGER.warn(REGISTRIES, "{} may cause world breakage!", remap.key);
                }
                this.block(remap.id);
            }
        }
        if (failed.isEmpty() && ignored > 0) {
            LOGGER.debug(REGISTRIES, "There were {} missing mappings that have been ignored", ignored);
        }
    }

    RegistryBuilder<V> getBuilder() {
        return this.builder;
    }

    private static record DumpRow(String id, String key, String value) {
    }

    private static record OverrideOwner<V>(String owner, ResourceKey<V> key) {
    }

    private class RegistryCodec implements Codec<V> {

        public <T> DataResult<Pair<V, T>> decode(DynamicOps<T> ops, T input) {
            return ops.compressMaps() ? ops.getNumberValue(input).flatMap(n -> {
                int id = n.intValue();
                if (ForgeRegistry.this.ids.get(id) == null) {
                    return DataResult.error(() -> "Unknown registry id in " + ForgeRegistry.this.key + ": " + n);
                } else {
                    V val = ForgeRegistry.this.getValue(id);
                    return DataResult.success(val);
                }
            }).map(v -> Pair.of(v, ops.empty())) : ResourceLocation.CODEC.decode(ops, input).flatMap(keyValuePair -> !ForgeRegistry.this.containsKey((ResourceLocation) keyValuePair.getFirst()) ? DataResult.error(() -> "Unknown registry key in " + ForgeRegistry.this.key + ": " + keyValuePair.getFirst()) : DataResult.success(keyValuePair.mapFirst(ForgeRegistry.this::getValue)));
        }

        public <T> DataResult<T> encode(V input, DynamicOps<T> ops, T prefix) {
            ResourceLocation key = ForgeRegistry.this.getKey(input);
            if (key == null) {
                return DataResult.error(() -> "Unknown registry element in " + ForgeRegistry.this.key + ": " + input);
            } else {
                T toMerge = (T) (ops.compressMaps() ? ops.createInt(ForgeRegistry.this.getID(input)) : ops.createString(key.toString()));
                return ops.mergeToPrimitive(prefix, toMerge);
            }
        }
    }

    public static class Snapshot {

        private static final Comparator<ResourceLocation> sorter = ResourceLocation::compareNamespaced;

        public final Object2IntMap<ResourceLocation> ids = new Object2IntRBTreeMap(sorter);

        public final Map<ResourceLocation, ResourceLocation> aliases = new TreeMap(sorter);

        public final IntSet blocked = new IntRBTreeSet();

        public final Map<ResourceLocation, String> overrides = new TreeMap(sorter);

        private FriendlyByteBuf binary = null;

        public CompoundTag write() {
            CompoundTag data = new CompoundTag();
            ListTag ids = new ListTag();
            this.ids.object2IntEntrySet().forEach(e -> {
                CompoundTag tag = new CompoundTag();
                tag.putString("K", ((ResourceLocation) e.getKey()).toString());
                tag.putInt("V", e.getIntValue());
                ids.add(tag);
            });
            data.put("ids", ids);
            ListTag aliases = new ListTag();
            this.aliases.entrySet().forEach(e -> {
                CompoundTag tag = new CompoundTag();
                tag.putString("K", ((ResourceLocation) e.getKey()).toString());
                tag.putString("V", ((ResourceLocation) e.getValue()).toString());
                aliases.add(tag);
            });
            data.put("aliases", aliases);
            ListTag overrides = new ListTag();
            this.overrides.entrySet().forEach(e -> {
                CompoundTag tag = new CompoundTag();
                tag.putString("K", ((ResourceLocation) e.getKey()).toString());
                tag.putString("V", (String) e.getValue());
                overrides.add(tag);
            });
            data.put("overrides", overrides);
            int[] blocked = this.blocked.intStream().sorted().toArray();
            data.putIntArray("blocked", blocked);
            return data;
        }

        public static ForgeRegistry.Snapshot read(CompoundTag nbt) {
            ForgeRegistry.Snapshot ret = new ForgeRegistry.Snapshot();
            if (nbt == null) {
                return ret;
            } else {
                ListTag list = nbt.getList("ids", 10);
                list.forEach(e -> {
                    CompoundTag comp = (CompoundTag) e;
                    ret.ids.put(new ResourceLocation(comp.getString("K")), comp.getInt("V"));
                });
                list = nbt.getList("aliases", 10);
                list.forEach(e -> {
                    CompoundTag comp = (CompoundTag) e;
                    ret.aliases.put(new ResourceLocation(comp.getString("K")), new ResourceLocation(comp.getString("V")));
                });
                list = nbt.getList("overrides", 10);
                list.forEach(e -> {
                    CompoundTag comp = (CompoundTag) e;
                    ret.overrides.put(new ResourceLocation(comp.getString("K")), comp.getString("V"));
                });
                int[] blocked = nbt.getIntArray("blocked");
                for (int i : blocked) {
                    ret.blocked.add(i);
                }
                return ret;
            }
        }

        public synchronized FriendlyByteBuf getPacketData() {
            if (this.binary == null) {
                FriendlyByteBuf pkt = new FriendlyByteBuf(Unpooled.buffer());
                pkt.writeMap(this.ids, FriendlyByteBuf::m_130085_, FriendlyByteBuf::m_130130_);
                pkt.writeMap(this.aliases, FriendlyByteBuf::m_130085_, FriendlyByteBuf::m_130085_);
                pkt.writeMap(this.overrides, FriendlyByteBuf::m_130085_, (b, v) -> b.writeUtf(v, 256));
                pkt.writeCollection(this.blocked, FriendlyByteBuf::m_130130_);
                this.binary = pkt;
            }
            return new FriendlyByteBuf(this.binary.slice());
        }

        public static ForgeRegistry.Snapshot read(FriendlyByteBuf buf) {
            if (buf == null) {
                return new ForgeRegistry.Snapshot();
            } else {
                ForgeRegistry.Snapshot ret = new ForgeRegistry.Snapshot();
                ret.ids.putAll(buf.readMap(FriendlyByteBuf::m_130281_, FriendlyByteBuf::m_130242_));
                ret.aliases.putAll(buf.readMap(FriendlyByteBuf::m_130281_, FriendlyByteBuf::m_130281_));
                ret.overrides.putAll(buf.readMap(FriendlyByteBuf::m_130281_, b -> b.readUtf(256)));
                ret.blocked.addAll(buf.readList(FriendlyByteBuf::m_130242_));
                return ret;
            }
        }
    }
}