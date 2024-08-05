package net.minecraft.client.multiplayer;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.exceptions.InsufficientPrivilegesException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;
import com.mojang.authlib.exceptions.UserBannedException;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.logging.LogUtils;
import java.math.BigInteger;
import java.security.PublicKey;
import java.time.Duration;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.Connection;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.login.ClientLoginPacketListener;
import net.minecraft.network.protocol.login.ClientboundCustomQueryPacket;
import net.minecraft.network.protocol.login.ClientboundGameProfilePacket;
import net.minecraft.network.protocol.login.ClientboundHelloPacket;
import net.minecraft.network.protocol.login.ClientboundLoginCompressionPacket;
import net.minecraft.network.protocol.login.ClientboundLoginDisconnectPacket;
import net.minecraft.network.protocol.login.ServerboundCustomQueryPacket;
import net.minecraft.network.protocol.login.ServerboundKeyPacket;
import net.minecraft.realms.DisconnectedRealmsScreen;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.util.Crypt;
import net.minecraft.util.HttpUtil;
import org.slf4j.Logger;

public class ClientHandshakePacketListenerImpl implements ClientLoginPacketListener {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final Minecraft minecraft;

    @Nullable
    private final ServerData serverData;

    @Nullable
    private final Screen parent;

    private final Consumer<Component> updateStatus;

    private final Connection connection;

    private GameProfile localGameProfile;

    private final boolean newWorld;

    @Nullable
    private final Duration worldLoadDuration;

    @Nullable
    private String minigameName;

    public ClientHandshakePacketListenerImpl(Connection connection0, Minecraft minecraft1, @Nullable ServerData serverData2, @Nullable Screen screen3, boolean boolean4, @Nullable Duration duration5, Consumer<Component> consumerComponent6) {
        this.connection = connection0;
        this.minecraft = minecraft1;
        this.serverData = serverData2;
        this.parent = screen3;
        this.updateStatus = consumerComponent6;
        this.newWorld = boolean4;
        this.worldLoadDuration = duration5;
    }

    @Override
    public void handleHello(ClientboundHelloPacket clientboundHelloPacket0) {
        Cipher $$4;
        Cipher $$5;
        String $$3;
        ServerboundKeyPacket $$7;
        try {
            SecretKey $$1 = Crypt.generateSecretKey();
            PublicKey $$2 = clientboundHelloPacket0.getPublicKey();
            $$3 = new BigInteger(Crypt.digestData(clientboundHelloPacket0.getServerId(), $$2, $$1)).toString(16);
            $$4 = Crypt.getCipher(2, $$1);
            $$5 = Crypt.getCipher(1, $$1);
            byte[] $$6 = clientboundHelloPacket0.getChallenge();
            $$7 = new ServerboundKeyPacket($$1, $$2, $$6);
        } catch (Exception var9) {
            throw new IllegalStateException("Protocol error", var9);
        }
        this.updateStatus.accept(Component.translatable("connect.authorizing"));
        HttpUtil.DOWNLOAD_EXECUTOR.submit(() -> {
            Component $$4x = this.authenticateServer($$3);
            if ($$4x != null) {
                if (this.serverData == null || !this.serverData.isLan()) {
                    this.connection.disconnect($$4x);
                    return;
                }
                LOGGER.warn($$4x.getString());
            }
            this.updateStatus.accept(Component.translatable("connect.encrypting"));
            this.connection.send($$7, PacketSendListener.thenRun(() -> this.connection.setEncryptionKey($$4, $$5)));
        });
    }

    @Nullable
    private Component authenticateServer(String string0) {
        try {
            this.getMinecraftSessionService().joinServer(this.minecraft.getUser().getGameProfile(), this.minecraft.getUser().getAccessToken(), string0);
            return null;
        } catch (AuthenticationUnavailableException var3) {
            return Component.translatable("disconnect.loginFailedInfo", Component.translatable("disconnect.loginFailedInfo.serversUnavailable"));
        } catch (InvalidCredentialsException var4) {
            return Component.translatable("disconnect.loginFailedInfo", Component.translatable("disconnect.loginFailedInfo.invalidSession"));
        } catch (InsufficientPrivilegesException var5) {
            return Component.translatable("disconnect.loginFailedInfo", Component.translatable("disconnect.loginFailedInfo.insufficientPrivileges"));
        } catch (UserBannedException var6) {
            return Component.translatable("disconnect.loginFailedInfo", Component.translatable("disconnect.loginFailedInfo.userBanned"));
        } catch (AuthenticationException var7) {
            return Component.translatable("disconnect.loginFailedInfo", var7.getMessage());
        }
    }

    private MinecraftSessionService getMinecraftSessionService() {
        return this.minecraft.getMinecraftSessionService();
    }

    @Override
    public void handleGameProfile(ClientboundGameProfilePacket clientboundGameProfilePacket0) {
        this.updateStatus.accept(Component.translatable("connect.joining"));
        this.localGameProfile = clientboundGameProfilePacket0.getGameProfile();
        this.connection.setProtocol(ConnectionProtocol.PLAY);
        this.connection.setListener(new ClientPacketListener(this.minecraft, this.parent, this.connection, this.serverData, this.localGameProfile, this.minecraft.getTelemetryManager().createWorldSessionManager(this.newWorld, this.worldLoadDuration, this.minigameName)));
    }

    @Override
    public void onDisconnect(Component component0) {
        if (this.parent != null && this.parent instanceof RealmsScreen) {
            this.minecraft.setScreen(new DisconnectedRealmsScreen(this.parent, CommonComponents.CONNECT_FAILED, component0));
        } else {
            this.minecraft.setScreen(new DisconnectedScreen(this.parent, CommonComponents.CONNECT_FAILED, component0));
        }
    }

    @Override
    public boolean isAcceptingMessages() {
        return this.connection.isConnected();
    }

    @Override
    public void handleDisconnect(ClientboundLoginDisconnectPacket clientboundLoginDisconnectPacket0) {
        this.connection.disconnect(clientboundLoginDisconnectPacket0.getReason());
    }

    @Override
    public void handleCompression(ClientboundLoginCompressionPacket clientboundLoginCompressionPacket0) {
        if (!this.connection.isMemoryConnection()) {
            this.connection.setupCompression(clientboundLoginCompressionPacket0.getCompressionThreshold(), false);
        }
    }

    @Override
    public void handleCustomQuery(ClientboundCustomQueryPacket clientboundCustomQueryPacket0) {
        this.updateStatus.accept(Component.translatable("connect.negotiating"));
        this.connection.send(new ServerboundCustomQueryPacket(clientboundCustomQueryPacket0.getTransactionId(), null));
    }

    public void setMinigameName(String string0) {
        this.minigameName = string0;
    }
}