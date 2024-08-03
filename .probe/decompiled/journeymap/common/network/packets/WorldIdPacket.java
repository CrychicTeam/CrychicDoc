package journeymap.common.network.packets;

import journeymap.client.JourneymapClient;
import journeymap.common.Journeymap;
import journeymap.common.network.data.PacketContext;
import journeymap.common.network.data.Side;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class WorldIdPacket {

    public static final ResourceLocation CHANNEL = new ResourceLocation("worldinfo", "world_id");

    private String worldId;

    public WorldIdPacket() {
        this.worldId = "";
    }

    public WorldIdPacket(String worldId) {
        this.worldId = worldId;
    }

    public String getWorldId() {
        return this.worldId;
    }

    public static WorldIdPacket decode(FriendlyByteBuf buf) {
        WorldIdPacket packet = new WorldIdPacket();
        try {
            if (buf.readableBytes() > 1) {
                buf.readByte();
                packet.worldId = buf.readUtf(32767);
            }
        } catch (Throwable var3) {
            Journeymap.getLogger().error(String.format("Failed to read message for worldId: %s", var3));
        }
        return packet;
    }

    public void encode(FriendlyByteBuf buf) {
        try {
            if (this.worldId != null) {
                buf.writeByte(42);
                buf.writeUtf(this.worldId);
            }
        } catch (Throwable var3) {
            Journeymap.getLogger().error("[toBytes]Failed to write message for worldId:" + var3);
        }
    }

    public static void handle(PacketContext<WorldIdPacket> ctx) {
        if (Side.CLIENT.equals(ctx.side())) {
            JourneymapClient.getInstance().getPacketHandler().onWorldIdReceived(ctx.message().getWorldId());
        } else {
            Journeymap.getInstance().getPacketHandler().onWorldIdRequest(ctx.sender());
        }
    }
}