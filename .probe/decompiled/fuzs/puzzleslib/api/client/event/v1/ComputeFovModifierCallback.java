package fuzs.puzzleslib.api.client.event.v1;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.data.DefaultedFloat;
import net.minecraft.world.entity.player.Player;

public interface ComputeFovModifierCallback {

    EventInvoker<ComputeFovModifierCallback> EVENT = EventInvoker.lookup(ComputeFovModifierCallback.class);

    void onComputeFovModifier(Player var1, DefaultedFloat var2);
}