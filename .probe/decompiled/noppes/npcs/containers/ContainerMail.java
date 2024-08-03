package noppes.npcs.containers;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.CustomContainer;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.PlayerMail;
import noppes.npcs.controllers.data.PlayerMailData;

public class ContainerMail extends ContainerNpcInterface {

    public static PlayerMail staticmail = new PlayerMail();

    public PlayerMail mail = staticmail;

    public final boolean canEdit;

    public final boolean canSend;

    public ContainerMail(int containerId, Inventory playerInventory, boolean canEdit, boolean canSend) {
        super(CustomContainer.container_mail, containerId, playerInventory);
        staticmail = new PlayerMail();
        this.canEdit = canEdit;
        this.canSend = canSend;
        playerInventory.m_5856_(this.player);
        for (int k = 0; k < 4; k++) {
            this.m_38897_(new SlotValid(this.mail, k, 179 + k * 24, 138, canEdit));
        }
        for (int j = 0; j < 3; j++) {
            for (int var7 = 0; var7 < 9; var7++) {
                this.m_38897_(new Slot(playerInventory, var7 + j * 9 + 9, 28 + var7 * 18, 175 + j * 18));
            }
        }
        for (int j = 0; j < 9; j++) {
            this.m_38897_(new Slot(playerInventory, j, 28 + j * 18, 230));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player par1Player, int limbSwingAmount) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) this.f_38839_.get(limbSwingAmount);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (limbSwingAmount < 4) {
                if (!this.m_38903_(itemstack1, 4, this.f_38839_.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.canEdit || !this.m_38903_(itemstack1, 0, 4, false)) {
                return null;
            }
            if (itemstack1.getCount() == 0) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemstack;
    }

    @Override
    public void removed(Player player) {
        super.m_6877_(player);
        if (!this.canEdit && !player.m_9236_().isClientSide) {
            PlayerMailData data = PlayerData.get(player).mailData;
            for (PlayerMail mail : data.playermail) {
                if (mail.time == this.mail.time && mail.sender.equals(this.mail.sender)) {
                    mail.readNBT(this.mail.writeNBT());
                    break;
                }
            }
        }
    }
}