package com.mrcrayfish.configured.network.message;

import com.mrcrayfish.configured.network.ClientPlayHandler;
import java.util.function.Consumer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record MessageSessionData(boolean developer, boolean lan) {

    public static final ResourceLocation ID = new ResourceLocation("configured", "session_data");

    public static void encode(MessageSessionData message, FriendlyByteBuf buffer) {
        buffer.writeBoolean(message.developer);
        buffer.writeBoolean(message.lan);
    }

    public static MessageSessionData decode(FriendlyByteBuf buffer) {
        return new MessageSessionData(buffer.readBoolean(), buffer.readBoolean());
    }

    public static void handle(MessageSessionData message, Consumer<Runnable> executor) {
        executor.accept((Runnable) () -> ClientPlayHandler.handleSessionData(message));
    }
}