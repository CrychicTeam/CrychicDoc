package mezz.jei.library.gui.recipes;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import mezz.jei.api.gui.IRecipeLayoutDrawable;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IModIdHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryDecorator;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IIngredientVisibility;
import mezz.jei.common.gui.TooltipRenderer;
import mezz.jei.common.gui.elements.DrawableNineSliceTexture;
import mezz.jei.common.gui.textures.Textures;
import mezz.jei.common.util.ImmutableRect2i;
import mezz.jei.common.util.MathUtil;
import mezz.jei.library.gui.ingredients.RecipeSlot;
import mezz.jei.library.gui.ingredients.RecipeSlots;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

public class RecipeLayout<R> implements IRecipeLayoutDrawable<R> {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final int RECIPE_BORDER_PADDING = 4;

    public static final int RECIPE_TRANSFER_BUTTON_SIZE = 13;

    private final int ingredientCycleOffset = (int) (Math.random() * 10000.0 % 2.147483647E9);

    private final IRecipeCategory<R> recipeCategory;

    private final Collection<IRecipeCategoryDecorator<R>> recipeCategoryDecorators;

    private final IIngredientManager ingredientManager;

    private final IModIdHelper modIdHelper;

    private final Textures textures;

    private final RecipeSlots recipeSlots;

    private final R recipe;

    private final DrawableNineSliceTexture recipeBorder;

    private ImmutableRect2i recipeTransferButtonArea;

    @Nullable
    private ShapelessIcon shapelessIcon;

    private int posX;

    private int posY;

    public static <T> Optional<IRecipeLayoutDrawable<T>> create(IRecipeCategory<T> recipeCategory, Collection<IRecipeCategoryDecorator<T>> decorators, T recipe, IFocusGroup focuses, IIngredientManager ingredientManager, IIngredientVisibility ingredientVisibility, IModIdHelper modIdHelper, Textures textures) {
        RecipeLayout<T> recipeLayout = new RecipeLayout<>(recipeCategory, decorators, recipe, ingredientManager, modIdHelper, textures);
        if (recipeLayout.setRecipeLayout(recipeCategory, recipe, focuses, ingredientVisibility)) {
            ResourceLocation recipeName = recipeCategory.getRegistryName(recipe);
            if (recipeName != null) {
                addOutputSlotTooltip(recipeLayout, recipeName, modIdHelper);
            }
            return Optional.of(recipeLayout);
        } else {
            return Optional.empty();
        }
    }

    private boolean setRecipeLayout(IRecipeCategory<R> recipeCategory, R recipe, IFocusGroup focuses, IIngredientVisibility ingredientVisibility) {
        RecipeLayoutBuilder builder = new RecipeLayoutBuilder(this.ingredientManager, this.ingredientCycleOffset);
        try {
            recipeCategory.setRecipe(builder, recipe, focuses);
            if (builder.isUsed()) {
                builder.setRecipeLayout(this, focuses, ingredientVisibility);
                return true;
            }
        } catch (LinkageError | RuntimeException var7) {
            LOGGER.error("Error caught from Recipe Category: {}", recipeCategory.getRecipeType().getUid(), var7);
        }
        return false;
    }

    private static void addOutputSlotTooltip(RecipeLayout<?> recipeLayout, ResourceLocation recipeName, IModIdHelper modIdHelper) {
        RecipeSlots recipeSlots = recipeLayout.recipeSlots;
        List<RecipeSlot> outputSlots = recipeSlots.getSlots().stream().filter(r -> r.getRole() == RecipeIngredientRole.OUTPUT).toList();
        if (!outputSlots.isEmpty()) {
            OutputSlotTooltipCallback callback = new OutputSlotTooltipCallback(recipeName, modIdHelper, recipeLayout.ingredientManager);
            for (RecipeSlot outputSlot : outputSlots) {
                outputSlot.addTooltipCallback(callback);
            }
        }
    }

    public RecipeLayout(IRecipeCategory<R> recipeCategory, Collection<IRecipeCategoryDecorator<R>> recipeCategoryDecorators, R recipe, IIngredientManager ingredientManager, IModIdHelper modIdHelper, Textures textures) {
        this.recipeCategory = recipeCategory;
        this.recipeCategoryDecorators = recipeCategoryDecorators;
        this.ingredientManager = ingredientManager;
        this.modIdHelper = modIdHelper;
        this.textures = textures;
        this.recipeSlots = new RecipeSlots();
        int width = recipeCategory.getWidth();
        int height = recipeCategory.getHeight();
        int buttonX = width + 4 + 2;
        int buttonY = height - 13;
        this.recipeTransferButtonArea = new ImmutableRect2i(buttonX, buttonY, 13, 13);
        this.recipe = recipe;
        this.recipeBorder = textures.getRecipeBackground();
    }

    @Override
    public void setPosition(int posX, int posY) {
        int xDiff = posX - this.posX;
        int yDiff = posY - this.posY;
        this.recipeTransferButtonArea = new ImmutableRect2i(this.recipeTransferButtonArea.getX() + xDiff, this.recipeTransferButtonArea.getY() + yDiff, this.recipeTransferButtonArea.getWidth(), this.recipeTransferButtonArea.getHeight());
        this.posX = posX;
        this.posY = posY;
    }

