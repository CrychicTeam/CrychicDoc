package net.mehvahdjukaar.moonlight.core.mixins;

import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.function.Consumer;
import net.mehvahdjukaar.moonlight.api.map.CustomMapData;
import net.mehvahdjukaar.moonlight.api.map.CustomMapDecoration;
import net.mehvahdjukaar.moonlight.api.map.ExpandedMapData;
import net.mehvahdjukaar.moonlight.api.map.markers.MapBlockMarker;
import net.mehvahdjukaar.moonlight.api.map.type.MapDecorationType;
import net.mehvahdjukaar.moonlight.core.Moonlight;
import net.mehvahdjukaar.moonlight.core.map.MapDataInternal;
import net.mehvahdjukaar.moonlight.core.misc.IHoldingPlayerExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.maps.MapBanner;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ MapItemSavedData.class })
public abstract class MapDataMixin extends SavedData implements ExpandedMapData {

    @Final
    @Shadow
    public byte scale;

    @Final
    @Shadow
    Map<String, MapDecoration> decorations;

    @Shadow
    @Final
    private Map<String, MapBanner> bannerMarkers;

    @Shadow
    public int centerX;

    @Shadow
    public int centerZ;

    @Shadow
    @Final
    private List<MapItemSavedData.HoldingPlayer> carriedBy;

    @Unique
    public Map<String, CustomMapDecoration> moonlight$customDecorations = Maps.newLinkedHashMap();

    @Unique
    private final Map<String, MapBlockMarker<?>> moonlight$customMapMarkers = Maps.newHashMap();

    @Unique
    public final Map<ResourceLocation, CustomMapData<?>> moonlight$customData = new LinkedHashMap();

    @Override
    public void setCustomDecorationsDirty() {
        this.m_77762_();
        this.carriedBy.forEach(h -> ((IHoldingPlayerExtension) h).moonlight$setCustomMarkersDirty());
    }

    @Override
    public <H extends CustomMapData.DirtyCounter> void setCustomDataDirty(CustomMapData.Type<?> type, Consumer<H> dirtySetter) {
        this.m_77762_();
        this.carriedBy.forEach(h -> ((IHoldingPlayerExtension) h).moonlight$setCustomDataDirty(type, dirtySetter));
    }

    @Override
    public Map<ResourceLocation, CustomMapData<?>> getCustomData() {
        return this.moonlight$customData;
    }

    @Override
    public Map<String, CustomMapDecoration> getCustomDecorations() {
        return this.moonlight$customDecorations;
    }

    @Override
    public Map<String, MapBlockMarker<?>> getCustomMarkers() {
        return this.moonlight$customMapMarkers;
    }

    @Override
    public int getVanillaDecorationSize() {
        return this.decorations.size();
    }

    @Override
    public <M extends MapBlockMarker<?>> void addCustomMarker(M marker) {
        CustomMapDecoration decoration = marker.createDecorationFromMarker((MapItemSavedData) this);
        if (decoration != null) {
            this.moonlight$customDecorations.put(marker.getMarkerId(), decoration);
            if (marker.shouldSave()) {
                this.moonlight$customMapMarkers.put(marker.getMarkerId(), marker);
            }
            this.setCustomDecorationsDirty();
        }
    }

