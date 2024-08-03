package com.rekindled.embers.item;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.capabilities.EmbersCapabilities;
import com.rekindled.embers.api.power.IEmberCapability;
import com.rekindled.embers.power.DefaultEmberItemCapability;
import com.rekindled.embers.util.DecimalFormats;
import java.text.DecimalFormat;
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
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;

public class CopperCellBlockItem extends BlockItem {

    public CopperCellBlockItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return stack.getCapability(EmbersCapabilities.EMBER_CAPABILITY).isPresent();
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        IEmberCapability cap = (IEmberCapability) stack.getCapability(EmbersCapabilities.EMBER_CAPABILITY).orElse(null);
        return cap != null ? Math.round(13.0F - (float) (cap.getEmberCapacity() - cap.getEmber()) * 13.0F / (float) cap.getEmberCapacity()) : 0;
    }

    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged || !ItemStack.isSameItem(oldStack, newStack);
    }

    @Override
    public int getBarColor(ItemStack pStack) {
        return 16737792;
    }

    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new DefaultEmberItemCapability(stack, 24000.0);
    }

    public static ItemStack getCharged() {
        ItemStack chargedCell = new ItemStack(RegistryManager.COPPER_CELL_ITEM.get());
        IEmberCapability cap = (IEmberCapability) chargedCell.getCapability(EmbersCapabilities.EMBER_CAPABILITY).orElse(null);
        cap.setEmber(cap.getEmberCapacity());
        return chargedCell;
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pPos, Level pLevel, @Nullable Player pPlayer, ItemStack pStack, BlockState pState) {
        IEmberCapability cap = (IEmberCapability) pStack.getCapability(EmbersCapabilities.EMBER_CAPABILITY).orElse(null);
        if (cap != null) {
            cap.writeToNBT(pStack.getOrCreateTagElement("BlockEntityTag"));
        }
        return BlockItem.updateCustomBlockEntityTag(pLevel, pPlayer, pPos, pStack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag isAdvanced) {
        IEmberCapability cap = (IEmberCapability) stack.getCapability(EmbersCapabilities.EMBER_CAPABILITY).orElse(null);
        if (cap != null) {
            DecimalFormat emberFormat = DecimalFormats.getDecimalFormat("embers.decimal_format.ember");
            tooltip.add(Component.translatable("embers.tooltip.item.ember", emberFormat.format(cap.getEmber()), emberFormat.format(cap.getEmberCapacity())).withStyle(ChatFormatting.GRAY));
        }
    }
}