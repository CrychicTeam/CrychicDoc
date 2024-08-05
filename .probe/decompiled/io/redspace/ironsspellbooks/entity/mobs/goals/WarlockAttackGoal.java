package io.redspace.ironsspellbooks.entity.mobs.goals;

import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import java.util.List;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class WarlockAttackGoal extends WizardAttackGoal {

    protected float meleeRange;

    protected boolean wantsToMelee;

    protected int meleeTime;

    protected int meleeDecisionTime;

    protected float meleeBiasMin;

    protected float meleeBiasMax;

    protected float meleeMoveSpeedModifier;

    protected int meleeAttackIntervalMin;

    protected int meleeAttackIntervalMax;

    public WarlockAttackGoal(IMagicEntity abstractSpellCastingMob, double pSpeedModifier, int minAttackInterval, int maxAttackInterval, float meleeRange) {
        super(abstractSpellCastingMob, pSpeedModifier, minAttackInterval, maxAttackInterval);
        this.meleeRange = meleeRange;
        this.meleeDecisionTime = this.mob.m_217043_().nextIntBetweenInclusive(80, 200);
        this.meleeBiasMin = 0.25F;
        this.meleeBiasMax = 0.75F;
        this.allowFleeing = false;
        this.meleeMoveSpeedModifier = (float) pSpeedModifier;
        this.meleeAttackIntervalMin = minAttackInterval;
        this.meleeAttackIntervalMax = maxAttackInterval;
    }

    @Override
    public void tick() {
        super.tick();
        if (++this.meleeTime > this.meleeDecisionTime) {
            this.meleeTime = 0;
            this.wantsToMelee = this.mob.m_217043_().nextFloat() <= this.meleeBias();
            this.meleeDecisionTime = this.mob.m_217043_().nextIntBetweenInclusive(60, 120);
        }
    }

    protected float meleeBias() {
        return Mth.clampedLerp(this.meleeBiasMin, this.meleeBiasMax, this.mob.m_21223_() / this.mob.m_21233_());
    }

    @Override
    protected void doMovement(double distanceSquared) {
        if (!this.wantsToMelee) {
            super.doMovement(distanceSquared);
        } else {
            if (this.target.isDeadOrDying()) {
                this.mob.m_21573_().stop();
            } else {
                this.mob.m_21391_(this.target, 30.0F, 30.0F);
                float strafeForwards = 0.0F;
                float speed = (float) this.movementSpeed();
                if (distanceSquared > (double) (this.meleeRange * this.meleeRange)) {
                    if (this.mob.f_19797_ % 5 == 0) {
                        this.mob.m_21573_().moveTo(this.target, (double) this.meleeMoveSpeedModifier);
                    }
                    this.mob.m_21566_().strafe(0.0F, 0.0F);
                } else {
                    this.mob.m_21573_().stop();
                    strafeForwards = 0.25F * this.meleeMoveSpeedModifier * (4.0 * distanceSquared > (double) (this.meleeRange * this.meleeRange) ? 1.5F : -1.0F);
                    if (++this.strafeTime > 25 && this.mob.m_217043_().nextDouble() < 0.1) {
                        this.strafingClockwise = !this.strafingClockwise;
                        this.strafeTime = 0;
                    }
                    float strafeDir = this.strafingClockwise ? 1.0F : -1.0F;
                    this.mob.m_21566_().strafe(strafeForwards, speed * strafeDir);
                }
                this.mob.m_21563_().setLookAt(this.target);
            }
        }
    }

    @Override
    protected void handleAttackLogic(double distanceSquared) {
        if (this.wantsToMelee && !(distanceSquared > (double) (this.meleeRange * this.meleeRange)) && !this.spellCastingMob.isCasting()) {
            if (--this.attackTime == 0) {
                this.mob.m_6674_(InteractionHand.MAIN_HAND);
                this.doMeleeAction();
            }
        } else {
            super.handleAttackLogic(distanceSquared);
        }
    }

    protected void doMeleeAction() {
        double distanceSquared = this.mob.m_20275_(this.target.m_20185_(), this.target.m_20186_(), this.target.m_20189_());
        this.mob.m_7327_(this.target);
        this.resetAttackTimer(distanceSquared);
    }

    public WarlockAttackGoal setMeleeBias(float meleeBiasMin, float meleeBiasMax) {
        this.meleeBiasMin = meleeBiasMin;
        this.meleeBiasMax = meleeBiasMax;
        return this;
    }

    public WarlockAttackGoal setSpells(List<AbstractSpell> attackSpells, List<AbstractSpell> defenseSpells, List<AbstractSpell> movementSpells, List<AbstractSpell> supportSpells) {
        return (WarlockAttackGoal) super.setSpells(attackSpells, defenseSpells, movementSpells, supportSpells);
    }

    public WarlockAttackGoal setSpellQuality(float minSpellQuality, float maxSpellQuality) {
        return (WarlockAttackGoal) super.setSpellQuality(minSpellQuality, maxSpellQuality);
    }

    public WarlockAttackGoal setSingleUseSpell(AbstractSpell spellType, int minDelay, int maxDelay, int minLevel, int maxLevel) {
        return (WarlockAttackGoal) super.setSingleUseSpell(spellType, minDelay, maxDelay, minLevel, maxLevel);
    }

    public WarlockAttackGoal setIsFlying() {
        return (WarlockAttackGoal) super.setIsFlying();
    }

    public WarlockAttackGoal setMeleeMovespeedModifier(float meleeMovespeedModifier) {
        this.meleeMoveSpeedModifier = meleeMovespeedModifier;
        return this;
    }

    public WarlockAttackGoal setMeleeAttackInverval(int min, int max) {
        this.meleeAttackIntervalMax = max;
        this.meleeAttackIntervalMin = min;
        return this;
    }

    @Override
    protected double movementSpeed() {
        return this.wantsToMelee ? (double) this.meleeMoveSpeedModifier * this.mob.m_21133_(Attributes.MOVEMENT_SPEED) * 2.0 : super.movementSpeed();
    }

    @Override
    protected void resetAttackTimer(double distanceSquared) {
        if (this.wantsToMelee && !(distanceSquared > (double) (this.meleeRange * this.meleeRange)) && !this.spellCastingMob.isCasting()) {
            float f = (float) Math.sqrt(distanceSquared) / this.attackRadius;
            this.attackTime = Mth.floor(f * (float) (this.meleeAttackIntervalMax - this.meleeAttackIntervalMin) + (float) this.meleeAttackIntervalMin);
        } else {
            super.resetAttackTimer(distanceSquared);
        }
    }
}