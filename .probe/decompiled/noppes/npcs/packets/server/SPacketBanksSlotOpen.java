package noppes.npcs.packets.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.data.BankData;
import noppes.npcs.packets.PacketServerBasic;

public class SPacketBanksSlotOpen extends PacketServerBasic {

    private final int slot;

    private final int bankId;

    public SPacketBanksSlotOpen(int slot, int bankId) {
        this.slot = slot;
        this.bankId = bankId;
    }

    @Override
    public boolean toolAllowed(ItemStack item) {
        return true;
    }

    @Override
    public boolean requiresNpc() {
        return true;
    }

    public static void encode(SPacketBanksSlotOpen msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.slot);
        buf.writeInt(msg.bankId);
    }

    public static SPacketBanksSlotOpen decode(FriendlyByteBuf buf) {
        return new SPacketBanksSlotOpen(buf.readInt(), buf.readInt());
    }

    @Override
    protected void handle() {
        if (this.npc.role.getType() == 3) {
            BankData data = PlayerDataController.instance.getBankData(this.player, this.bankId).getBankOrDefault(this.bankId);
            data.openBankGui(this.player, this.npc, this.bankId, this.slot);
        }
    }
}