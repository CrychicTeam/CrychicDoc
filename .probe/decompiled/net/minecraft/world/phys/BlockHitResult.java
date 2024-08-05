package net.minecraft.world.phys;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class BlockHitResult extends HitResult {

    private final Direction direction;

    private final BlockPos blockPos;

    private final boolean miss;

    private final boolean inside;

    public static BlockHitResult miss(Vec3 vec0, Direction direction1, BlockPos blockPos2) {
        return new BlockHitResult(true, vec0, direction1, blockPos2, false);
    }

    public BlockHitResult(Vec3 vec0, Direction direction1, BlockPos blockPos2, boolean boolean3) {
        this(false, vec0, direction1, blockPos2, boolean3);
    }

    private BlockHitResult(boolean boolean0, Vec3 vec1, Direction direction2, BlockPos blockPos3, boolean boolean4) {
        super(vec1);
        this.miss = boolean0;
        this.direction = direction2;
        this.blockPos = blockPos3;
        this.inside = boolean4;
    }

    public BlockHitResult withDirection(Direction direction0) {
        return new BlockHitResult(this.miss, this.f_82445_, direction0, this.blockPos, this.inside);
    }

    public BlockHitResult withPosition(BlockPos blockPos0) {
        return new BlockHitResult(this.miss, this.f_82445_, this.direction, blockPos0, this.inside);
    }

    public BlockPos getBlockPos() {
        return this.blockPos;
    }

    public Direction getDirection() {
        return this.direction;
    }

    @Override
    public HitResult.Type getType() {
        return this.miss ? HitResult.Type.MISS : HitResult.Type.BLOCK;
    }

    public boolean isInside() {
        return this.inside;
    }
}