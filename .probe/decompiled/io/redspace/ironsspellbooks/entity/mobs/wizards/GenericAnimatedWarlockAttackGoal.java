package io.redspace.ironsspellbooks.entity.mobs.wizards;

import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.entity.mobs.IAnimatedAttacker;
import io.redspace.ironsspellbooks.entity.mobs.goals.AttackAnimationData;
import io.redspace.ironsspellbooks.entity.mobs.goals.WarlockAttackGoal;
import io.redspace.ironsspellbooks.network.ClientboundSyncAnimation;
import io.redspace.ironsspellbooks.setup.Messages;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.phys.Vec3;

public class GenericAnimatedWarlockAttackGoal<T extends PathfinderMob & IAnimatedAttacker & IMagicEntity> extends WarlockAttackGoal {

    List<AttackAnimationData> moveList = new ArrayList();

    final T mob;

    int meleeAnimTimer = -1;

    @Nullable
    public AttackAnimationData currentAttack;

    @Nullable
    public AttackAnimationData nextAttack;

    @Nullable
    public AttackAnimationData queueCombo;

    float comboChance = 0.3F;

    public GenericAnimatedWarlockAttackGoal(T abstractSpellCastingMob, double pSpeedModifier, int minAttackInterval, int maxAttackInterval, float meleeRange) {
        super(abstractSpellCastingMob, pSpeedModifier, minAttackInterval, maxAttackInterval, meleeRange);
        this.nextAttack = this.randomizeNextAttack(0.0F);
        this.wantsToMelee = true;
        this.mob = abstractSpellCastingMob;
    }

    @Override
    protected void handleAttackLogic(double distanceSquared) {
        if (this.meleeAnimTimer >= 0 || this.wantsToMelee && !(distanceSquared > (double) (this.meleeRange * this.meleeRange)) && !this.mob.isCasting()) {
            this.mob.m_21563_().setLookAt(this.target);
            if (this.meleeAnimTimer > 0) {
                this.forceFaceTarget();
                this.meleeAnimTimer--;
                if (this.currentAttack.isHitFrame(this.meleeAnimTimer)) {
                    this.playSwingSound();
                    Vec3 lunge = this.target.m_20182_().subtract(this.mob.m_20182_()).normalize().scale(0.45F);
                    this.mob.m_5997_(lunge.x, lunge.y, lunge.z);
                    if (distanceSquared <= (double) (this.meleeRange * this.meleeRange)) {
                        boolean flag = this.mob.m_7327_(this.target);
                        this.target.f_19802_ = 0;
                        if (flag && this.currentAttack.isSingleHit() && this.mob.m_217043_().nextFloat() < this.comboChance * (float) (this.target.isBlocking() ? 2 : 1)) {
                            this.queueCombo = this.randomizeNextAttack(0.0F);
                        }
                    }
                }
            } else if (this.queueCombo != null && this.target != null && !this.target.isDeadOrDying()) {
                this.nextAttack = this.queueCombo;
                this.queueCombo = null;
                this.doMeleeAction();
            } else if (this.meleeAnimTimer == 0) {
                this.nextAttack = this.randomizeNextAttack((float) distanceSquared);
                this.resetAttackTimer(distanceSquared);
                this.meleeAnimTimer = -1;
            } else if (distanceSquared < (double) (this.meleeRange * this.meleeRange) * 1.2 * 1.2) {
                if (--this.attackTime == 0) {
                    this.doMeleeAction();
                } else if (this.attackTime < 0) {
                    this.resetAttackTimer(distanceSquared);
                }
            }
        } else {
            super.handleAttackLogic(distanceSquared);
        }
    }

    private AttackAnimationData randomizeNextAttack(float distanceSquared) {
        return this.moveList.isEmpty() ? null : (AttackAnimationData) this.moveList.get(this.mob.m_217043_().nextInt(this.moveList.size()));
    }

    private void forceFaceTarget() {
        double d0 = this.target.m_20185_() - this.mob.m_20185_();
        double d1 = this.target.m_20189_() - this.mob.m_20189_();
        float yRot = (float) (Mth.atan2(d1, d0) * 180.0F / (float) Math.PI) - 90.0F;
        this.mob.m_5618_(yRot);
        this.mob.m_5616_(yRot);
        this.mob.m_146922_(yRot);
    }

    @Override
    protected void doMeleeAction() {
        this.currentAttack = this.nextAttack;
        if (this.currentAttack != null) {
            this.mob.m_6674_(InteractionHand.MAIN_HAND);
            this.meleeAnimTimer = this.currentAttack.lengthInTicks;
            Messages.sendToPlayersTrackingEntity(new ClientboundSyncAnimation(this.currentAttack.animationId, this.mob), this.mob);
        }
    }

    @Override
    public boolean canContinueToUse() {
        return super.m_8045_() || this.meleeAnimTimer > 0;
    }

    @Override
    public void stop() {
        super.m_8041_();
        this.meleeAnimTimer = -1;
        this.queueCombo = null;
    }

    public void playSwingSound() {
        this.mob.m_5496_(SoundEvents.PLAYER_ATTACK_SWEEP, 1.0F, (float) Mth.randomBetweenInclusive(this.mob.m_217043_(), 12, 18) * 0.1F);
    }

    public GenericAnimatedWarlockAttackGoal<T> setMoveset(List<AttackAnimationData> moveset) {
        this.moveList = moveset;
        this.nextAttack = this.randomizeNextAttack(0.0F);
        return this;
    }

    public GenericAnimatedWarlockAttackGoal<T> setComboChance(float comboChance) {
        this.comboChance = comboChance;
        return this;
    }
}