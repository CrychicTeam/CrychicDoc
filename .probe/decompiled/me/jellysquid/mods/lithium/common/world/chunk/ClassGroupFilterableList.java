package me.jellysquid.mods.lithium.common.world.chunk;

import java.util.Collection;
import me.jellysquid.mods.lithium.common.entity.EntityClassGroup;

public interface ClassGroupFilterableList<T> {

    Collection<T> getAllOfGroupType(EntityClassGroup var1);
}