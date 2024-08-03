package noppes.npcs.packets.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.EventHooks;
import noppes.npcs.api.event.RoleEvent;
import noppes.npcs.containers.ContainerNPCBankInterface;
import noppes.npcs.controllers.BankController;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.data.Bank;
import noppes.npcs.controllers.data.BankData;
import noppes.npcs.controllers.data.PlayerBankData;
import noppes.npcs.packets.PacketServerBasic;

public class SPacketBankUpgrade extends PacketServerBasic {

    @Override
    public boolean toolAllowed(ItemStack item) {
        return true;
    }

    @Override
    public boolean requiresNpc() {
        return true;
    }

    public static void encode(SPacketBankUpgrade msg, FriendlyByteBuf buf) {
    }

    public static SPacketBankUpgrade decode(FriendlyByteBuf buf) {
        return new SPacketBankUpgrade();
    }

    @Override
    protected void handle() {
        if (this.npc.role.getType() == 3) {
            AbstractContainerMenu con = this.player.f_36096_;
            if (con != null && con instanceof ContainerNPCBankInterface container) {
                Bank bank = BankController.getInstance().getBank(container.bankid);
                ItemStack item = bank.upgradeInventory.getItem(container.slot);
                if (item != null && !item.isEmpty()) {
                    int price = item.getCount();
                    ItemStack currency = container.currencyMatrix.getItem(0);
                    if (currency != null && !currency.isEmpty() && price <= currency.getCount()) {
                        if (currency.getCount() - price == 0) {
                            container.currencyMatrix.setItem(0, ItemStack.EMPTY);
                        } else {
                            currency = currency.split(price);
                        }
                        this.player.closeContainer();
                        PlayerBankData data = PlayerDataController.instance.getBankData(this.player, bank.id);
                        BankData bankData = data.getBank(bank.id);
                        bankData.upgradedSlots.put(container.slot, true);
                        RoleEvent.BankUpgradedEvent event = new RoleEvent.BankUpgradedEvent(this.player, this.npc.wrappedNPC, container.slot);
                        EventHooks.onNPCRole(this.npc, event);
                        bankData.openBankGui(this.player, this.npc, bank.id, container.slot);
                    }
                }
            }
        }
    }
}