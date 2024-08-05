package net.minecraft.world.entity.ai.memory;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.behavior.PositionTracker;
import net.minecraft.world.phys.Vec3;

public class WalkTarget {

    private final PositionTracker target;

    private final float speedModifier;

    private final int closeEnoughDist;

    public WalkTarget(BlockPos blockPos0, float float1, int int2) {
        this(new BlockPosTracker(blockPos0), float1, int2);
    }

    public WalkTarget(Vec3 vec0, float float1, int int2) {
        this(new BlockPosTracker(BlockPos.containing(vec0)), float1, int2);
    }

    public WalkTarget(Entity entity0, float float1, int int2) {
        this(new EntityTracker(entity0, false), float1, int2);
    }

    public WalkTarget(PositionTracker positionTracker0, float float1, int int2) {
        this.target = positionTracker0;
        this.speedModifier = float1;
        this.closeEnoughDist = int2;
    }

    public PositionTracker getTarget() {
        return this.target;
    }

    public float getSpeedModifier() {
        return this.speedModifier;
    }

    public int getCloseEnoughDist() {
        return this.closeEnoughDist;
    }
}