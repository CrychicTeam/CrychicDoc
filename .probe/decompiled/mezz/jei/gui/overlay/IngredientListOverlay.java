package mezz.jei.gui.overlay;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import mezz.jei.api.gui.handlers.IGuiProperties;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.runtime.IIngredientListOverlay;
import mezz.jei.api.runtime.IScreenHelper;
import mezz.jei.common.config.IClientConfig;
import mezz.jei.common.config.IClientToggleState;
import mezz.jei.common.gui.textures.Textures;
import mezz.jei.common.input.IClickableIngredientInternal;
import mezz.jei.common.input.IInternalKeyMappings;
import mezz.jei.common.network.IConnectionToServer;
import mezz.jei.common.util.ImmutableRect2i;
import mezz.jei.gui.GuiProperties;
import mezz.jei.gui.elements.GuiIconToggleButton;
import mezz.jei.gui.filter.IFilterTextSource;
import mezz.jei.gui.input.GuiTextFieldFilter;
import mezz.jei.gui.input.ICharTypedHandler;
import mezz.jei.gui.input.IDragHandler;
import mezz.jei.gui.input.IRecipeFocusSource;
import mezz.jei.gui.input.IUserInputHandler;
import mezz.jei.gui.input.MouseUtil;
import mezz.jei.gui.input.handlers.CheatInputHandler;
import mezz.jei.gui.input.handlers.CombinedInputHandler;
import mezz.jei.gui.input.handlers.NullDragHandler;
import mezz.jei.gui.input.handlers.NullInputHandler;
import mezz.jei.gui.input.handlers.ProxyDragHandler;
import mezz.jei.gui.input.handlers.ProxyInputHandler;
import mezz.jei.gui.util.CheatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;

public class IngredientListOverlay implements IIngredientListOverlay, IRecipeFocusSource, ICharTypedHandler {

    private static final int BORDER_MARGIN = 6;

    private static final int INNER_PADDING = 2;

    private static final int BUTTON_SIZE = 20;

    private static final int SEARCH_HEIGHT = 20;

    private final GuiIconToggleButton configButton;

    private final IngredientGridWithNavigation contents;

    private final IClientConfig clientConfig;

    private final IClientToggleState toggleState;

    private final IConnectionToServer serverConnection;

    private final GuiTextFieldFilter searchField;

    private final IInternalKeyMappings keyBindings;

    private final CheatUtil cheatUtil;

    private final ScreenPropertiesCache screenPropertiesCache;

    private final IFilterTextSource filterTextSource;

    public IngredientListOverlay(IIngredientGridSource ingredientGridSource, IFilterTextSource filterTextSource, IScreenHelper screenHelper, IngredientGridWithNavigation contents, IClientConfig clientConfig, IClientToggleState toggleState, IConnectionToServer serverConnection, Textures textures, IInternalKeyMappings keyBindings, CheatUtil cheatUtil) {
        this.screenPropertiesCache = new ScreenPropertiesCache(screenHelper);
        this.contents = contents;
        this.clientConfig = clientConfig;
        this.toggleState = toggleState;
        this.serverConnection = serverConnection;
        this.searchField = new GuiTextFieldFilter(textures, contents::isEmpty);
        this.keyBindings = keyBindings;
        this.cheatUtil = cheatUtil;
        this.filterTextSource = filterTextSource;
        this.searchField.setValue(filterTextSource.getFilterText());
        this.searchField.setFocused(false);
        this.searchField.m_94151_(filterTextSource::setFilterText);
        filterTextSource.addListener(this.searchField::m_94144_);
        ingredientGridSource.addSourceListChangedListener(() -> {
            Minecraft minecraft = Minecraft.getInstance();
            Screen screen = minecraft.screen;
            this.updateScreen(screen, null);
        });
        this.configButton = ConfigButton.create(this::isListDisplayed, toggleState, textures, keyBindings);
    }

    @Override
    public boolean isListDisplayed() {
        return (this.toggleState.isOverlayEnabled() || this.keyBindings.getToggleOverlay().isUnbound()) && this.screenPropertiesCache.hasValidScreen() && this.contents.hasRoom();
    }

    private static ImmutableRect2i createDisplayArea(IGuiProperties guiProperties) {
        ImmutableRect2i screenRectangle = GuiProperties.getScreenRectangle(guiProperties);
        int guiRight = GuiProperties.getGuiRight(guiProperties);
        return screenRectangle.cropLeft(guiRight);
    }

    public void updateScreen(@Nullable Screen guiScreen, @Nullable Set<ImmutableRect2i> updatedGuiExclusionAreas) {
        this.screenPropertiesCache.updateScreen(guiScreen, updatedGuiExclusionAreas, this::onScreenPropertiesChanged);
    }

    private void onScreenPropertiesChanged() {
        this.screenPropertiesCache.getGuiProperties().ifPresentOrElse(guiProperties -> {
            ImmutableRect2i displayArea = createDisplayArea(guiProperties);
            Set<ImmutableRect2i> guiExclusionAreas = this.screenPropertiesCache.getGuiExclusionAreas();
            this.updateBounds(guiProperties, displayArea, guiExclusionAreas);
        }, () -> {
            this.contents.close();
            this.searchField.setFocused(false);
        });
    }

