package com.simibubi.create.compat.jei.category.sequencedAssembly;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.compat.jei.category.animations.AnimatedDeployer;
import com.simibubi.create.compat.jei.category.animations.AnimatedPress;
import com.simibubi.create.compat.jei.category.animations.AnimatedSaw;
import com.simibubi.create.compat.jei.category.animations.AnimatedSpout;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedRecipe;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.utility.Lang;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;

public abstract class SequencedAssemblySubCategory {

    private final int width;

    public SequencedAssemblySubCategory(int width) {
        this.width = width;
    }

    public int getWidth() {
        return this.width;
    }

    public void setRecipe(IRecipeLayoutBuilder builder, SequencedRecipe<?> recipe, IFocusGroup focuses, int x) {
    }

    public abstract void draw(SequencedRecipe<?> var1, GuiGraphics var2, double var3, double var5, int var7);

    public static class AssemblyCutting extends SequencedAssemblySubCategory {

        AnimatedSaw saw = new AnimatedSaw();

        public AssemblyCutting() {
            super(25);
        }

        @Override
        public void draw(SequencedRecipe<?> recipe, GuiGraphics graphics, double mouseX, double mouseY, int index) {
            PoseStack ms = graphics.pose();
            ms.pushPose();
            ms.translate(0.0F, 51.5F, 0.0F);
            ms.scale(0.6F, 0.6F, 0.6F);
            this.saw.draw(graphics, this.getWidth() / 2, 30);
            ms.popPose();
        }
    }

    public static class AssemblyDeploying extends SequencedAssemblySubCategory {

        AnimatedDeployer deployer = new AnimatedDeployer();

        public AssemblyDeploying() {
            super(25);
        }

        @Override
        public void setRecipe(IRecipeLayoutBuilder builder, SequencedRecipe<?> recipe, IFocusGroup focuses, int x) {
            IRecipeSlotBuilder slot = builder.addSlot(RecipeIngredientRole.INPUT, x + 4, 15).setBackground(CreateRecipeCategory.getRenderedSlot(), -1, -1).addIngredients(recipe.getRecipe().getIngredients().get(1));
            if (recipe.getAsAssemblyRecipe() instanceof DeployerApplicationRecipe deployerRecipe && deployerRecipe.shouldKeepHeldItem()) {
                slot.addTooltipCallback((recipeSlotView, tooltip) -> tooltip.add(1, Lang.translateDirect("recipe.deploying.not_consumed").withStyle(ChatFormatting.GOLD)));
            }
        }

        @Override
        public void draw(SequencedRecipe<?> recipe, GuiGraphics graphics, double mouseX, double mouseY, int index) {
            PoseStack ms = graphics.pose();
            this.deployer.offset = index;
            ms.pushPose();
            ms.translate(-7.0F, 50.0F, 0.0F);
            ms.scale(0.75F, 0.75F, 0.75F);
            this.deployer.draw(graphics, this.getWidth() / 2, 0);
            ms.popPose();
        }
    }

    public static class AssemblyPressing extends SequencedAssemblySubCategory {

        AnimatedPress press = new AnimatedPress(false);

        public AssemblyPressing() {
            super(25);
        }

        @Override
        public void draw(SequencedRecipe<?> recipe, GuiGraphics graphics, double mouseX, double mouseY, int index) {
            PoseStack ms = graphics.pose();
            this.press.offset = index;
            ms.pushPose();
            ms.translate(-5.0F, 50.0F, 0.0F);
            ms.scale(0.6F, 0.6F, 0.6F);
            this.press.draw(graphics, this.getWidth() / 2, 0);
            ms.popPose();
        }
    }

    public static class AssemblySpouting extends SequencedAssemblySubCategory {

        AnimatedSpout spout = new AnimatedSpout();

        public AssemblySpouting() {
            super(25);
        }

        @Override
        public void setRecipe(IRecipeLayoutBuilder builder, SequencedRecipe<?> recipe, IFocusGroup focuses, int x) {
            FluidIngredient fluidIngredient = recipe.getRecipe().getFluidIngredients().get(0);
            builder.addSlot(RecipeIngredientRole.INPUT, x + 4, 15).setBackground(CreateRecipeCategory.getRenderedSlot(), -1, -1).addIngredients(ForgeTypes.FLUID_STACK, CreateRecipeCategory.withImprovedVisibility(fluidIngredient.getMatchingFluidStacks())).addTooltipCallback(CreateRecipeCategory.addFluidTooltip(fluidIngredient.getRequiredAmount()));
        }

        @Override
        public void draw(SequencedRecipe<?> recipe, GuiGraphics graphics, double mouseX, double mouseY, int index) {
            PoseStack ms = graphics.pose();
            this.spout.offset = index;
            ms.pushPose();
            ms.translate(-7.0F, 50.0F, 0.0F);
            ms.scale(0.75F, 0.75F, 0.75F);
            this.spout.withFluids(recipe.getRecipe().getFluidIngredients().get(0).getMatchingFluidStacks()).draw(graphics, this.getWidth() / 2, 0);
            ms.popPose();
        }
    }
}