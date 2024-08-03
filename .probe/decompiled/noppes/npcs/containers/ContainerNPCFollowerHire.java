package noppes.npcs.containers;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.CustomContainer;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleFollower;

public class ContainerNPCFollowerHire extends ContainerNpcInterface {

    public SimpleContainer currencyMatrix;

    public RoleFollower role;

    public ContainerNPCFollowerHire(int containerId, Inventory playerInventory, int entityId) {
        super(CustomContainer.container_followerhire, containerId, playerInventory);
        EntityNPCInterface npc = (EntityNPCInterface) this.player.m_9236_().getEntity(entityId);
        this.role = (RoleFollower) npc.role;
        this.currencyMatrix = new SimpleContainer(1);
        this.m_38897_(new SlotNpcMercenaryCurrency(this.role, this.currencyMatrix, 0, 44, 35));
        for (int i1 = 0; i1 < 3; i1++) {
            for (int l1 = 0; l1 < 9; l1++) {
                this.m_38897_(new Slot(this.player.getInventory(), l1 + i1 * 9 + 9, 8 + l1 * 18, 84 + i1 * 18));
            }
        }
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