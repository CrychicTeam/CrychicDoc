package net.minecraftforge.client.event;

import com.mojang.datafixers.util.Either;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus.Internal;

public abstract class RenderTooltipEvent extends Event {

    @NotNull
    protected final ItemStack itemStack;

    protected final GuiGraphics graphics;

    protected int x;

    protected int y;

    protected Font font;

    protected final List<ClientTooltipComponent> components;

    @Internal
    protected RenderTooltipEvent(@NotNull ItemStack itemStack, GuiGraphics graphics, int x, int y, @NotNull Font font, @NotNull List<ClientTooltipComponent> components) {
        this.itemStack = itemStack;
        this.graphics = graphics;
        this.components = Collections.unmodifiableList(components);
        this.x = x;
        this.y = y;
        this.font = font;
    }

    @NotNull
    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public GuiGraphics getGraphics() {
        return this.graphics;
    }

    @NotNull
    public List<ClientTooltipComponent> getComponents() {
        return this.components;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    @NotNull
    public Font getFont() {
        return this.font;
    }

    public static class Color extends RenderTooltipEvent {

        private final int originalBackground;

        private final int originalBorderStart;

        private final int originalBorderEnd;

        private int backgroundStart;

        private int backgroundEnd;

        private int borderStart;

        private int borderEnd;

        @Internal
        public Color(@NotNull ItemStack stack, GuiGraphics graphics, int x, int y, @NotNull Font fr, int background, int borderStart, int borderEnd, @NotNull List<ClientTooltipComponent> components) {
            super(stack, graphics, x, y, fr, components);
            this.originalBackground = background;
            this.originalBorderStart = borderStart;
            this.originalBorderEnd = borderEnd;
            this.backgroundStart = background;
            this.backgroundEnd = background;
            this.borderStart = borderStart;
            this.borderEnd = borderEnd;
        }

        public int getBackgroundStart() {
            return this.backgroundStart;
        }

        public int getBackgroundEnd() {
            return this.backgroundEnd;
        }

        public void setBackground(int background) {
            this.backgroundStart = background;
            this.backgroundEnd = background;
        }

        public void setBackgroundStart(int backgroundStart) {
            this.backgroundStart = backgroundStart;
        }

        public void setBackgroundEnd(int backgroundEnd) {
            this.backgroundEnd = backgroundEnd;
        }

        public int getBorderStart() {
            return this.borderStart;
        }

        public void setBorderStart(int borderStart) {
            this.borderStart = borderStart;
        }

        public int getBorderEnd() {
            return this.borderEnd;
        }

        public void setBorderEnd(int borderEnd) {
            this.borderEnd = borderEnd;
        }

        public int getOriginalBackgroundStart() {
            return this.originalBackground;
        }

        public int getOriginalBackgroundEnd() {
            return this.originalBackground;
        }

        public int getOriginalBorderStart() {
            return this.originalBorderStart;
        }

        public int getOriginalBorderEnd() {
            return this.originalBorderEnd;
        }
    }

    @Cancelable
    public static class GatherComponents extends Event {

        private final ItemStack itemStack;

        private final int screenWidth;

        private final int screenHeight;

        private final List<Either<FormattedText, TooltipComponent>> tooltipElements;

        private int maxWidth;

        @Internal
        public GatherComponents(ItemStack itemStack, int screenWidth, int screenHeight, List<Either<FormattedText, TooltipComponent>> tooltipElements, int maxWidth) {
            this.itemStack = itemStack;
            this.screenWidth = screenWidth;
            this.screenHeight = screenHeight;
            this.tooltipElements = tooltipElements;
            this.maxWidth = maxWidth;
        }

        public ItemStack getItemStack() {
            return this.itemStack;
        }

        public int getScreenWidth() {
            return this.screenWidth;
        }

        public int getScreenHeight() {
            return this.screenHeight;
        }

        public List<Either<FormattedText, TooltipComponent>> getTooltipElements() {
            return this.tooltipElements;
        }

        public int getMaxWidth() {
            return this.maxWidth;
        }

        public void setMaxWidth(int maxWidth) {
            this.maxWidth = maxWidth;
        }
    }

    @Cancelable
    public static class Pre extends RenderTooltipEvent {

        private final int screenWidth;

        private final int screenHeight;

        private final ClientTooltipPositioner positioner;

        @Internal
        public Pre(@NotNull ItemStack stack, GuiGraphics graphics, int x, int y, int screenWidth, int screenHeight, @NotNull Font font, @NotNull List<ClientTooltipComponent> components, @NotNull ClientTooltipPositioner positioner) {
            super(stack, graphics, x, y, font, components);
            this.screenWidth = screenWidth;
            this.screenHeight = screenHeight;
            this.positioner = positioner;
        }

        public int getScreenWidth() {
            return this.screenWidth;
        }

        public int getScreenHeight() {
            return this.screenHeight;
        }

        public ClientTooltipPositioner getTooltipPositioner() {
            return this.positioner;
        }

        public void setFont(@NotNull Font fr) {
            this.font = fr;
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }
    }
}