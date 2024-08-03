package mezz.jei.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import mezz.jei.api.helpers.IColorHelper;
import mezz.jei.api.helpers.IModIdHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.runtime.IEditModeConfig;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.common.config.IClientConfig;
import mezz.jei.common.config.IClientToggleState;
import mezz.jei.common.config.IIngredientFilterConfig;
import mezz.jei.common.config.IIngredientGridConfig;
import mezz.jei.common.input.IClickableIngredientInternal;
import mezz.jei.common.input.IInternalKeyMappings;
import mezz.jei.common.network.IConnectionToServer;
import mezz.jei.common.util.ImmutableRect2i;
import mezz.jei.common.util.ImmutableSize2i;
import mezz.jei.common.util.MathUtil;
import mezz.jei.gui.ingredients.GuiIngredientProperties;
import mezz.jei.gui.input.IRecipeFocusSource;
import mezz.jei.gui.input.IUserInputHandler;
import mezz.jei.gui.input.handlers.DeleteItemInputHandler;
import mezz.jei.gui.util.AlignmentUtil;
import mezz.jei.gui.util.CheatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;

public class IngredientGrid implements IRecipeFocusSource, IIngredientGrid {

    private static final int INGREDIENT_PADDING = 1;

    public static final int INGREDIENT_WIDTH = GuiIngredientProperties.getWidth(1);

    public static final int INGREDIENT_HEIGHT = GuiIngredientProperties.getHeight(1);

    private final IIngredientGridConfig gridConfig;

    private final IngredientListRenderer ingredientListRenderer;

    private final DeleteItemInputHandler deleteItemHandler;

    private final IngredientGridTooltipHelper tooltipHelper;

    private Set<ImmutableRect2i> guiExclusionAreas = Set.of();

    private ImmutableRect2i area = ImmutableRect2i.EMPTY;

    public IngredientGrid(IIngredientManager ingredientManager, IIngredientGridConfig gridConfig, IEditModeConfig editModeConfig, IIngredientFilterConfig ingredientFilterConfig, IClientConfig clientConfig, IClientToggleState toggleState, IModIdHelper modIdHelper, IConnectionToServer serverConnection, IInternalKeyMappings keyBindings, IColorHelper colorHelper, CheatUtil cheatUtil) {
        this.gridConfig = gridConfig;
        this.ingredientListRenderer = new IngredientListRenderer(editModeConfig, toggleState, ingredientManager);
        this.tooltipHelper = new IngredientGridTooltipHelper(ingredientManager, ingredientFilterConfig, toggleState, modIdHelper, keyBindings, colorHelper);
        this.deleteItemHandler = new DeleteItemInputHandler(this, toggleState, clientConfig, serverConnection, cheatUtil);
    }

    public IUserInputHandler getInputHandler() {
        return this.deleteItemHandler;
    }

    public int size() {
        return this.ingredientListRenderer.size();
    }

    public void updateBounds(ImmutableRect2i availableArea, Set<ImmutableRect2i> guiExclusionAreas) {
        this.ingredientListRenderer.clear();
        this.area = calculateBounds(this.gridConfig, availableArea);
        this.guiExclusionAreas = guiExclusionAreas;
        for (int y = this.area.getY(); y < this.area.getY() + this.area.getHeight(); y += INGREDIENT_HEIGHT) {
            for (int x = this.area.getX(); x < this.area.getX() + this.area.getWidth(); x += INGREDIENT_WIDTH) {
                IngredientListSlot ingredientListSlot = new IngredientListSlot(x, y, INGREDIENT_WIDTH, INGREDIENT_HEIGHT, 1);
                ImmutableRect2i stackArea = ingredientListSlot.getArea();
                boolean blocked = MathUtil.intersects(guiExclusionAreas, stackArea.expandBy(2));
                ingredientListSlot.setBlocked(blocked);
                this.ingredientListRenderer.add(ingredientListSlot);
            }
        }
    }

    public static ImmutableSize2i calculateSize(IIngredientGridConfig config, ImmutableRect2i availableArea) {
        int columns = Math.min(availableArea.getWidth() / INGREDIENT_WIDTH, config.getMaxColumns());
        int rows = Math.min(availableArea.getHeight() / INGREDIENT_HEIGHT, config.getMaxRows());
        return rows >= config.getMinRows() && columns >= config.getMinColumns() ? new ImmutableSize2i(columns * INGREDIENT_WIDTH, rows * INGREDIENT_HEIGHT) : ImmutableSize2i.EMPTY;
    }

