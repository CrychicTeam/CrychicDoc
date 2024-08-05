package journeymap.common.network.packets;

import java.util.UUID;
import journeymap.client.JourneymapClient;
import journeymap.common.Journeymap;
import journeymap.common.network.data.PacketContext;
import journeymap.common.network.data.Side;
import journeymap.common.network.data.model.PlayerLocation;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class ServerPlayerLocationPacket implements PlayerLocation {

    public static final ResourceLocation CHANNEL = new ResourceLocation("journeymap", "player_loc");

    private int entityId;

    private UUID uniqueId;

    private double x;

    private double y;

    private double z;

    private byte yaw;

    private byte pitch;

    private boolean visible;

    public ServerPlayerLocationPacket() {
    }

    public ServerPlayerLocationPacket(Player player, boolean visible) {
        this.uniqueId = player.getGameProfile().getId();
        this.visible = visible;
        if (visible) {
            this.entityId = player.m_19879_();
            this.x = player.m_20185_();
            this.y = player.m_20186_();
            this.z = player.m_20189_();
            this.yaw = (byte) ((int) (player.m_146908_() * 256.0F / 360.0F));
            this.pitch = (byte) ((int) (player.m_146909_() * 256.0F / 360.0F));
        }
    }

    public static ServerPlayerLocationPacket decode(FriendlyByteBuf buf) {
        ServerPlayerLocationPacket packet = new ServerPlayerLocationPacket();
        try {
            if (buf.readableBytes() > 1) {
                packet.visible = buf.readBoolean();
                packet.uniqueId = buf.readUUID();
                if (packet.visible) {
                    packet.entityId = buf.readVarInt();
                    packet.x = buf.readDouble();
                    packet.y = buf.readDouble();
                    packet.z = buf.readDouble();
                    packet.yaw = buf.readByte();
                    packet.pitch = buf.readByte();
                }
            }
        } catch (Throwable var3) {
            Journeymap.getLogger().error("[toBytes]Failed to write message for player location request:" + var3);
        }
        return packet;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(this.visible);
        buf.writeUUID(this.uniqueId);
        if (this.visible) {
            buf.writeVarInt(this.entityId);
            buf.writeDouble(this.x);
            buf.writeDouble(this.y);
            buf.writeDouble(this.z);
            buf.writeByte(this.yaw);
            buf.writeByte(this.pitch);
        }
    }

    public static void handle(PacketContext<ServerPlayerLocationPacket> ctx) {
        if (Side.CLIENT.equals(ctx.side())) {
            JourneymapClient.getInstance().getPacketHandler().onPlayerLocationPacket(ctx.message());
        }
    }

    @Override
    public int getEntityId() {
        return this.entityId;
    }

    @Override
    public UUID getUniqueId() {
        return this.uniqueId;
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
    public byte getYaw() {
        return this.yaw;
    }

    @Override
    public byte getPitch() {
        return this.pitch;
    }

    @Override
    public boolean isVisible() {
        return this.visible;
    }
}