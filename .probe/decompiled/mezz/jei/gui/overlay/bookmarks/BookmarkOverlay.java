package mezz.jei.gui.overlay.bookmarks;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import mezz.jei.api.gui.handlers.IGuiProperties;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.runtime.IBookmarkOverlay;
import mezz.jei.api.runtime.IScreenHelper;
import mezz.jei.common.config.IClientConfig;
import mezz.jei.common.config.IClientToggleState;
import mezz.jei.common.gui.textures.Textures;
import mezz.jei.common.input.IClickableIngredientInternal;
import mezz.jei.common.input.IInternalKeyMappings;
import mezz.jei.common.network.IConnectionToServer;
import mezz.jei.common.util.ImmutableRect2i;
import mezz.jei.gui.bookmarks.BookmarkList;
import mezz.jei.gui.elements.GuiIconToggleButton;
import mezz.jei.gui.input.IDragHandler;
import mezz.jei.gui.input.IRecipeFocusSource;
import mezz.jei.gui.input.IUserInputHandler;
import mezz.jei.gui.input.MouseUtil;
import mezz.jei.gui.input.handlers.CheatInputHandler;
import mezz.jei.gui.input.handlers.CombinedInputHandler;
import mezz.jei.gui.input.handlers.NullDragHandler;
import mezz.jei.gui.input.handlers.ProxyDragHandler;
import mezz.jei.gui.input.handlers.ProxyInputHandler;
import mezz.jei.gui.overlay.IngredientGridWithNavigation;
import mezz.jei.gui.overlay.ScreenPropertiesCache;
import mezz.jei.gui.util.CheatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;

public class BookmarkOverlay implements IRecipeFocusSource, IBookmarkOverlay {

    private static final int BORDER_MARGIN = 6;

    private static final int INNER_PADDING = 2;

    private static final int BUTTON_SIZE = 20;

    private final CheatInputHandler cheatInputHandler;

    private final ScreenPropertiesCache screenPropertiesCache;

    private final IngredientGridWithNavigation contents;

    private final GuiIconToggleButton bookmarkButton;

    private final BookmarkList bookmarkList;

    private final IClientToggleState toggleState;

    public BookmarkOverlay(BookmarkList bookmarkList, Textures textures, IngredientGridWithNavigation contents, IClientConfig clientConfig, IClientToggleState toggleState, IScreenHelper screenHelper, IConnectionToServer serverConnection, IInternalKeyMappings keyBindings, CheatUtil cheatUtil) {
        this.bookmarkList = bookmarkList;
        this.toggleState = toggleState;
        this.bookmarkButton = BookmarkButton.create(this, bookmarkList, textures, toggleState, keyBindings);
        this.cheatInputHandler = new CheatInputHandler(this, toggleState, clientConfig, serverConnection, cheatUtil);
        this.contents = contents;
        this.screenPropertiesCache = new ScreenPropertiesCache(screenHelper);
        bookmarkList.addSourceListChangedListener(() -> {
            toggleState.setBookmarkEnabled(!bookmarkList.isEmpty());
            Minecraft minecraft = Minecraft.getInstance();
            Screen screen = minecraft.screen;
            this.updateScreen(screen, null);
        });
    }

    public boolean isListDisplayed() {
        return this.toggleState.isBookmarkOverlayEnabled() && this.screenPropertiesCache.hasValidScreen() && this.contents.hasRoom() && !this.bookmarkList.isEmpty();
    }

    public boolean hasRoom() {
        return this.contents.hasRoom();
    }

    public void updateScreen(@Nullable Screen guiScreen, @Nullable Set<ImmutableRect2i> updatedGuiExclusionAreas) {
        this.screenPropertiesCache.updateScreen(guiScreen, updatedGuiExclusionAreas, this::onScreenPropertiesChanged);
    }

    private void onScreenPropertiesChanged() {
        this.screenPropertiesCache.getGuiProperties().ifPresentOrElse(guiProperties -> {
            Set<ImmutableRect2i> guiExclusionAreas = this.screenPropertiesCache.getGuiExclusionAreas();
            this.updateBounds(guiProperties, guiExclusionAreas);
        }, this.contents::close);
    }

