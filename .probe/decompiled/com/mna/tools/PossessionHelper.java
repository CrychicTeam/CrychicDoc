package com.mna.tools;

import com.mna.api.entities.possession.PossessionActions;
import com.mna.effects.EffectInit;
import com.mna.network.messages.to_server.PossessionInputMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ForgeMod;

public class PossessionHelper {

    public static void handleRemoteInput(PossessionInputMessage message, Player sendingPlayer, Mob living) {
        switch(message.getType()) {
            case CLICK:
                if (sendingPlayer.m_9236_().isClientSide()) {
                    return;
                }
                if (sendingPlayer.getPersistentData().contains("posession_left_click_time")) {
                    long time = sendingPlayer.getPersistentData().getLong("posession_left_click_time");
                    if (sendingPlayer.m_9236_().getGameTime() < time) {
                        return;
                    }
                }
                if (!PossessionActions.Invoke(living) && !PossessionActions.InvokeDefault(living)) {
                    living.playAmbientSound();
                }
                sendingPlayer.getPersistentData().putLong("posession_left_click_time", sendingPlayer.m_9236_().getGameTime() + 30L);
                break;
            case MOVEMENT:
                living.m_19890_(living.m_20185_(), living.m_20186_(), living.m_20189_(), message.getYaw(), message.getPitch());
                living.m_5616_(message.getYawHead());
                double y = 0.0;
                boolean canFly = living.m_20068_() || living instanceof FlyingMob || living instanceof Bat || living.m_21023_(EffectInit.LEVITATION.get());
                if (message.getJump()) {
                    if (living.m_20096_() && !canFly) {
                        jump(living);
                    } else if (canFly) {
                        living.m_20242_(true);
                        y = 1.0;
                    }
                } else if (message.getSneak() && canFly) {
                    if (living.m_20096_()) {
                        living.m_20242_(false);
                    }
                    y = -1.0;
                }
                living.setSpeed((float) living.m_21133_(Attributes.MOVEMENT_SPEED) * 0.5F);
                if (living instanceof FlyingMob) {
                    living.m_7023_(new Vec3((double) message.getStrafe(), y, (double) message.getForward()));
                } else {
                    doTravel(living, new Vec3((double) message.getStrafe(), y, (double) message.getForward()));
                }
        }
    }

