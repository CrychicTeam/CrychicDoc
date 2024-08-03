package de.keksuccino.fancymenu.customization.background;

import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.layout.editor.LayoutEditorScreen;
import de.keksuccino.fancymenu.util.rendering.ui.widget.NavigatableWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class MenuBackground implements Renderable, GuiEventListener, NarratableEntry, NavigatableWidget {

    public final MenuBackgroundBuilder<?> builder;

    public float opacity = 1.0F;

    public boolean keepBackgroundAspectRatio = false;

    public MenuBackground(MenuBackgroundBuilder<?> builder) {
        this.builder = builder;
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

    public void onAfterResizeScreen() {
    }

    @Nullable
    public MenuBackground copy() {
        try {
            return this.builder.deserializeBackgroundInternal(this.builder.serializedBackgroundInternal(this));
        } catch (Exception var2) {
            var2.printStackTrace();
            return null;
        }
    }

    public static boolean isEditor() {
        return Minecraft.getInstance().screen instanceof LayoutEditorScreen;
    }

    public static int getScreenWidth() {
        return AbstractElement.getScreenWidth();
    }

    public static int getScreenHeight() {
        return AbstractElement.getScreenHeight();
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
        throw new RuntimeException("MenuBackgrounds are not focusable!");
    }

    @Override
    public boolean isNavigatable() {
        return false;
    }

    @Override
    public void setNavigatable(boolean navigatable) {
        throw new RuntimeException("MenuBackgrounds are not navigatable!");
    }
}