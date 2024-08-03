package snownee.loquat.core;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.phys.AABB;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import snownee.loquat.Loquat;
import snownee.loquat.LoquatRegistries;
import snownee.loquat.core.area.Area;
import snownee.loquat.core.area.Zone;
import snownee.loquat.duck.AreaManagerContainer;
import snownee.loquat.duck.LoquatServerPlayer;
import snownee.loquat.network.SOutlinesPacket;
import snownee.loquat.network.SSyncRestrictionPacket;

public class AreaManager extends SavedData {

    private final List<Area> areas = ObjectArrayList.of();

    private final Map<UUID, Area> map = Maps.newHashMap();

    private final Set<UUID> showOutlinePlayers = Sets.newHashSet();

    private final List<AreaEvent> events = ObjectArrayList.of();

    private final List<AreaEvent> pendingEvents = ObjectArrayList.of();

    private final Set<Object> boundsCache = Sets.newHashSet();

    private final Long2ObjectOpenHashMap<Set<Area>> chunkLookup = new Long2ObjectOpenHashMap();

    private final RestrictInstance fallbackRestriction = new RestrictInstance();

    private final Map<String, RestrictInstance> restrictions = Maps.newHashMap();

    private ServerLevel level;

    private boolean ticking;

    public AreaManager() {
        this.restrictions.put("*", this.fallbackRestriction);
    }

    public static AreaManager of(ServerLevel level) {
        AreaManagerContainer container = (AreaManagerContainer) level;
        AreaManager manager = container.loquat$getAreaManager();
        if (manager == null) {
            manager = level.getDataStorage().computeIfAbsent(AreaManager::load, AreaManager::new, "loquat");
            manager.level = level;
            container.loquat$setAreaManager(manager);
        }
        return manager;
    }

    public static AreaManager load(CompoundTag tag) {
        AreaManager manager = new AreaManager();
        loadAreas(tag.getList("Areas", 10)).forEach(manager::add);
        for (Tag t : tag.getList("Events", 10)) {
            try {
                AreaEvent event = AreaEvent.deserialize(manager, (CompoundTag) t);
                if (event != null) {
                    manager.events.add(event);
                }
            } catch (Exception var6) {
                Loquat.LOGGER.error("Failed to load area event", var6);
            }
        }
        if (tag.contains("Restrictions", 10)) {
            CompoundTag restrictions = tag.getCompound("Restrictions");
            if (restrictions.contains("*")) {
                manager.fallbackRestriction.deserializeNBT(manager, restrictions.getList("*", 10));
            }
            for (String key : restrictions.getAllKeys()) {
                if (!key.equals("*")) {
                    RestrictInstance restrictInstance = manager.getOrCreateRestrictInstance(key);
                    restrictInstance.deserializeNBT(manager, restrictions.getList(key, 10));
                    manager.restrictions.put(key, restrictInstance);
                }
            }
        }
        manager.m_77760_(false);
        return manager;
    }

    public static ListTag saveAreas(Collection<Area> areas) {
        return saveAreas(areas, false, null);
    }

    public static ListTag saveAreas(Collection<Area> areas, boolean skipMetadata, @Nullable BiConsumer<Area, CompoundTag> consumer) {
        ListTag tag = new ListTag();
        for (Area area : areas) {
            CompoundTag data = new CompoundTag();
            if (!skipMetadata) {
                if (area.getUuid() != null) {
                    data.putUUID("UUID", area.getUuid());
                }
                if (!area.getTags().isEmpty()) {
                    data.put("Tags", (Tag) area.getTags().stream().map(StringTag::m_129297_).collect(ListTag::new, AbstractList::add, AbstractList::add));
                }
                if (!area.getZones().isEmpty()) {
                    CompoundTag zones = new CompoundTag();
                    area.getZones().forEach((name, zone) -> zones.put(name, zone.serialize(new CompoundTag())));
                    data.put("Zones", zones);
                }
                if (area.getAttachedData() != null && !area.getAttachedData().isEmpty()) {
                    data.put("Data", area.getAttachedData());
                }
            }
            data.putString("Type", LoquatRegistries.AREA.getKey(area.getType()).toString());
            ((Area.Type<Area>) area.getType()).serialize(data, area);
            if (consumer != null) {
                consumer.accept(area, data);
            }
            tag.add(data);
        }
        return tag;
    }

