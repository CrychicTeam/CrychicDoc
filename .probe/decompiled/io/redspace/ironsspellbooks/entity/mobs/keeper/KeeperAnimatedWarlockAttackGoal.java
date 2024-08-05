package io.redspace.ironsspellbooks.entity.mobs.keeper;

import io.redspace.ironsspellbooks.entity.mobs.goals.WarlockAttackGoal;
import io.redspace.ironsspellbooks.network.ClientboundSyncAnimation;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.setup.Messages;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.Vec3;

public class KeeperAnimatedWarlockAttackGoal extends WarlockAttackGoal {

    final KeeperEntity keeper;

    int meleeAnimTimer = -1;

    public KeeperEntity.AttackType currentAttack;

    public KeeperEntity.AttackType nextAttack;

    public KeeperEntity.AttackType queueCombo;

    private boolean hasLunged;

    private boolean hasHitLunge;

    private Vec3 oldLungePos;

    public KeeperAnimatedWarlockAttackGoal(KeeperEntity abstractSpellCastingMob, double pSpeedModifier, int minAttackInterval, int maxAttackInterval, float meleeRange) {
        super(abstractSpellCastingMob, pSpeedModifier, minAttackInterval, maxAttackInterval, meleeRange);
        this.keeper = abstractSpellCastingMob;
        this.nextAttack = this.randomizeNextAttack(0.0F);
        this.wantsToMelee = true;
    }

    @Override
    protected float meleeBias() {
        return 1.0F;
    }

    @Override
    protected void handleAttackLogic(double distanceSquared) {
        float distance = Mth.sqrt((float) distanceSquared);
        this.mob.m_21563_().setLookAt(this.target);
        if (this.meleeAnimTimer > 0) {
            this.forceFaceTarget();
            this.meleeAnimTimer--;
            if (this.currentAttack.data.isHitFrame(this.meleeAnimTimer - 4)) {
                if (this.currentAttack != KeeperEntity.AttackType.Lunge) {
                    this.playSwingSound();
                }
            } else if (this.currentAttack.data.isHitFrame(this.meleeAnimTimer)) {
                if (this.currentAttack != KeeperEntity.AttackType.Lunge) {
                    Vec3 lunge = this.target.m_20182_().subtract(this.mob.m_20182_()).normalize().scale(0.55F);
                    this.mob.m_5997_(lunge.x, lunge.y, lunge.z);
                    if (distance <= this.meleeRange) {
                        boolean flag = this.mob.m_7327_(this.target);
                        this.target.f_19802_ = 0;
                        if (flag) {
                            this.playImpactSound();
                            if (this.currentAttack.data.isSingleHit() && (this.mob.m_217043_().nextFloat() < 0.75F || this.target.isBlocking())) {
                                this.queueCombo = this.randomizeNextAttack(0.0F);
                            }
                        }
                    }
                } else {
                    if (!this.hasLunged) {
                        Vec3 lunge = this.target.m_20182_().subtract(this.mob.m_20182_()).normalize().multiply(2.4, 0.5, 2.4).add(0.0, 0.15, 0.0);
                        this.mob.m_5997_(lunge.x, lunge.y, lunge.z);
                        this.oldLungePos = this.mob.m_20182_();
                        this.mob.m_21573_().stop();
                        this.hasLunged = true;
                        this.playSwingSound();
                    }
                    if (!this.hasHitLunge && distance <= this.meleeRange * 0.45F) {
                        if (this.mob.m_7327_(this.target)) {
                            this.playImpactSound();
                        }
                        Vec3 knockback = this.oldLungePos.subtract(this.target.m_20182_());
                        this.target.knockback(1.0, knockback.x, knockback.z);
                        this.hasHitLunge = true;
                    }
                }
            }
        } else if (this.queueCombo != null && this.target != null && !this.target.isDeadOrDying()) {
            this.nextAttack = this.queueCombo;
            this.queueCombo = null;
            this.doMeleeAction();
        } else if (this.meleeAnimTimer == 0) {
            this.nextAttack = this.randomizeNextAttack(distance);
            this.resetAttackTimer(distanceSquared);
            this.meleeAnimTimer = -1;
        } else if (distance < this.meleeRange * (float) (this.nextAttack == KeeperEntity.AttackType.Lunge ? 3 : 1)) {
            if (--this.attackTime == 0) {
                this.doMeleeAction();
            } else if (this.attackTime < 0) {
                this.resetAttackTimer(distanceSquared);
            }
        } else if (--this.attackTime < 0) {
            this.resetAttackTimer(distanceSquared);
            this.nextAttack = this.randomizeNextAttack(distance);
        }
    }

    private KeeperEntity.AttackType randomizeNextAttack(float distance) {
        int i;
        if (distance < this.meleeRange * 1.5F) {
            i = KeeperEntity.AttackType.values().length - 1;
        } else {
            if (this.mob.m_217043_().nextFloat() < 0.25F && distance > this.meleeRange * 2.5F) {
                return KeeperEntity.AttackType.Lunge;
            }
            i = KeeperEntity.AttackType.values().length;
        }
        return KeeperEntity.AttackType.values()[this.mob.m_217043_().nextInt(i)];
    }

    private void forceFaceTarget() {
        if (!this.hasLunged) {
            double d0 = this.target.m_20185_() - this.mob.m_20185_();
            double d1 = this.target.m_20189_() - this.mob.m_20189_();
            float yRot = (float) (Mth.atan2(d1, d0) * 180.0F / (float) Math.PI) - 90.0F;
            this.mob.m_5618_(yRot);
            this.mob.m_5616_(yRot);
            this.mob.m_146922_(yRot);
        }
    }

    @Override
    protected void doMeleeAction() {
        this.currentAttack = this.nextAttack;
        if (this.currentAttack != null) {
            this.mob.m_6674_(InteractionHand.MAIN_HAND);
            this.meleeAnimTimer = this.currentAttack.data.lengthInTicks;
            this.hasLunged = false;
            this.hasHitLunge = false;
            Messages.sendToPlayersTrackingEntity(new ClientboundSyncAnimation<>(this.currentAttack.toString(), this.keeper), this.keeper);
        }
    }

    @Override
    protected void doMovement(double distanceSquared) {
        if (this.target.isDeadOrDying()) {
            this.mob.m_21573_().stop();
        } else if (distanceSquared > (double) (this.meleeRange * this.meleeRange)) {
            this.mob.m_21573_().moveTo(this.target, this.speedModifier * 1.3F);
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
        this.mob.m_5496_(SoundRegistry.KEEPER_SWING.get(), 1.0F, (float) Mth.randomBetweenInclusive(this.mob.m_217043_(), 9, 13) * 0.1F);
    }

    public void playImpactSound() {
        this.mob.m_5496_(SoundRegistry.KEEPER_SWORD_IMPACT.get(), 1.0F, (float) Mth.randomBetweenInclusive(this.mob.m_217043_(), 9, 13) * 0.1F);
    }
}