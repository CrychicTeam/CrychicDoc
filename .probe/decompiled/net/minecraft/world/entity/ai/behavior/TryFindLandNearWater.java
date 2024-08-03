package net.minecraft.world.entity.ai.behavior;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.apache.commons.lang3.mutable.MutableLong;

public class TryFindLandNearWater {

    public static BehaviorControl<PathfinderMob> create(int int0, float float1) {
        MutableLong $$2 = new MutableLong(0L);
        return BehaviorBuilder.create(p_260348_ -> p_260348_.group(p_260348_.absent(MemoryModuleType.ATTACK_TARGET), p_260348_.absent(MemoryModuleType.WALK_TARGET), p_260348_.registered(MemoryModuleType.LOOK_TARGET)).apply(p_260348_, (p_259029_, p_259100_, p_259367_) -> (p_259876_, p_259531_, p_259771_) -> {
            if (p_259876_.m_6425_(p_259531_.m_20183_()).is(FluidTags.WATER)) {
                return false;
            } else if (p_259771_ < $$2.getValue()) {
                $$2.setValue(p_259771_ + 40L);
                return true;
            } else {
                CollisionContext $$8 = CollisionContext.of(p_259531_);
                BlockPos $$9 = p_259531_.m_20183_();
                BlockPos.MutableBlockPos $$10 = new BlockPos.MutableBlockPos();
                label45: for (BlockPos $$11 : BlockPos.withinManhattan($$9, int0, int0, int0)) {
                    if (($$11.m_123341_() != $$9.m_123341_() || $$11.m_123343_() != $$9.m_123343_()) && p_259876_.m_8055_($$11).m_60742_(p_259876_, $$11, $$8).isEmpty() && !p_259876_.m_8055_($$10.setWithOffset($$11, Direction.DOWN)).m_60742_(p_259876_, $$11, $$8).isEmpty()) {
                        for (Direction $$12 : Direction.Plane.HORIZONTAL) {
                            $$10.setWithOffset($$11, $$12);
                            if (p_259876_.m_8055_($$10).m_60795_() && p_259876_.m_8055_($$10.move(Direction.DOWN)).m_60713_(Blocks.WATER)) {
                                p_259367_.set(new BlockPosTracker($$11));
                                p_259100_.set(new WalkTarget(new BlockPosTracker($$11), float1, 0));
                                break label45;
                            }
                        }
                    }
                }
                $$2.setValue(p_259771_ + 40L);
                return true;
            }
        }));
    }
}