    public static List<Area> loadAreas(ListTag tag) {
        List<Area> areas = new ArrayList(tag.size());
        loadAreas(tag, (area, data) -> areas.add(area));
        return areas;
    }

    public static void loadAreas(ListTag tag, BiConsumer<Area, CompoundTag> consumer) {
        for (int i = 0; i < tag.size(); i++) {
            CompoundTag data = tag.getCompound(i);
            Area.Type<?> type = LoquatRegistries.AREA.get(new ResourceLocation(data.getString("Type")));
            Area area = type.deserialize(data);
            if (data.contains("UUID")) {
                area.setUuid(data.getUUID("UUID"));
            }
            if (data.contains("Tags")) {
                ListTag tags = data.getList("Tags", 8);
                for (int j = 0; j < tags.size(); j++) {
                    area.getTags().add(tags.getString(j));
                }
            }
            if (data.contains("Zones")) {
                CompoundTag zones = data.getCompound("Zones");
                for (String name : zones.getAllKeys()) {
                    area.getZones().put(name, Zone.deserialize(zones.getCompound(name)));
                }
            }
            if (data.contains("Data")) {
                area.setAttachedData(data.getCompound("Data"));
            }
            consumer.accept(area, data);
        }
    }

    public void add(Area area) {
        Objects.requireNonNull(area.getUuid(), "Area UUID cannot be null");
        Preconditions.checkState(!this.map.containsKey(area.getUuid()), "Area UUID already exists: %s", area);
        Object bounds = area.getBounds();
        Preconditions.checkState(!this.boundsCache.contains(bounds), "Area already exists: same bounds");
        this.areas.add(area);
        this.map.put(area.getUuid(), area);
        this.boundsCache.add(bounds);
        area.getChunksIn().forEach(chunk -> ((Set) this.chunkLookup.computeIfAbsent(chunk, c -> Sets.newHashSet())).add(area));
        this.setChanged(List.of(area));
    }

    public boolean contains(Area area) {
        return this.areas.contains(area);
    }

    public Area get(UUID uuid) {
        return (Area) this.map.get(uuid);
    }

    public Stream<Area> byTag(String tag) {
        return this.areas.stream().filter(a -> a.getTags().contains(tag));
    }

    public Stream<Area> byChunk(long chunkPos) {
        return ((Set) this.chunkLookup.getOrDefault(chunkPos, Set.of())).stream();
    }

    public Stream<Area> byPosition(BlockPos pos) {
        return this.byChunk(ChunkPos.asLong(pos)).filter(a -> a.contains(pos));
    }

    public boolean remove(UUID uuid) {
        Area area = (Area) this.map.remove(uuid);
        if (area == null) {
            return false;
        } else {
            this.areas.remove(area);
            this.events.removeIf(e -> e.getArea() == area);
            this.pendingEvents.removeIf(e -> e.getArea() == area);
            boolean notifyAll = this.fallbackRestriction.removeArea(area);
            Set<String> names = notifyAll ? null : Sets.newHashSet();
            this.restrictions.forEach((key, restriction) -> {
                if (restriction != this.fallbackRestriction) {
                    if (restriction.removeArea(area) && !notifyAll) {
                        names.add(key);
                    }
                }
            });
            if (notifyAll) {
                this.level.getServer().getPlayerList().getPlayers().forEach(SSyncRestrictionPacket::sync);
            } else {
                for (ServerPlayer player : this.level.getServer().getPlayerList().getPlayers()) {
                    if (names.contains(player.m_6302_())) {
                        SSyncRestrictionPacket.sync(player);
                    }
                }
            }
            this.boundsCache.remove(area.getBounds());
            area.getChunksIn().forEach(chunk -> {
                Set<Area> areas = (Set<Area>) this.chunkLookup.get(chunk);
                if (areas != null) {
                    areas.remove(area);
                }
            });
            this.showOutline(Long.MIN_VALUE, List.of(area));
            this.m_77762_();
            return true;
        }
    }

    public boolean removeAllInside(AABB aabb) {
        List<UUID> toRemove = new ArrayList();
        for (Area area : this.areas) {
            if (area.inside(aabb)) {
                toRemove.add(area.getUuid());
            }
        }
        toRemove.forEach(this::remove);
        return !toRemove.isEmpty();
    }

