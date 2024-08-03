package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FlowerBlock extends BushBlock implements SuspiciousEffectHolder {

    protected static final float AABB_OFFSET = 3.0F;

    protected static final VoxelShape SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 10.0, 11.0);

    private final MobEffect suspiciousStewEffect;

    private final int effectDuration;

    public FlowerBlock(MobEffect mobEffect0, int int1, BlockBehaviour.Properties blockBehaviourProperties2) {
        super(blockBehaviourProperties2);
        this.suspiciousStewEffect = mobEffect0;
        if (mobEffect0.isInstantenous()) {
            this.effectDuration = int1;
        } else {
            this.effectDuration = int1 * 20;
        }
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        Vec3 $$4 = blockState0.m_60824_(blockGetter1, blockPos2);
        return SHAPE.move($$4.x, $$4.y, $$4.z);
    }

    @Override
    public MobEffect getSuspiciousEffect() {
        return this.suspiciousStewEffect;
    }

    @Override
    public int getEffectDuration() {
        return this.effectDuration;
    }
}