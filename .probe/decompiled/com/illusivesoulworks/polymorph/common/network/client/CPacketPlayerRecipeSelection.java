package com.illusivesoulworks.polymorph.common.network.client;

import com.illusivesoulworks.polymorph.api.PolymorphApi;
import com.illusivesoulworks.polymorph.common.integration.PolymorphIntegrations;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ItemCombinerMenu;

public record CPacketPlayerRecipeSelection(ResourceLocation recipe) {

    public static void encode(CPacketPlayerRecipeSelection packet, FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(packet.recipe);
    }

    public static CPacketPlayerRecipeSelection decode(FriendlyByteBuf buffer) {
        return new CPacketPlayerRecipeSelection(buffer.readResourceLocation());
    }

    public static void handle(CPacketPlayerRecipeSelection packet, ServerPlayer player) {
        AbstractContainerMenu container = player.f_36096_;
        player.m_9236_().getRecipeManager().byKey(packet.recipe).ifPresent(recipe -> {
            PolymorphApi.common().getRecipeData(player).ifPresent(recipeData -> recipeData.selectRecipe(recipe));
            PolymorphIntegrations.selectRecipe(container, recipe);
            container.slotsChanged(player.m_150109_());
            if (container instanceof ItemCombinerMenu) {
                ((ItemCombinerMenu) container).createResult();
            }
        });
    }
}