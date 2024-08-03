package fuzs.puzzleslib.api.event.v1.entity.living;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;

@FunctionalInterface
public interface AnimalTameCallback {

    EventInvoker<AnimalTameCallback> EVENT = EventInvoker.lookup(AnimalTameCallback.class);

    EventResult onAnimalTame(Animal var1, Player var2);
}