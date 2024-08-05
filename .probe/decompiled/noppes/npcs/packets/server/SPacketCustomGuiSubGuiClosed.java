package noppes.npcs.packets.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.containers.ContainerCustomGui;
import noppes.npcs.packets.PacketServerBasic;

public class SPacketCustomGuiSubGuiClosed extends PacketServerBasic {

    @Override
    public boolean toolAllowed(ItemStack item) {
        return true;
    }

    public static void encode(SPacketCustomGuiSubGuiClosed msg, FriendlyByteBuf buf) {
    }

    public static SPacketCustomGuiSubGuiClosed decode(FriendlyByteBuf buf) {
        return new SPacketCustomGuiSubGuiClosed();
    }

    @Override
    protected void handle() {
        if (this.player.f_36096_ instanceof ContainerCustomGui container && container.customGui.hasSubGui()) {
            container.activeGui.close();
        }
    }
}