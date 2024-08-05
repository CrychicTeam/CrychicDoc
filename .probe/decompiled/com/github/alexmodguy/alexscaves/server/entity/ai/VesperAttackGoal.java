package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.entity.living.GloomothEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.VesperEntity;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.EnumSet;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.ForgeEventFactory;

public class VesperAttackGoal extends Goal {

    private VesperEntity entity;

    private Vec3 startOrbitFrom;

    private int orbitTime;

    private int maxOrbitTime;

    private boolean clockwise;

    public VesperAttackGoal(VesperEntity entity) {
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        this.entity = entity;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.entity.m_5448_();
        return target != null && target.isAlive() && !this.entity.m_20159_();
    }

    @Override
    public void tick() {
        if (this.entity.groundedFor <= 0) {
            if (this.entity.isHanging()) {
                this.entity.setHanging(false);
                this.entity.setFlying(true);
            } else if (!this.entity.isFlying()) {
                this.entity.setFlying(true);
            }
        } else {
            this.entity.setFlying(false);
        }
        LivingEntity target = this.entity.m_5448_();
        if (target != null && target.isAlive()) {
            double distance = (double) this.entity.m_20270_(target);
            float f = this.entity.m_20205_() + target.m_20205_();
            if (this.startOrbitFrom == null) {
                this.entity.m_21573_().moveTo(target, this.entity.isFlying() ? 2.5 : 1.0);
                this.entity.m_7618_(EntityAnchorArgument.Anchor.EYES, target.m_146892_());
            } else if (this.orbitTime < this.maxOrbitTime && this.entity.groundedFor <= 0) {
                this.orbitTime++;
                float zoomIn = 1.0F - (float) this.orbitTime / (float) this.maxOrbitTime;
                Vec3 orbitPos = this.orbitAroundPos(3.0F + zoomIn * 5.0F).add(0.0, (double) (4.0F + zoomIn * 3.0F), 0.0);
                this.entity.m_21573_().moveTo(orbitPos.x, orbitPos.y, orbitPos.z, this.entity.isFlying() ? 2.5 : 1.0);
                this.entity.m_7618_(EntityAnchorArgument.Anchor.EYES, orbitPos);
            } else {
                this.orbitTime = 0;
                this.startOrbitFrom = null;
            }
            if (distance < (double) f + 0.5) {
                if (this.entity.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
                    this.entity.setAnimation(VesperEntity.ANIMATION_BITE);
                } else if (this.entity.getAnimationTick() == 8 && this.entity.m_142582_(target)) {
                    boolean flag = target.isBlocking();
                    if (target.hurt(target.m_269291_().mobAttack(this.entity), (float) this.entity.m_21051_(Attributes.ATTACK_DAMAGE).getValue()) && !target.isAlive() && target instanceof GloomothEntity gloomothEntity && gloomothEntity.lightPos != null && gloomothEntity.m_20238_(Vec3.atCenterOf(gloomothEntity.lightPos)) < 32.0) {
                        this.entity.groundedFor = 100 + this.entity.m_217043_().nextInt(40);
                        this.entity.setFlying(false);
                    }
                    this.maxOrbitTime = 60 + this.entity.m_217043_().nextInt(80);
                    this.startOrbitFrom = target.m_146892_();
                    if (flag) {
                        if (target instanceof Player player) {
                            this.damageShieldFor(player, (float) this.entity.m_21051_(Attributes.ATTACK_DAMAGE).getBaseValue());
                        }
                        this.entity.groundedFor = 60 + this.entity.m_217043_().nextInt(40);
                        this.entity.setFlying(false);
                        this.startOrbitFrom = null;
                    }
                }
            }
        }
    }

    @Override
    public void start() {
        this.orbitTime = 0;
        this.maxOrbitTime = 80;
        this.startOrbitFrom = null;
    }

    public Vec3 orbitAroundPos(float circleDistance) {
        float angle = 3.0F * (float) Math.toRadians((double) ((float) (this.clockwise ? -this.orbitTime : this.orbitTime) * 3.0F));
        double extraX = (double) (circleDistance * Mth.sin(angle));
        double extraZ = (double) (circleDistance * Mth.cos(angle));
        return this.startOrbitFrom.add(extraX, 0.0, extraZ);
    }

    protected void damageShieldFor(Player holder, float damage) {
        if (holder.m_21211_().canPerformAction(ToolActions.SHIELD_BLOCK)) {
            if (!this.entity.m_9236_().isClientSide) {
                holder.awardStat(Stats.ITEM_USED.get(holder.m_21211_().getItem()));
            }
            if (damage >= 3.0F) {
                int i = 1 + Mth.floor(damage);
                InteractionHand hand = holder.m_7655_();
                holder.m_21211_().hurtAndBreak(i, holder, p_213833_1_ -> {
                    p_213833_1_.m_21190_(hand);
                    ForgeEventFactory.onPlayerDestroyItem(holder, holder.m_21211_(), hand);
                });
                if (holder.m_21211_().isEmpty()) {
                    if (hand == InteractionHand.MAIN_HAND) {
                        holder.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                    } else {
                        holder.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
                    }
                    holder.playSound(SoundEvents.SHIELD_BREAK, 0.8F, 0.8F + this.entity.m_9236_().random.nextFloat() * 0.4F);
                }
            }
        }
    }
}