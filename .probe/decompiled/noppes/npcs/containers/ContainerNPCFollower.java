package noppes.npcs.containers;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.CustomContainer;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleFollower;

public class ContainerNPCFollower extends ContainerNpcInterface {

    public InventoryNPC currencyMatrix;

    public RoleFollower role;

    public ContainerNPCFollower(int containerId, Inventory playerInventory, int entityId) {
        super(CustomContainer.container_follower, containerId, playerInventory);
        EntityNPCInterface npc = (EntityNPCInterface) this.player.m_9236_().getEntity(entityId);
        this.role = (RoleFollower) npc.role;
        this.currencyMatrix = new InventoryNPC("currency", 1, this);
        this.m_38897_(new SlotNpcMercenaryCurrency(this.role, this.currencyMatrix, 0, 26, 9));
        for (int j1 = 0; j1 < 9; j1++) {
            this.m_38897_(new Slot(this.player.getInventory(), j1, 8 + j1 * 18, 142));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player par1Player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public void removed(Player entityplayer) {
        super.m_6877_(entityplayer);
        if (!entityplayer.m_9236_().isClientSide) {
            ItemStack itemstack = this.currencyMatrix.removeItemNoUpdate(0);
            if (!NoppesUtilServer.IsItemStackNull(itemstack) && !entityplayer.m_9236_().isClientSide) {
                entityplayer.m_5552_(itemstack, 0.0F);
            }
        }
    }
}