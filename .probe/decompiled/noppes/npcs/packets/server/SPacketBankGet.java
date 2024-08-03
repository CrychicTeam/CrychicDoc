package noppes.npcs.packets.server;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import noppes.npcs.containers.ContainerManageBanks;
import noppes.npcs.controllers.BankController;
import noppes.npcs.controllers.data.Bank;
import noppes.npcs.packets.PacketServerBasic;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.client.PacketGuiData;

public class SPacketBankGet extends PacketServerBasic {

    private int bank;

    public SPacketBankGet(int bank) {
        this.bank = bank;
    }

    public static void encode(SPacketBankGet msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.bank);
    }

    public static SPacketBankGet decode(FriendlyByteBuf buf) {
        return new SPacketBankGet(buf.readInt());
    }

    @Override
    protected void handle() {
        sendBank(this.player, BankController.getInstance().getBank(this.bank));
    }

    public static void sendBank(ServerPlayer player, Bank bank) {
        CompoundTag compound = new CompoundTag();
        bank.addAdditionalSaveData(compound);
        Packets.send(player, new PacketGuiData(compound));
        if (player.f_36096_ instanceof ContainerManageBanks) {
            ((ContainerManageBanks) player.f_36096_).setBank(bank);
        }
        player.f_36096_.sendAllDataToRemote();
    }
}