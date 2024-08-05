package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.entity.living.ForsakenEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACDamageTypes;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;

public class ForsakenAttackGoal extends Goal {

    private ForsakenEntity entity;

    private BlockPos jumpTarget = null;

    private boolean jumpEnqueued = false;

    private boolean sonicEnqueued = false;

    private int navigationCheckCooldown = 0;

    private int attemptSonicDamageIn = 0;

    public ForsakenAttackGoal(ForsakenEntity entity) {
        this.entity = entity;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.entity.m_5448_();
        return target != null && target.isAlive();
    }

    @Override
    public void start() {
        this.navigationCheckCooldown = 0;
        this.jumpEnqueued = false;
        this.sonicEnqueued = this.entity.m_217043_().nextBoolean();
        this.attemptSonicDamageIn = 0;
    }

    @Override
    public void stop() {
        this.jumpEnqueued = false;
        this.sonicEnqueued = false;
        this.entity.setRunning(false);
    }

    @Override
    public void tick() {
        LivingEntity target = this.entity.m_5448_();
        if (target != null && target.isAlive()) {
            double distance = (double) this.entity.m_20270_(target);
            double attackDistance = (double) (this.entity.m_20205_() + target.m_20205_());
            boolean inPursuit = !this.isMovementFrozen();
            if (this.attemptSonicDamageIn > 0) {
                this.attemptSonicDamageIn--;
                if (this.attemptSonicDamageIn == 0 && this.entity.m_142582_(target)) {
                    target.hurt(ACDamageTypes.causeForsakenSonicBoomDamage(this.entity.m_9236_().registryAccess(), this.entity), this.entity.getSonicDamageAgainst(target));
                    this.knockBackAngle(target, 1.0, 0.0F);
                }
            }
            if (this.sonicEnqueued && this.entity.m_142582_(target) && distance < 200.0) {
                this.entity.m_7618_(EntityAnchorArgument.Anchor.EYES, target.m_146892_());
                this.entity.setSonarId(target.m_19879_());
                this.entity.m_21573_().stop();
                if (!(distance > 10.0) && !((double) this.entity.m_217043_().nextFloat() < 0.4)) {
                    this.tryAnimation(ForsakenEntity.ANIMATION_SONIC_BLAST);
                    this.entity.m_5496_(ACSoundRegistry.FORSAKEN_AOE.get(), this.entity.getSoundVolume(), this.entity.m_6100_());
                } else {
                    this.tryAnimation(ForsakenEntity.ANIMATION_SONIC_ATTACK);
                    this.entity.m_5496_(ACSoundRegistry.FORSAKEN_SCREECH.get(), this.entity.getSoundVolume(), this.entity.m_6100_());
                }
                if (this.entity.getAnimation() == ForsakenEntity.ANIMATION_SONIC_ATTACK) {
                    inPursuit = false;
                    if (this.entity.getAnimationTick() >= 10 && this.entity.getAnimationTick() <= 30 && this.attemptSonicDamageIn <= 0) {
                        this.attemptSonicDamageIn = (int) Math.ceil(distance * 0.2F);
                    }
                    if (this.entity.getAnimationTick() > 30) {
                        this.sonicEnqueued = false;
                    }
                }
                if (this.entity.getAnimation() == ForsakenEntity.ANIMATION_SONIC_BLAST) {
                    inPursuit = false;
                    if (this.entity.getAnimationTick() >= 10 && this.entity.getAnimationTick() <= 30 && this.entity.getAnimationTick() % 5 == 0) {
                        for (LivingEntity living : this.entity.m_9236_().m_45976_(LivingEntity.class, this.entity.m_20191_().inflate(16.0, 8.0, 16.0))) {
                            if (living != this.entity && !this.entity.m_7307_(living) && !living.m_7307_(this.entity) && living.m_20270_(this.entity) <= 14.0F && !living.m_6095_().is(ACTagRegistry.FORSAKEN_IGNORES)) {
                                living.hurt(ACDamageTypes.causeForsakenSonicBoomDamage(this.entity.m_9236_().registryAccess(), this.entity), (float) Math.ceil((double) (this.entity.getSonicDamageAgainst(target) * 0.65F)));
                            }
                        }
                    }
                    if (this.entity.getAnimationTick() > 40) {
                        this.sonicEnqueued = false;
                    }
                }
            } else if (this.jumpEnqueued) {
                if (this.jumpTarget == null) {
                    this.jumpTarget = this.findJumpTarget(target, distance > 20.0);
                } else {
                    inPursuit = false;
                    if (this.entity.isLeaping()) {
                        Vec3 vec3 = this.entity.m_20184_();
                        Vec3 vec31 = new Vec3((double) ((float) this.jumpTarget.m_123341_() + 0.5F) - this.entity.m_20185_(), 0.0, (double) ((float) this.jumpTarget.m_123343_() + 0.5F) - this.entity.m_20189_());
                        if (vec31.lengthSqr() > 1.0E-7) {
                            vec31 = vec31.scale(0.155F).add(vec3.scale(0.2));
                        }
                        this.entity.m_20334_(vec31.x, 0.2F + vec31.length() * 0.3F, vec31.z);
                        this.jumpEnqueued = false;
                        this.jumpTarget = null;
                    } else if (this.entity.m_20096_()) {
                        this.entity.m_7618_(EntityAnchorArgument.Anchor.EYES, Vec3.atCenterOf(this.jumpTarget));
                        this.tryAnimation(ForsakenEntity.ANIMATION_PREPARE_JUMP);
                    }
                }
            }
            if (inPursuit) {
                this.entity.m_7618_(EntityAnchorArgument.Anchor.EYES, target.m_146892_());
                this.entity.m_21573_().moveTo(target, 1.0);
                if (distance < attackDistance + 1.0 && this.entity.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
                    float attackType = this.entity.m_217043_().nextFloat();
                    if (attackType < 0.25F && target.m_20205_() < 2.0F) {
                        this.tryAnimation(this.entity.m_217043_().nextBoolean() ? ForsakenEntity.ANIMATION_LEFT_PICKUP : ForsakenEntity.ANIMATION_RIGHT_PICKUP);
                    } else if (attackType < 0.5F) {
                        this.tryAnimation(this.entity.m_217043_().nextBoolean() ? ForsakenEntity.ANIMATION_LEFT_SLASH : ForsakenEntity.ANIMATION_RIGHT_SLASH);
                    } else if (attackType < 0.75F) {
                        this.tryAnimation(ForsakenEntity.ANIMATION_GROUND_SMASH);
                    } else {
                        this.tryAnimation(ForsakenEntity.ANIMATION_BITE);
                        this.entity.m_216990_(ACSoundRegistry.FORSAKEN_BITE.get());
                    }
                }
            } else {
                this.entity.m_21573_().stop();
            }
            if ((distance > 20.0 && (double) this.entity.m_217043_().nextFloat() < 0.01 || this.entity.hasSonicCharge()) && !this.sonicEnqueued) {
                this.sonicEnqueued = true;
                this.entity.setSonicCharge(false);
            }
            if (distance > 30.0 && (double) this.entity.m_217043_().nextFloat() < 0.05 && !this.jumpEnqueued) {
                this.startCleanJump();
            }
            if (distance < 64.0 && distance > attackDistance && inPursuit) {
                this.entity.setRunning(true);
            } else {
                this.entity.setRunning(false);
            }
            if ((this.entity.getAnimation() == ForsakenEntity.ANIMATION_RIGHT_PICKUP || this.entity.getAnimation() == ForsakenEntity.ANIMATION_LEFT_PICKUP) && this.entity.getHeldMobId() == target.m_19879_() && this.entity.getAnimationTick() >= 30) {
                this.checkAndDealDamage(target, 1.2F, 5.0F);
            }
            if (this.entity.getAnimation() == ForsakenEntity.ANIMATION_RIGHT_SLASH && this.entity.getAnimationTick() >= 15 && this.entity.getAnimationTick() <= 18) {
                float knockbackStrength = 0.5F;
                if (this.checkAndDealDamage(target, 0.8F, 2.0F)) {
                    knockbackStrength = 3.0F;
                    this.entity.m_216990_(ACSoundRegistry.FORSAKEN_GRAB.get());
                }
                this.knockBackAngle(target, (double) knockbackStrength, -90.0F);
            }
            if (this.entity.getAnimation() == ForsakenEntity.ANIMATION_LEFT_SLASH && this.entity.getAnimationTick() >= 15 && this.entity.getAnimationTick() <= 18) {
                float knockbackStrength = 0.5F;
                if (this.checkAndDealDamage(target, 0.8F, 2.0F)) {
                    knockbackStrength = 3.0F;
                    this.entity.m_216990_(ACSoundRegistry.FORSAKEN_GRAB.get());
                }
                this.knockBackAngle(target, (double) knockbackStrength, 90.0F);
            }
            if (this.entity.getAnimation() == ForsakenEntity.ANIMATION_GROUND_SMASH && this.entity.getAnimationTick() >= 10 && this.entity.getAnimationTick() <= 15) {
                Vec3 smashPos = this.entity.m_20182_().add(new Vec3(0.0, 0.0, 3.5).yRot((float) (-Math.toRadians((double) this.entity.f_20883_))));
                List<LivingEntity> list = this.entity.m_9236_().m_45976_(LivingEntity.class, new AABB(smashPos.x - 4.0, smashPos.y - 2.0, smashPos.z - 4.0, smashPos.x + 4.0, smashPos.y + 3.0, smashPos.z + 4.0));
                boolean flag = false;
                for (LivingEntity livingx : list) {
                    if (livingx != this.entity && !this.entity.m_7307_(livingx) && !livingx.m_7307_(this.entity) && livingx.m_20238_(smashPos) <= 16.0 && !livingx.m_6095_().is(ACTagRegistry.FORSAKEN_IGNORES) && this.checkAndDealDamage(livingx, 0.8F, 3.0F) && livingx.m_20096_()) {
                        livingx.m_20256_(livingx.m_20184_().add(0.0, 0.5, 0.0));
                        flag = true;
                    }
                }
                if (flag) {
                    this.entity.m_216990_(ACSoundRegistry.FORSAKEN_GRAB.get());
                }
            }
            if (this.entity.getAnimation() == ForsakenEntity.ANIMATION_BITE && this.entity.getAnimationTick() >= 5 && this.entity.getAnimationTick() <= 8) {
                float knockbackStrength = 0.0F;
                if (this.checkAndDealDamage(target, 1.0, 1.0F)) {
                    knockbackStrength = 0.5F;
                }
                this.knockBackAngle(target, (double) knockbackStrength, 0.0F);
            }
            if (this.navigationCheckCooldown-- < 0 && this.entity.m_20096_()) {
                this.navigationCheckCooldown = 20 + this.entity.m_217043_().nextInt(40);
                if (!this.canReach(target)) {
                    this.startCleanJump();
                }
            }
        }
    }

