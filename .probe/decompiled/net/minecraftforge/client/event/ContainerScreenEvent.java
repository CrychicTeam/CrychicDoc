package net.minecraftforge.client.event;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.ApiStatus.Internal;

public abstract class ContainerScreenEvent extends Event {

    private final AbstractContainerScreen<?> containerScreen;

    @Internal
    protected ContainerScreenEvent(AbstractContainerScreen<?> containerScreen) {
        this.containerScreen = containerScreen;
    }

    public AbstractContainerScreen<?> getContainerScreen() {
        return this.containerScreen;
    }

    public abstract static class Render extends ContainerScreenEvent {

        private final GuiGraphics guiGraphics;

        private final int mouseX;

        private final int mouseY;

        @Internal
        protected Render(AbstractContainerScreen<?> guiContainer, GuiGraphics guiGraphics, int mouseX, int mouseY) {
            super(guiContainer);
            this.guiGraphics = guiGraphics;
            this.mouseX = mouseX;
            this.mouseY = mouseY;
        }

        public GuiGraphics getGuiGraphics() {
            return this.guiGraphics;
        }

        public int getMouseX() {
            return this.mouseX;
        }

        public int getMouseY() {
            return this.mouseY;
        }

        public static class Background extends ContainerScreenEvent.Render {

            @Internal
            public Background(AbstractContainerScreen<?> guiContainer, GuiGraphics guiGraphics, int mouseX, int mouseY) {
                super(guiContainer, guiGraphics, mouseX, mouseY);
            }
        }

        public static class Foreground extends ContainerScreenEvent.Render {

            @Internal
            public Foreground(AbstractContainerScreen<?> guiContainer, GuiGraphics guiGraphics, int mouseX, int mouseY) {
                super(guiContainer, guiGraphics, mouseX, mouseY);
            }
        }
    }
}