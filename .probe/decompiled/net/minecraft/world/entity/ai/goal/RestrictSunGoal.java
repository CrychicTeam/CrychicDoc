package net.minecraft.world.entity.ai.goal;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.util.GoalUtils;

public class RestrictSunGoal extends Goal {

    private final PathfinderMob mob;

    public RestrictSunGoal(PathfinderMob pathfinderMob0) {
        this.mob = pathfinderMob0;
    }

    @Override
    public boolean canUse() {
        return this.mob.m_9236_().isDay() && this.mob.m_6844_(EquipmentSlot.HEAD).isEmpty() && GoalUtils.hasGroundPathNavigation(this.mob);
    }

    @Override
    public void start() {
        ((GroundPathNavigation) this.mob.m_21573_()).setAvoidSun(true);
    }

    @Override
    public void stop() {
        if (GoalUtils.hasGroundPathNavigation(this.mob)) {
            ((GroundPathNavigation) this.mob.m_21573_()).setAvoidSun(false);
        }
    }
}