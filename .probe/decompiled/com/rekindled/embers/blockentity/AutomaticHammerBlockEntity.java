package com.rekindled.embers.blockentity;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.capabilities.EmbersCapabilities;
import com.rekindled.embers.api.event.DialInformationEvent;
import com.rekindled.embers.api.event.EmberEvent;
import com.rekindled.embers.api.power.IEmberCapability;
import com.rekindled.embers.api.tile.IExtraDialInformation;
import com.rekindled.embers.api.tile.IHammerable;
import com.rekindled.embers.api.tile.IMechanicallyPowered;
import com.rekindled.embers.api.upgrades.UpgradeContext;
import com.rekindled.embers.api.upgrades.UpgradeUtil;
import com.rekindled.embers.power.DefaultEmberCapability;
import com.rekindled.embers.util.Misc;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class AutomaticHammerBlockEntity extends BlockEntity implements IMechanicallyPowered, IExtraDialInformation {

    public static final double EMBER_COST = 40.0;

    public static final int PROCESS_TIME = 20;

    public IEmberCapability capability = new DefaultEmberCapability() {

        @Override
        public void onContentsChanged() {
            super.onContentsChanged();
            AutomaticHammerBlockEntity.this.setChanged();
        }
    };

    public long startTime = -1L;

    public int processTime = -1;

    protected List<UpgradeContext> upgrades = new ArrayList();

    public AutomaticHammerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.AUTOMATIC_HAMMER_ENTITY.get(), pPos, pBlockState);
        this.capability.setEmberCapacity(12000.0);
    }

    public AABB getRenderBoundingBox() {
        return new AABB(this.f_58858_.offset(-1, -1, -1), this.f_58858_.offset(2, 2, 2));
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.capability.deserializeNBT(nbt);
        this.startTime = nbt.getLong("startTime");
        this.processTime = nbt.getInt("processTime");
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        this.capability.writeToNBT(nbt);
        nbt.putLong("startTime", this.startTime);
        nbt.putInt("processTime", this.processTime);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        nbt.putLong("startTime", this.startTime);
        nbt.putInt("processTime", this.processTime);
        return nbt;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, AutomaticHammerBlockEntity blockEntity) {
        Direction facing = (Direction) state.m_61143_(BlockStateProperties.HORIZONTAL_FACING);
        blockEntity.upgrades = UpgradeUtil.getUpgrades(level, pos, new Direction[] { facing.getOpposite() });
        UpgradeUtil.verifyUpgrades(blockEntity, blockEntity.upgrades);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, AutomaticHammerBlockEntity blockEntity) {
        Direction facing = (Direction) state.m_61143_(BlockStateProperties.HORIZONTAL_FACING);
        blockEntity.upgrades = UpgradeUtil.getUpgrades(level, pos, new Direction[] { facing.getOpposite() });
        UpgradeUtil.verifyUpgrades(blockEntity, blockEntity.upgrades);
        if (!UpgradeUtil.doTick(blockEntity, blockEntity.upgrades)) {
            BlockEntity tile = level.getBlockEntity(pos.below().relative(facing));
            if (tile instanceof IHammerable) {
                double ember_cost = UpgradeUtil.getTotalEmberConsumption(blockEntity, 40.0, blockEntity.upgrades);
                IHammerable hammerable = (IHammerable) tile;
                boolean redstoneEnabled = level.m_276867_(pos);
                if (hammerable.isValid() && redstoneEnabled && blockEntity.capability.getEmber() >= ember_cost) {
                    boolean cancel = UpgradeUtil.doWork(blockEntity, blockEntity.upgrades);
                    int processTime = UpgradeUtil.getWorkTime(blockEntity, 20, blockEntity.upgrades);
                    if (!cancel && blockEntity.startTime + (long) processTime < level.getGameTime()) {
                        blockEntity.startTime = level.getGameTime();
                        blockEntity.processTime = processTime;
                        blockEntity.setChanged();
                    }
                }
                if (blockEntity.startTime + (long) (blockEntity.processTime / 2) == level.getGameTime() && blockEntity.capability.getEmber() >= ember_cost) {
                    UpgradeUtil.throwEvent(blockEntity, new EmberEvent(blockEntity, EmberEvent.EnumType.CONSUME, ember_cost), blockEntity.upgrades);
                    blockEntity.capability.removeAmount(ember_cost, true);
                    hammerable.onHit(blockEntity);
                }
            }
        }
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return !this.f_58859_ && cap == EmbersCapabilities.EMBER_CAPABILITY ? this.capability.getCapability(cap, side) : super.getCapability(cap, side);
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

    @Override
    public double getMechanicalSpeed(double power) {
        return Misc.getDiminishedPower(power, 20.0, 0.075);
    }

    @Override
    public double getNominalSpeed() {
        return 1.0;
    }

    @Override
    public double getMinimumPower() {
        return 10.0;
    }

    @Override
    public void addDialInformation(Direction facing, List<Component> information, String dialType) {
        UpgradeUtil.throwEvent(this, new DialInformationEvent(this, information, dialType), this.upgrades);
    }
}