package net.minecraft.client.gui.screens;

import com.mojang.logging.LogUtils;
import io.netty.channel.ChannelFuture;
import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;
import net.minecraft.DefaultUncaughtExceptionHandler;
import net.minecraft.Util;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.chat.report.ReportEnvironment;
import net.minecraft.client.multiplayer.resolver.ResolvedServerAddress;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.client.multiplayer.resolver.ServerNameResolver;
import net.minecraft.client.quickplay.QuickPlay;
import net.minecraft.client.quickplay.QuickPlayLog;
import net.minecraft.network.Connection;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.handshake.ClientIntentionPacket;
import net.minecraft.network.protocol.login.ServerboundHelloPacket;
import org.slf4j.Logger;

public class ConnectScreen extends Screen {

    private static final AtomicInteger UNIQUE_THREAD_ID = new AtomicInteger(0);

    static final Logger LOGGER = LogUtils.getLogger();

    private static final long NARRATION_DELAY_MS = 2000L;

    static final Component ABORT_CONNECTION = Component.translatable("connect.aborted");

    public static final Component UNKNOWN_HOST_MESSAGE = Component.translatable("disconnect.genericReason", Component.translatable("disconnect.unknownHost"));

    @Nullable
    volatile Connection connection;

    @Nullable
    ChannelFuture channelFuture;

    volatile boolean aborted;

    final Screen parent;

    private Component status = Component.translatable("connect.connecting");

    private long lastNarration = -1L;

    final Component connectFailedTitle;

    private ConnectScreen(Screen screen0, Component component1) {
        super(GameNarrator.NO_TITLE);
        this.parent = screen0;
        this.connectFailedTitle = component1;
    }

    public static void startConnecting(Screen screen0, Minecraft minecraft1, ServerAddress serverAddress2, ServerData serverData3, boolean boolean4) {
        if (minecraft1.screen instanceof ConnectScreen) {
            LOGGER.error("Attempt to connect while already connecting");
        } else {
            ConnectScreen $$5 = new ConnectScreen(screen0, boolean4 ? QuickPlay.ERROR_TITLE : CommonComponents.CONNECT_FAILED);
            minecraft1.clearLevel();
            minecraft1.prepareForMultiplayer();
            minecraft1.updateReportEnvironment(ReportEnvironment.thirdParty(serverData3 != null ? serverData3.ip : serverAddress2.getHost()));
            minecraft1.quickPlayLog().setWorldData(QuickPlayLog.Type.MULTIPLAYER, serverData3.ip, serverData3.name);
            minecraft1.setScreen($$5);
            $$5.connect(minecraft1, serverAddress2, serverData3);
        }
    }

    private void connect(final Minecraft minecraft0, final ServerAddress serverAddress1, @Nullable final ServerData serverData2) {
        LOGGER.info("Connecting to {}, {}", serverAddress1.getHost(), serverAddress1.getPort());
        Thread $$3 = new Thread("Server Connector #" + UNIQUE_THREAD_ID.incrementAndGet()) {

            public void run() {
                InetSocketAddress $$0 = null;
                try {
                    if (ConnectScreen.this.aborted) {
                        return;
                    }
                    Optional<InetSocketAddress> $$1 = ServerNameResolver.DEFAULT.resolveAddress(serverAddress1).map(ResolvedServerAddress::m_142641_);
                    if (ConnectScreen.this.aborted) {
                        return;
                    }
                    if (!$$1.isPresent()) {
                        minecraft0.execute(() -> minecraft0.setScreen(new DisconnectedScreen(ConnectScreen.this.parent, ConnectScreen.this.connectFailedTitle, ConnectScreen.UNKNOWN_HOST_MESSAGE)));
                        return;
                    }
                    $$0 = (InetSocketAddress) $$1.get();
                    Connection $$2;
                    synchronized (ConnectScreen.this) {
                        if (ConnectScreen.this.aborted) {
                            return;
                        }
                        $$2 = new Connection(PacketFlow.CLIENTBOUND);
                        ConnectScreen.this.channelFuture = Connection.connect($$0, minecraft0.options.useNativeTransport(), $$2);
                    }
                    ConnectScreen.this.channelFuture.syncUninterruptibly();
                    synchronized (ConnectScreen.this) {
                        if (ConnectScreen.this.aborted) {
                            $$2.disconnect(ConnectScreen.ABORT_CONNECTION);
                            return;
                        }
                        ConnectScreen.this.connection = $$2;
                    }
                    ConnectScreen.this.connection.setListener(new ClientHandshakePacketListenerImpl(ConnectScreen.this.connection, minecraft0, serverData2, ConnectScreen.this.parent, false, null, ConnectScreen.this::m_95717_));
                    ConnectScreen.this.connection.send(new ClientIntentionPacket($$0.getHostName(), $$0.getPort(), ConnectionProtocol.LOGIN));
                    ConnectScreen.this.connection.send(new ServerboundHelloPacket(minecraft0.getUser().getName(), Optional.ofNullable(minecraft0.getUser().getProfileId())));
                } catch (Exception var9) {
                    if (ConnectScreen.this.aborted) {
                        return;
                    }
                    Exception $$6;
                    if (var9.getCause() instanceof Exception $$5) {
                        $$6 = $$5;
                    } else {
                        $$6 = var9;
                    }
                    ConnectScreen.LOGGER.error("Couldn't connect to server", var9);
                    String $$8 = $$0 == null ? $$6.getMessage() : $$6.getMessage().replaceAll($$0.getHostName() + ":" + $$0.getPort(), "").replaceAll($$0.toString(), "");
                    minecraft0.execute(() -> minecraft0.setScreen(new DisconnectedScreen(ConnectScreen.this.parent, ConnectScreen.this.connectFailedTitle, Component.translatable("disconnect.genericReason", $$8))));
                }
            }
        };
        $$3.setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(LOGGER));
        $$3.start();
    }

    private void updateStatus(Component component0) {
        this.status = component0;
    }

    @Override
    public void tick() {
        if (this.connection != null) {
            if (this.connection.isConnected()) {
                this.connection.tick();
            } else {
                this.connection.handleDisconnection();
            }
        }
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    protected void init() {
        this.m_142416_(Button.builder(CommonComponents.GUI_CANCEL, p_289624_ -> {
            synchronized (this) {
                this.aborted = true;
                if (this.channelFuture != null) {
                    this.channelFuture.cancel(true);
                    this.channelFuture = null;
                }
                if (this.connection != null) {
                    this.connection.disconnect(ABORT_CONNECTION);
                }
            }
            this.f_96541_.setScreen(this.parent);
        }).bounds(this.f_96543_ / 2 - 100, this.f_96544_ / 4 + 120 + 12, 200, 20).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        long $$4 = Util.getMillis();
        if ($$4 - this.lastNarration > 2000L) {
            this.lastNarration = $$4;
            this.f_96541_.getNarrator().sayNow(Component.translatable("narrator.joining"));
        }
        guiGraphics0.drawCenteredString(this.f_96547_, this.status, this.f_96543_ / 2, this.f_96544_ / 2 - 50, 16777215);
        super.render(guiGraphics0, int1, int2, float3);
    }
}