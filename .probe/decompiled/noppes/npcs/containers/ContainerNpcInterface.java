package noppes.npcs.containers;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import noppes.npcs.api.IContainer;
import noppes.npcs.api.wrapper.ContainerWrapper;

public class ContainerNpcInterface extends AbstractContainerMenu {

    private int posX;

    private int posZ;

    public Player player;

    public IContainer scriptContainer;

    public ContainerNpcInterface(MenuType type, int containerId, Inventory playerInventory) {
        super(type, containerId);
        this.player = playerInventory.player;
        this.posX = Mth.floor(this.player.m_20185_());
        this.posZ = Mth.floor(this.player.m_20189_());
        this.player.m_20256_(Vec3.ZERO);
    }

    @Override
    public ItemStack quickMoveStack(Player player0, int int1) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return !player.m_213877_() && this.posX == Mth.floor(player.m_20185_()) && this.posZ == Mth.floor(player.m_20189_());
    }

    public static IContainer getOrCreateIContainer(ContainerNpcInterface container) {
        return container.scriptContainer != null ? container.scriptContainer : (container.scriptContainer = new ContainerWrapper(container));
    }
}