package me.jellysquid.mods.lithium.common.world.interests;

import java.util.stream.Stream;

public interface RegionBasedStorageSectionExtended<R> {

    Stream<R> getWithinChunkColumn(int var1, int var2);

    Iterable<R> getInChunkColumn(int var1, int var2);
}