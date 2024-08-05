package com.simibubi.create.foundation.mixin;

import com.google.common.collect.Maps;
import com.simibubi.create.content.trains.station.StationBlockEntity;
import com.simibubi.create.content.trains.station.StationMapData;
import com.simibubi.create.content.trains.station.StationMarker;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
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
public class MapItemSavedDataMixin implements StationMapData {

    @Unique
    private static final String STATION_MARKERS_KEY = "create:stations";

    @Shadow
    @Final
    public int centerX;

    @Shadow
    @Final
    public int centerZ;

    @Shadow
    @Final
    public byte scale;

    @Shadow
    @Final
    Map<String, MapDecoration> decorations;

    @Shadow
    private int trackedDecorationCount;

    @Unique
    private final Map<String, StationMarker> create$stationMarkers = Maps.newHashMap();

    @Inject(method = { "load(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData;" }, at = { @At("RETURN") })
    private static void create$onLoad(CompoundTag compound, CallbackInfoReturnable<MapItemSavedData> cir) {
        MapItemSavedData mapData = (MapItemSavedData) cir.getReturnValue();
        StationMapData stationMapData = (StationMapData) mapData;
        ListTag listTag = compound.getList("create:stations", 10);
        for (int i = 0; i < listTag.size(); i++) {
            StationMarker stationMarker = StationMarker.load(listTag.getCompound(i));
            stationMapData.addStationMarker(stationMarker);
        }
    }

    @Inject(method = { "save(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/nbt/CompoundTag;" }, at = { @At("RETURN") })
    public void create$onSave(CompoundTag compound, CallbackInfoReturnable<CompoundTag> cir) {
        ListTag listTag = new ListTag();
        for (StationMarker stationMarker : this.create$stationMarkers.values()) {
            listTag.add(stationMarker.save());
        }
        compound.put("create:stations", listTag);
    }

    @Override
    public void addStationMarker(StationMarker marker) {
        this.create$stationMarkers.put(marker.getId(), marker);
        int scaleMultiplier = 1 << this.scale;
        float localX = (float) (marker.getTarget().m_123341_() - this.centerX) / (float) scaleMultiplier;
        float localZ = (float) (marker.getTarget().m_123343_() - this.centerZ) / (float) scaleMultiplier;
        if (!(localX < -63.0F) && !(localX > 63.0F) && !(localZ < -63.0F) && !(localZ > 63.0F)) {
            byte localXByte = (byte) ((int) (localX * 2.0F + 0.5F));
            byte localZByte = (byte) ((int) (localZ * 2.0F + 0.5F));
            MapDecoration decoration = new StationMarker.Decoration(localXByte, localZByte, marker.getName());
            MapDecoration oldDecoration = (MapDecoration) this.decorations.put(marker.getId(), decoration);
            if (!decoration.equals(oldDecoration)) {
                if (oldDecoration != null && oldDecoration.getType().shouldTrackCount()) {
                    this.trackedDecorationCount--;
                }
                if (decoration.getType().shouldTrackCount()) {
                    this.trackedDecorationCount++;
                }
                this.setDecorationsDirty();
            }
        } else {
            this.removeDecoration(marker.getId());
        }
    }

    @Shadow
    private void removeDecoration(String identifier) {
        throw new AssertionError();
    }

    @Shadow
    private void setDecorationsDirty() {
        throw new AssertionError();
    }

    @Shadow
    public boolean isTrackedCountOverLimit(int trackedCount) {
        throw new AssertionError();
    }

    @Override
    public boolean toggleStation(LevelAccessor level, BlockPos pos, StationBlockEntity stationBlockEntity) {
        double xCenter = (double) pos.m_123341_() + 0.5;
        double zCenter = (double) pos.m_123343_() + 0.5;
        int scaleMultiplier = 1 << this.scale;
        double localX = (xCenter - (double) this.centerX) / (double) scaleMultiplier;
        double localZ = (zCenter - (double) this.centerZ) / (double) scaleMultiplier;
        if (!(localX < -63.0) && !(localX > 63.0) && !(localZ < -63.0) && !(localZ > 63.0)) {
            StationMarker marker = StationMarker.fromWorld(level, pos);
            if (marker == null) {
                return false;
            } else if (this.create$stationMarkers.remove(marker.getId(), marker)) {
                this.removeDecoration(marker.getId());
                return true;
            } else if (!this.isTrackedCountOverLimit(256)) {
                this.addStationMarker(marker);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Inject(method = { "checkBanners(Lnet/minecraft/world/level/BlockGetter;II)V" }, at = { @At("RETURN") })
    public void create$onCheckBanners(BlockGetter blockGetter, int x, int z, CallbackInfo ci) {
        this.create$checkStations(blockGetter, x, z);
    }

    @Unique
    private void create$checkStations(BlockGetter blockGetter, int x, int z) {
        Iterator<StationMarker> iterator = this.create$stationMarkers.values().iterator();
        List<StationMarker> newMarkers = new ArrayList();
        while (iterator.hasNext()) {
            StationMarker marker = (StationMarker) iterator.next();
            if (marker.getTarget().m_123341_() == x && marker.getTarget().m_123343_() == z) {
                StationMarker other = StationMarker.fromWorld(blockGetter, marker.getSource());
                if (!marker.equals(other)) {
                    iterator.remove();
                    this.removeDecoration(marker.getId());
                    if (other != null && marker.getTarget().equals(other.getTarget())) {
                        newMarkers.add(other);
                    }
                }
            }
        }
        for (StationMarker marker : newMarkers) {
            this.addStationMarker(marker);
        }
    }
}