package com.mna.gui.block;

import com.mna.gui.containers.block.ContainerSpectralCraftingTable;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.Slot;

public class GuiSpectralCraftingTable extends AbstractContainerScreen<ContainerSpectralCraftingTable> {

    private static final ResourceLocation CRAFTING_TABLE_GUI_TEXTURES = new ResourceLocation("textures/gui/container/crafting_table.png");

    private static final ResourceLocation RECIPE_BUTTON_TEXTURE = new ResourceLocation("textures/gui/recipe_button.png");

    private final RecipeBookComponent recipeBookGui = new RecipeBookComponent();

    private boolean widthTooNarrow;

    public GuiSpectralCraftingTable(ContainerSpectralCraftingTable screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    protected void init() {
        super.init();
        this.widthTooNarrow = this.f_96543_ < 379;
        this.recipeBookGui.init(this.f_96543_, this.f_96544_, this.f_96541_, this.widthTooNarrow, (RecipeBookMenu<?>) this.f_97732_);
        this.f_97735_ = this.recipeBookGui.updateScreenPosition(this.f_96543_, this.f_97726_);
        this.m_7787_(this.recipeBookGui);
        this.m_264313_(this.recipeBookGui);
        this.m_142416_(new ImageButton(this.f_97735_ + 5, this.f_96544_ / 2 - 49, 20, 18, 0, 0, 19, RECIPE_BUTTON_TEXTURE, button -> {
            this.recipeBookGui.initVisuals();
            this.recipeBookGui.toggleVisibility();
            this.f_97735_ = this.recipeBookGui.updateScreenPosition(this.f_96543_, this.f_97726_);
            ((ImageButton) button).m_264152_(this.f_97735_ + 5, this.f_96544_ / 2 - 49);
        }));
        this.f_97728_ = 29;
    }

    @Override
    public void containerTick() {
        this.recipeBookGui.tick();
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.m_280273_(pGuiGraphics);
        if (this.recipeBookGui.isVisible() && this.widthTooNarrow) {
            this.renderBg(pGuiGraphics, partialTicks, mouseX, mouseY);
            this.recipeBookGui.render(pGuiGraphics, mouseX, mouseY, partialTicks);
        } else {
            this.recipeBookGui.render(pGuiGraphics, mouseX, mouseY, partialTicks);
            super.render(pGuiGraphics, mouseX, mouseY, partialTicks);
            this.recipeBookGui.renderGhostRecipe(pGuiGraphics, this.f_97735_, this.f_97736_, true, partialTicks);
        }
        this.m_280072_(pGuiGraphics, mouseX, mouseY);
        this.recipeBookGui.renderTooltip(pGuiGraphics, this.f_97735_, this.f_97736_, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float partialTicks, int x, int y) {
        this.m_280273_(pGuiGraphics);
        RenderSystem.setShader(GameRenderer::m_172817_);
        int i = this.f_97735_;
        int j = (this.f_96544_ - this.f_97727_) / 2;
        pGuiGraphics.blit(CRAFTING_TABLE_GUI_TEXTURES, i, j, 0, 0, this.f_97726_, this.f_97727_);
    }

    @Override
    protected boolean isHovering(int x, int y, int width, int height, double mouseX, double mouseY) {
        return (!this.widthTooNarrow || !this.recipeBookGui.isVisible()) && super.isHovering(x, y, width, height, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.recipeBookGui.mouseClicked(mouseX, mouseY, button)) {
            this.m_7522_(this.recipeBookGui);
            return true;
        } else {
            return this.widthTooNarrow && this.recipeBookGui.isVisible() ? true : super.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    protected boolean hasClickedOutside(double mouseX, double mouseY, int guiLeftIn, int guiTopIn, int mouseButton) {
        boolean flag = mouseX < (double) guiLeftIn || mouseY < (double) guiTopIn || mouseX >= (double) (guiLeftIn + this.f_97726_) || mouseY >= (double) (guiTopIn + this.f_97727_);
        return this.recipeBookGui.hasClickedOutside(mouseX, mouseY, this.f_97735_, this.f_97736_, this.f_97726_, this.f_97727_, mouseButton) && flag;
    }

    @Override
    protected void slotClicked(Slot slotIn, int slotId, int mouseButton, ClickType type) {
        super.slotClicked(slotIn, slotId, mouseButton, type);
        this.recipeBookGui.slotClicked(slotIn);
    }

    public void recipesUpdated() {
        this.recipeBookGui.recipesUpdated();
    }

    public RecipeBookComponent getRecipeGui() {
        return this.recipeBookGui;
    }
}