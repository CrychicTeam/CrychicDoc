package net.blay09.mods.balm.api.event.client.screen;

import net.blay09.mods.balm.api.event.BalmEvent;
import net.minecraft.client.gui.screens.Screen;

public abstract class ScreenMouseEvent extends BalmEvent {

    private final Screen screen;

    private final double mouseX;

    private final double mouseY;

    private final int button;

    public ScreenMouseEvent(Screen screen, double mouseX, double mouseY, int button) {
        this.screen = screen;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.button = button;
    }

    public Screen getScreen() {
        return this.screen;
    }

    public double getMouseX() {
        return this.mouseX;
    }

    public double getMouseY() {
        return this.mouseY;
    }

    public int getButton() {
        return this.button;
    }

    public abstract static class Click extends ScreenMouseEvent {

        public Click(Screen screen, double mouseX, double mouseY, int button) {
            super(screen, mouseX, mouseY, button);
        }

        public static class Post extends ScreenMouseEvent.Click {

            public Post(Screen screen, double mouseX, double mouseY, int button) {
                super(screen, mouseX, mouseY, button);
            }
        }

        public static class Pre extends ScreenMouseEvent.Click {

            public Pre(Screen screen, double mouseX, double mouseY, int button) {
                super(screen, mouseX, mouseY, button);
            }
        }
    }

    public abstract static class Drag extends ScreenMouseEvent {

        private final double dragX;

        private final double dragY;

        public Drag(Screen screen, double mouseX, double mouseY, int button, double dragX, double dragY) {
            super(screen, mouseX, mouseY, button);
            this.dragX = dragX;
            this.dragY = dragY;
        }

        public double getDragX() {
            return this.dragX;
        }

        public double getDragY() {
            return this.dragY;
        }

        public static class Post extends ScreenMouseEvent.Drag {

            public Post(Screen screen, double mouseX, double mouseY, int button, double dragX, double dragY) {
                super(screen, mouseX, mouseY, button, dragX, dragY);
            }
        }

        public static class Pre extends ScreenMouseEvent.Drag {

            public Pre(Screen screen, double mouseX, double mouseY, int button, double dragX, double dragY) {
                super(screen, mouseX, mouseY, button, dragX, dragY);
            }
        }
    }

    public abstract static class Release extends ScreenMouseEvent {

        public Release(Screen screen, double mouseX, double mouseY, int button) {
            super(screen, mouseX, mouseY, button);
        }

        public static class Post extends ScreenMouseEvent.Release {

            public Post(Screen screen, double mouseX, double mouseY, int button) {
                super(screen, mouseX, mouseY, button);
            }
        }

        public static class Pre extends ScreenMouseEvent.Release {

            public Pre(Screen screen, double mouseX, double mouseY, int button) {
                super(screen, mouseX, mouseY, button);
            }
        }
    }
}