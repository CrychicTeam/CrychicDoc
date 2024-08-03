package net.minecraft.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.BlastFurnaceMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;

public class BlastFurnaceBlockEntity extends AbstractFurnaceBlockEntity {

    public BlastFurnaceBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        super(BlockEntityType.BLAST_FURNACE, blockPos0, blockState1, RecipeType.BLASTING);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.blast_furnace");
    }

    @Override
    protected int getBurnDuration(ItemStack itemStack0) {
        return super.getBurnDuration(itemStack0) / 2;
    }

    @Override
    protected AbstractContainerMenu createMenu(int int0, Inventory inventory1) {
        return new BlastFurnaceMenu(int0, inventory1, this, this.f_58311_);
    }
}