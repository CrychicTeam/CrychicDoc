package noppes.npcs.shared.common;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class ContainerEmpty extends AbstractContainerMenu {

    public ContainerEmpty() {
        super(null, 0);
    }

    @Override
    public ItemStack quickMoveStack(Player player0, int int1) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player var1) {
        return false;
    }
}