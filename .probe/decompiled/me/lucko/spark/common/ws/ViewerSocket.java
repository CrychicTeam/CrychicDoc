package me.lucko.spark.common.ws;

import java.security.PublicKey;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import me.lucko.spark.common.SparkPlatform;
import me.lucko.spark.common.sampler.AbstractSampler;
import me.lucko.spark.common.sampler.Sampler;
import me.lucko.spark.lib.bytesocks.BytesocksClient;
import me.lucko.spark.lib.protobuf.ByteString;
import me.lucko.spark.proto.SparkProtos;
import me.lucko.spark.proto.SparkSamplerProtos;
import me.lucko.spark.proto.SparkWebSocketProtos;

public class ViewerSocket implements ViewerSocketConnection.Listener, AutoCloseable {

    private static final long SOCKET_INITIAL_TIMEOUT = TimeUnit.SECONDS.toMillis(60L);

    private static final long SOCKET_ESTABLISHED_TIMEOUT = TimeUnit.SECONDS.toMillis(30L);

    private final SparkPlatform platform;

    private final Sampler.ExportProps exportProps;

    private final ViewerSocketConnection socket;

    private boolean closed = false;

    private final long socketOpenTime = System.currentTimeMillis();

    private long lastPing = 0L;

    private String lastPayloadId = null;

    public ViewerSocket(SparkPlatform platform, BytesocksClient client, Sampler.ExportProps exportProps) throws Exception {
        this.platform = platform;
        this.exportProps = exportProps;
        this.socket = new ViewerSocketConnection(platform, client, this);
    }

    private void log(String message) {
        this.platform.getPlugin().log(Level.INFO, "[Viewer - " + this.socket.getChannelId() + "] " + message);
    }

    public SparkSamplerProtos.SocketChannelInfo getPayload() {
        return SparkSamplerProtos.SocketChannelInfo.newBuilder().setChannelId(this.socket.getChannelId()).setPublicKey(ByteString.copyFrom(this.platform.getTrustedKeyStore().getLocalPublicKey().getEncoded())).build();
    }

    public boolean isOpen() {
        return !this.closed && this.socket.isOpen();
    }

    public void processWindowRotate(AbstractSampler sampler) {
        if (!this.closed) {
            long time = System.currentTimeMillis();
            if (time - this.socketOpenTime > SOCKET_INITIAL_TIMEOUT && time - this.lastPing > SOCKET_ESTABLISHED_TIMEOUT) {
                this.log("No clients have pinged for 30s, closing socket");
                this.close();
            } else if (this.lastPing != 0L) {
                try {
                    SparkSamplerProtos.SamplerData samplerData = sampler.toProto(this.platform, this.exportProps);
                    String key = this.platform.getBytebinClient().postContent(samplerData, "application/x-spark-sampler", "live").key();
                    this.sendUpdatedSamplerData(key);
                } catch (Exception var6) {
                    this.platform.getPlugin().log(Level.WARNING, "Error whilst sending updated sampler data to the socket");
                    var6.printStackTrace();
                }
            }
        }
    }

    public void processSamplerStopped(AbstractSampler sampler) {
        if (!this.closed) {
            this.close();
        }
    }

    public void close() {
        this.socket.sendPacket(builder -> builder.setServerPong(SparkWebSocketProtos.ServerPong.newBuilder().setOk(false).build()));
        this.socket.close();
        this.closed = true;
    }

    @Override
    public boolean isKeyTrusted(PublicKey publicKey) {
        return this.platform.getTrustedKeyStore().isKeyTrusted(publicKey);
    }

    public void sendClientTrustedMessage(String clientId) {
        this.socket.sendPacket(builder -> builder.setServerConnectResponse(SparkWebSocketProtos.ServerConnectResponse.newBuilder().setClientId(clientId).setState(SparkWebSocketProtos.ServerConnectResponse.State.ACCEPTED).build()));
    }

    public void sendUpdatedSamplerData(String payloadId) {
        this.socket.sendPacket(builder -> builder.setServerUpdateSampler(SparkWebSocketProtos.ServerUpdateSamplerData.newBuilder().setPayloadId(payloadId).build()));
        this.lastPayloadId = payloadId;
    }

    public void sendUpdatedStatistics(SparkProtos.PlatformStatistics platform, SparkProtos.SystemStatistics system) {
        this.socket.sendPacket(builder -> builder.setServerUpdateStatistics(SparkWebSocketProtos.ServerUpdateStatistics.newBuilder().setPlatform(platform).setSystem(system).build()));
    }

    @Override
    public void onPacket(SparkWebSocketProtos.PacketWrapper packet, boolean verified, PublicKey publicKey) throws Exception {
        switch(packet.getPacketCase()) {
            case CLIENT_PING:
                this.onClientPing(packet.getClientPing(), publicKey);
                break;
            case CLIENT_CONNECT:
                this.onClientConnect(packet.getClientConnect(), verified, publicKey);
                break;
            default:
                throw new IllegalArgumentException("Unexpected packet: " + packet.getPacketCase());
        }
    }

    private void onClientPing(SparkWebSocketProtos.ClientPing packet, PublicKey publicKey) {
        this.lastPing = System.currentTimeMillis();
        this.socket.sendPacket(builder -> builder.setServerPong(SparkWebSocketProtos.ServerPong.newBuilder().setOk(!this.closed).setData(packet.getData()).build()));
    }

    private void onClientConnect(SparkWebSocketProtos.ClientConnect packet, boolean verified, PublicKey publicKey) {
        if (publicKey == null) {
            throw new IllegalStateException("Missing public key");
        } else {
            this.lastPing = System.currentTimeMillis();
            String clientId = packet.getClientId();
            this.log("Client connected: clientId=" + clientId + ", keyhash=" + hashPublicKey(publicKey) + ", desc=" + packet.getDescription());
            SparkWebSocketProtos.ServerConnectResponse.Builder resp = SparkWebSocketProtos.ServerConnectResponse.newBuilder().setClientId(clientId).setSettings(SparkWebSocketProtos.ServerConnectResponse.Settings.newBuilder().setSamplerInterval(60).setStatisticsInterval(10).build());
            if (this.lastPayloadId != null) {
                resp.setLastPayloadId(this.lastPayloadId);
            }
            if (this.closed) {
                resp.setState(SparkWebSocketProtos.ServerConnectResponse.State.REJECTED);
            } else if (verified) {
                resp.setState(SparkWebSocketProtos.ServerConnectResponse.State.ACCEPTED);
            } else {
                resp.setState(SparkWebSocketProtos.ServerConnectResponse.State.UNTRUSTED);
                this.platform.getTrustedKeyStore().addPendingKey(clientId, publicKey);
            }
            this.socket.sendPacket(builder -> builder.setServerConnectResponse(resp.build()));
        }
    }

    private static String hashPublicKey(PublicKey publicKey) {
        return publicKey == null ? "null" : Integer.toHexString(publicKey.hashCode());
    }
}