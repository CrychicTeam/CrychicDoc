package net.minecraft.world.entity.ai.behavior;

import java.util.Optional;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.mutable.MutableLong;

public class StrollAroundPoi {

    private static final int MIN_TIME_BETWEEN_STROLLS = 180;

    private static final int STROLL_MAX_XZ_DIST = 8;

    private static final int STROLL_MAX_Y_DIST = 6;

    public static OneShot<PathfinderMob> create(MemoryModuleType<GlobalPos> memoryModuleTypeGlobalPos0, float float1, int int2) {
        MutableLong $$3 = new MutableLong(0L);
        return BehaviorBuilder.create(p_258827_ -> p_258827_.group(p_258827_.registered(MemoryModuleType.WALK_TARGET), p_258827_.present(memoryModuleTypeGlobalPos0)).apply(p_258827_, (p_258821_, p_258822_) -> (p_258834_, p_258835_, p_258836_) -> {
            GlobalPos $$9 = p_258827_.get(p_258822_);
            if (p_258834_.m_46472_() != $$9.dimension() || !$$9.pos().m_203195_(p_258835_.m_20182_(), (double) int2)) {
                return false;
            } else if (p_258836_ <= $$3.getValue()) {
                return true;
            } else {
                Optional<Vec3> $$10 = Optional.ofNullable(LandRandomPos.getPos(p_258835_, 8, 6));
                p_258821_.setOrErase($$10.map(p_258816_ -> new WalkTarget(p_258816_, float1, 1)));
                $$3.setValue(p_258836_ + 180L);
                return true;
            }
        }));
    }
}