package org.violetmoon.quark.content.tweaks.ai;

import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import org.violetmoon.quark.content.tweaks.module.PatTheDogsModule;

public class WantLoveGoal extends Goal {

    private static final String PET_TIME = "quark:PetTime";

    private final TamableAnimal creature;

    private LivingEntity leapTarget;

    public final float leapUpMotion;

    public static void setPetTime(TamableAnimal entity) {
        entity.getPersistentData().putLong("quark:PetTime", entity.m_9236_().getGameTime());
    }

    public static boolean canPet(TamableAnimal entity) {
        return timeSinceLastPet(entity) > 20L;
    }

    public static boolean needsPets(TamableAnimal entity) {
        return PatTheDogsModule.dogsWantLove <= 0 ? false : timeSinceLastPet(entity) > (long) PatTheDogsModule.dogsWantLove;
    }

    public static long timeSinceLastPet(TamableAnimal entity) {
        if (!entity.isTame()) {
            return 0L;
        } else {
            long lastPetAt = entity.getPersistentData().getLong("quark:PetTime");
            return entity.m_9236_().getGameTime() - lastPetAt;
        }
    }

    public WantLoveGoal(TamableAnimal creature, float leapMotion) {
        this.creature = creature;
        this.leapUpMotion = leapMotion;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP, Goal.Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        if (!needsPets(this.creature)) {
            return false;
        } else {
            this.leapTarget = this.creature.m_269323_();
            if (this.leapTarget == null) {
                return false;
            } else {
                double distanceToTarget = this.creature.m_20280_(this.leapTarget);
                return 4.0 <= distanceToTarget && distanceToTarget <= 16.0 && this.creature.m_20096_() && this.creature.m_217043_().nextInt(5) == 0;
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !needsPets(this.creature) ? false : !this.creature.m_20096_();
    }

    @Override
    public void start() {
        Vec3 leapPos = this.leapTarget.m_20182_();
        Vec3 creaturePos = this.creature.m_20182_();
        double dX = leapPos.x - creaturePos.x;
        double dZ = leapPos.z - creaturePos.z;
        float leapMagnitude = (float) Math.sqrt(dX * dX + dZ * dZ);
        Vec3 motion = this.creature.m_20184_();
        if ((double) leapMagnitude >= 1.0E-4) {
            motion = motion.add(dX / (double) leapMagnitude * 0.4 + motion.x * 0.2, 0.0, dZ / (double) leapMagnitude * 0.4 + motion.z * 0.2);
        }
        motion = motion.add(0.0, (double) this.leapUpMotion, 0.0);
        this.creature.m_20256_(motion);
    }
}