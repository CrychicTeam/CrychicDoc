package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;

public class ClientboundPlaceGhostRecipePacket implements Packet<ClientGamePacketListener> {

    private final int containerId;

    private final ResourceLocation recipe;

    public ClientboundPlaceGhostRecipePacket(int int0, Recipe<?> recipe1) {
        this.containerId = int0;
        this.recipe = recipe1.getId();
    }

    public ClientboundPlaceGhostRecipePacket(FriendlyByteBuf friendlyByteBuf0) {
        this.containerId = friendlyByteBuf0.readByte();
        this.recipe = friendlyByteBuf0.readResourceLocation();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeByte(this.containerId);
        friendlyByteBuf0.writeResourceLocation(this.recipe);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handlePlaceRecipe(this);
    }

    public ResourceLocation getRecipe() {
        return this.recipe;
    }

    public int getContainerId() {
        return this.containerId;
    }
}