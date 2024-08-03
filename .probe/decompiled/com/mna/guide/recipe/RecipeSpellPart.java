package com.mna.guide.recipe;

import com.mna.Registries;
import com.mna.api.guidebook.RecipeRendererBase;
import com.mna.api.spells.base.ISpellComponent;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.tools.MATags;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.gui.GuiTextures;
import com.mna.recipes.ItemAndPatternRecipe;
import com.mna.recipes.manaweaving.ManaweavingPattern;
import com.mna.recipes.manaweaving.ManaweavingPatternHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.mutable.MutableInt;

public class RecipeSpellPart extends RecipeRendererBase {

    ItemAndPatternRecipe recipe;

    ISpellComponent output;

    ArrayList<List<ItemStack>> reagents = new ArrayList();

    public RecipeSpellPart(int xIn, int yIn) {
        super(xIn, yIn);
    }

    @Override
    protected void drawForeground(GuiGraphics pGuiGraphics, int x, int y, int mouseX, int mouseY, float partialTicks) {
        if (this.recipe != null) {
            this.renderSpellIcon(pGuiGraphics, x, y);
            this.renderManaweavePatterns(pGuiGraphics, x, y);
            this.renderRecipeStack(pGuiGraphics, 0, 41, 44);
            this.renderRecipeStack(pGuiGraphics, 1, 162, 159);
            this.renderRecipeStack(pGuiGraphics, 2, 41, 159);
            this.renderRecipeStack(pGuiGraphics, 3, 162, 44);
            this.renderRecipeStack(pGuiGraphics, 4, 41, 101);
            this.renderRecipeStack(pGuiGraphics, 5, 162, 101);
            this.renderRecipeStack(pGuiGraphics, 6, 101, 44);
            this.renderRecipeStack(pGuiGraphics, 7, 101, 159);
            Font fr = this.minecraft.font;
            int tier = this.recipe.getTier();
            MutableInt playerTier = new MutableInt(0);
            this.minecraft.player.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(p -> playerTier.setValue(p.getTier()));
            int color = tier <= playerTier.getValue() ? FastColor.ARGB32.color(255, 0, 128, 0) : FastColor.ARGB32.color(255, 255, 0, 0);
            Component name = Component.translatable(this.output.getRegistryName().toString());
            Component tierPrompt = Component.translatable("gui.mna.item-tier", tier);
            int stringWidth = fr.width(name);
            int textX = x + this.f_93618_ / 2 - stringWidth / 2;
            int textY = 10;
            pGuiGraphics.drawString(this.minecraft.font, name, x + this.f_93618_ / 2 - this.minecraft.font.width(name) / 2, y + textY, FastColor.ARGB32.color(255, 255, 255, 255), false);
            pGuiGraphics.drawString(this.minecraft.font, tierPrompt, x + this.f_93618_ / 2 - this.minecraft.font.width(tierPrompt) / 2, y + textY + 10, color, false);
            if (this.output.getFactionRequirement() != null) {
                int xPadding = 3;
                this.renderFactionIcon(pGuiGraphics, this.output.getFactionRequirement(), textX + stringWidth + xPadding, y + textY);
            }
            if (this.output instanceof SpellEffect) {
                ItemStack affinity = (ItemStack) GuiTextures.affinityIcons.getOrDefault(((SpellEffect) this.output).getAffinity(), ItemStack.EMPTY);
                if (!affinity.isEmpty()) {
                    this.renderItemStack(pGuiGraphics, affinity, textX - 19, y + 5);
                }
            }
        }
    }

    private void renderRecipeStack(GuiGraphics pGuiGraphics, int index, int x, int y) {
        if (index < this.reagents.size()) {
            this.renderItemStack(pGuiGraphics, (List<ItemStack>) this.reagents.get(index), (int) ((float) this.m_252754_() / this.scale + (float) x), (int) ((float) this.m_252907_() / this.scale + (float) (y + 1)), 1.0F);
        }
    }

