package me.steinborn.krypton.mod.shared;

import java.util.Collection;
import net.minecraft.world.entity.Entity;

public interface WorldEntityByChunkAccess {

    Collection<Entity> getEntitiesInChunk(int var1, int var2);
}