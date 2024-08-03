package net.minecraft.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SmokerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;

public class SmokerBlockEntity extends AbstractFurnaceBlockEntity {

    public SmokerBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        super(BlockEntityType.SMOKER, blockPos0, blockState1, RecipeType.SMOKING);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.smoker");
    }

    @Override
    protected int getBurnDuration(ItemStack itemStack0) {
        return super.getBurnDuration(itemStack0) / 2;
    }

    @Override
    protected AbstractContainerMenu createMenu(int int0, Inventory inventory1) {
        return new SmokerMenu(int0, inventory1, this, this.f_58311_);
    }
}