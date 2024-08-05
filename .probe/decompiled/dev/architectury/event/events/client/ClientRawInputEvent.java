package dev.architectury.event.events.client;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.architectury.event.EventResult;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface ClientRawInputEvent {

    Event<ClientRawInputEvent.MouseScrolled> MOUSE_SCROLLED = EventFactory.createEventResult();

    Event<ClientRawInputEvent.MouseClicked> MOUSE_CLICKED_PRE = EventFactory.createEventResult();

    Event<ClientRawInputEvent.MouseClicked> MOUSE_CLICKED_POST = EventFactory.createEventResult();

    Event<ClientRawInputEvent.KeyPressed> KEY_PRESSED = EventFactory.createEventResult();

    public interface KeyPressed {

        EventResult keyPressed(Minecraft var1, int var2, int var3, int var4, int var5);
    }

    public interface MouseClicked {

        EventResult mouseClicked(Minecraft var1, int var2, int var3, int var4);
    }

    public interface MouseScrolled {

        EventResult mouseScrolled(Minecraft var1, double var2);
    }
}