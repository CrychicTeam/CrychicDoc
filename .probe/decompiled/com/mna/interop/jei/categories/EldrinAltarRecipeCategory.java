package com.mna.interop.jei.categories;

import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.tools.MATags;
import com.mna.blocks.BlockInit;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.gui.GuiTextures;
import com.mna.interop.jei.MARecipeTypes;
import com.mna.recipes.eldrin.EldrinAltarRecipe;
import com.mna.tools.render.GuiRenderUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import org.apache.commons.lang3.mutable.MutableInt;

public class EldrinAltarRecipeCategory implements IRecipeCategory<EldrinAltarRecipe> {

    private final IDrawable background;

    private final IDrawable icon;

    private final String localizedName;

    private int xSize = 152;

    private int ySize = 177;

    public EldrinAltarRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(GuiTextures.Jei.ELDRIN, 0, 0, this.xSize, this.ySize).setTextureSize(152, 177).build();
        this.localizedName = I18n.get("gui.mna.jei.eldrin_altar");
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockInit.ELDRIN_ALTAR.get()));
    }

    @Override
    public Component getTitle() {
        return Component.translatable(this.localizedName);
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    public void setRecipe(IRecipeLayoutBuilder builder, EldrinAltarRecipe recipe, IFocusGroup focuses) {
        int cX = this.xSize / 2 - 14;
        int cY = this.ySize / 2 + 15;
        float radians = (float) (-Math.PI / 2);
        float radianStep = (float) (Math.PI / 4);
        int ingredDist = 44;
        builder.setShapeless();
        int count = 0;
        for (ResourceLocation rLoc : recipe.getRequiredItems()) {
            int ingredX = cX + (int) Math.round(Math.cos((double) radians) * (double) ingredDist);
            int ingredY = cY + (int) Math.round(Math.sin((double) radians) * (double) ingredDist);
            if (count++ == 0) {
                ingredX = cX;
                ingredY = cY;
            }
            if (count == 2 || count == 4) {
                ingredX += 4;
            } else if (count == 6 || count == 8) {
                ingredX -= 4;
            } else if (count == 5) {
                ingredY++;
            } else if (count == 9) {
                ingredY--;
            }
            builder.addSlot(RecipeIngredientRole.INPUT, ingredX - 8, ingredY).addIngredients(Ingredient.of(MATags.smartLookupItem(rLoc).stream().map(item -> {
                ItemStack stack = new ItemStack(item);
                if (stack.getItem() == Items.POTION && stack.getTag() == null) {
                    stack = PotionUtils.setPotion(stack, Potions.WATER);
                }
                return stack;
            })));
            radians += radianStep;
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 54, 29).addIngredient(VanillaTypes.ITEM_STACK, recipe.getResultItem());
    }

    public void draw(EldrinAltarRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics pGuiGraphics, double mouseX, double mouseY) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.font != null) {
            int tier = recipe.getTier();
            int playerTier = ((IPlayerProgression) mc.player.getCapability(PlayerProgressionProvider.PROGRESSION).resolve().get()).getTier();
            int color = tier <= playerTier ? FastColor.ARGB32.color(255, 0, 128, 0) : FastColor.ARGB32.color(255, 255, 0, 0);
            Component name = Component.translatable(recipe.getResultItem().getDescriptionId().toString());
            Component tierPrompt = Component.translatable("gui.mna.item-tier-short", tier);
            int stringWidth = mc.font.width(name);
            int textX = this.xSize / 2 - stringWidth / 2;
            int textY = 5;
            pGuiGraphics.drawString(mc.font, name, 0, textY, FastColor.ARGB32.color(255, 255, 255, 255), false);
            pGuiGraphics.drawString(mc.font, tierPrompt, 0, 15, color, false);
            GuiRenderUtils.renderFactionIcon(pGuiGraphics, recipe.getFactionRequirement(), textX + stringWidth + 3, textY);
            GuiRenderUtils.renderByproducts(pGuiGraphics, this.xSize / 2 + 1, 8, recipe, true);
            int affStep = 24;
            int affX = this.xSize - 27;
            int affY = mc.options.forceUnicodeFont.get() ? 22 : 24;
            MutableInt count = new MutableInt(0);
            recipe.getPowerRequirements().forEach((a, v) -> {
                int rY = affY + count.getAndIncrement() * affStep;
                String aText = a.name();
                float scale = mc.options.forceUnicodeFont.get() ? 1.0F : 0.7F;
                pGuiGraphics.pose().pushPose();
                pGuiGraphics.pose().scale(scale, scale, scale);
                pGuiGraphics.drawString(mc.font, aText, (int) ((float) affX / scale), (int) ((float) (rY + 6) / scale), FastColor.ARGB32.color(255, a.getColor()[0], a.getColor()[1], a.getColor()[2]), false);
                String vText = String.format("%.0f", v);
                pGuiGraphics.drawString(mc.font, vText, (int) ((float) affX / scale), (int) ((float) (rY + 16) / scale), FastColor.ARGB32.color(255, a.getColor()[0], a.getColor()[1], a.getColor()[2]), false);
                pGuiGraphics.pose().popPose();
            });
            RenderSystem.enableBlend();
        }
    }

    @Override
    public RecipeType<EldrinAltarRecipe> getRecipeType() {
        return MARecipeTypes.ELDRIN_ALTAR;
    }
}