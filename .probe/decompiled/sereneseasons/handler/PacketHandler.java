package sereneseasons.handler;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import sereneseasons.network.message.MessageSyncSeasonCycle;

public class PacketHandler {

    public static final String PROTOCOL_VERSION = Integer.toString(0);

    public static final SimpleChannel HANDLER = NetworkRegistry.ChannelBuilder.named(new ResourceLocation("sereneseasons", "main_channel")).clientAcceptedVersions(PROTOCOL_VERSION::equals).serverAcceptedVersions(PROTOCOL_VERSION::equals).networkProtocolVersion(() -> PROTOCOL_VERSION).simpleChannel();

    private static int nextFreeIndex;

    public static void init() {
        registerMessage(MessageSyncSeasonCycle.class, MessageSyncSeasonCycle::encode, MessageSyncSeasonCycle::decode, MessageSyncSeasonCycle.Handler::handle);
    }

    private static <T> void registerMessage(Class<T> type, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> consumer) {
        HANDLER.registerMessage(nextFreeIndex++, type, encoder, decoder, consumer);
    }
}