    private void updateBounds(IGuiProperties guiProperties, Set<ImmutableRect2i> guiExclusionAreas) {
        ImmutableRect2i displayArea = getDisplayArea(guiProperties);
        ImmutableRect2i availableContentsArea = displayArea.cropBottom(22);
        this.contents.updateBounds(availableContentsArea, guiExclusionAreas);
        this.contents.updateLayout(false);
        if (this.contents.hasRoom()) {
            ImmutableRect2i contentsArea = this.contents.getBackgroundArea();
            ImmutableRect2i bookmarkButtonArea = displayArea.insetBy(6).matchWidthAndX(contentsArea).keepBottom(20).keepLeft(20);
            this.bookmarkButton.updateBounds(bookmarkButtonArea);
        } else {
            ImmutableRect2i bookmarkButtonArea = displayArea.insetBy(6).keepBottom(20).keepLeft(20);
            this.bookmarkButton.updateBounds(bookmarkButtonArea);
        }
    }

    private static ImmutableRect2i getDisplayArea(IGuiProperties guiProperties) {
        int width = guiProperties.getGuiLeft();
        if (width <= 0) {
            width = 0;
        }
        int screenHeight = guiProperties.getScreenHeight();
        return new ImmutableRect2i(0, 0, width, screenHeight);
    }

    public void drawScreen(Minecraft minecraft, GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (this.isListDisplayed()) {
            this.contents.draw(minecraft, guiGraphics, mouseX, mouseY, partialTicks);
        }
        if (this.screenPropertiesCache.hasValidScreen()) {
            this.bookmarkButton.draw(guiGraphics, mouseX, mouseY, partialTicks);
        }
    }

    public void drawTooltips(Minecraft minecraft, GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (this.isListDisplayed()) {
            this.contents.drawTooltips(minecraft, guiGraphics, mouseX, mouseY);
        }
        if (this.screenPropertiesCache.hasValidScreen()) {
            this.bookmarkButton.drawTooltips(guiGraphics, mouseX, mouseY);
        }
    }

    @Override
    public Stream<IClickableIngredientInternal<?>> getIngredientUnderMouse(double mouseX, double mouseY) {
        return this.isListDisplayed() ? this.contents.getIngredientUnderMouse(mouseX, mouseY) : Stream.empty();
    }

    @Override
    public Optional<ITypedIngredient<?>> getIngredientUnderMouse() {
        double mouseX = MouseUtil.getX();
        double mouseY = MouseUtil.getY();
        return this.getIngredientUnderMouse(mouseX, mouseY).map(IClickableIngredientInternal::getTypedIngredient).findFirst();
    }

    @Nullable
    @Override
    public <T> T getIngredientUnderMouse(IIngredientType<T> ingredientType) {
        double mouseX = MouseUtil.getX();
        double mouseY = MouseUtil.getY();
        return (T) this.getIngredientUnderMouse(mouseX, mouseY).map(IClickableIngredientInternal::getTypedIngredient).map(i -> i.getIngredient(ingredientType)).flatMap(Optional::stream).findFirst().orElse(null);
    }

    public IUserInputHandler createInputHandler() {
        IUserInputHandler bookmarkButtonInputHandler = this.bookmarkButton.createInputHandler();
        IUserInputHandler displayedInputHandler = new CombinedInputHandler(this.cheatInputHandler, this.contents.createInputHandler(), bookmarkButtonInputHandler);
        return new ProxyInputHandler(() -> this.isListDisplayed() ? displayedInputHandler : bookmarkButtonInputHandler);
    }

    public IDragHandler createDragHandler() {
        IDragHandler displayedDragHandler = this.contents.createDragHandler();
        return new ProxyDragHandler(() -> (IDragHandler) (this.isListDisplayed() ? displayedDragHandler : NullDragHandler.INSTANCE));
    }

    public void drawOnForeground(Minecraft minecraft, GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (this.isListDisplayed()) {
            this.contents.drawOnForeground(minecraft, guiGraphics, mouseX, mouseY);
        }
    }
}