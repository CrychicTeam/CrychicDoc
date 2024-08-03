package net.minecraftforge.client.event;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.Event.HasResult;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

@OnlyIn(Dist.CLIENT)
public abstract class ScreenEvent extends Event {

    private final Screen screen;

    @Internal
    protected ScreenEvent(Screen screen) {
        this.screen = (Screen) Objects.requireNonNull(screen);
    }

    public Screen getScreen() {
        return this.screen;
    }

    public static class BackgroundRendered extends ScreenEvent {

        private final GuiGraphics guiGraphics;

        @Internal
        public BackgroundRendered(Screen screen, GuiGraphics guiGraphics) {
            super(screen);
            this.guiGraphics = guiGraphics;
        }

        public GuiGraphics getGuiGraphics() {
            return this.guiGraphics;
        }
    }

    public static class CharacterTyped extends ScreenEvent {

        private final char codePoint;

        private final int modifiers;

        @Internal
        public CharacterTyped(Screen screen, char codePoint, int modifiers) {
            super(screen);
            this.codePoint = codePoint;
            this.modifiers = modifiers;
        }

        public char getCodePoint() {
            return this.codePoint;
        }

        public int getModifiers() {
            return this.modifiers;
        }

        public static class Post extends ScreenEvent.CharacterTyped {

            @Internal
            public Post(Screen screen, char codePoint, int modifiers) {
                super(screen, codePoint, modifiers);
            }
        }

        @Cancelable
        public static class Pre extends ScreenEvent.CharacterTyped {

            @Internal
            public Pre(Screen screen, char codePoint, int modifiers) {
                super(screen, codePoint, modifiers);
            }
        }
    }

    public static class Closing extends ScreenEvent {

        @Internal
        public Closing(Screen screen) {
            super(screen);
        }
    }

    public abstract static class Init extends ScreenEvent {

        private final Consumer<GuiEventListener> add;

        private final Consumer<GuiEventListener> remove;

        private final List<GuiEventListener> listenerList;

        @Internal
        protected Init(Screen screen, List<GuiEventListener> listenerList, Consumer<GuiEventListener> add, Consumer<GuiEventListener> remove) {
            super(screen);
            this.listenerList = Collections.unmodifiableList(listenerList);
            this.add = add;
            this.remove = remove;
        }

        public List<GuiEventListener> getListenersList() {
            return this.listenerList;
        }

        public void addListener(GuiEventListener listener) {
            this.add.accept(listener);
        }

        public void removeListener(GuiEventListener listener) {
            this.remove.accept(listener);
        }

        public static class Post extends ScreenEvent.Init {

            @Internal
            public Post(Screen screen, List<GuiEventListener> list, Consumer<GuiEventListener> add, Consumer<GuiEventListener> remove) {
                super(screen, list, add, remove);
            }
        }

        @Cancelable
        public static class Pre extends ScreenEvent.Init {

            @Internal
            public Pre(Screen screen, List<GuiEventListener> list, Consumer<GuiEventListener> add, Consumer<GuiEventListener> remove) {
                super(screen, list, add, remove);
            }
        }
    }

    private abstract static class KeyInput extends ScreenEvent {

        private final int keyCode;

        private final int scanCode;

        private final int modifiers;

        @Internal
        protected KeyInput(Screen screen, int keyCode, int scanCode, int modifiers) {
            super(screen);
            this.keyCode = keyCode;
            this.scanCode = scanCode;
            this.modifiers = modifiers;
        }

        public int getKeyCode() {
            return this.keyCode;
        }

        public int getScanCode() {
            return this.scanCode;
        }

        public int getModifiers() {
            return this.modifiers;
        }
    }

    public abstract static class KeyPressed extends ScreenEvent.KeyInput {

        @Internal
        public KeyPressed(Screen screen, int keyCode, int scanCode, int modifiers) {
            super(screen, keyCode, scanCode, modifiers);
        }

        @Cancelable
        public static class Post extends ScreenEvent.KeyPressed {

