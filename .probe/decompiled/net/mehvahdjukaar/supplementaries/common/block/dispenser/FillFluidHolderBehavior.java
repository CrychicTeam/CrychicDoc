package net.mehvahdjukaar.supplementaries.common.block.dispenser;

import net.mehvahdjukaar.moonlight.api.block.ISoftFluidTankProvider;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidTank;
import net.mehvahdjukaar.moonlight.api.util.DispenserHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.BlockEntity;

class FillFluidHolderBehavior extends DispenserHelper.AdditionalDispenserBehavior {

    public FillFluidHolderBehavior(Item item) {
        super(item);
    }

    @Override
    protected InteractionResultHolder<ItemStack> customBehavior(BlockSource source, ItemStack stack) {
        ServerLevel world = source.getLevel();
        BlockPos blockpos = source.getPos().relative((Direction) source.getBlockState().m_61143_(DispenserBlock.FACING));
        BlockEntity te = world.m_7702_(blockpos);
        if (te instanceof ISoftFluidTankProvider tile) {
            if (tile.canInteractWithSoftFluidTank()) {
                SoftFluidTank tank = tile.getSoftFluidTank();
                if (!tank.isFull()) {
                    ItemStack returnStack = tank.interactWithItem(stack, world, blockpos, false);
                    if (returnStack != null) {
                        te.setChanged();
                        return InteractionResultHolder.success(returnStack);
                    }
                }
            }
            return InteractionResultHolder.fail(stack);
        } else {
            return InteractionResultHolder.pass(stack);
        }
    }
}