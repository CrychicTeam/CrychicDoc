package com.mna.items.artifice;

import com.mna.api.items.TieredItem;
import com.mna.gui.containers.providers.NamedFilterItem;
import com.mna.inventory.ItemInventoryBase;
import com.mna.items.base.IItemWithGui;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class FilterItem extends TieredItem implements IItemWithGui<FilterItem> {

    public static final int INVENTORY_SIZE = 24;

    public FilterItem() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (world.isClientSide) {
            return InteractionResultHolder.pass(player.m_21120_(hand));
        } else {
            return IItemWithGui.super.openGuiIfModifierPressed(this.m_7968_(), player, world) ? InteractionResultHolder.consume(player.m_21120_(hand)) : InteractionResultHolder.pass(player.m_21120_(hand));
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flags) {
        IItemWithGui.super.appendHoverText(stack, world, tooltip, flags);
        this.addContentsDescription(stack, world, tooltip);
    }

    public void addContentsDescription(ItemStack stack, Level world, List<Component> tooltip) {
        ItemInventoryBase inv = new ItemInventoryBase(stack);
        List<ItemStack> nonAir = inv.getNonAirItems();
        if (nonAir.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < Math.min(nonAir.size(), 5); i++) {
                if (i != 0) {
                    sb.append(", ");
                }
                sb.append(Component.translatable(((ItemStack) nonAir.get(i)).getDescriptionId()).getString());
            }
            if (nonAir.size() > 4) {
                sb.append("...");
            }
            tooltip.add(Component.literal(sb.toString()).withStyle(ChatFormatting.GREEN));
        } else {
            tooltip.add(Component.translatable("item.mna.filter_item.empty").withStyle(ChatFormatting.RED));
        }
        tooltip.add(Component.translatable("item.mna.filter_item.match_durability").append(Component.translatable(this.getMatchDurability(stack) ? "item.mna.filter_item.yes" : "item.mna.filter_item.no").withStyle(ChatFormatting.GOLD)));
        tooltip.add(Component.translatable("item.mna.filter_item.match_tag").append(Component.translatable(this.getMatchTag(stack) ? "item.mna.filter_item.yes" : "item.mna.filter_item.no").withStyle(ChatFormatting.GOLD)));
    }

    @Override
    public MenuProvider getProvider(ItemStack stack) {
        return new NamedFilterItem();
    }

    public void setItems(ItemStack filter, NonNullList<ItemStack> list, boolean matchDurability, boolean matchTag) {
        ItemInventoryBase inv = new ItemInventoryBase(filter, 24);
        for (int i = 0; i < inv.size(); i++) {
            inv.setStackInSlot(i, ItemStack.EMPTY);
        }
        for (int i = 0; i < Math.min(inv.size(), list.size()); i++) {
            inv.setStackInSlot(i, list.get(i));
        }
        inv.writeItemStack();
        this.setMatchDurability(filter, matchDurability);
        this.setMatchTag(filter, matchTag);
    }

    public void setMatchDurability(ItemStack stack, boolean durability) {
        stack.getOrCreateTag().putBoolean("match_durability", durability);
    }

    public boolean getMatchDurability(ItemStack stack) {
        return !stack.hasTag() ? false : stack.getTag().getBoolean("match_durability");
    }

    public void setMatchTag(ItemStack stack, boolean durability) {
        stack.getOrCreateTag().putBoolean("match_tag", durability);
    }

    public boolean getMatchTag(ItemStack stack) {
        return !stack.hasTag() ? false : stack.getTag().getBoolean("match_tag");
    }
}