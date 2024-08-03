package dev.architectury.extensions.injected;

import net.minecraft.core.Holder;
import net.minecraft.world.level.gameevent.GameEvent;

public interface InjectedGameEventExtension extends InjectedRegistryEntryExtension<GameEvent> {

    @Override
    default Holder<GameEvent> arch$holder() {
        return ((GameEvent) this).builtInRegistryHolder();
    }
}