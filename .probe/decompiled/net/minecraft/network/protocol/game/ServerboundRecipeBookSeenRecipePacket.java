package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;

public class ServerboundRecipeBookSeenRecipePacket implements Packet<ServerGamePacketListener> {

    private final ResourceLocation recipe;

    public ServerboundRecipeBookSeenRecipePacket(Recipe<?> recipe0) {
        this.recipe = recipe0.getId();
    }

    public ServerboundRecipeBookSeenRecipePacket(FriendlyByteBuf friendlyByteBuf0) {
        this.recipe = friendlyByteBuf0.readResourceLocation();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeResourceLocation(this.recipe);
    }

    public void handle(ServerGamePacketListener serverGamePacketListener0) {
        serverGamePacketListener0.handleRecipeBookSeenRecipePacket(this);
    }

    public ResourceLocation getRecipe() {
        return this.recipe;
    }
}