package noppes.npcs.packets.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noppes.npcs.shared.common.PacketBasic;

public class PacketChat extends PacketBasic {

    private final Component message;

    public PacketChat(Component message) {
        this.message = message;
    }

    public static void encode(PacketChat msg, FriendlyByteBuf buf) {
        buf.writeComponent(msg.message);
    }

    public static PacketChat decode(FriendlyByteBuf buf) {
        return new PacketChat(buf.readComponent());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handle() {
        this.player.m_213846_(this.message);
    }
}