    private void updateBounds(IGuiProperties guiProperties, ImmutableRect2i displayArea, Set<ImmutableRect2i> guiExclusionAreas) {
        boolean searchBarCentered = isSearchBarCentered(this.clientConfig, guiProperties);
        ImmutableRect2i availableContentsArea = this.getAvailableContentsArea(displayArea, searchBarCentered);
        this.contents.updateBounds(availableContentsArea, guiExclusionAreas);
        this.contents.updateLayout(false);
        ImmutableRect2i searchAndConfigArea = this.getSearchAndConfigArea(displayArea, searchBarCentered, guiProperties);
        ImmutableRect2i searchArea = searchAndConfigArea.cropRight(20);
        ImmutableRect2i configButtonArea = searchAndConfigArea.keepRight(20);
        this.searchField.setValue(this.filterTextSource.getFilterText());
        this.searchField.updateBounds(searchArea);
        this.configButton.updateBounds(configButtonArea);
    }

    private static boolean isSearchBarCentered(IClientConfig clientConfig, IGuiProperties guiProperties) {
        return clientConfig.isCenterSearchBarEnabled() && GuiProperties.getGuiBottom(guiProperties) + 20 < guiProperties.getScreenHeight();
    }

    private ImmutableRect2i getAvailableContentsArea(ImmutableRect2i displayArea, boolean searchBarCentered) {
        return searchBarCentered ? displayArea : displayArea.cropBottom(22);
    }

    private ImmutableRect2i getSearchAndConfigArea(ImmutableRect2i displayArea, boolean searchBarCentered, IGuiProperties guiProperties) {
        displayArea = displayArea.insetBy(6);
        if (searchBarCentered) {
            ImmutableRect2i guiRectangle = GuiProperties.getGuiRectangle(guiProperties);
            return displayArea.keepBottom(20).matchWidthAndX(guiRectangle);
        } else if (this.contents.hasRoom()) {
            ImmutableRect2i contentsArea = this.contents.getBackgroundArea();
            return displayArea.keepBottom(20).matchWidthAndX(contentsArea);
        } else {
            return displayArea.keepBottom(20);
        }
    }

    public void drawScreen(Minecraft minecraft, GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (this.isListDisplayed()) {
            this.searchField.renderWidget(guiGraphics, mouseX, mouseY, partialTicks);
            this.contents.draw(minecraft, guiGraphics, mouseX, mouseY, partialTicks);
        }
        if (this.screenPropertiesCache.hasValidScreen()) {
            this.configButton.draw(guiGraphics, mouseX, mouseY, partialTicks);
        }
    }

    public void drawTooltips(Minecraft minecraft, GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (this.isListDisplayed()) {
            this.contents.drawTooltips(minecraft, guiGraphics, mouseX, mouseY);
        }
        if (this.screenPropertiesCache.hasValidScreen()) {
            this.configButton.drawTooltips(guiGraphics, mouseX, mouseY);
        }
    }

    public void drawOnForeground(Minecraft minecraft, GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (this.isListDisplayed()) {
            this.contents.drawOnForeground(minecraft, guiGraphics, mouseX, mouseY);
        }
    }

    public void handleTick() {
        if (this.isListDisplayed()) {
            this.searchField.m_94120_();
        }
    }

    @Override
    public Stream<IClickableIngredientInternal<?>> getIngredientUnderMouse(double mouseX, double mouseY) {
        return this.isListDisplayed() ? this.contents.getIngredientUnderMouse(mouseX, mouseY) : Stream.empty();
    }

    public IUserInputHandler createInputHandler() {
        IUserInputHandler displayedInputHandler = new CombinedInputHandler(this.searchField.createInputHandler(), this.configButton.createInputHandler(), this.contents.createInputHandler(), new CheatInputHandler(this.contents, this.toggleState, this.clientConfig, this.serverConnection, this.cheatUtil));
        IUserInputHandler configButtonInputHandler = this.configButton.createInputHandler();
        return new ProxyInputHandler(() -> {
            if (this.isListDisplayed()) {
                return displayedInputHandler;
            } else {
                return (IUserInputHandler) (this.screenPropertiesCache.hasValidScreen() ? configButtonInputHandler : NullInputHandler.INSTANCE);
            }
        });
    }

    public IDragHandler createDragHandler() {
        IDragHandler displayedDragHandler = this.contents.createDragHandler();
        return new ProxyDragHandler(() -> (IDragHandler) (this.isListDisplayed() ? displayedDragHandler : NullDragHandler.INSTANCE));
    }

    @Override
    public boolean hasKeyboardFocus() {
        return this.isListDisplayed() && this.searchField.m_93696_();
    }

    @Override
    public boolean onCharTyped(char codePoint, int modifiers) {
        return this.searchField.m_5534_(codePoint, modifiers);
    }

    @Override
    public Optional<ITypedIngredient<?>> getIngredientUnderMouse() {
        if (this.isListDisplayed()) {
            double mouseX = MouseUtil.getX();
            double mouseY = MouseUtil.getY();
            return this.contents.getIngredientUnderMouse(mouseX, mouseY).map(IClickableIngredientInternal::getTypedIngredient).findFirst();
        } else {
            return Optional.empty();
        }
    }

    @Nullable
    @Override
    public <T> T getIngredientUnderMouse(IIngredientType<T> ingredientType) {
        if (this.isListDisplayed()) {
            double mouseX = MouseUtil.getX();
            double mouseY = MouseUtil.getY();
            return (T) this.contents.getIngredientUnderMouse(mouseX, mouseY).map(IClickableIngredientInternal::getTypedIngredient).map(i -> i.getIngredient(ingredientType)).flatMap(Optional::stream).findFirst().orElse(null);
        } else {
            return null;
        }
    }

    @Override
    public <T> List<T> getVisibleIngredients(IIngredientType<T> ingredientType) {
        return this.isListDisplayed() ? this.contents.getVisibleIngredients(ingredientType).toList() : Collections.emptyList();
    }
}