package com.mrcrayfish.configured.impl.framework.message;

import com.mrcrayfish.configured.impl.framework.handler.FrameworkClientHandler;
import com.mrcrayfish.configured.impl.framework.handler.FrameworkServerHandler;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class MessageFramework {

    public static record Request(ResourceLocation id) {

        public static final ResourceLocation ID = new ResourceLocation("configured", "request_framework_config");

        public static void encode(MessageFramework.Request message, FriendlyByteBuf buf) {
            buf.writeResourceLocation(message.id());
        }

        public static MessageFramework.Request decode(FriendlyByteBuf buf) {
            return new MessageFramework.Request(buf.readResourceLocation());
        }

        public static void handle(MessageFramework.Request message, Consumer<Runnable> executor, @Nullable Player player, Consumer<Component> disconnect) {
            if (player instanceof ServerPlayer serverPlayer) {
                executor.accept((Runnable) () -> FrameworkServerHandler.handleRequestConfig(serverPlayer, message, disconnect));
            }
        }
    }

    public static record Response(byte[] data) {

        public static final ResourceLocation ID = new ResourceLocation("configured", "response_framework_config");

        public static void encode(MessageFramework.Response message, FriendlyByteBuf buf) {
            buf.writeByteArray(message.data());
        }

        public static MessageFramework.Response decode(FriendlyByteBuf buf) {
            return new MessageFramework.Response(buf.readByteArray());
        }

        public static void handle(MessageFramework.Response message, Consumer<Runnable> executor, Consumer<Component> disconnect) {
            executor.accept((Runnable) () -> FrameworkClientHandler.handleResponse(message, disconnect));
        }
    }

    public static record Sync(ResourceLocation id, byte[] data) {

        public static final ResourceLocation ID = new ResourceLocation("configured", "sync_framework_config");

        public static void encode(MessageFramework.Sync message, FriendlyByteBuf buffer) {
            buffer.writeResourceLocation(message.id);
            buffer.writeByteArray(message.data);
        }

        public static MessageFramework.Sync decode(FriendlyByteBuf buffer) {
            return new MessageFramework.Sync(buffer.readResourceLocation(), buffer.readByteArray());
        }

        public static void handle(MessageFramework.Sync message, Consumer<Runnable> executor, @Nullable Player player, Consumer<Component> disconnect) {
            if (player instanceof ServerPlayer serverPlayer) {
                executor.accept((Runnable) () -> FrameworkServerHandler.handleServerSync(serverPlayer, message, disconnect));
            }
        }
    }
}