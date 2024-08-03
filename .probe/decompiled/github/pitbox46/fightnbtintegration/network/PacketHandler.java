package github.pitbox46.fightnbtintegration.network;

import java.util.function.Function;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {

    private static final String PROTOCOL_VERSION = "3.2.1";

    public static SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation("fightnbtintegration", "main"), () -> "3.2.1", "3.2.1"::equals, "3.2.1"::equals);

    private static int ID = 0;

    public static void init() {
        registerPacket(SSyncConfig.class, pb -> new SSyncConfig().readPacketData(pb));
    }

    public static <T extends IPacket> void registerPacket(Class<T> packetClass, Function<FriendlyByteBuf, T> decoder) {
        CHANNEL.registerMessage(ID++, packetClass, IPacket::writePacketData, decoder, (msg, ctx) -> {
            ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> msg.processPacket((NetworkEvent.Context) ctx.get()));
            ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
        });
    }
}