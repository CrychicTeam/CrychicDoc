package mezz.jei.gui.overlay;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IScreenHelper;
import mezz.jei.common.config.IClientConfig;
import mezz.jei.common.config.IClientToggleState;
import mezz.jei.common.config.IIngredientGridConfig;
import mezz.jei.common.gui.elements.DrawableNineSliceTexture;
import mezz.jei.common.gui.textures.Textures;
import mezz.jei.common.input.IClickableIngredientInternal;
import mezz.jei.common.input.IInternalKeyMappings;
import mezz.jei.common.network.IConnectionToServer;
import mezz.jei.common.util.ImmutableRect2i;
import mezz.jei.common.util.MathUtil;
import mezz.jei.gui.PageNavigation;
import mezz.jei.gui.ghost.GhostIngredientDragManager;
import mezz.jei.gui.input.IDragHandler;
import mezz.jei.gui.input.IPaged;
import mezz.jei.gui.input.IRecipeFocusSource;
import mezz.jei.gui.input.IUserInputHandler;
import mezz.jei.gui.input.UserInput;
import mezz.jei.gui.input.handlers.CombinedInputHandler;
import mezz.jei.gui.input.handlers.LimitedAreaInputHandler;
import mezz.jei.gui.recipes.RecipesGui;
import mezz.jei.gui.util.CheatUtil;
import mezz.jei.gui.util.CommandUtil;
import mezz.jei.gui.util.MaximalRectangle;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.ItemStack;

public class IngredientGridWithNavigation implements IRecipeFocusSource {

    private static final int NAVIGATION_HEIGHT = 20;

    private static final int BORDER_MARGIN = 6;

    private static final int BORDER_PADDING = 5;

    private static final int INNER_PADDING = 2;

    private int firstItemIndex = 0;

    private final IngredientGridWithNavigation.IngredientGridPaged pageDelegate;

    private final PageNavigation navigation;

    private final IIngredientGridConfig gridConfig;

    private final CheatUtil cheatUtil;

    private final IClientToggleState toggleState;

    private final IClientConfig clientConfig;

    private final IngredientGrid ingredientGrid;

    private final IIngredientGridSource ingredientSource;

    private final DrawableNineSliceTexture background;

    private final DrawableNineSliceTexture slotBackground;

    private final CommandUtil commandUtil;

    private final GhostIngredientDragManager ghostIngredientDragManager;

    private ImmutableRect2i backgroundArea = ImmutableRect2i.EMPTY;

    private ImmutableRect2i slotBackgroundArea = ImmutableRect2i.EMPTY;

    private Set<ImmutableRect2i> guiExclusionAreas = Set.of();

    public IngredientGridWithNavigation(IIngredientGridSource ingredientSource, IngredientGrid ingredientGrid, IClientToggleState toggleState, IClientConfig clientConfig, IConnectionToServer serverConnection, IIngredientGridConfig gridConfig, DrawableNineSliceTexture background, DrawableNineSliceTexture slotBackground, Textures textures, CheatUtil cheatUtil, IScreenHelper screenHelper, IIngredientManager ingredientManager) {
        this.toggleState = toggleState;
        this.clientConfig = clientConfig;
        this.ingredientGrid = ingredientGrid;
        this.ingredientSource = ingredientSource;
        this.gridConfig = gridConfig;
        this.cheatUtil = cheatUtil;
        this.pageDelegate = new IngredientGridWithNavigation.IngredientGridPaged();
        this.navigation = new PageNavigation(this.pageDelegate, false, textures);
        this.background = background;
        this.slotBackground = slotBackground;
        this.commandUtil = new CommandUtil(clientConfig, serverConnection);
        this.ghostIngredientDragManager = new GhostIngredientDragManager(this, screenHelper, ingredientManager, toggleState);
        this.ingredientSource.addSourceListChangedListener(() -> this.updateLayout(true));
    }

    public boolean hasRoom() {
        return this.ingredientGrid.hasRoom();
    }

    public void updateLayout(boolean resetToFirstPage) {
        if (resetToFirstPage) {
            this.firstItemIndex = 0;
        }
        List<ITypedIngredient<?>> ingredientList = this.ingredientSource.getIngredientList();
        if (this.firstItemIndex >= ingredientList.size()) {
            this.firstItemIndex = 0;
        }
        this.ingredientGrid.set(this.firstItemIndex, ingredientList);
        this.navigation.updatePageNumber();
    }

