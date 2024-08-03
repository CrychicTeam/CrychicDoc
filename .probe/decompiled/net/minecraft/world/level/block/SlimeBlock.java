package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class SlimeBlock extends HalfTransparentBlock {

    public SlimeBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
    }

    @Override
    public void fallOn(Level level0, BlockState blockState1, BlockPos blockPos2, Entity entity3, float float4) {
        if (entity3.isSuppressingBounce()) {
            super.m_142072_(level0, blockState1, blockPos2, entity3, float4);
        } else {
            entity3.causeFallDamage(float4, 0.0F, level0.damageSources().fall());
        }
    }

    @Override
    public void updateEntityAfterFallOn(BlockGetter blockGetter0, Entity entity1) {
        if (entity1.isSuppressingBounce()) {
            super.m_5548_(blockGetter0, entity1);
        } else {
            this.bounceUp(entity1);
        }
    }

    private void bounceUp(Entity entity0) {
        Vec3 $$1 = entity0.getDeltaMovement();
        if ($$1.y < 0.0) {
            double $$2 = entity0 instanceof LivingEntity ? 1.0 : 0.8;
            entity0.setDeltaMovement($$1.x, -$$1.y * $$2, $$1.z);
        }
    }

    @Override
    public void stepOn(Level level0, BlockPos blockPos1, BlockState blockState2, Entity entity3) {
        double $$4 = Math.abs(entity3.getDeltaMovement().y);
        if ($$4 < 0.1 && !entity3.isSteppingCarefully()) {
            double $$5 = 0.4 + $$4 * 0.2;
            entity3.setDeltaMovement(entity3.getDeltaMovement().multiply($$5, 1.0, $$5));
        }
        super.m_141947_(level0, blockPos1, blockState2, entity3);
    }
}