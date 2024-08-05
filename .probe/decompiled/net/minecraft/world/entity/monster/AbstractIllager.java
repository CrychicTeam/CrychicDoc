package net.minecraft.world.entity.monster;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;

public abstract class AbstractIllager extends Raider {

    protected AbstractIllager(EntityType<? extends AbstractIllager> entityTypeExtendsAbstractIllager0, Level level1) {
        super(entityTypeExtendsAbstractIllager0, level1);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
    }

    @Override
    public MobType getMobType() {
        return MobType.ILLAGER;
    }

    public AbstractIllager.IllagerArmPose getArmPose() {
        return AbstractIllager.IllagerArmPose.CROSSED;
    }

    @Override
    public boolean canAttack(LivingEntity livingEntity0) {
        return livingEntity0 instanceof AbstractVillager && livingEntity0.isBaby() ? false : super.m_6779_(livingEntity0);
    }

    public static enum IllagerArmPose {

        CROSSED,
        ATTACKING,
        SPELLCASTING,
        BOW_AND_ARROW,
        CROSSBOW_HOLD,
        CROSSBOW_CHARGE,
        CELEBRATING,
        NEUTRAL
    }

    protected class RaiderOpenDoorGoal extends OpenDoorGoal {

        public RaiderOpenDoorGoal(Raider raider0) {
            super(raider0, false);
        }

        @Override
        public boolean canUse() {
            return super.m_8036_() && AbstractIllager.this.m_37886_();
        }
    }
}