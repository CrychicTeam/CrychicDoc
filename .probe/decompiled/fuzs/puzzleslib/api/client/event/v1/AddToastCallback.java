package fuzs.puzzleslib.api.client.event.v1;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;

@FunctionalInterface
public interface AddToastCallback {

    EventInvoker<AddToastCallback> EVENT = EventInvoker.lookup(AddToastCallback.class);

    EventResult onAddToast(ToastComponent var1, Toast var2);
}