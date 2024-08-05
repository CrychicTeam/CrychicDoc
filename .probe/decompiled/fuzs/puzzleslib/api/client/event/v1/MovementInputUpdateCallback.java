package fuzs.puzzleslib.api.client.event.v1;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;

@FunctionalInterface
public interface MovementInputUpdateCallback {

    EventInvoker<MovementInputUpdateCallback> EVENT = EventInvoker.lookup(MovementInputUpdateCallback.class);

    void onMovementInputUpdate(LocalPlayer var1, Input var2);
}