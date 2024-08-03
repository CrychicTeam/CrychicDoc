package com.simibubi.create.content.kinetics.clock;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.advancement.CreateAdvancement;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.damageTypes.CreateDamageSources;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.NBTHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class CuckooClockBlockEntity extends KineticBlockEntity {

    public LerpedFloat hourHand = LerpedFloat.angular();

    public LerpedFloat minuteHand = LerpedFloat.angular();

    public LerpedFloat animationProgress = LerpedFloat.linear();

    public CuckooClockBlockEntity.Animation animationType = CuckooClockBlockEntity.Animation.NONE;

    private boolean sendAnimationUpdate;

    public CuckooClockBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        this.registerAwardables(behaviours, new CreateAdvancement[] { AllAdvancements.CUCKOO_CLOCK });
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        if (clientPacket && compound.contains("Animation")) {
            this.animationType = NBTHelper.readEnum(compound, "Animation", CuckooClockBlockEntity.Animation.class);
            this.animationProgress.startWithValue(0.0);
        }
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        if (clientPacket && this.sendAnimationUpdate) {
            NBTHelper.writeEnum(compound, "Animation", this.animationType);
        }
        this.sendAnimationUpdate = false;
        super.write(compound, clientPacket);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getSpeed() != 0.0F) {
            boolean isNatural = this.f_58857_.dimensionType().natural();
            int dayTime = (int) (this.f_58857_.getDayTime() * (long) (isNatural ? 1 : 24) % 24000L);
            int hours = (dayTime / 1000 + 6) % 24;
            int minutes = dayTime % 1000 * 60 / 1000;
            if (!isNatural) {
                if (this.f_58857_.isClientSide) {
                    this.moveHands(hours, minutes);
                    if (AnimationTickHolder.getTicks() % 6 == 0) {
                        this.playSound((SoundEvent) SoundEvents.NOTE_BLOCK_HAT.get(), 0.0625F, 2.0F);
                    } else if (AnimationTickHolder.getTicks() % 3 == 0) {
                        this.playSound((SoundEvent) SoundEvents.NOTE_BLOCK_HAT.get(), 0.0625F, 1.5F);
                    }
                }
            } else {
                if (!this.f_58857_.isClientSide) {
                    if (this.animationType == CuckooClockBlockEntity.Animation.NONE) {
                        if (hours == 12 && minutes < 5) {
                            this.startAnimation(CuckooClockBlockEntity.Animation.PIG);
                        }
                        if (hours == 18 && minutes < 36 && minutes > 31) {
                            this.startAnimation(CuckooClockBlockEntity.Animation.CREEPER);
                        }
                    } else {
                        float value = this.animationProgress.getValue();
                        this.animationProgress.setValue((double) (value + 1.0F));
                        if (value > 100.0F) {
                            this.animationType = CuckooClockBlockEntity.Animation.NONE;
                        }
                        if (this.animationType == CuckooClockBlockEntity.Animation.SURPRISE && Mth.equal(this.animationProgress.getValue(), 50.0F)) {
                            Vec3 center = VecHelper.getCenterOf(this.f_58858_);
                            this.f_58857_.m_46961_(this.f_58858_, false);
                            DamageSource damageSource = CreateDamageSources.cuckooSurprise(this.f_58857_);
                            this.f_58857_.explode(null, damageSource, null, center.x, center.y, center.z, 3.0F, false, Level.ExplosionInteraction.BLOCK);
                        }
                    }
                }
                if (this.f_58857_.isClientSide) {
                    this.moveHands(hours, minutes);
                    if (this.animationType == CuckooClockBlockEntity.Animation.NONE) {
                        if (AnimationTickHolder.getTicks() % 32 == 0) {
                            this.playSound((SoundEvent) SoundEvents.NOTE_BLOCK_HAT.get(), 0.0625F, 2.0F);
                        } else if (AnimationTickHolder.getTicks() % 16 == 0) {
                            this.playSound((SoundEvent) SoundEvents.NOTE_BLOCK_HAT.get(), 0.0625F, 1.5F);
                        }
                    } else {
                        boolean isSurprise = this.animationType == CuckooClockBlockEntity.Animation.SURPRISE;
                        float valuex = this.animationProgress.getValue();
                        this.animationProgress.setValue((double) (valuex + 1.0F));
                        if (valuex > 100.0F) {
                            this.animationType = null;
                        }
                        if (valuex == 1.0F) {
                            this.playSound((SoundEvent) SoundEvents.NOTE_BLOCK_CHIME.get(), 2.0F, 0.5F);
                        }
                        if (valuex == 21.0F) {
                            this.playSound((SoundEvent) SoundEvents.NOTE_BLOCK_CHIME.get(), 2.0F, 0.793701F);
                        }
                        if (valuex > 30.0F && isSurprise) {
                            Vec3 pos = VecHelper.offsetRandomly(VecHelper.getCenterOf(this.f_58858_), this.f_58857_.random, 0.5F);
                            this.f_58857_.addParticle(ParticleTypes.LARGE_SMOKE, pos.x, pos.y, pos.z, 0.0, 0.0, 0.0);
                        }
                        if (valuex == 40.0F && isSurprise) {
                            this.playSound(SoundEvents.TNT_PRIMED, 1.0F, 1.0F);
                        }
                        int step = isSurprise ? 3 : 15;
                        for (int phase = 30; phase <= 60; phase += step) {
                            if (valuex == (float) (phase - step / 3)) {
                                this.playSound(SoundEvents.CHEST_OPEN, 0.0625F, 2.0F);
                            }
                            if (valuex == (float) phase) {
                                if (this.animationType == CuckooClockBlockEntity.Animation.PIG) {
                                    this.playSound(SoundEvents.PIG_AMBIENT, 0.25F, 1.0F);
                                } else {
                                    this.playSound(SoundEvents.CREEPER_HURT, 0.25F, 3.0F);
                                }
                            }
                            if (valuex == (float) (phase + step / 3)) {
                                this.playSound(SoundEvents.CHEST_CLOSE, 0.0625F, 2.0F);
                            }
                        }
                    }
                }
            }
        }
    }

    public void startAnimation(CuckooClockBlockEntity.Animation animation) {
        this.animationType = animation;
        if (animation != null && CuckooClockBlock.containsSurprise(this.m_58900_())) {
            this.animationType = CuckooClockBlockEntity.Animation.SURPRISE;
        }
        this.animationProgress.startWithValue(0.0);
        this.sendAnimationUpdate = true;
        if (animation == CuckooClockBlockEntity.Animation.CREEPER) {
            this.awardIfNear(AllAdvancements.CUCKOO_CLOCK, 32);
        }
        this.sendData();
    }

    public void moveHands(int hours, int minutes) {
        float hourTarget = (float) (30 * (hours % 12));
        float minuteTarget = (float) (6 * minutes);
        this.hourHand.chase((double) hourTarget, 0.2F, LerpedFloat.Chaser.EXP);
        this.minuteHand.chase((double) minuteTarget, 0.2F, LerpedFloat.Chaser.EXP);
        this.hourHand.tickChaser();
        this.minuteHand.tickChaser();
    }

    private void playSound(SoundEvent sound, float volume, float pitch) {
        Vec3 vec = VecHelper.getCenterOf(this.f_58858_);
        this.f_58857_.playLocalSound(vec.x, vec.y, vec.z, sound, SoundSource.BLOCKS, volume, pitch, false);
    }

    static enum Animation {

        PIG, CREEPER, SURPRISE, NONE
    }
}