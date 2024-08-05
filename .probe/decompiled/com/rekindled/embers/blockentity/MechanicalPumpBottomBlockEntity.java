package com.rekindled.embers.blockentity;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.capabilities.EmbersCapabilities;
import com.rekindled.embers.api.event.DialInformationEvent;
import com.rekindled.embers.api.event.EmberEvent;
import com.rekindled.embers.api.power.IEmberCapability;
import com.rekindled.embers.api.tile.IExtraDialInformation;
import com.rekindled.embers.api.tile.IMechanicallyPowered;
import com.rekindled.embers.api.upgrades.UpgradeContext;
import com.rekindled.embers.api.upgrades.UpgradeUtil;
import com.rekindled.embers.datagen.EmbersSounds;
import com.rekindled.embers.power.DefaultEmberCapability;
import com.rekindled.embers.util.Misc;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class MechanicalPumpBottomBlockEntity extends BlockEntity implements IMechanicallyPowered, IExtraDialInformation {

    public IEmberCapability capability = new DefaultEmberCapability() {

        @Override
        public void onContentsChanged() {
            super.onContentsChanged();
            MechanicalPumpBottomBlockEntity.this.setChanged();
        }
    };

    public static final double EMBER_COST = 0.5;

    public int progress;

    public int totalProgress;

    public int lastProgress;

    private List<UpgradeContext> upgrades;

    public MechanicalPumpBottomBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.MECHANICAL_PUMP_BOTTOM_ENTITY.get(), pPos, pBlockState);
        this.capability.setEmberCapacity(1000.0);
    }

    public AABB getRenderBoundingBox() {
        return new AABB(this.f_58858_.offset(0, 1, 0), this.f_58858_.offset(1, 3, 1));
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.capability.deserializeNBT(nbt);
        if (nbt.contains("progress")) {
            this.progress = nbt.getInt("progress");
        }
        this.totalProgress = nbt.getInt("totalProgress");
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        this.capability.writeToNBT(nbt);
        nbt.putInt("progress", this.progress);
        nbt.putInt("totalProgress", this.totalProgress);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        nbt.putInt("totalProgress", this.totalProgress);
        return nbt;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return !this.f_58859_ && cap == EmbersCapabilities.EMBER_CAPABILITY ? this.capability.getCapability(cap, side) : super.getCapability(cap, side);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, MechanicalPumpBottomBlockEntity blockEntity) {
        blockEntity.upgrades = UpgradeUtil.getUpgrades(level, pos, Misc.horizontals);
        UpgradeUtil.verifyUpgrades(blockEntity, blockEntity.upgrades);
        if (!UpgradeUtil.doTick(blockEntity, blockEntity.upgrades)) {
            double emberCost = UpgradeUtil.getTotalEmberConsumption(blockEntity, 0.5, blockEntity.upgrades);
            if (blockEntity.capability.getEmber() >= emberCost) {
                boolean cancel = UpgradeUtil.doWork(blockEntity, blockEntity.upgrades);
                if (!cancel) {
                    int speed = (int) UpgradeUtil.getTotalSpeedModifier(blockEntity, blockEntity.upgrades);
                    blockEntity.progress += speed;
                    blockEntity.totalProgress += speed;
                    UpgradeUtil.throwEvent(blockEntity, new EmberEvent(blockEntity, EmberEvent.EnumType.CONSUME, emberCost), blockEntity.upgrades);
                    blockEntity.capability.removeAmount(emberCost, true);
                    if (blockEntity.progress > 400) {
                        blockEntity.progress -= 400;
                        blockEntity.attemptPump(pos.below());
                        blockEntity.playSound(speed);
                    }
                    blockEntity.setChanged();
                }
            }
        }
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, MechanicalPumpBottomBlockEntity blockEntity) {
        blockEntity.upgrades = UpgradeUtil.getUpgrades(level, pos, Misc.horizontals);
        UpgradeUtil.verifyUpgrades(blockEntity, blockEntity.upgrades);
        blockEntity.lastProgress = blockEntity.totalProgress;
    }

    public boolean attemptPump(BlockPos pos) {
        BlockState state = this.f_58857_.getBlockState(pos);
        if (state.m_60734_() instanceof BucketPickup && !state.m_60819_().isEmpty() && state.m_60819_().isSource()) {
            FluidStack stack = new FluidStack((Fluid) state.m_60819_().holder().get(), 1000);
            if (stack != null) {
                MechanicalPumpTopBlockEntity t = (MechanicalPumpTopBlockEntity) this.f_58857_.getBlockEntity(this.f_58858_.above());
                int filled = t.getTank().fill(stack, IFluidHandler.FluidAction.SIMULATE);
                if (filled == stack.getAmount()) {
                    t.getTank().fill(stack, IFluidHandler.FluidAction.EXECUTE);
                    ((BucketPickup) state.m_60734_()).pickupBlock(this.f_58857_, pos, state);
                    return false;
                }
            }
        }
        return true;
    }

    public void playSound(int speed) {
        float pitch;
        SoundEvent sound;
        if (speed >= 20) {
            sound = EmbersSounds.PUMP_FAST.get();
            pitch = (float) speed / 20.0F;
        } else if (speed >= 10) {
            sound = EmbersSounds.PUMP_MID.get();
            pitch = (float) speed / 10.0F;
        } else {
            sound = EmbersSounds.PUMP_SLOW.get();
            pitch = (float) speed;
        }
        this.f_58857_.playSound(null, this.f_58858_.above(), sound, SoundSource.BLOCKS, 1.0F, pitch);
    }

    @Override
    public double getMechanicalSpeed(double power) {
        return Math.min(power / 2.0, 100.0);
    }

    @Override
    public double getMinimumPower() {
        return 2.0;
    }

    @Override
    public double getNominalSpeed() {
        return 10.0;
    }

    @Override
    public void addDialInformation(Direction facing, List<Component> information, String dialType) {
        UpgradeUtil.throwEvent(this, new DialInformationEvent(this, information, dialType), this.upgrades);
    }

    public void invalidateCaps() {
        super.invalidateCaps();
        this.capability.invalidate();
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (this.f_58857_ instanceof ServerLevel) {
            ((ServerLevel) this.f_58857_).getChunkSource().blockChanged(this.f_58858_);
        }
    }
}