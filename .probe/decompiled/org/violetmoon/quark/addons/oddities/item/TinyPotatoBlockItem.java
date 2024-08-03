package org.violetmoon.quark.addons.oddities.item;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.addons.oddities.block.TinyPotatoBlock;
import org.violetmoon.quark.addons.oddities.util.TinyPotatoInfo;
import org.violetmoon.quark.api.IRuneColorProvider;
import org.violetmoon.quark.base.handler.ContributorRewardHandler;
import org.violetmoon.quark.content.tools.base.RuneColor;
import org.violetmoon.zeta.item.ZetaBlockItem;
import org.violetmoon.zeta.util.ItemNBTHelper;

public class TinyPotatoBlockItem extends ZetaBlockItem implements IRuneColorProvider {

    private static final int NOT_MY_NAME = 17;

    private static final List<String> TYPOS = List.of("vaskii", "vazki", "voskii", "vazkkii", "vazkki", "vazzki", "vaskki", "vozkii", "vazkil", "vaskil", "vazkill", "vaskill", "vaski");

    private static final String TICKS = "notMyNameTicks";

    public TinyPotatoBlockItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    @Override
    public boolean canEquipZeta(ItemStack stack, EquipmentSlot equipmentSlot, Entity entity) {
        if (equipmentSlot == EquipmentSlot.HEAD && entity instanceof Player player && ContributorRewardHandler.getTier(player) > 0) {
            return true;
        }
        return false;
    }

    @NotNull
    @Override
    public String getDescriptionId(@NotNull ItemStack stack) {
        return TinyPotatoBlock.isAngry(stack) ? super.m_5671_(stack) + ".angry" : super.m_5671_(stack);
    }

    private void updateData(ItemStack stack) {
        CompoundTag tileTag = stack.getTagElement("BlockEntityTag");
        if (tileTag != null) {
            if (tileTag.contains("angery", 99)) {
                boolean angry = tileTag.getBoolean("angery");
                if (angry) {
                    ItemNBTHelper.setBoolean(stack, "angery", true);
                } else if (TinyPotatoBlock.isAngry(stack)) {
                    stack.getOrCreateTag().remove("angery");
                }
                tileTag.remove("angery");
            }
            if (tileTag.contains("name", 8)) {
                stack.setHoverName(Component.Serializer.fromJson(tileTag.getString("name")));
                tileTag.remove("name");
            }
        }
        if (!ItemNBTHelper.getBoolean(stack, "angery", false)) {
            stack.getOrCreateTag().remove("angery");
        }
    }

    @Override
    public boolean onEntityItemUpdateZeta(ItemStack stack, ItemEntity entity) {
        this.updateData(stack);
        return super.onEntityItemUpdateZeta(stack, entity);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level world, @NotNull Entity holder, int itemSlot, boolean isSelected) {
        this.updateData(stack);
        if (!world.isClientSide && holder instanceof Player player && holder.tickCount % 30 == 0 && TYPOS.contains(ChatFormatting.stripFormatting(stack.getDisplayName().getString()))) {
            int ticks = ItemNBTHelper.getInt(stack, "notMyNameTicks", 0);
            if (ticks < 17) {
                player.m_213846_(Component.translatable("quark.misc.you_came_to_the_wrong_neighborhood." + ticks).withStyle(ChatFormatting.RED));
                ItemNBTHelper.setInt(stack, "notMyNameTicks", ticks + 1);
            }
        }
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return stack.hasCustomHoverName() && TinyPotatoInfo.fromComponent(stack.getHoverName()).enchanted() ? true : super.m_5812_(stack);
    }

    @Override
    public RuneColor getRuneColor(ItemStack stack) {
        return stack.hasCustomHoverName() ? TinyPotatoInfo.fromComponent(stack.getHoverName()).runeColor() : null;
    }
}