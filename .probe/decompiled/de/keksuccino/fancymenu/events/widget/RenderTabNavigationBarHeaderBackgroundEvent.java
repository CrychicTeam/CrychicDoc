package de.keksuccino.fancymenu.events.widget;

import de.keksuccino.fancymenu.util.event.acara.EventBase;
import java.util.Objects;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.tabs.TabNavigationBar;
import org.jetbrains.annotations.NotNull;

public class RenderTabNavigationBarHeaderBackgroundEvent extends EventBase {

    private final TabNavigationBar tabNavigationBar;

    private final GuiGraphics graphics;

    private final int headerWidth;

    private final int headerHeight;

    protected RenderTabNavigationBarHeaderBackgroundEvent(@NotNull TabNavigationBar tabNavigationBar, @NotNull GuiGraphics graphics, int headerWidth, int headerHeight) {
        this.tabNavigationBar = (TabNavigationBar) Objects.requireNonNull(tabNavigationBar);
        this.graphics = (GuiGraphics) Objects.requireNonNull(graphics);
        this.headerWidth = headerWidth;
        this.headerHeight = headerHeight;
    }

    public TabNavigationBar getTabNavigationBar() {
        return this.tabNavigationBar;
    }

    public GuiGraphics getGraphics() {
        return this.graphics;
    }

    public int getHeaderWidth() {
        return this.headerWidth;
    }

    public int getHeaderHeight() {
        return this.headerHeight;
    }

    @Override
    public boolean isCancelable() {
        return false;
    }

    public static class Post extends RenderTabNavigationBarHeaderBackgroundEvent {

        public Post(@NotNull TabNavigationBar tabNavigationBar, @NotNull GuiGraphics graphics, int headerWidth, int headerHeight) {
            super(tabNavigationBar, graphics, headerWidth, headerHeight);
        }
    }

    public static class Pre extends RenderTabNavigationBarHeaderBackgroundEvent {

        public Pre(@NotNull TabNavigationBar tabNavigationBar, @NotNull GuiGraphics graphics, int headerWidth, int headerHeight) {
            super(tabNavigationBar, graphics, headerWidth, headerHeight);
        }

        @Override
        public boolean isCancelable() {
            return true;
        }
    }
}