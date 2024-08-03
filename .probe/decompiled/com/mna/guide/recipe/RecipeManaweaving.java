package com.mna.guide.recipe;

import com.mna.api.guidebook.RecipeRendererBase;
import com.mna.api.tools.MATags;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.gui.GuiTextures;
import com.mna.recipes.manaweaving.ManaweavingPattern;
import com.mna.recipes.manaweaving.ManaweavingPatternHelper;
import com.mna.recipes.manaweaving.ManaweavingRecipe;
import com.mna.tools.render.GuiRenderUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import org.apache.commons.lang3.mutable.MutableInt;

public class RecipeManaweaving extends RecipeRendererBase implements ICyclingRecipeRenderer<RecipeManaweaving> {

    private ManaweavingRecipe[] patterns;

    public RecipeManaweaving(int x, int y) {
        super(x, y);
    }

    @Override
    public void init_cycling(ResourceLocation[] rLocs) {
        ArrayList<ManaweavingRecipe> patternList = new ArrayList();
        for (ResourceLocation rLoc : rLocs) {
            Optional<? extends Recipe<?>> pattern = this.resolveRecipe(rLoc);
            if (pattern.isPresent() && pattern.get() instanceof ManaweavingRecipe) {
                patternList.add((ManaweavingRecipe) pattern.get());
            }
        }
        this.patterns = (ManaweavingRecipe[]) patternList.toArray(new ManaweavingRecipe[0]);
    }

    @Override
    public void init_internal(ResourceLocation recipeLocation) {
        Optional<? extends Recipe<?>> pattern = this.resolveRecipe(recipeLocation);
        if (pattern.isPresent() && pattern.get() instanceof ManaweavingRecipe) {
            this.patterns = new ManaweavingRecipe[] { (ManaweavingRecipe) pattern.get() };
        } else {
            this.patterns = null;
        }
    }

    private Optional<? extends Recipe<?>> resolveRecipe(ResourceLocation recipeLocation) {
        Optional<? extends Recipe<?>> resolved = this.minecraft.level.getRecipeManager().byKey(recipeLocation);
        if (!resolved.isPresent()) {
            if (recipeLocation.getNamespace().equals("mna") && !recipeLocation.getPath().startsWith("manaweaving/")) {
                recipeLocation = new ResourceLocation(recipeLocation.getNamespace(), "manaweaving/" + recipeLocation.getPath());
            }
            resolved = this.minecraft.level.getRecipeManager().byKey(recipeLocation);
        }
        return resolved;
    }

    @Override
    protected ResourceLocation backgroundTexture() {
        return GuiTextures.Recipe.MANAWEAVING_ALTAR;
    }

    @Override
    protected void drawForeground(GuiGraphics pGuiGraphics, int x, int y, int mouseX, int mouseY, float partialTicks) {
        if (this.patterns.length != 0) {
            int index = this.getIndex();
            ManaweavingRecipe pattern = this.patterns[index];
            if (pattern != null) {
                float patternScale = 0.1F;
                int startX = (int) ((float) (x + 31) / patternScale);
                int startY = (int) ((float) (y + 225) / patternScale);
                RenderSystem.setShaderColor(0.5F, 0.0F, 1.0F, 1.0F);
                for (int i = 0; i < pattern.getRequiredPatterns().length; i++) {
                    ManaweavingPattern p = ManaweavingPatternHelper.GetManaweavingRecipe(this.minecraft.level, pattern.getRequiredPatterns()[i]);
                    if (p != null) {
                        GuiRenderUtils.renderManaweavePattern(pGuiGraphics, startX, startY, patternScale, p);
                        startX += (int) Math.floor((double) (33.6F / patternScale));
                    }
                }
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                startX = (int) ((float) this.m_252754_() / this.scale + 8.0F);
                startY = (int) ((float) this.m_252907_() / this.scale + 44.0F);
                for (int ix = 0; ix < pattern.getRequiredItems().length; ix++) {
                    List<ItemStack> items = (List<ItemStack>) MATags.smartLookupItem(pattern.getRequiredItems()[ix]).stream().map(item -> new ItemStack(item)).collect(Collectors.toList());
                    if (items != null && items.size() != 0) {
                        if (ix == 0) {
                            this.renderItemStack(pGuiGraphics, items, startX + 68, startY + 33, 1.0F);
                        } else {
                            this.renderItemStack(pGuiGraphics, items, startX, startY, 1.0F);
                        }
                        startX += 34;
                        if (ix == 4) {
                            startX += 33;
                        }
                        if ((float) startX > (float) this.m_252754_() / this.scale + 112.0F) {
                            startY += 33;
                            startX = (int) ((float) this.m_252754_() / this.scale + 42.0F);
                        }
                    }
                }
                ItemStack output = pattern.getResultItem();
                this.renderItemStack(pGuiGraphics, output, (int) ((float) this.m_252754_() / this.scale + 162.0F), (int) ((float) this.m_252907_() / this.scale + 77.0F), 1.0F);
                this.renderByproducts(pGuiGraphics, this.m_5711_() - 73, this.m_93694_() / 2 - 18, pattern);
                Font fr = this.minecraft.font;
                String lineFormatted = I18n.get("gui.mna.shapeless");
                pGuiGraphics.drawString(fr, lineFormatted, x + 171 - fr.width(lineFormatted) / 2, y + 62, 4210752, false);
                int tier = pattern.getTier();
                MutableInt playerTier = new MutableInt(0);
                this.minecraft.player.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(p -> playerTier.setValue(p.getTier()));
                int color = tier <= playerTier.getValue() ? FastColor.ARGB32.color(255, 0, 128, 0) : FastColor.ARGB32.color(255, 255, 0, 0);
                Component name = Component.translatable(pattern.getResultItem().getDescriptionId().toString());
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
                RenderSystem.enableBlend();
            }
        }
    }

    @Override
    public int getTier() {
        if (this.patterns == null) {
            return 1;
        } else {
            int minTier = 5;
            for (ManaweavingRecipe pattern : this.patterns) {
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