    private static ImmutableRect2i avoidExclusionAreas(ImmutableRect2i availableArea, ImmutableRect2i estimatedNavigationArea, Set<ImmutableRect2i> guiExclusionAreas, IIngredientGridConfig gridConfig) {
        int maxDimension = Math.max(availableArea.getWidth(), availableArea.getHeight());
        int samplingScale = Math.max(IngredientGrid.INGREDIENT_HEIGHT / 2, maxDimension / 25);
        ImmutableRect2i largestSafeArea = (ImmutableRect2i) MaximalRectangle.getLargestRectangles(availableArea, guiExclusionAreas, samplingScale).max(Comparator.comparingInt(rect -> IngredientGrid.calculateSize(gridConfig, rect).getArea()).thenComparing(r -> r.getWidth() * r.getHeight())).orElse(ImmutableRect2i.EMPTY);
        boolean intersectsNavigationArea = guiExclusionAreas.stream().anyMatch(estimatedNavigationArea::intersects);
        if (intersectsNavigationArea) {
            return largestSafeArea;
        } else {
            IngredientGrid.SlotInfo slotInfo = IngredientGrid.calculateBlockedSlotPercentage(gridConfig, availableArea, guiExclusionAreas);
            IngredientGrid.SlotInfo safeSlotInfo = IngredientGrid.calculateBlockedSlotPercentage(gridConfig, largestSafeArea, guiExclusionAreas);
            return !((double) slotInfo.percentBlocked() > 0.25) && safeSlotInfo.total() <= slotInfo.total() ? availableArea : largestSafeArea;
        }
    }

    private void updateGridBounds(ImmutableRect2i availableArea, boolean navigationEnabled) {
        ImmutableRect2i availableGridArea = availableArea.insetBy(6);
        if (this.gridConfig.drawBackground()) {
            availableGridArea = availableGridArea.insetBy(7);
        }
        ImmutableRect2i estimatedGridArea = IngredientGrid.calculateBounds(this.gridConfig, availableGridArea);
        if (!estimatedGridArea.isEmpty()) {
            ImmutableRect2i slotBackgroundArea = calculateSlotBackgroundArea(estimatedGridArea, this.gridConfig);
            ImmutableRect2i estimatedNavigationArea = calculateNavigationArea(slotBackgroundArea, navigationEnabled);
            if (this.gridConfig.drawBackground()) {
                estimatedNavigationArea.expandBy(7);
            }
            availableGridArea = avoidExclusionAreas(availableArea, estimatedNavigationArea, this.guiExclusionAreas, this.gridConfig).insetBy(6).cropTop(22);
            if (this.gridConfig.drawBackground()) {
                availableGridArea = availableGridArea.insetBy(7);
            }
        }
        this.ingredientGrid.updateBounds(availableGridArea, this.guiExclusionAreas);
    }

    public void updateBounds(ImmutableRect2i availableArea, Set<ImmutableRect2i> guiExclusionAreas) {
        this.guiExclusionAreas = guiExclusionAreas;
        boolean navigationEnabled = switch(this.gridConfig.getButtonNavigationVisibility()) {
            case ENABLED ->
                true;
            case DISABLED ->
                false;
            case AUTO_HIDE ->
                {
                }
        };
        if (navigationEnabled) {
            this.updateGridBounds(availableArea, true);
        }
        if (this.hasRoom()) {
            this.slotBackgroundArea = calculateSlotBackgroundArea(this.ingredientGrid.getArea(), this.gridConfig);
            ImmutableRect2i navigationArea = calculateNavigationArea(this.slotBackgroundArea, navigationEnabled);
            this.navigation.updateBounds(navigationArea);
            this.backgroundArea = MathUtil.union(this.slotBackgroundArea, navigationArea);
            if (this.gridConfig.drawBackground()) {
                this.backgroundArea = this.backgroundArea.expandBy(5);
            }
        }
    }

    private static ImmutableRect2i calculateSlotBackgroundArea(ImmutableRect2i ingredientGridArea, IIngredientGridConfig gridConfig) {
        return gridConfig.drawBackground() ? ingredientGridArea.expandBy(2) : ingredientGridArea;
    }

    private static ImmutableRect2i calculateNavigationArea(ImmutableRect2i slotBackgroundArea, boolean navigationEnabled) {
        return !navigationEnabled ? ImmutableRect2i.EMPTY : slotBackgroundArea.keepTop(20).moveUp(22);
    }

    public ImmutableRect2i getBackgroundArea() {
        return this.backgroundArea;
    }

