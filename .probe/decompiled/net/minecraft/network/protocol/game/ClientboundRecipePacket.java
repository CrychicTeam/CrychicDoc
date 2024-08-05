package net.minecraft.network.protocol.game;

import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.List;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.RecipeBookSettings;

public class ClientboundRecipePacket implements Packet<ClientGamePacketListener> {

    private final ClientboundRecipePacket.State state;

    private final List<ResourceLocation> recipes;

    private final List<ResourceLocation> toHighlight;

    private final RecipeBookSettings bookSettings;

    public ClientboundRecipePacket(ClientboundRecipePacket.State clientboundRecipePacketState0, Collection<ResourceLocation> collectionResourceLocation1, Collection<ResourceLocation> collectionResourceLocation2, RecipeBookSettings recipeBookSettings3) {
        this.state = clientboundRecipePacketState0;
        this.recipes = ImmutableList.copyOf(collectionResourceLocation1);
        this.toHighlight = ImmutableList.copyOf(collectionResourceLocation2);
        this.bookSettings = recipeBookSettings3;
    }

    public ClientboundRecipePacket(FriendlyByteBuf friendlyByteBuf0) {
        this.state = friendlyByteBuf0.readEnum(ClientboundRecipePacket.State.class);
        this.bookSettings = RecipeBookSettings.read(friendlyByteBuf0);
        this.recipes = friendlyByteBuf0.readList(FriendlyByteBuf::m_130281_);
        if (this.state == ClientboundRecipePacket.State.INIT) {
            this.toHighlight = friendlyByteBuf0.readList(FriendlyByteBuf::m_130281_);
        } else {
            this.toHighlight = ImmutableList.of();
        }
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeEnum(this.state);
        this.bookSettings.write(friendlyByteBuf0);
        friendlyByteBuf0.writeCollection(this.recipes, FriendlyByteBuf::m_130085_);
        if (this.state == ClientboundRecipePacket.State.INIT) {
            friendlyByteBuf0.writeCollection(this.toHighlight, FriendlyByteBuf::m_130085_);
        }
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleAddOrRemoveRecipes(this);
    }

    public List<ResourceLocation> getRecipes() {
        return this.recipes;
    }

    public List<ResourceLocation> getHighlights() {
        return this.toHighlight;
    }

    public RecipeBookSettings getBookSettings() {
        return this.bookSettings;
    }

    public ClientboundRecipePacket.State getState() {
        return this.state;
    }

    public static enum State {

        INIT, ADD, REMOVE
    }
}