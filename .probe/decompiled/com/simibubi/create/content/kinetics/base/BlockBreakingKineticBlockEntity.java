package com.simibubi.create.content.kinetics.base;

import com.simibubi.create.foundation.utility.BlockHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public abstract class BlockBreakingKineticBlockEntity extends KineticBlockEntity {

    public static final AtomicInteger NEXT_BREAKER_ID = new AtomicInteger();

    protected int ticksUntilNextProgress;

    protected int destroyProgress;

    protected int breakerId = -NEXT_BREAKER_ID.incrementAndGet();

    protected BlockPos breakingPos;

    public BlockBreakingKineticBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void onSpeedChanged(float prevSpeed) {
        super.onSpeedChanged(prevSpeed);
        if (this.destroyProgress == -1) {
            this.destroyNextTick();
        }
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        if (this.ticksUntilNextProgress == -1) {
            this.destroyNextTick();
        }
    }

    public void destroyNextTick() {
        this.ticksUntilNextProgress = 1;
    }

    protected abstract BlockPos getBreakingPos();

    protected boolean shouldRun() {
        return true;
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.putInt("Progress", this.destroyProgress);
        compound.putInt("NextTick", this.ticksUntilNextProgress);
        if (this.breakingPos != null) {
            compound.put("Breaking", NbtUtils.writeBlockPos(this.breakingPos));
        }
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        this.destroyProgress = compound.getInt("Progress");
        this.ticksUntilNextProgress = compound.getInt("NextTick");
        if (compound.contains("Breaking")) {
            this.breakingPos = NbtUtils.readBlockPos(compound.getCompound("Breaking"));
        }
        super.read(compound, clientPacket);
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if (!this.f_58857_.isClientSide && this.destroyProgress != 0) {
            this.f_58857_.destroyBlockProgress(this.breakerId, this.breakingPos, -1);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.f_58857_.isClientSide) {
            if (this.shouldRun()) {
                if (this.getSpeed() != 0.0F) {
                    this.breakingPos = this.getBreakingPos();
                    if (this.ticksUntilNextProgress >= 0) {
                        if (this.ticksUntilNextProgress-- <= 0) {
                            BlockState stateToBreak = this.f_58857_.getBlockState(this.breakingPos);
                            float blockHardness = stateToBreak.m_60800_(this.f_58857_, this.breakingPos);
                            if (!this.canBreak(stateToBreak, blockHardness)) {
                                if (this.destroyProgress != 0) {
                                    this.destroyProgress = 0;
                                    this.f_58857_.destroyBlockProgress(this.breakerId, this.breakingPos, -1);
                                }
                            } else {
                                float breakSpeed = this.getBreakSpeed();
                                this.destroyProgress = this.destroyProgress + Mth.clamp((int) (breakSpeed / blockHardness), 1, 10 - this.destroyProgress);
                                this.f_58857_.playSound(null, this.f_58858_, stateToBreak.m_60827_().getHitSound(), SoundSource.NEUTRAL, 0.25F, 1.0F);
                                if (this.destroyProgress >= 10) {
                                    this.onBlockBroken(stateToBreak);
                                    this.destroyProgress = 0;
                                    this.ticksUntilNextProgress = -1;
                                    this.f_58857_.destroyBlockProgress(this.breakerId, this.breakingPos, -1);
                                } else {
                                    this.ticksUntilNextProgress = (int) (blockHardness / breakSpeed);
                                    this.f_58857_.destroyBlockProgress(this.breakerId, this.breakingPos, this.destroyProgress);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean canBreak(BlockState stateToBreak, float blockHardness) {
        return isBreakable(stateToBreak, blockHardness);
    }

    public static boolean isBreakable(BlockState stateToBreak, float blockHardness) {
        return !stateToBreak.m_278721_() && !(stateToBreak.m_60734_() instanceof AirBlock) && blockHardness != -1.0F;
    }

    public void onBlockBroken(BlockState stateToBreak) {
        Vec3 vec = VecHelper.offsetRandomly(VecHelper.getCenterOf(this.breakingPos), this.f_58857_.random, 0.125F);
        BlockHelper.destroyBlock(this.f_58857_, this.breakingPos, 1.0F, stack -> {
            if (!stack.isEmpty()) {
                if (this.f_58857_.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
                    if (!this.f_58857_.restoringBlockSnapshots) {
                        ItemEntity itementity = new ItemEntity(this.f_58857_, vec.x, vec.y, vec.z, stack);
                        itementity.setDefaultPickUpDelay();
                        itementity.m_20256_(Vec3.ZERO);
                        this.f_58857_.m_7967_(itementity);
                    }
                }
            }
        });
    }

    protected float getBreakSpeed() {
        return Math.abs(this.getSpeed() / 100.0F);
    }
}