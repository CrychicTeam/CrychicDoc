package me.jellysquid.mods.lithium.mixin.math.fast_blockpos;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin({ BlockPos.class })
public abstract class BlockPosMixin extends Vec3i {

    public BlockPosMixin(int x, int y, int z) {
        super(x, y, z);
    }

    @Overwrite
    public BlockPos above() {
        return new BlockPos(this.m_123341_(), this.m_123342_() + 1, this.m_123343_());
    }

    @Overwrite
    public BlockPos above(int distance) {
        return new BlockPos(this.m_123341_(), this.m_123342_() + distance, this.m_123343_());
    }

    @Overwrite
    public BlockPos below() {
        return new BlockPos(this.m_123341_(), this.m_123342_() - 1, this.m_123343_());
    }

    @Overwrite
    public BlockPos below(int distance) {
        return new BlockPos(this.m_123341_(), this.m_123342_() - distance, this.m_123343_());
    }

    @Overwrite
    public BlockPos north() {
        return new BlockPos(this.m_123341_(), this.m_123342_(), this.m_123343_() - 1);
    }

    @Overwrite
    public BlockPos north(int distance) {
        return new BlockPos(this.m_123341_(), this.m_123342_(), this.m_123343_() - distance);
    }

    @Overwrite
    public BlockPos south() {
        return new BlockPos(this.m_123341_(), this.m_123342_(), this.m_123343_() + 1);
    }

    @Overwrite
    public BlockPos south(int distance) {
        return new BlockPos(this.m_123341_(), this.m_123342_(), this.m_123343_() + distance);
    }

    @Overwrite
    public BlockPos west() {
        return new BlockPos(this.m_123341_() - 1, this.m_123342_(), this.m_123343_());
    }

    @Overwrite
    public BlockPos west(int distance) {
        return new BlockPos(this.m_123341_() - distance, this.m_123342_(), this.m_123343_());
    }

    @Overwrite
    public BlockPos east() {
        return new BlockPos(this.m_123341_() + 1, this.m_123342_(), this.m_123343_());
    }

    @Overwrite
    public BlockPos east(int distance) {
        return new BlockPos(this.m_123341_() + distance, this.m_123342_(), this.m_123343_());
    }
}