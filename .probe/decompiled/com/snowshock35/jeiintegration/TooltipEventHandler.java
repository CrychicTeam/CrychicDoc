package com.snowshock35.jeiintegration;

import com.mojang.blaze3d.platform.InputConstants;
import com.snowshock35.jeiintegration.config.Config;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Level;

public class TooltipEventHandler {

    private Config.Client config = Config.CLIENT;

    private static boolean isDebugMode() {
        return Minecraft.getInstance().options.advancedItemTooltips;
    }

    private static boolean isShiftKeyDown() {
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 340) || InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 344);
    }

    private void registerTooltip(ItemTooltipEvent e, Component tooltip, String configOption) {
        boolean isEnabled = false;
        if (Objects.equals(configOption, "enabled")) {
            isEnabled = true;
        } else if (Objects.equals(configOption, "onShift") && isShiftKeyDown()) {
            isEnabled = true;
        } else if (Objects.equals(configOption, "onDebug") && isDebugMode()) {
            isEnabled = true;
        } else if (Objects.equals(configOption, "onShiftAndDebug") && isShiftKeyDown() && isDebugMode()) {
            isEnabled = true;
        }
        if (isEnabled) {
            e.getToolTip().add(tooltip);
        }
    }

    private void registerTooltips(ItemTooltipEvent e, Collection<Component> tooltips, String configValue) {
        for (Component tooltip : tooltips) {
            this.registerTooltip(e, tooltip, configValue);
        }
    }

    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent e) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setGroupingSize(3);
        ItemStack itemStack = e.getItemStack();
        Item item = itemStack.getItem();
        if (!e.getItemStack().isEmpty()) {
            int burnTime = 0;
            try {
                burnTime = ForgeHooks.getBurnTime(itemStack, RecipeType.SMELTING);
            } catch (Exception var17) {
                JEIIntegration.logger.log(Level.WARN, "):\n\nSomething went wrong!");
            }
            if (burnTime > 0) {
                Component burnTooltip = Component.translatable("tooltip.jeiintegration.burnTime").append(Component.literal(" " + decimalFormat.format((long) burnTime) + " ")).append(Component.translatable("tooltip.jeiintegration.burnTime.suffix")).withStyle(ChatFormatting.DARK_GRAY);
                this.registerTooltip(e, burnTooltip, this.config.burnTimeTooltipMode.get());
            }
            int maxDamage = itemStack.getMaxDamage();
            int currentDamage = maxDamage - itemStack.getDamageValue();
            if (maxDamage > 0) {
                Component durabilityTooltip = Component.translatable("tooltip.jeiintegration.durability").append(Component.literal(" " + currentDamage + "/" + maxDamage)).withStyle(ChatFormatting.DARK_GRAY);
                this.registerTooltip(e, durabilityTooltip, this.config.durabilityTooltipMode.get());
            }
            int enchantability = item.getEnchantmentValue(itemStack);
            if (enchantability > 0) {
                Component enchantabilityTooltip = Component.translatable("tooltip.jeiintegration.enchantability").append(Component.literal(" " + enchantability)).withStyle(ChatFormatting.DARK_GRAY);
                this.registerTooltip(e, enchantabilityTooltip, this.config.enchantabilityTooltipMode.get());
            }
            FoodProperties foodProperties = item.getFoodProperties(itemStack, Minecraft.getInstance().player);
            if (item.isEdible() && foodProperties != null) {
                int healVal = foodProperties.getNutrition();
                float satVal = (float) healVal * foodProperties.getSaturationModifier() * 2.0F;
                Component foodTooltip = Component.translatable("tooltip.jeiintegration.hunger").append(Component.literal(" " + healVal + " ")).append(Component.translatable("tooltip.jeiintegration.saturation")).append(Component.literal(" " + decimalFormat.format((double) satVal))).withStyle(ChatFormatting.DARK_GRAY);
                this.registerTooltip(e, foodTooltip, this.config.foodTooltipMode.get());
            }
            CompoundTag nbtData = item.getShareTag(itemStack);
            if (nbtData != null) {
                Component nbtTooltip = Component.translatable("tooltip.jeiintegration.nbtTagData").append(Component.literal(" " + nbtData)).withStyle(ChatFormatting.DARK_GRAY);
                this.registerTooltip(e, nbtTooltip, this.config.nbtTooltipMode.get());
            }
            Component registryTooltip = Component.translatable("tooltip.jeiintegration.registryName").append(Component.literal(" " + ForgeRegistries.ITEMS.getKey(item))).withStyle(ChatFormatting.DARK_GRAY);
            this.registerTooltip(e, registryTooltip, this.config.registryNameTooltipMode.get());
            int stackSize = e.getItemStack().getMaxStackSize();
            if (stackSize > 0) {
                Component stackSizeTooltip = Component.translatable("tooltip.jeiintegration.maxStackSize").append(Component.literal(" " + itemStack.getMaxStackSize())).withStyle(ChatFormatting.DARK_GRAY);
                this.registerTooltip(e, stackSizeTooltip, this.config.maxStackSizeTooltipMode.get());
            }
            if (itemStack.getTags().toList().size() > 0) {
                Component tagsTooltip = Component.translatable("tooltip.jeiintegration.tags").withStyle(ChatFormatting.DARK_GRAY);
                Set<Component> tags = new HashSet();
                for (ResourceLocation tag : itemStack.getTags().map(TagKey::f_203868_).toList()) {
                    tags.add(Component.literal("    " + tag).withStyle(ChatFormatting.DARK_GRAY));
                }
                this.registerTooltip(e, tagsTooltip, this.config.tagsTooltipMode.get());
                this.registerTooltips(e, tags, this.config.tagsTooltipMode.get());
            }
            Component translationKeyTooltip = Component.translatable("tooltip.jeiintegration.translationKey").append(Component.literal(" " + itemStack.getDescriptionId())).withStyle(ChatFormatting.DARK_GRAY);
            this.registerTooltip(e, translationKeyTooltip, this.config.translationKeyTooltipMode.get());
        }
    }
}