            @Internal
            public Post(Screen screen, int keyCode, int scanCode, int modifiers) {
                super(screen, keyCode, scanCode, modifiers);
            }
        }

        @Cancelable
        public static class Pre extends ScreenEvent.KeyPressed {

            @Internal
            public Pre(Screen screen, int keyCode, int scanCode, int modifiers) {
                super(screen, keyCode, scanCode, modifiers);
            }
        }
    }

    public abstract static class KeyReleased extends ScreenEvent.KeyInput {

        @Internal
        public KeyReleased(Screen screen, int keyCode, int scanCode, int modifiers) {
            super(screen, keyCode, scanCode, modifiers);
        }

        @Cancelable
        public static class Post extends ScreenEvent.KeyReleased {

            @Internal
            public Post(Screen screen, int keyCode, int scanCode, int modifiers) {
                super(screen, keyCode, scanCode, modifiers);
            }
        }

        @Cancelable
        public static class Pre extends ScreenEvent.KeyReleased {

            @Internal
            public Pre(Screen screen, int keyCode, int scanCode, int modifiers) {
                super(screen, keyCode, scanCode, modifiers);
            }
        }
    }

    public abstract static class MouseButtonPressed extends ScreenEvent.MouseInput {

        private final int button;

        @Internal
        public MouseButtonPressed(Screen screen, double mouseX, double mouseY, int button) {
            super(screen, mouseX, mouseY);
            this.button = button;
        }

        public int getButton() {
            return this.button;
        }

        @HasResult
        public static class Post extends ScreenEvent.MouseButtonPressed {

            private final boolean handled;

            @Internal
            public Post(Screen screen, double mouseX, double mouseY, int button, boolean handled) {
                super(screen, mouseX, mouseY, button);
                this.handled = handled;
            }

            public boolean wasHandled() {
                return this.handled;
            }
        }

        @Cancelable
        public static class Pre extends ScreenEvent.MouseButtonPressed {

            @Internal
            public Pre(Screen screen, double mouseX, double mouseY, int button) {
                super(screen, mouseX, mouseY, button);
            }
        }
    }

    public abstract static class MouseButtonReleased extends ScreenEvent.MouseInput {

        private final int button;

        @Internal
        public MouseButtonReleased(Screen screen, double mouseX, double mouseY, int button) {
            super(screen, mouseX, mouseY);
            this.button = button;
        }

        public int getButton() {
            return this.button;
        }

        @HasResult
        public static class Post extends ScreenEvent.MouseButtonReleased {

            private final boolean handled;

            @Internal
            public Post(Screen screen, double mouseX, double mouseY, int button, boolean handled) {
                super(screen, mouseX, mouseY, button);
                this.handled = handled;
            }

            public boolean wasHandled() {
                return this.handled;
            }
        }

        @Cancelable
        public static class Pre extends ScreenEvent.MouseButtonReleased {

            @Internal
            public Pre(Screen screen, double mouseX, double mouseY, int button) {
                super(screen, mouseX, mouseY, button);
            }
        }
    }

    public abstract static class MouseDragged extends ScreenEvent.MouseInput {

        private final int mouseButton;

        private final double dragX;

        private final double dragY;

        @Internal
        public MouseDragged(Screen screen, double mouseX, double mouseY, int mouseButton, double dragX, double dragY) {
            super(screen, mouseX, mouseY);
            this.mouseButton = mouseButton;
            this.dragX = dragX;
            this.dragY = dragY;
        }

        public int getMouseButton() {
            return this.mouseButton;
        }

        public double getDragX() {
            return this.dragX;
        }

        public double getDragY() {
            return this.dragY;
        }

        public static class Post extends ScreenEvent.MouseDragged {

            @Internal
            public Post(Screen screen, double mouseX, double mouseY, int mouseButton, double dragX, double dragY) {
                super(screen, mouseX, mouseY, mouseButton, dragX, dragY);
            }
        }

