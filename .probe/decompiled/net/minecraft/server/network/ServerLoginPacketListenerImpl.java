package net.minecraft.server.network;

import com.google.common.primitives.Ints;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.logging.LogUtils;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.security.PrivateKey;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import net.minecraft.DefaultUncaughtExceptionHandler;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.TickablePacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundDisconnectPacket;
import net.minecraft.network.protocol.login.ClientboundGameProfilePacket;
import net.minecraft.network.protocol.login.ClientboundHelloPacket;
import net.minecraft.network.protocol.login.ClientboundLoginCompressionPacket;
import net.minecraft.network.protocol.login.ClientboundLoginDisconnectPacket;
import net.minecraft.network.protocol.login.ServerLoginPacketListener;
import net.minecraft.network.protocol.login.ServerboundCustomQueryPacket;
import net.minecraft.network.protocol.login.ServerboundHelloPacket;
import net.minecraft.network.protocol.login.ServerboundKeyPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Crypt;
import net.minecraft.util.CryptException;
import net.minecraft.util.RandomSource;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;

public class ServerLoginPacketListenerImpl implements ServerLoginPacketListener, TickablePacketListener {

    private static final AtomicInteger UNIQUE_THREAD_ID = new AtomicInteger(0);

    static final Logger LOGGER = LogUtils.getLogger();

    private static final int MAX_TICKS_BEFORE_LOGIN = 600;

    private static final RandomSource RANDOM = RandomSource.create();

    private final byte[] challenge;

    final MinecraftServer server;

    final Connection connection;

    ServerLoginPacketListenerImpl.State state = ServerLoginPacketListenerImpl.State.HELLO;

    private int tick;

    @Nullable
    GameProfile gameProfile;

    private final String serverId = "";

    @Nullable
    private ServerPlayer delayedAcceptPlayer;

    public ServerLoginPacketListenerImpl(MinecraftServer minecraftServer0, Connection connection1) {
        this.server = minecraftServer0;
        this.connection = connection1;
        this.challenge = Ints.toByteArray(RANDOM.nextInt());
    }

    @Override
    public void tick() {
        if (this.state == ServerLoginPacketListenerImpl.State.READY_TO_ACCEPT) {
            this.handleAcceptedLogin();
        } else if (this.state == ServerLoginPacketListenerImpl.State.DELAY_ACCEPT) {
            ServerPlayer $$0 = this.server.getPlayerList().getPlayer(this.gameProfile.getId());
            if ($$0 == null) {
                this.state = ServerLoginPacketListenerImpl.State.READY_TO_ACCEPT;
                this.placeNewPlayer(this.delayedAcceptPlayer);
                this.delayedAcceptPlayer = null;
            }
        }
        if (this.tick++ == 600) {
            this.disconnect(Component.translatable("multiplayer.disconnect.slow_login"));
        }
    }

    @Override
    public boolean isAcceptingMessages() {
        return this.connection.isConnected();
    }

    public void disconnect(Component component0) {
        try {
            LOGGER.info("Disconnecting {}: {}", this.getUserName(), component0.getString());
            this.connection.send(new ClientboundLoginDisconnectPacket(component0));
            this.connection.disconnect(component0);
        } catch (Exception var3) {
            LOGGER.error("Error whilst disconnecting player", var3);
        }
    }

    public void handleAcceptedLogin() {
        if (!this.gameProfile.isComplete()) {
            this.gameProfile = this.createFakeProfile(this.gameProfile);
        }
        Component $$0 = this.server.getPlayerList().canPlayerLogin(this.connection.getRemoteAddress(), this.gameProfile);
        if ($$0 != null) {
            this.disconnect($$0);
        } else {
            this.state = ServerLoginPacketListenerImpl.State.ACCEPTED;
            if (this.server.getCompressionThreshold() >= 0 && !this.connection.isMemoryConnection()) {
                this.connection.send(new ClientboundLoginCompressionPacket(this.server.getCompressionThreshold()), PacketSendListener.thenRun(() -> this.connection.setupCompression(this.server.getCompressionThreshold(), true)));
            }
            this.connection.send(new ClientboundGameProfilePacket(this.gameProfile));
            ServerPlayer $$1 = this.server.getPlayerList().getPlayer(this.gameProfile.getId());
            try {
                ServerPlayer $$2 = this.server.getPlayerList().getPlayerForLogin(this.gameProfile);
                if ($$1 != null) {
                    this.state = ServerLoginPacketListenerImpl.State.DELAY_ACCEPT;
                    this.delayedAcceptPlayer = $$2;
                } else {
                    this.placeNewPlayer($$2);
                }
            } catch (Exception var5) {
                LOGGER.error("Couldn't place player in world", var5);
                Component $$4 = Component.translatable("multiplayer.disconnect.invalid_player_data");
                this.connection.send(new ClientboundDisconnectPacket($$4));
                this.connection.disconnect($$4);
            }
        }
    }

    private void placeNewPlayer(ServerPlayer serverPlayer0) {
        this.server.getPlayerList().placeNewPlayer(this.connection, serverPlayer0);
    }

    @Override
    public void onDisconnect(Component component0) {
        LOGGER.info("{} lost connection: {}", this.getUserName(), component0.getString());
    }

    public String getUserName() {
        return this.gameProfile != null ? this.gameProfile + " (" + this.connection.getRemoteAddress() + ")" : String.valueOf(this.connection.getRemoteAddress());
    }