    public static ImmutableRect2i calculateBounds(IIngredientGridConfig config, ImmutableRect2i availableArea) {
        ImmutableSize2i size = calculateSize(config, availableArea);
        return AlignmentUtil.align(size, availableArea, config.getHorizontalAlignment(), config.getVerticalAlignment());
    }

    public static IngredientGrid.SlotInfo calculateBlockedSlotPercentage(IIngredientGridConfig config, ImmutableRect2i availableArea, Set<ImmutableRect2i> exclusionAreas) {
        ImmutableRect2i area = calculateBounds(config, availableArea);
        int total = 0;
        int blocked = 0;
        for (int y = area.getY(); y < area.getY() + area.getHeight(); y += INGREDIENT_HEIGHT) {
            for (int x = area.getX(); x < area.getX() + area.getWidth(); x += INGREDIENT_WIDTH) {
                IngredientListSlot ingredientListSlot = new IngredientListSlot(x, y, INGREDIENT_WIDTH, INGREDIENT_HEIGHT, 1);
                ImmutableRect2i stackArea = ingredientListSlot.getArea();
                if (MathUtil.intersects(exclusionAreas, stackArea.expandBy(2))) {
                    blocked++;
                }
                total++;
            }
        }
        return new IngredientGrid.SlotInfo(total, blocked);
    }

    public ImmutableRect2i getArea() {
        return this.area;
    }

    public void draw(Minecraft minecraft, GuiGraphics guiGraphics, int mouseX, int mouseY) {
        RenderSystem.disableBlend();
        this.ingredientListRenderer.render(guiGraphics);
        if (this.isMouseOver((double) mouseX, (double) mouseY) && !this.deleteItemHandler.shouldDeleteItemOnClick(minecraft, (double) mouseX, (double) mouseY)) {
            this.ingredientListRenderer.getSlots().filter(s -> s.getArea().contains((double) mouseX, (double) mouseY)).filter(s -> s.getIngredientRenderer().isPresent()).findFirst().ifPresent(s -> drawHighlight(guiGraphics, s.getArea()));
        }
    }

    public static void drawHighlight(GuiGraphics guiGraphics, ImmutableRect2i area) {
        guiGraphics.fillGradient(RenderType.guiOverlay(), area.getX(), area.getY(), area.getX() + area.getWidth(), area.getY() + area.getHeight(), -2130706433, -2130706433, 0);
    }

    public void drawTooltips(Minecraft minecraft, GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (this.isMouseOver((double) mouseX, (double) mouseY)) {
            if (this.deleteItemHandler.shouldDeleteItemOnClick(minecraft, (double) mouseX, (double) mouseY)) {
                this.deleteItemHandler.drawTooltips(guiGraphics, mouseX, mouseY);
            } else {
                this.ingredientListRenderer.getSlots().filter(s -> s.isMouseOver((double) mouseX, (double) mouseY)).map(IngredientListSlot::getTypedIngredient).flatMap(Optional::stream).findFirst().ifPresent(ingredient -> this.tooltipHelper.drawTooltip(guiGraphics, mouseX, mouseY, ingredient));
            }
        }
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return this.area.contains(mouseX, mouseY) && this.guiExclusionAreas.stream().noneMatch(area -> area.contains(mouseX, mouseY));
    }

    @Override
    public Stream<IClickableIngredientInternal<?>> getIngredientUnderMouse(double mouseX, double mouseY) {
        return this.ingredientListRenderer.getSlots().filter(s -> s.isMouseOver(mouseX, mouseY)).map(IngredientListSlot::getIngredientRenderer).flatMap(Optional::stream);
    }

    public <T> Stream<T> getVisibleIngredients(IIngredientType<T> ingredientType) {
        return this.ingredientListRenderer.getSlots().map(IngredientListSlot::getTypedIngredient).flatMap(Optional::stream).map(i -> i.getIngredient(ingredientType)).flatMap(Optional::stream);
    }

    public void set(int firstItemIndex, List<ITypedIngredient<?>> ingredientList) {
        this.ingredientListRenderer.set(firstItemIndex, ingredientList);
    }

    public boolean hasRoom() {
        return !this.area.isEmpty();
    }

    public static record SlotInfo(int total, int blocked) {

        public float percentBlocked() {
            return (float) this.blocked / (float) this.total;
        }
    }
}