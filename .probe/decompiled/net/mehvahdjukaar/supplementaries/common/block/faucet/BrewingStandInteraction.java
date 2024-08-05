package net.mehvahdjukaar.supplementaries.common.block.faucet;

import com.mojang.datafixers.util.Pair;
import net.mehvahdjukaar.moonlight.api.fluids.FluidContainerList;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;

class BrewingStandInteraction implements FaucetSource.Tile, FaucetTarget.Tile {

    public FluidOffer getProvidedFluid(Level level, BlockPos pos, Direction dir, BlockEntity tile) {
        if (tile instanceof BrewingStandBlockEntity brewingStand) {
            for (int i = 0; i < 3; i++) {
                ItemStack stack = brewingStand.getItem(i);
                Pair<SoftFluidStack, FluidContainerList.Category> opt = SoftFluidStack.fromItem(stack);
                if (opt != null) {
                    return FluidOffer.of((SoftFluidStack) opt.getFirst());
                }
            }
        }
        return null;
    }

    public void drain(Level level, BlockPos pos, Direction dir, BlockEntity tile, int amount) {
        if (tile instanceof BrewingStandBlockEntity brewingStand) {
            for (int i = 0; i < 3; i++) {
                ItemStack stack = brewingStand.getItem(i);
                Pair<SoftFluidStack, FluidContainerList.Category> opt = SoftFluidStack.fromItem(stack);
                if (opt != null) {
                    brewingStand.setItem(i, stack.getItem().getCraftingRemainingItem().getDefaultInstance());
                    tile.setChanged();
                    return;
                }
            }
        }
    }

    public Integer fill(Level level, BlockPos pos, BlockEntity tile, SoftFluidStack fluid, int minAmount) {
        if (tile instanceof BrewingStandBlockEntity brewingStand) {
            for (int i = 0; i < 3; i++) {
                ItemStack stack = brewingStand.getItem(i);
                Pair<ItemStack, FluidContainerList.Category> filled = fluid.toItem(stack, true);
                if (filled != null) {
                    ItemStack filledItem = (ItemStack) filled.getFirst();
                    brewingStand.setItem(i, ItemStack.EMPTY);
                    if (brewingStand.canPlaceItem(i, filledItem)) {
                        brewingStand.setItem(i, ((ItemStack) filled.getFirst()).copy());
                        tile.setChanged();
                        return ((FluidContainerList.Category) filled.getSecond()).getAmount();
                    }
                    brewingStand.setItem(i, stack);
                }
            }
        }
        return null;
    }
}