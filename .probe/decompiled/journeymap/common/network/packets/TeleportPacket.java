package journeymap.common.network.packets;

import journeymap.common.Journeymap;
import journeymap.common.network.data.PacketContext;
import journeymap.common.network.data.Side;
import journeymap.common.network.data.model.Location;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class TeleportPacket implements Location {

    public static final ResourceLocation CHANNEL = new ResourceLocation("journeymap", "teleport_req");

    private double x;

    private double y;

    private double z;

    private String dim;

    public TeleportPacket() {
    }

    public TeleportPacket(double x, double y, double z, String dim) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dim = dim;
    }

    public static TeleportPacket decode(FriendlyByteBuf buf) {
        TeleportPacket packet = new TeleportPacket();
        try {
            if (buf.readableBytes() > 1) {
                packet.x = buf.readDouble();
                packet.y = buf.readDouble();
                packet.z = buf.readDouble();
                packet.dim = buf.readUtf(32767);
            }
        } catch (Throwable var3) {
            Journeymap.getLogger().error(String.format("Failed to read message for teleport packet: %s", var3));
        }
        return packet;
    }

    @Override
    public double getX() {
        return this.x;
    }

    @Override
    public double getY() {
        return this.y;
    }

    @Override
    public double getZ() {
        return this.z;
    }

    @Override
    public String getDim() {
        return this.dim;
    }

    public void encode(FriendlyByteBuf buf) {
        try {
            buf.writeDouble(this.x);
            buf.writeDouble(this.y);
            buf.writeDouble(this.z);
            buf.writeUtf(this.dim);
        } catch (Throwable var3) {
            Journeymap.getLogger().error("[toBytes]Failed to write message for teleport packet:" + var3);
        }
    }

    public static void handle(PacketContext<TeleportPacket> ctx) {
        if (Side.SERVER.equals(ctx.side())) {
            Journeymap.getInstance().getPacketHandler().handleTeleportPacket(ctx.sender(), ctx.message());
        }
    }
}