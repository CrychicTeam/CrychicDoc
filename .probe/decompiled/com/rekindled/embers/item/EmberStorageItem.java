package com.rekindled.embers.item;

import com.rekindled.embers.EmbersClientEvents;
import com.rekindled.embers.api.capabilities.EmbersCapabilities;
import com.rekindled.embers.api.power.IEmberCapability;
import com.rekindled.embers.power.DefaultEmberItemCapability;
import com.rekindled.embers.util.DecimalFormats;
import com.rekindled.embers.util.Misc;
import java.text.DecimalFormat;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;

public abstract class EmberStorageItem extends Item {

    public EmberStorageItem(Item.Properties properties) {
        super(properties);
    }

    public static ItemStack withFill(Item item, double ember) {
        ItemStack stack = new ItemStack(item);
        IEmberCapability cap = (IEmberCapability) stack.getCapability(EmbersCapabilities.EMBER_CAPABILITY).orElse(null);
        cap.setEmber(ember);
        return stack;
    }

    public abstract double getCapacity();

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
        return new DefaultEmberItemCapability(stack, this.getCapacity());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag isAdvanced) {
        IEmberCapability cap = (IEmberCapability) stack.getCapability(EmbersCapabilities.EMBER_CAPABILITY).orElse(null);
        if (cap != null) {
            DecimalFormat emberFormat = DecimalFormats.getDecimalFormat("embers.decimal_format.ember");
            tooltip.add(Component.translatable("embers.tooltip.item.ember", emberFormat.format(cap.getEmber()), emberFormat.format(cap.getEmberCapacity())).withStyle(ChatFormatting.GRAY));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class ColorHandler implements ItemColor {

        @Override
        public int getColor(ItemStack stack, int tintIndex) {
            if (tintIndex == 0) {
                IEmberCapability capability = (IEmberCapability) stack.getCapability(EmbersCapabilities.EMBER_CAPABILITY, null).orElse(null);
                if (capability != null) {
                    float coeff = (float) (capability.getEmber() / capability.getEmberCapacity());
                    float timerSine = ((float) Math.sin(8.0 * Math.toRadians((double) (EmbersClientEvents.ticks % 360))) + 1.0F) / 2.0F;
                    int r = 255;
                    int g = (int) (255.0F * (1.0F - coeff) + (64.0F * timerSine + 64.0F) * coeff);
                    int b = (int) (255.0F * (1.0F - coeff) + 16.0F * coeff);
                    return Misc.intColor(r, g, b);
                }
            }
            return 16777215;
        }
    }
}