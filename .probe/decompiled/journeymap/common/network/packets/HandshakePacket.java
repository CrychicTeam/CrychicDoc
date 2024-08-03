package journeymap.common.network.packets;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import journeymap.common.Journeymap;
import journeymap.common.network.data.PacketContext;
import journeymap.common.network.data.Side;
import journeymap.common.version.Version;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class HandshakePacket {

    public static final ResourceLocation CHANNEL = new ResourceLocation("journeymap", "version");

    private String version;

    public HandshakePacket() {
    }

    public HandshakePacket(String version) {
        this.version = version;
    }

    public static HandshakePacket decode(FriendlyByteBuf buf) {
        HandshakePacket packet = new HandshakePacket();
        try {
            if (buf.readableBytes() > 1) {
                packet.version = buf.readUtf(32767);
            }
        } catch (Throwable var3) {
            Journeymap.getLogger().error(String.format("Failed to read message for handshake: %s", var3));
        }
        return packet;
    }

    public void encode(FriendlyByteBuf buf) {
        try {
            buf.writeUtf(this.version);
        } catch (Throwable var3) {
            Journeymap.getLogger().error("[toBytes]Failed to read message for handshake: " + var3);
        }
    }

    public static void handle(PacketContext<HandshakePacket> ctx) {
        String jsonString = ctx.message().version;
        try {
            Version otherVersion = Version.fromJson(jsonString);
            if (Side.CLIENT.equals(ctx.side())) {
                if (!otherVersion.isValid(Journeymap.MINIMUM_SERVER_ACCEPTABLE_VERSION, ctx.side())) {
                    String message = "Journeymap Version Mismatch need at least " + Journeymap.MINIMUM_SERVER_ACCEPTABLE_VERSION + " or higher. Current " + ctx.side().opposite() + " version attempt -> " + otherVersion;
                    Minecraft.getInstance().player.connection.getConnection().disconnect(Component.literal(message));
                }
            } else if (otherVersion.isValid(Journeymap.MINIMUM_CLIENT_ACCEPTABLE_VERSION, ctx.side())) {
                Journeymap.getInstance().getDispatcher().sendHandshakePacket(ctx.sender(), Journeymap.JM_VERSION.toJson());
            } else {
                String message = "Journeymap Version Mismatch need at least " + Journeymap.MINIMUM_CLIENT_ACCEPTABLE_VERSION + " or higher. Current " + ctx.side().opposite() + " version attempt -> " + otherVersion;
                disconnect(ctx.sender(), message);
            }
        } catch (Exception var4) {
            String message = "Error: Likely due to version mismatch " + ctx.side() + " is running:" + Journeymap.JM_VERSION + " Exception:" + var4;
            disconnect(ctx.sender(), message + var4);
        }
    }

    public static void disconnect(ServerPlayer player, String message) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        Component text = Component.literal(message);
        player.connection.send(new ClientboundSystemChatPacket(text, false));
        executorService.schedule(() -> player.connection.disconnect(text), 1L, TimeUnit.SECONDS);
    }
}