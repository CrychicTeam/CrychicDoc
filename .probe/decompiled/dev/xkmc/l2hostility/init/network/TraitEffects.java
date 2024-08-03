package dev.xkmc.l2hostility.init.network;

import java.util.function.Consumer;
import java.util.function.Supplier;

public enum TraitEffects {

    UNDYING(() -> ClientSyncHandler::triggerUndying), AURA(() -> ClientSyncHandler::triggerAura);

    public final Supplier<Consumer<TraitEffectToClient>> func;

    private TraitEffects(Supplier<Consumer<TraitEffectToClient>> func) {
        this.func = func;
    }
}