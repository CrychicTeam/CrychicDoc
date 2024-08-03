package io.github.lightman314.lightmanscurrency.api.misc.world;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class WorldPosition {

    public static final WorldPosition VOID = new WorldPosition(null, BlockPos.ZERO);

    private final ResourceKey<Level> dimension;

    private final BlockPos pos;

    @Nullable
    public final ResourceKey<Level> getDimension() {
        return this.dimension;
    }

    public final boolean sameDimension(@Nonnull WorldPosition other) {
        return !this.isVoid() && !other.isVoid() ? this.dimension.equals(other.dimension) : true;
    }

    public final boolean sameDimension(@Nonnull Level level) {
        return this.isVoid() ? true : this.dimension.equals(level.dimension());
    }

    public final boolean isVoid() {
        return this.dimension == null;
    }

    @Nonnull
    public BlockPos getPos() {
        return this.pos;
    }

    public final WorldArea getArea(int horizRadius, int vertSize, int vertOffset) {
        return WorldArea.of(this, horizRadius, vertSize, vertOffset);
    }

    private WorldPosition(@Nullable ResourceKey<Level> dimension, @Nonnull BlockPos pos) {
        this.dimension = dimension;
        this.pos = pos;
    }

    @Nonnull
    public static WorldPosition of(@Nullable ResourceKey<Level> dimension, @Nullable BlockPos pos) {
        return dimension == null ? VOID : new WorldPosition(dimension, pos != null ? pos : BlockPos.ZERO);
    }

    @Nonnull
    public static WorldPosition ofLevel(@Nullable Level level, @Nullable BlockPos pos) {
        return of(level != null ? level.dimension() : null, pos);
    }

    @Nonnull
    public static WorldPosition ofBE(@Nullable BlockEntity be) {
        return be == null ? VOID : ofLevel(be.getLevel(), be.getBlockPos());
    }

    @Nonnull
    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        if (this.dimension == null) {
            return tag;
        } else {
            tag.putString("Dimension", this.dimension.location().toString());
            tag.putInt("X", this.pos.m_123341_());
            tag.putInt("Y", this.pos.m_123342_());
            tag.putInt("Z", this.pos.m_123343_());
            return tag;
        }
    }

    @Nonnull
    public static WorldPosition load(@Nonnull CompoundTag tag) {
        if (tag.contains("Dimension")) {
            ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(tag.getString("Dimension")));
            BlockPos pos = new BlockPos(tag.getInt("X"), tag.getInt("Y"), tag.getInt("Z"));
            return of(dimension, pos);
        } else {
            return VOID;
        }
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof WorldPosition other) {
            if (other == this) {
                return true;
            } else if (other.isVoid() != this.isVoid()) {
                return false;
            } else {
                return this.isVoid() ? true : other.dimension.equals(this.dimension) && other.pos.equals(this.pos);
            }
        } else {
            return false;
        }
    }
}