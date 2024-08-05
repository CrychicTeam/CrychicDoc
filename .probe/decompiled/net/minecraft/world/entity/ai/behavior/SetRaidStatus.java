package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.schedule.Activity;

public class SetRaidStatus {

    public static BehaviorControl<LivingEntity> create() {
        return BehaviorBuilder.create(p_259382_ -> p_259382_.point((p_260026_, p_260271_, p_259518_) -> {
            if (p_260026_.f_46441_.nextInt(20) != 0) {
                return false;
            } else {
                Brain<?> $$3 = p_260271_.getBrain();
                Raid $$4 = p_260026_.getRaidAt(p_260271_.m_20183_());
                if ($$4 != null) {
                    if ($$4.hasFirstWaveSpawned() && !$$4.isBetweenWaves()) {
                        $$3.setDefaultActivity(Activity.RAID);
                        $$3.setActiveActivityIfPossible(Activity.RAID);
                    } else {
                        $$3.setDefaultActivity(Activity.PRE_RAID);
                        $$3.setActiveActivityIfPossible(Activity.PRE_RAID);
                    }
                }
                return true;
            }
        }));
    }
}