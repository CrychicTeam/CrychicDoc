package me.jellysquid.mods.sodium.client.render.vertex;

import com.mojang.blaze3d.vertex.VertexConsumer;

public class VertexConsumerUtils {

    public static net.caffeinemc.mods.sodium.api.vertex.buffer.VertexBufferWriter convertOrLog(VertexConsumer consumer) {
        net.caffeinemc.mods.sodium.api.vertex.buffer.VertexBufferWriter writer = net.caffeinemc.mods.sodium.api.vertex.buffer.VertexBufferWriter.tryOf(consumer);
        if (writer == null) {
            VertexConsumerTracker.logBadConsumer(consumer);
        }
        return writer;
    }
}