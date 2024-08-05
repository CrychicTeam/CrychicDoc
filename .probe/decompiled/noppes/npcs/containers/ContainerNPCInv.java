package noppes.npcs.containers;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.CustomContainer;
import noppes.npcs.entity.EntityNPCInterface;

public class ContainerNPCInv extends AbstractContainerMenu {

    public ContainerNPCInv(int containerId, Inventory playerInventory, int entityId) {
        super(CustomContainer.container_inv, containerId);
        EntityNPCInterface npc = (EntityNPCInterface) playerInventory.player.m_9236_().getEntity(entityId);
        this.m_38897_(new SlotNPCArmor(npc.inventory, 0, 9, 22, EquipmentSlot.HEAD));
        this.m_38897_(new SlotNPCArmor(npc.inventory, 1, 9, 40, EquipmentSlot.CHEST));
        this.m_38897_(new SlotNPCArmor(npc.inventory, 2, 9, 58, EquipmentSlot.LEGS));
        this.m_38897_(new SlotNPCArmor(npc.inventory, 3, 9, 76, EquipmentSlot.FEET));
        this.m_38897_(new Slot(npc.inventory, 4, 81, 22));
        this.m_38897_(new Slot(npc.inventory, 5, 81, 40));
        this.m_38897_(new Slot(npc.inventory, 6, 81, 58));
        for (int l = 0; l < 9; l++) {
            this.m_38897_(new Slot(npc.inventory, l + 7, 191, 16 + l * 21));
        }
        for (int i1 = 0; i1 < 3; i1++) {
            for (int l1 = 0; l1 < 9; l1++) {
                this.m_38897_(new Slot(playerInventory, l1 + i1 * 9 + 9, l1 * 18 + 8, 113 + i1 * 18));
            }
        }
        for (int j1 = 0; j1 < 9; j1++) {
            this.m_38897_(new Slot(playerInventory, j1, j1 * 18 + 8, 171));
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