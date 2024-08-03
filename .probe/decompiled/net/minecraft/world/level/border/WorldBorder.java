package net.minecraft.world.level.border;

import com.google.common.collect.Lists;
import com.mojang.serialization.DynamicLike;
import java.util.List;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WorldBorder {

    public static final double MAX_SIZE = 5.999997E7F;

    public static final double MAX_CENTER_COORDINATE = 2.9999984E7;

    private final List<BorderChangeListener> listeners = Lists.newArrayList();

    private double damagePerBlock = 0.2;

    private double damageSafeZone = 5.0;

    private int warningTime = 15;

    private int warningBlocks = 5;

    private double centerX;

    private double centerZ;

    int absoluteMaxSize = 29999984;

    private WorldBorder.BorderExtent extent = new WorldBorder.StaticBorderExtent(5.999997E7F);

    public static final WorldBorder.Settings DEFAULT_SETTINGS = new WorldBorder.Settings(0.0, 0.0, 0.2, 5.0, 5, 15, 5.999997E7F, 0L, 0.0);

    public boolean isWithinBounds(BlockPos blockPos0) {
        return (double) (blockPos0.m_123341_() + 1) > this.getMinX() && (double) blockPos0.m_123341_() < this.getMaxX() && (double) (blockPos0.m_123343_() + 1) > this.getMinZ() && (double) blockPos0.m_123343_() < this.getMaxZ();
    }

    public boolean isWithinBounds(ChunkPos chunkPos0) {
        return (double) chunkPos0.getMaxBlockX() > this.getMinX() && (double) chunkPos0.getMinBlockX() < this.getMaxX() && (double) chunkPos0.getMaxBlockZ() > this.getMinZ() && (double) chunkPos0.getMinBlockZ() < this.getMaxZ();
    }

    public boolean isWithinBounds(double double0, double double1) {
        return double0 > this.getMinX() && double0 < this.getMaxX() && double1 > this.getMinZ() && double1 < this.getMaxZ();
    }

    public boolean isWithinBounds(double double0, double double1, double double2) {
        return double0 > this.getMinX() - double2 && double0 < this.getMaxX() + double2 && double1 > this.getMinZ() - double2 && double1 < this.getMaxZ() + double2;
    }

    public boolean isWithinBounds(AABB aABB0) {
        return aABB0.maxX > this.getMinX() && aABB0.minX < this.getMaxX() && aABB0.maxZ > this.getMinZ() && aABB0.minZ < this.getMaxZ();
    }

    public BlockPos clampToBounds(double double0, double double1, double double2) {
        return BlockPos.containing(Mth.clamp(double0, this.getMinX(), this.getMaxX()), double1, Mth.clamp(double2, this.getMinZ(), this.getMaxZ()));
    }

    public double getDistanceToBorder(Entity entity0) {
        return this.getDistanceToBorder(entity0.getX(), entity0.getZ());
    }

    public VoxelShape getCollisionShape() {
        return this.extent.getCollisionShape();
    }

    public double getDistanceToBorder(double double0, double double1) {
        double $$2 = double1 - this.getMinZ();
        double $$3 = this.getMaxZ() - double1;
        double $$4 = double0 - this.getMinX();
        double $$5 = this.getMaxX() - double0;
        double $$6 = Math.min($$4, $$5);
        $$6 = Math.min($$6, $$2);
        return Math.min($$6, $$3);
    }

    public boolean isInsideCloseToBorder(Entity entity0, AABB aABB1) {
        double $$2 = Math.max(Mth.absMax(aABB1.getXsize(), aABB1.getZsize()), 1.0);
        return this.getDistanceToBorder(entity0) < $$2 * 2.0 && this.isWithinBounds(entity0.getX(), entity0.getZ(), $$2);
    }

    public BorderStatus getStatus() {
        return this.extent.getStatus();
    }

    public double getMinX() {
        return this.extent.getMinX();
    }

    public double getMinZ() {
        return this.extent.getMinZ();
    }

    public double getMaxX() {
        return this.extent.getMaxX();
    }

    public double getMaxZ() {
        return this.extent.getMaxZ();
    }

    public double getCenterX() {
        return this.centerX;
    }

    public double getCenterZ() {
        return this.centerZ;
    }

    public void setCenter(double double0, double double1) {
        this.centerX = double0;
        this.centerZ = double1;
        this.extent.onCenterChange();
        for (BorderChangeListener $$2 : this.getListeners()) {
            $$2.onBorderCenterSet(this, double0, double1);
        }
    }

    public double getSize() {
        return this.extent.getSize();
    }

    public long getLerpRemainingTime() {
        return this.extent.getLerpRemainingTime();
    }

    public double getLerpTarget() {
        return this.extent.getLerpTarget();
    }

    public void setSize(double double0) {
        this.extent = new WorldBorder.StaticBorderExtent(double0);
        for (BorderChangeListener $$1 : this.getListeners()) {
            $$1.onBorderSizeSet(this, double0);
        }
    }

    public void lerpSizeBetween(double double0, double double1, long long2) {
        this.extent = (WorldBorder.BorderExtent) (double0 == double1 ? new WorldBorder.StaticBorderExtent(double1) : new WorldBorder.MovingBorderExtent(double0, double1, long2));
        for (BorderChangeListener $$3 : this.getListeners()) {
            $$3.onBorderSizeLerping(this, double0, double1, long2);
        }
    }

    protected List<BorderChangeListener> getListeners() {
        return Lists.newArrayList(this.listeners);
    }

    public void addListener(BorderChangeListener borderChangeListener0) {
        this.listeners.add(borderChangeListener0);
    }

    public void removeListener(BorderChangeListener borderChangeListener0) {
        this.listeners.remove(borderChangeListener0);
    }

    public void setAbsoluteMaxSize(int int0) {
        this.absoluteMaxSize = int0;
        this.extent.onAbsoluteMaxSizeChange();
    }

    public int getAbsoluteMaxSize() {
        return this.absoluteMaxSize;
    }

    public double getDamageSafeZone() {
        return this.damageSafeZone;
    }

    public void setDamageSafeZone(double double0) {
        this.damageSafeZone = double0;
        for (BorderChangeListener $$1 : this.getListeners()) {
            $$1.onBorderSetDamageSafeZOne(this, double0);
        }
    }

    public double getDamagePerBlock() {
        return this.damagePerBlock;
    }

    public void setDamagePerBlock(double double0) {
        this.damagePerBlock = double0;
        for (BorderChangeListener $$1 : this.getListeners()) {
            $$1.onBorderSetDamagePerBlock(this, double0);
        }
    }

    public double getLerpSpeed() {
        return this.extent.getLerpSpeed();
    }

    public int getWarningTime() {
        return this.warningTime;
    }

    public void setWarningTime(int int0) {
        this.warningTime = int0;
        for (BorderChangeListener $$1 : this.getListeners()) {
            $$1.onBorderSetWarningTime(this, int0);
        }
    }

    public int getWarningBlocks() {
        return this.warningBlocks;
    }

    public void setWarningBlocks(int int0) {
        this.warningBlocks = int0;
        for (BorderChangeListener $$1 : this.getListeners()) {
            $$1.onBorderSetWarningBlocks(this, int0);
        }
    }

    public void tick() {
        this.extent = this.extent.update();
    }

    public WorldBorder.Settings createSettings() {
        return new WorldBorder.Settings(this);
    }

    public void applySettings(WorldBorder.Settings worldBorderSettings0) {
        this.setCenter(worldBorderSettings0.getCenterX(), worldBorderSettings0.getCenterZ());
        this.setDamagePerBlock(worldBorderSettings0.getDamagePerBlock());
        this.setDamageSafeZone(worldBorderSettings0.getSafeZone());
        this.setWarningBlocks(worldBorderSettings0.getWarningBlocks());
        this.setWarningTime(worldBorderSettings0.getWarningTime());
        if (worldBorderSettings0.getSizeLerpTime() > 0L) {
            this.lerpSizeBetween(worldBorderSettings0.getSize(), worldBorderSettings0.getSizeLerpTarget(), worldBorderSettings0.getSizeLerpTime());
        } else {
            this.setSize(worldBorderSettings0.getSize());
        }
    }

    interface BorderExtent {

        double getMinX();

        double getMaxX();

        double getMinZ();

        double getMaxZ();

        double getSize();

        double getLerpSpeed();

        long getLerpRemainingTime();

        double getLerpTarget();

        BorderStatus getStatus();

        void onAbsoluteMaxSizeChange();

        void onCenterChange();

        WorldBorder.BorderExtent update();

        VoxelShape getCollisionShape();
    }

    class MovingBorderExtent implements WorldBorder.BorderExtent {

        private final double from;

        private final double to;

        private final long lerpEnd;

        private final long lerpBegin;

        private final double lerpDuration;

        MovingBorderExtent(double double0, double double1, long long2) {
            this.from = double0;
            this.to = double1;
            this.lerpDuration = (double) long2;
            this.lerpBegin = Util.getMillis();
            this.lerpEnd = this.lerpBegin + long2;
        }

        @Override
        public double getMinX() {
            return Mth.clamp(WorldBorder.this.getCenterX() - this.getSize() / 2.0, (double) (-WorldBorder.this.absoluteMaxSize), (double) WorldBorder.this.absoluteMaxSize);
        }

        @Override
        public double getMinZ() {
            return Mth.clamp(WorldBorder.this.getCenterZ() - this.getSize() / 2.0, (double) (-WorldBorder.this.absoluteMaxSize), (double) WorldBorder.this.absoluteMaxSize);
        }

        @Override
        public double getMaxX() {
            return Mth.clamp(WorldBorder.this.getCenterX() + this.getSize() / 2.0, (double) (-WorldBorder.this.absoluteMaxSize), (double) WorldBorder.this.absoluteMaxSize);
        }

        @Override
        public double getMaxZ() {
            return Mth.clamp(WorldBorder.this.getCenterZ() + this.getSize() / 2.0, (double) (-WorldBorder.this.absoluteMaxSize), (double) WorldBorder.this.absoluteMaxSize);
        }

        @Override
        public double getSize() {
            double $$0 = (double) (Util.getMillis() - this.lerpBegin) / this.lerpDuration;
            return $$0 < 1.0 ? Mth.lerp($$0, this.from, this.to) : this.to;
        }

        @Override
        public double getLerpSpeed() {
            return Math.abs(this.from - this.to) / (double) (this.lerpEnd - this.lerpBegin);
        }

        @Override
        public long getLerpRemainingTime() {
            return this.lerpEnd - Util.getMillis();
        }

        @Override
        public double getLerpTarget() {
            return this.to;
        }

        @Override
        public BorderStatus getStatus() {
            return this.to < this.from ? BorderStatus.SHRINKING : BorderStatus.GROWING;
        }

        @Override
        public void onCenterChange() {
        }

        @Override
        public void onAbsoluteMaxSizeChange() {
        }

        @Override
        public WorldBorder.BorderExtent update() {
            return (WorldBorder.BorderExtent) (this.getLerpRemainingTime() <= 0L ? WorldBorder.this.new StaticBorderExtent(this.to) : this);
        }

        @Override
        public VoxelShape getCollisionShape() {
            return Shapes.join(Shapes.INFINITY, Shapes.box(Math.floor(this.getMinX()), Double.NEGATIVE_INFINITY, Math.floor(this.getMinZ()), Math.ceil(this.getMaxX()), Double.POSITIVE_INFINITY, Math.ceil(this.getMaxZ())), BooleanOp.ONLY_FIRST);
        }
    }

    public static class Settings {

        private final double centerX;

        private final double centerZ;

        private final double damagePerBlock;

        private final double safeZone;

        private final int warningBlocks;

        private final int warningTime;

        private final double size;

        private final long sizeLerpTime;

        private final double sizeLerpTarget;

        Settings(double double0, double double1, double double2, double double3, int int4, int int5, double double6, long long7, double double8) {
            this.centerX = double0;
            this.centerZ = double1;
            this.damagePerBlock = double2;
            this.safeZone = double3;
            this.warningBlocks = int4;
            this.warningTime = int5;
            this.size = double6;
            this.sizeLerpTime = long7;
            this.sizeLerpTarget = double8;
        }

        Settings(WorldBorder worldBorder0) {
            this.centerX = worldBorder0.getCenterX();
            this.centerZ = worldBorder0.getCenterZ();
            this.damagePerBlock = worldBorder0.getDamagePerBlock();
            this.safeZone = worldBorder0.getDamageSafeZone();
            this.warningBlocks = worldBorder0.getWarningBlocks();
            this.warningTime = worldBorder0.getWarningTime();
            this.size = worldBorder0.getSize();
            this.sizeLerpTime = worldBorder0.getLerpRemainingTime();
            this.sizeLerpTarget = worldBorder0.getLerpTarget();
        }

        public double getCenterX() {
            return this.centerX;
        }

        public double getCenterZ() {
            return this.centerZ;
        }

        public double getDamagePerBlock() {
            return this.damagePerBlock;
        }

        public double getSafeZone() {
            return this.safeZone;
        }

        public int getWarningBlocks() {
            return this.warningBlocks;
        }

        public int getWarningTime() {
            return this.warningTime;
        }

        public double getSize() {
            return this.size;
        }

        public long getSizeLerpTime() {
            return this.sizeLerpTime;
        }

        public double getSizeLerpTarget() {
            return this.sizeLerpTarget;
        }

        public static WorldBorder.Settings read(DynamicLike<?> dynamicLike0, WorldBorder.Settings worldBorderSettings1) {
            double $$2 = Mth.clamp(dynamicLike0.get("BorderCenterX").asDouble(worldBorderSettings1.centerX), -2.9999984E7, 2.9999984E7);
            double $$3 = Mth.clamp(dynamicLike0.get("BorderCenterZ").asDouble(worldBorderSettings1.centerZ), -2.9999984E7, 2.9999984E7);
            double $$4 = dynamicLike0.get("BorderSize").asDouble(worldBorderSettings1.size);
            long $$5 = dynamicLike0.get("BorderSizeLerpTime").asLong(worldBorderSettings1.sizeLerpTime);
            double $$6 = dynamicLike0.get("BorderSizeLerpTarget").asDouble(worldBorderSettings1.sizeLerpTarget);
            double $$7 = dynamicLike0.get("BorderSafeZone").asDouble(worldBorderSettings1.safeZone);
            double $$8 = dynamicLike0.get("BorderDamagePerBlock").asDouble(worldBorderSettings1.damagePerBlock);
            int $$9 = dynamicLike0.get("BorderWarningBlocks").asInt(worldBorderSettings1.warningBlocks);
            int $$10 = dynamicLike0.get("BorderWarningTime").asInt(worldBorderSettings1.warningTime);
            return new WorldBorder.Settings($$2, $$3, $$8, $$7, $$9, $$10, $$4, $$5, $$6);
        }

        public void write(CompoundTag compoundTag0) {
            compoundTag0.putDouble("BorderCenterX", this.centerX);
            compoundTag0.putDouble("BorderCenterZ", this.centerZ);
            compoundTag0.putDouble("BorderSize", this.size);
            compoundTag0.putLong("BorderSizeLerpTime", this.sizeLerpTime);
            compoundTag0.putDouble("BorderSafeZone", this.safeZone);
            compoundTag0.putDouble("BorderDamagePerBlock", this.damagePerBlock);
            compoundTag0.putDouble("BorderSizeLerpTarget", this.sizeLerpTarget);
            compoundTag0.putDouble("BorderWarningBlocks", (double) this.warningBlocks);
            compoundTag0.putDouble("BorderWarningTime", (double) this.warningTime);
        }
    }

    class StaticBorderExtent implements WorldBorder.BorderExtent {

        private final double size;

        private double minX;

        private double minZ;

        private double maxX;

        private double maxZ;

        private VoxelShape shape;

        public StaticBorderExtent(double double0) {
            this.size = double0;
            this.updateBox();
        }

        @Override
        public double getMinX() {
            return this.minX;
        }

        @Override
        public double getMaxX() {
            return this.maxX;
        }

        @Override
        public double getMinZ() {
            return this.minZ;
        }

        @Override
        public double getMaxZ() {
            return this.maxZ;
        }

        @Override
        public double getSize() {
            return this.size;
        }

        @Override
        public BorderStatus getStatus() {
            return BorderStatus.STATIONARY;
        }

        @Override
        public double getLerpSpeed() {
            return 0.0;
        }

        @Override
        public long getLerpRemainingTime() {
            return 0L;
        }

        @Override
        public double getLerpTarget() {
            return this.size;
        }

        private void updateBox() {
            this.minX = Mth.clamp(WorldBorder.this.getCenterX() - this.size / 2.0, (double) (-WorldBorder.this.absoluteMaxSize), (double) WorldBorder.this.absoluteMaxSize);
            this.minZ = Mth.clamp(WorldBorder.this.getCenterZ() - this.size / 2.0, (double) (-WorldBorder.this.absoluteMaxSize), (double) WorldBorder.this.absoluteMaxSize);
            this.maxX = Mth.clamp(WorldBorder.this.getCenterX() + this.size / 2.0, (double) (-WorldBorder.this.absoluteMaxSize), (double) WorldBorder.this.absoluteMaxSize);
            this.maxZ = Mth.clamp(WorldBorder.this.getCenterZ() + this.size / 2.0, (double) (-WorldBorder.this.absoluteMaxSize), (double) WorldBorder.this.absoluteMaxSize);
            this.shape = Shapes.join(Shapes.INFINITY, Shapes.box(Math.floor(this.getMinX()), Double.NEGATIVE_INFINITY, Math.floor(this.getMinZ()), Math.ceil(this.getMaxX()), Double.POSITIVE_INFINITY, Math.ceil(this.getMaxZ())), BooleanOp.ONLY_FIRST);
        }

        @Override
        public void onAbsoluteMaxSizeChange() {
            this.updateBox();
        }

        @Override
        public void onCenterChange() {
            this.updateBox();
        }

        @Override
        public WorldBorder.BorderExtent update() {
            return this;
        }

        @Override
        public VoxelShape getCollisionShape() {
            return this.shape;
        }
    }
}