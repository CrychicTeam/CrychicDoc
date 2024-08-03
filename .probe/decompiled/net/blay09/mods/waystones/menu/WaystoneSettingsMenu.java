package net.blay09.mods.waystones.menu;

import net.blay09.mods.waystones.api.IWaystone;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public class WaystoneSettingsMenu extends AbstractContainerMenu {

    private final IWaystone waystone;

    public WaystoneSettingsMenu(MenuType<WaystoneSettingsMenu> type, IWaystone waystone, int windowId) {
        super(type, windowId);
        this.waystone = waystone;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        BlockPos pos = this.waystone.getPos();
        return player.m_20275_((double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5) <= 64.0;
    }

    public IWaystone getWaystone() {
        return this.waystone;
    }
}