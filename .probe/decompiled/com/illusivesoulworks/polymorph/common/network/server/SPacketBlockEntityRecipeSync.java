package com.illusivesoulworks.polymorph.common.network.server;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record SPacketBlockEntityRecipeSync(BlockPos blockPos, ResourceLocation selected) {

    public BlockPos getBlockPos() {
        return this.blockPos;
    }

    public ResourceLocation getSelected() {
        return this.selected;
    }

    public static void encode(SPacketBlockEntityRecipeSync packet, FriendlyByteBuf buffer) {
        buffer.writeBlockPos(packet.getBlockPos());
        buffer.writeResourceLocation(packet.getSelected());
    }

    public static SPacketBlockEntityRecipeSync decode(FriendlyByteBuf buffer) {
        return new SPacketBlockEntityRecipeSync(buffer.readBlockPos(), buffer.readResourceLocation());
    }

    public static void handle(SPacketBlockEntityRecipeSync packet) {
        ClientPacketHandler.handle(packet);
    }
}