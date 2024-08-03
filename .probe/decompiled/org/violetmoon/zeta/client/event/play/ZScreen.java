package org.violetmoon.zeta.client.event.play;

import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import org.violetmoon.zeta.event.bus.Cancellable;
import org.violetmoon.zeta.event.bus.IZetaPlayEvent;

public interface ZScreen extends IZetaPlayEvent {

    Screen getScreen();

    public interface CharacterTyped extends ZScreen, Cancellable {

        char getCodePoint();

        int getModifiers();

        public interface Post extends ZScreen.CharacterTyped {
        }

        public interface Pre extends ZScreen.CharacterTyped {
        }
    }

    public interface Init extends ZScreen {

        List<GuiEventListener> getListenersList();

        void addListener(GuiEventListener var1);

        void removeListener(GuiEventListener var1);

        public interface Post extends ZScreen.Init {
        }

        public interface Pre extends ZScreen.Init {
        }
    }

    public interface KeyPressed extends ZScreen, Cancellable {

        int getKeyCode();

        int getScanCode();

        int getModifiers();

        public interface Post extends ZScreen.KeyPressed {
        }

        public interface Pre extends ZScreen.KeyPressed {
        }
    }

    public interface MouseButtonPressed extends ZScreen {

        int getButton();

        double getMouseX();

        double getMouseY();

        public interface Post extends ZScreen.MouseButtonPressed {
        }

        public interface Pre extends ZScreen.MouseButtonPressed, Cancellable {
        }
    }

    public interface MouseScrolled extends ZScreen, Cancellable {

        double getScrollDelta();

        public interface Post extends ZScreen.MouseScrolled {
        }

        public interface Pre extends ZScreen.MouseScrolled {
        }
    }

    public interface Opening extends ZScreen, Cancellable {

        Screen getCurrentScreen();

        Screen getNewScreen();

        void setNewScreen(Screen var1);
    }

    public interface Render extends ZScreen {

        GuiGraphics getGuiGraphics();

        int getMouseX();

        int getMouseY();

        public interface Post extends ZScreen.Render {
        }

        public interface Pre extends ZScreen.Render {
        }
    }
}