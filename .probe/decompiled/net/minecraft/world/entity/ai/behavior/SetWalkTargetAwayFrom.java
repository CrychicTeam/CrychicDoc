package net.minecraft.world.entity.ai.behavior;

import java.util.Optional;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.phys.Vec3;

public class SetWalkTargetAwayFrom {

    public static BehaviorControl<PathfinderMob> pos(MemoryModuleType<BlockPos> memoryModuleTypeBlockPos0, float float1, int int2, boolean boolean3) {
        return create(memoryModuleTypeBlockPos0, float1, int2, boolean3, Vec3::m_82539_);
    }

    public static OneShot<PathfinderMob> entity(MemoryModuleType<? extends Entity> memoryModuleTypeExtendsEntity0, float float1, int int2, boolean boolean3) {
        return create(memoryModuleTypeExtendsEntity0, float1, int2, boolean3, Entity::m_20182_);
    }

    private static <T> OneShot<PathfinderMob> create(MemoryModuleType<T> memoryModuleTypeT0, float float1, int int2, boolean boolean3, Function<T, Vec3> functionTVec4) {
        return BehaviorBuilder.create(p_259292_ -> p_259292_.group(p_259292_.registered(MemoryModuleType.WALK_TARGET), p_259292_.present(memoryModuleTypeT0)).apply(p_259292_, (p_260063_, p_260053_) -> (p_259973_, p_259323_, p_259275_) -> {
            Optional<WalkTarget> $$10 = p_259292_.tryGet(p_260063_);
            if ($$10.isPresent() && !boolean3) {
                return false;
            } else {
                Vec3 $$11 = p_259323_.m_20182_();
                Vec3 $$12 = (Vec3) functionTVec4.apply(p_259292_.get(p_260053_));
                if (!$$11.closerThan($$12, (double) int2)) {
                    return false;
                } else {
                    if ($$10.isPresent() && ((WalkTarget) $$10.get()).getSpeedModifier() == float1) {
                        Vec3 $$13 = ((WalkTarget) $$10.get()).getTarget().currentPosition().subtract($$11);
                        Vec3 $$14 = $$12.subtract($$11);
                        if ($$13.dot($$14) < 0.0) {
                            return false;
                        }
                    }
                    for (int $$15 = 0; $$15 < 10; $$15++) {
                        Vec3 $$16 = LandRandomPos.getPosAway(p_259323_, 16, 7, $$12);
                        if ($$16 != null) {
                            p_260063_.set(new WalkTarget($$16, float1, 0));
                            break;
                        }
                    }
                    return true;
                }
            }
        }));
    }
}