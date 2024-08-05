package com.mna.items.base;

import com.mna.items.filters.ItemFilterGroup;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public abstract class ItemBagBase extends Item implements IItemWithGui<ItemBagBase> {

    public int size = 18;

    public abstract ItemFilterGroup filterGroup();

    @Override
    public abstract MenuProvider getProvider(ItemStack var1);

    public ItemBagBase() {
        super(new Item.Properties().stacksTo(1));
    }

    public ItemBagBase(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        IItemWithGui.super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, @Nonnull InteractionHand hand) {
        if (!world.isClientSide) {
            this.openGuiIfModifierPressed(player.m_21120_(hand), player, world);
        }
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.m_21120_(hand));
    }

    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        if (nbt != null && nbt.contains("Parent")) {
            nbt.put("Items", nbt.get("Parent"));
            stack.setTag(nbt);
        }
        return null;
    }
}