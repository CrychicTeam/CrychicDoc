package com.simibubi.create.content.logistics.depot;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;

public class DepotBlockEntity extends SmartBlockEntity {

    DepotBehaviour depotBehaviour;

    public DepotBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(this.depotBehaviour = new DepotBehaviour(this));
        this.depotBehaviour.addSubBehaviours(behaviours);
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == ForgeCapabilities.ITEM_HANDLER ? this.depotBehaviour.getItemCapability(cap, side) : super.getCapability(cap, side);
    }

    public ItemStack getHeldItem() {
        return this.depotBehaviour.getHeldItemStack();
    }
}