package com.rekindled.embers.item;

import com.rekindled.embers.ConfigManager;
import com.rekindled.embers.block.FluidDialBlock;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import org.jetbrains.annotations.Nullable;

public class FluidVesselBlockItem extends BlockItem {

    public FluidVesselBlockItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    public int getMaxStackSize(ItemStack stack) {
        return stack.hasTag() && !FluidStack.loadFluidStackFromNBT(stack.getTag().getCompound("Fluid")).isEmpty() ? 1 : super.getMaxStackSize(stack);
    }

    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new FluidHandlerItemStack(stack, ConfigManager.FLUID_VESSEL_CAPACITY.get());
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pPos, Level pLevel, @Nullable Player pPlayer, ItemStack pStack, BlockState pState) {
        CompoundTag nbt = pStack.getOrCreateTag();
        if (nbt.contains("Fluid")) {
            nbt.put("BlockEntityTag", nbt.get("Fluid"));
        }
        return BlockItem.updateCustomBlockEntityTag(pLevel, pPlayer, pPos, pStack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag isAdvanced) {
        IFluidHandler cap = (IFluidHandler) stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).orElse(null);
        if (cap != null) {
            tooltip.add(FluidDialBlock.formatFluidStack(cap.getFluidInTank(0), cap.getTankCapacity(0)).withStyle(ChatFormatting.GRAY));
        }
    }
}