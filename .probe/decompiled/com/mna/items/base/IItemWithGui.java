package com.mna.items.base;

import com.mna.KeybindInit;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import org.apache.commons.lang3.mutable.MutableBoolean;

public interface IItemWithGui<T extends Item> extends IBagItem {

    MenuProvider getProvider(ItemStack var1);

    default boolean addItemWithGuiTooltip(ItemStack stack) {
        return true;
    }

    default boolean requiresModifierKey() {
        return true;
    }

    default boolean openGuiIfModifierPressed(ItemStack stack, Player player, Level world) {
        MutableBoolean guiOpened = new MutableBoolean(false);
        player.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> {
            if (!this.requiresModifierKey() || m.isModifierPressed()) {
                if (!world.isClientSide) {
                    NetworkHooks.openScreen((ServerPlayer) player, this.getProvider(stack));
                }
                guiOpened.setTrue();
            }
        });
        return guiOpened.booleanValue();
    }

    default void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (this.addItemWithGuiTooltip(stack)) {
            if (this.requiresModifierKey()) {
                String txt = I18n.get(((KeyMapping) KeybindInit.InventoryItemOpen.get()).getKey().getName());
                tooltip.add(Component.translatable("item.mna.item-with-gui.open-with", txt).withStyle(ChatFormatting.AQUA));
            } else {
                tooltip.add(Component.translatable("item.mna.item-with-gui.open-rclick").withStyle(ChatFormatting.AQUA));
            }
        }
    }
}