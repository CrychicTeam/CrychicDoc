package me.jellysquid.mods.lithium.common.block;

import me.jellysquid.mods.lithium.common.entity.block_tracking.SectionedBlockChangeTracker;

public interface BlockListeningSection {

    void addToCallback(ListeningBlockStatePredicate var1, SectionedBlockChangeTracker var2);

    void removeFromCallback(ListeningBlockStatePredicate var1, SectionedBlockChangeTracker var2);
}