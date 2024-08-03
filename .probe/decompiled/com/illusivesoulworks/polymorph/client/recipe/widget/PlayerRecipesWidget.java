package com.illusivesoulworks.polymorph.client.recipe.widget;

import com.illusivesoulworks.polymorph.api.PolymorphApi;
import com.illusivesoulworks.polymorph.api.client.widget.AbstractRecipesWidget;
import com.illusivesoulworks.polymorph.api.common.base.IPolymorphCommon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;

public class PlayerRecipesWidget extends AbstractRecipesWidget {

    final Slot outputSlot;

    public PlayerRecipesWidget(AbstractContainerScreen<?> containerScreen, Slot outputSlot) {
        super(containerScreen);
        this.outputSlot = outputSlot;
    }

    @Override
    public void selectRecipe(ResourceLocation resourceLocation) {
        IPolymorphCommon commonApi = PolymorphApi.common();
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            player.m_9236_().getRecipeManager().byKey(resourceLocation).ifPresent(recipe -> commonApi.getRecipeData(player).ifPresent(recipeData -> recipeData.selectRecipe(recipe)));
        }
        commonApi.getPacketDistributor().sendPlayerRecipeSelectionC2S(resourceLocation);
    }

    @Override
    public Slot getOutputSlot() {
        return this.outputSlot;
    }
}