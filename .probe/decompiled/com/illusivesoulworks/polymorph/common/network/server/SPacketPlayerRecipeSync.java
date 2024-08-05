package com.illusivesoulworks.polymorph.common.network.server;

import com.illusivesoulworks.polymorph.api.common.base.IRecipePair;
import com.illusivesoulworks.polymorph.common.impl.RecipePair;
import java.util.SortedSet;
import java.util.TreeSet;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record SPacketPlayerRecipeSync(SortedSet<IRecipePair> recipeList, ResourceLocation selected) {

    public SPacketPlayerRecipeSync(SortedSet<IRecipePair> recipeList, ResourceLocation selected) {
        if (recipeList != null) {
            this.recipeList.addAll(recipeList);
        }
        this.selected = selected;
    }

    public SortedSet<IRecipePair> getRecipeList() {
        return this.recipeList;
    }

    public ResourceLocation getSelected() {
        return this.selected;
    }

    public static void encode(SPacketPlayerRecipeSync packet, FriendlyByteBuf buffer) {
        if (!packet.recipeList.isEmpty()) {
            buffer.writeInt(packet.recipeList.size());
            for (IRecipePair data : packet.recipeList) {
                buffer.writeResourceLocation(data.getResourceLocation());
                buffer.writeItem(data.getOutput());
            }
            if (packet.selected != null) {
                buffer.writeResourceLocation(packet.selected);
            }
        }
    }

    public static SPacketPlayerRecipeSync decode(FriendlyByteBuf buffer) {
        SortedSet<IRecipePair> recipeDataset = new TreeSet();
        ResourceLocation selected = null;
        if (buffer.isReadable()) {
            int size = buffer.readInt();
            for (int i = 0; i < size; i++) {
                recipeDataset.add(new RecipePair(buffer.readResourceLocation(), buffer.readItem()));
            }
            if (buffer.isReadable()) {
                selected = buffer.readResourceLocation();
            }
        }
        return new SPacketPlayerRecipeSync(recipeDataset, selected);
    }

    public static void handle(SPacketPlayerRecipeSync packet) {
        ClientPacketHandler.handle(packet);
    }
}