package dev.shadowsoffire.placebo.reload;

import com.mojang.datafixers.util.Either;
import dev.shadowsoffire.placebo.Placebo;
import dev.shadowsoffire.placebo.codec.CodecProvider;
import dev.shadowsoffire.placebo.network.MessageHelper;
import dev.shadowsoffire.placebo.network.MessageProvider;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public abstract class ReloadListenerPacket<T extends ReloadListenerPacket<T>> {

    final String path;

    public ReloadListenerPacket(String path) {
        this.path = path;
    }

    public static class Content<V extends CodecProvider<? super V>> extends ReloadListenerPacket<ReloadListenerPacket.Content<V>> {

        final ResourceLocation key;

        final Either<V, FriendlyByteBuf> data;

        public Content(String path, ResourceLocation key, V item) {
            super(path);
            this.key = key;
            this.data = Either.left(item);
        }

        private Content(String path, ResourceLocation key, FriendlyByteBuf buf) {
            super(path);
            this.key = key;
            this.data = Either.right(buf);
        }

        private V readItem() {
            FriendlyByteBuf buf = (FriendlyByteBuf) this.data.right().get();
            CodecProvider ex;
            try {
                ex = DynamicRegistry.SyncManagement.readItem(this.path, buf);
            } catch (Exception var6) {
                Placebo.LOGGER.error("Failure when deserializing a dynamic registry object via network: Registry: {}, Object ID: {}", this.path, this.key);
                var6.printStackTrace();
                throw new RuntimeException(var6);
            } finally {
                buf.release();
            }
            return (V) ex;
        }

        public static class Provider<V extends CodecProvider<? super V>> implements MessageProvider<ReloadListenerPacket.Content<V>> {

            @Override
            public Class<ReloadListenerPacket.Content> getMsgClass() {
                return ReloadListenerPacket.Content.class;
            }

            public void write(ReloadListenerPacket.Content<V> msg, FriendlyByteBuf buf) {
                buf.writeUtf(msg.path, 50);
                buf.writeResourceLocation(msg.key);
                DynamicRegistry.SyncManagement.writeItem(msg.path, (V) msg.data.left().get(), buf);
            }

            public ReloadListenerPacket.Content<V> read(FriendlyByteBuf buf) {
                String path = buf.readUtf(50);
                ResourceLocation key = buf.readResourceLocation();
                return new ReloadListenerPacket.Content<>(path, key, new FriendlyByteBuf(buf.copy()));
            }

            public void handle(ReloadListenerPacket.Content<V> msg, Supplier<NetworkEvent.Context> ctx) {
                MessageHelper.handlePacket(() -> DynamicRegistry.SyncManagement.acceptItem(msg.path, msg.key, msg.readItem()), ctx);
            }
        }
    }

    public static class End extends ReloadListenerPacket<ReloadListenerPacket.End> {

        public End(String path) {
            super(path);
        }

        public static class Provider implements MessageProvider<ReloadListenerPacket.End> {

            @Override
            public Class<?> getMsgClass() {
                return ReloadListenerPacket.End.class;
            }

            public void write(ReloadListenerPacket.End msg, FriendlyByteBuf buf) {
                buf.writeUtf(msg.path, 50);
            }

            public ReloadListenerPacket.End read(FriendlyByteBuf buf) {
                return new ReloadListenerPacket.End(buf.readUtf(50));
            }

            public void handle(ReloadListenerPacket.End msg, Supplier<NetworkEvent.Context> ctx) {
                MessageHelper.handlePacket(() -> DynamicRegistry.SyncManagement.endSync(msg.path), ctx);
            }
        }
    }

    public static class Start extends ReloadListenerPacket<ReloadListenerPacket.Start> {

        public Start(String path) {
            super(path);
        }

        public static class Provider implements MessageProvider<ReloadListenerPacket.Start> {

            @Override
            public Class<ReloadListenerPacket.Start> getMsgClass() {
                return ReloadListenerPacket.Start.class;
            }

            public void write(ReloadListenerPacket.Start msg, FriendlyByteBuf buf) {
                buf.writeUtf(msg.path, 50);
            }

            public ReloadListenerPacket.Start read(FriendlyByteBuf buf) {
                return new ReloadListenerPacket.Start(buf.readUtf(50));
            }

            public void handle(ReloadListenerPacket.Start msg, Supplier<NetworkEvent.Context> ctx) {
                MessageHelper.handlePacket(() -> DynamicRegistry.SyncManagement.initSync(msg.path), ctx);
            }
        }
    }
}