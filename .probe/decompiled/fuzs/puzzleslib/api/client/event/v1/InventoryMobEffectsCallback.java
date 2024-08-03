package fuzs.puzzleslib.api.client.event.v1;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.data.MutableBoolean;
import fuzs.puzzleslib.api.event.v1.data.MutableInt;
import net.minecraft.client.gui.screens.Screen;

@FunctionalInterface
public interface InventoryMobEffectsCallback {

    EventInvoker<InventoryMobEffectsCallback> EVENT = EventInvoker.lookup(InventoryMobEffectsCallback.class);

    EventResult onInventoryMobEffects(Screen var1, int var2, MutableBoolean var3, MutableInt var4);
}