    @Override
    public void drawRecipe(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        IDrawable background = this.recipeCategory.getBackground();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int recipeMouseX = mouseX - this.posX;
        int recipeMouseY = mouseY - this.posY;
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate((float) this.posX, (float) this.posY, 0.0F);
        int width = this.recipeCategory.getWidth() + 8;
        int height = this.recipeCategory.getHeight() + 8;
        this.recipeBorder.draw(guiGraphics, -4, -4, width, height);
        background.draw(guiGraphics);
        poseStack.pushPose();
        this.recipeCategory.draw(this.recipe, this.recipeSlots.getView(), guiGraphics, (double) recipeMouseX, (double) recipeMouseY);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
        for (IRecipeCategoryDecorator<R> decorator : this.recipeCategoryDecorators) {
            poseStack.pushPose();
            decorator.draw(this.recipe, this.recipeCategory, this.recipeSlots.getView(), guiGraphics, (double) recipeMouseX, (double) recipeMouseY);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            poseStack.popPose();
        }
        if (this.shapelessIcon != null) {
            this.shapelessIcon.draw(guiGraphics);
        }
        this.recipeSlots.draw(guiGraphics);
        poseStack.popPose();
        RenderSystem.disableBlend();
    }

    @Override
    public void drawOverlays(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int recipeMouseX = mouseX - this.posX;
        int recipeMouseY = mouseY - this.posY;
        IRecipeSlotDrawable hoveredSlot = (IRecipeSlotDrawable) this.recipeSlots.getHoveredSlot((double) recipeMouseX, (double) recipeMouseY).orElse(null);
        RenderSystem.disableBlend();
        PoseStack poseStack = guiGraphics.pose();
        if (hoveredSlot != null) {
            poseStack.pushPose();
            poseStack.translate((float) this.posX, (float) this.posY, 0.0F);
            hoveredSlot.drawHoverOverlays(guiGraphics);
            poseStack.popPose();
            hoveredSlot.getDisplayedIngredient().ifPresent(i -> {
                List<Component> tooltip = hoveredSlot.getTooltip();
                tooltip = this.modIdHelper.addModNameToIngredientTooltip(tooltip, i);
                TooltipRenderer.drawHoveringText(guiGraphics, tooltip, mouseX, mouseY, i, this.ingredientManager);
            });
        } else if (this.isMouseOver((double) mouseX, (double) mouseY)) {
            List<Component> tooltipStrings = this.recipeCategory.getTooltipStrings(this.recipe, this.recipeSlots.getView(), (double) recipeMouseX, (double) recipeMouseY);
            for (IRecipeCategoryDecorator<R> decorator : this.recipeCategoryDecorators) {
                tooltipStrings = decorator.decorateExistingTooltips(tooltipStrings, this.recipe, this.recipeCategory, this.recipeSlots.getView(), (double) recipeMouseX, (double) recipeMouseY);
            }
            if (tooltipStrings.isEmpty() && this.shapelessIcon != null) {
                tooltipStrings = this.shapelessIcon.getTooltipStrings(recipeMouseX, recipeMouseY);
            }
            if (!tooltipStrings.isEmpty()) {
                TooltipRenderer.drawHoveringText(guiGraphics, tooltipStrings, mouseX, mouseY);
            }
        }
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return MathUtil.contains(this.getRect(), mouseX, mouseY);
    }

    @Override
    public Rect2i getRect() {
        return new Rect2i(this.posX, this.posY, this.recipeCategory.getWidth(), this.recipeCategory.getHeight());
    }

    @Override
    public <T> Optional<T> getIngredientUnderMouse(int mouseX, int mouseY, IIngredientType<T> ingredientType) {
        return this.getRecipeSlotUnderMouse((double) mouseX, (double) mouseY).flatMap(slot -> slot.getDisplayedIngredient(ingredientType));
    }

    @Override
    public Optional<IRecipeSlotDrawable> getRecipeSlotUnderMouse(double mouseX, double mouseY) {
        double recipeMouseX = mouseX - (double) this.posX;
        double recipeMouseY = mouseY - (double) this.posY;
        return this.recipeSlots.getHoveredSlot(recipeMouseX, recipeMouseY).map(r -> r);
    }

    public void moveRecipeTransferButton(int posX, int posY) {
        this.recipeTransferButtonArea = new ImmutableRect2i(posX + this.posX, posY + this.posY, this.recipeTransferButtonArea.getWidth(), this.recipeTransferButtonArea.getHeight());
    }

    public void setShapeless() {
        this.shapelessIcon = new ShapelessIcon(this.textures);
        int categoryWidth = this.recipeCategory.getWidth();
        int x = categoryWidth - this.shapelessIcon.getIcon().getWidth();
        int y = 0;
        this.shapelessIcon.setPosition(x, y);
    }

    public void setShapeless(int shapelessX, int shapelessY) {
        this.shapelessIcon = new ShapelessIcon(this.textures);
        this.shapelessIcon.setPosition(shapelessX, shapelessY);
    }

    @Override
    public IRecipeCategory<R> getRecipeCategory() {
        return this.recipeCategory;
    }

    @Override
    public Rect2i getRecipeTransferButtonArea() {
        return this.recipeTransferButtonArea.toMutable();
    }

    @Override
    public IRecipeSlotsView getRecipeSlotsView() {
        return this.recipeSlots.getView();
    }

    @Override
    public R getRecipe() {
        return this.recipe;
    }

    public RecipeSlots getRecipeSlots() {
        return this.recipeSlots;
    }
}