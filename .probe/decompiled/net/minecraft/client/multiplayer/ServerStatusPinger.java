package net.minecraft.client.multiplayer;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.logging.LogUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.multiplayer.resolver.ResolvedServerAddress;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.client.multiplayer.resolver.ServerNameResolver;
import net.minecraft.network.Connection;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.handshake.ClientIntentionPacket;
import net.minecraft.network.protocol.status.ClientStatusPacketListener;
import net.minecraft.network.protocol.status.ClientboundPongResponsePacket;
import net.minecraft.network.protocol.status.ClientboundStatusResponsePacket;
import net.minecraft.network.protocol.status.ServerStatus;
import net.minecraft.network.protocol.status.ServerboundPingRequestPacket;
import net.minecraft.network.protocol.status.ServerboundStatusRequestPacket;
import net.minecraft.util.Mth;
import org.slf4j.Logger;

public class ServerStatusPinger {

    static final Splitter SPLITTER = Splitter.on('\u0000').limit(6);

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final Component CANT_CONNECT_MESSAGE = Component.translatable("multiplayer.status.cannot_connect").withStyle(p_265659_ -> p_265659_.withColor(-65536));

    private final List<Connection> connections = Collections.synchronizedList(Lists.newArrayList());

    public void pingServer(final ServerData serverData0, final Runnable runnable1) throws UnknownHostException {
        ServerAddress $$2 = ServerAddress.parseString(serverData0.ip);
        Optional<InetSocketAddress> $$3 = ServerNameResolver.DEFAULT.resolveAddress($$2).map(ResolvedServerAddress::m_142641_);
        if (!$$3.isPresent()) {
            this.onPingFailed(ConnectScreen.UNKNOWN_HOST_MESSAGE, serverData0);
        } else {
            final InetSocketAddress $$4 = (InetSocketAddress) $$3.get();
            final Connection $$5 = Connection.connectToServer($$4, false);
            this.connections.add($$5);
            serverData0.motd = Component.translatable("multiplayer.status.pinging");
            serverData0.ping = -1L;
            serverData0.playerList = Collections.emptyList();
            $$5.setListener(new ClientStatusPacketListener() {

                private boolean success;

                private boolean receivedPing;

                private long pingStart;

                @Override
                public void handleStatusResponse(ClientboundStatusResponsePacket p_105489_) {
                    if (this.receivedPing) {
                        $$5.disconnect(Component.translatable("multiplayer.status.unrequested"));
                    } else {
                        this.receivedPing = true;
                        ServerStatus $$1 = p_105489_.status();
                        serverData0.motd = $$1.description();
                        $$1.version().ifPresentOrElse(p_273307_ -> {
                            serverData0.version = Component.literal(p_273307_.name());
                            serverData0.protocol = p_273307_.protocol();
                        }, () -> {
                            serverData0.version = Component.translatable("multiplayer.status.old");
                            serverData0.protocol = 0;
                        });
                        $$1.players().ifPresentOrElse(p_273230_ -> {
                            serverData0.status = ServerStatusPinger.formatPlayerCount(p_273230_.online(), p_273230_.max());
                            serverData0.players = p_273230_;
                            if (!p_273230_.sample().isEmpty()) {
                                List<Component> $$2 = new ArrayList(p_273230_.sample().size());
                                for (GameProfile $$3 : p_273230_.sample()) {
                                    $$2.add(Component.literal($$3.getName()));
                                }
                                if (p_273230_.sample().size() < p_273230_.online()) {
                                    $$2.add(Component.translatable("multiplayer.status.and_more", p_273230_.online() - p_273230_.sample().size()));
                                }
                                serverData0.playerList = $$2;
                            } else {
                                serverData0.playerList = List.of();
                            }
                        }, () -> serverData0.status = Component.translatable("multiplayer.status.unknown").withStyle(ChatFormatting.DARK_GRAY));
                        $$1.favicon().ifPresent(p_272704_ -> {
                            if (!Arrays.equals(p_272704_.iconBytes(), serverData0.getIconBytes())) {
                                serverData0.setIconBytes(p_272704_.iconBytes());
                                runnable1.run();
                            }
                        });
                        this.pingStart = Util.getMillis();
                        $$5.send(new ServerboundPingRequestPacket(this.pingStart));
                        this.success = true;
                    }
                }

                @Override
                public void handlePongResponse(ClientboundPongResponsePacket p_105487_) {
                    long $$1 = this.pingStart;
                    long $$2 = Util.getMillis();
                    serverData0.ping = $$2 - $$1;
                    $$5.disconnect(Component.translatable("multiplayer.status.finished"));
                }

                @Override
                public void onDisconnect(Component p_105485_) {
                    if (!this.success) {
                        ServerStatusPinger.this.onPingFailed(p_105485_, serverData0);
                        ServerStatusPinger.this.pingLegacyServer($$4, serverData0);
                    }
                }

                @Override
                public boolean isAcceptingMessages() {
                    return $$5.isConnected();
                }
            });
            try {
                $$5.send(new ClientIntentionPacket($$2.getHost(), $$2.getPort(), ConnectionProtocol.STATUS));
                $$5.send(new ServerboundStatusRequestPacket());
            } catch (Throwable var8) {
                LOGGER.error("Failed to ping server {}", $$2, var8);
            }
        }
    }