    @Override
    public boolean removeCustomMarker(String key) {
        this.moonlight$customDecorations.remove(key);
        if (this.moonlight$customMapMarkers.containsKey(key)) {
            this.moonlight$customMapMarkers.remove(key);
            this.setCustomDecorationsDirty();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public MapItemSavedData copy() {
        MapItemSavedData newData = MapItemSavedData.load(this.m_7176_(new CompoundTag()));
        newData.m_77762_();
        return newData;
    }

    @Override
    public void resetCustomDecoration() {
        if (!this.bannerMarkers.isEmpty() || !this.moonlight$customMapMarkers.isEmpty()) {
            this.setCustomDecorationsDirty();
        }
        for (String key : this.moonlight$customMapMarkers.keySet()) {
            this.moonlight$customDecorations.remove(key);
        }
        this.moonlight$customMapMarkers.clear();
        for (String key : this.bannerMarkers.keySet()) {
            this.decorations.remove(key);
        }
        this.bannerMarkers.clear();
    }

    @Override
    public boolean toggleCustomDecoration(LevelAccessor world, BlockPos pos) {
        if (world.m_5776_()) {
            List<MapBlockMarker<?>> markers = MapDataInternal.getMarkersFromWorld(world, pos);
            return !markers.isEmpty();
        } else {
            double d0 = (double) pos.m_123341_() + 0.5;
            double d1 = (double) pos.m_123343_() + 0.5;
            int i = 1 << this.scale;
            double d2 = (d0 - (double) this.centerX) / (double) i;
            double d3 = (d1 - (double) this.centerZ) / (double) i;
            if (d2 >= -63.0 && d3 >= -63.0 && d2 <= 63.0 && d3 <= 63.0) {
                List<MapBlockMarker<?>> markers = MapDataInternal.getMarkersFromWorld(world, pos);
                boolean changed = false;
                for (MapBlockMarker<?> marker : markers) {
                    if (marker != null) {
                        String id = marker.getMarkerId();
                        if (marker.equals(this.moonlight$customMapMarkers.get(id))) {
                            this.removeCustomMarker(id);
                        } else {
                            this.addCustomMarker(marker);
                        }
                        changed = true;
                    }
                }
                return changed;
            } else {
                return false;
            }
        }
    }

    @Inject(method = { "locked" }, at = { @At("RETURN") })
    public void locked(CallbackInfoReturnable<MapItemSavedData> cir) {
        MapItemSavedData data = (MapItemSavedData) cir.getReturnValue();
        if (data instanceof ExpandedMapData expandedMapData) {
            expandedMapData.getCustomMarkers().putAll(this.getCustomMarkers());
            expandedMapData.getCustomDecorations().putAll(this.getCustomDecorations());
        }
        this.moonlight$copyCustomData(data);
    }

    @Inject(method = { "scaled" }, at = { @At("RETURN") })
    public void scaled(CallbackInfoReturnable<MapItemSavedData> cir) {
        MapItemSavedData data = (MapItemSavedData) cir.getReturnValue();
        this.moonlight$copyCustomData(data);
    }

    @Unique
    private void moonlight$copyCustomData(MapItemSavedData data) {
        if (data instanceof ExpandedMapData ed) {
            for (Entry<ResourceLocation, CustomMapData<?>> d : this.moonlight$customData.entrySet()) {
                CustomMapData<?> v = (CustomMapData<?>) d.getValue();
                if (v.persistOnCopyOrLock()) {
                    CompoundTag t = new CompoundTag();
                    v.save(t);
                    ((CustomMapData) ed.getCustomData().get(d.getKey())).load(t);
                }
            }
        }
    }

    @Inject(method = { "tickCarriedBy" }, at = { @At("TAIL") })
    public void tickCarriedBy(Player player, ItemStack stack, CallbackInfo ci) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("CustomDecorations", 9)) {
            ListTag listTag = tag.getList("CustomDecorations", 10);
            for (int j = 0; j < listTag.size(); j++) {
                CompoundTag com = listTag.getCompound(j);
                if (!this.decorations.containsKey(com.getString("id"))) {
                    String name = com.getString("type");
                    MapDecorationType<? extends CustomMapDecoration, ?> type = MapDataInternal.get(name);
                    if (type != null) {
                        BlockPos pos = new BlockPos(com.getInt("x"), 64, com.getInt("z"));
                        MapBlockMarker<?> marker = type.createEmptyMarker();
                        marker.setPos(pos);
                        this.addCustomMarker(marker);
                    } else {
                        Moonlight.LOGGER.warn("Failed to load map decoration " + name + ". Skipping it");
                    }
                }
            }
        }
    }

    @Inject(method = { "load" }, at = { @At("RETURN") })
    private static void load(CompoundTag compound, CallbackInfoReturnable<MapItemSavedData> cir) {
        MapItemSavedData data = (MapItemSavedData) cir.getReturnValue();
        if (compound.contains("customMarkers") && data instanceof ExpandedMapData mapData) {
            ListTag listNBT = compound.getList("customMarkers", 10);
            for (int j = 0; j < listNBT.size(); j++) {
                MapBlockMarker<?> marker = MapDataInternal.readWorldMarker(listNBT.getCompound(j));
                if (marker != null) {
                    mapData.getCustomMarkers().put(marker.getMarkerId(), marker);
                    mapData.addCustomMarker(marker);
                }
            }
            mapData.getCustomData().values().forEach(customMapData -> customMapData.load(compound));
        }
    }

    @Inject(method = { "save" }, at = { @At("RETURN") })
    public void save(CompoundTag tag, CallbackInfoReturnable<CompoundTag> cir) {
        CompoundTag com = (CompoundTag) cir.getReturnValue();
        ListTag listNBT = new ListTag();
        for (MapBlockMarker<?> marker : this.moonlight$customMapMarkers.values()) {
            if (marker.shouldSave()) {
                CompoundTag com2 = new CompoundTag();
                com2.put(marker.getTypeId(), marker.saveToNBT());
                listNBT.add(com2);
            }
        }
        com.put("customMarkers", listNBT);
        this.moonlight$customData.forEach((s, o) -> o.save(tag));
    }

    @Inject(method = { "checkBanners" }, at = { @At("TAIL") })
    public void checkCustomDeco(BlockGetter world, int x, int z, CallbackInfo ci) {
        List<String> toRemove = new ArrayList();
        List<MapBlockMarker<?>> toAdd = new ArrayList();
        for (Entry<String, MapBlockMarker<?>> e : this.moonlight$customMapMarkers.entrySet()) {
            MapBlockMarker<?> marker = (MapBlockMarker<?>) e.getValue();
            if (marker.getPos().m_123341_() == x && marker.getPos().m_123343_() == z && marker.shouldRefresh()) {
                MapBlockMarker<?> newMarker = marker.getType().getWorldMarkerFromWorld(world, marker.getPos());
                String id = (String) e.getKey();
                if (newMarker == null) {
                    toRemove.add(id);
                } else if (!Objects.equals(marker, newMarker)) {
                    toRemove.add(id);
                    toAdd.add(newMarker);
                }
            }
        }
        toRemove.forEach(this::removeCustomMarker);
        toAdd.forEach(this::addCustomMarker);
    }

    @Inject(method = { "<init>" }, at = { @At("TAIL") })
    public void initCustomData(int i, int j, byte b, boolean bl, boolean bl2, boolean bl3, ResourceKey resourceKey, CallbackInfo ci) {
        for (CustomMapData.Type<?> d : MapDataInternal.CUSTOM_MAP_DATA_TYPES.values()) {
            this.moonlight$customData.put(d.id(), (CustomMapData) d.factory().get());
        }
    }
}