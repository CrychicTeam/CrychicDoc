package journeymap.common.network.packets;

import journeymap.client.JourneymapClient;
import journeymap.common.Journeymap;
import journeymap.common.network.data.PacketContext;
import journeymap.common.network.data.Side;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class MultiplayerOptionsPacket {

    public static final ResourceLocation CHANNEL = new ResourceLocation("journeymap", "mp_options_req");

    private String payload;

    public MultiplayerOptionsPacket() {
    }

    public MultiplayerOptionsPacket(String payload) {
        this.payload = payload;
    }

    public String getPayload() {
        return this.payload;
    }

    public static MultiplayerOptionsPacket decode(FriendlyByteBuf buf) {
        MultiplayerOptionsPacket packet = new MultiplayerOptionsPacket();
        try {
            if (buf.readableBytes() > 1) {
                buf.readByte();
                packet.payload = buf.readUtf(32767);
            }
        } catch (Throwable var3) {
            Journeymap.getLogger().error(String.format("Failed to read message for multiplayer options request: %s", var3));
        }
        return packet;
    }

    public void encode(FriendlyByteBuf buf) {
        try {
            if (this.payload != null) {
                buf.writeByte(42);
                buf.writeUtf(this.payload);
            }
        } catch (Throwable var3) {
            Journeymap.getLogger().error("[toBytes]Failed to write message for multiplayer options request:" + var3);
        }
    }

    public static void handle(PacketContext<MultiplayerOptionsPacket> ctx) {
        if (Side.SERVER.equals(ctx.side())) {
            if (ctx.message().getPayload() == null) {
                Journeymap.getInstance().getPacketHandler().onMultiplayerOptionsOpen(ctx.sender());
            } else {
                Journeymap.getInstance().getPacketHandler().onMultiplayerOptionsSave(ctx.sender(), ctx.message().getPayload());
            }
        } else {
            JourneymapClient.getInstance().getPacketHandler().onMultiplayerDataResponse(ctx.message().getPayload());
        }
    }
}