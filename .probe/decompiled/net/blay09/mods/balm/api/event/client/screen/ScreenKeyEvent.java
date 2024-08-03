package net.blay09.mods.balm.api.event.client.screen;

import net.blay09.mods.balm.api.event.BalmEvent;
import net.minecraft.client.gui.screens.Screen;

public abstract class ScreenKeyEvent extends BalmEvent {

    private final Screen screen;

    private final int key;

    private final int scanCode;

    private final int modifiers;

    public ScreenKeyEvent(Screen screen, int key, int scanCode, int modifiers) {
        this.screen = screen;
        this.key = key;
        this.scanCode = scanCode;
        this.modifiers = modifiers;
    }

    public Screen getScreen() {
        return this.screen;
    }

    public int getKey() {
        return this.key;
    }

    public int getScanCode() {
        return this.scanCode;
    }

    public int getModifiers() {
        return this.modifiers;
    }

    public static class Press extends ScreenKeyEvent {

        public Press(Screen screen, int key, int scanCode, int modifiers) {
            super(screen, key, scanCode, modifiers);
        }

        public static class Post extends ScreenKeyEvent.Press {

            public Post(Screen screen, int key, int scanCode, int modifiers) {
                super(screen, key, scanCode, modifiers);
            }
        }

        public static class Pre extends ScreenKeyEvent.Press {

            public Pre(Screen screen, int key, int scanCode, int modifiers) {
                super(screen, key, scanCode, modifiers);
            }
        }
    }

    public static class Release extends ScreenKeyEvent {

        public Release(Screen screen, int key, int scanCode, int modifiers) {
            super(screen, key, scanCode, modifiers);
        }

        public static class Post extends ScreenKeyEvent.Release {

            public Post(Screen screen, int key, int scanCode, int modifiers) {
                super(screen, key, scanCode, modifiers);
            }
        }

        public static class Pre extends ScreenKeyEvent.Release {

            public Pre(Screen screen, int key, int scanCode, int modifiers) {
                super(screen, key, scanCode, modifiers);
            }
        }
    }
}