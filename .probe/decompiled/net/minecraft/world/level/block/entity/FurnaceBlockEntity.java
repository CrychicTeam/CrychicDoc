package net.minecraft.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.FurnaceMenu;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;

public class FurnaceBlockEntity extends AbstractFurnaceBlockEntity {

    public FurnaceBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        super(BlockEntityType.FURNACE, blockPos0, blockState1, RecipeType.SMELTING);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.furnace");
    }

    @Override
    protected AbstractContainerMenu createMenu(int int0, Inventory inventory1) {
        return new FurnaceMenu(int0, inventory1, this, this.f_58311_);
    }
}