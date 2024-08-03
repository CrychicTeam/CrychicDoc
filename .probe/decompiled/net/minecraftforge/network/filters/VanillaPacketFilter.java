package net.minecraftforge.network.filters;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import java.util.List;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Function;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import org.jetbrains.annotations.NotNull;

public abstract class VanillaPacketFilter extends MessageToMessageEncoder<Packet<?>> {

    protected final Map<Class<? extends Packet<?>>, BiConsumer<Packet<?>, List<? super Packet<?>>>> handlers;

    protected VanillaPacketFilter(Map<Class<? extends Packet<?>>, BiConsumer<Packet<?>, List<? super Packet<?>>>> handlers) {
        this.handlers = handlers;
    }

    @NotNull
    protected static <T extends Packet<?>> Entry<Class<? extends Packet<?>>, BiConsumer<Packet<?>, List<? super Packet<?>>>> handler(Class<T> cls, Function<T, ? extends Packet<?>> function) {
        return handler(cls, (BiConsumer<Packet<?>, List<? super Packet<?>>>) ((pkt, list) -> list.add(function.apply((Packet) cls.cast(pkt)))));
    }

    @NotNull
    protected static <T extends Packet<?>> Entry<Class<? extends Packet<?>>, BiConsumer<Packet<?>, List<? super Packet<?>>>> handler(Class<T> cls, BiConsumer<Packet<?>, List<? super Packet<?>>> consumer) {
        return new SimpleEntry(cls, consumer);
    }

    protected abstract boolean isNecessary(Connection var1);

    protected void encode(ChannelHandlerContext ctx, Packet<?> msg, List<Object> out) {
        BiConsumer<Packet<?>, List<? super Packet<?>>> consumer = (BiConsumer<Packet<?>, List<? super Packet<?>>>) this.handlers.getOrDefault(msg.getClass(), (BiConsumer) (pkt, list) -> list.add(pkt));
        consumer.accept(msg, out);
    }
}