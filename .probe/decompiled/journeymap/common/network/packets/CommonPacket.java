package journeymap.common.network.packets;

import journeymap.common.Journeymap;
import journeymap.common.network.data.PacketContext;
import journeymap.common.network.data.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class CommonPacket {

    public static final ResourceLocation CHANNEL = new ResourceLocation("journeymap", "common");

    public static CommonPacket decode(FriendlyByteBuf buf) {
        return new CommonPacket();
    }

    public void encode(FriendlyByteBuf buf) {
    }

    public static void handle(PacketContext<CommonPacket> ctx) {
        if (Side.CLIENT.equals(ctx.side())) {
            String message = "Journeymap Version Mismatch need at least " + Journeymap.MINIMUM_SERVER_ACCEPTABLE_VERSION + " or higher. Current " + ctx.side().opposite() + " version attempt -> unknown";
            Journeymap.getLogger().warn(message);
            Minecraft.getInstance().player.connection.getConnection().disconnect(Component.literal(message));
        } else {
            String message = "Journeymap Version Mismatch need at least " + Journeymap.MINIMUM_CLIENT_ACCEPTABLE_VERSION + " or higher. Current " + ctx.side().opposite() + " version attempt -> unknown";
            Journeymap.getLogger().warn(message);
            HandshakePacket.disconnect(ctx.sender(), message);
        }
    }
}