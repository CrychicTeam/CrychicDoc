package com.simibubi.create.compat.jei.category;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.compat.jei.category.animations.AnimatedCrafter;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.utility.Components;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.IShapedRecipe;
import org.jetbrains.annotations.NotNull;

@ParametersAreNonnullByDefault
public class MechanicalCraftingCategory extends CreateRecipeCategory<CraftingRecipe> {

    private final AnimatedCrafter crafter = new AnimatedCrafter();

    static int maxSize = 100;

    public MechanicalCraftingCategory(CreateRecipeCategory.Info<CraftingRecipe> info) {
        super(info);
    }

    public void setRecipe(IRecipeLayoutBuilder builder, CraftingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.OUTPUT, 134, 81).addItemStack(getResultItem(recipe));
        int x = getXPadding(recipe);
        int y = getYPadding(recipe);
        float scale = getScale(recipe);
        IIngredientRenderer<ItemStack> renderer = new MechanicalCraftingCategory.CrafterIngredientRenderer(recipe);
        int i = 0;
        for (Ingredient ingredient : recipe.m_7527_()) {
            float f = 19.0F * scale;
            int xPosition = (int) ((float) (x + 1) + (float) (i % getWidth(recipe)) * f);
            int yPosition = (int) ((float) (y + 1) + (float) (i / getWidth(recipe)) * f);
            builder.addSlot(RecipeIngredientRole.INPUT, xPosition, yPosition).setCustomRenderer(VanillaTypes.ITEM_STACK, renderer).addIngredients(ingredient);
            i++;
        }
    }

    public static float getScale(CraftingRecipe recipe) {
        int w = getWidth(recipe);
        int h = getHeight(recipe);
        return Math.min(1.0F, (float) maxSize / (19.0F * (float) Math.max(w, h)));
    }

    public static int getYPadding(CraftingRecipe recipe) {
        return 53 - (int) ((double) (getScale(recipe) * (float) getHeight(recipe) * 19.0F) * 0.5);
    }

    public static int getXPadding(CraftingRecipe recipe) {
        return 53 - (int) ((double) (getScale(recipe) * (float) getWidth(recipe) * 19.0F) * 0.5);
    }

    private static int getWidth(CraftingRecipe recipe) {
        return recipe instanceof IShapedRecipe ? ((IShapedRecipe) recipe).getRecipeWidth() : 1;
    }

    private static int getHeight(CraftingRecipe recipe) {
        return recipe instanceof IShapedRecipe ? ((IShapedRecipe) recipe).getRecipeHeight() : 1;
    }

    public void draw(CraftingRecipe recipe, IRecipeSlotsView iRecipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        PoseStack matrixStack = graphics.pose();
        matrixStack.pushPose();
        float scale = getScale(recipe);
        matrixStack.translate((float) getXPadding(recipe), (float) getYPadding(recipe), 0.0F);
        for (int row = 0; row < getHeight(recipe); row++) {
            for (int col = 0; col < getWidth(recipe); col++) {
                if (!((Ingredient) recipe.m_7527_().get(row * getWidth(recipe) + col)).isEmpty()) {
                    matrixStack.pushPose();
                    matrixStack.translate((float) (col * 19) * scale, (float) (row * 19) * scale, 0.0F);
                    matrixStack.scale(scale, scale, scale);
                    AllGuiTextures.JEI_SLOT.render(graphics, 0, 0);
                    matrixStack.popPose();
                }
            }
        }
        matrixStack.popPose();
        AllGuiTextures.JEI_SLOT.render(graphics, 133, 80);
        AllGuiTextures.JEI_DOWN_ARROW.render(graphics, 128, 59);
        this.crafter.draw(graphics, 129, 25);
        matrixStack.pushPose();
        matrixStack.translate(0.0F, 0.0F, 300.0F);
        int amount = 0;
        for (Ingredient ingredient : recipe.m_7527_()) {
            if (Ingredient.EMPTY != ingredient) {
                amount++;
            }
        }
        graphics.drawString(Minecraft.getInstance().font, amount + "", 142, 39, 16777215);
        matrixStack.popPose();
    }

    private static final class CrafterIngredientRenderer implements IIngredientRenderer<ItemStack> {

        private final CraftingRecipe recipe;

        private final float scale;

        public CrafterIngredientRenderer(CraftingRecipe recipe) {
            this.recipe = recipe;
            this.scale = MechanicalCraftingCategory.getScale(recipe);
        }

        public void render(GuiGraphics graphics, @NotNull ItemStack ingredient) {
            PoseStack matrixStack = graphics.pose();
            matrixStack.pushPose();
            float scale = MechanicalCraftingCategory.getScale(this.recipe);
            matrixStack.scale(scale, scale, scale);
            if (ingredient != null) {
                PoseStack modelViewStack = RenderSystem.getModelViewStack();
                modelViewStack.pushPose();
                RenderSystem.applyModelViewMatrix();
                RenderSystem.enableDepthTest();
                Minecraft minecraft = Minecraft.getInstance();
                Font font = this.getFontRenderer(minecraft, ingredient);
                graphics.renderItem(ingredient, 0, 0);
                graphics.renderItemDecorations(font, ingredient, 0, 0, null);
                RenderSystem.disableBlend();
                modelViewStack.popPose();
                RenderSystem.applyModelViewMatrix();
            }
            matrixStack.popPose();
        }

        @Override
        public int getWidth() {
            return (int) (16.0F * this.scale);
        }

        @Override
        public int getHeight() {
            return (int) (16.0F * this.scale);
        }

        public List<Component> getTooltip(ItemStack ingredient, TooltipFlag tooltipFlag) {
            Minecraft minecraft = Minecraft.getInstance();
            Player player = minecraft.player;
            try {
                return ingredient.getTooltipLines(player, tooltipFlag);
            } catch (LinkageError | RuntimeException var8) {
                List<Component> list = new ArrayList();
                MutableComponent crash = Components.translatable("jei.tooltip.error.crash");
                list.add(crash.withStyle(ChatFormatting.RED));
                return list;
            }
        }
    }
}