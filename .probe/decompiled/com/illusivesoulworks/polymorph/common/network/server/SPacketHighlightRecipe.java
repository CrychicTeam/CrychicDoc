package com.illusivesoulworks.polymorph.common.network.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record SPacketHighlightRecipe(ResourceLocation recipe) {

    public ResourceLocation getRecipe() {
        return this.recipe;
    }

    public static void encode(SPacketHighlightRecipe packet, FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(packet.recipe);
    }

    public static SPacketHighlightRecipe decode(FriendlyByteBuf buffer) {
        return new SPacketHighlightRecipe(buffer.readResourceLocation());
    }

    public static void handle(SPacketHighlightRecipe packet) {
        ClientPacketHandler.handle(packet);
    }
}