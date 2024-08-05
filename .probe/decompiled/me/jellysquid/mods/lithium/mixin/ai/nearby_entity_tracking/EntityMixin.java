package me.jellysquid.mods.lithium.mixin.ai.nearby_entity_tracking;

import me.jellysquid.mods.lithium.common.entity.nearby_tracker.NearbyEntityListenerMulti;
import me.jellysquid.mods.lithium.common.entity.nearby_tracker.NearbyEntityListenerProvider;
import me.jellysquid.mods.lithium.common.entity.nearby_tracker.NearbyEntityTracker;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ Entity.class })
public class EntityMixin implements NearbyEntityListenerProvider {

    private NearbyEntityListenerMulti tracker = null;

    @Nullable
    @Override
    public NearbyEntityListenerMulti getListener() {
        return this.tracker;
    }

    @Override
    public void addListener(NearbyEntityTracker listener) {
        if (this.tracker == null) {
            this.tracker = new NearbyEntityListenerMulti();
        }
        this.tracker.addListener(listener);
    }
}