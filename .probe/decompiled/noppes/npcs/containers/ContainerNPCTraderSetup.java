package noppes.npcs.containers;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.CustomContainer;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleTrader;

public class ContainerNPCTraderSetup extends AbstractContainerMenu {

    public RoleTrader role;

    public ContainerNPCTraderSetup(int containerId, Inventory playerInventory, int entityId) {
        super(CustomContainer.container_tradersetup, containerId);
        EntityNPCInterface npc = (EntityNPCInterface) playerInventory.player.m_9236_().getEntity(entityId);
        this.role = (RoleTrader) npc.role;
        for (int i = 0; i < 18; i++) {
            int x = 7;
            x += i % 3 * 94;
            int y = 15;
            y += i / 3 * 22;
            this.m_38897_(new Slot(this.role.inventoryCurrency, i + 18, x, y));
            this.m_38897_(new Slot(this.role.inventoryCurrency, i, x + 18, y));
            this.m_38897_(new Slot(this.role.inventorySold, i, x + 43, y));
        }
        for (int i1 = 0; i1 < 3; i1++) {
            for (int l1 = 0; l1 < 9; l1++) {
                this.m_38897_(new Slot(playerInventory, l1 + i1 * 9 + 9, 48 + l1 * 18, 147 + i1 * 18));
            }
        }
        for (int j1 = 0; j1 < 9; j1++) {
            this.m_38897_(new Slot(playerInventory, j1, 48 + j1 * 18, 205));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player par1Player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player entityplayer) {
        return true;
    }
}