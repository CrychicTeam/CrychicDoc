package dev.architectury.networking.transformers;

import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.networking.NetworkManager;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Experimental;

@Experimental
public class SplitPacketTransformer implements PacketTransformer {

    private static final Logger LOGGER = LogManager.getLogger(SplitPacketTransformer.class);

    private static final byte START = 0;

    private static final byte PART = 1;

    private static final byte END = 2;

    private static final byte ONLY = 3;

    private final Map<SplitPacketTransformer.PartKey, SplitPacketTransformer.PartData> cache = Collections.synchronizedMap(new HashMap());

    public SplitPacketTransformer() {
        PlayerEvent.PLAYER_QUIT.register(player -> this.cache.keySet().removeIf(key -> Objects.equals(key.playerUUID, player.m_20148_())));
        EnvExecutor.runInEnv(Env.CLIENT, () -> new SplitPacketTransformer.Client()::init);
    }

    @Override
    public void inbound(NetworkManager.Side side, ResourceLocation id, FriendlyByteBuf buf, NetworkManager.PacketContext context, PacketTransformer.TransformationSink sink) {
        SplitPacketTransformer.PartKey key = side == NetworkManager.Side.S2C ? new SplitPacketTransformer.PartKey(side, null) : new SplitPacketTransformer.PartKey(side, context.getPlayer().m_20148_());
        switch(buf.readByte()) {
            case 0:
                SplitPacketTransformer.PartData data = new SplitPacketTransformer.PartData(id, buf.readInt());
                if (this.cache.put(key, data) != null) {
                    LOGGER.warn("Received invalid START packet for SplitPacketTransformer with packet id " + id + " for side " + side);
                }
                buf.retain();
                data.parts.add(buf);
                break;
            case 1:
                SplitPacketTransformer.PartData datax;
                if ((datax = (SplitPacketTransformer.PartData) this.cache.get(key)) == null) {
                    LOGGER.warn("Received invalid PART packet for SplitPacketTransformer with packet id " + id + " for side " + side);
                    buf.release();
                } else if (!datax.id.equals(id)) {
                    LOGGER.warn("Received invalid PART packet for SplitPacketTransformer with packet id " + id + " for side " + side + ", id in cache is " + datax.id);
                    buf.release();
                    for (FriendlyByteBuf partxx : datax.parts) {
                        if (partxx != buf) {
                            partxx.release();
                        }
                    }
                    this.cache.remove(key);
                } else {
                    buf.retain();
                    datax.parts.add(buf);
                }
                break;
            case 2:
                SplitPacketTransformer.PartData data;
                if ((data = (SplitPacketTransformer.PartData) this.cache.get(key)) == null) {
                    LOGGER.warn("Received invalid END packet for SplitPacketTransformer with packet id " + id + " for side " + side);
                    buf.release();
                } else if (data.id.equals(id)) {
                    buf.retain();
                    data.parts.add(buf);
                } else {
                    LOGGER.warn("Received invalid END packet for SplitPacketTransformer with packet id " + id + " for side " + side + ", id in cache is " + data.id);
                    buf.release();
                    for (FriendlyByteBuf part : data.parts) {
                        if (part != buf) {
                            part.release();
                        }
                    }
                    this.cache.remove(key);
                }
                if (data.parts.size() != data.partsExpected) {
                    LOGGER.warn("Received invalid END packet for SplitPacketTransformer with packet id " + id + " for side " + side + " with size " + data.parts + ", parts expected is " + data.partsExpected);
                    for (FriendlyByteBuf partx : data.parts) {
                        if (partx != buf) {
                            partx.release();
                        }
                    }
                } else {
                    FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.wrappedBuffer((ByteBuf[]) data.parts.toArray(new ByteBuf[0])));
                    sink.accept(side, data.id, byteBuf);
                    byteBuf.release();
                }
                this.cache.remove(key);
                break;
            case 3:
                sink.accept(side, id, buf);
                break;
            default:
                throw new IllegalStateException("Illegal split packet header!");
        }
    }

    @Override
    public void outbound(NetworkManager.Side side, ResourceLocation id, FriendlyByteBuf buf, PacketTransformer.TransformationSink sink) {
        int maxSize = (side == NetworkManager.Side.C2S ? 32767 : 1048576) - 1 - 20 - id.toString().getBytes(StandardCharsets.UTF_8).length;
        if (buf.readableBytes() <= maxSize) {
            ByteBuf stateBuf = Unpooled.buffer(1);
            stateBuf.writeByte(3);
            FriendlyByteBuf packetBuffer = new FriendlyByteBuf(Unpooled.wrappedBuffer(new ByteBuf[] { stateBuf, buf }));
            sink.accept(side, id, packetBuffer);
        } else {
            int partSize = maxSize - 4;
            int parts = (int) Math.ceil((double) ((float) buf.readableBytes() / (float) partSize));
            for (int i = 0; i < parts; i++) {
                FriendlyByteBuf packetBuffer = new FriendlyByteBuf(Unpooled.buffer());
                if (i == 0) {
                    packetBuffer.writeByte(0);
                    packetBuffer.writeInt(parts);
                } else if (i == parts - 1) {
                    packetBuffer.writeByte(2);
                } else {
                    packetBuffer.writeByte(1);
                }
                int next = Math.min(buf.readableBytes(), partSize);
                packetBuffer.writeBytes(buf.retainedSlice(buf.readerIndex(), next));
                buf.skipBytes(next);
                sink.accept(side, id, packetBuffer);
            }
            buf.release();
        }
    }

    private class Client {

        @OnlyIn(Dist.CLIENT)
        private void init() {
            ClientPlayerEvent.CLIENT_PLAYER_QUIT.register(player -> SplitPacketTransformer.this.cache.keySet().removeIf(key -> key.side == NetworkManager.Side.S2C));
        }
    }

    private static class PartData {

        private final ResourceLocation id;

        private final int partsExpected;

        private final List<FriendlyByteBuf> parts;

        public PartData(ResourceLocation id, int partsExpected) {
            this.id = id;
            this.partsExpected = partsExpected;
            this.parts = new ArrayList();
        }
    }

    private static class PartKey {

        private final NetworkManager.Side side;

        @Nullable
        private final UUID playerUUID;

        public PartKey(NetworkManager.Side side, @Nullable UUID playerUUID) {
            this.side = side;
            this.playerUUID = playerUUID;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else {
                return !(o instanceof SplitPacketTransformer.PartKey key) ? false : this.side == key.side && Objects.equals(this.playerUUID, key.playerUUID);
            }
        }

        public int hashCode() {
            return Objects.hash(new Object[] { this.side, this.playerUUID });
        }

        public String toString() {
            return "PartKey{side=" + this.side + ", playerUUID=" + this.playerUUID + "}";
        }
    }
}