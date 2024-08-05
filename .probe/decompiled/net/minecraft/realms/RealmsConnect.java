package net.minecraft.realms;

import com.mojang.logging.LogUtils;
import com.mojang.realmsclient.dto.RealmsServer;
import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import net.minecraft.client.multiplayer.chat.report.ReportEnvironment;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.client.quickplay.QuickPlayLog;
import net.minecraft.network.Connection;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.handshake.ClientIntentionPacket;
import net.minecraft.network.protocol.login.ServerboundHelloPacket;
import org.slf4j.Logger;

public class RealmsConnect {

    static final Logger LOGGER = LogUtils.getLogger();

    final Screen onlineScreen;

    volatile boolean aborted;

    @Nullable
    Connection connection;

    public RealmsConnect(Screen screen0) {
        this.onlineScreen = screen0;
    }

    public void connect(final RealmsServer realmsServer0, ServerAddress serverAddress1) {
        final Minecraft $$2 = Minecraft.getInstance();
        $$2.setConnectedToRealms(true);
        $$2.prepareForMultiplayer();
        $$2.getNarrator().sayNow(Component.translatable("mco.connect.success"));
        final String $$3 = serverAddress1.getHost();
        final int $$4 = serverAddress1.getPort();
        (new Thread("Realms-connect-task") {

            public void run() {
                InetSocketAddress $$0 = null;
                try {
                    $$0 = new InetSocketAddress($$3, $$4);
                    if (RealmsConnect.this.aborted) {
                        return;
                    }
                    RealmsConnect.this.connection = Connection.connectToServer($$0, $$2.options.useNativeTransport());
                    if (RealmsConnect.this.aborted) {
                        return;
                    }
                    ClientHandshakePacketListenerImpl $$1 = new ClientHandshakePacketListenerImpl(RealmsConnect.this.connection, $$2, realmsServer0.toServerData($$3), RealmsConnect.this.onlineScreen, false, null, p_120726_ -> {
                    });
                    if (realmsServer0.worldType == RealmsServer.WorldType.MINIGAME) {
                        $$1.setMinigameName(realmsServer0.minigameName);
                    }
                    RealmsConnect.this.connection.setListener($$1);
                    if (RealmsConnect.this.aborted) {
                        return;
                    }
                    RealmsConnect.this.connection.send(new ClientIntentionPacket($$3, $$4, ConnectionProtocol.LOGIN));
                    if (RealmsConnect.this.aborted) {
                        return;
                    }
                    String $$2 = $$2.getUser().getName();
                    UUID $$3 = $$2.getUser().getProfileId();
                    RealmsConnect.this.connection.send(new ServerboundHelloPacket($$2, Optional.ofNullable($$3)));
                    $$2.updateReportEnvironment(ReportEnvironment.realm(realmsServer0));
                    $$2.quickPlayLog().setWorldData(QuickPlayLog.Type.REALMS, String.valueOf(realmsServer0.id), realmsServer0.name);
                } catch (Exception var5) {
                    $$2.getDownloadedPackSource().clearServerPack();
                    if (RealmsConnect.this.aborted) {
                        return;
                    }
                    RealmsConnect.LOGGER.error("Couldn't connect to world", var5);
                    String $$5 = var5.toString();
                    if ($$0 != null) {
                        String $$6 = $$0 + ":" + $$4;
                        $$5 = $$5.replaceAll($$6, "");
                    }
                    DisconnectedRealmsScreen $$7 = new DisconnectedRealmsScreen(RealmsConnect.this.onlineScreen, CommonComponents.CONNECT_FAILED, Component.translatable("disconnect.genericReason", $$5));
                    $$2.execute(() -> $$2.setScreen($$7));
                }
            }
        }).start();
    }

    public void abort() {
        this.aborted = true;
        if (this.connection != null && this.connection.isConnected()) {
            this.connection.disconnect(Component.translatable("disconnect.genericReason"));
            this.connection.handleDisconnection();
        }
    }

    public void tick() {
        if (this.connection != null) {
            if (this.connection.isConnected()) {
                this.connection.tick();
            } else {
                this.connection.handleDisconnection();
            }
        }
    }
}