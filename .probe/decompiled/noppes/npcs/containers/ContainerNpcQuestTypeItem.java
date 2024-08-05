package noppes.npcs.containers;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.CustomContainer;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.controllers.data.Quest;
import noppes.npcs.quests.QuestItem;

public class ContainerNpcQuestTypeItem extends AbstractContainerMenu {

    public ContainerNpcQuestTypeItem(int containerId, Inventory playerInventory) {
        super(CustomContainer.container_questtypeitem, containerId);
        Quest quest = NoppesUtilServer.getEditingQuest(playerInventory.player);
        for (int i1 = 0; i1 < 3; i1++) {
            this.m_38897_(new Slot(((QuestItem) quest.questInterface).items, i1, 44, 39 + i1 * 25));
        }
        for (int i1 = 0; i1 < 3; i1++) {
            for (int l1 = 0; l1 < 9; l1++) {
                this.m_38897_(new Slot(playerInventory, l1 + i1 * 9 + 9, 8 + l1 * 18, 113 + i1 * 18));
            }
        }
        for (int j1 = 0; j1 < 9; j1++) {
            this.m_38897_(new Slot(playerInventory, j1, 8 + j1 * 18, 171));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player par1Player, int i) {
        return null;
    }

    @Override
    public boolean stillValid(Player entityplayer) {
        return true;
    }
}