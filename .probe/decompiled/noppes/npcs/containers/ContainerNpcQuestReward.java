package noppes.npcs.containers;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.CustomContainer;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.controllers.data.Quest;

public class ContainerNpcQuestReward extends AbstractContainerMenu {

    public ContainerNpcQuestReward(int containerId, Inventory playerInventory) {
        super(CustomContainer.container_questreward, containerId);
        Quest quest = NoppesUtilServer.getEditingQuest(playerInventory.player);
        for (int l = 0; l < 3; l++) {
            for (int k1 = 0; k1 < 3; k1++) {
                this.m_38897_(new Slot(quest.rewardItems, k1 + l * 3, 105 + k1 * 18, 17 + l * 18));
            }
        }
        for (int i1 = 0; i1 < 3; i1++) {
            for (int l1 = 0; l1 < 9; l1++) {
                this.m_38897_(new Slot(playerInventory, l1 + i1 * 9 + 9, 8 + l1 * 18, 84 + i1 * 18));
            }
        }
        for (int j1 = 0; j1 < 9; j1++) {
            this.m_38897_(new Slot(playerInventory, j1, 8 + j1 * 18, 142));
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