package com.illusivesoulworks.polymorph.api.client.widget;

import com.illusivesoulworks.polymorph.api.client.base.IRecipesWidget;
import com.illusivesoulworks.polymorph.api.common.base.IRecipePair;
import com.illusivesoulworks.polymorph.platform.Services;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;

public abstract class AbstractRecipesWidget implements IRecipesWidget {

    public static final ResourceLocation WIDGETS = new ResourceLocation("polymorph", "textures/gui/widgets.png");

    public static final int BUTTON_X_OFFSET = 0;

    public static final int BUTTON_Y_OFFSET = -22;

    public static final int WIDGET_X_OFFSET = -4;

    public static final int WIDGET_Y_OFFSET = -26;

    protected final AbstractContainerScreen<?> containerScreen;

    protected final int xOffset;

    protected final int yOffset;

    protected SelectionWidget selectionWidget;

    protected OpenSelectionButton openButton;

    public AbstractRecipesWidget(AbstractContainerScreen<?> containerScreen, int xOffset, int yOffset) {
        this.containerScreen = containerScreen;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public AbstractRecipesWidget(AbstractContainerScreen<?> containerScreen) {
        this(containerScreen, -4, -26);
    }

    @Override
    public void initChildWidgets() {
        int x = Services.CLIENT_PLATFORM.getScreenLeft(this.containerScreen) + this.getXPos();
        int y = Services.CLIENT_PLATFORM.getScreenTop(this.containerScreen) + this.getYPos();
        this.selectionWidget = new SelectionWidget(x + this.xOffset, y + this.yOffset, this.getXPos() + this.xOffset, this.getYPos() + this.yOffset, this::selectRecipe, this.containerScreen);
        this.openButton = new OpenSelectionButton(this.containerScreen, this.getXPos(), this.getYPos(), clickWidget -> this.selectionWidget.setActive(!this.selectionWidget.isActive()));
        this.openButton.f_93624_ = this.selectionWidget.getOutputWidgets().size() > 1;
    }

    protected void resetWidgetOffsets() {
        int x = this.getXPos();
        int y = this.getYPos();
        this.selectionWidget.setOffsets(x + this.xOffset, y + this.yOffset);
        this.openButton.setOffsets(x, y);
    }

    @Override
    public abstract void selectRecipe(ResourceLocation var1);

    @Override
    public SelectionWidget getSelectionWidget() {
        return this.selectionWidget;
    }

    @Override
    public void highlightRecipe(ResourceLocation resourceLocation) {
        this.selectionWidget.highlightButton(resourceLocation);
    }

    @Override
    public void setRecipesList(Set<IRecipePair> recipesList, ResourceLocation selected) {
        SortedSet<IRecipePair> sorted = new TreeSet(recipesList);
        this.selectionWidget.setRecipeList(sorted);
        this.openButton.f_93624_ = recipesList.size() > 1;
        if (selected != null) {
            this.highlightRecipe(selected);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float renderPartialTicks) {
        this.selectionWidget.render(guiGraphics, mouseX, mouseY, renderPartialTicks);
        this.openButton.m_88315_(guiGraphics, mouseX, mouseY, renderPartialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.openButton.m_6375_(mouseX, mouseY, button)) {
            return true;
        } else if (this.selectionWidget.mouseClicked(mouseX, mouseY, button)) {
            this.selectionWidget.setActive(false);
            return true;
        } else if (this.selectionWidget.isActive()) {
            if (!this.openButton.m_6375_(mouseX, mouseY, button)) {
                this.selectionWidget.setActive(false);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getXPos() {
        return this.getOutputSlot().x + 0;
    }

    @Override
    public int getYPos() {
        return this.getOutputSlot().y + -22;
    }
}