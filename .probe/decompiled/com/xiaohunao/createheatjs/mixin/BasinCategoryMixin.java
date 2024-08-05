package com.xiaohunao.createheatjs.mixin;

import com.simibubi.create.compat.jei.category.BasinCategory;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.xiaohunao.createheatjs.HeatData;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ BasinCategory.class })
public abstract class BasinCategoryMixin extends CreateRecipeCategory<BasinRecipe> {

    public BasinCategoryMixin(CreateRecipeCategory.Info<BasinRecipe> info) {
        super(info);
    }

    @Inject(method = { "Lcom/simibubi/create/compat/jei/category/BasinCategory;setRecipe(Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;Lcom/simibubi/create/content/processing/basin/BasinRecipe;Lmezz/jei/api/recipe/IFocusGroup;)V" }, at = { @At(value = "INVOKE", ordinal = 0, target = "Lcom/simibubi/create/content/processing/basin/BasinRecipe;getRollableResults()Ljava/util/List;") }, remap = false, cancellable = true)
    private void onSetBurnerType(IRecipeLayoutBuilder builder, BasinRecipe recipe, IFocusGroup focuses, CallbackInfo ci) {
        int size = recipe.getRollableResults().size() + recipe.getFluidResults().size();
        int i = 0;
        for (ProcessingOutput result : recipe.getRollableResults()) {
            int xPosition = 142 - (size % 2 != 0 && i == size - 1 ? 0 : (i % 2 == 0 ? 10 : -9));
            int yPosition = -19 * (i / 2) + 51;
            builder.addSlot(RecipeIngredientRole.OUTPUT, xPosition, yPosition).setBackground(getRenderedSlot(result), -1, -1).addItemStack(result.getStack()).addTooltipCallback(addStochasticTooltip(result));
            i++;
        }
        for (FluidStack fluidResult : recipe.getFluidResults()) {
            int xPosition = 142 - (size % 2 != 0 && i == size - 1 ? 0 : (i % 2 == 0 ? 10 : -9));
            int yPosition = -19 * (i / 2) + 51;
            builder.addSlot(RecipeIngredientRole.OUTPUT, xPosition, yPosition).setBackground(getRenderedSlot(), -1, -1).addIngredient(ForgeTypes.FLUID_STACK, withImprovedVisibility(fluidResult)).addTooltipCallback(addFluidTooltip(fluidResult.getAmount()));
            i++;
        }
        ci.cancel();
    }

    @Inject(method = { "draw(Lcom/simibubi/create/content/processing/basin/BasinRecipe;Lmezz/jei/api/gui/ingredient/IRecipeSlotsView;Lnet/minecraft/client/gui/GuiGraphics;DD)V" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;IIIZ)I") })
    private void draw(BasinRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY, CallbackInfo ci) {
        HeatCondition heat = recipe.getRequiredHeat();
        String requiredHeat = heat.toString();
        if (HeatData.getHeatData(requiredHeat).isJeiTip()) {
            graphics.drawString(Minecraft.getInstance().font, Component.translatable("gui.create.jei.category.basin.heat." + requiredHeat.toLowerCase() + ".title"), 9, 0, heat.getColor(), false);
        }
    }
}