    private boolean canReach(LivingEntity target) {
        Path path = this.entity.m_21573_().createPath(target, 0);
        if (path == null) {
            return false;
        } else {
            Node node = path.getEndNode();
            if (node == null) {
                return false;
            } else {
                int i = node.x - target.m_146903_();
                int j = node.y - target.m_146904_();
                int k = node.z - target.m_146907_();
                return (double) (i * i + j * j + k * k) <= 3.0;
            }
        }
    }

    private boolean isMovementFrozen() {
        return this.entity.getAnimation() == ForsakenEntity.ANIMATION_LEFT_PICKUP || this.entity.getAnimation() == ForsakenEntity.ANIMATION_RIGHT_PICKUP;
    }

    private void startCleanJump() {
        this.jumpTarget = null;
        this.jumpEnqueued = true;
    }

    private boolean checkAndDealDamage(LivingEntity target, double multiplier, float extraRange) {
        if (this.entity.m_142582_(target) && this.entity.m_20270_(target) < this.entity.m_20205_() + target.m_20205_() + extraRange) {
            boolean b = target.hurt(target.m_269291_().mobAttack(this.entity), (float) (multiplier * this.entity.m_21051_(Attributes.ATTACK_DAMAGE).getValue()));
            if (this.entity.m_217043_().nextInt(2) == 0) {
                this.startCleanJump();
            }
            if (!this.sonicEnqueued && this.entity.m_217043_().nextInt(5) == 0) {
                this.sonicEnqueued = true;
            }
            return b;
        } else {
            return false;
        }
    }

