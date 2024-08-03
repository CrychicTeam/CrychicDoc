package me.jellysquid.mods.lithium.common.entity.block_tracking;

import java.util.ArrayList;
import me.jellysquid.mods.lithium.common.block.BlockListeningSection;
import me.jellysquid.mods.lithium.common.block.BlockStateFlags;
import me.jellysquid.mods.lithium.common.block.ListeningBlockStatePredicate;

public final class ChunkSectionChangeCallback {

    private final ArrayList<SectionedBlockChangeTracker>[] trackers = new ArrayList[BlockStateFlags.NUM_LISTENING_FLAGS];

    private short listeningMask = 0;

    public short onBlockChange(int flagIndex, BlockListeningSection section) {
        ArrayList<SectionedBlockChangeTracker> sectionedBlockChangeTrackers = this.trackers[flagIndex];
        this.trackers[flagIndex] = null;
        for (int i = 0; i < sectionedBlockChangeTrackers.size(); i++) {
            ((SectionedBlockChangeTracker) sectionedBlockChangeTrackers.get(i)).setChanged(section);
        }
        this.listeningMask = (short) (this.listeningMask & ~(1 << flagIndex));
        return this.listeningMask;
    }

    public short addTracker(SectionedBlockChangeTracker tracker, ListeningBlockStatePredicate blockGroup) {
        int blockGroupIndex = blockGroup.getIndex();
        ArrayList<SectionedBlockChangeTracker> sectionedBlockChangeTrackers = this.trackers[blockGroupIndex];
        if (sectionedBlockChangeTrackers == null) {
            this.trackers[blockGroupIndex] = sectionedBlockChangeTrackers = new ArrayList();
        }
        sectionedBlockChangeTrackers.add(tracker);
        this.listeningMask = (short) (this.listeningMask | 1 << blockGroupIndex);
        return this.listeningMask;
    }

    public short removeTracker(SectionedBlockChangeTracker tracker, ListeningBlockStatePredicate blockGroup) {
        int blockGroupIndex = blockGroup.getIndex();
        ArrayList<SectionedBlockChangeTracker> sectionedBlockChangeTrackers = this.trackers[blockGroupIndex];
        if (sectionedBlockChangeTrackers != null) {
            sectionedBlockChangeTrackers.remove(tracker);
            if (sectionedBlockChangeTrackers.isEmpty()) {
                this.listeningMask = (short) (this.listeningMask & ~(1 << blockGroup.getIndex()));
            }
        }
        return this.listeningMask;
    }
}