package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.entity.living.LuxtructosaurusEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.SauropodBaseEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.server.entity.pathfinding.raycoms.AdvancedPathNavigate;
import com.github.alexthe666.citadel.server.entity.pathfinding.raycoms.PathResult;
import java.util.EnumSet;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.phys.Vec3;

public class LuxtructosaurusMeleeGoal extends Goal {

    private LuxtructosaurusEntity luxtructosaurus;

    private int navigationCheckCooldown = 0;

    private int flamesCooldown = 0;

    private int roarCooldown = 0;

    private int successfulJumpCooldown = 0;

    public LuxtructosaurusMeleeGoal(LuxtructosaurusEntity luxtructosaurus) {
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.luxtructosaurus = luxtructosaurus;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.luxtructosaurus.m_5448_();
        return target != null && target.isAlive();
    }

    @Override
    public void start() {
        this.navigationCheckCooldown = 0;
        this.flamesCooldown = 0;
        this.roarCooldown = 0;
    }

    @Override
    public void stop() {
        this.luxtructosaurus.turningFast = false;
    }

    @Override
    public void tick() {
        LivingEntity target = this.luxtructosaurus.m_5448_();
        if (target != null && target.isAlive()) {
            double distance = (double) this.luxtructosaurus.m_20270_(target);
            double attackDistance = (double) (this.luxtructosaurus.m_20205_() + target.m_20205_());
            boolean farFlag = false;
            if (this.luxtructosaurus.getAnimation() != SauropodBaseEntity.ANIMATION_SPEW_FLAMES && this.luxtructosaurus.getAnimation() != SauropodBaseEntity.ANIMATION_LEFT_KICK && this.luxtructosaurus.getAnimation() != SauropodBaseEntity.ANIMATION_RIGHT_KICK && this.luxtructosaurus.getAnimation() != SauropodBaseEntity.ANIMATION_LEFT_WHIP && this.luxtructosaurus.getAnimation() != SauropodBaseEntity.ANIMATION_RIGHT_WHIP) {
                this.luxtructosaurus.turningFast = false;
            } else {
                this.luxtructosaurus.turningFast = true;
                Vec3 vec3 = target.m_20182_().subtract(this.luxtructosaurus.m_20182_());
                this.luxtructosaurus.f_20883_ = Mth.approachDegrees(this.luxtructosaurus.f_20883_, -((float) Mth.atan2(vec3.x, vec3.z)) * (180.0F / (float) Math.PI), 15.0F);
                this.luxtructosaurus.f_20884_ = this.luxtructosaurus.f_20883_;
                this.luxtructosaurus.m_21563_().setLookAt(target.m_20185_(), target.m_20188_(), target.m_20189_());
            }
            if (distance > attackDistance) {
                this.luxtructosaurus.m_21573_().moveTo(target, 1.0);
            }
            if (this.luxtructosaurus.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
                if (distance < attackDistance + 4.0) {
                    float random = this.luxtructosaurus.m_217043_().nextFloat();
                    if ((double) random < 0.33 && this.luxtructosaurus.m_20096_()) {
                        this.luxtructosaurus.setAnimation(LuxtructosaurusEntity.ANIMATION_STOMP);
                        this.luxtructosaurus.m_5496_(ACSoundRegistry.LUXTRUCTOSAURUS_ATTACK_STOMP.get(), 3.0F, this.luxtructosaurus.m_6100_());
                    } else if ((double) random < 0.66 && distance < attackDistance + 1.0) {
                        this.luxtructosaurus.m_5496_(ACSoundRegistry.LUXTRUCTOSAURUS_KICK.get(), 3.0F, this.luxtructosaurus.m_6100_());
                        this.luxtructosaurus.setAnimation(this.luxtructosaurus.m_217043_().nextBoolean() ? LuxtructosaurusEntity.ANIMATION_LEFT_KICK : LuxtructosaurusEntity.ANIMATION_RIGHT_KICK);
                    } else {
                        this.luxtructosaurus.m_5496_(ACSoundRegistry.LUXTRUCTOSAURUS_TAIL.get(), 3.0F, this.luxtructosaurus.m_6100_());
                        this.luxtructosaurus.setAnimation(this.luxtructosaurus.m_217043_().nextBoolean() ? LuxtructosaurusEntity.ANIMATION_RIGHT_WHIP : LuxtructosaurusEntity.ANIMATION_LEFT_WHIP);
                    }
                } else {
                    farFlag = true;
                    if (this.luxtructosaurus.isEnraged() && this.flamesCooldown == 0) {
                        this.flamesCooldown = 200 + this.luxtructosaurus.m_217043_().nextInt(300);
                        this.luxtructosaurus.setAnimation(SauropodBaseEntity.ANIMATION_SPEW_FLAMES);
                    }
                }
                int roarRandomChance = farFlag ? 90 : 150;
                if (this.luxtructosaurus.isEnraged()) {
                    roarRandomChance += 200;
                }
                if (this.roarCooldown == 0 && this.luxtructosaurus.m_217043_().nextInt(roarRandomChance) == 0 || !this.luxtructosaurus.isEnraged() && this.luxtructosaurus.m_21223_() < this.luxtructosaurus.m_21233_() * 0.25F) {
                    this.roarCooldown = 100 + this.luxtructosaurus.m_217043_().nextInt(200);
                    this.luxtructosaurus.setAnimation(SauropodBaseEntity.ANIMATION_ROAR);
                    this.luxtructosaurus.enragedFor = 500 + this.luxtructosaurus.m_217043_().nextInt(200);
                    this.luxtructosaurus.setEnraged(true);
                }
            }
            if (this.successfulJumpCooldown <= 0 && this.navigationCheckCooldown-- < 0 && (this.luxtructosaurus.m_20096_() || this.luxtructosaurus.m_20077_())) {
                this.navigationCheckCooldown = 20 + this.luxtructosaurus.m_217043_().nextInt(40);
                if (!this.canReach(target) && this.luxtructosaurus.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
                    this.luxtructosaurus.setAnimation(LuxtructosaurusEntity.ANIMATION_JUMP);
                    this.luxtructosaurus.m_5496_(ACSoundRegistry.LUXTRUCTOSAURUS_JUMP.get(), 5.0F, this.luxtructosaurus.m_6100_());
                    this.luxtructosaurus.jumpTarget = target.m_20182_();
                    this.successfulJumpCooldown = 100 + this.luxtructosaurus.m_217043_().nextInt(200);
                }
            }
        }
        if (this.flamesCooldown > 0) {
            this.flamesCooldown--;
        }
        if (this.roarCooldown > 0) {
            this.roarCooldown--;
        }
        if (this.successfulJumpCooldown > 0) {
            this.successfulJumpCooldown--;
        }
    }

    private boolean canReach(LivingEntity target) {
        if (target.m_20270_(this.luxtructosaurus) > 50.0F) {
            return false;
        } else {
            PathResult path = ((AdvancedPathNavigate) this.luxtructosaurus.m_21573_()).moveToLivingEntity(target, 1.0);
            if (path != null && path.getPath() != null) {
                Node node = path.getPath().getEndNode();
                if (node == null) {
                    return false;
                } else {
                    int i = node.x - target.m_146903_();
                    int j = node.y - target.m_146904_();
                    int k = node.z - target.m_146907_();
                    return (double) (i * i + j * j + k * k) <= 3.0;
                }
            } else {
                return false;
            }
        }
    }
}