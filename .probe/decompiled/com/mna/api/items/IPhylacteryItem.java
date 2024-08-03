package com.mna.api.items;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;

public interface IPhylacteryItem {

    boolean isFull(ItemStack var1);

    float getFillPct(ItemStack var1);

    float getContainedSouls(ItemStack var1);

    boolean setContainedSouls(ItemStack var1, float var2);

    @Nullable
    EntityType<? extends Mob> getContainedEntity(ItemStack var1);

    boolean fill(ItemStack var1, EntityType<? extends Mob> var2, float var3, Level var4);

    boolean fill(IItemHandler var1, EntityType<? extends Mob> var2, float var3, Level var4, boolean var5);

    boolean fill(Container var1, EntityType<? extends Mob> var2, float var3, Level var4, boolean var5);

    int getMaximumFill();

    default void appendTooltip(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (stack.getItem() instanceof IPhylacteryItem) {
            EntityType<?> type = this.getContainedEntity(stack);
            if (type != null) {
                String entityType = I18n.get(type.getDescriptionId());
                float amount = this.getContainedSouls(stack);
                if (!this.isFull(stack)) {
                    tooltip.add(Component.translatable("item.mna.crystal_phylactery.tooltip", entityType, (int) amount, this.getMaximumFill()).withStyle(ChatFormatting.GOLD));
                } else {
                    tooltip.add(Component.translatable("item.mna.crystal_phylactery.tooltip", entityType, (int) amount, this.getMaximumFill()).withStyle(ChatFormatting.DARK_PURPLE));
                }
            } else {
                tooltip.add(Component.translatable("item.mna.crystal_phylactery.empty").withStyle(ChatFormatting.AQUA));
            }
        }
    }
}