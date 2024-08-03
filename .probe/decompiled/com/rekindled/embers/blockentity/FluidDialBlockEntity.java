package com.rekindled.embers.blockentity;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.tile.IDialEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class FluidDialBlockEntity extends BlockEntity implements IDialEntity {

    public FluidStack[] fluids = new FluidStack[0];

    public int[] capacities = new int[0];

    public int extraLines = 0;

    public boolean display = false;

    public FluidDialBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.FLUID_DIAL_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public void load(CompoundTag nbt) {
        ListTag tanks = nbt.getList("tanks", 10);
        this.fluids = new FluidStack[tanks.size()];
        this.capacities = new int[tanks.size()];
        if (tanks.size() > 0) {
            for (int i = 0; i < tanks.size(); i++) {
                CompoundTag tank = tanks.getCompound(i);
                this.fluids[i] = FluidStack.loadFluidStackFromNBT(tank);
                this.capacities[i] = tank.getInt("capacity");
            }
        }
        if (nbt.contains("more_lines")) {
            this.extraLines = nbt.getInt("more_lines");
        }
        if (nbt.contains("display")) {
            this.display = nbt.getBoolean("display");
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.getUpdateTag(100);
    }

    public CompoundTag getUpdateTag(int maxLines) {
        CompoundTag nbt = super.getUpdateTag();
        BlockState state = this.f_58857_.getBlockState(this.f_58858_);
        boolean display = false;
        if (state.m_61138_(BlockStateProperties.FACING)) {
            Direction facing = (Direction) state.m_61143_(BlockStateProperties.FACING);
            BlockEntity blockEntity = this.f_58857_.getBlockEntity(this.f_58858_.relative(facing, -1));
            if (blockEntity != null) {
                IFluidHandler cap = (IFluidHandler) blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER, facing.getOpposite()).orElse((IFluidHandler) blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER, null).orElse(null));
                if (cap != null) {
                    ListTag tanks = new ListTag();
                    for (int i = 0; i < cap.getTanks() && i + this.extraLines < maxLines; i++) {
                        FluidStack contents = cap.getFluidInTank(i);
                        CompoundTag tank = new CompoundTag();
                        contents.writeToNBT(tank);
                        tank.putInt("capacity", cap.getTankCapacity(i));
                        tanks.add(tank);
                    }
                    nbt.put("tanks", tanks);
                    if (cap.getTanks() > maxLines) {
                        nbt.putInt("more_lines", cap.getTanks() - maxLines);
                    } else {
                        nbt.putInt("more_lines", 0);
                    }
                    display = true;
                }
            }
        }
        nbt.putBoolean("display", display);
        return nbt;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket(int maxLines) {
        return ClientboundBlockEntityDataPacket.create(this, BE -> this.getUpdateTag(maxLines));
    }
}