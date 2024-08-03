package net.mehvahdjukaar.moonlight.core.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import net.mehvahdjukaar.moonlight.api.map.CustomMapData;
import net.mehvahdjukaar.moonlight.api.map.CustomMapDecoration;
import net.mehvahdjukaar.moonlight.api.map.ExpandedMapData;
import net.mehvahdjukaar.moonlight.api.map.markers.MapBlockMarker;
import net.mehvahdjukaar.moonlight.core.map.MapDataInternal;
import net.mehvahdjukaar.moonlight.core.misc.IHoldingPlayerExtension;
import net.mehvahdjukaar.moonlight.core.misc.IMapDataPacketExtension;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ MapItemSavedData.HoldingPlayer.class })
public abstract class HoldingPlayerMixin implements IHoldingPlayerExtension {

    @Unique
    private final ReentrantLock moonlight$concurrentLock = new ReentrantLock();

    @Unique
    private final Map<CustomMapData.Type<?>, CustomMapData.DirtyCounter> moonlight$customDataDirty = new IdentityHashMap();

    @Unique
    private boolean moonlight$customMarkersDirty = true;

    @Unique
    private int moonlight$dirtyDecorationTicks = 0;

    @Unique
    private int moonlight$volatileDecorationRefreshTicks = 0;

    @Final
    @Shadow
    MapItemSavedData f_77961_;

    @Shadow
    @Final
    public Player player;

    @Inject(method = { "<init>" }, at = { @At("TAIL") })
    public void initializeDirty(MapItemSavedData mapItemSavedData, Player player, CallbackInfo ci) {
        this.moonlight$customMarkersDirty = true;
        for (CustomMapData<?> v : ((ExpandedMapData) mapItemSavedData).getCustomData().values()) {
            this.moonlight$customDataDirty.put(v.getType(), v.createDirtyCounter());
        }
    }

    @Inject(method = { "nextUpdatePacket" }, at = { @At("HEAD") }, cancellable = true)
    public void checkLocked(int mapId, CallbackInfoReturnable<Packet<?>> cir) {
        if (this.moonlight$concurrentLock.isLocked()) {
            cir.setReturnValue(null);
        }
    }

    @ModifyReturnValue(method = { "nextUpdatePacket" }, at = { @At("TAIL") })
    public Packet<?> addExtraPacketData(@Nullable Packet<?> packet, int mapId) {
        MapItemSavedData data = this.f_77961_;
        ExpandedMapData ed = (ExpandedMapData) data;
        boolean updateData = false;
        boolean updateDeco = false;
        List<Entry<CustomMapData.Type<?>, CustomMapData.DirtyCounter>> dirtyData = new ArrayList();
        for (Entry<CustomMapData.Type<?>, CustomMapData.DirtyCounter> e : this.moonlight$customDataDirty.entrySet()) {
            CustomMapData.DirtyCounter value = (CustomMapData.DirtyCounter) e.getValue();
            if (value.isDirty()) {
                dirtyData.add(e);
                updateData = true;
            }
        }
        if (this.moonlight$customMarkersDirty && this.moonlight$dirtyDecorationTicks++ % 5 == 0) {
            this.moonlight$customMarkersDirty = false;
            updateDeco = true;
        }
        List<CustomMapDecoration> extra = new ArrayList();
        for (MapBlockMarker<?> m : MapDataInternal.getDynamicServer(this.player, mapId, data)) {
            CustomMapDecoration d = m.createDecorationFromMarker(data);
            if (d != null) {
                extra.add(d);
            }
        }
        if (!extra.isEmpty() || this.moonlight$volatileDecorationRefreshTicks++ % 80 == 0) {
            updateDeco = true;
        }
        if (updateData || updateDeco) {
            if (packet == null) {
                packet = new ClientboundMapItemDataPacket(mapId, this.f_77961_.scale, this.f_77961_.locked, null, null);
            }
            IMapDataPacketExtension ep = (IMapDataPacketExtension) packet;
            if (updateData) {
                CompoundTag customDataTag = new CompoundTag();
                for (Entry<CustomMapData.Type<?>, CustomMapData.DirtyCounter> ex : dirtyData) {
                    saveDataToUpdateTag(ed, customDataTag, ex);
                    ((CustomMapData.DirtyCounter) ex.getValue()).clearDirty();
                }
                ep.moonlight$sendCustomMapDataTag(customDataTag);
            }
            if (updateDeco) {
                List<CustomMapDecoration> decorations = new ArrayList(ed.getCustomDecorations().values());
                decorations.addAll(extra);
                ep.moonlight$sendCustomDecorations(decorations);
            }
        }
        return packet;
    }

    @Unique
    private static <C extends CustomMapData.DirtyCounter, D extends CustomMapData<C>> void saveDataToUpdateTag(ExpandedMapData ed, CompoundTag customDataTag, Entry<CustomMapData.Type<?>, CustomMapData.DirtyCounter> e) {
        D d = (D) ed.getCustomData().get(((CustomMapData.Type) e.getKey()).id());
        C value = (C) e.getValue();
        d.saveToUpdateTag(customDataTag, value);
    }

    @Override
    public <H extends CustomMapData.DirtyCounter> void moonlight$setCustomDataDirty(CustomMapData.Type<?> type, Consumer<H> dirtySetter) {
        try {
            this.moonlight$concurrentLock.lock();
            CustomMapData.DirtyCounter t = (CustomMapData.DirtyCounter) this.moonlight$customDataDirty.get(type);
            dirtySetter.accept(t);
        } finally {
            this.moonlight$concurrentLock.unlock();
        }
    }

    @Override
    public void moonlight$setCustomMarkersDirty() {
        this.moonlight$customMarkersDirty = true;
    }

    @Inject(method = { "markColorsDirty" }, at = { @At("HEAD") })
    public void lockData(int x, int z, CallbackInfo ci) {
        this.moonlight$concurrentLock.lock();
    }

    @Inject(method = { "markColorsDirty" }, at = { @At("RETURN") })
    public void sanityCheck(int x, int z, CallbackInfo ci) {
        this.moonlight$concurrentLock.unlock();
    }
}