package net.minecraftforge.client.event;

import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class ToastAddEvent extends Event {

    private final Toast toast;

    public ToastAddEvent(Toast toast) {
        this.toast = toast;
    }

    public Toast getToast() {
        return this.toast;
    }
}