    @Override
    public void handleHello(ServerboundHelloPacket serverboundHelloPacket0) {
        Validate.validState(this.state == ServerLoginPacketListenerImpl.State.HELLO, "Unexpected hello packet", new Object[0]);
        Validate.validState(isValidUsername(serverboundHelloPacket0.name()), "Invalid characters in username", new Object[0]);
        GameProfile $$1 = this.server.getSingleplayerProfile();
        if ($$1 != null && serverboundHelloPacket0.name().equalsIgnoreCase($$1.getName())) {
            this.gameProfile = $$1;
            this.state = ServerLoginPacketListenerImpl.State.READY_TO_ACCEPT;
        } else {
            this.gameProfile = new GameProfile(null, serverboundHelloPacket0.name());
            if (this.server.usesAuthentication() && !this.connection.isMemoryConnection()) {
                this.state = ServerLoginPacketListenerImpl.State.KEY;
                this.connection.send(new ClientboundHelloPacket("", this.server.getKeyPair().getPublic().getEncoded(), this.challenge));
            } else {
                this.state = ServerLoginPacketListenerImpl.State.READY_TO_ACCEPT;
            }
        }
    }

    public static boolean isValidUsername(String string0) {
        return string0.chars().filter(p_203791_ -> p_203791_ <= 32 || p_203791_ >= 127).findAny().isEmpty();
    }

    @Override
    public void handleKey(ServerboundKeyPacket serverboundKeyPacket0) {
        Validate.validState(this.state == ServerLoginPacketListenerImpl.State.KEY, "Unexpected key packet", new Object[0]);
        final String $$5;
        try {
            PrivateKey $$1 = this.server.getKeyPair().getPrivate();
            if (!serverboundKeyPacket0.isChallengeValid(this.challenge, $$1)) {
                throw new IllegalStateException("Protocol error");
            }
            SecretKey $$2 = serverboundKeyPacket0.getSecretKey($$1);
            Cipher $$3 = Crypt.getCipher(2, $$2);
            Cipher $$4 = Crypt.getCipher(1, $$2);
            $$5 = new BigInteger(Crypt.digestData("", this.server.getKeyPair().getPublic(), $$2)).toString(16);
            this.state = ServerLoginPacketListenerImpl.State.AUTHENTICATING;
            this.connection.setEncryptionKey($$3, $$4);
        } catch (CryptException var7) {
            throw new IllegalStateException("Protocol error", var7);
        }
        Thread $$8 = new Thread("User Authenticator #" + UNIQUE_THREAD_ID.incrementAndGet()) {

            public void run() {
                GameProfile $$0 = ServerLoginPacketListenerImpl.this.gameProfile;
                try {
                    ServerLoginPacketListenerImpl.this.gameProfile = ServerLoginPacketListenerImpl.this.server.getSessionService().hasJoinedServer(new GameProfile(null, $$0.getName()), $$5, this.getAddress());
                    if (ServerLoginPacketListenerImpl.this.gameProfile != null) {
                        ServerLoginPacketListenerImpl.LOGGER.info("UUID of player {} is {}", ServerLoginPacketListenerImpl.this.gameProfile.getName(), ServerLoginPacketListenerImpl.this.gameProfile.getId());
                        ServerLoginPacketListenerImpl.this.state = ServerLoginPacketListenerImpl.State.READY_TO_ACCEPT;
                    } else if (ServerLoginPacketListenerImpl.this.server.isSingleplayer()) {
                        ServerLoginPacketListenerImpl.LOGGER.warn("Failed to verify username but will let them in anyway!");
                        ServerLoginPacketListenerImpl.this.gameProfile = $$0;
                        ServerLoginPacketListenerImpl.this.state = ServerLoginPacketListenerImpl.State.READY_TO_ACCEPT;
                    } else {
                        ServerLoginPacketListenerImpl.this.disconnect(Component.translatable("multiplayer.disconnect.unverified_username"));
                        ServerLoginPacketListenerImpl.LOGGER.error("Username '{}' tried to join with an invalid session", $$0.getName());
                    }
                } catch (AuthenticationUnavailableException var3) {
                    if (ServerLoginPacketListenerImpl.this.server.isSingleplayer()) {
                        ServerLoginPacketListenerImpl.LOGGER.warn("Authentication servers are down but will let them in anyway!");
                        ServerLoginPacketListenerImpl.this.gameProfile = $$0;
                        ServerLoginPacketListenerImpl.this.state = ServerLoginPacketListenerImpl.State.READY_TO_ACCEPT;
                    } else {
                        ServerLoginPacketListenerImpl.this.disconnect(Component.translatable("multiplayer.disconnect.authservers_down"));
                        ServerLoginPacketListenerImpl.LOGGER.error("Couldn't verify username because servers are unavailable");
                    }
                }
            }

            @Nullable
            private InetAddress getAddress() {
                SocketAddress $$0 = ServerLoginPacketListenerImpl.this.connection.getRemoteAddress();
                return ServerLoginPacketListenerImpl.this.server.getPreventProxyConnections() && $$0 instanceof InetSocketAddress ? ((InetSocketAddress) $$0).getAddress() : null;
            }
        };
        $$8.setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(LOGGER));
        $$8.start();
    }

    @Override
    public void handleCustomQueryPacket(ServerboundCustomQueryPacket serverboundCustomQueryPacket0) {
        this.disconnect(Component.translatable("multiplayer.disconnect.unexpected_query_response"));
    }

    protected GameProfile createFakeProfile(GameProfile gameProfile0) {
        UUID $$1 = UUIDUtil.createOfflinePlayerUUID(gameProfile0.getName());
        return new GameProfile($$1, gameProfile0.getName());
    }

    static enum State {

        HELLO,
        KEY,
        AUTHENTICATING,
        NEGOTIATING,
        READY_TO_ACCEPT,
        DELAY_ACCEPT,
        ACCEPTED
    }
}