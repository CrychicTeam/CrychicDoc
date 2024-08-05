package com.illusivesoulworks.polymorph.common.network.client;

import com.illusivesoulworks.polymorph.api.PolymorphApi;
import com.illusivesoulworks.polymorph.common.integration.PolymorphIntegrations;
import java.util.Optional;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

public record CPacketStackRecipeSelection(ResourceLocation recipe) {

    public static void encode(CPacketStackRecipeSelection packet, FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(packet.recipe);
    }

    public static CPacketStackRecipeSelection decode(FriendlyByteBuf buffer) {
        return new CPacketStackRecipeSelection(buffer.readResourceLocation());
    }

    public static void handle(CPacketStackRecipeSelection packet, ServerPlayer player) {
        Level world = player.m_20193_();
        Optional<? extends Recipe<?>> maybeRecipe = world.getRecipeManager().byKey(packet.recipe);
        maybeRecipe.ifPresent(recipe -> {
            AbstractContainerMenu container = player.f_36096_;
            PolymorphApi.common().getRecipeDataFromItemStack(container).ifPresent(recipeData -> {
                recipeData.selectRecipe(recipe);
                PolymorphIntegrations.selectRecipe(container, recipe);
            });
        });
    }
}