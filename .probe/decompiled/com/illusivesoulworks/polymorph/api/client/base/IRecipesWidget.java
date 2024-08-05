package com.illusivesoulworks.polymorph.api.client.base;

import com.illusivesoulworks.polymorph.api.client.widget.SelectionWidget;
import com.illusivesoulworks.polymorph.api.common.base.IRecipePair;
import java.util.Set;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;

public interface IRecipesWidget {

    void initChildWidgets();

    void selectRecipe(ResourceLocation var1);

    void highlightRecipe(ResourceLocation var1);

    void setRecipesList(Set<IRecipePair> var1, ResourceLocation var2);

    void render(GuiGraphics var1, int var2, int var3, float var4);

    boolean mouseClicked(double var1, double var3, int var5);

    Slot getOutputSlot();

    SelectionWidget getSelectionWidget();

    int getXPos();

    int getYPos();
}