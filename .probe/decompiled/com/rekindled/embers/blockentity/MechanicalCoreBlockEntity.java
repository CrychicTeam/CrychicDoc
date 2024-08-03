package com.rekindled.embers.blockentity;

import com.rekindled.embers.ConfigManager;
import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.tile.IExtraCapabilityInformation;
import com.rekindled.embers.api.tile.IExtraDialInformation;
import com.rekindled.embers.api.upgrades.IUpgradeProxy;
import com.rekindled.embers.api.upgrades.UpgradeContext;
import com.rekindled.embers.api.upgrades.UpgradeUtil;
import com.rekindled.embers.util.Misc;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class MechanicalCoreBlockEntity extends BlockEntity implements IExtraDialInformation, IExtraCapabilityInformation, IUpgradeProxy {

    public MechanicalCoreBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.MECHANICAL_CORE_ENTITY.get(), pPos, pBlockState);
    }

    public MechanicalCoreBlockEntity.BlockEntityDirection getAttachedMultiblock(int distanceLeft) {
        if (distanceLeft < 1) {
            return null;
        } else {
            BlockPos sidePos = this.f_58858_.relative(this.getAttachedSide());
            if (Misc.isSideProxyable(this.f_58857_.getBlockState(sidePos), this.getFace())) {
                return new MechanicalCoreBlockEntity.BlockEntityDirection(this.f_58857_.getBlockEntity(sidePos), this.getFace());
            } else {
                return this.f_58857_.getBlockEntity(sidePos) instanceof MechanicalCoreBlockEntity coreEntity ? coreEntity.getAttachedMultiblock(distanceLeft - 1) : null;
            }
        }
    }

    public BlockEntity getAttachedBlockEntity(int distanceLeft) {
        if (distanceLeft < 1) {
            return null;
        } else {
            BlockPos sidePos = this.f_58858_.relative(this.getAttachedSide());
            if (Misc.isSideProxyable(this.f_58857_.getBlockState(sidePos), this.getFace())) {
                return this.f_58857_.getBlockEntity(sidePos);
            } else {
                return this.f_58857_.getBlockEntity(sidePos) instanceof MechanicalCoreBlockEntity coreEntity ? coreEntity.getAttachedBlockEntity(distanceLeft - 1) : null;
            }
        }
    }

    public Direction getAttachedSide() {
        return this.getFace().getOpposite();
    }

    public Direction getFace() {
        return (Direction) this.m_58900_().m_61143_(BlockStateProperties.FACING);
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        MechanicalCoreBlockEntity.BlockEntityDirection multiblock = this.getAttachedMultiblock(ConfigManager.MAX_PROXY_DISTANCE.get());
        return multiblock != null ? multiblock.blockEntity.getCapability(cap, multiblock.direction) : super.getCapability(cap, side);
    }

    @Override
    public void addDialInformation(Direction facing, List<Component> information, String dialType) {
        MechanicalCoreBlockEntity.BlockEntityDirection multiblock = this.getAttachedMultiblock(ConfigManager.MAX_PROXY_DISTANCE.get());
        if (multiblock != null && multiblock.blockEntity instanceof IExtraDialInformation) {
            ((IExtraDialInformation) multiblock.blockEntity).addDialInformation(multiblock.direction, information, dialType);
        }
    }

    @Override
    public boolean hasCapabilityDescription(Capability<?> capability) {
        BlockEntity multiblock = this.getAttachedBlockEntity(ConfigManager.MAX_PROXY_DISTANCE.get());
        return multiblock instanceof IExtraCapabilityInformation ? ((IExtraCapabilityInformation) multiblock).hasCapabilityDescription(capability) : false;
    }

    @Override
    public void addCapabilityDescription(List<Component> strings, Capability<?> capability, Direction facing) {
        MechanicalCoreBlockEntity.BlockEntityDirection multiblock = this.getAttachedMultiblock(ConfigManager.MAX_PROXY_DISTANCE.get());
        if (multiblock != null && multiblock.blockEntity instanceof IExtraCapabilityInformation) {
            ((IExtraCapabilityInformation) multiblock.blockEntity).addCapabilityDescription(strings, capability, multiblock.direction);
        }
    }

    @Override
    public void addOtherDescription(List<Component> strings, Direction facing) {
        MechanicalCoreBlockEntity.BlockEntityDirection multiblock = this.getAttachedMultiblock(ConfigManager.MAX_PROXY_DISTANCE.get());
        if (multiblock != null && multiblock.blockEntity instanceof IExtraCapabilityInformation) {
            ((IExtraCapabilityInformation) multiblock.blockEntity).addOtherDescription(strings, multiblock.direction);
        }
    }

    @Override
    public void collectUpgrades(List<UpgradeContext> upgrades, int distanceLeft) {
        for (Direction facing : Direction.values()) {
            if (this.isSocket(facing)) {
                UpgradeUtil.collectUpgrades(this.f_58857_, this.f_58858_.relative(facing), facing.getOpposite(), upgrades, distanceLeft);
            }
        }
    }

    @Override
    public boolean isSocket(Direction facing) {
        return facing != this.getAttachedSide();
    }

    @Override
    public boolean isProvider(Direction facing) {
        return facing == this.getAttachedSide();
    }

    public static class BlockEntityDirection {

        public BlockEntity blockEntity;

        public Direction direction;

        public BlockEntityDirection(BlockEntity blockEntity, Direction direction) {
            this.blockEntity = blockEntity;
            this.direction = direction;
        }
    }
}