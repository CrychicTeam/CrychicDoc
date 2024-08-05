package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;

public class ServerboundPlaceRecipePacket implements Packet<ServerGamePacketListener> {

    private final int containerId;

    private final ResourceLocation recipe;

    private final boolean shiftDown;

    public ServerboundPlaceRecipePacket(int int0, Recipe<?> recipe1, boolean boolean2) {
        this.containerId = int0;
        this.recipe = recipe1.getId();
        this.shiftDown = boolean2;
    }

    public ServerboundPlaceRecipePacket(FriendlyByteBuf friendlyByteBuf0) {
        this.containerId = friendlyByteBuf0.readByte();
        this.recipe = friendlyByteBuf0.readResourceLocation();
        this.shiftDown = friendlyByteBuf0.readBoolean();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeByte(this.containerId);
        friendlyByteBuf0.writeResourceLocation(this.recipe);
        friendlyByteBuf0.writeBoolean(this.shiftDown);
    }

    public void handle(ServerGamePacketListener serverGamePacketListener0) {
        serverGamePacketListener0.handlePlaceRecipe(this);
    }

    public int getContainerId() {
        return this.containerId;
    }

    public ResourceLocation getRecipe() {
        return this.recipe;
    }

    public boolean isShiftDown() {
        return this.shiftDown;
    }
}