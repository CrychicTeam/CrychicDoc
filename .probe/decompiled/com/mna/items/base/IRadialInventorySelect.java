package com.mna.items.base;

import com.mna.KeybindInit;
import com.mna.inventory.ItemInventoryBase;
import com.mna.network.ClientMessageDispatcher;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandlerModifiable;

public interface IRadialInventorySelect extends IRadialMenuItem {

    String NBT_INDEX = "index";

    @Override
    default void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        String txt = I18n.get(((KeyMapping) KeybindInit.RadialMenuOpen.get()).getKey().getDisplayName().getString());
        tooltip.add(Component.translatable("item.mna.item-with-gui.radial-open", txt).withStyle(ChatFormatting.AQUA));
    }

    int capacity();

    default int capacity(@Nullable Player player) {
        return this.capacity();
    }

    default void setIndex(@Nullable Player player, InteractionHand hand, ItemStack stack, int index) {
        this.setIndex(stack, index);
    }

    void setIndex(ItemStack var1, int var2);

    int getIndex(ItemStack var1);

    default IItemHandlerModifiable getInventory(ItemStack stackEquipped) {
        return this.getInventory(stackEquipped, null);
    }

    default IItemHandlerModifiable getInventory(ItemStack stackEquipped, @Nullable Player player) {
        return new ItemInventoryBase(stackEquipped, this.capacity());
    }

    default void setSlot(Player player, InteractionHand hand, int index, boolean offhand, boolean packet) {
        ItemStack stack = player.m_21120_(hand);
        if (stack.getItem() instanceof IRadialInventorySelect) {
            this.setIndex(player, hand, stack, index);
            if (packet) {
                ClientMessageDispatcher.sendRadialInventorySlotChange(index, offhand);
            }
        }
    }
}