    private void renderManaweavePatterns(GuiGraphics pGuiGraphics, int x, int y) {
        float patternScale = 0.1F;
        int POINT_RENDER_SIZE = 13;
        int startX = (int) ((float) (x + 31) / patternScale);
        int startY = (int) ((float) (y + 225) / patternScale);
        RenderSystem.setShaderColor(0.5F, 0.0F, 1.0F, 1.0F);
        for (int i = 0; i < this.recipe.getRequiredPatterns().length; i++) {
            ManaweavingPattern p = ManaweavingPatternHelper.GetManaweavingRecipe(this.minecraft.level, this.recipe.getRequiredPatterns()[i]);
            if (p != null) {
                pGuiGraphics.pose().pushPose();
                pGuiGraphics.pose().scale(patternScale, patternScale, patternScale);
                byte[][] pData = p.get();
                int pointSize = 13;
                for (int k = 0; k < pData.length; k++) {
                    for (int j = 0; j < pData[k].length; j++) {
                        if (pData[k][j] != 0) {
                            pGuiGraphics.blit(GuiTextures.Widgets.GUIDE_WIDGETS, startX - j * pointSize, startY + k * pointSize, 0, 0, POINT_RENDER_SIZE, POINT_RENDER_SIZE);
                        }
                    }
                }
                pGuiGraphics.pose().popPose();
                startX = (int) ((float) startX + 34.0F / patternScale);
            }
        }
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void renderSpellIcon(GuiGraphics pGuiGraphics, int x, int y) {
        if (this.output != null) {
            pGuiGraphics.blit(this.output.getGuiIcon(), x + 102, y + 101, 0.0F, 0.0F, 16, 16, 16, 16);
        }
    }

    @Override
    protected ResourceLocation backgroundTexture() {
        return GuiTextures.Recipe.SPELLPART;
    }

    @Override
    public void init_internal(ResourceLocation recipeLocation) {
        Optional<? extends Recipe<?>> pattern = this.minecraft.level.getRecipeManager().byKey(recipeLocation);
        if (pattern.isPresent() && pattern.get() instanceof ItemAndPatternRecipe) {
            this.recipe = (ItemAndPatternRecipe) pattern.get();
        }
        if (this.recipe != null) {
            ResourceLocation rLoc = this.recipe.getOutput();
            if (rLoc.getNamespace().equals("mna")) {
                rLoc = new ResourceLocation("mna", "shapes/" + this.recipe.getOutput().getPath());
                this.output = (ISpellComponent) ((IForgeRegistry) Registries.Shape.get()).getValue(rLoc);
                if (this.output == null) {
                    rLoc = new ResourceLocation("mna", "components/" + this.recipe.getOutput().getPath());
                    this.output = (ISpellComponent) ((IForgeRegistry) Registries.SpellEffect.get()).getValue(rLoc);
                }
                if (this.output == null) {
                    rLoc = new ResourceLocation("mna", "modifiers/" + this.recipe.getOutput().getPath());
                    this.output = (ISpellComponent) ((IForgeRegistry) Registries.Modifier.get()).getValue(rLoc);
                }
            } else {
                this.output = (ISpellComponent) ((IForgeRegistry) Registries.Shape.get()).getValue(this.recipe.getOutput());
                if (this.output == null) {
                    this.output = (ISpellComponent) ((IForgeRegistry) Registries.SpellEffect.get()).getValue(this.recipe.getOutput());
                }
                if (this.output == null) {
                    this.output = (ISpellComponent) ((IForgeRegistry) Registries.Modifier.get()).getValue(this.recipe.getOutput());
                }
            }
            for (int i = 0; i < this.recipe.getRequiredItems().length; i++) {
                this.reagents.add((List) MATags.smartLookupItem(this.recipe.getRequiredItems()[i]).stream().map(item -> new ItemStack(item)).collect(Collectors.toList()));
            }
        }
    }

    @Override
    public int getTier() {
        return this.recipe != null ? this.recipe.getTier() : 1;
    }
}