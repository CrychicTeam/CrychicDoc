package noppes.npcs.packets.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.packets.PacketServerBasic;

public class SPacketPlayerCloseContainer extends PacketServerBasic {

    @Override
    public boolean toolAllowed(ItemStack item) {
        return true;
    }

    public static void encode(SPacketPlayerCloseContainer msg, FriendlyByteBuf buf) {
    }

    public static SPacketPlayerCloseContainer decode(FriendlyByteBuf buf) {
        return new SPacketPlayerCloseContainer();
    }

    @Override
    protected void handle() {
        this.player.closeContainer();
    }
}