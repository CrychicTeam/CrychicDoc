package com.simibubi.create.content.logistics.filter;

import com.simibubi.create.AllItems;
import com.simibubi.create.AllKeys;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;

public class FilterItem extends Item implements MenuProvider {

    private FilterItem.FilterType type;

    public static FilterItem regular(Item.Properties properties) {
        return new FilterItem(FilterItem.FilterType.REGULAR, properties);
    }

    public static FilterItem attribute(Item.Properties properties) {
        return new FilterItem(FilterItem.FilterType.ATTRIBUTE, properties);
    }

    private FilterItem(FilterItem.FilterType type, Item.Properties properties) {
        super(properties);
        this.type = type;
    }

    @Nonnull
    @Override
    public InteractionResult useOn(UseOnContext context) {
        return context.getPlayer() == null ? InteractionResult.PASS : this.use(context.getLevel(), context.getPlayer(), context.getHand()).getResult();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (!AllKeys.shiftDown()) {
            List<Component> makeSummary = this.makeSummary(stack);
            if (makeSummary.isEmpty()) {
                return;
            }
            tooltip.add(Components.literal(" "));
            tooltip.addAll(makeSummary);
        }
    }

    private List<Component> makeSummary(ItemStack filter) {
        List<Component> list = new ArrayList();
        if (!filter.hasTag()) {
            return list;
        } else {
            if (this.type == FilterItem.FilterType.REGULAR) {
                ItemStackHandler filterItems = getFilterItems(filter);
                boolean blacklist = filter.getOrCreateTag().getBoolean("Blacklist");
                list.add((blacklist ? Lang.translateDirect("gui.filter.deny_list") : Lang.translateDirect("gui.filter.allow_list")).withStyle(ChatFormatting.GOLD));
                int count = 0;
                for (int i = 0; i < filterItems.getSlots(); i++) {
                    if (count > 3) {
                        list.add(Components.literal("- ...").withStyle(ChatFormatting.DARK_GRAY));
                        break;
                    }
                    ItemStack filterStack = filterItems.getStackInSlot(i);
                    if (!filterStack.isEmpty()) {
                        list.add(Components.literal("- ").append(filterStack.getHoverName()).withStyle(ChatFormatting.GRAY));
                        count++;
                    }
                }
                if (count == 0) {
                    return Collections.emptyList();
                }
            }
            if (this.type == FilterItem.FilterType.ATTRIBUTE) {
                AttributeFilterMenu.WhitelistMode whitelistMode = AttributeFilterMenu.WhitelistMode.values()[filter.getOrCreateTag().getInt("WhitelistMode")];
                list.add((whitelistMode == AttributeFilterMenu.WhitelistMode.WHITELIST_CONJ ? Lang.translateDirect("gui.attribute_filter.allow_list_conjunctive") : (whitelistMode == AttributeFilterMenu.WhitelistMode.WHITELIST_DISJ ? Lang.translateDirect("gui.attribute_filter.allow_list_disjunctive") : Lang.translateDirect("gui.attribute_filter.deny_list"))).withStyle(ChatFormatting.GOLD));
                int count = 0;
                for (Tag inbt : filter.getOrCreateTag().getList("MatchedAttributes", 10)) {
                    CompoundTag compound = (CompoundTag) inbt;
                    ItemAttribute attribute = ItemAttribute.fromNBT(compound);
                    if (attribute != null) {
                        boolean inverted = compound.getBoolean("Inverted");
                        if (count > 3) {
                            list.add(Components.literal("- ...").withStyle(ChatFormatting.DARK_GRAY));
                            break;
                        }
                        list.add(Components.literal("- ").append(attribute.format(inverted)));
                        count++;
                    }
                }
                if (count == 0) {
                    return Collections.emptyList();
                }
            }
            return list;
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack heldItem = player.m_21120_(hand);
        if (!player.m_6144_() && hand == InteractionHand.MAIN_HAND) {
            if (!world.isClientSide && player instanceof ServerPlayer) {
                NetworkHooks.openScreen((ServerPlayer) player, this, buf -> buf.writeItem(heldItem));
            }
            return InteractionResultHolder.success(heldItem);
        } else {
            return InteractionResultHolder.pass(heldItem);
        }
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        ItemStack heldItem = player.m_21205_();
        if (this.type == FilterItem.FilterType.REGULAR) {
            return FilterMenu.create(id, inv, heldItem);
        } else {
            return this.type == FilterItem.FilterType.ATTRIBUTE ? AttributeFilterMenu.create(id, inv, heldItem) : null;
        }
    }

    @Override
    public Component getDisplayName() {
        return this.m_41466_();
    }

    public static ItemStackHandler getFilterItems(ItemStack stack) {
        ItemStackHandler newInv = new ItemStackHandler(18);
        if (AllItems.FILTER.get() != stack.getItem()) {
            throw new IllegalArgumentException("Cannot get filter items from non-filter: " + stack);
        } else if (!stack.hasTag()) {
            return newInv;
        } else {
            CompoundTag invNBT = stack.getOrCreateTagElement("Items");
            if (!invNBT.isEmpty()) {
                newInv.deserializeNBT(invNBT);
            }
            return newInv;
        }
    }

    public static boolean testDirect(ItemStack filter, ItemStack stack, boolean matchNBT) {
        return matchNBT ? ItemHandlerHelper.canItemStacksStack(filter, stack) : ItemHelper.sameItem(filter, stack);
    }

    private static enum FilterType {

        REGULAR, ATTRIBUTE
    }
}