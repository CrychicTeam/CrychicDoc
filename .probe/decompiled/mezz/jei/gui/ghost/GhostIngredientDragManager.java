package mezz.jei.gui.ghost;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IScreenHelper;
import mezz.jei.common.config.IClientToggleState;
import mezz.jei.common.input.IClickableIngredientInternal;
import mezz.jei.common.util.ImmutableRect2i;
import mezz.jei.gui.input.IDragHandler;
import mezz.jei.gui.input.IRecipeFocusSource;
import mezz.jei.gui.input.UserInput;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class GhostIngredientDragManager {

    private final IRecipeFocusSource source;

    private final IScreenHelper screenHelper;

    private final IIngredientManager ingredientManager;

    private final IClientToggleState toggleState;

    private final List<GhostIngredientReturning<?>> ghostIngredientsReturning = new ArrayList();

    @Nullable
    private GhostIngredientDrag<?> ghostIngredientDrag;

    @Nullable
    private ITypedIngredient<?> hoveredIngredient;

    @Nullable
    private List<Rect2i> hoveredTargetAreas;

    public GhostIngredientDragManager(IRecipeFocusSource source, IScreenHelper screenHelper, IIngredientManager ingredientManager, IClientToggleState toggleState) {
        this.source = source;
        this.screenHelper = screenHelper;
        this.ingredientManager = ingredientManager;
        this.toggleState = toggleState;
    }

    public void drawTooltips(Minecraft minecraft, GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (!(minecraft.screen instanceof AbstractContainerScreen)) {
            this.drawGhostIngredientHighlights(minecraft, guiGraphics, mouseX, mouseY);
        }
        if (this.ghostIngredientDrag != null) {
            this.ghostIngredientDrag.drawItem(guiGraphics, mouseX, mouseY);
        }
        this.ghostIngredientsReturning.forEach(returning -> returning.drawItem(guiGraphics));
        this.ghostIngredientsReturning.removeIf(GhostIngredientReturning::isComplete);
    }

    public void drawOnForeground(Minecraft minecraft, GuiGraphics guiGraphics, int mouseX, int mouseY) {
        this.drawGhostIngredientHighlights(minecraft, guiGraphics, mouseX, mouseY);
    }

    private void drawGhostIngredientHighlights(Minecraft minecraft, GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (this.ghostIngredientDrag != null) {
            this.ghostIngredientDrag.drawTargets(guiGraphics, mouseX, mouseY);
        } else {
            ITypedIngredient<?> hovered = (ITypedIngredient<?>) this.source.getIngredientUnderMouse((double) mouseX, (double) mouseY).map(IClickableIngredientInternal::getTypedIngredient).findFirst().orElse(null);
            if (!equals(hovered, this.hoveredIngredient)) {
                this.hoveredIngredient = hovered;
                this.hoveredTargetAreas = null;
                Screen currentScreen = minecraft.screen;
                if (currentScreen != null && hovered != null) {
                    this.screenHelper.getGhostIngredientHandler(currentScreen).filter(IGhostIngredientHandler::shouldHighlightTargets).ifPresent(handler -> this.hoveredTargetAreas = handler.getTargetsTyped(currentScreen, hovered, false).stream().map(IGhostIngredientHandler.Target::getArea).toList());
                }
            }
            if (this.hoveredTargetAreas != null && !this.toggleState.isCheatItemsEnabled()) {
                GhostIngredientDrag.drawTargets(guiGraphics, mouseX, mouseY, this.hoveredTargetAreas);
            }
        }
    }

    private static boolean equals(@Nullable ITypedIngredient<?> a, @Nullable ITypedIngredient<?> b) {
        if (a == b) {
            return true;
        } else {
            return a != null && b != null ? a.getIngredient() == b.getIngredient() : false;
        }
    }

    public void stopDrag() {
        if (this.ghostIngredientDrag != null) {
            this.ghostIngredientDrag.stop();
            this.ghostIngredientDrag = null;
        }
        this.hoveredIngredient = null;
        this.hoveredTargetAreas = null;
    }

    private <T extends Screen, V> boolean handleClickGhostIngredient(T currentScreen, IClickableIngredientInternal<V> clicked, UserInput input) {
        return (Boolean) this.screenHelper.getGhostIngredientHandler(currentScreen).map(handler -> {
            ITypedIngredient<V> ingredient = clicked.getTypedIngredient();
            IIngredientType<V> type = ingredient.getType();
            List<IGhostIngredientHandler.Target<V>> targets = handler.getTargetsTyped(currentScreen, ingredient, true);
            if (targets.isEmpty()) {
                return false;
            } else {
                IIngredientRenderer<V> ingredientRenderer = this.ingredientManager.getIngredientRenderer(type);
                ImmutableRect2i clickedArea = clicked.getArea();
                this.ghostIngredientDrag = new GhostIngredientDrag<>(handler, targets, this.ingredientManager, ingredientRenderer, ingredient, input.getMouseX(), input.getMouseY(), clickedArea);
                return true;
            }
        }).orElse(false);
    }

    public IDragHandler createDragHandler() {
        return new GhostIngredientDragManager.DragHandler();
    }

    private class DragHandler implements IDragHandler {

        @Override
        public Optional<IDragHandler> handleDragStart(Screen screen, UserInput input) {
            Minecraft minecraft = Minecraft.getInstance();
            LocalPlayer player = minecraft.player;
            return player == null ? Optional.empty() : GhostIngredientDragManager.this.source.getIngredientUnderMouse(input.getMouseX(), input.getMouseY()).findFirst().flatMap(clicked -> {
                ItemStack mouseItem = player.f_36096_.getCarried();
                return mouseItem.isEmpty() && GhostIngredientDragManager.this.handleClickGhostIngredient(screen, clicked, input) ? Optional.of(this) : Optional.empty();
            });
        }

        @Override
        public boolean handleDragComplete(Screen screen, UserInput input) {
            if (GhostIngredientDragManager.this.ghostIngredientDrag == null) {
                return false;
            } else {
                boolean success = GhostIngredientDragManager.this.ghostIngredientDrag.onClick(input);
                double mouseX = input.getMouseX();
                double mouseY = input.getMouseY();
                if (!success && GhostIngredientDrag.farEnoughToDraw(GhostIngredientDragManager.this.ghostIngredientDrag, mouseX, mouseY)) {
                    GhostIngredientReturning.create(GhostIngredientDragManager.this.ghostIngredientDrag, mouseX, mouseY).ifPresent(GhostIngredientDragManager.this.ghostIngredientsReturning::add);
                }
                GhostIngredientDragManager.this.ghostIngredientDrag = null;
                GhostIngredientDragManager.this.hoveredTargetAreas = null;
                return success;
            }
        }

        @Override
        public void handleDragCanceled() {
            GhostIngredientDragManager.this.stopDrag();
        }
    }
}