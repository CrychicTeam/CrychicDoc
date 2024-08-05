package net.minecraft.client.gui.screens.inventory;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.recipebook.AbstractFurnaceRecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.Slot;

public abstract class AbstractFurnaceScreen<T extends AbstractFurnaceMenu> extends AbstractContainerScreen<T> implements RecipeUpdateListener {

    private static final ResourceLocation RECIPE_BUTTON_LOCATION = new ResourceLocation("textures/gui/recipe_button.png");

    public final AbstractFurnaceRecipeBookComponent recipeBookComponent;

    private boolean widthTooNarrow;

    private final ResourceLocation texture;

    public AbstractFurnaceScreen(T t0, AbstractFurnaceRecipeBookComponent abstractFurnaceRecipeBookComponent1, Inventory inventory2, Component component3, ResourceLocation resourceLocation4) {
        super(t0, inventory2, component3);
        this.recipeBookComponent = abstractFurnaceRecipeBookComponent1;
        this.texture = resourceLocation4;
    }

    @Override
    public void init() {
        super.init();
        this.widthTooNarrow = this.f_96543_ < 379;
        this.recipeBookComponent.m_100309_(this.f_96543_, this.f_96544_, this.f_96541_, this.widthTooNarrow, (RecipeBookMenu) this.f_97732_);
        this.f_97735_ = this.recipeBookComponent.m_181401_(this.f_96543_, this.f_97726_);
        this.m_142416_(new ImageButton(this.f_97735_ + 20, this.f_96544_ / 2 - 49, 20, 18, 0, 0, 19, RECIPE_BUTTON_LOCATION, p_289628_ -> {
            this.recipeBookComponent.m_100384_();
            this.f_97735_ = this.recipeBookComponent.m_181401_(this.f_96543_, this.f_97726_);
            p_289628_.m_264152_(this.f_97735_ + 20, this.f_96544_ / 2 - 49);
        }));
        this.f_97728_ = (this.f_97726_ - this.f_96547_.width(this.f_96539_)) / 2;
    }

    @Override
    public void containerTick() {
        super.containerTick();
        this.recipeBookComponent.m_100386_();
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        if (this.recipeBookComponent.m_100385_() && this.widthTooNarrow) {
            this.renderBg(guiGraphics0, float3, int1, int2);
            this.recipeBookComponent.m_88315_(guiGraphics0, int1, int2, float3);
        } else {
            this.recipeBookComponent.m_88315_(guiGraphics0, int1, int2, float3);
            super.render(guiGraphics0, int1, int2, float3);
            this.recipeBookComponent.m_280128_(guiGraphics0, this.f_97735_, this.f_97736_, true, float3);
        }
        this.m_280072_(guiGraphics0, int1, int2);
        this.recipeBookComponent.m_280545_(guiGraphics0, this.f_97735_, this.f_97736_, int1, int2);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics0, float float1, int int2, int int3) {
        int $$4 = this.f_97735_;
        int $$5 = this.f_97736_;
        guiGraphics0.blit(this.texture, $$4, $$5, 0, 0, this.f_97726_, this.f_97727_);
        if (((AbstractFurnaceMenu) this.f_97732_).isLit()) {
            int $$6 = ((AbstractFurnaceMenu) this.f_97732_).getLitProgress();
            guiGraphics0.blit(this.texture, $$4 + 56, $$5 + 36 + 12 - $$6, 176, 12 - $$6, 14, $$6 + 1);
        }
        int $$7 = ((AbstractFurnaceMenu) this.f_97732_).getBurnProgress();
        guiGraphics0.blit(this.texture, $$4 + 79, $$5 + 34, 176, 14, $$7 + 1, 16);
    }

    @Override
    public boolean mouseClicked(double double0, double double1, int int2) {
        if (this.recipeBookComponent.m_6375_(double0, double1, int2)) {
            return true;
        } else {
            return this.widthTooNarrow && this.recipeBookComponent.m_100385_() ? true : super.mouseClicked(double0, double1, int2);
        }
    }

    @Override
    protected void slotClicked(Slot slot0, int int1, int int2, ClickType clickType3) {
        super.slotClicked(slot0, int1, int2, clickType3);
        this.recipeBookComponent.slotClicked(slot0);
    }

    @Override
    public boolean keyPressed(int int0, int int1, int int2) {
        return this.recipeBookComponent.m_7933_(int0, int1, int2) ? false : super.keyPressed(int0, int1, int2);
    }

    @Override
    protected boolean hasClickedOutside(double double0, double double1, int int2, int int3, int int4) {
        boolean $$5 = double0 < (double) int2 || double1 < (double) int3 || double0 >= (double) (int2 + this.f_97726_) || double1 >= (double) (int3 + this.f_97727_);
        return this.recipeBookComponent.m_100297_(double0, double1, this.f_97735_, this.f_97736_, this.f_97726_, this.f_97727_, int4) && $$5;
    }

    @Override
    public boolean charTyped(char char0, int int1) {
        return this.recipeBookComponent.m_5534_(char0, int1) ? true : super.m_5534_(char0, int1);
    }

    @Override
    public void recipesUpdated() {
        this.recipeBookComponent.m_100387_();
    }

    @Override
    public RecipeBookComponent getRecipeBookComponent() {
        return this.recipeBookComponent;
    }
}