    public void draw(Minecraft minecraft, GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (this.gridConfig.drawBackground()) {
            this.background.draw(guiGraphics, this.backgroundArea);
            this.slotBackground.draw(guiGraphics, this.slotBackgroundArea);
        }
        this.ingredientGrid.draw(minecraft, guiGraphics, mouseX, mouseY);
        this.navigation.draw(minecraft, guiGraphics, mouseX, mouseY, partialTicks);
    }

    public void drawTooltips(Minecraft minecraft, GuiGraphics guiGraphics, int mouseX, int mouseY) {
        this.ghostIngredientDragManager.drawTooltips(minecraft, guiGraphics, mouseX, mouseY);
        this.ingredientGrid.drawTooltips(minecraft, guiGraphics, mouseX, mouseY);
    }

    public boolean isMouseOver(double mouseX, double mouseY) {
        return this.backgroundArea.contains(mouseX, mouseY) && this.guiExclusionAreas.stream().noneMatch(area -> area.contains(mouseX, mouseY));
    }

    public IUserInputHandler createInputHandler() {
        return new CombinedInputHandler(new IngredientGridWithNavigation.UserInputHandler(this.pageDelegate, this.ingredientGrid, this.toggleState, this.clientConfig, this.commandUtil, this.cheatUtil, this::isMouseOver), this.ingredientGrid.getInputHandler(), this.navigation.createInputHandler());
    }

    @Override
    public Stream<IClickableIngredientInternal<?>> getIngredientUnderMouse(double mouseX, double mouseY) {
        return this.ingredientGrid.getIngredientUnderMouse(mouseX, mouseY);
    }

    public <T> Stream<T> getVisibleIngredients(IIngredientType<T> ingredientType) {
        return this.ingredientGrid.getVisibleIngredients(ingredientType);
    }

    public boolean isEmpty() {
        return this.ingredientSource.getIngredientList().isEmpty();
    }

    public void close() {
        this.ghostIngredientDragManager.stopDrag();
    }

    public void drawOnForeground(Minecraft minecraft, GuiGraphics guiGraphics, int mouseX, int mouseY) {
        this.ghostIngredientDragManager.drawOnForeground(minecraft, guiGraphics, mouseX, mouseY);
    }

    public IDragHandler createDragHandler() {
        return this.ghostIngredientDragManager.createDragHandler();
    }

    private class IngredientGridPaged implements IPaged {

        @Override
        public boolean nextPage() {
            int itemsCount = IngredientGridWithNavigation.this.ingredientSource.getIngredientList().size();
            if (itemsCount > 0) {
                IngredientGridWithNavigation.this.firstItemIndex = IngredientGridWithNavigation.this.firstItemIndex + IngredientGridWithNavigation.this.ingredientGrid.size();
                if (IngredientGridWithNavigation.this.firstItemIndex >= itemsCount) {
                    IngredientGridWithNavigation.this.firstItemIndex = 0;
                }
                IngredientGridWithNavigation.this.updateLayout(false);
                return true;
            } else {
                IngredientGridWithNavigation.this.firstItemIndex = 0;
                IngredientGridWithNavigation.this.updateLayout(false);
                return false;
            }
        }

        @Override
        public boolean previousPage() {
            int itemsPerPage = IngredientGridWithNavigation.this.ingredientGrid.size();
            if (itemsPerPage == 0) {
                IngredientGridWithNavigation.this.firstItemIndex = 0;
                IngredientGridWithNavigation.this.updateLayout(false);
                return false;
            } else {
                int itemsCount = IngredientGridWithNavigation.this.ingredientSource.getIngredientList().size();
                int pageNum = IngredientGridWithNavigation.this.firstItemIndex / itemsPerPage;
                if (pageNum == 0) {
                    pageNum = itemsCount / itemsPerPage;
                } else {
                    pageNum--;
                }
                IngredientGridWithNavigation.this.firstItemIndex = itemsPerPage * pageNum;
                if (IngredientGridWithNavigation.this.firstItemIndex > 0 && IngredientGridWithNavigation.this.firstItemIndex == itemsCount) {
                    IngredientGridWithNavigation.this.firstItemIndex = itemsPerPage * --pageNum;
                }
                IngredientGridWithNavigation.this.updateLayout(false);
                return true;
            }
        }

        @Override
        public boolean hasNext() {
            int itemsPerPage = IngredientGridWithNavigation.this.ingredientGrid.size();
            return itemsPerPage > 0 && IngredientGridWithNavigation.this.ingredientSource.getIngredientList().size() > itemsPerPage;
        }

        @Override
        public boolean hasPrevious() {
            int itemsPerPage = IngredientGridWithNavigation.this.ingredientGrid.size();
            return itemsPerPage > 0 && IngredientGridWithNavigation.this.ingredientSource.getIngredientList().size() > itemsPerPage;
        }

