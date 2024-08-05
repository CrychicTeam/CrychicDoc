package net.minecraft.world.entity.ai.behavior;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.phys.Vec3;

public class SetWalkTargetFromBlockMemory {

    public static OneShot<Villager> create(MemoryModuleType<GlobalPos> memoryModuleTypeGlobalPos0, float float1, int int2, int int3, int int4) {
        return BehaviorBuilder.create(p_258717_ -> p_258717_.group(p_258717_.registered(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE), p_258717_.absent(MemoryModuleType.WALK_TARGET), p_258717_.present(memoryModuleTypeGlobalPos0)).apply(p_258717_, (p_258709_, p_258710_, p_258711_) -> (p_275056_, p_275057_, p_275058_) -> {
            GlobalPos $$12 = p_258717_.get(p_258711_);
            Optional<Long> $$13 = p_258717_.tryGet(p_258709_);
            if ($$12.dimension() == p_275056_.m_46472_() && (!$$13.isPresent() || p_275056_.m_46467_() - (Long) $$13.get() <= (long) int4)) {
                if ($$12.pos().m_123333_(p_275057_.m_20183_()) > int3) {
                    Vec3 $$14 = null;
                    int $$15 = 0;
                    int $$16 = 1000;
                    while ($$14 == null || BlockPos.containing($$14).m_123333_(p_275057_.m_20183_()) > int3) {
                        $$14 = DefaultRandomPos.getPosTowards(p_275057_, 15, 7, Vec3.atBottomCenterOf($$12.pos()), (float) (Math.PI / 2));
                        if (++$$15 == 1000) {
                            p_275057_.releasePoi(memoryModuleTypeGlobalPos0);
                            p_258711_.erase();
                            p_258709_.set(p_275058_);
                            return true;
                        }
                    }
                    p_258710_.set(new WalkTarget($$14, float1, int2));
                } else if ($$12.pos().m_123333_(p_275057_.m_20183_()) > int2) {
                    p_258710_.set(new WalkTarget($$12.pos(), float1, int2));
                }
            } else {
                p_275057_.releasePoi(memoryModuleTypeGlobalPos0);
                p_258711_.erase();
                p_258709_.set(p_275058_);
            }
            return true;
        }));
    }
}