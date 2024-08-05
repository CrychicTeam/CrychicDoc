package dev.shadowsoffire.placebo.network;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

public class MessageHelper {

    public static <MSG> void registerMessage(SimpleChannel channel, int id, Class<MSG> messageType, BiConsumer<MSG, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, MSG> decoder, BiConsumer<MSG, Supplier<NetworkEvent.Context>> messageConsumer) {
        channel.registerMessage(id, messageType, encoder, decoder, messageConsumer);
    }

    public static <T> void registerMessage(SimpleChannel channel, int id, MessageProvider<T> prov) {
        channel.registerMessage(id, prov.getMsgClass(), prov::write, prov::read, prov::handle, prov.getNetworkDirection());
    }

    public static void handlePacket(Runnable r, Supplier<NetworkEvent.Context> ctxSup) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSup.get();
        ctx.enqueueWork(r);
        ctx.setPacketHandled(true);
    }
}