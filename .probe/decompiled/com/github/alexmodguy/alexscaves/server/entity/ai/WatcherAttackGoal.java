package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.entity.living.WatcherEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.EnumSet;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

public class WatcherAttackGoal extends Goal {

    private WatcherEntity watcher;

    private int navigationCheckCooldown = 0;

    private int possessions = 0;

    private boolean canReachViaGround = false;

    private int retreatFor = 0;

    private Vec3 retreatTo = null;

    public WatcherAttackGoal(WatcherEntity watcher) {
        this.m_7021_(EnumSet.of(Goal.Flag.LOOK, Goal.Flag.MOVE));
        this.watcher = watcher;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.watcher.m_5448_();
        return target != null && target.isAlive();
    }

    @Override
    public void tick() {
        LivingEntity target = this.watcher.m_5448_();
        if (this.navigationCheckCooldown-- < 0) {
            this.calculateReach();
        }
        if (target != null && target.isAlive()) {
            double dist = (double) this.watcher.m_20270_(target);
            this.watcher.m_7618_(EntityAnchorArgument.Anchor.EYES, target.m_146892_());
            if (this.retreatFor-- > 0) {
                if (this.retreatTo == null) {
                    this.retreatFor = 0;
                } else {
                    Vec3 retreatVec = this.retreatTo.subtract(this.watcher.m_20182_());
                    if (retreatVec.length() > 1.0) {
                        retreatVec = retreatVec.normalize();
                        this.watcher.m_20256_(this.watcher.m_20184_().add(retreatVec.scale(0.2F)));
                        this.watcher.setShadeMode(true);
                    } else {
                        this.retreatTo = null;
                    }
                }
            } else {
                if (!this.canReachViaGround && !this.watcher.isShadeMode() && this.watcher.m_20096_()) {
                    this.watcher.setShadeMode(true);
                }
                this.watcher.setShadeMode(!this.canReachViaGround);
                if (dist < 6.0 && this.watcher.m_142582_(target) && this.watcher.m_20096_()) {
                    this.watcher.setShadeMode(false);
                }
                if (dist > (double) (target.m_20205_() + this.watcher.m_20205_() + 0.5F)) {
                    this.watcher.m_21573_().moveTo(target, (double) (this.watcher.isRunning() ? 1.3F : 1.0F) + Math.min(Math.log((double) (this.possessions + 1)), 1.0) * 0.6F);
                } else {
                    if (this.watcher.m_142582_(target)) {
                        if (this.watcher.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
                            this.watcher.setAnimation(this.watcher.m_217043_().nextBoolean() ? WatcherEntity.ANIMATION_ATTACK_0 : WatcherEntity.ANIMATION_ATTACK_1);
                            this.watcher.m_216990_(ACSoundRegistry.WATCHER_ATTACK.get());
                        } else if (this.watcher.getAnimationTick() == 8) {
                            target.hurt(target.m_269291_().mobAttack(this.watcher), (float) this.watcher.m_21051_(Attributes.ATTACK_DAMAGE).getValue());
                            target.knockback(0.5, this.watcher.m_20185_() - target.m_20185_(), this.watcher.m_20189_() - target.m_20189_());
                            this.retreatFor = 30 + this.watcher.m_217043_().nextInt(30);
                            for (int i = 0; i < 15; i++) {
                                Vec3 vec3 = DefaultRandomPos.getPosAway(this.watcher, 30, 15, target.m_20182_());
                                if (vec3 != null) {
                                    this.retreatTo = vec3;
                                    break;
                                }
                            }
                        }
                    }
                    this.watcher.m_21573_().stop();
                }
            }
            if (this.possessions > 2 || dist < 20.0) {
                this.watcher.setRunning(true);
            }
            if (this.watcher.attemptPossession(target)) {
                if (this.watcher.getPossessedEntity() == null) {
                    this.possessions++;
                }
                this.watcher.setPossessedEntityUUID(target.m_20148_());
            }
        }
    }

    @Override
    public void start() {
        super.start();
        this.navigationCheckCooldown = 0;
        this.possessions = 0;
        this.retreatFor = 0;
        this.retreatTo = null;
    }

    public void calculateReach() {
        LivingEntity target = this.watcher.m_5448_();
        if (target != null && target.isAlive()) {
            this.canReachViaGround = this.watcher.canReach(target, false);
            this.navigationCheckCooldown = 10 + this.watcher.m_217043_().nextInt(40);
        }
    }

    @Override
    public void stop() {
        super.stop();
        this.watcher.setShadeMode(false);
        this.watcher.setRunning(false);
        this.retreatFor = 0;
        this.retreatTo = null;
    }
}