package net.minecraft.world.entity.ai.behavior;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.phys.Vec3;

public class GoToClosestVillage {

    public static BehaviorControl<Villager> create(float float0, int int1) {
        return BehaviorBuilder.create(p_258357_ -> p_258357_.group(p_258357_.absent(MemoryModuleType.WALK_TARGET)).apply(p_258357_, p_258366_ -> (p_274970_, p_274971_, p_274972_) -> {
            if (p_274970_.isVillage(p_274971_.m_20183_())) {
                return false;
            } else {
                PoiManager $$6 = p_274970_.getPoiManager();
                int $$7 = $$6.sectionsToVillage(SectionPos.of(p_274971_.m_20183_()));
                Vec3 $$8 = null;
                for (int $$9 = 0; $$9 < 5; $$9++) {
                    Vec3 $$10 = LandRandomPos.getPos(p_274971_, 15, 7, p_147554_ -> (double) (-$$6.sectionsToVillage(SectionPos.of(p_147554_))));
                    if ($$10 != null) {
                        int $$11 = $$6.sectionsToVillage(SectionPos.of(BlockPos.containing($$10)));
                        if ($$11 < $$7) {
                            $$8 = $$10;
                            break;
                        }
                        if ($$11 == $$7) {
                            $$8 = $$10;
                        }
                    }
                }
                if ($$8 != null) {
                    p_258366_.set(new WalkTarget($$8, float0, int1));
                }
                return true;
            }
        }));
    }
}