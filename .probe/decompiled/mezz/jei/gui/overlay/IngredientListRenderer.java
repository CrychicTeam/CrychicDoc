package mezz.jei.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.runtime.IEditModeConfig;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.common.config.IClientToggleState;
import mezz.jei.common.util.ImmutableRect2i;
import mezz.jei.common.util.SafeIngredientUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;

public class IngredientListRenderer {

    private static final int BLACKLIST_COLOR = -65536;

    private final List<IngredientListSlot> slots = new ArrayList();

    private final ElementRenderersByType renderers = new ElementRenderersByType();

    private final IEditModeConfig editModeConfig;

    private final IClientToggleState toggleState;

    private final IIngredientManager ingredientManager;

    private int blocked = 0;

    public IngredientListRenderer(IEditModeConfig editModeConfig, IClientToggleState toggleState, IIngredientManager ingredientManager) {
        this.editModeConfig = editModeConfig;
        this.toggleState = toggleState;
        this.ingredientManager = ingredientManager;
    }

    public void clear() {
        this.slots.clear();
        this.renderers.clear();
        this.blocked = 0;
    }

    public int size() {
        return this.slots.size() - this.blocked;
    }

    public void add(IngredientListSlot ingredientListSlot) {
        this.slots.add(ingredientListSlot);
    }

    public Stream<IngredientListSlot> getSlots() {
        return this.slots.stream().filter(s -> !s.isBlocked());
    }

    public void set(int startIndex, List<ITypedIngredient<?>> ingredientList) {
        this.renderers.clear();
        this.blocked = 0;
        int i = startIndex;
        for (IngredientListSlot ingredientListSlot : this.slots) {
            if (ingredientListSlot.isBlocked()) {
                ingredientListSlot.clear();
                this.blocked++;
            } else {
                if (i >= ingredientList.size()) {
                    ingredientListSlot.clear();
                } else {
                    ITypedIngredient<?> ingredient = (ITypedIngredient<?>) ingredientList.get(i);
                    this.set(ingredientListSlot, ingredient);
                }
                i++;
            }
        }
    }

    private <V> void set(IngredientListSlot ingredientListSlot, ITypedIngredient<V> value) {
        ElementRenderer<V> renderer = new ElementRenderer<>(value);
        ingredientListSlot.setIngredientRenderer(renderer);
        IIngredientType<V> ingredientType = value.getType();
        this.renderers.put(ingredientType, renderer);
    }

    public void render(GuiGraphics guiGraphics) {
        for (IIngredientType<?> ingredientType : this.renderers.getTypes()) {
            this.renderIngredientType(guiGraphics, ingredientType);
        }
    }

    private <T> void renderIngredientType(GuiGraphics guiGraphics, IIngredientType<T> ingredientType) {
        Collection<ElementRenderer<T>> slots = this.renderers.get(ingredientType);
        IIngredientRenderer<T> ingredientRenderer = this.ingredientManager.getIngredientRenderer(ingredientType);
        for (ElementRenderer<T> slot : slots) {
            this.renderIngredient(guiGraphics, slot, ingredientRenderer);
        }
    }

    private <T> void renderIngredient(GuiGraphics guiGraphics, ElementRenderer<T> slot, IIngredientRenderer<T> ingredientRenderer) {
        ITypedIngredient<T> typedIngredient = slot.getTypedIngredient();
        ImmutableRect2i area = slot.getArea();
        int slotPadding = slot.getPadding();
        if (this.toggleState.isEditModeEnabled()) {
            renderEditMode(guiGraphics, area, slotPadding, this.editModeConfig, typedIngredient);
            RenderSystem.enableBlend();
        }
        int xPosition = area.getX() + slotPadding;
        int yPosition = area.getY() + slotPadding;
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate((float) xPosition, (float) yPosition, 0.0F);
        SafeIngredientUtil.render(this.ingredientManager, ingredientRenderer, guiGraphics, typedIngredient);
        poseStack.popPose();
    }

    private static <T> void renderEditMode(GuiGraphics guiGraphics, ImmutableRect2i area, int padding, IEditModeConfig editModeConfig, ITypedIngredient<T> typedIngredient) {
        if (editModeConfig.isIngredientHiddenUsingConfigFile(typedIngredient)) {
            guiGraphics.fill(RenderType.guiOverlay(), area.getX() + padding, area.getY() + padding, area.getX() + 16 + padding, area.getY() + 16 + padding, -65536);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}