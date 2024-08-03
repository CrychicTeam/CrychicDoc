package com.simibubi.create.compat.jei.category;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.content.kinetics.deployer.ItemApplicationRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.utility.Lang;
import java.util.List;
import java.util.Optional;
import javax.annotation.ParametersAreNonnullByDefault;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

@ParametersAreNonnullByDefault
public class ItemApplicationCategory extends CreateRecipeCategory<ItemApplicationRecipe> {

    public ItemApplicationCategory(CreateRecipeCategory.Info<ItemApplicationRecipe> info) {
        super(info);
    }

    public void setRecipe(IRecipeLayoutBuilder builder, ItemApplicationRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 27, 38).setBackground(getRenderedSlot(), -1, -1).addIngredients(recipe.getProcessedItem());
        builder.addSlot(RecipeIngredientRole.INPUT, 51, 5).setBackground(getRenderedSlot(), -1, -1).addIngredients(recipe.getRequiredHeldItem()).addTooltipCallback(recipe.shouldKeepHeldItem() ? (view, tooltip) -> tooltip.add(1, Lang.translateDirect("recipe.deploying.not_consumed").withStyle(ChatFormatting.GOLD)) : (view, tooltip) -> {
        });
        List<ProcessingOutput> results = recipe.getRollableResults();
        boolean single = results.size() == 1;
        for (int i = 0; i < results.size(); i++) {
            ProcessingOutput output = (ProcessingOutput) results.get(i);
            int xOffset = i % 2 == 0 ? 0 : 19;
            int yOffset = i / 2 * -19;
            builder.addSlot(RecipeIngredientRole.OUTPUT, single ? 132 : 132 + xOffset, 38 + yOffset).setBackground(getRenderedSlot(output), -1, -1).addItemStack(output.getStack()).addTooltipCallback(addStochasticTooltip(output));
        }
    }

    public void draw(ItemApplicationRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        AllGuiTextures.JEI_SHADOW.render(graphics, 62, 47);
        AllGuiTextures.JEI_DOWN_ARROW.render(graphics, 74, 10);
        Optional<ItemStack> displayedIngredient = ((IRecipeSlotView) recipeSlotsView.getSlotViews().get(0)).getDisplayedIngredient(VanillaTypes.ITEM_STACK);
        if (!displayedIngredient.isEmpty()) {
            if (((ItemStack) displayedIngredient.get()).getItem() instanceof BlockItem blockItem) {
                BlockState state = blockItem.getBlock().defaultBlockState();
                PoseStack matrixStack = graphics.pose();
                matrixStack.pushPose();
                matrixStack.translate(74.0F, 51.0F, 100.0F);
                matrixStack.mulPose(Axis.XP.rotationDegrees(-15.5F));
                matrixStack.mulPose(Axis.YP.rotationDegrees(22.5F));
                byte scale = 20;
                GuiGameElement.of(state).lighting(AnimatedKinetics.DEFAULT_LIGHTING).scale((double) scale).render(graphics);
                matrixStack.popPose();
            }
        }
    }
}