    public void showOutline(long duration, Collection<Area> areas) {
        if (this.level != null) {
            this.showOutlinePlayers.stream().map(this.level::m_8791_).filter(Objects::nonNull).forEach(player -> SOutlinesPacket.outlines((ServerPlayer) player, duration, false, false, areas));
        }
    }

    public void setChanged(Collection<Area> areas) {
        this.m_77762_();
        this.showOutline(Long.MAX_VALUE, areas);
    }

    @NotNull
    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.put("Areas", saveAreas(this.areas));
        ListTag eventList = new ListTag();
        for (AreaEvent event : this.events) {
            if (!event.isFinished()) {
                eventList.add(event.serialize(new CompoundTag()));
            }
        }
        tag.put("Events", eventList);
        CompoundTag restrictionsData = new CompoundTag();
        for (Entry<String, RestrictInstance> entry : this.restrictions.entrySet()) {
            ((RestrictInstance) entry.getValue()).serializeNBT(this).ifPresent($ -> restrictionsData.put((String) entry.getKey(), $));
        }
        tag.put("Restrictions", restrictionsData);
        return tag;
    }

    public List<Area> areas() {
        return Collections.unmodifiableList(this.areas);
    }

    public List<AreaEvent> events() {
        return Collections.unmodifiableList(this.events);
    }

    public void addEvent(AreaEvent event) {
        if (!this.contains(event.getArea())) {
            Loquat.LOGGER.warn("Attempted to add event for non-existent area: {}", event.getArea());
        } else if (this.ticking) {
            this.pendingEvents.add(event);
        } else {
            this.events.add(event);
            this.m_77762_();
        }
    }

    public void tick() {
        this.ticking = true;
        this.events.removeIf(event -> {
            try {
                event.tick(this.level);
                event.ticksExisted++;
                return event.isFinished();
            } catch (Exception var3) {
                Loquat.LOGGER.error("Failed to tick area event", var3);
                return true;
            }
        });
        this.ticking = false;
        if (!this.pendingEvents.isEmpty()) {
            this.events.addAll(this.pendingEvents);
            this.pendingEvents.clear();
            this.m_77762_();
        }
    }

    public void onPlayerAdded(ServerPlayer player) {
        ((LoquatServerPlayer) player).loquat$reset();
        boolean showOutline = this.showOutlinePlayers.contains(player.m_20148_());
        SOutlinesPacket.outlines(player, Long.MAX_VALUE, true, false, showOutline ? this.areas : List.of());
        SSyncRestrictionPacket.sync(player);
    }

    public RestrictInstance getOrCreateRestrictInstance(String playerName) {
        return (RestrictInstance) this.restrictions.computeIfAbsent(playerName, $ -> {
            RestrictInstance restrictInstance = new RestrictInstance();
            restrictInstance.setFallback(this.fallbackRestriction);
            return restrictInstance;
        });
    }

    public int clearEvents(Collection<? extends Area> areas) {
        MutableInt count = new MutableInt();
        Set<Area> set = Set.copyOf(areas);
        this.events.removeIf(event -> {
            if (set.contains(event.getArea())) {
                count.increment();
                return true;
            } else {
                return false;
            }
        });
        this.pendingEvents.removeIf(event -> {
            if (set.contains(event.getArea())) {
                count.increment();
                return true;
            } else {
                return false;
            }
        });
        if (count.intValue() > 0) {
            this.m_77762_();
        }
        return count.intValue();
    }

    public int clearRestrictions(Collection<? extends Area> areas) {
        int count = 0;
        for (Area area : areas) {
            for (RestrictInstance restrictInstance : this.restrictions.values()) {
                if (restrictInstance.removeArea(area)) {
                    count++;
                }
            }
            if (this.fallbackRestriction.removeArea(area)) {
                count++;
            }
        }
        if (count > 0) {
            this.m_77762_();
        }
        return count;
    }

    public Set<UUID> getShowOutlinePlayers() {
        return this.showOutlinePlayers;
    }

    public RestrictInstance getFallbackRestriction() {
        return this.fallbackRestriction;
    }
}