        @Cancelable
        public static class Pre extends ScreenEvent.MouseDragged {

            @Internal
            public Pre(Screen screen, double mouseX, double mouseY, int mouseButton, double dragX, double dragY) {
                super(screen, mouseX, mouseY, mouseButton, dragX, dragY);
            }
        }
    }

    private abstract static class MouseInput extends ScreenEvent {

        private final double mouseX;

        private final double mouseY;

        @Internal
        protected MouseInput(Screen screen, double mouseX, double mouseY) {
            super(screen);
            this.mouseX = mouseX;
            this.mouseY = mouseY;
        }

        public double getMouseX() {
            return this.mouseX;
        }

        public double getMouseY() {
            return this.mouseY;
        }
    }

    public abstract static class MouseScrolled extends ScreenEvent.MouseInput {

        private final double scrollDelta;

        @Internal
        public MouseScrolled(Screen screen, double mouseX, double mouseY, double scrollDelta) {
            super(screen, mouseX, mouseY);
            this.scrollDelta = scrollDelta;
        }

        public double getScrollDelta() {
            return this.scrollDelta;
        }

        public static class Post extends ScreenEvent.MouseScrolled {

            @Internal
            public Post(Screen screen, double mouseX, double mouseY, double scrollDelta) {
                super(screen, mouseX, mouseY, scrollDelta);
            }
        }

        @Cancelable
        public static class Pre extends ScreenEvent.MouseScrolled {

            @Internal
            public Pre(Screen screen, double mouseX, double mouseY, double scrollDelta) {
                super(screen, mouseX, mouseY, scrollDelta);
            }
        }
    }

    @Cancelable
    public static class Opening extends ScreenEvent {

        @Nullable
        private final Screen currentScreen;

        private Screen newScreen;

        @Internal
        public Opening(@Nullable Screen currentScreen, Screen screen) {
            super(screen);
            this.currentScreen = currentScreen;
            this.newScreen = screen;
        }

        @Nullable
        public Screen getCurrentScreen() {
            return this.currentScreen;
        }

        @Nullable
        public Screen getNewScreen() {
            return this.newScreen;
        }

        public void setNewScreen(Screen newScreen) {
            this.newScreen = newScreen;
        }
    }

    public abstract static class Render extends ScreenEvent {

        private final GuiGraphics guiGraphics;

        private final int mouseX;

        private final int mouseY;

        private final float partialTick;

        @Internal
        protected Render(Screen screen, GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            super(screen);
            this.guiGraphics = guiGraphics;
            this.mouseX = mouseX;
            this.mouseY = mouseY;
            this.partialTick = partialTick;
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

        public float getPartialTick() {
            return this.partialTick;
        }

        public static class Post extends ScreenEvent.Render {

            @Internal
            public Post(Screen screen, GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                super(screen, guiGraphics, mouseX, mouseY, partialTick);
            }
        }

        @Cancelable
        public static class Pre extends ScreenEvent.Render {

            @Internal
            public Pre(Screen screen, GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                super(screen, guiGraphics, mouseX, mouseY, partialTick);
            }
        }
    }

    @Cancelable
    public static class RenderInventoryMobEffects extends ScreenEvent {

        private final int availableSpace;

        private boolean compact;

        private int horizontalOffset;

        @Internal
        public RenderInventoryMobEffects(Screen screen, int availableSpace, boolean compact, int horizontalOffset) {
            super(screen);
            this.availableSpace = availableSpace;
            this.compact = compact;
            this.horizontalOffset = horizontalOffset;
        }

        public int getAvailableSpace() {
            return this.availableSpace;
        }

        public boolean isCompact() {
            return this.compact;
        }

        public int getHorizontalOffset() {
            return this.horizontalOffset;
        }

        public void setHorizontalOffset(int offset) {
            this.horizontalOffset = offset;
        }

        public void addHorizontalOffset(int offset) {
            this.horizontalOffset += offset;
        }

        public void setCompact(boolean compact) {
            this.compact = compact;
        }
    }
}