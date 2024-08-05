package com.simibubi.create.content.equipment.blueprint;

import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class BlueprintAssignCompleteRecipePacket extends SimplePacketBase {

    private ResourceLocation recipeID;

    public BlueprintAssignCompleteRecipePacket(ResourceLocation recipeID) {
        this.recipeID = recipeID;
    }

    public BlueprintAssignCompleteRecipePacket(FriendlyByteBuf buffer) {
        this.recipeID = buffer.readResourceLocation();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(this.recipeID);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                if (player.f_36096_ instanceof BlueprintMenu c) {
                    player.m_9236_().getRecipeManager().byKey(this.recipeID).ifPresent(r -> BlueprintItem.assignCompleteRecipe(c.player.m_9236_(), c.ghostInventory, r));
                }
            }
        });
        return true;
    }
}