package com.illusivesoulworks.polymorph.client.recipe.widget;

import com.illusivesoulworks.polymorph.api.PolymorphApi;
import com.illusivesoulworks.polymorph.api.client.widget.AbstractRecipesWidget;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;

public abstract class PersistentRecipesWidget extends AbstractRecipesWidget {

    public PersistentRecipesWidget(AbstractContainerScreen<?> containerScreen) {
        super(containerScreen);
    }

    @Override
    public void selectRecipe(ResourceLocation resourceLocation) {
        PolymorphApi.common().getPacketDistributor().sendPersistentRecipeSelectionC2S(resourceLocation);
    }
}