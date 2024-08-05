package com.simibubi.create.content.kinetics.base;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.OrientedContraptionEntity;
import com.simibubi.create.content.contraptions.behaviour.MovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.mounted.MountedContraption;
import com.simibubi.create.content.trains.entity.CarriageContraption;
import com.simibubi.create.foundation.utility.BlockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class BlockBreakingMovementBehaviour implements MovementBehaviour {

    @Override
    public void startMoving(MovementContext context) {
        if (!context.world.isClientSide) {
            context.data.putInt("BreakerId", -BlockBreakingKineticBlockEntity.NEXT_BREAKER_ID.incrementAndGet());
        }
    }

    @Override
    public void visitNewPosition(MovementContext context, BlockPos pos) {
        Level world = context.world;
        BlockState stateVisited = world.getBlockState(pos);
        if (!stateVisited.m_60796_(world, pos)) {
            this.damageEntities(context, pos, world);
        }
        if (!world.isClientSide) {
            if (this.canBreak(world, pos, stateVisited)) {
                context.data.put("BreakingPos", NbtUtils.writeBlockPos(pos));
                context.stall = true;
            }
        }
    }

    public void damageEntities(MovementContext context, BlockPos pos, Level world) {
        if (context.contraption.entity instanceof OrientedContraptionEntity oce && oce.nonDamageTicks > 0) {
            return;
        }
        DamageSource damageSource = this.getDamageSource(world);
        if (damageSource != null || this.throwsEntities(world)) {
            label69: for (Entity entity : world.m_45976_(Entity.class, new AABB(pos))) {
                if (!(entity instanceof ItemEntity) && !(entity instanceof AbstractContraptionEntity) && !entity.isPassengerOfSameVehicle(context.contraption.entity)) {
                    if (entity instanceof AbstractMinecart) {
                        for (Entity passenger : entity.getIndirectPassengers()) {
                            if (passenger instanceof AbstractContraptionEntity && ((AbstractContraptionEntity) passenger).getContraption() == context.contraption) {
                                continue label69;
                            }
                        }
                    }
                    if (damageSource != null && !world.isClientSide) {
                        float damage = (float) Mth.clamp(6.0 * Math.pow(context.relativeMotion.length(), 0.4) + 1.0, 2.0, 10.0);
                        entity.hurt(damageSource, damage);
                    }
                    if (this.throwsEntities(world) && world.isClientSide == (entity instanceof Player)) {
                        this.throwEntity(context, entity);
                    }
                }
            }
        }
    }

    protected void throwEntity(MovementContext context, Entity entity) {
        Vec3 motionBoost = context.motion.add(0.0, context.motion.length() / 4.0, 0.0);
        int maxBoost = 4;
        if (motionBoost.length() > (double) maxBoost) {
            motionBoost = motionBoost.subtract(motionBoost.normalize().scale(motionBoost.length() - (double) maxBoost));
        }
        entity.setDeltaMovement(entity.getDeltaMovement().add(motionBoost));
        entity.hurtMarked = true;
    }

    protected DamageSource getDamageSource(Level level) {
        return null;
    }

    protected boolean throwsEntities(Level level) {
        return this.getDamageSource(level) != null;
    }

    @Override
    public void cancelStall(MovementContext context) {
        CompoundTag data = context.data;
        if (!context.world.isClientSide) {
            if (data.contains("BreakingPos")) {
                Level world = context.world;
                int id = data.getInt("BreakerId");
                BlockPos breakingPos = NbtUtils.readBlockPos(data.getCompound("BreakingPos"));
                data.remove("Progress");
                data.remove("TicksUntilNextProgress");
                data.remove("BreakingPos");
                MovementBehaviour.super.cancelStall(context);
                world.destroyBlockProgress(id, breakingPos, -1);
            }
        }
    }

    @Override
    public void stopMoving(MovementContext context) {
        this.cancelStall(context);
    }

    @Override
    public void tick(MovementContext context) {
        this.tickBreaker(context);
        CompoundTag data = context.data;
        if (data.contains("WaitingTicks")) {
            int waitingTicks = data.getInt("WaitingTicks");
            if (waitingTicks-- > 0) {
                data.putInt("WaitingTicks", waitingTicks);
                context.stall = true;
            } else {
                BlockPos pos = NbtUtils.readBlockPos(data.getCompound("LastPos"));
                data.remove("WaitingTicks");
                data.remove("LastPos");
                context.stall = false;
                this.visitNewPosition(context, pos);
            }
        }
    }

    public void tickBreaker(MovementContext context) {
        CompoundTag data = context.data;
        if (!context.world.isClientSide) {
            if (!data.contains("BreakingPos")) {
                context.stall = false;
            } else if (context.relativeMotion.equals(Vec3.ZERO)) {
                context.stall = false;
            } else {
                int ticksUntilNextProgress = data.getInt("TicksUntilNextProgress");
                if (ticksUntilNextProgress-- > 0) {
                    data.putInt("TicksUntilNextProgress", ticksUntilNextProgress);
                } else {
                    Level world = context.world;
                    BlockPos breakingPos = NbtUtils.readBlockPos(data.getCompound("BreakingPos"));
                    int destroyProgress = data.getInt("Progress");
                    int id = data.getInt("BreakerId");
                    BlockState stateToBreak = world.getBlockState(breakingPos);
                    float blockHardness = stateToBreak.m_60800_(world, breakingPos);
                    if (!this.canBreak(world, breakingPos, stateToBreak)) {
                        if (destroyProgress != 0) {
                            int var17 = false;
                            data.remove("Progress");
                            data.remove("TicksUntilNextProgress");
                            data.remove("BreakingPos");
                            world.destroyBlockProgress(id, breakingPos, -1);
                        }
                        context.stall = false;
                    } else {
                        float breakSpeed = this.getBlockBreakingSpeed(context);
                        destroyProgress += Mth.clamp((int) (breakSpeed / blockHardness), 1, 10 - destroyProgress);
                        world.playSound(null, breakingPos, stateToBreak.m_60827_().getHitSound(), SoundSource.NEUTRAL, 0.25F, 1.0F);
                        if (destroyProgress < 10) {
                            ticksUntilNextProgress = (int) (blockHardness / breakSpeed);
                            world.destroyBlockProgress(id, breakingPos, destroyProgress);
                            data.putInt("TicksUntilNextProgress", ticksUntilNextProgress);
                            data.putInt("Progress", destroyProgress);
                        } else {
                            world.destroyBlockProgress(id, breakingPos, -1);
                            BlockPos ogPos = breakingPos;
                            for (BlockState stateAbove = world.getBlockState(breakingPos.above()); stateAbove.m_60734_() instanceof FallingBlock; stateAbove = world.getBlockState(breakingPos.above())) {
                                breakingPos = breakingPos.above();
                            }
                            stateToBreak = world.getBlockState(breakingPos);
                            context.stall = false;
                            if (this.shouldDestroyStartBlock(stateToBreak)) {
                                this.destroyBlock(context, breakingPos);
                            }
                            this.onBlockBroken(context, ogPos, stateToBreak);
                            int var14 = -1;
                            data.remove("Progress");
                            data.remove("TicksUntilNextProgress");
                            data.remove("BreakingPos");
                        }
                    }
                }
            }
        }
    }

    protected void destroyBlock(MovementContext context, BlockPos breakingPos) {
        BlockHelper.destroyBlock(context.world, breakingPos, 1.0F, stack -> this.dropItem(context, stack));
    }

    protected float getBlockBreakingSpeed(MovementContext context) {
        float lowerLimit = 0.0078125F;
        if (context.contraption instanceof MountedContraption) {
            lowerLimit = 1.0F;
        }
        if (context.contraption instanceof CarriageContraption) {
            lowerLimit = 2.0F;
        }
        return Mth.clamp(Math.abs(context.getAnimationSpeed()) / 500.0F, lowerLimit, 16.0F);
    }

    protected boolean shouldDestroyStartBlock(BlockState stateToBreak) {
        return true;
    }

    public boolean canBreak(Level world, BlockPos breakingPos, BlockState state) {
        float blockHardness = state.m_60800_(world, breakingPos);
        return BlockBreakingKineticBlockEntity.isBreakable(state, blockHardness);
    }

    protected void onBlockBroken(MovementContext context, BlockPos pos, BlockState brokenState) {
        if (brokenState.m_60734_() instanceof FallingBlock) {
            CompoundTag data = context.data;
            data.putInt("WaitingTicks", 10);
            data.put("LastPos", NbtUtils.writeBlockPos(pos));
            context.stall = true;
        }
    }
}