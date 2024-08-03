package com.mna.guide.recipe;

import com.mna.api.affinity.Affinity;
import com.mna.api.guidebook.RecipeRendererBase;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.gui.GuiTextures;
import com.mna.recipes.eldrin.FumeFilterRecipe;
import java.util.ArrayList;
import java.util.Optional;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import org.apache.commons.lang3.mutable.MutableInt;

public class RecipeFumeFilter extends RecipeRendererBase implements ICyclingRecipeRenderer<RecipeFumeFilter> {

    private FumeFilterRecipe[] patterns;

    public RecipeFumeFilter(int xIn, int yIn) {
        super(xIn, yIn);
    }

    @Override
    public void init_cycling(ResourceLocation[] rLocs) {
        ArrayList<FumeFilterRecipe> patternList = new ArrayList();
        for (ResourceLocation rLoc : rLocs) {
            Optional<? extends Recipe<?>> pattern = this.resolveRecipe(rLoc);
            if (pattern.isPresent() && pattern.get() instanceof FumeFilterRecipe) {
                patternList.add((FumeFilterRecipe) pattern.get());
            }
        }
        this.patterns = (FumeFilterRecipe[]) patternList.toArray(new FumeFilterRecipe[0]);
    }

    @Override
    public void init_internal(ResourceLocation recipeLocation) {
        Optional<? extends Recipe<?>> pattern = this.resolveRecipe(recipeLocation);
        if (pattern.isPresent() && pattern.get() instanceof FumeFilterRecipe) {
            this.patterns = new FumeFilterRecipe[] { (FumeFilterRecipe) pattern.get() };
        } else {
            this.patterns = null;
        }
    }

    private Optional<? extends Recipe<?>> resolveRecipe(ResourceLocation recipeLocation) {
        if (recipeLocation.getNamespace().equals("mna") && !recipeLocation.getPath().startsWith("fume_filter/")) {
            recipeLocation = new ResourceLocation(recipeLocation.getNamespace(), "fume_filter/" + recipeLocation.getPath());
        }
        return this.minecraft.level.getRecipeManager().byKey(recipeLocation);
    }

    @Override
    protected ResourceLocation backgroundTexture() {
        return GuiTextures.Recipe.FUME_FILTER;
    }

    @Override
    protected void drawForeground(GuiGraphics pGuiGraphics, int x, int y, int mouseX, int mouseY, float partialTicks) {
        int index = this.getIndex();
        if (this.patterns.length != 0) {
            FumeFilterRecipe pattern = this.patterns[index];
            if (pattern != null) {
                int cX = this.m_252754_() + (int) ((float) (this.f_93618_ / 2 - 77) * this.scale);
                int affY = this.m_252907_() + (int) (82.0F * this.scale);
                Font fr = this.minecraft.font;
                ItemStack output = pattern.getGuiRepresentationStack();
                this.renderItemStack(pGuiGraphics, output, cX, this.m_252907_() + (int) (88.0F * this.scale), this.scale);
                String vText = String.format("%.0f", pattern.getTotalGeneration() * 0.01F);
                int strLen = fr.width(vText);
                int affPosX = this.m_252754_() + (int) ((float) (this.f_93618_ - 70 - strLen / 2) * this.scale);
                Affinity a = pattern.getAffinity();
                ItemStack guiStack = (ItemStack) GuiTextures.affinityIcons.get(a);
                this.renderItemStack(pGuiGraphics, guiStack, affPosX, affY - 4, this.scale);
                if (a == Affinity.WIND) {
                    pGuiGraphics.drawString(fr, vText, (int) ((float) (affPosX + 16) / this.scale), (int) ((float) affY / this.scale), FastColor.ARGB32.color(255, 0, 0, 0), false);
                } else {
                    pGuiGraphics.drawString(fr, vText, (int) ((float) (affPosX + 16) / this.scale), (int) ((float) affY / this.scale), FastColor.ARGB32.color(255, a.getColor()[0], a.getColor()[1], a.getColor()[2]), false);
                }
                int tier = pattern.getTier();
                MutableInt playerTier = new MutableInt(0);
                this.minecraft.player.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(p -> playerTier.setValue(p.getTier()));
                int color = tier <= playerTier.getValue() ? FastColor.ARGB32.color(255, 0, 128, 0) : FastColor.ARGB32.color(255, 255, 0, 0);
                Component name = Component.translatable("gui.mna.jei.eldrin_fume.generation");
                Component tierPrompt = Component.translatable("gui.mna.item-tier", tier);
                int stringWidth = this.minecraft.font.width(name);
                int textX = x + this.f_93618_ / 2 - stringWidth / 2;
                int textY = y + 5;
                pGuiGraphics.drawString(fr, name, textX, textY, FastColor.ARGB32.color(255, 255, 255, 255), false);
                pGuiGraphics.drawString(fr, tierPrompt, x + this.f_93618_ / 2 - this.minecraft.font.width(tierPrompt) / 2, y + 15, color, false);
                if (pattern.getFactionRequirement() != null) {
                    int xPadding = 3;
                    this.renderFactionIcon(pGuiGraphics, pattern.getFactionRequirement(), textX + stringWidth + xPadding, textY);
                }
            }
        }
    }

    @Override
    public int getTier() {
        if (this.patterns == null) {
            return 1;
        } else {
            int minTier = 5;
            for (FumeFilterRecipe pattern : this.patterns) {
                if (pattern.getTier() < minTier) {
                    minTier = pattern.getTier();
                }
            }
            return minTier;
        }
    }

    @Override
    public int countRecipes() {
        return this.patterns.length;
    }
}