package net.mehvahdjukaar.supplementaries.api;

import net.mehvahdjukaar.supplementaries.common.misc.mob_container.MobContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

public abstract class CapturedMobInstance<T extends Entity> {

    protected final T entity;

    protected final float containerWidth;

    protected final float containerHeight;

    protected CapturedMobInstance(T entity, float width, float height) {
        this.entity = entity;
        this.containerWidth = width;
        this.containerHeight = height;
    }

    @Nullable
    public T getEntityForRenderer() {
        return this.entity;
    }

    public void containerTick(Level world, BlockPos pos, float entityScale, CompoundTag entityData) {
    }

    public InteractionResult onPlayerInteract(Level world, BlockPos pos, Player player, InteractionHand hand, CompoundTag entityData) {
        return InteractionResult.PASS;
    }

    public void onContainerWaterlogged(boolean waterlogged) {
        if (this.entity instanceof WaterAnimal && this.entity.isInWater() != waterlogged) {
            this.entity.wasTouchingWater = waterlogged;
            Pair<Float, Float> dim = MobContainer.calculateMobDimensionsForContainer(this.entity, this.containerWidth, this.containerHeight, waterlogged);
            double py = (double) ((Float) dim.getRight()).floatValue() + 1.0E-4;
            this.entity.setPos(this.entity.getX(), py, this.entity.getZ());
            this.entity.yOld = py;
        }
    }

    public static class Default<T extends Entity> extends CapturedMobInstance<T> {

        public Default(T entity, float width, float height) {
            super(entity, width, height);
        }
    }
}