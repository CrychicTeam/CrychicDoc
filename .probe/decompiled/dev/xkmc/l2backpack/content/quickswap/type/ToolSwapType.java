package dev.xkmc.l2backpack.content.quickswap.type;

import dev.xkmc.l2backpack.content.quickswap.entry.ISingleSwapHandler;
import dev.xkmc.l2backpack.init.data.BackpackConfig;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ToolSwapType extends QuickSwapType implements ISingleSwapAction {

    public ToolSwapType(String name, int index) {
        super(name, index);
    }

    @Override
    public boolean activePopup() {
        return BackpackConfig.CLIENT.popupToolOnSwitch.get();
    }

    @Override
    public ItemStack getSignatureItem(Player player) {
        return player.m_21205_();
    }

    @Override
    public void swapSingle(Player player, ISingleSwapHandler handler) {
        ItemStack stack = handler.getStack();
        handler.replace(player.m_21205_());
        player.m_21008_(InteractionHand.MAIN_HAND, stack);
    }
}