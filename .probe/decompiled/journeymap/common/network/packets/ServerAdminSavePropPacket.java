package journeymap.common.network.packets;

import journeymap.common.Journeymap;
import journeymap.common.network.data.PacketContext;
import journeymap.common.network.data.Side;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class ServerAdminSavePropPacket {

    public static final ResourceLocation CHANNEL = new ResourceLocation("journeymap", "admin_save");

    private Integer type;

    private String payload;

    private String dimension;

    public ServerAdminSavePropPacket() {
    }

    public ServerAdminSavePropPacket(Integer type, String payload, String dimension) {
        this.payload = payload;
        this.type = type;
        this.dimension = dimension;
    }

    public int getType() {
        return this.type;
    }

    public String getPayload() {
        return this.payload;
    }

    public static ServerAdminSavePropPacket decode(FriendlyByteBuf buf) {
        ServerAdminSavePropPacket packet = new ServerAdminSavePropPacket();
        try {
            if (buf.readableBytes() > 1) {
                buf.readByte();
                packet.type = buf.readInt();
                packet.dimension = buf.readUtf(32767);
                packet.payload = buf.readUtf(32767);
            }
        } catch (Throwable var3) {
            Journeymap.getLogger().error(String.format("Failed to read message for admin save: %s", var3));
        }
        return packet;
    }

    public void encode(FriendlyByteBuf buf) {
        try {
            if (this.payload != null && this.type != null && this.dimension != null) {
                buf.writeByte(42);
                buf.writeInt(this.type);
                buf.writeUtf(this.dimension);
                buf.writeUtf(this.payload);
            }
        } catch (Throwable var3) {
            Journeymap.getLogger().error("[toBytes]Failed to write message for admin save:" + var3);
        }
    }

    public static void handle(PacketContext<ServerAdminSavePropPacket> ctx) {
        if (Side.SERVER.equals(ctx.side())) {
            Journeymap.getInstance().getPacketHandler().onServerAdminSave(ctx.sender(), ctx.message().type, ctx.message().payload, ctx.message().dimension);
        }
    }
}