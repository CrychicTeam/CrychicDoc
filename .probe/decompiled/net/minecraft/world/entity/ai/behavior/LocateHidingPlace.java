package net.minecraft.world.entity.ai.behavior;

import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;

public class LocateHidingPlace {

    public static OneShot<LivingEntity> create(int int0, float float1, int int2) {
        return BehaviorBuilder.create(p_258505_ -> p_258505_.group(p_258505_.absent(MemoryModuleType.WALK_TARGET), p_258505_.registered(MemoryModuleType.HOME), p_258505_.registered(MemoryModuleType.HIDING_PLACE), p_258505_.registered(MemoryModuleType.PATH), p_258505_.registered(MemoryModuleType.LOOK_TARGET), p_258505_.registered(MemoryModuleType.BREED_TARGET), p_258505_.registered(MemoryModuleType.INTERACTION_TARGET)).apply(p_258505_, (p_258484_, p_258485_, p_258486_, p_258487_, p_258488_, p_258489_, p_258490_) -> (p_289346_, p_289347_, p_289348_) -> {
            p_289346_.getPoiManager().find(p_217258_ -> p_217258_.is(PoiTypes.HOME), p_23425_ -> true, p_289347_.m_20183_(), int2 + 1, PoiManager.Occupancy.ANY).filter(p_289334_ -> p_289334_.m_203195_(p_289347_.m_20182_(), (double) int2)).or(() -> p_289346_.getPoiManager().getRandom(p_217256_ -> p_217256_.is(PoiTypes.HOME), p_23421_ -> true, PoiManager.Occupancy.ANY, p_289347_.m_20183_(), int0, p_289347_.getRandom())).or(() -> p_258505_.tryGet(p_258485_).map(GlobalPos::m_122646_)).ifPresent(p_289359_ -> {
                p_258487_.erase();
                p_258488_.erase();
                p_258489_.erase();
                p_258490_.erase();
                p_258486_.set(GlobalPos.of(p_289346_.m_46472_(), p_289359_));
                if (!p_289359_.m_203195_(p_289347_.m_20182_(), (double) int2)) {
                    p_258484_.set(new WalkTarget(p_289359_, float1, int2));
                }
            });
            return true;
        }));
    }
}