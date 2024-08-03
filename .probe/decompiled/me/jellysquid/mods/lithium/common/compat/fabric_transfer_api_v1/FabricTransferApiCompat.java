package me.jellysquid.mods.lithium.common.compat.fabric_transfer_api_v1;

import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.loading.LoadingModList;

public class FabricTransferApiCompat {

    public static final boolean FABRIC_TRANSFER_API_V_1_PRESENT = LoadingModList.get().getModFileById("fabric_transfer_api_v1") != null;

    public static boolean canHopperInteractWithApiInventory(HopperBlockEntity hopperBlockEntity, BlockState hopperState, boolean extracting) {
        Direction direction = extracting ? Direction.UP : (Direction) hopperState.m_61143_(HopperBlock.FACING);
        BlockPos targetPos = hopperBlockEntity.m_58899_().relative(direction);
        Object target = ItemStorage.SIDED.find(hopperBlockEntity.m_58904_(), targetPos, direction.getOpposite());
        return target != null;
    }
}