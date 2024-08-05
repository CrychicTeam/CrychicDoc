package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.inventory.RecipeBookType;

public class ServerboundRecipeBookChangeSettingsPacket implements Packet<ServerGamePacketListener> {

    private final RecipeBookType bookType;

    private final boolean isOpen;

    private final boolean isFiltering;

    public ServerboundRecipeBookChangeSettingsPacket(RecipeBookType recipeBookType0, boolean boolean1, boolean boolean2) {
        this.bookType = recipeBookType0;
        this.isOpen = boolean1;
        this.isFiltering = boolean2;
    }

    public ServerboundRecipeBookChangeSettingsPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.bookType = friendlyByteBuf0.readEnum(RecipeBookType.class);
        this.isOpen = friendlyByteBuf0.readBoolean();
        this.isFiltering = friendlyByteBuf0.readBoolean();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeEnum(this.bookType);
        friendlyByteBuf0.writeBoolean(this.isOpen);
        friendlyByteBuf0.writeBoolean(this.isFiltering);
    }

    public void handle(ServerGamePacketListener serverGamePacketListener0) {
        serverGamePacketListener0.handleRecipeBookChangeSettingsPacket(this);
    }

    public RecipeBookType getBookType() {
        return this.bookType;
    }

    public boolean isOpen() {
        return this.isOpen;
    }

    public boolean isFiltering() {
        return this.isFiltering;
    }
}