    private void knockBackAngle(LivingEntity target, double strength, float angle) {
        float yRot = this.entity.f_20883_ + angle;
        target.knockback(strength, (double) Mth.sin(yRot * (float) (Math.PI / 180.0)), (double) (-Mth.cos(yRot * (float) (Math.PI / 180.0))));
    }

    private boolean tryAnimation(Animation animation) {
        if (this.entity.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
            this.entity.setAnimation(animation);
            return true;
        } else {
            return false;
        }
    }

    private BlockPos findJumpTarget(LivingEntity target, boolean far) {
        int lengthOfRadius = far ? this.entity.m_217043_().nextInt(2) + 4 : this.entity.m_217043_().nextInt(10) + 15;
        Vec3 offset = target.m_20182_().add(new Vec3(0.0, 0.0, (double) lengthOfRadius).yRot((float) ((Math.PI * 2) * (double) this.entity.m_217043_().nextFloat())));
        Vec3 vec3 = null;
        if (far) {
            BlockPos farPos = LandRandomPos.movePosUpOutOfSolid(this.entity, BlockPos.containing(offset));
            if (farPos != null) {
                vec3 = Vec3.atCenterOf(farPos);
            }
        } else {
            vec3 = LandRandomPos.getPosTowards(this.entity, 20, 10, offset);
        }
        if (vec3 != null) {
            BlockPos blockpos = BlockPos.containing(vec3);
            AABB aabb = this.entity.m_20191_().move(vec3.add(0.5, 1.0, 0.5).subtract(this.entity.m_20182_()));
            if (this.entity.m_9236_().getBlockState(blockpos.below()).m_60804_(this.entity.m_9236_(), blockpos.below()) && this.entity.m_21439_(WalkNodeEvaluator.getBlockPathTypeStatic(this.entity.m_9236_(), blockpos.mutable())) == 0.0F && this.entity.m_9236_().m_5450_(this.entity, Shapes.create(aabb))) {
                return blockpos;
            }
        }
        return null;
    }
}