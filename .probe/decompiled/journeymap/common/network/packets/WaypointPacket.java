package journeymap.common.network.packets;

import journeymap.client.JourneymapClient;
import journeymap.common.Journeymap;
import journeymap.common.network.data.PacketContext;
import journeymap.common.network.data.Side;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class WaypointPacket {

    public static final ResourceLocation CHANNEL = new ResourceLocation("journeymap", "waypoint");

    private String action;

    private String waypoint;

    private boolean announce;

    public WaypointPacket() {
    }

    public WaypointPacket(String waypoint, boolean announce, String action) {
        this.action = action;
        this.waypoint = waypoint;
        this.announce = announce;
    }

    public static WaypointPacket decode(FriendlyByteBuf buf) {
        WaypointPacket packet = new WaypointPacket();
        try {
            if (buf.readableBytes() > 1) {
                packet.waypoint = buf.readUtf();
                packet.action = buf.readUtf();
                packet.announce = buf.readBoolean();
            }
        } catch (Throwable var3) {
            Journeymap.getLogger().error(String.format("Failed to read message for waypoint: %s", var3));
        }
        return packet;
    }

    public void encode(FriendlyByteBuf buf) {
        try {
            buf.writeUtf(this.waypoint);
            buf.writeUtf(this.action);
            buf.writeBoolean(this.announce);
        } catch (Throwable var3) {
            Journeymap.getLogger().error("[toBytes]Failed to read message for waypoint: " + var3);
        }
    }

    public static void handle(PacketContext<WaypointPacket> ctx) {
        if (Side.CLIENT.equals(ctx.side())) {
            JourneymapClient.getInstance().getPacketHandler().onWaypointCreatePacket(ctx.message().waypoint, ctx.message().action, ctx.message().announce);
        }
    }
}