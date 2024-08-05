package noppes.npcs.packets.server;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.controllers.BankController;
import noppes.npcs.controllers.data.Bank;
import noppes.npcs.packets.PacketServerBasic;

public class SPacketBanksGet extends PacketServerBasic {

    public static void encode(SPacketBanksGet msg, FriendlyByteBuf buf) {
    }

    public static SPacketBanksGet decode(FriendlyByteBuf buf) {
        return new SPacketBanksGet();
    }

    @Override
    protected void handle() {
        sendBankDataAll(this.player);
    }

    public static void sendBankDataAll(ServerPlayer player) {
        Map<String, Integer> map = new HashMap();
        for (Bank bank : BankController.getInstance().banks.values()) {
            map.put(bank.name, bank.id);
        }
        NoppesUtilServer.sendScrollData(player, map);
    }
}