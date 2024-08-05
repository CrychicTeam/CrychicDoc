package dev.architectury.event.events.client;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.architectury.event.EventResult;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface ClientScreenInputEvent {

    Event<ClientScreenInputEvent.MouseScrolled> MOUSE_SCROLLED_PRE = EventFactory.createEventResult();

    Event<ClientScreenInputEvent.MouseScrolled> MOUSE_SCROLLED_POST = EventFactory.createEventResult();

    Event<ClientScreenInputEvent.MouseClicked> MOUSE_CLICKED_PRE = EventFactory.createEventResult();

    Event<ClientScreenInputEvent.MouseClicked> MOUSE_CLICKED_POST = EventFactory.createEventResult();

    Event<ClientScreenInputEvent.MouseReleased> MOUSE_RELEASED_PRE = EventFactory.createEventResult();

    Event<ClientScreenInputEvent.MouseReleased> MOUSE_RELEASED_POST = EventFactory.createEventResult();

    Event<ClientScreenInputEvent.MouseDragged> MOUSE_DRAGGED_PRE = EventFactory.createEventResult();

    Event<ClientScreenInputEvent.MouseDragged> MOUSE_DRAGGED_POST = EventFactory.createEventResult();

    Event<ClientScreenInputEvent.KeyTyped> CHAR_TYPED_PRE = EventFactory.createEventResult();

    Event<ClientScreenInputEvent.KeyTyped> CHAR_TYPED_POST = EventFactory.createEventResult();

    Event<ClientScreenInputEvent.KeyPressed> KEY_PRESSED_PRE = EventFactory.createEventResult();

    Event<ClientScreenInputEvent.KeyPressed> KEY_PRESSED_POST = EventFactory.createEventResult();

    Event<ClientScreenInputEvent.KeyReleased> KEY_RELEASED_PRE = EventFactory.createEventResult();

    Event<ClientScreenInputEvent.KeyReleased> KEY_RELEASED_POST = EventFactory.createEventResult();

    public interface KeyPressed {

        EventResult keyPressed(Minecraft var1, Screen var2, int var3, int var4, int var5);
    }

    public interface KeyReleased {

        EventResult keyReleased(Minecraft var1, Screen var2, int var3, int var4, int var5);
    }

    public interface KeyTyped {

        EventResult charTyped(Minecraft var1, Screen var2, char var3, int var4);
    }

    public interface MouseClicked {

        EventResult mouseClicked(Minecraft var1, Screen var2, double var3, double var5, int var7);
    }

    public interface MouseDragged {

        EventResult mouseDragged(Minecraft var1, Screen var2, double var3, double var5, int var7, double var8, double var10);
    }

    public interface MouseReleased {

        EventResult mouseReleased(Minecraft var1, Screen var2, double var3, double var5, int var7);
    }

    public interface MouseScrolled {

        EventResult mouseScrolled(Minecraft var1, Screen var2, double var3, double var5, double var7);
    }
}