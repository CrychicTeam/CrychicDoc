package net.minecraft.client.gui.screens.inventory;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.Slot;

public class CraftingScreen extends AbstractContainerScreen<CraftingMenu> implements RecipeUpdateListener {

    private static final ResourceLocation CRAFTING_TABLE_LOCATION = new ResourceLocation("textures/gui/container/crafting_table.png");

    private static final ResourceLocation RECIPE_BUTTON_LOCATION = new ResourceLocation("textures/gui/recipe_button.png");

    private final RecipeBookComponent recipeBookComponent = new RecipeBookComponent();

    private boolean widthTooNarrow;

    public CraftingScreen(CraftingMenu craftingMenu0, Inventory inventory1, Component component2) {
        super(craftingMenu0, inventory1, component2);
    }

    @Override
    protected void init() {
        super.init();
        this.widthTooNarrow = this.f_96543_ < 379;
        this.recipeBookComponent.init(this.f_96543_, this.f_96544_, this.f_96541_, this.widthTooNarrow, (RecipeBookMenu<?>) this.f_97732_);
        this.f_97735_ = this.recipeBookComponent.updateScreenPosition(this.f_96543_, this.f_97726_);
        this.m_142416_(new ImageButton(this.f_97735_ + 5, this.f_96544_ / 2 - 49, 20, 18, 0, 0, 19, RECIPE_BUTTON_LOCATION, p_289630_ -> {
            this.recipeBookComponent.toggleVisibility();
            this.f_97735_ = this.recipeBookComponent.updateScreenPosition(this.f_96543_, this.f_97726_);
            p_289630_.m_264152_(this.f_97735_ + 5, this.f_96544_ / 2 - 49);
        }));
        this.m_7787_(this.recipeBookComponent);
        this.m_264313_(this.recipeBookComponent);
        this.f_97728_ = 29;
    }

    @Override
    public void containerTick() {
        super.containerTick();
        this.recipeBookComponent.tick();
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        if (this.recipeBookComponent.isVisible() && this.widthTooNarrow) {
            this.renderBg(guiGraphics0, float3, int1, int2);
            this.recipeBookComponent.render(guiGraphics0, int1, int2, float3);
        } else {
            this.recipeBookComponent.render(guiGraphics0, int1, int2, float3);
            super.render(guiGraphics0, int1, int2, float3);
            this.recipeBookComponent.renderGhostRecipe(guiGraphics0, this.f_97735_, this.f_97736_, true, float3);
        }
        this.m_280072_(guiGraphics0, int1, int2);
        this.recipeBookComponent.renderTooltip(guiGraphics0, this.f_97735_, this.f_97736_, int1, int2);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics0, float float1, int int2, int int3) {
        int $$4 = this.f_97735_;
        int $$5 = (this.f_96544_ - this.f_97727_) / 2;
        guiGraphics0.blit(CRAFTING_TABLE_LOCATION, $$4, $$5, 0, 0, this.f_97726_, this.f_97727_);
    }

    @Override
    protected boolean isHovering(int int0, int int1, int int2, int int3, double double4, double double5) {
        return (!this.widthTooNarrow || !this.recipeBookComponent.isVisible()) && super.isHovering(int0, int1, int2, int3, double4, double5);
    }

    @Override
    public boolean mouseClicked(double double0, double double1, int int2) {
        if (this.recipeBookComponent.mouseClicked(double0, double1, int2)) {
            this.m_7522_(this.recipeBookComponent);
            return true;
        } else {
            return this.widthTooNarrow && this.recipeBookComponent.isVisible() ? true : super.mouseClicked(double0, double1, int2);
        }
    }

    @Override
    protected boolean hasClickedOutside(double double0, double double1, int int2, int int3, int int4) {
        boolean $$5 = double0 < (double) int2 || double1 < (double) int3 || double0 >= (double) (int2 + this.f_97726_) || double1 >= (double) (int3 + this.f_97727_);
        return this.recipeBookComponent.hasClickedOutside(double0, double1, this.f_97735_, this.f_97736_, this.f_97726_, this.f_97727_, int4) && $$5;
    }

    @Override
    protected void slotClicked(Slot slot0, int int1, int int2, ClickType clickType3) {
        super.slotClicked(slot0, int1, int2, clickType3);
        this.recipeBookComponent.slotClicked(slot0);
    }

    @Override
    public void recipesUpdated() {
        this.recipeBookComponent.recipesUpdated();
    }

    @Override
    public RecipeBookComponent getRecipeBookComponent() {
        return this.recipeBookComponent;
    }
}