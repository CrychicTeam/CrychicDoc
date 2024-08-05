package me.lucko.spark.common.platform.world;

public interface ChunkInfo<E> {

    int getX();

    int getZ();

    CountMap<E> getEntityCounts();

    String entityTypeName(E var1);
}