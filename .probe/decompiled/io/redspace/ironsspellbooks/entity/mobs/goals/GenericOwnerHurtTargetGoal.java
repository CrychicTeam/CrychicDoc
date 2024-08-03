package io.redspace.ironsspellbooks.entity.mobs.goals;

import io.redspace.ironsspellbooks.entity.mobs.MagicSummon;
import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

public class GenericOwnerHurtTargetGoal extends TargetGoal {

    private final Mob entity;

    private final OwnerGetter owner;

    private LivingEntity ownerLastHurt;

    private int timestamp;

    public GenericOwnerHurtTargetGoal(Mob entity, OwnerGetter ownerGetter) {
        super(entity, false);
        this.entity = entity;
        this.owner = ownerGetter;
        this.m_7021_(EnumSet.of(Goal.Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        LivingEntity owner = this.owner.get();
        if (owner == null) {
            return false;
        } else {
            this.ownerLastHurt = owner.getLastHurtMob();
            int i = owner.getLastHurtMobTimestamp();
            return i != this.timestamp && this.m_26150_(this.ownerLastHurt, TargetingConditions.DEFAULT) && (!(this.ownerLastHurt instanceof MagicSummon summon) || summon.getSummoner() != owner);
        }
    }

    @Override
    public void start() {
        this.f_26135_.setTarget(this.ownerLastHurt);
        LivingEntity owner = this.owner.get();
        if (owner != null) {
            this.timestamp = owner.getLastHurtMobTimestamp();
        }
        super.start();
    }
}