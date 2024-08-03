package noppes.npcs.containers;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.CustomContainer;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.JobItemGiver;

public class ContainerNpcItemGiver extends AbstractContainerMenu {

    private JobItemGiver role;

    public ContainerNpcItemGiver(int containerId, Inventory playerInventory, int entityId) {
        super(CustomContainer.container_itemgiver, containerId);
        EntityNPCInterface npc = (EntityNPCInterface) playerInventory.player.m_9236_().getEntity(entityId);
        this.role = (JobItemGiver) npc.job;
        for (int j1 = 0; j1 < 9; j1++) {
            this.m_38897_(new Slot(this.role.inventory, j1, 6 + j1 * 18, 90));
        }
        for (int i1 = 0; i1 < 3; i1++) {
            for (int l1 = 0; l1 < 9; l1++) {
                this.m_38897_(new Slot(playerInventory, l1 + i1 * 9 + 9, 6 + l1 * 18, 116 + i1 * 18));
            }
        }
        for (int j1 = 0; j1 < 9; j1++) {
            this.m_38897_(new Slot(playerInventory, j1, 6 + j1 * 18, 174));
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