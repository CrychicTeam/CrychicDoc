package mezz.jei.library.gui.ingredients;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotTooltipCallback;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IIngredientVisibility;
import mezz.jei.common.util.ImmutableRect2i;
import mezz.jei.common.util.SafeIngredientUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

public class RecipeSlot implements IRecipeSlotView, IRecipeSlotDrawable {

    private static final int MAX_DISPLAYED_INGREDIENTS = 100;

    private final IIngredientManager ingredientManager;

    private final RecipeIngredientRole role;

    private final CycleTimer cycleTimer;

    private final List<IRecipeSlotTooltipCallback> tooltipCallbacks = new ArrayList();

    private final RendererOverrides rendererOverrides;

    @Unmodifiable
    private List<Optional<ITypedIngredient<?>>> displayIngredients = List.of();

    @Unmodifiable
    private List<Optional<ITypedIngredient<?>>> allIngredients = List.of();

    private ImmutableRect2i rect;

    @Nullable
    private IDrawable background;

    @Nullable
    private IDrawable overlay;

    @Nullable
    private String slotName;

    public RecipeSlot(IIngredientManager ingredientManager, RecipeIngredientRole role, int xPos, int yPos, int ingredientCycleOffset) {
        this.ingredientManager = ingredientManager;
        this.rendererOverrides = new RendererOverrides();
        this.role = role;
        this.rect = new ImmutableRect2i(xPos, yPos, 16, 16);
        this.cycleTimer = new CycleTimer(ingredientCycleOffset);
    }

    @Unmodifiable
    @Override
    public Stream<ITypedIngredient<?>> getAllIngredients() {
        return this.allIngredients.stream().flatMap(Optional::stream);
    }

    @Override
    public boolean isEmpty() {
        return this.getAllIngredients().findAny().isEmpty();
    }

    @Override
    public <T> Stream<T> getIngredients(IIngredientType<T> ingredientType) {
        return this.getAllIngredients().map(i -> i.getIngredient(ingredientType)).flatMap(Optional::stream);
    }

    @Override
    public Optional<ITypedIngredient<?>> getDisplayedIngredient() {
        return this.cycleTimer.getCycledItem(this.displayIngredients);
    }

    @Override
    public <T> Optional<T> getDisplayedIngredient(IIngredientType<T> ingredientType) {
        return this.getDisplayedIngredient().flatMap(i -> i.getIngredient(ingredientType));
    }

    @Override
    public Optional<String> getSlotName() {
        return Optional.ofNullable(this.slotName);
    }

    @Override
    public RecipeIngredientRole getRole() {
        return this.role;
    }

    @Override
    public void drawHighlight(GuiGraphics guiGraphics, int color) {
        int x = this.rect.getX();
        int y = this.rect.getY();
        int width = this.rect.getWidth();
        int height = this.rect.getHeight();
        guiGraphics.fillGradient(RenderType.guiOverlay(), x, y, x + width, y + height, color, color, 0);
    }

    private <T> List<Component> getTooltip(ITypedIngredient<T> typedIngredient) {
        IIngredientType<T> ingredientType = typedIngredient.getType();
        IIngredientRenderer<T> ingredientRenderer = this.getIngredientRenderer(ingredientType);
        IIngredientHelper<T> ingredientHelper = this.ingredientManager.getIngredientHelper(ingredientType);
        List<Component> tooltip = SafeIngredientUtil.getTooltip(this.ingredientManager, ingredientRenderer, typedIngredient);
        for (IRecipeSlotTooltipCallback tooltipCallback : this.tooltipCallbacks) {
            tooltipCallback.onTooltip(this, tooltip);
        }
        List<T> ingredients = this.getIngredients(ingredientType).toList();
        ingredientHelper.getTagEquivalent(ingredients).ifPresent(tagEquivalent -> {
            MutableComponent acceptsAny = Component.translatable("jei.tooltip.recipe.tag", tagEquivalent);
            tooltip.add(acceptsAny.withStyle(ChatFormatting.GRAY));
        });
        return tooltip;
    }

    public void setBackground(IDrawable background) {
        this.background = background;
    }

    public void setOverlay(IDrawable overlay) {
        this.overlay = overlay;
    }

    public void set(List<Optional<ITypedIngredient<?>>> ingredients, Set<Integer> focusMatches, IIngredientVisibility ingredientVisibility) {
        this.allIngredients = List.copyOf(ingredients);
        if (!focusMatches.isEmpty()) {
            this.displayIngredients = focusMatches.stream().filter(i -> i < this.allIngredients.size()).map(i -> (Optional) this.allIngredients.get(i)).toList();
        } else {
            this.displayIngredients = this.allIngredients.stream().filter(i -> i.isEmpty() || ingredientVisibility.isIngredientVisible((ITypedIngredient) i.get())).limit(100L).toList();
            if (this.displayIngredients.isEmpty()) {
                this.displayIngredients = this.allIngredients.stream().limit(100L).toList();
            }
        }
    }

    @Override
    public void addTooltipCallback(IRecipeSlotTooltipCallback tooltipCallback) {
        this.tooltipCallbacks.add(tooltipCallback);
    }

    public <T> void addRenderOverride(IIngredientType<T> ingredientType, IIngredientRenderer<T> ingredientRenderer) {
        this.rendererOverrides.addOverride(ingredientType, ingredientRenderer);
        this.rect = new ImmutableRect2i(this.rect.getX(), this.rect.getY(), this.rendererOverrides.getIngredientWidth(), this.rendererOverrides.getIngredientHeight());
    }

    private <T> IIngredientRenderer<T> getIngredientRenderer(IIngredientType<T> ingredientType) {
        return (IIngredientRenderer<T>) Optional.of(this.rendererOverrides).flatMap(r -> r.getIngredientRenderer(ingredientType)).orElseGet(() -> this.ingredientManager.getIngredientRenderer(ingredientType));
    }

    @Override
    public void draw(GuiGraphics guiGraphics) {
        this.cycleTimer.onDraw();
        int x = this.rect.getX();
        int y = this.rect.getY();
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate((float) x, (float) y, 0.0F);
        if (this.background != null) {
            this.background.draw(guiGraphics);
        }
        RenderSystem.enableBlend();
        this.getDisplayedIngredient().ifPresent(ingredient -> this.drawIngredient(guiGraphics, ingredient));
        if (this.overlay != null) {
            RenderSystem.enableBlend();
            poseStack.pushPose();
            poseStack.translate(0.0F, 0.0F, 200.0F);
            this.overlay.draw(guiGraphics);
            poseStack.popPose();
        }
        RenderSystem.disableBlend();
        poseStack.popPose();
    }

    private <T> void drawIngredient(GuiGraphics guiGraphics, ITypedIngredient<T> typedIngredient) {
        IIngredientType<T> ingredientType = typedIngredient.getType();
        IIngredientRenderer<T> ingredientRenderer = this.getIngredientRenderer(ingredientType);
        SafeIngredientUtil.render(this.ingredientManager, ingredientRenderer, guiGraphics, typedIngredient);
    }

    @Override
    public void drawHoverOverlays(GuiGraphics guiGraphics) {
        this.drawHighlight(guiGraphics, -2130706433);
    }

    @Override
    public List<Component> getTooltip() {
        return (List<Component>) this.getDisplayedIngredient().map(this::getTooltip).orElseGet(List::of);
    }

    @Override
    public Rect2i getRect() {
        return this.rect.toMutable();
    }

    public void setSlotName(String slotName) {
        this.slotName = slotName;
    }
}