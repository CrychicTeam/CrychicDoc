package net.minecraftforge.network;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceArrayMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.login.ClientboundCustomQueryPacket;
import net.minecraft.network.protocol.login.ServerboundCustomQueryPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.LogicalSide;
import org.apache.commons.lang3.tuple.Pair;

public enum NetworkDirection {

    PLAY_TO_SERVER(NetworkEvent.ClientCustomPayloadEvent::new, LogicalSide.CLIENT, ServerboundCustomPayloadPacket.class, 1, (d, i, n) -> new ServerboundCustomPayloadPacket(n, d)), PLAY_TO_CLIENT(NetworkEvent.ServerCustomPayloadEvent::new, LogicalSide.SERVER, ClientboundCustomPayloadPacket.class, 0, (d, i, n) -> new ClientboundCustomPayloadPacket(n, d)), LOGIN_TO_SERVER(NetworkEvent.ClientCustomPayloadLoginEvent::new, LogicalSide.CLIENT, ServerboundCustomQueryPacket.class, 3, (d, i, n) -> new ServerboundCustomQueryPacket(i, d)), LOGIN_TO_CLIENT(NetworkEvent.ServerCustomPayloadLoginEvent::new, LogicalSide.SERVER, ClientboundCustomQueryPacket.class, 2, (d, i, n) -> new ClientboundCustomQueryPacket(i, n, d));

    private final BiFunction<ICustomPacket<?>, Supplier<NetworkEvent.Context>, NetworkEvent> eventSupplier;

    private final LogicalSide logicalSide;

    private final Class<? extends Packet> packetClass;

    private final int otherWay;

    private final NetworkDirection.Factory factory;

    private static final Reference2ReferenceArrayMap<Class<? extends Packet>, NetworkDirection> packetLookup = (Reference2ReferenceArrayMap<Class<? extends Packet>, NetworkDirection>) Stream.of(values()).collect(Collectors.toMap(NetworkDirection::getPacketClass, Function.identity(), (m1, m2) -> m1, Reference2ReferenceArrayMap::new));

    private NetworkDirection(BiFunction<ICustomPacket<?>, Supplier<NetworkEvent.Context>, NetworkEvent> eventSupplier, LogicalSide logicalSide, Class<? extends Packet> clazz, int i, NetworkDirection.Factory factory) {
        this.eventSupplier = eventSupplier;
        this.logicalSide = logicalSide;
        this.packetClass = clazz;
        this.otherWay = i;
        this.factory = factory;
    }

    private Class<? extends Packet> getPacketClass() {
        return this.packetClass;
    }

    public static <T extends ICustomPacket<?>> NetworkDirection directionFor(Class<T> customPacket) {
        return (NetworkDirection) packetLookup.get(customPacket);
    }

    public NetworkDirection reply() {
        return values()[this.otherWay];
    }

    public NetworkEvent getEvent(ICustomPacket<?> buffer, Supplier<NetworkEvent.Context> manager) {
        return (NetworkEvent) this.eventSupplier.apply(buffer, manager);
    }

    public LogicalSide getOriginationSide() {
        return this.logicalSide;
    }

    public LogicalSide getReceptionSide() {
        return this.reply().logicalSide;
    }

    public <T extends Packet<?>> ICustomPacket<T> buildPacket(Pair<FriendlyByteBuf, Integer> packetData, ResourceLocation channelName) {
        return this.factory.create((FriendlyByteBuf) packetData.getLeft(), (Integer) packetData.getRight(), channelName);
    }

    private interface Factory<T extends Packet<?>> {

        ICustomPacket<T> create(FriendlyByteBuf var1, Integer var2, ResourceLocation var3);
    }
}