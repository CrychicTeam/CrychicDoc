package net.minecraft.world.entity.ai.behavior;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.apache.commons.lang3.mutable.MutableLong;

public class TryFindLand {

    private static final int COOLDOWN_TICKS = 60;

    public static BehaviorControl<PathfinderMob> create(int int0, float float1) {
        MutableLong $$2 = new MutableLong(0L);
        return BehaviorBuilder.create(p_259851_ -> p_259851_.group(p_259851_.absent(MemoryModuleType.ATTACK_TARGET), p_259851_.absent(MemoryModuleType.WALK_TARGET), p_259851_.registered(MemoryModuleType.LOOK_TARGET)).apply(p_259851_, (p_259686_, p_259882_, p_259123_) -> (p_260032_, p_260019_, p_259854_) -> {
            if (!p_260032_.m_6425_(p_260019_.m_20183_()).is(FluidTags.WATER)) {
                return false;
            } else if (p_259854_ < $$2.getValue()) {
                $$2.setValue(p_259854_ + 60L);
                return true;
            } else {
                BlockPos $$8 = p_260019_.m_20183_();
                BlockPos.MutableBlockPos $$9 = new BlockPos.MutableBlockPos();
                CollisionContext $$10 = CollisionContext.of(p_260019_);
                for (BlockPos $$11 : BlockPos.withinManhattan($$8, int0, int0, int0)) {
                    if ($$11.m_123341_() != $$8.m_123341_() || $$11.m_123343_() != $$8.m_123343_()) {
                        BlockState $$12 = p_260032_.m_8055_($$11);
                        BlockState $$13 = p_260032_.m_8055_($$9.setWithOffset($$11, Direction.DOWN));
                        if (!$$12.m_60713_(Blocks.WATER) && p_260032_.m_6425_($$11).isEmpty() && $$12.m_60742_(p_260032_, $$11, $$10).isEmpty() && $$13.m_60783_(p_260032_, $$9, Direction.UP)) {
                            BlockPos $$14 = $$11.immutable();
                            p_259123_.set(new BlockPosTracker($$14));
                            p_259882_.set(new WalkTarget(new BlockPosTracker($$14), float1, 1));
                            break;
                        }
                    }
                }
                $$2.setValue(p_259854_ + 60L);
                return true;
            }
        }));
    }
}