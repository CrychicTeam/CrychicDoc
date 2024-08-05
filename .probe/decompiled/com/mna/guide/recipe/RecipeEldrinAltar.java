package com.mna.guide.recipe;

import com.mna.api.guidebook.RecipeRendererBase;
import com.mna.api.tools.MATags;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.gui.GuiTextures;
import com.mna.recipes.eldrin.EldrinAltarRecipe;
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
import org.apache.commons.lang3.mutable.MutableInt;

public class RecipeEldrinAltar extends RecipeRendererBase implements ICyclingRecipeRenderer<RecipeEldrinAltar> {

    private EldrinAltarRecipe[] patterns;

    public RecipeEldrinAltar(int xIn, int yIn) {
        super(xIn, yIn);
    }

    @Override
    public void init_cycling(ResourceLocation[] rLocs) {
        ArrayList<EldrinAltarRecipe> patternList = new ArrayList();
        for (ResourceLocation rLoc : rLocs) {
            Optional<? extends Recipe<?>> pattern = this.resolveRecipe(rLoc);
            if (pattern.isPresent() && pattern.get() instanceof EldrinAltarRecipe) {
                patternList.add((EldrinAltarRecipe) pattern.get());
            }
        }
        this.patterns = (EldrinAltarRecipe[]) patternList.toArray(new EldrinAltarRecipe[0]);
    }

    @Override
    public void init_internal(ResourceLocation recipeLocation) {
        Optional<? extends Recipe<?>> pattern = this.resolveRecipe(recipeLocation);
        if (pattern.isPresent() && pattern.get() instanceof EldrinAltarRecipe) {
            this.patterns = new EldrinAltarRecipe[] { (EldrinAltarRecipe) pattern.get() };
        } else {
            this.patterns = null;
        }
    }

    private Optional<? extends Recipe<?>> resolveRecipe(ResourceLocation recipeLocation) {
        Optional<? extends Recipe<?>> resolved = this.minecraft.level.getRecipeManager().byKey(recipeLocation);
        if (!resolved.isPresent()) {
            if (recipeLocation.getNamespace().equals("mna") && !recipeLocation.getPath().startsWith("eldrin_altar/")) {
                recipeLocation = new ResourceLocation(recipeLocation.getNamespace(), "eldrin_altar/" + recipeLocation.getPath());
            }
            resolved = this.minecraft.level.getRecipeManager().byKey(recipeLocation);
        }
        return resolved;
    }

    @Override
    protected ResourceLocation backgroundTexture() {
        return GuiTextures.Recipe.ELDRIN;
    }

    @Override
    protected void drawForeground(GuiGraphics pGuiGraphics, int x, int y, int mouseX, int mouseY, float partialTicks) {
        int index = this.getIndex();
        if (this.patterns.length != 0) {
            EldrinAltarRecipe pattern = this.patterns[index];
            if (pattern != null) {
                int cX = (int) ((float) this.m_252754_() / this.scale + (float) (this.f_93618_ / 2 - 23));
                int cY = (int) ((float) this.m_252907_() / this.scale + (float) (this.f_93619_ / 2 + 25));
                float radians = (float) (-Math.PI / 2);
                float radianStep = (float) (Math.PI / 4);
                int ingredDist = 64;
                int affStep = 30;
                int affY = (int) ((float) this.m_252907_() / this.scale + (float) (this.f_93619_ - 30));
                Font fr = this.minecraft.font;
                ItemStack output = pattern.getResultItem();
                this.renderItemStack(pGuiGraphics, output, cX, (int) ((float) this.m_252907_() / this.scale + 45.0F), 1.0F);
                int loopCount = 0;
                for (int i = 0; i < pattern.getRequiredItems().length; i++) {
                    List<ItemStack> items = (List<ItemStack>) MATags.smartLookupItem(pattern.getRequiredItems()[i]).stream().map(item -> new ItemStack(item)).collect(Collectors.toList());
                    if (items != null && items.size() != 0) {
                        int ingredX = cX + (int) Math.round(Math.cos((double) radians) * (double) ingredDist);
                        int ingredY = cY + (int) Math.round(Math.sin((double) radians) * (double) ingredDist);
                        if (loopCount++ == 0) {
                            ingredX = cX;
                            ingredY = cY;
                        }
                        if (loopCount == 2 || loopCount == 4) {
                            ingredX = (int) ((float) ingredX + 6.0F * this.scale);
                        } else if (loopCount == 6 || loopCount == 8) {
                            ingredX = (int) ((float) ingredX - 6.0F * this.scale);
                        }
                        this.renderItemStack(pGuiGraphics, items, ingredX, ingredY, 1.0F);
                        radians += radianStep;
                    }
                }
                MutableInt count = new MutableInt(0);
                int affPosX = (int) ((float) this.m_252754_() / this.scale + (float) this.f_93618_ - 40.0F);
                pattern.getPowerRequirements().forEach((a, v) -> {
                    ItemStack guiStack = (ItemStack) GuiTextures.affinityIcons.get(a);
                    int rY = affY - count.getAndIncrement() * affStep - 5;
                    this.renderItemStack(pGuiGraphics, guiStack, affPosX, rY, 1.0F);
                    String vText = String.format("%.0f", v);
                    pGuiGraphics.drawString(fr, vText, affPosX, rY + 16, FastColor.ARGB32.color(255, a.getColor()[0], a.getColor()[1], a.getColor()[2]), false);
                });
                this.renderByproducts(pGuiGraphics, this.m_5711_() / 2 + 10, 26, pattern, true);
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
            }
        }
    }

    @Override
    public int getTier() {
        if (this.patterns == null) {
            return 1;
        } else {
            int minTier = 5;
            for (EldrinAltarRecipe pattern : this.patterns) {
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