package net.minecraft.world.entity.ai.behavior;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;

public class TryLaySpawnOnWaterNearLand {

    public static BehaviorControl<LivingEntity> create(Block block0) {
        return BehaviorBuilder.create(p_259781_ -> p_259781_.group(p_259781_.absent(MemoryModuleType.ATTACK_TARGET), p_259781_.present(MemoryModuleType.WALK_TARGET), p_259781_.present(MemoryModuleType.IS_PREGNANT)).apply(p_259781_, (p_259765_, p_259602_, p_260037_) -> (p_269881_, p_269882_, p_269883_) -> {
            if (!p_269882_.m_20069_() && p_269882_.m_20096_()) {
                BlockPos $$5 = p_269882_.m_20183_().below();
                for (Direction $$6 : Direction.Plane.HORIZONTAL) {
                    BlockPos $$7 = $$5.relative($$6);
                    if (p_269881_.m_8055_($$7).m_60812_(p_269881_, $$7).getFaceShape(Direction.UP).isEmpty() && p_269881_.m_6425_($$7).is(Fluids.WATER)) {
                        BlockPos $$8 = $$7.above();
                        if (p_269881_.m_8055_($$8).m_60795_()) {
                            BlockState $$9 = block0.defaultBlockState();
                            p_269881_.m_7731_($$8, $$9, 3);
                            p_269881_.m_220407_(GameEvent.BLOCK_PLACE, $$8, GameEvent.Context.of(p_269882_, $$9));
                            p_269881_.m_6269_(null, p_269882_, SoundEvents.FROG_LAY_SPAWN, SoundSource.BLOCKS, 1.0F, 1.0F);
                            p_260037_.erase();
                            return true;
                        }
                    }
                }
                return true;
            } else {
                return false;
            }
        }));
    }
}