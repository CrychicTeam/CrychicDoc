package io.redspace.ironsspellbooks.entity.mobs.dead_king_boss;

import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.mobs.goals.WarlockAttackGoal;
import io.redspace.ironsspellbooks.network.ClientboundSyncAnimation;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.setup.Messages;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class DeadKingAnimatedWarlockAttackGoal extends WarlockAttackGoal {

    final DeadKingBoss deadKing;

    int meleeAnimTimer = -1;

    public DeadKingBoss.AttackType currentAttack;

    public DeadKingBoss.AttackType nextAttack;

    public DeadKingBoss.AttackType queueCombo;

    public DeadKingAnimatedWarlockAttackGoal(DeadKingBoss abstractSpellCastingMob, double pSpeedModifier, int minAttackInterval, int maxAttackInterval, float meleeRange) {
        super(abstractSpellCastingMob, pSpeedModifier, minAttackInterval, maxAttackInterval, meleeRange);
        this.deadKing = abstractSpellCastingMob;
        this.nextAttack = this.randomizeNextAttack(0.0F);
        this.wantsToMelee = true;
    }

    @Override
    protected void handleAttackLogic(double distanceSquared) {
        if (this.meleeAnimTimer >= 0 || this.wantsToMelee && !(distanceSquared > (double) (this.meleeRange * this.meleeRange)) && !this.spellCastingMob.isCasting()) {
            this.mob.m_21563_().setLookAt(this.target);
            this.deadKing.isMeleeing = this.meleeAnimTimer > 0;
            if (this.meleeAnimTimer > 0) {
                this.forceFaceTarget();
                this.meleeAnimTimer--;
                if (this.currentAttack.data.isHitFrame(this.meleeAnimTimer - 4)) {
                    if (this.currentAttack == DeadKingBoss.AttackType.SLAM) {
                        this.mob.m_216990_(SoundRegistry.DEAD_KING_SLAM.get());
                    } else {
                        this.playSwingSound();
                    }
                } else if (this.currentAttack.data.isHitFrame(this.meleeAnimTimer)) {
                    Vec3 lunge = this.target.m_20182_().subtract(this.mob.m_20182_()).normalize().scale(0.35F);
                    this.mob.m_5997_(lunge.x, lunge.y, lunge.z);
                    if (this.currentAttack == DeadKingBoss.AttackType.SLAM) {
                        Vec3 slamPos = this.mob.m_20182_().add(this.mob.m_20156_().multiply(1.0, 0.0, 1.0).normalize().scale(2.5));
                        Vec3 bbHalf = new Vec3((double) this.meleeRange, (double) this.meleeRange, (double) this.meleeRange).scale(0.3);
                        float damage = (float) this.mob.m_21133_(Attributes.ATTACK_DAMAGE) * 1.5F;
                        this.mob.f_19853_.m_45976_(LivingEntity.class, new AABB(slamPos.subtract(bbHalf), slamPos.add(bbHalf))).forEach(entity -> {
                            if (entity.isPickable() && !DamageSources.isFriendlyFireBetween(this.mob, entity)) {
                                entity.hurt(this.mob.m_9236_().damageSources().mobAttack(this.mob), damage);
                                Vec3 impulse = slamPos.subtract(entity.m_20182_()).add(0.0, 0.75, 0.0).normalize().scale(Mth.lerp(entity.m_20238_(this.mob.m_20182_()) / (double) (this.meleeRange * this.meleeRange), 2.0, 0.5));
                                entity.m_20256_(entity.m_20184_().add(impulse));
                                entity.f_19864_ = true;
                                if (entity instanceof Player player && player.m_21254_()) {
                                    player.disableShield(true);
                                }
                            }
                        });
                    } else if (distanceSquared <= (double) (this.meleeRange * this.meleeRange)) {
                        boolean flag = this.mob.m_7327_(this.target);
                        this.target.f_19802_ = 0;
                        if (flag && this.currentAttack.data.isSingleHit() && (this.mob.m_217043_().nextFloat() < 0.75F || this.target.isBlocking())) {
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

    private DeadKingBoss.AttackType randomizeNextAttack(float distanceSquared) {
        return this.mob.m_217043_().nextFloat() < 0.3F ? DeadKingBoss.AttackType.SLAM : DeadKingBoss.AttackType.DOUBLE_SWING;
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
            this.meleeAnimTimer = this.currentAttack.data.lengthInTicks;
            Messages.sendToPlayersTrackingEntity(new ClientboundSyncAnimation<>(this.currentAttack.toString(), this.deadKing), this.deadKing);
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
        this.mob.m_5496_(SoundRegistry.DEAD_KING_SWING.get(), 1.0F, (float) Mth.randomBetweenInclusive(this.mob.m_217043_(), 9, 13) * 0.1F);
    }
}