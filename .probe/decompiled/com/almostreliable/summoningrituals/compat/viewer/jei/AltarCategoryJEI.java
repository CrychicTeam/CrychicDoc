package com.almostreliable.summoningrituals.compat.viewer.jei;

import com.almostreliable.summoningrituals.Registration;
import com.almostreliable.summoningrituals.compat.viewer.common.AltarCategory;
import com.almostreliable.summoningrituals.compat.viewer.jei.ingredient.block.JEIBlockReferenceRenderer;
import com.almostreliable.summoningrituals.compat.viewer.jei.ingredient.item.JEIAltarRenderer;
import com.almostreliable.summoningrituals.compat.viewer.jei.ingredient.item.JEICatalystRenderer;
import com.almostreliable.summoningrituals.compat.viewer.jei.ingredient.mob.JEIMobRenderer;
import com.almostreliable.summoningrituals.recipe.AltarRecipe;
import com.almostreliable.summoningrituals.util.GameUtils;
import com.almostreliable.summoningrituals.util.TextUtils;
import com.almostreliable.summoningrituals.util.Utils;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class AltarCategoryJEI extends AltarCategory<IDrawable, IIngredientRenderer<ItemStack>> implements IRecipeCategory<AltarRecipe> {

    static final RecipeType<AltarRecipe> TYPE = new RecipeType<>(Utils.getRL("altar"), AltarRecipe.class);

    private final IDrawable background;

    private final JEIBlockReferenceRenderer blockReferenceRenderer;

    private final JEIMobRenderer mobRenderer;

    AltarCategoryJEI(IGuiHelper guiHelper) {
        super(guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, Registration.ALTAR_ITEM.get().getDefaultInstance()), new JEIAltarRenderer(20), new JEICatalystRenderer(16));
        this.background = guiHelper.drawableBuilder(TEXTURE, 0, 0, 172, 148).setTextureSize(188, 148).build();
        this.blockReferenceRenderer = new JEIBlockReferenceRenderer(20);
        this.mobRenderer = new JEIMobRenderer(16);
    }

    @Override
    public RecipeType<AltarRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    public void setRecipe(IRecipeLayoutBuilder builder, AltarRecipe recipe, IFocusGroup focuses) {
        if (recipe.getBlockBelow() != null) {
            builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 76, 61).setCustomRenderer(AlmostJEI.BLOCK_REFERENCE, this.blockReferenceRenderer).addIngredient(AlmostJEI.BLOCK_REFERENCE, recipe.getBlockBelow());
        }
        builder.addSlot(RecipeIngredientRole.INPUT, 78, 32).setCustomRenderer(VanillaTypes.ITEM_STACK, this.catalystRenderer).addIngredients(recipe.getCatalyst());
        handleInputs(0, 0, recipe, (x, y, inputs) -> builder.addSlot(RecipeIngredientRole.INPUT, x, y).addItemStacks(inputs), (x, y, mob, egg) -> {
            builder.addSlot(RecipeIngredientRole.INPUT, x, y).setCustomRenderer(AlmostJEI.MOB, this.mobRenderer).addIngredient(AlmostJEI.MOB, mob);
            if (egg != null) {
                builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addItemStack(egg.m_7968_());
            }
        });
        handleOutputs(0, 0, recipe, (x, y, output) -> builder.addSlot(RecipeIngredientRole.OUTPUT, x, y).addItemStack(output), (x, y, mob, egg) -> {
            builder.addSlot(RecipeIngredientRole.OUTPUT, x, y).setCustomRenderer(AlmostJEI.MOB, this.mobRenderer).addIngredient(AlmostJEI.MOB, mob);
            if (egg != null) {
                builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addItemStack(egg.m_7968_());
            }
        });
    }

    public void draw(AltarRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        PoseStack stack = guiGraphics.pose();
        stack.pushPose();
        float altarY = 53.0F - (float) (recipe.getBlockBelow() == null ? 0 : 4);
        stack.translate(76.0F, altarY, 0.0F);
        this.altarRenderer.render(guiGraphics, null);
        stack.popPose();
        this.drawLabel(guiGraphics, TextUtils.f("{}:", TextUtils.translateAsString("label", "outputs")), GameUtils.ANCHOR.BOTTOM_LEFT, 2, 128, 3580928);
        if (!recipe.getSacrifices().isEmpty()) {
            this.drawLabel(guiGraphics, TextUtils.f("{}:", TextUtils.translateAsString("label", "region")), GameUtils.ANCHOR.TOP_LEFT, 1, 1, 41727);
            this.drawLabel(guiGraphics, recipe.getSacrifices().getDisplayRegion(), GameUtils.ANCHOR.TOP_LEFT, 1, 11, 16777215);
        }
        List<AltarCategory.SpriteWidget> sprites = this.conditionSpriteWidgets.stream().filter(s -> s.test(recipe)).toList();
        int spriteOffset = 0;
        for (AltarCategory.SpriteWidget sprite : sprites) {
            sprite.render(guiGraphics, 0, spriteOffset);
            spriteOffset += 17;
        }
    }

    public List<Component> getTooltipStrings(AltarRecipe recipe, IRecipeSlotsView slotsView, double mX, double mY) {
        return this.getTooltip(recipe, 0, 0, mX, mY);
    }
}