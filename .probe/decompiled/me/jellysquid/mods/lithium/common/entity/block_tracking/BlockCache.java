package me.jellysquid.mods.lithium.common.entity.block_tracking;

import it.unimi.dsi.fastutil.objects.Object2DoubleArrayMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import me.jellysquid.mods.lithium.common.block.BlockStateFlags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fluids.FluidType;

public final class BlockCache {

    private static final int MIN_DELAY = 300;

    private int initDelay;

    private AABB trackedPos;

    private SectionedBlockChangeTracker tracker = null;

    private long trackingSince;

    private boolean canSkipSupportingBlockSearch;

    private boolean canSkipBlockTouching;

    private byte cachedTouchingFireLava;

    private byte cachedIsSuffocating;

    private final Object2DoubleArrayMap<FluidType> fluidType2FluidHeightMap;

    public BlockCache() {
        this.trackedPos = null;
        this.initDelay = 0;
        this.fluidType2FluidHeightMap = new Object2DoubleArrayMap(2);
    }

    public boolean isTracking() {
        return this.tracker != null;
    }

    public void initTracking(Entity entity) {
        if (this.isTracking()) {
            throw new IllegalStateException("Cannot init cache that is already initialized!");
        } else {
            this.tracker = SectionedBlockChangeTracker.registerAt(entity.level(), entity.getBoundingBox(), BlockStateFlags.ANY);
            this.initDelay = 0;
            this.resetCachedInfo();
        }
    }

    public void updateCache(Entity entity) {
        if (!this.isTracking() && this.initDelay < 300) {
            this.initDelay++;
        } else {
            AABB boundingBox = entity.getBoundingBox();
            if (boundingBox.equals(this.trackedPos)) {
                if (!this.isTracking()) {
                    this.initTracking(entity);
                } else if (!this.tracker.isUnchangedSince(this.trackingSince)) {
                    this.resetCachedInfo();
                }
            } else {
                if (this.isTracking() && !this.tracker.matchesMovedBox(boundingBox)) {
                    this.tracker.unregister();
                    this.tracker = null;
                }
                this.resetTrackedPos(boundingBox);
            }
        }
    }

    public void resetTrackedPos(AABB boundingBox) {
        this.trackedPos = boundingBox;
        this.initDelay = 0;
        this.resetCachedInfo();
    }

    public void resetCachedInfo() {
        this.trackingSince = !this.isTracking() ? Long.MIN_VALUE : this.tracker.getWorldTime();
        this.canSkipSupportingBlockSearch = false;
        this.cachedIsSuffocating = -1;
        this.cachedTouchingFireLava = -1;
        this.canSkipBlockTouching = false;
        this.fluidType2FluidHeightMap.clear();
    }

    public void remove() {
        if (this.tracker != null) {
            this.tracker.unregister();
        }
    }

    public boolean canSkipBlockTouching() {
        return this.isTracking() && this.canSkipBlockTouching;
    }

    public void setCanSkipBlockTouching(boolean value) {
        this.canSkipBlockTouching = value;
    }

    public double getStationaryFluidHeightOrDefault(FluidType fluid, double defaultValue) {
        return this.isTracking() ? this.fluidType2FluidHeightMap.getOrDefault(fluid, defaultValue) : defaultValue;
    }

    public void setCachedFluidHeight(FluidType fluid, double fluidHeight) {
        this.fluidType2FluidHeightMap.put(fluid, fluidHeight);
    }

    public Object2DoubleMap<FluidType> getCachedFluidHeightMap() {
        return this.isTracking() ? this.fluidType2FluidHeightMap : null;
    }

    public byte getIsTouchingFireLava() {
        return this.isTracking() ? this.cachedTouchingFireLava : -1;
    }

    public void setCachedTouchingFireLava(boolean b) {
        this.cachedTouchingFireLava = (byte) (b ? 1 : 0);
    }

    public byte getIsSuffocating() {
        return this.isTracking() ? this.cachedIsSuffocating : -1;
    }

    public void setCachedIsSuffocating(boolean b) {
        this.cachedIsSuffocating = (byte) (b ? 1 : 0);
    }

    public boolean canSkipSupportingBlockSearch() {
        return this.isTracking() && this.canSkipSupportingBlockSearch;
    }

    public void setCanSkipSupportingBlockSearch(boolean canSkip) {
        this.canSkipSupportingBlockSearch = canSkip;
    }
}