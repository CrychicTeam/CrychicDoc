package de.keksuccino.fancymenu.customization.element;

import de.keksuccino.fancymenu.customization.ScreenCustomization;
import de.keksuccino.fancymenu.customization.element.anchor.ElementAnchorPoint;
import de.keksuccino.fancymenu.customization.element.anchor.ElementAnchorPoints;
import de.keksuccino.fancymenu.customization.element.editor.AbstractEditorElement;
import de.keksuccino.fancymenu.customization.layer.ScreenCustomizationLayer;
import de.keksuccino.fancymenu.customization.layer.ScreenCustomizationLayerHandler;
import de.keksuccino.fancymenu.customization.layout.Layout;
import de.keksuccino.fancymenu.customization.layout.editor.LayoutEditorScreen;
import de.keksuccino.fancymenu.customization.loadingrequirement.internal.LoadingRequirementContainer;
import de.keksuccino.fancymenu.customization.placeholder.PlaceholderParser;
import de.keksuccino.fancymenu.util.properties.RuntimePropertyContainer;
import de.keksuccino.fancymenu.util.rendering.ui.widget.NavigatableWidget;
import de.keksuccino.konkrete.math.MathUtils;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractElement implements Renderable, GuiEventListener, NarratableEntry, NavigatableWidget {

    public static final AbstractElement EMPTY_ELEMENT = new AbstractElement(null) {

        @Override
        public void render(@NotNull GuiGraphics g, int i1, int i2, float f) {
        }
    };

    public static final int STAY_ON_SCREEN_EDGE_ZONE_SIZE = 2;

    public final ElementBuilder<?, ?> builder;

    public ElementAnchorPoint anchorPoint = ElementAnchorPoints.MID_CENTERED;

    public String anchorPointElementIdentifier = null;

    protected AbstractElement cachedElementAnchorPointParent = null;

    public int posOffsetX = 0;

    public int posOffsetY = 0;

    public int baseWidth = 0;

    public int baseHeight = 0;

    public String advancedX;

    public Integer cachedAdvancedX;

    public long lastAdvancedXParse = -1L;

    public String advancedY;

    public Integer cachedAdvancedY;

    public long lastAdvancedYParse = -1L;

    public String advancedWidth;

    public Integer cachedAdvancedWidth;

    public long lastAdvancedWidthParse = -1L;

    public String advancedHeight;

    public Integer cachedAdvancedHeight;

    public long lastAdvancedHeightParse = -1L;

    public boolean stretchX = false;

    public boolean stretchY = false;

    public boolean stayOnScreen = true;

    public volatile boolean visible = true;

    public volatile AbstractElement.AppearanceDelay appearanceDelay = AbstractElement.AppearanceDelay.NO_DELAY;

    public volatile float appearanceDelayInSeconds = 1.0F;

    public volatile boolean fadeIn = false;

    public volatile float fadeInSpeed = 1.0F;

    public volatile float opacity = 1.0F;

    public float customGuiScale = -1.0F;

    public LoadingRequirementContainer loadingRequirementContainer = new LoadingRequirementContainer();

    @Nullable
    public String customElementLayerName = null;

    private String instanceIdentifier;

    @Nullable
    protected Layout parentLayout;

    @Nullable
    protected RuntimePropertyContainer cachedMemory;

    public AbstractElement(@NotNull ElementBuilder<?, ?> builder) {
        this.builder = builder;
        this.instanceIdentifier = ScreenCustomization.generateUniqueIdentifier();
    }

    public void setParentLayout(@Nullable Layout parentLayout) {
        this.parentLayout = parentLayout;
    }

    @Nullable
    public Layout getParentLayout() {
        return this.parentLayout;
    }

    @Override
    public abstract void render(@NotNull GuiGraphics var1, int var2, int var3, float var4);

    public void tick() {
    }

    public void onCloseScreen() {
    }

    public void onOpenScreen() {
    }

    public void onBeforeResizeScreen() {
    }

    public void onDestroyElement() {
    }

    @Nullable
    public List<GuiEventListener> getWidgetsToRegister() {
        return null;
    }

    @NotNull
    public String getInstanceIdentifier() {
        return this.instanceIdentifier;
    }

    public void setInstanceIdentifier(@NotNull String id) {
        this.instanceIdentifier = (String) Objects.requireNonNull(id);
    }

    public int getAbsoluteX() {
        int x = 0;
        if (this.anchorPoint != null) {
            x = this.anchorPoint.getElementPositionX(this);
        }
        if (this.advancedX != null) {
            long now = System.currentTimeMillis();
            if (this.lastAdvancedXParse + 30L > now && this.cachedAdvancedX != null) {
                x = this.cachedAdvancedX;
            } else {
                String s = PlaceholderParser.replacePlaceholders(this.advancedX).replace(" ", "");
                if (MathUtils.isDouble(s)) {
                    x = (int) Double.parseDouble(s);
                    this.cachedAdvancedX = x;
                    this.lastAdvancedXParse = now;
                }
            }
        }
        if (this.stretchX) {
            x = 0;
        } else if (this.stayOnScreen) {
            if (x < 2) {
                x = 2;
            }
            if (x > getScreenWidth() - 2 - this.getAbsoluteWidth()) {
                x = getScreenWidth() - 2 - this.getAbsoluteWidth();
            }
        }
        return x;
    }

    public int getAbsoluteY() {
        int y = 0;
        if (this.anchorPoint != null) {
            y = this.anchorPoint.getElementPositionY(this);
        }
        if (this.advancedY != null) {
            long now = System.currentTimeMillis();
            if (this.lastAdvancedYParse + 30L > now && this.cachedAdvancedY != null) {
                y = this.cachedAdvancedY;
            } else {
                String s = PlaceholderParser.replacePlaceholders(this.advancedY).replace(" ", "");
                if (MathUtils.isDouble(s)) {
                    y = (int) Double.parseDouble(s);
                    this.cachedAdvancedY = y;
                    this.lastAdvancedYParse = now;
                }
            }
        }
        if (this.stretchY) {
            y = 0;
        } else if (this.stayOnScreen) {
            if (y < 2) {
                y = 2;
            }
            if (y > getScreenHeight() - 2 - this.getAbsoluteHeight()) {
                y = getScreenHeight() - 2 - this.getAbsoluteHeight();
            }
        }
        return y;
    }

    public int getAbsoluteWidth() {
        if (this.advancedWidth != null) {
            long now = System.currentTimeMillis();
            if (this.lastAdvancedWidthParse + 30L > now && this.cachedAdvancedWidth != null) {
                return this.cachedAdvancedWidth;
            }
            String s = PlaceholderParser.replacePlaceholders(this.advancedWidth).replace(" ", "");
            if (MathUtils.isDouble(s)) {
                this.cachedAdvancedWidth = (int) Double.parseDouble(s);
                this.lastAdvancedWidthParse = now;
                return this.cachedAdvancedWidth;
            }
        }
        return this.stretchX ? getScreenWidth() : this.baseWidth;
    }

    public int getAbsoluteHeight() {
        if (this.advancedHeight != null) {
            long now = System.currentTimeMillis();
            if (this.lastAdvancedHeightParse + 30L > now && this.cachedAdvancedHeight != null) {
                return this.cachedAdvancedHeight;
            }
            String s = PlaceholderParser.replacePlaceholders(this.advancedHeight).replace(" ", "");
            if (MathUtils.isDouble(s)) {
                this.cachedAdvancedHeight = (int) Double.parseDouble(s);
                this.lastAdvancedHeightParse = now;
                return this.cachedAdvancedHeight;
            }
        }
        return this.stretchY ? getScreenHeight() : this.baseHeight;
    }

    @Nullable
    public AbstractElement getElementAnchorPointParent() {
        if (this.anchorPointElementIdentifier == null) {
            return null;
        } else {
            if (this.cachedElementAnchorPointParent == null) {
                this.cachedElementAnchorPointParent = getElementByInstanceIdentifier(this.anchorPointElementIdentifier);
            }
            return this.cachedElementAnchorPointParent;
        }
    }

    public void setElementAnchorPointParent(@Nullable AbstractElement element) {
        this.cachedElementAnchorPointParent = element;
    }

    public int getChildElementAnchorPointX() {
        return this.getAbsoluteX();
    }

    public int getChildElementAnchorPointY() {
        return this.getAbsoluteY();
    }

    public boolean shouldRender() {
        return !this.loadingRequirementsMet() ? false : this.visible;
    }

    protected boolean loadingRequirementsMet() {
        return isEditor() ? true : this.loadingRequirementContainer.requirementsMet();
    }

    @NotNull
    public Component getDisplayName() {
        return (Component) (this.customElementLayerName != null ? Component.literal(this.customElementLayerName) : this.builder.getDisplayName(this));
    }

    @NotNull
    public RuntimePropertyContainer getMemory() {
        if (this.cachedMemory == null) {
            this.cachedMemory = ElementMemories.getMemory(this.getInstanceIdentifier());
        }
        return this.cachedMemory;
    }

    public static String fixBackslashPath(String path) {
        return path != null ? path.replace("\\", "/") : null;
    }

    protected static boolean isEditor() {
        return getScreen() instanceof LayoutEditorScreen;
    }

    @Nullable
    public static Screen getScreen() {
        return Minecraft.getInstance().screen;
    }

    public static int getScreenWidth() {
        Screen s = getScreen();
        return s != null ? s.width : 0;
    }

    public static int getScreenHeight() {
        Screen s = getScreen();
        return s != null ? s.height : 0;
    }

    @Nullable
    public static AbstractElement getElementByInstanceIdentifier(String identifier) {
        identifier = identifier.replace("vanillabtn:", "").replace("button_compatibility_id:", "");
        if (isEditor()) {
            AbstractEditorElement editorElement = ((LayoutEditorScreen) getScreen()).getElementByInstanceIdentifier(identifier);
            if (editorElement != null) {
                return editorElement.element;
            }
        } else {
            ScreenCustomizationLayer layer = ScreenCustomizationLayerHandler.getActiveLayer();
            if (layer != null) {
                return layer.getElementByInstanceIdentifier(identifier);
            }
        }
        return null;
    }

    @NotNull
    public static Component buildComponent(@NotNull String serializedComponentOrPlainText) {
        serializedComponentOrPlainText = PlaceholderParser.replacePlaceholders(serializedComponentOrPlainText);
        if (!serializedComponentOrPlainText.startsWith("{")) {
            return Component.literal(serializedComponentOrPlainText);
        } else {
            try {
                Component c = Component.Serializer.fromJson(serializedComponentOrPlainText);
                if (c != null) {
                    return c;
                }
            } catch (Exception var2) {
            }
            return Component.literal(serializedComponentOrPlainText);
        }
    }

    @Override
    public void setFocused(boolean var1) {
    }

    @Override
    public boolean isFocused() {
        return false;
    }

    @NotNull
    @Override
    public NarratableEntry.NarrationPriority narrationPriority() {
        return NarratableEntry.NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(@NotNull NarrationElementOutput narrationElementOutput) {
    }

    @Override
    public boolean isFocusable() {
        return false;
    }

    @Override
    public void setFocusable(boolean focusable) {
        throw new RuntimeException("AbstractElements are not focusable!");
    }

    @Override
    public boolean isNavigatable() {
        return false;
    }

    @Override
    public void setNavigatable(boolean navigatable) {
        throw new RuntimeException("AbstractElements are not navigatable!");
    }

    public static enum Alignment {

        LEFT("left"), RIGHT("right"), CENTERED("centered");

        public final String key;

        private Alignment(String key) {
            this.key = key;
        }

        public static AbstractElement.Alignment getByName(@NotNull String name) {
            for (AbstractElement.Alignment a : values()) {
                if (a.key.equals(name)) {
                    return a;
                }
            }
            return null;
        }
    }

    public static enum AppearanceDelay {

        NO_DELAY("no_delay"), FIRST_TIME("first_time"), EVERY_TIME("every_time");

        public final String name;

        private AppearanceDelay(String name) {
            this.name = name;
        }

        @Nullable
        public static AbstractElement.AppearanceDelay getByName(@NotNull String name) {
            for (AbstractElement.AppearanceDelay d : values()) {
                if (d.name.equals(name)) {
                    return d;
                }
            }
            return null;
        }
    }
}