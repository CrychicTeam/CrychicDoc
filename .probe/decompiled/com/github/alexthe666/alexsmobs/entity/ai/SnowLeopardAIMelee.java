package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntitySnowLeopard;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.EnumSet;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.Vec3;

public class SnowLeopardAIMelee extends Goal {

    private final EntitySnowLeopard leopard;

    private LivingEntity target;

    private boolean secondPartOfLeap = false;

    private Vec3 leapPos = null;

    private int jumpCooldown = 0;

    private boolean stalk = false;

    public SnowLeopardAIMelee(EntitySnowLeopard snowLeopard) {
        this.leopard = snowLeopard;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Nullable
    private static BlockPos getRandomDelta(RandomSource p_226343_0_, int p_226343_1_, int p_226343_2_, int p_226343_3_, @Nullable Vec3 p_226343_4_, double p_226343_5_) {
        if (p_226343_4_ != null && p_226343_5_ < Math.PI) {
            double lvt_7_2_ = Mth.atan2(p_226343_4_.z, p_226343_4_.x) - (float) (Math.PI / 2);
            double lvt_9_2_ = lvt_7_2_ + (double) (2.0F * p_226343_0_.nextFloat() - 1.0F) * p_226343_5_;
            double lvt_11_1_ = Math.sqrt(p_226343_0_.nextDouble()) * (double) Mth.SQRT_OF_TWO * (double) p_226343_1_;
            double lvt_13_1_ = -lvt_11_1_ * Math.sin(lvt_9_2_);
            double lvt_15_1_ = lvt_11_1_ * Math.cos(lvt_9_2_);
            if (Math.abs(lvt_13_1_) <= (double) p_226343_1_ && Math.abs(lvt_15_1_) <= (double) p_226343_1_) {
                int lvt_17_1_ = p_226343_0_.nextInt(2 * p_226343_2_ + 1) - p_226343_2_ + p_226343_3_;
                return AMBlockPos.fromCoords(lvt_13_1_, (double) lvt_17_1_, lvt_15_1_);
            } else {
                return null;
            }
        } else {
            int lvt_7_1_ = p_226343_0_.nextInt(2 * p_226343_1_ + 1) - p_226343_1_;
            int lvt_8_1_ = p_226343_0_.nextInt(2 * p_226343_2_ + 1) - p_226343_2_ + p_226343_3_;
            int lvt_9_1_ = p_226343_0_.nextInt(2 * p_226343_1_ + 1) - p_226343_1_;
            return new BlockPos(lvt_7_1_, lvt_8_1_, lvt_9_1_);
        }
    }

    public static BlockPos moveUpToAboveSolid(BlockPos p_226342_0_, int p_226342_1_, int p_226342_2_, Predicate<BlockPos> p_226342_3_) {
        if (p_226342_1_ < 0) {
            throw new IllegalArgumentException("aboveSolidAmount was " + p_226342_1_ + ", expected >= 0");
        } else if (!p_226342_3_.test(p_226342_0_)) {
            return p_226342_0_;
        } else {
            BlockPos lvt_4_1_ = p_226342_0_.above();
            while (lvt_4_1_.m_123342_() < p_226342_2_ && p_226342_3_.test(lvt_4_1_)) {
                lvt_4_1_ = lvt_4_1_.above();
            }
            BlockPos lvt_5_1_ = lvt_4_1_;
            while (lvt_5_1_.m_123342_() < p_226342_2_ && lvt_5_1_.m_123342_() - lvt_4_1_.m_123342_() < p_226342_1_) {
                BlockPos lvt_6_1_ = lvt_5_1_.above();
                if (p_226342_3_.test(lvt_6_1_)) {
                    break;
                }
                lvt_5_1_ = lvt_6_1_;
            }
            return lvt_5_1_;
        }
    }

    @Override
    public boolean canUse() {
        return this.leopard.m_5448_() != null && !this.leopard.isSleeping() && !this.leopard.isSitting() && (this.leopard.m_5448_().isAlive() || this.leopard.m_5448_() instanceof Player) && !this.leopard.m_6162_();
    }

    @Override
    public void start() {
        this.target = this.leopard.m_5448_();
        if (this.target instanceof Player && this.leopard.m_21188_() != null && this.leopard.m_21188_() == this.target) {
            this.stalk = this.leopard.m_20270_(this.target) > 10.0F;
        } else {
            this.stalk = this.leopard.m_20270_(this.target) > 4.0F;
        }
        this.secondPartOfLeap = false;
    }

    @Override
    public void stop() {
        this.secondPartOfLeap = false;
        this.stalk = false;
        this.leapPos = null;
        this.jumpCooldown = 0;
        this.leopard.setTackling(false);
        this.leopard.setSlSneaking(false);
    }

    @Override
    public void tick() {
        if (this.jumpCooldown > 0) {
            this.jumpCooldown--;
        }
        if (this.stalk) {
            if (this.secondPartOfLeap) {
                this.leopard.setTackling(!this.leopard.m_20096_());
                this.leopard.m_21391_(this.target, 180.0F, 10.0F);
                this.leopard.f_20883_ = this.leopard.m_146908_();
                if (this.leopard.m_20270_(this.target) < 3.0F && this.leopard.m_142582_(this.target)) {
                    this.target.hurt(this.leopard.m_269291_().mobAttack(this.leopard), (float) (this.leopard.m_21051_(Attributes.ATTACK_DAMAGE).getValue() * 2.5));
                    this.stalk = false;
                    this.secondPartOfLeap = false;
                } else if (this.leopard.m_20096_() && this.jumpCooldown == 0) {
                    this.leopard.setSlSneaking(false);
                    this.jumpCooldown = 10 + this.leopard.m_217043_().nextInt(10);
                    Vec3 vector3d = this.leopard.m_20184_();
                    Vec3 vector3d1 = new Vec3(this.target.m_20185_() - this.leopard.m_20185_(), 0.0, this.target.m_20189_() - this.leopard.m_20189_());
                    if (vector3d1.lengthSqr() > 1.0E-7) {
                        vector3d1 = vector3d1.normalize().scale(0.9).add(vector3d.scale(0.8));
                    }
                    this.leopard.m_20334_(vector3d1.x, vector3d1.y + 0.6F, vector3d1.z);
                }
            } else if (this.leapPos != null && !(this.target.m_20238_(this.leapPos) > 250.0)) {
                this.leopard.setSlSneaking(true);
                this.leopard.m_21573_().moveTo(this.leapPos.x, this.leapPos.y, this.leapPos.z, 1.0);
                if (this.leopard.m_20275_(this.leapPos.x, this.leapPos.y, this.leapPos.z) < 9.0 && this.leopard.m_142582_(this.target)) {
                    this.secondPartOfLeap = true;
                    this.leopard.m_21573_().stop();
                }
            } else {
                Vec3 vector3d1 = this.calculateFarPoint(50.0);
                if (vector3d1 != null) {
                    this.leapPos = vector3d1;
                } else {
                    this.leapPos = LandRandomPos.getPosTowards(this.leopard, 10, 10, this.target.m_20182_());
                }
            }
        } else {
            this.leopard.setSlSneaking(false);
            this.leopard.m_21573_().moveTo(this.target, 1.0);
            if (this.leopard.m_20270_(this.target) < 3.0F) {
                if (this.leopard.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
                    this.leopard.setAnimation(this.leopard.m_217043_().nextBoolean() ? EntitySnowLeopard.ANIMATION_ATTACK_R : EntitySnowLeopard.ANIMATION_ATTACK_L);
                } else if (this.leopard.getAnimationTick() == 5) {
                    this.leopard.m_7327_(this.target);
                }
            }
        }
    }

    private Vec3 calculateFarPoint(double dist) {
        Vec3 highest = null;
        for (int i = 0; i < 10; i++) {
            Vec3 vector3d1 = this.calculateVantagePoint(this.target, 8, 3, 1, this.target.m_20182_().subtract(this.leopard.m_20185_(), this.leopard.m_20186_(), this.leopard.m_20189_()), false, (float) (Math.PI / 2), this.leopard::m_21692_, false, 0, 0, true);
            if (vector3d1 != null && this.target.m_20238_(vector3d1) > dist && (highest == null || highest.y() < vector3d1.y)) {
                highest = vector3d1;
            }
        }
        return highest;
    }

    @Nullable
    private Vec3 calculateVantagePoint(LivingEntity creature, int xz, int y, int p_226339_3_, @Nullable Vec3 p_226339_4_, boolean p_226339_5_, double p_226339_6_, ToDoubleFunction<BlockPos> p_226339_8_, boolean p_226339_9_, int p_226339_10_, int p_226339_11_, boolean p_226339_12_) {
        PathNavigation lvt_13_1_ = this.leopard.m_21573_();
        RandomSource lvt_14_1_ = creature.getRandom();
        boolean lvt_15_2_;
        if (this.leopard.m_21536_()) {
            lvt_15_2_ = this.leopard.m_21534_().m_203195_(creature.m_20182_(), (double) (this.leopard.m_21535_() + (float) xz) + 1.0);
        } else {
            lvt_15_2_ = false;
        }
        boolean lvt_16_1_ = false;
        double lvt_17_1_ = Double.NEGATIVE_INFINITY;
        BlockPos lvt_19_1_ = creature.m_20183_();
        for (int lvt_20_1_ = 0; lvt_20_1_ < 10; lvt_20_1_++) {
            BlockPos lvt_21_1_ = getRandomDelta(lvt_14_1_, xz, y, p_226339_3_, p_226339_4_, p_226339_6_);
            if (lvt_21_1_ != null) {
                int lvt_22_1_ = lvt_21_1_.m_123341_();
                int lvt_23_1_ = lvt_21_1_.m_123342_();
                int lvt_24_1_ = lvt_21_1_.m_123343_();
                if (this.leopard.m_21536_() && xz > 1) {
                    BlockPos lvt_25_2_ = this.leopard.m_21534_();
                    if (creature.m_20185_() > (double) lvt_25_2_.m_123341_()) {
                        lvt_22_1_ -= lvt_14_1_.nextInt(xz / 2);
                    } else {
                        lvt_22_1_ += lvt_14_1_.nextInt(xz / 2);
                    }
                    if (creature.m_20189_() > (double) lvt_25_2_.m_123343_()) {
                        lvt_24_1_ -= lvt_14_1_.nextInt(xz / 2);
                    } else {
                        lvt_24_1_ += lvt_14_1_.nextInt(xz / 2);
                    }
                }
                BlockPos lvt_25_2_x = AMBlockPos.fromCoords((double) lvt_22_1_ + creature.m_20185_(), (double) lvt_23_1_ + creature.m_20186_(), (double) lvt_24_1_ + creature.m_20189_());
                if (lvt_25_2_x.m_123342_() >= 0 && lvt_25_2_x.m_123342_() <= creature.m_9236_().m_151558_() && (!lvt_15_2_ || this.leopard.m_21444_(lvt_25_2_x)) && (!p_226339_12_ || lvt_13_1_.isStableDestination(lvt_25_2_x))) {
                    if (p_226339_9_) {
                        lvt_25_2_x = moveUpToAboveSolid(lvt_25_2_x, lvt_14_1_.nextInt(p_226339_10_ + 1) + p_226339_11_, creature.m_9236_().m_151558_(), p_226341_1_ -> creature.m_9236_().getBlockState(p_226341_1_).m_280296_());
                    }
                    if (p_226339_5_ || !creature.m_9236_().getFluidState(lvt_25_2_x).is(FluidTags.WATER)) {
                        BlockPathTypes lvt_26_1_ = WalkNodeEvaluator.getBlockPathTypeStatic(creature.m_9236_(), lvt_25_2_x.mutable());
                        if (this.leopard.m_21439_(lvt_26_1_) == 0.0F) {
                            double lvt_27_1_ = p_226339_8_.applyAsDouble(lvt_25_2_x);
                            if (lvt_27_1_ > lvt_17_1_) {
                                lvt_17_1_ = lvt_27_1_;
                                lvt_19_1_ = lvt_25_2_x;
                                lvt_16_1_ = true;
                            }
                        }
                    }
                }
            }
        }
        return lvt_16_1_ ? Vec3.atBottomCenterOf(lvt_19_1_) : null;
    }
}