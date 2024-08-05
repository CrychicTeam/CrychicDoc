package me.jellysquid.mods.lithium.mixin.util.block_tracking.block_listening;

import me.jellysquid.mods.lithium.common.entity.block_tracking.SectionedBlockChangeTracker;
import me.jellysquid.mods.lithium.common.util.deduplication.LithiumInterner;
import me.jellysquid.mods.lithium.common.util.deduplication.LithiumInternerWrapper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ Level.class })
public class WorldMixin implements LithiumInternerWrapper<SectionedBlockChangeTracker> {

    private final LithiumInterner<SectionedBlockChangeTracker> blockChangeTrackers = new LithiumInterner<>();

    public SectionedBlockChangeTracker getCanonical(SectionedBlockChangeTracker value) {
        return this.blockChangeTrackers.getCanonical(value);
    }

    public void deleteCanonical(SectionedBlockChangeTracker value) {
        this.blockChangeTrackers.deleteCanonical(value);
    }
}