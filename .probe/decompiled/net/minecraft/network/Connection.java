package net.minecraft.network;

import com.google.common.collect.Queues;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mojang.logging.LogUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.local.LocalChannel;
import io.netty.channel.local.LocalServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.TimeoutException;
import io.netty.util.AttributeKey;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Queue;
import java.util.concurrent.RejectedExecutionException;
import javax.annotation.Nullable;
import javax.crypto.Cipher;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.BundlerInfo;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.ClientboundDisconnectPacket;
import net.minecraft.network.protocol.login.ClientboundLoginDisconnectPacket;
import net.minecraft.server.RunningOnDifferentThreadException;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.util.Mth;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class Connection extends SimpleChannelInboundHandler<Packet<?>> {

    private static final float AVERAGE_PACKETS_SMOOTHING = 0.75F;

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final Marker ROOT_MARKER = MarkerFactory.getMarker("NETWORK");

    public static final Marker PACKET_MARKER = Util.make(MarkerFactory.getMarker("NETWORK_PACKETS"), p_202569_ -> p_202569_.add(ROOT_MARKER));

    public static final Marker PACKET_RECEIVED_MARKER = Util.make(MarkerFactory.getMarker("PACKET_RECEIVED"), p_202562_ -> p_202562_.add(PACKET_MARKER));

    public static final Marker PACKET_SENT_MARKER = Util.make(MarkerFactory.getMarker("PACKET_SENT"), p_202557_ -> p_202557_.add(PACKET_MARKER));

    public static final AttributeKey<ConnectionProtocol> ATTRIBUTE_PROTOCOL = AttributeKey.valueOf("protocol");

    public static final LazyLoadedValue<NioEventLoopGroup> NETWORK_WORKER_GROUP = new LazyLoadedValue<>(() -> new NioEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Client IO #%d").setDaemon(true).build()));

    public static final LazyLoadedValue<EpollEventLoopGroup> NETWORK_EPOLL_WORKER_GROUP = new LazyLoadedValue<>(() -> new EpollEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Epoll Client IO #%d").setDaemon(true).build()));

    public static final LazyLoadedValue<DefaultEventLoopGroup> LOCAL_WORKER_GROUP = new LazyLoadedValue<>(() -> new DefaultEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Local Client IO #%d").setDaemon(true).build()));

    private final PacketFlow receiving;

    private final Queue<Connection.PacketHolder> queue = Queues.newConcurrentLinkedQueue();

    private Channel channel;

    private SocketAddress address;

    private PacketListener packetListener;

    private Component disconnectedReason;

    private boolean encrypted;

    private boolean disconnectionHandled;

    private int receivedPackets;

    private int sentPackets;

    private float averageReceivedPackets;

    private float averageSentPackets;

    private int tickCount;

    private boolean handlingFault;

    @Nullable
    private volatile Component delayedDisconnect;

    public Connection(PacketFlow packetFlow0) {
        this.receiving = packetFlow0;
    }

    public void channelActive(ChannelHandlerContext channelHandlerContext0) throws Exception {
        super.channelActive(channelHandlerContext0);
        this.channel = channelHandlerContext0.channel();
        this.address = this.channel.remoteAddress();
        try {
            this.setProtocol(ConnectionProtocol.HANDSHAKING);
        } catch (Throwable var3) {
            LOGGER.error(LogUtils.FATAL_MARKER, "Failed to change protocol to handshake", var3);
        }
        if (this.delayedDisconnect != null) {
            this.disconnect(this.delayedDisconnect);
        }
    }

    public void setProtocol(ConnectionProtocol connectionProtocol0) {
        this.channel.attr(ATTRIBUTE_PROTOCOL).set(connectionProtocol0);
        this.channel.attr(BundlerInfo.BUNDLER_PROVIDER).set(connectionProtocol0);
        this.channel.config().setAutoRead(true);
        LOGGER.debug("Enabled auto read");
    }

    public void channelInactive(ChannelHandlerContext channelHandlerContext0) {
        this.disconnect(Component.translatable("disconnect.endOfStream"));
    }

    public void exceptionCaught(ChannelHandlerContext channelHandlerContext0, Throwable throwable1) {
        if (throwable1 instanceof SkipPacketException) {
            LOGGER.debug("Skipping packet due to errors", throwable1.getCause());
        } else {
            boolean $$2 = !this.handlingFault;
            this.handlingFault = true;
            if (this.channel.isOpen()) {
                if (throwable1 instanceof TimeoutException) {
                    LOGGER.debug("Timeout", throwable1);
                    this.disconnect(Component.translatable("disconnect.timeout"));
                } else {
                    Component $$3 = Component.translatable("disconnect.genericReason", "Internal Exception: " + throwable1);
                    if ($$2) {
                        LOGGER.debug("Failed to sent packet", throwable1);
                        ConnectionProtocol $$4 = this.getCurrentProtocol();
                        Packet<?> $$5 = (Packet<?>) ($$4 == ConnectionProtocol.LOGIN ? new ClientboundLoginDisconnectPacket($$3) : new ClientboundDisconnectPacket($$3));
                        this.send($$5, PacketSendListener.thenRun(() -> this.disconnect($$3)));
                        this.setReadOnly();
                    } else {
                        LOGGER.debug("Double fault", throwable1);
                        this.disconnect($$3);
                    }
                }
            }
        }
    }

    protected void channelRead0(ChannelHandlerContext channelHandlerContext0, Packet<?> packet1) {
        if (this.channel.isOpen()) {
            try {
                genericsFtw(packet1, this.packetListener);
            } catch (RunningOnDifferentThreadException var4) {
            } catch (RejectedExecutionException var5) {
                this.disconnect(Component.translatable("multiplayer.disconnect.server_shutdown"));
            } catch (ClassCastException var6) {
                LOGGER.error("Received {} that couldn't be processed", packet1.getClass(), var6);
                this.disconnect(Component.translatable("multiplayer.disconnect.invalid_packet"));
            }
            this.receivedPackets++;
        }
    }

    private static <T extends PacketListener> void genericsFtw(Packet<T> packetT0, PacketListener packetListener1) {
        packetT0.handle((T) packetListener1);
    }

    public void setListener(PacketListener packetListener0) {
        Validate.notNull(packetListener0, "packetListener", new Object[0]);
        this.packetListener = packetListener0;
    }

    public void send(Packet<?> packet0) {
        this.send(packet0, null);
    }

    public void send(Packet<?> packet0, @Nullable PacketSendListener packetSendListener1) {
        if (this.isConnected()) {
            this.flushQueue();
            this.sendPacket(packet0, packetSendListener1);
        } else {
            this.queue.add(new Connection.PacketHolder(packet0, packetSendListener1));
        }
    }

    private void sendPacket(Packet<?> packet0, @Nullable PacketSendListener packetSendListener1) {
        ConnectionProtocol $$2 = ConnectionProtocol.getProtocolForPacket(packet0);
        ConnectionProtocol $$3 = this.getCurrentProtocol();
        this.sentPackets++;
        if ($$3 != $$2) {
            if ($$2 == null) {
                throw new IllegalStateException("Encountered packet without set protocol: " + packet0);
            }
            LOGGER.debug("Disabled auto read");
            this.channel.config().setAutoRead(false);
        }
        if (this.channel.eventLoop().inEventLoop()) {
            this.doSendPacket(packet0, packetSendListener1, $$2, $$3);
        } else {
            this.channel.eventLoop().execute(() -> this.doSendPacket(packet0, packetSendListener1, $$2, $$3));
        }
    }

    private void doSendPacket(Packet<?> packet0, @Nullable PacketSendListener packetSendListener1, ConnectionProtocol connectionProtocol2, ConnectionProtocol connectionProtocol3) {
        if (connectionProtocol2 != connectionProtocol3) {
            this.setProtocol(connectionProtocol2);
        }
        ChannelFuture $$4 = this.channel.writeAndFlush(packet0);
        if (packetSendListener1 != null) {
            $$4.addListener(p_243167_ -> {
                if (p_243167_.isSuccess()) {
                    packetSendListener1.onSuccess();
                } else {
                    Packet<?> $$2 = packetSendListener1.onFailure();
                    if ($$2 != null) {
                        ChannelFuture $$3 = this.channel.writeAndFlush($$2);
                        $$3.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
                    }
                }
            });
        }
        $$4.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }

    private ConnectionProtocol getCurrentProtocol() {
        return (ConnectionProtocol) this.channel.attr(ATTRIBUTE_PROTOCOL).get();
    }

    private void flushQueue() {
        if (this.channel != null && this.channel.isOpen()) {
            synchronized (this.queue) {
                Connection.PacketHolder $$0;
                while (($$0 = (Connection.PacketHolder) this.queue.poll()) != null) {
                    this.sendPacket($$0.packet, $$0.listener);
                }
            }
        }
    }

    public void tick() {
        this.flushQueue();
        if (this.packetListener instanceof TickablePacketListener $$0) {
            $$0.tick();
        }
        if (!this.isConnected() && !this.disconnectionHandled) {
            this.handleDisconnection();
        }
        if (this.channel != null) {
            this.channel.flush();
        }
        if (this.tickCount++ % 20 == 0) {
            this.tickSecond();
        }
    }

    protected void tickSecond() {
        this.averageSentPackets = Mth.lerp(0.75F, (float) this.sentPackets, this.averageSentPackets);
        this.averageReceivedPackets = Mth.lerp(0.75F, (float) this.receivedPackets, this.averageReceivedPackets);
        this.sentPackets = 0;
        this.receivedPackets = 0;
    }

    public SocketAddress getRemoteAddress() {
        return this.address;
    }

    public void disconnect(Component component0) {
        if (this.channel == null) {
            this.delayedDisconnect = component0;
        }
        if (this.isConnected()) {
            this.channel.close().awaitUninterruptibly();
            this.disconnectedReason = component0;
        }
    }

    public boolean isMemoryConnection() {
        return this.channel instanceof LocalChannel || this.channel instanceof LocalServerChannel;
    }

    public PacketFlow getReceiving() {
        return this.receiving;
    }

    public PacketFlow getSending() {
        return this.receiving.getOpposite();
    }

    public static Connection connectToServer(InetSocketAddress inetSocketAddress0, boolean boolean1) {
        Connection $$2 = new Connection(PacketFlow.CLIENTBOUND);
        ChannelFuture $$3 = connect(inetSocketAddress0, boolean1, $$2);
        $$3.syncUninterruptibly();
        return $$2;
    }

    public static ChannelFuture connect(InetSocketAddress inetSocketAddress0, boolean boolean1, final Connection connection2) {
        Class<? extends SocketChannel> $$3;
        LazyLoadedValue<? extends EventLoopGroup> $$4;
        if (Epoll.isAvailable() && boolean1) {
            $$3 = EpollSocketChannel.class;
            $$4 = NETWORK_EPOLL_WORKER_GROUP;
        } else {
            $$3 = NioSocketChannel.class;
            $$4 = NETWORK_WORKER_GROUP;
        }
        return ((Bootstrap) ((Bootstrap) ((Bootstrap) new Bootstrap().group($$4.get())).handler(new ChannelInitializer<Channel>() {

            protected void initChannel(Channel p_129552_) {
                try {
                    p_129552_.config().setOption(ChannelOption.TCP_NODELAY, true);
                } catch (ChannelException var3) {
                }
                ChannelPipeline $$1 = p_129552_.pipeline().addLast("timeout", new ReadTimeoutHandler(30));
                Connection.configureSerialization($$1, PacketFlow.CLIENTBOUND);
                $$1.addLast("packet_handler", connection2);
            }
        })).channel($$3)).connect(inetSocketAddress0.getAddress(), inetSocketAddress0.getPort());
    }

    public static void configureSerialization(ChannelPipeline channelPipeline0, PacketFlow packetFlow1) {
        PacketFlow $$2 = packetFlow1.getOpposite();
        channelPipeline0.addLast("splitter", new Varint21FrameDecoder()).addLast("decoder", new PacketDecoder(packetFlow1)).addLast("prepender", new Varint21LengthFieldPrepender()).addLast("encoder", new PacketEncoder($$2)).addLast("unbundler", new PacketBundleUnpacker($$2)).addLast("bundler", new PacketBundlePacker(packetFlow1));
    }

    public static Connection connectToLocalServer(SocketAddress socketAddress0) {
        final Connection $$1 = new Connection(PacketFlow.CLIENTBOUND);
        ((Bootstrap) ((Bootstrap) ((Bootstrap) new Bootstrap().group((EventLoopGroup) LOCAL_WORKER_GROUP.get())).handler(new ChannelInitializer<Channel>() {

            protected void initChannel(Channel p_129557_) {
                ChannelPipeline $$1 = p_129557_.pipeline();
                $$1.addLast("packet_handler", $$1);
            }
        })).channel(LocalChannel.class)).connect(socketAddress0).syncUninterruptibly();
        return $$1;
    }

    public void setEncryptionKey(Cipher cipher0, Cipher cipher1) {
        this.encrypted = true;
        this.channel.pipeline().addBefore("splitter", "decrypt", new CipherDecoder(cipher0));
        this.channel.pipeline().addBefore("prepender", "encrypt", new CipherEncoder(cipher1));
    }

    public boolean isEncrypted() {
        return this.encrypted;
    }

    public boolean isConnected() {
        return this.channel != null && this.channel.isOpen();
    }

    public boolean isConnecting() {
        return this.channel == null;
    }

    public PacketListener getPacketListener() {
        return this.packetListener;
    }

    @Nullable
    public Component getDisconnectedReason() {
        return this.disconnectedReason;
    }

    public void setReadOnly() {
        if (this.channel != null) {
            this.channel.config().setAutoRead(false);
        }
    }

    public void setupCompression(int int0, boolean boolean1) {
        if (int0 >= 0) {
            if (this.channel.pipeline().get("decompress") instanceof CompressionDecoder) {
                ((CompressionDecoder) this.channel.pipeline().get("decompress")).setThreshold(int0, boolean1);
            } else {
                this.channel.pipeline().addBefore("decoder", "decompress", new CompressionDecoder(int0, boolean1));
            }
            if (this.channel.pipeline().get("compress") instanceof CompressionEncoder) {
                ((CompressionEncoder) this.channel.pipeline().get("compress")).setThreshold(int0);
            } else {
                this.channel.pipeline().addBefore("encoder", "compress", new CompressionEncoder(int0));
            }
        } else {
            if (this.channel.pipeline().get("decompress") instanceof CompressionDecoder) {
                this.channel.pipeline().remove("decompress");
            }
            if (this.channel.pipeline().get("compress") instanceof CompressionEncoder) {
                this.channel.pipeline().remove("compress");
            }
        }
    }

    public void handleDisconnection() {
        if (this.channel != null && !this.channel.isOpen()) {
            if (this.disconnectionHandled) {
                LOGGER.warn("handleDisconnection() called twice");
            } else {
                this.disconnectionHandled = true;
                if (this.getDisconnectedReason() != null) {
                    this.getPacketListener().onDisconnect(this.getDisconnectedReason());
                } else if (this.getPacketListener() != null) {
                    this.getPacketListener().onDisconnect(Component.translatable("multiplayer.disconnect.generic"));
                }
            }
        }
    }

    public float getAverageReceivedPackets() {
        return this.averageReceivedPackets;
    }

    public float getAverageSentPackets() {
        return this.averageSentPackets;
    }

    static class PacketHolder {

        final Packet<?> packet;

        @Nullable
        final PacketSendListener listener;

        public PacketHolder(Packet<?> packet0, @Nullable PacketSendListener packetSendListener1) {
            this.packet = packet0;
            this.listener = packetSendListener1;
        }
    }
}