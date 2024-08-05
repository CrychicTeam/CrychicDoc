package net.minecraft.client.gui.screens.recipebook;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.List;
import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.StateSwitchingButton;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;

public class RecipeBookTabButton extends StateSwitchingButton {

    private final RecipeBookCategories category;

    private static final float ANIMATION_TIME = 15.0F;

    private float animationTime;

    public RecipeBookTabButton(RecipeBookCategories recipeBookCategories0) {
        super(0, 0, 35, 27, false);
        this.category = recipeBookCategories0;
        this.m_94624_(153, 2, 35, 0, RecipeBookComponent.RECIPE_BOOK_LOCATION);
    }

    public void startAnimation(Minecraft minecraft0) {
        ClientRecipeBook $$1 = minecraft0.player.getRecipeBook();
        List<RecipeCollection> $$2 = $$1.getCollection(this.category);
        if (minecraft0.player.f_36096_ instanceof RecipeBookMenu) {
            for (RecipeCollection $$3 : $$2) {
                for (Recipe<?> $$4 : $$3.getRecipes($$1.m_12689_((RecipeBookMenu) minecraft0.player.f_36096_))) {
                    if ($$1.m_12717_($$4)) {
                        this.animationTime = 15.0F;
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        if (this.animationTime > 0.0F) {
            float $$4 = 1.0F + 0.1F * (float) Math.sin((double) (this.animationTime / 15.0F * (float) Math.PI));
            guiGraphics0.pose().pushPose();
            guiGraphics0.pose().translate((float) (this.m_252754_() + 8), (float) (this.m_252907_() + 12), 0.0F);
            guiGraphics0.pose().scale(1.0F, $$4, 1.0F);
            guiGraphics0.pose().translate((float) (-(this.m_252754_() + 8)), (float) (-(this.m_252907_() + 12)), 0.0F);
        }
        Minecraft $$5 = Minecraft.getInstance();
        RenderSystem.disableDepthTest();
        int $$6 = this.f_94610_;
        int $$7 = this.f_94611_;
        if (this.f_94609_) {
            $$6 += this.f_94612_;
        }
        if (this.m_198029_()) {
            $$7 += this.f_94613_;
        }
        int $$8 = this.m_252754_();
        if (this.f_94609_) {
            $$8 -= 2;
        }
        guiGraphics0.blit(this.f_94608_, $$8, this.m_252907_(), $$6, $$7, this.f_93618_, this.f_93619_);
        RenderSystem.enableDepthTest();
        this.renderIcon(guiGraphics0, $$5.getItemRenderer());
        if (this.animationTime > 0.0F) {
            guiGraphics0.pose().popPose();
            this.animationTime -= float3;
        }
    }

    private void renderIcon(GuiGraphics guiGraphics0, ItemRenderer itemRenderer1) {
        List<ItemStack> $$2 = this.category.getIconItems();
        int $$3 = this.f_94609_ ? -2 : 0;
        if ($$2.size() == 1) {
            guiGraphics0.renderFakeItem((ItemStack) $$2.get(0), this.m_252754_() + 9 + $$3, this.m_252907_() + 5);
        } else if ($$2.size() == 2) {
            guiGraphics0.renderFakeItem((ItemStack) $$2.get(0), this.m_252754_() + 3 + $$3, this.m_252907_() + 5);
            guiGraphics0.renderFakeItem((ItemStack) $$2.get(1), this.m_252754_() + 14 + $$3, this.m_252907_() + 5);
        }
    }

    public RecipeBookCategories getCategory() {
        return this.category;
    }

    public boolean updateVisibility(ClientRecipeBook clientRecipeBook0) {
        List<RecipeCollection> $$1 = clientRecipeBook0.getCollection(this.category);
        this.f_93624_ = false;
        if ($$1 != null) {
            for (RecipeCollection $$2 : $$1) {
                if ($$2.hasKnownRecipes() && $$2.hasFitting()) {
                    this.f_93624_ = true;
                    break;
                }
            }
        }
        return this.f_93624_;
    }
}