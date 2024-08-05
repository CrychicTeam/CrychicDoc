package me.lucko.spark.common.ws;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;
import java.util.function.Consumer;
import java.util.logging.Level;
import me.lucko.spark.common.SparkPlatform;
import me.lucko.spark.lib.bytesocks.BytesocksClient;
import me.lucko.spark.lib.protobuf.ByteString;
import me.lucko.spark.proto.SparkWebSocketProtos;

public class ViewerSocketConnection implements BytesocksClient.Listener, AutoCloseable {

    public static final int VERSION_1 = 1;

    public static final CryptoAlgorithm CRYPTO = CryptoAlgorithm.RSA2048;

    private final SparkPlatform platform;

    private final ViewerSocketConnection.Listener listener;

    private final PrivateKey privateKey;

    private final BytesocksClient.Socket socket;

    public ViewerSocketConnection(SparkPlatform platform, BytesocksClient client, ViewerSocketConnection.Listener listener) throws Exception {
        this.platform = platform;
        this.listener = listener;
        this.privateKey = platform.getTrustedKeyStore().getLocalPrivateKey();
        this.socket = client.createAndConnect(this);
    }

    public String getChannelId() {
        return this.socket.channelId();
    }

    public boolean isOpen() {
        return this.socket.isOpen();
    }

    @Override
    public void onText(String data) {
        try {
            SparkWebSocketProtos.RawPacket packet = this.decodeRawPacket(data);
            this.handleRawPacket(packet);
        } catch (Exception var3) {
            this.platform.getPlugin().log(Level.WARNING, "Exception occurred while reading data from the socket");
            var3.printStackTrace();
        }
    }

    @Override
    public void onError(Throwable error) {
        this.platform.getPlugin().log(Level.INFO, "Socket error: " + error.getClass().getName() + " " + error.getMessage());
        error.printStackTrace();
    }

    @Override
    public void onClose(int statusCode, String reason) {
    }

    public void sendPacket(Consumer<SparkWebSocketProtos.PacketWrapper.Builder> packetBuilder) {
        SparkWebSocketProtos.PacketWrapper.Builder builder = SparkWebSocketProtos.PacketWrapper.newBuilder();
        packetBuilder.accept(builder);
        SparkWebSocketProtos.PacketWrapper wrapper = builder.build();
        try {
            this.sendPacket(wrapper);
        } catch (Exception var5) {
            this.platform.getPlugin().log(Level.WARNING, "Exception occurred while sending data to the socket");
            var5.printStackTrace();
        }
    }

    private void sendPacket(SparkWebSocketProtos.PacketWrapper packet) throws Exception {
        ByteString msg = packet.toByteString();
        Signature sign = CRYPTO.createSignature();
        sign.initSign(this.privateKey);
        sign.update(msg.asReadOnlyByteBuffer());
        byte[] signature = sign.sign();
        this.sendRawPacket(SparkWebSocketProtos.RawPacket.newBuilder().setVersion(1).setSignature(ByteString.copyFrom(signature)).setMessage(msg).build());
    }

    private void sendRawPacket(SparkWebSocketProtos.RawPacket packet) throws IOException {
        byte[] buf = packet.toByteArray();
        String encoded = Base64.getEncoder().encodeToString(buf);
        this.socket.send(encoded);
    }

    private SparkWebSocketProtos.RawPacket decodeRawPacket(String data) throws IOException {
        byte[] buf = Base64.getDecoder().decode(data);
        return SparkWebSocketProtos.RawPacket.parseFrom(buf);
    }

    private void handleRawPacket(SparkWebSocketProtos.RawPacket packet) throws Exception {
        int version = packet.getVersion();
        if (version != 1) {
            throw new IllegalArgumentException("Unsupported packet version " + version);
        } else {
            ByteString message = packet.getMessage();
            PublicKey publicKey = CRYPTO.decodePublicKey(packet.getPublicKey());
            ByteString signature = packet.getSignature();
            boolean verified = false;
            if (signature != null && publicKey != null && this.listener.isKeyTrusted(publicKey)) {
                Signature sign = CRYPTO.createSignature();
                sign.initVerify(publicKey);
                sign.update(message.asReadOnlyByteBuffer());
                verified = sign.verify(signature.toByteArray());
            }
            SparkWebSocketProtos.PacketWrapper wrapper = SparkWebSocketProtos.PacketWrapper.parseFrom(message);
            this.listener.onPacket(wrapper, verified, publicKey);
        }
    }

    public void close() {
        this.socket.close(1001, "spark plugin disconnected");
    }

    public interface Listener {

        boolean isKeyTrusted(PublicKey var1);

        void onPacket(SparkWebSocketProtos.PacketWrapper var1, boolean var2, PublicKey var3) throws Exception;
    }
}