    void onPingFailed(Component component0, ServerData serverData1) {
        LOGGER.error("Can't ping {}: {}", serverData1.ip, component0.getString());
        serverData1.motd = CANT_CONNECT_MESSAGE;
        serverData1.status = CommonComponents.EMPTY;
    }

    void pingLegacyServer(final InetSocketAddress inetSocketAddress0, final ServerData serverData1) {
        ((Bootstrap) ((Bootstrap) ((Bootstrap) new Bootstrap().group((EventLoopGroup) Connection.NETWORK_WORKER_GROUP.get())).handler(new ChannelInitializer<Channel>() {

            protected void initChannel(Channel p_105498_) {
                try {
                    p_105498_.config().setOption(ChannelOption.TCP_NODELAY, true);
                } catch (ChannelException var3) {
                }
                p_105498_.pipeline().addLast(new ChannelHandler[] { new SimpleChannelInboundHandler<ByteBuf>() {

                    public void channelActive(ChannelHandlerContext p_105506_) throws Exception {
                        super.channelActive(p_105506_);
                        ByteBuf $$1 = Unpooled.buffer();
                        try {
                            $$1.writeByte(254);
                            $$1.writeByte(1);
                            $$1.writeByte(250);
                            char[] $$2 = "MC|PingHost".toCharArray();
                            $$1.writeShort($$2.length);
                            for (char $$3 : $$2) {
                                $$1.writeChar($$3);
                            }
                            $$1.writeShort(7 + 2 * inetSocketAddress0.getHostName().length());
                            $$1.writeByte(127);
                            $$2 = inetSocketAddress0.getHostName().toCharArray();
                            $$1.writeShort($$2.length);
                            for (char $$4 : $$2) {
                                $$1.writeChar($$4);
                            }
                            $$1.writeInt(inetSocketAddress0.getPort());
                            p_105506_.channel().writeAndFlush($$1).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                        } finally {
                            $$1.release();
                        }
                    }

                    protected void channelRead0(ChannelHandlerContext p_105503_, ByteBuf p_105504_) {
                        short $$2 = p_105504_.readUnsignedByte();
                        if ($$2 == 255) {
                            String $$3 = new String(p_105504_.readBytes(p_105504_.readShort() * 2).array(), StandardCharsets.UTF_16BE);
                            String[] $$4 = (String[]) Iterables.toArray(ServerStatusPinger.SPLITTER.split($$3), String.class);
                            if ("§1".equals($$4[0])) {
                                int $$5 = Mth.getInt($$4[1], 0);
                                String $$6 = $$4[2];
                                String $$7 = $$4[3];
                                int $$8 = Mth.getInt($$4[4], -1);
                                int $$9 = Mth.getInt($$4[5], -1);
                                serverData1.protocol = -1;
                                serverData1.version = Component.literal($$6);
                                serverData1.motd = Component.literal($$7);
                                serverData1.status = ServerStatusPinger.formatPlayerCount($$8, $$9);
                                serverData1.players = new ServerStatus.Players($$9, $$8, List.of());
                            }
                        }
                        p_105503_.close();
                    }

                    public void exceptionCaught(ChannelHandlerContext p_105511_, Throwable p_105512_) {
                        p_105511_.close();
                    }
                } });
            }
        })).channel(NioSocketChannel.class)).connect(inetSocketAddress0.getAddress(), inetSocketAddress0.getPort());
    }

    static Component formatPlayerCount(int int0, int int1) {
        return Component.literal(Integer.toString(int0)).append(Component.literal("/").withStyle(ChatFormatting.DARK_GRAY)).append(Integer.toString(int1)).withStyle(ChatFormatting.GRAY);
    }

    public void tick() {
        synchronized (this.connections) {
            Iterator<Connection> $$0 = this.connections.iterator();
            while ($$0.hasNext()) {
                Connection $$1 = (Connection) $$0.next();
                if ($$1.isConnected()) {
                    $$1.tick();
                } else {
                    $$0.remove();
                    $$1.handleDisconnection();
                }
            }
        }
    }

    public void removeAll() {
        synchronized (this.connections) {
            Iterator<Connection> $$0 = this.connections.iterator();
            while ($$0.hasNext()) {
                Connection $$1 = (Connection) $$0.next();
                if ($$1.isConnected()) {
                    $$0.remove();
                    $$1.disconnect(Component.translatable("multiplayer.status.cancelled"));
                }
            }
        }
    }
}