        @Override
        public int getPageCount() {
            int itemCount = IngredientGridWithNavigation.this.ingredientSource.getIngredientList().size();
            int stacksPerPage = IngredientGridWithNavigation.this.ingredientGrid.size();
            if (stacksPerPage == 0) {
                return 1;
            } else {
                int pageCount = MathUtil.divideCeil(itemCount, stacksPerPage);
                return Math.max(1, pageCount);
            }
        }

        @Override
        public int getPageNumber() {
            int stacksPerPage = IngredientGridWithNavigation.this.ingredientGrid.size();
            return stacksPerPage == 0 ? 0 : IngredientGridWithNavigation.this.firstItemIndex / stacksPerPage;
        }
    }

    private static class UserInputHandler implements IUserInputHandler {

        private final IngredientGridWithNavigation.IngredientGridPaged paged;

        private final IRecipeFocusSource focusSource;

        private final IClientToggleState toggleState;

        private final IClientConfig clientConfig;

        private final UserInput.MouseOverable mouseOverable;

        private final CommandUtil commandUtil;

        private final CheatUtil cheatUtil;

        private UserInputHandler(IngredientGridWithNavigation.IngredientGridPaged paged, IRecipeFocusSource focusSource, IClientToggleState toggleState, IClientConfig clientConfig, CommandUtil commandUtil, CheatUtil cheatUtil, UserInput.MouseOverable mouseOverable) {
            this.paged = paged;
            this.focusSource = focusSource;
            this.toggleState = toggleState;
            this.clientConfig = clientConfig;
            this.mouseOverable = mouseOverable;
            this.commandUtil = commandUtil;
            this.cheatUtil = cheatUtil;
        }

        @Override
        public Optional<IUserInputHandler> handleMouseScrolled(double mouseX, double mouseY, double scrollDelta) {
            if (!this.mouseOverable.isMouseOver(mouseX, mouseY)) {
                return Optional.empty();
            } else if (scrollDelta < 0.0) {
                this.paged.nextPage();
                return Optional.of(this);
            } else if (scrollDelta > 0.0) {
                this.paged.previousPage();
                return Optional.of(this);
            } else {
                return Optional.empty();
            }
        }

        @Override
        public Optional<IUserInputHandler> handleUserInput(Screen screen, UserInput input, IInternalKeyMappings keyBindings) {
            if (input.is(keyBindings.getNextPage())) {
                this.paged.nextPage();
                return Optional.of(this);
            } else if (input.is(keyBindings.getPreviousPage())) {
                this.paged.previousPage();
                return Optional.of(this);
            } else {
                return this.checkHotbarKeys(screen, input);
            }
        }

        protected Optional<IUserInputHandler> checkHotbarKeys(Screen screen, UserInput input) {
            if (this.clientConfig.isCheatToHotbarUsingHotkeysEnabled() && this.toggleState.isCheatItemsEnabled() && !(screen instanceof RecipesGui)) {
                double mouseX = input.getMouseX();
                double mouseY = input.getMouseY();
                if (!this.mouseOverable.isMouseOver(mouseX, mouseY)) {
                    return Optional.empty();
                } else {
                    Minecraft minecraft = Minecraft.getInstance();
                    Options gameSettings = minecraft.options;
                    int hotbarSlot = getHotbarSlotForInput(input, gameSettings);
                    return hotbarSlot < 0 ? Optional.empty() : this.focusSource.getIngredientUnderMouse(mouseX, mouseY).flatMap(clickedIngredient -> {
                        ItemStack cheatItemStack = this.cheatUtil.getCheatItemStack(clickedIngredient);
                        if (!cheatItemStack.isEmpty()) {
                            this.commandUtil.setHotbarStack(cheatItemStack, hotbarSlot);
                            ImmutableRect2i area = clickedIngredient.getArea();
                            return Stream.of(LimitedAreaInputHandler.create(this, area));
                        } else {
                            return Stream.empty();
                        }
                    }).findFirst();
                }
            } else {
                return Optional.empty();
            }
        }

        private static int getHotbarSlotForInput(UserInput input, Options gameSettings) {
            for (int hotbarSlot = 0; hotbarSlot < gameSettings.keyHotbarSlots.length; hotbarSlot++) {
                KeyMapping keyHotbarSlot = gameSettings.keyHotbarSlots[hotbarSlot];
                if (input.is(keyHotbarSlot)) {
                    return hotbarSlot;
                }
            }
            return -1;
        }
    }
}