    public static void doTravel(LivingEntity ent, Vec3 travelVector) {
        double d0 = 0.08;
        d0 = ent.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).getValue();
        boolean flag = ent.m_20184_().y <= 0.0;
        FluidState fluidstate = ent.m_9236_().getFluidState(ent.m_20183_());
        if (ent.m_20069_() && ent.isAffectedByFluids() && !ent.canStandOnFluid(fluidstate)) {
            double d8 = ent.m_20186_();
            float f5 = ent.m_20142_() ? 0.9F : 0.8F;
            float f6 = 0.02F;
            float f7 = (float) EnchantmentHelper.getDepthStrider(ent);
            if (f7 > 3.0F) {
                f7 = 3.0F;
            }
            if (!ent.m_20096_()) {
                f7 *= 0.5F;
            }
            if (f7 > 0.0F) {
                f5 += (0.54600006F - f5) * f7 / 3.0F;
                f6 += (ent.getSpeed() - f6) * f7 / 3.0F;
            }
            if (ent.hasEffect(MobEffects.DOLPHINS_GRACE)) {
                f5 = 0.96F;
            }
            f6 *= (float) ent.getAttribute(ForgeMod.SWIM_SPEED.get()).getValue();
            ent.m_19920_(f6, travelVector);
            ent.m_6478_(MoverType.SELF, ent.m_20184_());
            Vec3 vector3d6 = ent.m_20184_();
            if (ent.f_19862_ && ent.onClimbable()) {
                vector3d6 = new Vec3(vector3d6.x, 0.2, vector3d6.z);
            }
            ent.m_20256_(vector3d6.multiply((double) f5, 0.8F, (double) f5));
            Vec3 vector3d2 = ent.getFluidFallingAdjustedMovement(d0, flag, ent.m_20184_());
            ent.m_20256_(vector3d2);
            if (ent.f_19862_ && ent.m_20229_(vector3d2.x, vector3d2.y + 0.6F - ent.m_20186_() + d8, vector3d2.z)) {
                ent.m_20334_(vector3d2.x, 0.3F, vector3d2.z);
            }
        } else if (ent.m_20077_() && ent.isAffectedByFluids() && !ent.canStandOnFluid(fluidstate)) {
            double d7 = ent.m_20186_();
            ent.m_19920_(0.02F, travelVector);
            ent.m_6478_(MoverType.SELF, ent.m_20184_());
            if (ent.m_204036_(FluidTags.LAVA) <= ent.m_20204_()) {
                ent.m_20256_(ent.m_20184_().multiply(0.5, 0.8F, 0.5));
                Vec3 vector3d3 = ent.getFluidFallingAdjustedMovement(d0, flag, ent.m_20184_());
                ent.m_20256_(vector3d3);
            } else {
                ent.m_20256_(ent.m_20184_().scale(0.5));
            }
            if (!ent.m_20068_()) {
                ent.m_20256_(ent.m_20184_().add(0.0, -d0 / 4.0, 0.0));
            }
            Vec3 vector3d4 = ent.m_20184_();
            if (ent.f_19862_ && ent.m_20229_(vector3d4.x, vector3d4.y + 0.6F - ent.m_20186_() + d7, vector3d4.z)) {
                ent.m_20334_(vector3d4.x, 0.3F, vector3d4.z);
            }
        } else if (ent.isFallFlying()) {
            Vec3 vector3d = ent.m_20184_();
            if (vector3d.y > -0.5) {
                ent.f_19789_ = 1.0F;
            }
            Vec3 vector3d1 = ent.m_20154_();
            float f = ent.m_146909_() * (float) (Math.PI / 180.0);
            double d1 = Math.sqrt(vector3d1.x * vector3d1.x + vector3d1.z * vector3d1.z);
            double d3 = Math.sqrt(vector3d.horizontalDistanceSqr());
            double d4 = vector3d1.length();
            float f1 = Mth.cos(f);
            f1 = (float) ((double) f1 * (double) f1 * Math.min(1.0, d4 / 0.4));
            vector3d = ent.m_20184_().add(0.0, d0 * (-1.0 + (double) f1 * 0.75), 0.0);
            if (vector3d.y < 0.0 && d1 > 0.0) {
                double d5 = vector3d.y * -0.1 * (double) f1;
                vector3d = vector3d.add(vector3d1.x * d5 / d1, d5, vector3d1.z * d5 / d1);
            }
            if (f < 0.0F && d1 > 0.0) {
                double d9 = d3 * (double) (-Mth.sin(f)) * 0.04;
                vector3d = vector3d.add(-vector3d1.x * d9 / d1, d9 * 3.2, -vector3d1.z * d9 / d1);
            }
            if (d1 > 0.0) {
                vector3d = vector3d.add((vector3d1.x / d1 * d3 - vector3d.x) * 0.1, 0.0, (vector3d1.z / d1 * d3 - vector3d.z) * 0.1);
            }
            ent.m_20256_(vector3d.multiply(0.99F, 0.98F, 0.99F));
            ent.m_6478_(MoverType.SELF, ent.m_20184_());
            if (ent.f_19862_ && !ent.m_9236_().isClientSide()) {
                double d10 = Math.sqrt(ent.m_20184_().horizontalDistanceSqr());
                double d6 = d3 - d10;
                float f2 = (float) (d6 * 10.0 - 3.0);
                if (f2 > 0.0F) {
                    ent.m_5496_(SoundEvents.GENERIC_BIG_FALL, (float) (0.9F + Math.random() * 0.2F), (float) (0.9F + Math.random() * 0.2F));
                    ent.hurt(ent.m_269291_().flyIntoWall(), f2);
                }
            }
            if (ent.m_20096_() && !ent.m_9236_().isClientSide()) {
                ent.m_20115_(7, false);
            }
        } else {
            BlockPos blockpos = ent.m_20183_().below();
            float f3 = ent.m_9236_().getBlockState(blockpos).getFriction(ent.m_9236_(), blockpos, ent);
            float f4 = ent.m_20096_() ? f3 * 0.91F : 0.91F;
            Vec3 vector3d5 = ent.handleRelativeFrictionAndCalculateMovement(travelVector, f3);
            double d2 = vector3d5.y;
            if (ent.hasEffect(MobEffects.LEVITATION)) {
                d2 += (0.05 * (double) (ent.getEffect(MobEffects.LEVITATION).getAmplifier() + 1) - vector3d5.y) * 0.2;
                ent.f_19789_ = 0.0F;
            } else if (ent.m_9236_().isClientSide() && !ent.m_9236_().isLoaded(blockpos)) {
                if (ent.m_20186_() > 0.0) {
                    d2 = -0.1;
                } else {
                    d2 = 0.0;
                }
            } else if (!ent.m_20068_()) {
                d2 -= d0;
            }
            ent.m_20334_(vector3d5.x * (double) f4, d2 * 0.98F, vector3d5.z * (double) f4);
        }
    }

    public static float getJumpUpwardsMotion(LivingEntity ent) {
        return 0.42F * getJumpFactor(ent);
    }

    public static void jump(LivingEntity ent) {
        float f = getJumpUpwardsMotion(ent);
        if (ent.hasEffect(MobEffects.JUMP)) {
            f += 0.1F * (float) (ent.getEffect(MobEffects.JUMP).getAmplifier() + 1);
        }
        Vec3 vector3d = ent.m_20184_();
        ent.m_20334_(vector3d.x, (double) f, vector3d.z);
        if (ent.m_20142_()) {
            float f1 = ent.m_146908_() * (float) (Math.PI / 180.0);
            ent.m_20256_(ent.m_20184_().add((double) (-Mth.sin(f1) * 0.2F), 0.0, (double) (Mth.cos(f1) * 0.2F)));
        }
        ent.f_19812_ = true;
        ForgeHooks.onLivingJump(ent);
    }

    public static float getJumpFactor(LivingEntity ent) {
        float f = ent.m_9236_().getBlockState(ent.m_20183_()).m_60734_().getJumpFactor();
        float f1 = ent.m_9236_().getBlockState(ent.m_20183_().below()).m_60734_().getJumpFactor();
        return (double) f == 1.0 ? f1 : f;
    }
}