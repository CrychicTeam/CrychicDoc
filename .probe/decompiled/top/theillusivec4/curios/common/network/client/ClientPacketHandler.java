package top.theillusivec4.curios.common.network.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import top.theillusivec4.curios.common.inventory.container.CuriosContainerV2;
import top.theillusivec4.curios.common.network.server.SPacketQuickMove;

public class ClientPacketHandler {

    public static void handlePacket(SPacketQuickMove msg) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player != null && player.f_36096_ instanceof CuriosContainerV2 container) {
            container.quickMoveStack(player, msg.moveIndex);
        }
    }
}