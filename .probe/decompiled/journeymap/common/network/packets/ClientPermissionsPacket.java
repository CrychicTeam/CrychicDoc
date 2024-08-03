package journeymap.common.network.packets;

import journeymap.client.JourneymapClient;
import journeymap.common.Journeymap;
import journeymap.common.network.data.PacketContext;
import journeymap.common.network.data.Side;
import journeymap.common.network.data.model.ClientState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class ClientPermissionsPacket implements ClientState {

    public static final ResourceLocation CHANNEL = new ResourceLocation("journeymap", "perm_req");

    private String payload;

    private boolean serverAdmin;

    private boolean hasServerMod = false;

    public ClientPermissionsPacket() {
        this.payload = "";
    }

    public ClientPermissionsPacket(String payload, boolean serverAdmin, boolean hasServerMod) {
        this.payload = payload;
        this.serverAdmin = serverAdmin;
        this.hasServerMod = hasServerMod;
    }

    @Override
    public String getPayload() {
        return this.payload;
    }

    @Override
    public boolean isServerAdmin() {
        return this.serverAdmin;
    }

    @Override
    public boolean hasServerMod() {
        return this.hasServerMod;
    }

    public static ClientPermissionsPacket decode(FriendlyByteBuf buf) {
        ClientPermissionsPacket packet = new ClientPermissionsPacket();
        try {
            if (buf.readableBytes() > 1) {
                buf.readByte();
                packet.serverAdmin = buf.readBoolean();
                packet.payload = buf.readUtf(32767);
                try {
                    packet.hasServerMod = buf.readBoolean();
                } catch (Exception var3) {
                    Journeymap.getLogger().debug("Permission set from external source.");
                    packet.hasServerMod = false;
                }
            }
        } catch (Throwable var4) {
            Journeymap.getLogger().error(String.format("Failed to read message for client permissions: %s", var4));
        }
        return packet;
    }

    public void encode(FriendlyByteBuf buf) {
        try {
            if (this.payload != null) {
                buf.writeByte(42);
                buf.writeBoolean(this.serverAdmin);
                buf.writeUtf(this.payload);
                buf.writeBoolean(this.hasServerMod);
            }
        } catch (Throwable var3) {
            Journeymap.getLogger().error("[toBytes]Failed to write message for client permissions:" + var3);
        }
    }

    public static void handle(PacketContext<ClientPermissionsPacket> ctx) {
        if (Side.SERVER.equals(ctx.side())) {
            Journeymap.getInstance().getPacketHandler().onClientPermsRequest(ctx.sender());
        } else {
            JourneymapClient.getInstance().getPacketHandler().onClientStateUpdate(ctx.message());
        }
    }
}